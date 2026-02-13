package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.AddPlantPage;
import pages.LoginPage;
import pages.PlantsPage;
import utils.ConfigReader;
import utils.DriverFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UISteps_214154T {

    LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
    PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
    AddPlantPage addPlantPage = new AddPlantPage(DriverFactory.getDriver());

    private List<String> paginationTestPlants = new ArrayList<>();

    @Given("Admin is logged into the system")
    public void admin_is_logged_into_the_system() {
        DriverFactory.getDriver().get(ConfigReader.getProperty("url") + "/ui/login");
        loginPage.login("admin", "admin123");
    }

    @Given("User is logged into the system")
    public void user_is_logged_into_the_system() {
        DriverFactory.getDriver().get(ConfigReader.getProperty("url") + "/ui/login");
        loginPage.login("testuser", "test123");
    }

    @When("Navigate to Plants tab using the side bar")
    public void navigate_to_plants_tab_using_the_side_bar() {
        plantsPage.clickPlantsTab();
    }

    @When("Enter plant name {string} in the search input box")
    public void enter_plant_name_in_the_search_input_box(String plantName) {
        plantsPage.enterPlantName(plantName);
    }

    @When("Click Search button")
    public void click_search_button() {
        plantsPage.clickSearch();
    }

    @Then("Show the plant list filtered by entered name {string}")
    public void show_the_plant_list_filtered_by_entered_name(String plantName) {
        Assert.assertTrue(plantsPage.isPlantInList(plantName),
                "Plant " + plantName + " not found in the search results!");
    }

    @When("Select the category {string} from the category selector")
    public void select_the_category_from_the_category_selector(String categoryName) {
        plantsPage.selectPlantCategory(categoryName);
    }

    @Then("Show the plant list filtered by the selected category {string}")
    public void show_the_plant_list_filtered_by_the_selected_category(String categoryName) {
        List<String> displayedCategories = plantsPage.getUniqueCategoriesFromTable();
        Assert.assertFalse(displayedCategories.isEmpty(),
                "No plants found in the filtered results!");
        for (String category : displayedCategories) {
            Assert.assertEquals(category, categoryName,
                    "Found plant with category '" + category + "' but expected only '" + categoryName + "'");
        }
    }

    @When("Click Quantity column header")
    public void click_quantity_column_header() {
        plantsPage.clickQuantityColumnHeader();
    }

    @Then("The plants list should be sorted by quantity")
    public void the_plants_list_should_be_sorted_by_quantity() {
        List<Integer> quantities = plantsPage.getPlantQuantitiesFromTable();
        Assert.assertFalse(quantities.isEmpty(), "No plants found in the table!");

        // Check if sorted (either ascending or descending)
        boolean isAscending = true;
        boolean isDescending = true;

        for (int i = 0; i < quantities.size() - 1; i++) {
            if (quantities.get(i) > quantities.get(i + 1)) {
                isAscending = false;
            }
            if (quantities.get(i) < quantities.get(i + 1)) {
                isDescending = false;
            }
        }

        Assert.assertTrue(isAscending || isDescending,
                "Plants list is not sorted by quantity. Quantities: " + quantities);
    }

    @Then("{string} message is displayed")
    public void message_is_displayed(String message) {
        Assert.assertTrue(plantsPage.isNoResultsMessageDisplayed(),
                "Expected message '" + message + "' is not displayed!");
    }

    @When("Create {int} test plants for pagination")
    public void create_test_plants_for_pagination(Integer count) {
        for (int i = 1; i <= count; i++) {
            String plantName = "PaginationPlant" + i + "_214154T";
            paginationTestPlants.add(plantName);

            plantsPage.clickAddPlant();
            addPlantPage.enterPlantName(plantName);
            addPlantPage.selectCategory("sri lankan");
            addPlantPage.enterPrice(String.valueOf(1000 + i * 100));
            addPlantPage.enterQuantity(String.valueOf(10 + i));
            addPlantPage.clickSave();

            // Wait for save to complete
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        // Navigate back to plants page to see all plants
        plantsPage.clickPlantsTab();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @When("I click the Next button")
    public void i_click_the_next_button() {
        plantsPage.clickNextPage();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @Then("The relevant page of plants is displayed in the table")
    public void the_relevant_page_of_plants_is_displayed_in_the_table() {
        // Verify that plants are displayed (table is not empty)
        Assert.assertTrue(plantsPage.getPlantsTableRowCount() > 0,
                "No plants displayed in the table!");
    }

    @When("I click the Previous button")
    public void i_click_the_previous_button() {
        plantsPage.clickPreviousPage();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @Then("The previous page of plants is displayed in the table")
    public void the_previous_page_of_plants_is_displayed_in_the_table() {
        // Verify that plants are displayed (table is not empty)
        Assert.assertTrue(plantsPage.getPlantsTableRowCount() > 0,
                "No plants displayed in the table!");
    }

    @When("Delete all pagination test plants")
    public void delete_all_pagination_test_plants() {
        for (String plantName : paginationTestPlants) {
            try {
                plantsPage.clickPlantsTab();
                Thread.sleep(500);
                plantsPage.deletePlant(plantName);
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Could not delete plant: " + plantName + ". Error: " + e.getMessage());
            }
        }
        paginationTestPlants.clear();
    }

    @When("Click Price column header")
    public void click_price_column_header() {
        plantsPage.clickPriceColumnHeader();
    }

    @Then("The plants list should be sorted by price")
    public void the_plants_list_should_be_sorted_by_price() {
        List<Double> prices = plantsPage.getPlantPricesFromTable();
        Assert.assertFalse(prices.isEmpty(), "No plants found in the table!");

        // Check if sorted (either ascending or descending)
        boolean isAscending = true;
        boolean isDescending = true;

        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                isAscending = false;
            }
            if (prices.get(i) < prices.get(i + 1)) {
                isDescending = false;
            }
        }

        Assert.assertTrue(isAscending || isDescending,
                "Plants list is not sorted by price. Prices: " + prices);
    }

    @When("Click Name column header")
    public void click_name_column_header() {
        plantsPage.clickNameColumnHeader();
    }

    @Then("The plants list should be sorted by name")
    public void the_plants_list_should_be_sorted_by_name() {
        List<String> names = plantsPage.getPlantNamesFromTable();
        Assert.assertFalse(names.isEmpty(), "No plants found in the table!");

        // Create sorted copies for comparison
        List<String> sortedAsc = new ArrayList<>(names);
        Collections.sort(sortedAsc);

        List<String> sortedDesc = new ArrayList<>(names);
        Collections.sort(sortedDesc, Collections.reverseOrder());

        boolean isAscending = names.equals(sortedAsc);
        boolean isDescending = names.equals(sortedDesc);

        Assert.assertTrue(isAscending || isDescending,
                "Plants list is not sorted by name. Names: " + names);
    }
}
