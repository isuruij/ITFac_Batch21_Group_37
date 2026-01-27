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
import java.util.stream.Collectors;

public class APISteps_214154T {

    private String authToken;
    private Response response;

    @Given("Admin has a valid authorization token")
    public void admin_has_a_valid_authorization_token() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);

        if (loginResponse.getStatusCode() != 200) {
            loginResponse = APIUtils.post("/login", credentials, null);
        }

        if (loginResponse.getStatusCode() != 200) {
            System.out.println("Login Failed. Status: " + loginResponse.getStatusCode());
            System.out.println("Response Body: " + loginResponse.getBody().asString());
        }

        Assert.assertEquals(loginResponse.getStatusCode(), 200,
                "Login failed. Status Code: " + loginResponse.getStatusCode());

        authToken = loginResponse.jsonPath().getString("token");
        if (authToken == null) {
            authToken = loginResponse.jsonPath().getString("accessToken");
        }

        Assert.assertNotNull(authToken, "Authorization token is null");
    }

    @When("Admin sends a GET request to {string}")
    public void admin_sends_a_get_request_to(String endpoint) {
        response = APIUtils.get(endpoint, authToken);
    }

    @Then("The API should return a status code of {int}")
    public void the_api_should_return_a_status_code_of(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, "Unexpected Status Code");
    }

    @Then("The plant list should be sorted by Quantity in ascending order")
    public void the_plant_list_should_be_sorted_by_quantity_in_ascending_order() {
        List<Integer> quantities = response.jsonPath().getList("content.quantity", Integer.class);

        if (quantities == null || quantities.isEmpty()) {
            quantities = response.jsonPath().getList("quantity", Integer.class);
        }

        Assert.assertNotNull(quantities, "No plant quantities found in response");

        List<Integer> sorted = quantities.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(quantities, sorted, "Plant list is not sorted by quantity");
    }
}
