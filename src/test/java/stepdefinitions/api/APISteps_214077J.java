package stepdefinitions.api;

import io.cucumber.java.Before;
import io.cucumber.java.After;
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

    @Before("@M4-API-01")
    public void setupForUpdateParentTest() {
        // Ensure strictly fresh state
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");
        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        authToken = loginResponse.jsonPath().getString("token");

        String parentName = "ParAPI01";
        String childName = "ChiAPI01";
        
        // Cleanup potential leftovers
        cleanUpCategoryByName(childName);
        cleanUpCategoryByName(parentName);

        // Create Parent
        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("name", parentName);
        Response parentRes = APIUtils.post("/api/categories", parentBody, authToken);
        int parentId = parentRes.jsonPath().getInt("id");
        createdCategoryIds.put(parentName, parentId);

        // Create Child
        Map<String, Object> childBody = new HashMap<>();
        childBody.put("name", childName);
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentId);
        childBody.put("parentCategory", parentObj);
        
        Response childRes = APIUtils.post("/api/categories", childBody, authToken);
        int childId = childRes.jsonPath().getInt("id");
        createdCategoryIds.put(childName, childId);
    }
    
    @After("@M4-API-01")
    public void tearDownUpdateParentTest() {
        if (authToken != null) {
            cleanUpCategoryByName("ChiAPI01");
            cleanUpCategoryByName("ParAPI01");
        }
    }

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
        // Let's try to fetch all categories and find the one with the name.
        Response allCats = APIUtils.get("/api/categories", authToken);
        if (allCats.getStatusCode() == 200) {
            List<Map<String, Object>> cats = allCats.jsonPath().getList("$");
             for (Map<String, Object> cat : cats) {
                 if (name.equals(cat.get("name"))) {
                     APIUtils.delete("/api/categories/" + cat.get("id"), authToken);
                 }
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

    @When("I send a POST request to create a category with name {string} without parent ID")
    public void i_send_a_post_request_to_create_a_category_with_name_without_parent_id(String categoryName) {
        Map<String, String> body = new HashMap<>();
        body.put("name", categoryName);
        response = APIUtils.post("/api/categories", body, authToken);
        if (response.getStatusCode() == 201) {
             Integer id = response.jsonPath().getInt("id");
             if(id != null) {
                createdCategoryIds.put(categoryName, id);
             }
        }
    }

    @After("@M4-API-02")
    public void tearDownM4API02() {
        if (authToken != null) {
            cleanUpCategoryByName("M4MainCat");
             cleanUpCategoryByName("NoParCat");
        }
    }

    @Given("A category with two-part name {string} exists")
    @Given("A category with name {string} exists")
    public void a_category_with_two_part_name_exists(String name) {
        cleanUpCategoryByName(name);
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        Response res = APIUtils.post("/api/categories", body, authToken);
        if (res.getStatusCode() == 201 || res.getStatusCode() == 200) {
            createdCategoryIds.put(name, res.jsonPath().getInt("id"));
        } else {
             System.out.println("DEBUG: Failed to create category " + name + ". Body: " + res.getBody().asString());
             Assert.fail("Failed to create setup category: " + name + ". Status: " + res.getStatusCode());
        }
    }

    @When("I search for the category {string}")
    public void i_search_for_the_category(String query) {
        // Using 'name' or 'search' parameter based on common API patterns for this project or generic assumption
        String encodedQuery = query.replace(" ", "%20");
        response = APIUtils.get("/api/categories?search=" + encodedQuery, authToken);
        if (response.getStatusCode() != 200) {
             // Fallback: try 'name' param if 'search' fails or returns all?
             // But actually, usually search APIs return 200 even if empty.
        }
    }

    @Then("The response should contain the category {string}")
    public void the_response_should_contain_the_category(String categoryName) {
        // Response is expected to be a list of categories
        List<Map<String, Object>> categories = response.jsonPath().getList("$");
        Assert.assertNotNull(categories, "Search result should not be null");
        
        boolean found = false;
        for (Map<String, Object> cat : categories) {
            if (categoryName.equals(cat.get("name"))) {
                found = true; 
                break;
            }
        }
        Assert.assertTrue(found, "Category '" + categoryName + "' not found in search results.");
    }

    @After("@M4-API-03")
    public void tearDownTwoPartTest() {
        if (authToken != null) {
            cleanUpCategoryByName("Two Part");
        }
    }

    @When("I send a POST request to create the sub-category {string} under {string} again")
    public void i_send_a_post_request_to_create_the_sub_category_under_again(String childName, String parentName) {
        Integer parentId = createdCategoryIds.get(parentName);
        Assert.assertNotNull(parentId, "Parent category ID not found in created map for: " + parentName);

        Map<String, Object> body = new HashMap<>();
        body.put("name", childName);
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentId);
        body.put("parentCategory", parentObj);

        // Attempting to create duplicate
        response = APIUtils.post("/api/categories", body, authToken);
    }

    @Then("The response status code is 400 or 409")
    public void the_response_status_code_is_400_or_409() {
        int sc = response.getStatusCode();
        Assert.assertTrue(sc == 400 || sc == 409, "Expected 400 or 409 but got " + sc);
    }

    @When("I send a GET request to fetch a category with non-existing ID {string}")
    public void i_send_a_get_request_to_fetch_a_category_with_non_existing_id(String id) {
        response = APIUtils.get("/api/categories/" + id, authToken);
    }

    @When("I send a GET request to fetch a category with invalid format ID {string}")
    public void i_send_a_get_request_to_fetch_a_category_with_invalid_format_id(String id) {
        response = APIUtils.get("/api/categories/" + id, authToken);
    }

    @Given("Valid Test User token is available")
    public void valid_test_user_token_is_available() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "test123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed for testuser");
        authToken = loginResponse.jsonPath().getString("token");
    }

    @When("I search for categories with name {string} page {int} and size {int}")
    public void i_search_for_categories_with_name_page_and_size(String name, int page, int size) {
        // Construct query using 'name' as requested, but also 'search' if 'name' is not standard
        // M4-API-08 specifically requests 'name' parameter. 
        // If the API implementation is strictly following M4-API-03 which used 'search', 'name' might fail.
        // However, I will stick to the requirement "name=".
        String queryString = "?name=" + name + "&page=" + page + "&size=" + size;
        
        System.out.println("DEBUG: Sending GET to /api/categories" + queryString);
        response = APIUtils.get("/api/categories" + queryString, authToken);
        System.out.println("DEBUG: search response status: " + response.getStatusCode());
        System.out.println("DEBUG: search response body: " + response.getBody().asString());
    }

    @Then("The response should contain categories {string} and {string}")
    public void the_response_should_contain_categories_and(String cat1, String cat2) {
        String responseBody = response.getBody().asString();
        List<Map<String, Object>> content = null;
        
        try {
             // Try standard Spring Page "content" field
             content = response.jsonPath().getList("content");
        } catch (Exception e) {
            // ignore
        }
        
        if (content == null) {
             try {
                // Try parsing the root body as a list directly using path "" (root)
                content = response.jsonPath().getList("");
             } catch (Exception e) {
                 System.out.println("DEBUG: Failed to parse response with jsonPath(\"\"): " + e.getMessage());
             }
        }

        boolean found1 = false;
        boolean found2 = false;

        if (content != null && !content.isEmpty() && content.get(0) != null) {
            for (Object item : content) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> c = (Map<String, Object>) item;
                    Object n = c.get("name");
                    if (n != null) {
                        if (n.equals(cat1)) found1 = true;
                        if (n.equals(cat2)) found2 = true;
                    }
                }
            }
        } else {
             // Fallback: If parsing failed to produce expected maps, check the raw string
             // This safeguards against RestAssured Type Ref issues on some environments
             System.out.println("DEBUG: Parsing failed or returned nulls. Fallback to string check.");
             if (responseBody.contains("\"name\":\"" + cat1 + "\"")) found1 = true;
             if (responseBody.contains("\"name\":\"" + cat2 + "\"")) found2 = true;
        }
        
        Assert.assertTrue(found1, "Category " + cat1 + " not found in response.");
        Assert.assertTrue(found2, "Category " + cat2 + " not found in response.");
    }

    @After("@M4-API-08")
    public void tearDownPaginationTest() {
        // Need admin token to delete?
        // If authToken is currently testuser, I might need to login as admin again or rely on the helper which might fail 
        // if cleanup requires admin rights (likely).
        // Best practice: Re-login as admin for teardown.
        
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");
        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        if(loginResponse.getStatusCode() == 200) {
            authToken = loginResponse.jsonPath().getString("token");
            cleanUpCategoryByName("PageCatOne");
            cleanUpCategoryByName("PageCatTwo");
        }
    }



    @After("@M4-API-05")
    public void tearDownDuplicateTest() {
        if (authToken != null) {
            cleanUpCategoryByName("ChiDup");
            cleanUpCategoryByName("ParDup");
        }
    }

    @After("@M4-API-06")
    public void tearDownCaseInsensitiveTest() {
        if (authToken != null) {
            cleanUpCategoryByName("MixCase");
        }
    }

    @When("I send a GET request to {string} with page {int}")
    public void i_send_a_get_request_to_with_page(String endpoint, int page) {
        // endpoint is literal "/api/categories/page" string from feature file
        // We append query param
        response = APIUtils.get(endpoint + "?page=" + page + "&size=10", authToken);
    }

    @Then("The response status code is 400 or returns last page")
    public void the_response_status_code_is_400_or_returns_last_page() {
        int sc = response.getStatusCode();
        if (sc == 400) {
            // Pass
            return;
        } else if (sc == 200) {
            // Verify if it is valid behavior (e.g. empty content or last page)
            // We'll defer detailed check to the next step, but here we confirm 200 is acceptable
        } else {
            Assert.fail("Expected status 400 or 200, but got " + sc);
        }
    }

    @Then("The pagination fields should be consistent")
    public void the_pagination_fields_should_be_consistent() {
        if (response.getStatusCode() == 400) return; // No pagination check needed if error

        // If 200, check structure
        Map<String, Object> body = null;
        try {
            body = response.jsonPath().getMap("$");
        } catch (Exception e) {
            Assert.fail("Response body is not a JSON object (Page object expected). Body: " + response.getBody().asString());
        }
        
        if (body == null) Assert.fail("Body is null");

        // Use safe get
        Object lastObj = body.get("last");
        Object emptyObj = body.get("empty");
        Object numberObj = body.get("number");
        Object totalPagesObj = body.get("totalPages");
        
        // Logic:
        // If we requested page 999 and got 200...
        // 1. It might be empty content.
        // 2. It might be the actual last page (some APIs clamp page number).
        
        List<?> content = response.jsonPath().getList("content");
        
        if (content == null || content.isEmpty()) {
             // Should be marked as empty
             if (emptyObj instanceof Boolean) Assert.assertTrue((Boolean)emptyObj, "'empty' field should be true");
        } else {
             // If content is present, it implies page clamping or we have > 999 pages?
             // Since we created very few categories, we definitely don't have 999 pages.
             // So if content is not empty, it MUST be the last page (clamping).
             if (lastObj instanceof Boolean) Assert.assertTrue((Boolean)lastObj, "Should be the last page if content is returned");
        }
    }
    
    @After("@M4-API-09")
    public void tearDownExceedPageTest() {
        if (authToken != null) {
            cleanUpCategoryByName("PagExc");
        }
    }
}
