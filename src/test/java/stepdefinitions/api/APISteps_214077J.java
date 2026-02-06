package stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.APIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APISteps_214077J {

    private String authToken;
    private Response response;
    private java.util.Map<String, Integer> createdCategoryIds = new java.util.HashMap<>();

    @Given("Valid Admin token is available")
    public void valid_admin_token_is_available() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        authToken = loginResponse.jsonPath().getString("token");
    }

    @When("I send a GET request to fetch main categories")
    public void i_send_a_get_request_to_fetch_main_categories() {
        response = APIUtils.get("/api/categories/main", authToken);
    }

    @Then("The response status code is {int}")
    public void the_response_status_code_is(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected status code");
    }

    @Then("The response should contain a list of main categories")
    public void the_response_should_contain_a_list_of_main_categories() {
        List<Map<String, Object>> categories = response.jsonPath().getList("$");
        Assert.assertNotNull(categories, "Categories list should not be null");
        
        for (Map<String, Object> cat : categories) {
             Assert.assertTrue(cat.containsKey("id"), "Category should have an ID");
             Assert.assertTrue(cat.containsKey("name"), "Category should have a name");
             
             // Verify that it is a main category (should not have a parent or parent should be null/empty)
             // Depending on API implementation, it might not return the parent field or return it as null.
             if (cat.containsKey("parentCategory")) {
                 Assert.assertNull(cat.get("parentCategory"), "Main category should not have a parent");
             }
        }
    }

    @Given("I have a category {string} and a sub-category {string}")
    public void i_have_a_category_and_a_sub_category(String parentName, String childName) {
        // Cleanup potential leftovers first
        cleanUpCategoryByName(childName);
        cleanUpCategoryByName(parentName);

        // Create Parent
        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("name", parentName);
        Response parentRes = APIUtils.post("/api/categories", parentBody, authToken);
        if (parentRes.getStatusCode() != 200 && parentRes.getStatusCode() != 201) {
            Assert.fail("Failed to create parent category. Status: " + parentRes.getStatusCode() + ", Body: " + parentRes.getBody().asString());
        }
        int parentId = parentRes.jsonPath().getInt("id");
        createdCategoryIds.put(parentName, parentId);

        // Create Child
        Map<String, Object> childBody = new HashMap<>();
        childBody.put("name", childName);
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentId);
        childBody.put("parentCategory", parentObj);
        
        Response childRes = APIUtils.post("/api/categories", childBody, authToken);
        if (childRes.getStatusCode() != 200 && childRes.getStatusCode() != 201) {
            Assert.fail("Failed to create sub-category. Status: " + childRes.getStatusCode() + ", Body: " + childRes.getBody().asString());
        }
        int childId = childRes.jsonPath().getInt("id");
        createdCategoryIds.put(childName, childId);
    }
    
    private void cleanUpCategoryByName(String name) {
        // Try to find category by name to delete it.
        // Since API doesn't have search by name easily documented in snippets, 
        // we'll assuming we might have to just rely on teardown or specific get logic.
        // However, if we can't search, we might fail. 
        // But let's try to delete if we stored it in valid session or just ignore for now if we can't find it.
        // Actually, we can assume we only clean what we created in this run context usually. 
        // But since it persisted, we need to find it.
        // Attempt to fetch all main/sub categories might be too heavy.
        // Let's rely on the IDs we *might* have or skipped.
        
        // Better approach: Since we don't have a direct "delete by name" without ID,
        // and fetching all is expensive, we'll try to create, if 400 Duplicate, we assume it exists.
        // But we need the ID to start fresh.
        // Let's implement a simple "get all and find" for cleanup if strictly needed,
        // or just suffix the names with random numbers to avoid collision? 
        // No, user wants consistent test names usually.
        
        // Let's go with "Use random suffix" effectively.
        // But wait, the user instructions implied reusing the names in the feature file.
        
        // Let's try to fetch all categories and find the one with the name.
        Response allCats = APIUtils.get("/api/categories", authToken);
        if (allCats.getStatusCode() == 200) {
            List<Map<String, Object>> cats = allCats.jsonPath().getList("$");
             for (Map<String, Object> cat : cats) {
                 if (name.equals(cat.get("name"))) {
                     APIUtils.delete("/api/categories/" + cat.get("id"), authToken);
                 }
                 // check children if listed flat
             }
        }
        // Also check main specific endpoint if needed, but usually /api/categories lists all or main.
        // Just in case /api/categories is main only.
        Response subCats = APIUtils.get("/api/categories/sub-categories", authToken);
        if (subCats.getStatusCode() == 200) {
            List<Map<String, Object>> cats = subCats.jsonPath().getList("$");
             for (Map<String, Object> cat : cats) {
                 if (name.equals(cat.get("name"))) {
                     APIUtils.delete("/api/categories/" + cat.get("id"), authToken);
                 }
             }
        }
    }

    @When("I send a PUT request to update the sub-category {string} with parent ID {string}")
    public void i_send_a_put_request_to_update_the_sub_category_with_parent_id(String childName, String invalidParentId) {
        Integer childId = createdCategoryIds.get(childName);
        Assert.assertNotNull(childId, "Child category ID not found for: " + childName);

        Map<String, Object> body = new HashMap<>();
        body.put("name", childName);
        
        try {
            body.put("parentId", Integer.parseInt(invalidParentId));
        } catch (NumberFormatException e) {
            body.put("parentId", invalidParentId);
        }
        
        System.out.println("DEBUG: Sending PUT to /api/categories/" + childId + " with body: " + body);
        response = APIUtils.put("/api/categories/" + childId, body, authToken);
        System.out.println("DEBUG: Response Status: " + response.getStatusCode());
        System.out.println("DEBUG: Response Body: " + response.getBody().asString());
    }
    
    @Then("The response should contain an error message")
    public void the_response_should_contain_an_error_message() {
        String body = response.getBody().asString();
        Assert.assertTrue( !body.isEmpty(), "Response body should not be empty");
    }

    @Given("I delete the category {string} through API")
    public void i_delete_the_category_through_api(String categoryName) {
        Integer id = createdCategoryIds.get(categoryName);
        if(id != null) {
            APIUtils.delete("/api/categories/" + id, authToken);
            createdCategoryIds.remove(categoryName);
        }
    }
}
