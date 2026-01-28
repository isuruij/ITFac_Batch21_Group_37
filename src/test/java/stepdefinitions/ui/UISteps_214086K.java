package stepdefinitions.ui;

import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.CategoriesPage;
import utils.DriverFactory;

public class UISteps_214086K {

    CategoriesPage categoriesPage = new CategoriesPage(DriverFactory.getDriver());
    private String createdCategoryName;

    @When("Navigate to Categories tab")
    public void navigate_to_categories_tab() {
        categoriesPage.clickCategoriesTab();
    }

    @When("Click on Add a Category Button")
    public void click_on_add_category_button() {
        categoriesPage.clickAddCategory();
    }

    @When("Select {string} from the parent category dropdown")
    public void select_from_the_parent_category_dropdown(String parentName) {
        categoriesPage.selectParentCategory(parentName);
    }

    @When("Enter category name {string}")
    public void enter_category_name(String categoryName) {
        this.createdCategoryName = categoryName;
        categoriesPage.enterCategoryName(categoryName);
    }

    @When("Click on Save button")
    public void click_on_save_button() {
        categoriesPage.clickSave();
    }

    @Then("Verify that the new category {string} is added to the list")
    public void verify_that_the_new_category_is_added_to_the_list(String categoryName) {
        Assert.assertTrue(categoriesPage.isCategoryInList(categoryName),
                "Category '" + categoryName + "' was not found in the list.");
    }

    @After
    public void tearDown() {
        if (createdCategoryName != null) {
            try {
                categoriesPage.clickCategoriesTab(); // Ensure we are on the list page
                categoriesPage.deleteCategory(createdCategoryName);
            } catch (Exception e) {
                System.out.println("Cleanup failed: " + e.getMessage());
            }
        }
    }
}
