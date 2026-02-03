package stepdefinitions.api;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.APIUtils;

import java.util.HashMap;
import java.util.Map;

public class APISteps_214077J {

    private String authToken;
    private Response response;
    private int subCategoryId;
    private int parentCategoryId;
    private Map<String, Object> requestBody;

    @Given("Admin user is authenticated and has a valid JWT bearer token")
    public void admin_user_is_authenticated_and_has_a_valid_jwt_bearer_token() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);

        if (loginResponse.getStatusCode() != 200) {
            loginResponse = APIUtils.post("/login", credentials, null);
        }

        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        
        authToken = loginResponse.jsonPath().getString("token");
        if(authToken == null) {
             authToken = loginResponse.jsonPath().getString("accessToken");
        }
        Assert.assertNotNull(authToken, "Token is null");
    }

    @Given("A sub-category already exists in the system")
    public void a_sub_category_already_exists_in_the_system() {
        // 1. Create Parent Category
        Map<String, Object> parentCategory = new HashMap<>();
        String parentName = "Parent_" + System.currentTimeMillis();
        parentCategory.put("name", parentName);
        
        Response parentResponse = APIUtils.post("/api/categories", parentCategory, authToken);
        Assert.assertTrue(parentResponse.getStatusCode() == 200 || parentResponse.getStatusCode() == 201, "Failed to create parent category");
        parentCategoryId = parentResponse.jsonPath().getInt("id");

        // 2. Create Sub Category
        Map<String, Object> subCategory = new HashMap<>();
        String subName = "Sub_" + System.currentTimeMillis();
        subCategory.put("name", subName);
        subCategory.put("parent_id", parentCategoryId); // Trying snake_case first
        
        Response subResponse = APIUtils.post("/api/categories", subCategory, authToken);
        
        // If creation failed, maybe it expects camelCase
        if(subResponse.getStatusCode() >= 400) {
             subCategory.remove("parent_id");
             subCategory.put("parentId", parentCategoryId);
             subResponse = APIUtils.post("/api/categories", subCategory, authToken);
        }

        Assert.assertTrue(subResponse.getStatusCode() == 200 || subResponse.getStatusCode() == 201, "Failed to create sub category");
        subCategoryId = subResponse.jsonPath().getInt("id");
    }

    @When("I prepare a PUT request to update the sub-category with a non-existing parent category ID")
    public void i_prepare_a_put_request_to_update_the_sub_category_with_a_non_existing_parent_category_id() {
        requestBody = new HashMap<>();
        requestBody.put("name", "Updated_Sub_" + System.currentTimeMillis());
        // Use a non-existing ID. 
        requestBody.put("parent_id", 9999999); 
        requestBody.put("parentId", 9999999);
    }

    @When("I send the request with the Admin authorization bearer token")
    public void i_send_the_request_with_the_admin_authorization_bearer_token() {
        response = APIUtils.put("/api/categories/" + subCategoryId, requestBody, authToken);
    }

    @Then("The API rejects the request with status code 400 or 404")
    public void the_api_rejects_the_request_with_status_code_400_or_404() {
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == 400 || statusCode == 404, "Expected 400 or 404 but got " + statusCode);
    }

    @Then("The response body contains a meaningful error message")
    public void the_response_body_contains_a_meaningful_error_message() {
        String body = response.getBody().asString();
        Assert.assertTrue(body != null && !body.isEmpty(), "Response body is empty");
    }

    @After("@M4-API-01")
    public void tearDown() {
        if (subCategoryId != 0) {
            APIUtils.delete("/api/categories/" + subCategoryId, authToken);
        }
        if (parentCategoryId != 0) {
            APIUtils.delete("/api/categories/" + parentCategoryId, authToken);
        }
    }
}
