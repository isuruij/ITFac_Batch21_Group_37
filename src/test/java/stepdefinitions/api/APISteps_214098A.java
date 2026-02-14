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

    @When("I send a GET request to fetch all sales")
    public void i_send_a_get_request_to_fetch_all_sales() {
        response = APIUtils.get("/api/sales", authToken);
    }

    @When("I send a GET request to fetch sales with page {int} size {int} and sort {string}")
    public void i_send_a_get_request_to_fetch_sales_with_page_size_and_sort(Integer page, Integer size, String sort) {
        String endpoint = "/api/sales/page?page=" + page + "&size=" + size + "&sort=" + sort;
        response = APIUtils.get(endpoint, authToken);
    }

}
