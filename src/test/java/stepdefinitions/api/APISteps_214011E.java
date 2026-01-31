package stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.APIUtils;

import java.util.HashMap;
import java.util.Map;

public class APISteps_214011E {

    private String authToken;
    private Response response;
    private int plantId;

    @Given("Valid Admin token")
    public void valid_admin_token() {
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

    @Given("Valid Test User token")
    public void valid_test_user_token() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "test123"); 
        
        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);


        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed for Test User");
        
        authToken = loginResponse.jsonPath().getString("token");
        if(authToken == null) {
             authToken = loginResponse.jsonPath().getString("accessToken");
        }
        Assert.assertNotNull(authToken, "Token is null");
    }


    @Then("Receive status code {int}")
    public void receive_status_code(int expectedStatusCode) {
        Assert.assertNotNull(response, "Response is null");
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch");
    }


    @When("Admin create a plant with name {string} price {string} quantity {string} in category {string}")
    public void admin_create_a_plant(String name, String price, String quantity, String categoryId) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", Double.parseDouble(price));
        body.put("quantity", Integer.parseInt(quantity));
        body.put("categoryId", Integer.parseInt(categoryId));
        
        String endpoint = "/api/plants/category/" + categoryId;
        response = APIUtils.post(endpoint, body, authToken);
        
        if (response.getStatusCode() == 201) {
            plantId = response.jsonPath().getInt("id");
        }
    }

    @When("Test User create a plant with name {string} price {string} quantity {string} in category {string}")
    public void testUser_create_a_plant(String name, String price, String quantity, String categoryId) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", Double.parseDouble(price));
        body.put("quantity", Integer.parseInt(quantity));
        body.put("categoryId", Integer.parseInt(categoryId));
        
        String endpoint = "/api/plants/category/" + categoryId;
        response = APIUtils.post(endpoint, body, authToken);
        
        if (response.getStatusCode() == 201) {
            plantId = response.jsonPath().getInt("id");
        }
    }

    @When("Admin updates the plant with name {string} price {string} quantity {string} in category {string}")
    public void admin_updates_the_plant_with_name_price(String name, String price, String quantity, String categoryId) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", Double.parseDouble(price));
        body.put("quantity", Integer.parseInt(quantity));
        body.put("categoryId", Integer.parseInt(categoryId));
        // Assuming partial update or minimum fields needed. 
        // If API requires full object, we might need to store previous values. 
        // But for this test let's try sending just updated fields or minimal set.
        
        String endpoint = "/api/plants/" + plantId;
        response = APIUtils.put(endpoint, body, authToken);
    }

    @When("Test User updates the plant with name {string} price {string} quantity {string} in category {string}")
    public void testUser_updates_the_plant_with_name_price(String name, String price, String quantity, String categoryId) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", Double.parseDouble(price));
        body.put("quantity", Integer.parseInt(quantity));
        body.put("categoryId", Integer.parseInt(categoryId));
        // Assuming partial update or minimum fields needed. 
        // If API requires full object, we might need to store previous values. 
        // But for this test let's try sending just updated fields or minimal set.
        
        String endpoint = "/api/plants/" + plantId;
        response = APIUtils.put(endpoint, body, authToken);
    }

    @When("Admin deletes the plant")
    public void admin_deletes_the_plant() {
        String endpoint = "/api/plants/" + plantId;
        response = APIUtils.delete(endpoint, authToken);
    }

    @When("Test User deletes the plant")
    public void testUser_deletes_the_plant() {
        String endpoint = "/api/plants/" + plantId;
        response = APIUtils.delete(endpoint, authToken);
    }

    @Then("The response should contain the plant name {string}")
    public void the_response_should_contain_the_plant_name(String expectedName) {
        String actualName = response.jsonPath().getString("name");
        Assert.assertEquals(actualName, expectedName, "Plant name mismatch");
    }
}
