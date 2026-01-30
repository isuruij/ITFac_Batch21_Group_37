package stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.APIUtils;

import java.util.HashMap;
import java.util.Map;

public class APISteps_214086K {

    private String authToken;
    private Response response;
    private String createdCategoryName;

    @Given("Valid Admin token available")
    public void valid_admin_token_available() {
        loginAsAdmin();
    }

    private void loginAsAdmin() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        authToken = loginResponse.jsonPath().getString("token");
    }

    @When("I send a POST request to create a category with name {string}")
    public void i_send_a_post_request_to_create_a_category_with_name(String categoryName) {
        createdCategoryName = categoryName;
        Map<String, String> body = new HashMap<>();
        body.put("name", createdCategoryName);

        response = APIUtils.post("/api/categories", body, authToken);
    }

    @Then("The response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");
    }

    @After("@M2-API-01")
    public void tearDown() {
        if (response != null && response.getStatusCode() == 201) {
            Integer categoryId = response.jsonPath().getInt("id");
            if (categoryId != null) {
                APIUtils.delete("/api/categories/" + categoryId, authToken);
                System.out.println("API Cleanup: Deleted category with ID: " + categoryId);
            }
        }
    }

    private Integer categoryIdForEdit;

    @io.cucumber.java.Before("@M2-API-02")
    public void setupForEdit() {
        // Login to get token for setup
        loginAsAdmin();

        // Create "office" category
        Map<String, String> body = new HashMap<>();
        body.put("name", "office");
        Response createResponse = APIUtils.post("/api/categories", body, authToken);
        if (createResponse.getStatusCode() == 201) {
            categoryIdForEdit = createResponse.jsonPath().getInt("id");
            System.out.println("Setup: Created category 'office' with ID: " + categoryIdForEdit);
        } else {
            System.out.println("Setup failed: Could not create 'office' category.");
        }
    }

    @When("I send a PUT request to update the category to {string}")
    public void i_send_a_put_request_to_update_the_category_to(String newName) {
        if (categoryIdForEdit != null) {
            Map<String, String> body = new HashMap<>();
            body.put("name", newName);
            response = APIUtils.put("/api/categories/" + categoryIdForEdit, body, authToken);
        } else {
            throw new RuntimeException("Category ID for edit is null. Setup might have failed.");
        }
    }

    @After("@M2-API-02")
    public void tearDownEdit() {
        if (categoryIdForEdit != null) {
            APIUtils.delete("/api/categories/" + categoryIdForEdit, authToken);
            System.out.println("Cleanup: Deleted category with ID: " + categoryIdForEdit);
        }
    }

    private Integer categoryIdForDelete;

    @io.cucumber.java.Before("@M2-API-03")
    public void setupForDelete() {
        // Login to get token for setup
        loginAsAdmin();

        // Create "Herbs" category
        Map<String, String> body = new HashMap<>();
        body.put("name", "Herbs");
        Response createResponse = APIUtils.post("/api/categories", body, authToken);
        if (createResponse.getStatusCode() == 201) {
            categoryIdForDelete = createResponse.jsonPath().getInt("id");
            System.out.println("Setup: Created category 'Herbs' with ID: " + categoryIdForDelete);
        } else {
            System.out.println("Setup failed: Could not create 'Herbs' category.");
        }
    }

    @When("I send a DELETE request to delete the category {string}")
    public void i_send_a_delete_request_to_delete_the_category(String categoryName) {
        Integer idToDelete = null;
        if (categoryIdForDelete != null) {
            idToDelete = categoryIdForDelete;
        } else if (categoryIdForDeleteByUser != null) {
            idToDelete = categoryIdForDeleteByUser;
        }

        if (idToDelete != null) {
            response = APIUtils.delete("/api/categories/" + idToDelete, authToken);
        } else {
            throw new RuntimeException("Category ID for delete is null. Setup might have failed.");
        }
    }

    @When("I send a POST request to create a category with missing name")
    public void i_send_a_post_request_to_create_a_category_with_missing_name() {
        Map<String, String> body = new HashMap<>();
        body.put("name", ""); // Emulating missing/empty name
        response = APIUtils.post("/api/categories", body, authToken);
    }

    @Given("Valid User token available")
    public void valid_user_token_available() {
        loginAsUser();
    }

    private void loginAsUser() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "test123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        authToken = loginResponse.jsonPath().getString("token");
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = APIUtils.get(endpoint, authToken);
    }

    private Integer categoryIdForGet;

    @io.cucumber.java.Before("@M2-API-07")
    public void setupForGet() {

        loginAsAdmin();
        String adminToken = authToken;

        // Create "apicat" category
        Map<String, String> body = new HashMap<>();
        body.put("name", "apicat");
        Response createResponse = APIUtils.post("/api/categories", body, adminToken);
        if (createResponse.getStatusCode() == 201) {
            categoryIdForGet = createResponse.jsonPath().getInt("id");
            System.out.println("Setup: Created category 'apicat' with ID: " + categoryIdForGet);
        } else {
            System.out.println("Setup failed: Could not create 'apicat' category.");
        }
    }

    @When("I send a GET request to fetch category with name {string}")
    public void i_send_a_get_request_to_fetch_category_with_name(String categoryName) {
        if (categoryIdForGet != null) {
            response = APIUtils.get("/api/categories/" + categoryIdForGet, authToken);
        } else {
            throw new RuntimeException("Category ID for GET is null. Setup might have failed.");
        }
    }

    @After("@M2-API-07")
    public void tearDownGet() {
        if (categoryIdForGet != null) {
            loginAsAdmin();
            String adminToken = authToken;

            APIUtils.delete("/api/categories/" + categoryIdForGet, adminToken);
            System.out.println("Cleanup: Deleted category with ID: " + categoryIdForGet);
        }
    }

    private Integer categoryIdForUpdateByUser;

    @io.cucumber.java.Before("@M2-API-09")
    public void setupForUpdateByUser() {
        loginAsAdmin();
        String adminToken = authToken;

        // Create "apicat" category
        Map<String, String> body = new HashMap<>();
        body.put("name", "apicat");
        Response createResponse = APIUtils.post("/api/categories", body, adminToken);
        if (createResponse.getStatusCode() == 201) {
            categoryIdForUpdateByUser = createResponse.jsonPath().getInt("id");
            System.out.println("Setup: Created category 'apicat' with ID: " + categoryIdForUpdateByUser);
        } else {
            System.out.println("Setup failed: Could not create 'apicat' category.");
        }
    }

    @When("I send a PUT request to update the category {string}")
    public void i_send_a_put_request_to_update_the_category(String categoryName) {
        if (categoryIdForUpdateByUser != null) {
            Map<String, String> body = new HashMap<>();
            body.put("name", categoryName + "_Updated"); // Just changing the name payload

            // Using authToken which should be User token from Given step
            response = APIUtils.put("/api/categories/" + categoryIdForUpdateByUser, body, authToken);
        } else {
            throw new RuntimeException("Category ID for Update is null. Setup might have failed.");
        }
    }

    @After("@M2-API-09")
    public void tearDownUpdateByUser() {
        if (categoryIdForUpdateByUser != null) {
            loginAsAdmin();
            String adminToken = authToken;

            APIUtils.delete("/api/categories/" + categoryIdForUpdateByUser, adminToken);
            System.out.println("Cleanup: Deleted category with ID: " + categoryIdForUpdateByUser);
        }
    }

    private Integer categoryIdForDeleteByUser;

    @io.cucumber.java.Before("@M2-API-10")
    public void setupForDeleteByUser() {
        loginAsAdmin();
        String adminToken = authToken;

        // Create "apicat" category
        Map<String, String> body = new HashMap<>();
        body.put("name", "apicat");
        Response createResponse = APIUtils.post("/api/categories", body, adminToken);
        if (createResponse.getStatusCode() == 201) {
            categoryIdForDeleteByUser = createResponse.jsonPath().getInt("id");
            System.out.println("Setup: Created category 'apicat' with ID: " + categoryIdForDeleteByUser);
        } else {
            System.out.println("Setup failed: Could not create 'apicat' category.");
        }
    }

    @After("@M2-API-10")
    public void tearDownDeleteByUser() {
        if (categoryIdForDeleteByUser != null) {
            loginAsAdmin();
            String adminToken = authToken;

            // Test user should fail to delete, so it should still exist. We must cleanup.
            APIUtils.delete("/api/categories/" + categoryIdForDeleteByUser, adminToken);
            System.out.println("Cleanup: Deleted category with ID: " + categoryIdForDeleteByUser);
        }
    }
}
