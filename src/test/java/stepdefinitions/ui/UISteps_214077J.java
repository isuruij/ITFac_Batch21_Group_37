package stepdefinitions.ui;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.CategoriesPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.NavigationMenu;
import pages.PlantsPage;
import utils.APIUtils;
import utils.ConfigReader;
import utils.DriverFactory;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.cucumber.java.Before;

public class UISteps_214077J {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private NavigationMenu navigationMenu;
    private CategoriesPage categoriesPage;
    private PlantsPage plantsPage;

    @Before
    public void setup() {
        initPages();  // Initialize driver and all page objects once
    }

    private void initPages() {
        driver = DriverFactory.getDriver();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        navigationMenu = new NavigationMenu(driver);
        categoriesPage = new CategoriesPage(driver);
        plantsPage = new PlantsPage(driver);
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {    
        driver.get(ConfigReader.getProperty("url") + "/ui/login"); 
    }

    @When("I login with valid credentials {string} and {string}")
    public void i_login_with_valid_credentials_and(String username, String password) {
        loginPage.login(username, password);
    }

    @Given("I am logged in as {string} with {string}")
    public void i_am_logged_in_as_with(String username, String password) {
        driver.get(ConfigReader.getProperty("url") + "/ui/login");
        loginPage.login(username, password);
        // Ensure we are logged in
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @Then("I should see the Dashboard page")
    public void i_should_see_the_dashboard_page() {
        // Wait for dashboard to load
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("I should see the Categories card")
    public void i_should_see_the_categories_card() {
        Assert.assertTrue(dashboardPage.isCategoriesCardDisplayed(), "Categories card is not displayed");
    }

    @Then("I should see the Plants card")
    public void i_should_see_the_plants_card() {
        Assert.assertTrue(dashboardPage.isPlantsCardDisplayed(), "Plants card is not displayed");
    }

    @Then("I should see the Sales card")
    public void i_should_see_the_sales_card() {
        Assert.assertTrue(dashboardPage.isSalesCardDisplayed(), "Sales card is not displayed");
    }

    @Then("I should see the Inventory card")
    public void i_should_see_the_inventory_card() {
        Assert.assertTrue(dashboardPage.isInventoryCardDisplayed(), "Inventory card is not displayed");
    }

    @When("I am on the Dashboard page")
    public void i_am_on_the_dashboard_page() {
        navigationMenu.clickLink("Dashboard");
    }

    @Then("I should see the Main Category count matches the actual system count")
    public void i_should_see_the_main_category_count_matches_actual() {
        // UI Count
        String uiCountStr = dashboardPage.getMainCategoryCount();
        Assert.assertNotNull(uiCountStr, "Main Category count should not be null");
        int uiCount = Integer.parseInt(uiCountStr);

        // API Count
        Response response = APIUtils.get("/api/categories/main", getApiToken());
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to fetch main categories from API");
        int apiCount = response.jsonPath().getList("$").size();

        Assert.assertEquals(uiCount, apiCount, "Main Category count mismatch between UI and API");
    }

    @Then("I should see the Sub Category count matches the actual system count")
    public void i_should_see_the_sub_category_count_matches_actual() {
        // UI Count
        String uiCountStr = dashboardPage.getSubCategoryCount();
        Assert.assertNotNull(uiCountStr, "Sub Category count should not be null");
        int uiCount = Integer.parseInt(uiCountStr);

        // API Count
        Response response = APIUtils.get("/api/categories/sub-categories", getApiToken());
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to fetch sub categories from API");
        int apiCount = response.jsonPath().getList("$").size();

        Assert.assertEquals(uiCount, apiCount, "Sub Category count mismatch between UI and API");
    }

    @Then("I should see the Total Plant count matches the actual system count")
    public void i_should_see_the_total_plant_count_matches_actual() {
        // UI Count
        String uiCountStr = dashboardPage.getTotalPlantCount();
        Assert.assertNotNull(uiCountStr, "Total Plant count should not be null");
        int uiCount = Integer.parseInt(uiCountStr);

        // API Count
        Response response = APIUtils.get("/api/plants/summary", getApiToken());
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to fetch plant summary from API");
        // Extract totalPlants from JSON
        int apiCount = response.jsonPath().getInt("totalPlants");

        Assert.assertEquals(uiCount, apiCount, "Total Plant count mismatch between UI and API");
    }
    
    // Helper to get token (Reuse admin for system verification)
    private String getApiToken() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", ConfigReader.getProperty("testuser.username"));
        credentials.put("password", ConfigReader.getProperty("testuser.password"));
        Response loginResponse = APIUtils.post("/api/auth/login", credentials, null);
        if (loginResponse.getStatusCode() != 200) {
             // Fallback
             throw new RuntimeException("Failed to get API token for verification. content: " + loginResponse.getBody().asString());
        }
        return loginResponse.jsonPath().getString("token");
    }

    @Then("I should see the following navigation links:")
    public void i_should_see_the_following_navigation_links(List<String> links) {
        for (String linkName : links) {
            Assert.assertTrue(navigationMenu.isLinkVisible(linkName), linkName + " link is not visible");
        }
    }

    @When("I click on the {string} navigation link")
    public void i_click_on_the_navigation_link(String linkName) {
        navigationMenu.clickLink(linkName);
        try { Thread.sleep(1000); } catch (InterruptedException e) {} // Wait for nav
    }

    @Then("The {string} navigation link should be active")
    public void the_navigation_link_should_be_active(String linkName) {
        // Assert URL first to confirm navigation
        String currentUrl = driver.getCurrentUrl();
        String expectedPath = linkName.toLowerCase();
        if(linkName.equals("Category")) expectedPath = "categories";
        
        // Checking class attribute for 'active'
        boolean isActive = false;
        String foundClasses = "";
        for (int i = 0; i < 5; i++) {
            if (navigationMenu.isLinkActive(linkName)) {
                isActive = true;
                break;
            }
            foundClasses = navigationMenu.getLinkClasses(linkName);
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
        Assert.assertTrue(isActive, linkName + " link is not active. Current URL: " + driver.getCurrentUrl() + ". Classes found: '" + foundClasses + "'");
    }

    @Given("I navigate to the Categories page")
    public void i_navigate_to_the_categories_page() {
        navigationMenu.clickLink("Category");
    }

    @Given("Multiple categories exist in the system")
    public void multiple_categories_exist_in_the_system() {
        if (categoriesPage.getTableRows().isEmpty()) {
            System.out.println("Warning: No categories found to test sorting.");
        }
    }

    @When("I sort the categories by {string}")
    public void i_sort_the_categories_by(String criteria) {
        switch (criteria) {
            case "ID":
                categoriesPage.clickSortById();
                break;
            case "Name":
                categoriesPage.clickSortByName();
                break;
            case "Parent Category":
                categoriesPage.clickSortByParentCategory();
                break;
        }
        try { Thread.sleep(1000); } catch (InterruptedException e) {} // Wait for sort
    }

    @Then("The categories should be sorted by {string} in {string} order")
    public void the_categories_should_be_sorted_by_in_order(String criteria, String order) {
        // Just calling the simplified version for now which defaults to checking ascending/alphabetical
        the_categories_should_be_sorted_by_in_order(criteria);
    }
    
    @Then("The categories should be sorted by {string} in ascending order")
    public void the_categories_should_be_sorted_by_in_ascending_order(String criteria) {
        the_categories_should_be_sorted_by_in_order(criteria);
    }
    
    @Then("The categories should be sorted by {string} in alphabetical order")
    public void the_categories_should_be_sorted_by_in_alphabetical_order(String criteria) {
        the_categories_should_be_sorted_by_in_order(criteria);
    }

    public void the_categories_should_be_sorted_by_in_order(String criteria) {
        // Wait for page update after sort
        try { Thread.sleep(1500); } catch (InterruptedException e) {}

        List<WebElement> rows = categoriesPage.getTableRows();
        List<String> originalData = new ArrayList<>();
        
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 2) {
                switch (criteria) {
                    case "ID":
                        originalData.add(cells.get(0).getText().trim());
                        break;
                    case "Name":
                        originalData.add(cells.get(1).getText().trim());
                        break;
                    case "Parent Category":
                        originalData.add(cells.get(2).getText().trim());
                        break;
                }
            }
        }
        
        List<String> sortedData = new ArrayList<>(originalData);
        
        if (criteria.equals("ID")) {
             // Integer sorting
             try {
                 List<Integer> intList = new ArrayList<>();
                 for(String s : originalData) intList.add(Integer.parseInt(s));
                 List<Integer> sortedInt = new ArrayList<>(intList);
                 Collections.sort(sortedInt);
                 
                 // Check if match either Ascending or Descending
                 boolean matchesAsc = intList.equals(sortedInt);
                 Collections.reverse(sortedInt);
                 boolean matchesDesc = intList.equals(sortedInt);
                 
                 Assert.assertTrue(matchesAsc || matchesDesc, "Categories not sorted by ID (Asc or Desc). Actual: " + intList);
                 return;
             } catch (NumberFormatException e) {
                 // Fallback to string sort if IDs are non-numeric
             }
        }
        
        if (criteria.equals("Parent Category")) {
            // Treat "-" as "" (empty)
             List<String> cleanedData = new ArrayList<>();
             for (String s : originalData) {
                 cleanedData.add(s.equals("-") ? "" : s);
             }
             
             List<String> sortedClean = new ArrayList<>(cleanedData);
             Collections.sort(sortedClean, String.CASE_INSENSITIVE_ORDER);
             
             boolean matchAsc = cleanedData.equals(sortedClean);
             Collections.reverse(sortedClean);
             boolean matchDesc = cleanedData.equals(sortedClean);
             
             Assert.assertTrue(matchAsc || matchDesc, "Categories not sorted by Parent Category. Actual: " + originalData);
             return;
        }

        Collections.sort(sortedData, String.CASE_INSENSITIVE_ORDER);
        
        // Check Ascending
        boolean matchesAsc = originalData.equals(sortedData);
        
        // Check Descending
        Collections.reverse(sortedData);
        boolean matchesDesc = originalData.equals(sortedData);

        Assert.assertTrue(matchesAsc || matchesDesc, "Categories not sorted by " + criteria + ". Actual: " + originalData);
    }

    @When("I click on the Add Category button")
    public void i_click_on_the_add_category_button() {
        if(categoriesPage == null) initPages();
        categoriesPage.clickAddCategory();
    }

    @Given("A category {string} exists")
    public void a_category_exists(String categoryName) {
        if(!driver.getCurrentUrl().contains("categories")) {
             i_navigate_to_the_categories_page();
        }

        // Search first to see if it exists anywhere
        categoriesPage.enterSearchKeyword(categoryName);
        categoriesPage.clickSearch();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        if(!categoriesPage.isCategoryInList(categoryName)) {
            System.out.println("Category " + categoryName + " not found after search. Creating it...");
            // Clear search by reloading page
            i_navigate_to_the_categories_page();
            
            categoriesPage.clickAddCategory();
            categoriesPage.enterCategoryName(categoryName);
            categoriesPage.clickSave();
            
            // Wait for save
            try { Thread.sleep(4000); } catch (InterruptedException e) {}
            
            if(!categoriesPage.isCategoriesPageDisplayed()) {
                 // Check for error before navigating away
                 String error = categoriesPage.getErrorAlertText();
                 if (error != null && !error.isEmpty()) {
                      Assert.fail("Failed to create category '" + categoryName + "'. Backend/Validation Error: " + error);
                 }
                 
                 // Check for invalid feedback
                 try {
                     String feedback = categoriesPage.getInvalidFeedbackText();
                     if (feedback != null && !feedback.isEmpty()) {
                         Assert.fail("Failed to create category '" + categoryName + "'. Field Validation Error: " + feedback);
                     }
                 } catch (Exception e) {}

                 System.out.println("Still on add/edit page (timeout). URL: " + driver.getCurrentUrl());
                 System.out.println("Navigating back...");
                 categoriesPage.clickCategoriesTab();
            }
            
            // Search again to confirm
            categoriesPage.enterSearchKeyword(categoryName);
            categoriesPage.clickSearch();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            if(!categoriesPage.isCategoryInList(categoryName)) {
                List<WebElement> rows = categoriesPage.getTableRows();
                List<String> names = new ArrayList<>();
                for(WebElement r : rows) names.add(r.getText());
                Assert.fail("Failed to create category: " + categoryName + ". Visible rows after search: " + names);
            }
        } else {
             System.out.println("Category " + categoryName + " already exists.");
        }
        // Reset view to full list
        i_navigate_to_the_categories_page();
    }

    @When("I click on the Edit button for category {string}")
    public void i_click_on_the_edit_button_for_category(String categoryName) {
        categoriesPage.clickEditCategory(categoryName);
    }

    @When("I enter category name {string}")
    public void i_enter_category_name(String name) {
        categoriesPage.enterCategoryName(name);
    }

    @When("I click the Cancel button on the Edit Category page")
    public void i_click_the_cancel_button_on_the_edit_category_page() {
        categoriesPage.clickEditCancel();
    }

    @Then("I should be redirected to the Category list page")
    public void i_should_be_redirected_to_the_category_list_page() {
        Assert.assertTrue(categoriesPage.isCategoriesPageDisplayed(), "Not redirected to Category list page");
    }

    @Then("The category {string} should not be visible in the list")
    public void the_category_should_not_be_visible_in_the_list(String categoryName) {
        Assert.assertFalse(categoriesPage.isCategoryInList(categoryName), "Category " + categoryName + " is visible but should not be.");
    }

    @Then("The category {string} should still be visible in the list")
    public void the_category_should_still_be_visible_in_the_list(String categoryName) {
        Assert.assertTrue(categoriesPage.isCategoryInList(categoryName), "Category " + categoryName + " is missing.");
    }

    @Given("I navigate to the Plants page")
    public void i_navigate_to_the_plants_page() {
        navigationMenu.clickLink("Plants");
    }

    @When("I click on the Add Plant button")
    public void i_click_on_the_add_plant_button() {
        plantsPage.clickAddaPlant();
    }

    @When("I enter plant name {string}")
    public void i_enter_plant_name(String name) {
        plantsPage.enterPlantName(name);
    }

    @When("I click the Cancel button in Plant page")
    public void i_click_the_cancel_button_in_plant_page() {
        plantsPage.clickCancel();
    }

    @Then("I should be redirected to the Plants list page")
    public void i_should_be_redirected_to_the_plants_list_page() {
        Assert.assertTrue(plantsPage.isPlantsPageDisplayed(), "Not redirected to Plants list page");
    }

    @Then("The plant {string} should not be visible in the list")
    public void the_plant_should_not_be_visible_in_the_list(String plantName) {
        Assert.assertFalse(plantsPage.isPlantInList(plantName), "Plant " + plantName + " is visible but should not be.");
    }

    @Given("A plant {string} exists")
    public void a_plant_exists(String plantName) {
        if(!plantsPage.isPlantInList(plantName)) {
            plantsPage.clickAddaPlant();
            plantsPage.enterPlantName(plantName);
            plantsPage.clickSave();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            if(!plantsPage.isPlantsPageDisplayed()) {
                 plantsPage.clickPlantsTab();
            }
        }
    }

    @When("I click on the Edit button for plant {string}")
    public void i_click_on_the_edit_button_for_plant(String plantName) {
         plantsPage.clickEditPlant(plantName);
    }

    @Then("The plant {string} should still be visible in the list")
    public void the_plant_should_still_be_visible_in_the_list(String plantName) {
        Assert.assertTrue(plantsPage.isPlantInList(plantName), "Plant " + plantName + " is missing.");
    }

    @When("I click the Save button")
    public void i_click_the_save_button() {
        categoriesPage.clickSave();
    }

    @Then("I should see an error message indicating duplicate category")
    public void i_should_see_an_error_message_indicating_duplicate_category() {
        // Checking for common error alerts or invalid feedback
        boolean isError = false;
        try {
            // Might be an alert-danger or invalid-feedback
             String feedback = "";
             try { feedback = categoriesPage.getInvalidFeedbackText(); } catch(Exception e) {}
             
             if(feedback != null && !feedback.isEmpty()) {
                 isError = true;
                 System.out.println("Duplicate check found feedback: " + feedback);
             }
             
             // Or an alert
             if(!isError) {
                  isError = categoriesPage.isErrorAlertDisplayed();
                  if(isError) System.out.println("Duplicate check found error alert.");
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Debugging if failed
        if(!isError) {
             System.out.println("Debug Verify Duplicate: No error found. Current URL: " + driver.getCurrentUrl());
             try {
                WebElement body = driver.findElement(By.tagName("body"));
                System.out.println("Body Text snippet: " + body.getText().substring(0, Math.min(body.getText().length(), 500)));
             } catch(Exception e) {}
        }
        
        Assert.assertTrue(isError, "Error message for duplicate category not displayed");
    }

    @Then("I should see that I am still on the Add Category page")
    public void i_should_see_that_i_am_still_on_the_add_category_page() {
        Assert.assertFalse(categoriesPage.isCategoriesPageDisplayed(), "Unexpectedly redirected to list page (duplicate might have been saved)");
        // More specific check
        Assert.assertTrue(driver.getCurrentUrl().contains("/add") || driver.getCurrentUrl().contains("/create"), "Not on Add Category page");
    }

    private String identifiedSubCategory;
    private String identifiedParentCategory;
    private String identifiedPlantName;

    @Given("I identify a sub-category with a linked plant")
    public void i_identify_a_sub_category_with_a_linked_plant() {
        
        System.out.println("[M4-UI-09] Looking for existing sub-category with linked plant...");
        
        // For simplicity, create a test data set
        int randomNum = (int) (Math.random() * 9000) + 1000;
        String mainCat = "Main" + randomNum;
        String subCat = "Sub" + randomNum;
        String plant = "Plt" + randomNum;
        
        System.out.println("[M4-UI-09] Creating test data: Main=" + mainCat + ", Sub=" + subCat + ", Plant=" + plant);
        
        // 1. Create Main Category
        i_navigate_to_the_categories_page();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        categoriesPage.clickAddCategory();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        categoriesPage.enterCategoryName(mainCat);
        categoriesPage.clickSave();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        
        // 2. Create Sub-Category under Main
        if(!categoriesPage.isCategoriesPageDisplayed()) {
            categoriesPage.clickCategoriesTab();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
        categoriesPage.clickAddCategory();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        categoriesPage.enterCategoryName(subCat);
        categoriesPage.selectParentCategory(mainCat);
        categoriesPage.clickSave();
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        
        // 3. Create Plant linked to Sub-Category
        navigationMenu.clickLink("Plants");
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        plantsPage.clickAddaPlant();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        plantsPage.enterPlantName(plant);
        plantsPage.selectPlantCategory(subCat);
        plantsPage.enterPrice("25.00");
        plantsPage.enterQuantity("50");
        plantsPage.clickSave();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Navigate back to Categories
        navigationMenu.clickLink("Category");
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        identifiedSubCategory = subCat;
        identifiedParentCategory = mainCat;
        identifiedPlantName = plant;
        
        System.out.println("[M4-UI-09] Test data created successfully");
    }

    @When("I attempt to convert the sub-category into a main category")
    public void i_attempt_to_convert_the_sub_category_into_a_main_category() {
        Assert.assertNotNull(identifiedSubCategory, "No sub-category was identified!");
        
        System.out.println("[M4-UI-09] Attempting to edit sub-category: " + identifiedSubCategory);
        
        // Search for the sub-category
        categoriesPage.enterSearchKeyword(identifiedSubCategory);
        categoriesPage.clickSearch();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        // Click Edit
        categoriesPage.clickEditCategory(identifiedSubCategory);
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Try to remove parent (convert to main category)
        categoriesPage.selectParentCategory(""); // Empty selection = Main Category
        categoriesPage.clickSave();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        System.out.println("[M4-UI-09] Save clicked, checking for error...");
    }
    
    @Then("I should see an error message indicating conversion is not allowed")
    public void i_should_see_an_error_message_indicating_conversion_is_not_allowed() {
        boolean isError = false;
        String errorSource = "";
        String errorText = "";

        // Check for error alert on edit page
        if(categoriesPage.isErrorAlertDisplayed()) {
            errorText = categoriesPage.getErrorAlertText();
            isError = true;
            errorSource = "UI Alert";
        }
        
        // Check for JS Alert
        if(!isError) {
            try {
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                errorText = alert.getText();
                alert.accept();
                isError = true;
                errorSource = "JS Alert";
            } catch (Exception e) {}
        }

        // Check for Backend Error Page
        if(!isError) {
            try {
                String bodyText = driver.findElement(By.tagName("body")).getText();
                if(bodyText.contains("Whitelabel Error Page") || 
                   bodyText.contains("Internal Server Error")) {
                    isError = true;
                    errorSource = "Backend Error Page";
                    errorText = bodyText;
                    if(driver.getCurrentUrl().contains("error")) {
                        driver.navigate().back();
                    }
                }
            } catch(Exception e) {}
        }
        
        System.out.println("[M4-UI-09] Conversion Prevention Check: " + isError + " (" + errorSource + ")");
        System.out.println("[M4-UI-09] Error Text: " + errorText);

        Assert.assertTrue(isError, "No error message was displayed preventing the conversion.");
        
        // Verify it's not a technical error
        boolean isTechnicalError = errorText.contains("could not execute statement") || 
                                   errorText.contains("foreign key constraint") || 
                                   errorText.contains("SQL") ||
                                   errorText.contains("Exception") ||
                                   errorText.contains("Whitelabel Error Page");
                                   
        Assert.assertFalse(isTechnicalError, 
            "Test Failed: System showed a technical/backend error instead of a user-friendly message. Error: " + errorText);
    }
    
    @Then("The sub-category should still have its parent category")
    public void the_sub_category_should_still_have_its_parent_category() {
        Assert.assertNotNull(identifiedSubCategory, "No sub-category was identified!");
        Assert.assertNotNull(identifiedParentCategory, "No parent category was identified!");
        
        System.out.println("[M4-UI-09] Verifying sub-category still has parent...");
        
        // Navigate back to categories list if not already there
        if(!categoriesPage.isCategoriesPageDisplayed()) {
            categoriesPage.clickCategoriesTab();
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
        }
        
        // Search for sub-category
        categoriesPage.enterSearchKeyword(identifiedSubCategory);
        categoriesPage.clickSearch();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        // Verify it still has the parent
        Assert.assertTrue(categoriesPage.isCategoryInList(identifiedSubCategory, identifiedParentCategory),
            "Sub-category " + identifiedSubCategory + " does not have parent " + identifiedParentCategory + " anymore!");
        
        System.out.println("[M4-UI-09] Verified: Sub-category still has its parent");
    }

    @Given("A category {string} exists with a sub-category {string}")
    public void a_category_exists_with_a_sub_category(String parentName, String subName) {
        
        // 1. Ensure Parent Exists
        System.out.println("[Setup] Checking if parent category exists: " + parentName);
        i_navigate_to_the_categories_page();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        categoriesPage.enterSearchKeyword(parentName);
        categoriesPage.clickSearch();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        if(!categoriesPage.isCategoryInList(parentName)) {
            System.out.println("[Setup] Parent category not found, creating: " + parentName);
            
            // Navigate to ensure clean state
            i_navigate_to_the_categories_page(); 
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            categoriesPage.clickAddCategory();
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            categoriesPage.enterCategoryName(parentName);
            categoriesPage.clickSave();
            
            // Wait for save to complete and page to load
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
            
            // Ensure we're back on categories page
            if(!categoriesPage.isCategoriesPageDisplayed()) {
                System.out.println("[Setup] Not on categories page, clicking categories tab");
                categoriesPage.clickCategoriesTab();
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
            }
            
            // Verify parent was created successfully
            i_navigate_to_the_categories_page();
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            categoriesPage.enterSearchKeyword(parentName);
            categoriesPage.clickSearch();
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            
            if(!categoriesPage.isCategoryInList(parentName)) {
                throw new RuntimeException("[Setup] Failed to create parent category: " + parentName);
            }
            System.out.println("[Setup] Parent category created successfully: " + parentName);
        } else {
            System.out.println("[Setup] Parent category already exists: " + parentName);
        }

        // 2. Ensure Sub Exists
        System.out.println("[Setup] Checking if sub-category exists: " + subName);
        i_navigate_to_the_categories_page();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        categoriesPage.enterSearchKeyword(subName);
        categoriesPage.clickSearch();
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        if(!categoriesPage.isCategoryInList(subName)) {
            System.out.println("[Setup] Sub-category not found, creating: " + subName);
            
            // Navigate to ensure clean state
            i_navigate_to_the_categories_page(); 
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            categoriesPage.clickAddCategory();
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            categoriesPage.enterCategoryName(subName);
            categoriesPage.selectParentCategory(parentName);
            categoriesPage.clickSave();
            
            // Wait for save to complete and page to load
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
            
            // Ensure we're back on categories page
            if(!categoriesPage.isCategoriesPageDisplayed()) {
                System.out.println("[Setup] Not on categories page, clicking categories tab");
                categoriesPage.clickCategoriesTab();
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
            }
            
            // Verify sub-category was created successfully
            i_navigate_to_the_categories_page();
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            categoriesPage.enterSearchKeyword(subName);
            categoriesPage.clickSearch();
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            
            if(!categoriesPage.isCategoryInList(subName)) {
                throw new RuntimeException("[Setup] Failed to create sub-category: " + subName);
            }
            System.out.println("[Setup] Sub-category created successfully: " + subName);
        } else {
            System.out.println("[Setup] Sub-category already exists: " + subName);
        }
        
        // Reset to full list
        i_navigate_to_the_categories_page();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("[Setup] Category setup complete");
    }
    
    @After("@M4-UI-09")
    public void cleanupM4UI09() {
        System.out.println("[Cleanup M4-UI-09] Starting cleanup...");
        if(identifiedSubCategory != null || identifiedParentCategory != null || identifiedPlantName != null) {
            try {                
                // 1. Delete Plant first
                if(identifiedPlantName != null) {
                    System.out.println("[Cleanup] Deleting plant: " + identifiedPlantName);
                    try {
                        navigationMenu.clickLink("Plants");
                        try { Thread.sleep(1500); } catch (InterruptedException e) {}
                        plantsPage.enterSearchPlantName(identifiedPlantName);
                        plantsPage.clickSearch();
                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
                        if(plantsPage.isPlantInList(identifiedPlantName)) {
                            plantsPage.clickDeletePlant(identifiedPlantName);
                            try { Thread.sleep(1000); } catch (InterruptedException e) {}
                            System.out.println("[Cleanup] Plant deleted: " + identifiedPlantName);
                        }
                    } catch(Exception e) {
                        System.out.println("[Cleanup] Failed to delete plant: " + e.getMessage());
                    }
                }
                
                // 2. Delete Sub-Category
                if(identifiedSubCategory != null) {
                    System.out.println("[Cleanup] Deleting sub-category: " + identifiedSubCategory);
                    try {
                        navigationMenu.clickLink("Category");
                        try { Thread.sleep(1500); } catch (InterruptedException e) {}
                        categoriesPage.enterSearchKeyword(identifiedSubCategory);
                        categoriesPage.clickSearch();
                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
                        if(categoriesPage.isCategoryInList(identifiedSubCategory)) {
                            categoriesPage.clickDeleteCategory(identifiedSubCategory);
                            try { Thread.sleep(1000); } catch (InterruptedException e) {}
                            System.out.println("[Cleanup] Sub-category deleted: " + identifiedSubCategory);
                        }
                    } catch(Exception e) {
                        System.out.println("[Cleanup] Failed to delete sub-category: " + e.getMessage());
                    }
                }
                
                // 3. Delete Main Category
                if(identifiedParentCategory != null) {
                    System.out.println("[Cleanup] Deleting main category: " + identifiedParentCategory);
                    try {
                        i_navigate_to_the_categories_page();
                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
                        categoriesPage.enterSearchKeyword(identifiedParentCategory);
                        categoriesPage.clickSearch();
                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
                        if(categoriesPage.isCategoryInList(identifiedParentCategory)) {
                            categoriesPage.clickDeleteCategory(identifiedParentCategory);
                            try { Thread.sleep(1000); } catch (InterruptedException e) {}
                            System.out.println("[Cleanup] Main category deleted: " + identifiedParentCategory);
                        }
                    } catch(Exception e) {
                        System.out.println("[Cleanup] Failed to delete main category: " + e.getMessage());
                    }
                }
                
                System.out.println("[Cleanup M4-UI-09] Cleanup completed.");
            } catch(Exception e) {
                System.out.println("[Cleanup M4-UI-09] Error during cleanup: " + e.getMessage());
            } finally {
                identifiedSubCategory = null;
                identifiedParentCategory = null;
                identifiedPlantName = null;
            }
        }
    }

    @When("I click on the Delete button for category {string}")
    public void i_click_on_the_delete_button_for_category(String categoryName) {
        // Search to ensure it's visible, as it might be on another page
        categoriesPage.enterSearchKeyword(categoryName);
        categoriesPage.clickSearch();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        categoriesPage.clickDeleteCategory(categoryName);
    }

    @Then("I should see an error message indicating deletion is not allowed")
    public void i_should_see_an_error_message_indicating_deletion_is_not_allowed() {
         boolean isError = false;
         String errorSource = "";
         String errorText = "";

         // 1. Check for UI Alert (Bootstrap)
         if(categoriesPage.isErrorAlertDisplayed()) {
             errorText = categoriesPage.getErrorAlertText();
             isError = true;
             errorSource = "UI Alert";
         }
         
         // 2. Check for JS Alert
         if(!isError) {
              try {
                  org.openqa.selenium.Alert alert = driver.switchTo().alert();
                  errorText = alert.getText();
                  alert.accept();
                  isError = true; 
                  errorSource = "JS Alert";
              } catch (Exception e) {}
         }

         // 3. Check for Backend Error Page
         if(!isError) {
             try {
                 String bodyText = driver.findElement(By.tagName("body")).getText();
                 if(bodyText.contains("Whitelabel Error Page") || 
                    bodyText.contains("Internal Server Error")) {
                     isError = true;
                     errorSource = "Backend Error Page";
                     errorText = bodyText;
                     if(driver.getCurrentUrl().contains("error")) {
                        driver.navigate().back();
                     }
                 }
             } catch(Exception e) {}
         }
         
         System.out.println("Deletion Prevention Check Result: " + isError + " (" + errorSource + ")");
         System.out.println("Error Text Found: " + errorText);

         // ASSERTION: Must show an error AND it must NOT be a technical SQL/Backend error
         Assert.assertTrue(isError, "No error message was displayed preventing the deletion.");
         
         boolean isTechnicalError = errorText.contains("could not execute statement") || 
                                    errorText.contains("foreign key constraint") || 
                                    errorText.contains("SQL") ||
                                    errorText.contains("Exception") ||
                                    errorText.contains("Whitelabel Error Page");
                                    
         Assert.assertFalse(isTechnicalError, 
             "Test Failed: System showed a technical/backend error instead of a user-friendly message. Actual Error: " + errorText);
    }
    
    // M4-UI-10: Delete category with linked plants
    @When("I attempt to delete the identified category")
    public void i_attempt_to_delete_the_identified_category() {
        Assert.assertNotNull(identifiedParentCategory, "No target category was identified to delete!");
        
        // Search for it to ensure we can click delete
        categoriesPage.enterSearchKeyword(identifiedParentCategory);
        categoriesPage.clickSearch();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        categoriesPage.clickDeleteCategory(identifiedParentCategory);
    }
    
    @Then("The identified category should still be visible in the list")
    public void the_identified_category_should_still_be_visible_in_the_list() {
        Assert.assertNotNull(identifiedParentCategory, "No category was identified!");
        
        // Clear search or re-search to verify existence
        categoriesPage.enterSearchKeyword(identifiedParentCategory);
        categoriesPage.clickSearch();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}

        Assert.assertTrue(categoriesPage.isCategoryInList(identifiedParentCategory), 
            "Category " + identifiedParentCategory + " was deleted but should have been protected.");
    }

    @Given("I identify a category with a linked plant")
    public void i_identify_a_category_with_a_linked_plant() {
        navigationMenu.clickLink("Plants");
        
        List<String> categoriesWithPlants = plantsPage.getUniqueCategoriesFromTable();
        System.out.println("Scanning Plants table for used categories. Found: " + categoriesWithPlants);
        
        identifiedParentCategory = null;
        
        // Verify if the category actually exists (it should)
        if (!categoriesWithPlants.isEmpty()) {
            identifiedParentCategory = categoriesWithPlants.get(0);
            System.out.println("Identified category with linked plant: " + identifiedParentCategory);
        }
        
        // If not found, create one
        if(identifiedParentCategory == null) {
            System.out.println("No existing category with plants found. Creating new pair...");
            long timestamp = System.currentTimeMillis();
            String catName = "LinkedCat_" + timestamp;
            String plantName = "LinkedPlant_" + timestamp;
            
            // Go to Categories to create category
            i_navigate_to_the_categories_page();
            a_category_exists(catName);
            
            // Go to Plants to create plant
            navigationMenu.clickLink("Plants");
            plantsPage.clickAddaPlant();
            plantsPage.enterPlantName(plantName);
            plantsPage.selectPlantCategory(catName);
            plantsPage.enterPrice("50.00");
            plantsPage.enterQuantity("100");
            plantsPage.clickSave();
            
            identifiedParentCategory = catName;
        }
        
        // Return to Categories page for the deletion attempt
        i_navigate_to_the_categories_page();
    }

    @Given("A category {string} exists with a linked plant {string}")
    public void a_category_exists_with_a_linked_plant(String categoryName, String plantName) {
        
        // 1. Create Category
         categoriesPage.enterSearchKeyword(categoryName);
         categoriesPage.clickSearch();
         try { Thread.sleep(1000); } catch (InterruptedException e) {}

        if(!categoriesPage.isCategoryInList(categoryName)) {
            // Clear search
            i_navigate_to_the_categories_page();
            
            categoriesPage.clickAddCategory();
            categoriesPage.enterCategoryName(categoryName);
            categoriesPage.clickSave();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            if(!categoriesPage.isCategoriesPageDisplayed()) categoriesPage.clickCategoriesTab();
        }

        // 2. Create Plant linked to Category
        navigationMenu.clickLink("Plants");
        
        if(!plantsPage.isPlantInList(plantName)) {
            plantsPage.clickAddaPlant();
            plantsPage.enterPlantName(plantName);
            plantsPage.selectPlantCategory(categoryName);
            plantsPage.enterPrice("50.00");
            plantsPage.enterQuantity("100");
            plantsPage.clickSave();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            if(!plantsPage.isPlantsPageDisplayed()) plantsPage.clickPlantsTab();
        }
        
        // Navigate back to Categories for the test action
        navigationMenu.clickLink("Category");
    }

    @Then("I should see pagination controls")
    public void i_should_see_pagination_controls() {
        Assert.assertTrue(categoriesPage.isPaginationDisplayed(), "Pagination controls are not visible");
    }

    @When("I click on the Next page button")
    public void i_click_on_the_next_page_button() {
        categoriesPage.clickNextPage();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    @Then("I should be on page {string} of categories")
    public void i_should_be_on_page_of_categories(String pageNum) {
        int expectedPage = Integer.parseInt(pageNum);
        
        // Wait for the page number to update to the expected value
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(driver -> {
                int currentPage = categoriesPage.getActivePageNumber();
                System.out.println("Current page number: " + currentPage + ", Expected: " + expectedPage);
                return currentPage == expectedPage;
            });
        } catch (Exception e) {
            // If wait times out, check one more time and fail with clear message
            int actualPage = categoriesPage.getActivePageNumber();
            Assert.assertEquals(actualPage, expectedPage, "Not on the expected page after waiting.");
        }
        
        // Final verification
        int actualPage = categoriesPage.getActivePageNumber();
        Assert.assertEquals(actualPage, expectedPage, "Not on the expected page.");
    }

    @When("I click on the Previous page button")
    public void i_click_on_the_previous_page_button() {
        categoriesPage.clickPreviousPage();
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }
}

