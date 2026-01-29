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
    
    @When("Click edit icon on plant {string}")
    public void click_edit_icon_on_plant(String plantName) {
        plantsPage.clickEditPlant(plantName);
    }

    @Then("Plant {string} has price {string}")
    public void plant_has_price(String plantName, String price) {
        String plantInfo = plantsPage.getPlantPrice(plantName);
        Assert.assertTrue(plantInfo.contains(price), "Price " + price + " not found for plant " + plantName + ". Row text: " + plantInfo);
    }

    @Then("Plant {string} is added to the list")
    public void plant_is_added_to_the_list(String plantName) {
        plantsPage.enterPlantName(plantName);
        plantsPage.clickSearch();
        Assert.assertTrue(plantsPage.isPlantInList(plantName), "Plant " + plantName + " was not found in the list.");
    }

    @Then("Error message {string} is displayed")
    public void error_message_is_displayed(String expectedError) {
        String actualError = addPlantPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError), "Expected error containing '" + expectedError + "' but got '" + actualError + "'");
    }
}
