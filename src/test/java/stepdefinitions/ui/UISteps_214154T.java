package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;
import pages.PlantsPage;
import utils.ConfigReader;
import utils.DriverFactory;

public class UISteps_214154T {

    @Given("Admin is logged into the system")
    public void admin_is_logged_into_the_system() {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        DriverFactory.getDriver().get(ConfigReader.getProperty("url") + "/ui/login");
        String adminUsername = ConfigReader.getProperty("admin.username");
        String adminPassword = ConfigReader.getProperty("admin.password");
        loginPage.login(adminUsername, adminPassword);
    }

    @Given("Existing plants are already added in the system")
    public void existing_plants_are_already_added_in_the_system() {
        // Precondition: This test assumes that plants exist in the system
        // The test will verify the search functionality using the configured test plant
        // If needed, this step can be enhanced to verify plant existence via API
    }

    @When("Navigate to Plants tab using the side bar")
    public void navigate_to_plants_tab_using_the_side_bar() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        plantsPage.clickPlantsTab();
    }

    @When("Enter plant name in the search input box")
    public void enter_plant_name_in_the_search_input_box() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        String plantName = ConfigReader.getProperty("test.plant.name");
        plantsPage.enterPlantName(plantName);
    }

    @When("Click Search button")
    public void click_search_button() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        plantsPage.clickSearch();
    }

    @Then("Show the plant list filtered by entered name")
    public void show_the_plant_list_filtered_by_entered_name() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        String plantName = ConfigReader.getProperty("test.plant.name");
        Assert.assertTrue(plantsPage.isPlantInList(plantName),
                "Plant " + plantName + " not found in the search results!");
    }

    @Given("Existing categories are already added in the system")
    public void existing_categories_are_already_added_in_the_system() {
        // Precondition: This test assumes that categories exist in the system
        // The test will verify the filter functionality using the configured test
        // category
    }

    @When("Select the needed category from the category selector")
    public void select_the_needed_category_from_the_category_selector() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        String category = ConfigReader.getProperty("test.plant.category");
        plantsPage.selectCategory(category);
    }

    @Then("Show the plant list filtered by the selected category")
    public void show_the_plant_list_filtered_by_the_selected_category() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        String category = ConfigReader.getProperty("test.plant.category");
        Assert.assertTrue(plantsPage.isPlantListFilteredByCategory(category),
                "Plants are not filtered by category: " + category);
    }

    @When("Click Quantity column header to sort by quantity")
    public void click_quantity_column_header_to_sort_by_quantity() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        plantsPage.clickQuantityHeader();
    }

    @Then("Show the sorted plants list by quantity")
    public void show_the_sorted_plants_list_by_quantity() {
        PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
        Assert.assertTrue(plantsPage.isPlantListSortedByQuantity(),
                "Plants are not sorted by quantity!");
    }
}
