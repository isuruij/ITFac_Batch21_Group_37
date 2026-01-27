package stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.LoginPage;
import pages.PlantsPage;
import utils.ConfigReader;
import utils.DriverFactory;

public class PlantSteps {

    LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
    PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());

    @Given("Admin is logged into the system")
    public void admin_is_logged_into_the_system() {
        DriverFactory.getDriver().get(ConfigReader.getProperty("url") + "/ui/login"); // Assuming login page is
                                                                                      // /ui/login based on form action
        // Note: The HTML form action was /ui/login, but usually GET returns the page.
        // If the base URL opens login directly, this is fine.
        // Let's assume Valid Credentials for 'Admin' as per requirement "Logged in as
        // Admin"
        loginPage.login("admin", "admin123"); // You might need to change these credentials
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
}
