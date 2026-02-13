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

import java.util.List;

public class UISteps_214154T {

    LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
    PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
    AddPlantPage addPlantPage = new AddPlantPage(DriverFactory.getDriver());

    @Given("Admin is logged into the system")
    public void admin_is_logged_into_the_system() {
        DriverFactory.getDriver().get(ConfigReader.getProperty("url") + "/ui/login");
        loginPage.login("admin", "admin123");
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
}
