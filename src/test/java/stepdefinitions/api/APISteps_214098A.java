package stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.APIUtils;

import java.util.HashMap;
import java.util.Map;

public class APISteps_214098A {

    private String authToken;
    private Response response;

    @Given("I have a valid Admin token for Sales")
    public void i_have_a_valid_admin_token_for_sales() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        authToken = loginResponse.jsonPath().getString("token");
    }

    @Given("I have a valid User token for Sales")
    public void i_have_a_valid_user_token_for_sales() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "test123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        authToken = loginResponse.jsonPath().getString("token");
    }

    @When("I send a GET request to fetch the sale with ID {int}")
    public void i_send_a_get_request_to_fetch_the_sale_with_id(Integer id) {
        response = APIUtils.get("/api/sales/" + id, authToken);
    }

    @Then("I verify the sales response status code is {int}")
    public void i_verify_the_sales_response_status_code_is(int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected status code");
    }

    @When("I send a DELETE request to delete the sale with ID {int}")
    public void i_send_a_delete_request_to_delete_the_sale_with_id(Integer id) {
        response = APIUtils.delete("/api/sales/" + id, authToken);
    }

    @When("I send a POST request to create a sale for plant {int} with quantity {int}")
    public void i_send_a_post_request_to_create_a_sale_for_plant_with_quantity(Integer plantId, Integer quantity) {
        String endpoint = "/api/sales/plant/" + plantId + "?quantity=" + quantity;
        response = APIUtils.post(endpoint, null, authToken);
    }

    // Avoid remove this. The following code is M5-API-01 specific and has been
    // commented out for now. It can be uncommented and used when implementing the
    // sales creation test case.

    private Integer categoryIdForSale;
    private Integer plantIdForSale;
    private Integer saleIdForSale;

    @io.cucumber.java.Before("@M5-API-01")
    public void setupForSale() {
    i_have_a_valid_admin_token_for_sales();
    String adminToken = authToken;

    // 1. Create Category
    Map<String, String> catBody = new HashMap<>();
    catBody.put("name", "CatForSale");
    Response catResponse = APIUtils.post("/api/categories", catBody, adminToken);
    if (catResponse.getStatusCode() == 201) {
    categoryIdForSale = catResponse.jsonPath().getInt("id");
    System.out.println("Setup: Created category 'CatForSale' with ID: " +
    categoryIdForSale);
    } else {
    System.out.println("Setup failed: Could not create category 'CatForSale'. Status: " + catResponse.getStatusCode());
    }

    // 2. Create Plant
    if (categoryIdForSale != null) {
    Map<String, Object> plantBody = new HashMap<>();
    plantBody.put("name", "PlantForSale");
    plantBody.put("price", 10.0);
    plantBody.put("quantity", 100);
    plantBody.put("categoryId", categoryIdForSale);

    Response plantResponse = APIUtils.post("/api/plants/category/" +
    categoryIdForSale, plantBody, adminToken);
    if (plantResponse.getStatusCode() == 201) {
    plantIdForSale = plantResponse.jsonPath().getInt("id");
    System.out.println("Setup: Created plant 'PlantForSale' with ID: " +
    plantIdForSale);
    } else {
    System.out.println("Setup failed: Could not create plant. Status: " +
    plantResponse.getStatusCode() + " Response: " +
    plantResponse.getBody().asString());
    throw new RuntimeException("Setup failed: Could not create plant. Status: " +
    plantResponse.getStatusCode());
    }
    }
    }

    @When("I send a POST request to create a sale for the newly created plant with quantity {int}")
    public void i_send_a_post_request_to_create_a_sale_for_the_newly_created_plant_with_quantity(Integer quantity) {
    if (plantIdForSale == null) throw new RuntimeException("Plant ID is null. Setup failed.");

    String endpoint = "/api/sales/plant/" + plantIdForSale + "?quantity=" +
    quantity;
    response = APIUtils.post(endpoint, null, authToken);

    if (response.getStatusCode() == 201) {
    saleIdForSale = response.jsonPath().getInt("id");
    }
    }

    @io.cucumber.java.After("@M5-API-01")
    public void tearDownSale() {
    i_have_a_valid_admin_token_for_sales();

    if (saleIdForSale != null) {
    APIUtils.delete("/api/sales/" + saleIdForSale, authToken);
    }

    if (plantIdForSale != null) {
    APIUtils.delete("/api/plants/" + plantIdForSale, authToken);
    }

    if (categoryIdForSale != null) {
    APIUtils.delete("/api/categories/" + categoryIdForSale, authToken);
    }
    }

}
