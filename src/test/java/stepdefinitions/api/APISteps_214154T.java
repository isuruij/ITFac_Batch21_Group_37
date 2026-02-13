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
    private List<Object> firstPageData;

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

    @Given("User has a valid authorization token")
    public void user_has_a_valid_authorization_token() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "test123");

        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);

        if (loginResponse.getStatusCode() != 200) {
            loginResponse = APIUtils.post("/login", credentials, null);
        }

        Assert.assertEquals(loginResponse.getStatusCode(), 200,
                "User login failed. Status Code: " + loginResponse.getStatusCode());

        authToken = loginResponse.jsonPath().getString("token");
        if (authToken == null) {
            authToken = loginResponse.jsonPath().getString("accessToken");
        }

        Assert.assertNotNull(authToken, "User authorization token is null");
    }

    @When("Admin sends a GET request to {string}")
    public void admin_sends_a_get_request_to(String endpoint) {
        response = APIUtils.get(endpoint, authToken);
    }

    @When("User sends a GET request to {string}")
    public void user_sends_a_get_request_to(String endpoint) {
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
        Assert.assertFalse(quantities.isEmpty(), "Plant list is empty");

        List<Integer> sorted = quantities.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(quantities, sorted, "Plant list is not sorted by quantity in ascending order");
    }

    @Then("Only plants from category ID {int} are returned")
    public void only_plants_from_category_id_are_returned(Integer categoryId) {
        List<Integer> categoryIds = response.jsonPath().getList("content.category.id", Integer.class);

        if (categoryIds == null || categoryIds.isEmpty()) {
            categoryIds = response.jsonPath().getList("category.id", Integer.class);
        }

        Assert.assertNotNull(categoryIds, "No category IDs found in response");

        for (Integer id : categoryIds) {
            Assert.assertEquals(id, categoryId,
                    "Found plant with category ID " + id + " but expected only category ID " + categoryId);
        }
    }

    @Then("The response contains the first page of plant data")
    public void the_response_contains_the_first_page_of_plant_data() {
        firstPageData = response.jsonPath().getList("content");

        if (firstPageData == null || firstPageData.isEmpty()) {
            firstPageData = response.jsonPath().getList("$");
        }

        Assert.assertNotNull(firstPageData, "No plant data found in response");
        Assert.assertFalse(firstPageData.isEmpty(), "First page is empty");
    }

    @Then("The response contains the next set of plant records")
    public void the_response_contains_the_next_set_of_plant_records() {
        List<Object> secondPageData = response.jsonPath().getList("content");

        if (secondPageData == null || secondPageData.isEmpty()) {
            secondPageData = response.jsonPath().getList("$");
        }

        Assert.assertNotNull(secondPageData, "No plant data found in second page response");

        // Verify pagination metadata if available
        Integer pageNumber = response.jsonPath().getInt("pageable.pageNumber");
        if (pageNumber != null) {
            Assert.assertEquals(pageNumber, Integer.valueOf(1), "Expected page number 1");
        }
    }

    @Then("The plant list should be sorted by Price in descending order")
    public void the_plant_list_should_be_sorted_by_price_in_descending_order() {
        List<Double> prices = response.jsonPath().getList("content.price", Double.class);

        if (prices == null || prices.isEmpty()) {
            prices = response.jsonPath().getList("price", Double.class);
        }

        Assert.assertNotNull(prices, "No plant prices found in response");
        Assert.assertFalse(prices.isEmpty(), "Plant list is empty");

        List<Double> sorted = prices.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
        Assert.assertEquals(prices, sorted, "Plant list is not sorted by price in descending order");
    }

    @Then("The plant list should be sorted by Name in ascending order")
    public void the_plant_list_should_be_sorted_by_name_in_ascending_order() {
        List<String> names = response.jsonPath().getList("content.name", String.class);

        if (names == null || names.isEmpty()) {
            names = response.jsonPath().getList("name", String.class);
        }

        Assert.assertNotNull(names, "No plant names found in response");
        Assert.assertFalse(names.isEmpty(), "Plant list is empty");

        List<String> sorted = names.stream().sorted().collect(Collectors.toList());
        Assert.assertEquals(names, sorted, "Plant list is not sorted by name in ascending order");
    }
}
