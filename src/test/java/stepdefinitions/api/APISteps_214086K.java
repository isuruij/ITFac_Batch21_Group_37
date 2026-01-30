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
}
