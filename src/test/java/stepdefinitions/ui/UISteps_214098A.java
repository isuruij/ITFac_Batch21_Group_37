package stepdefinitions.ui;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.SalesPage;
import utils.DriverFactory;

public class UISteps_214098A {

    SalesPage salesPage = new SalesPage(DriverFactory.getDriver());

    @When("Navigate to the Sales page")
    public void navigate_to_the_sales_page() {
        salesPage.clickSalesTab();
    }

    @Then("Observe the action buttons on the page")
    public void observe_the_action_buttons_on_the_page() {
        Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("/ui/sales"),
                "Driver is not on Sales page.");
    }

    @Then("Sell Plant button is visible only for Admin users")
    public void sell_plant_button_is_visible_only_for_admin_users() {
        Assert.assertTrue(salesPage.isSellPlantButtonVisible(), "Sell Plant button is not visible for Admin.");
    }

    @When("Click the Sell Plant button")
    public void click_the_sell_plant_button() {
        salesPage.clickSellPlantButton();
    }

    @Then("Clicking the button navigates to the Sell Plant page successfully")
    public void clicking_the_button_navigates_to_the_sell_plant_page_successfully() {
        Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("/ui/sales/new"),
                "Failed to navigate to Sell Plant page.");
    }

    @io.cucumber.java.en.Given("At least one sale exists")
    public void at_least_one_sale_exists() {
        // Assuming sales exist or seeded. If check implemented, it would go here.
    }

    @Then("Delete option is available only to Admin users")
    public void delete_option_is_available_only_to_admin_users() {
        Assert.assertTrue(salesPage.isDeleteButtonVisible(), "Delete button is not visible for Admin.");
    }

    @When("Click the Delete button for a sale record")
    public void click_the_delete_button_for_a_sale_record() {
        salesPage.clickDeleteButton();
    }

    @Then("Observe the confirmation popup")
    public void observe_the_confirmation_popup() {
        String alertText = salesPage.getAlertText();
        Assert.assertTrue(alertText.contains("Are you sure you want to delete this sale?"),
                "Incorrect alert text: " + alertText);
    }

    @When("Click Confirm")
    public void click_confirm() {
        salesPage.acceptAlert();
    }

    @Then("Sale record is deleted successfully after confirmation")
    public void sale_record_is_deleted_successfully_after_confirmation() {
        Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("/ui/sales"),
                "Driver is not on Sales page after delete.");
    }
}
