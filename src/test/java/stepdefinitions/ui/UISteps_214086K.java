package stepdefinitions.ui;

import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.CategoriesPage;
import utils.DriverFactory;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;

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

    @Then("Verify that the new category {string} with parent {string} is added to the list")
    public void verify_that_the_new_category_is_added_to_the_list_under_parent(String categoryName, String parentName) {
        Assert.assertTrue(categoriesPage.isCategoryInList(categoryName, parentName),
                "Category '" + categoryName + "' with parent '" + parentName + "' was not found in the list.");
    }

    @After("@M2-UI-01 or @M2-UI-02")
    public void tearDownAPI() {
        if (createdCategoryName == null)
            return;

        try {
            // 1. Login
            String token = RestAssured.given()
                    .baseUri("http://localhost:8080")
                    .contentType("application/json")
                    .body("{\"username\": \"admin\", \"password\": \"admin123\"}")
                    .post("/api/auth/login")
                    .jsonPath().getString("token");

            // 2. Get All Categories to find ID
            List<Map<String, Object>> categories = RestAssured.given()
                    .baseUri("http://localhost:8080")
                    .header("Authorization", "Bearer " + token)
                    .get("/api/categories")
                    .jsonPath().getList("");

            Integer categoryId = null;
            if (categories != null) {
                for (Map<String, Object> cat : categories) {
                    Object nameObj = cat.get("name");
                    if (nameObj != null && createdCategoryName.equalsIgnoreCase(nameObj.toString())) {
                        categoryId = (Integer) cat.get("id");
                        break;
                    }
                }
            }

            // 3. Delete
            if (categoryId != null) {
                RestAssured.given()
                        .baseUri("http://localhost:8080")
                        .header("Authorization", "Bearer " + token)
                        .delete("/api/categories/" + categoryId)
                        .then().statusCode(204);
                System.out.println(
                        "API Cleanup: Deleted category '" + createdCategoryName + "' (ID: " + categoryId + ")");
            } else {
                System.out.println("API Cleanup: Category '" + createdCategoryName + "' not found in DB.");
            }

        } catch (Exception e) {
            System.err.println("API Cleanup Failed: " + e.getMessage());
        }
    }
}
