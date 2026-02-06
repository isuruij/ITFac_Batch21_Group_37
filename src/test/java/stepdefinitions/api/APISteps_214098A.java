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

    @When("I send a GET request to fetch the sale with ID {int}")
    public void i_send_a_get_request_to_fetch_the_sale_with_id(Integer id) {
        response = APIUtils.get("/api/sales/" + id, authToken);
    }

    @Then("I verify the sales response status code is {int}")
    public void i_verify_the_sales_response_status_code_is(int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Unexpected status code");
    }
}
