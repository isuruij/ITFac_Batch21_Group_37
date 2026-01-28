package stepdefinitions.ui;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.AddPlantPage;
import pages.PlantsPage;
import utils.DriverFactory;

public class UISteps_214011E {

    PlantsPage plantsPage = new PlantsPage(DriverFactory.getDriver());
    AddPlantPage addPlantPage = new AddPlantPage(DriverFactory.getDriver());

    @When("Navigate to Plant tab")
    public void navigate_to_plant_tab() {
        plantsPage.clickPlantsTab();
    }

    @When("Click 'Add a Plant' button")
    public void click_add_a_plant_button() {
        plantsPage.clickAddPlant();
    }

    @When("Fill a plant name {string}")
    public void fill_a_plant_name(String plantName) {
        addPlantPage.enterPlantName(plantName);
    }

    @When("Select category {string}")
    public void select_category(String category) {
        addPlantPage.selectCategory(category);
    }

    @When("Set price {string}")
    public void set_price(String price) {
        addPlantPage.enterPrice(price);
    }

    @When("Set quantity {string}")
    public void set_quantity(String quantity) {
        addPlantPage.enterQuantity(quantity);
    }

    @When("Click 'Save' button")
    public void click_save_button() {
        addPlantPage.clickSave();
    }

    @Then("Plant {string} is added to the list")
    public void plant_is_added_to_the_list(String plantName) {
        // Refresh to see the new plant if needed, or wait
        // Based on PlantsPage.isPlantInList, it waits for table presence. 
        // We might need to ensure the list is refreshed or search for it.
        // The existing logic in UISteps_214154T uses search to verify.
        // The scenario says "Plant is added to the list", implies it's visible. 
        // I'll stick to isPlantInList which iterates rows. 
        // However, usually adding a plant might redirect to list or stay on page.
        // I'll assume it redirects to list or I need to navigate back?
        // The user steps say "Plant is added to the list".
        // Use existing search/verify logic might be safer.
        // Logic: Search for it and verify.
        
        plantsPage.enterPlantName(plantName);
        plantsPage.clickSearch();
        Assert.assertTrue(plantsPage.isPlantInList(plantName), "Plant " + plantName + " was not found in the list.");
    }
}
