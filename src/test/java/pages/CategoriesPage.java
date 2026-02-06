package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class CategoriesPage {
    WebDriver driver;

    // Sidebar Link
    @FindBy(xpath = "//a[contains(@href, '/ui/categories')]")
    WebElement categoriesSidebarLink;

    // Add Button
    @FindBy(xpath = "//a[contains(@href, '/ui/categories/add') and contains(text(), 'Add A Category')]")
    WebElement addaCategoryBtn;

    // Add a category Button
    @FindBy(xpath = "//a[contains(@href, '/ui/categories/add') and contains(text(), 'Add Category')]")
    WebElement addCategoryBtn;

    // Add/Edit Form Elements
    @FindBy(name = "name")
    WebElement nameInput;

    @FindBy(tagName = "select")
    WebElement parentCategorySelect;

    @FindBy(xpath = "//button[text()='Save']")
    WebElement saveBtn;

    // Success Message - Guessing locator based on common frameworks
    @FindBy(xpath = "//div[contains(@class, 'alert-success')]")
    WebElement successAlert;
    
    // Error Alert
    @FindBy(xpath = "//div[contains(@class, 'alert-danger')]")
    WebElement errorAlert;

    // Sorting Headers
    @FindBy(xpath = "//a[contains(@href, 'sortField=id')]")
    WebElement idHeader;

    @FindBy(xpath = "//a[contains(@href, 'sortField=name')]")
    WebElement nameHeader;
    
    @FindBy(xpath = "//a[contains(@href, 'sortField=parent.name')]")
    WebElement parentCategoryHeader;

    // Table
    By categoryRowsLocator = By.xpath("//tbody/tr");

    public CategoriesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickSortById() {
       idHeader.click();
    }

    public void clickSortByName() {
       nameHeader.click();
    }

    public void clickSortByParentCategory() {
       parentCategoryHeader.click();
    }

    public List<WebElement> getTableRows() {
        return driver.findElements(categoryRowsLocator);
    }

    public void clickCategoriesTab() {

        categoriesSidebarLink.click();
    }

    public void clickAddaCategory() {
        addaCategoryBtn.click();
    }

    public void clickAddCategory() {
        addCategoryBtn.click();
    }

    public void enterCategoryName(String name) {
        nameInput.clear();
        nameInput.sendKeys(name);
    }

    public void selectParentCategory(String parentName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(parentCategorySelect));

        Select select = new Select(parentCategorySelect);
        try {
            select.selectByVisibleText(parentName);
        } catch (Exception e) {
            System.out.println("Could not select by text: " + parentName + ". Trying partial match...");
            boolean found = false;
            for(WebElement option : select.getOptions()) {
                if(option.getText().trim().contains(parentName)) {
                    select.selectByVisibleText(option.getText());
                    found = true;
                    break;
                }
            }
            if(!found) {
                System.out.println("Failed to select parent category: " + parentName);
                // Optionally throw to fail test setup
                // throw new RuntimeException("Parent category not found in dropdown: " + parentName);
            }
        }
    }

    public void clickSave() {
        saveBtn.click();
    }

    public boolean isCategoryInList(String categoryName, String parentCategoryName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(categoryRowsLocator));

        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> rows = driver.findElements(categoryRowsLocator);
                for (WebElement row : rows) {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() > 2) {
                        // Check 2nd column (Name)
                        boolean nameMatch = cells.get(1).getText().toLowerCase().contains(categoryName.toLowerCase());

                        if (nameMatch) {
                            if (parentCategoryName != null && !parentCategoryName.isEmpty()) {
                                // Check 3rd column (Parent Category)
                                String actualParent = cells.get(2).getText().trim();
                                if (actualParent.equalsIgnoreCase(parentCategoryName)) {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        }
                    }
                }
                return false;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        return false;
    }

    public boolean isCategoryInList(String categoryName) {
        return isCategoryInList(categoryName, null);
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return successAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isErrorAlertDisplayed() {
        try {
            return errorAlert.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorAlertText() {
        try {
            return errorAlert.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Error Feedback
    @FindBy(className = "invalid-feedback")
    WebElement invalidFeedback;

    public String getInvalidFeedbackText() {
        return invalidFeedback.getText();
    }

    @FindBy(xpath = "//a[text()='Cancel']")
    WebElement cancelBtn;

    public void clickCancel() {
        cancelBtn.click();
    }

    @FindBy(xpath = "//form[contains(@action, '/edit/')]//a[text()='Cancel']")
    WebElement editCancelBtn;

    public void clickEditCancel() {
        editCancelBtn.click();
    }

    public boolean isCategoriesPageDisplayed() {
        return driver.getCurrentUrl().contains("/ui/categories") && !driver.getCurrentUrl().contains("/add");
    }

    @FindBy(className = "pagination")
    WebElement pagination;

    @FindBy(xpath = "//a[contains(text(), 'Next')]")
    WebElement nextPageBtn;

    @FindBy(xpath = "//a[contains(text(), 'Previous')]")
    WebElement previousPageBtn;

    @FindBy(xpath = "//li[contains(@class, 'page-item active')]//a")
    WebElement activePageNumber;

    public boolean isPaginationDisplayed() {
        try {
            return pagination.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickNextPage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(nextPageBtn));
            nextPageBtn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // Force click with JS if intercepted (e.g. by overlay or footer)
            ((org.openqa.selenium.JavascriptExecutor)driver).executeScript("arguments[0].click();", nextPageBtn);
        }
    }

    public void clickPreviousPage() {
        try {
             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
             wait.until(ExpectedConditions.elementToBeClickable(previousPageBtn));
             previousPageBtn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
             ((org.openqa.selenium.JavascriptExecutor)driver).executeScript("arguments[0].click();", previousPageBtn);
        }
    }

    public int getActivePageNumber() {
        try {
            return Integer.parseInt(activePageNumber.getText().trim());
        } catch (Exception e) {
            return 1;
        }
    }

    // Search
    @FindBy(name = "name")
    WebElement searchInput;

    @FindBy(xpath = "//button[text()='Search']")
    WebElement searchBtn;

    public void enterSearchKeyword(String keyword) {
        searchInput.clear();
        searchInput.sendKeys(keyword);
    }

    public void clickSearch() {
        searchBtn.click();
    }

    public boolean isAddCategoryButtonDisplayed() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            List<WebElement> userButtons = driver.findElements(By.xpath("//a[contains(@href, '/ui/categories/add')]"));
            return !userButtons.isEmpty() && userButtons.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        } finally {
            // Restore implicit wait (assuming default is 10)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public boolean isAnyDeleteButtonDisplayed() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            // Look for any delete button. Specifically check for the button inside the form
            // as usually users have permission or not.
            // Based on HTML provided: <button class="btn btn-sm btn-outline-danger"
            // title="Delete">
            List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[@title='Delete']"));

            // If buttons exist, check if any is displayed and enabled
            if (!deleteButtons.isEmpty()) {
                WebElement firstBtn = deleteButtons.get(0);
                return firstBtn.isDisplayed() && firstBtn.isEnabled();
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }


    public void clickEditCategory(String categoryName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(categoryRowsLocator));
        
        List<WebElement> rows = driver.findElements(categoryRowsLocator);
        boolean found = false;
        for(WebElement row : rows) {
            // Check if row contains the category name (in the designated column preferably, or just anywhere)
            if(row.getText().contains(categoryName)) {
                List<WebElement> userLinks = row.findElements(By.tagName("a"));
                for(WebElement link : userLinks) {
                     String href = link.getAttribute("href");
                     String title = link.getAttribute("title");
                     String text = link.getText();
                     
                     // Robust check for Edit button
                     if((href != null && href.contains("edit")) || 
                        (title != null && title.toLowerCase().contains("edit")) ||
                        (text != null && text.toLowerCase().contains("edit"))) { 
                          link.click();
                          found = true;
                          break;
                     }
                }
                if(found) break;
            }
        }
        if(!found) {
             throw new RuntimeException("Category not found for editing: " + categoryName + ". Available rows: " + rows.size());
        }
    }

    public List<String> getUniqueParentNamesFromTable() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(categoryRowsLocator));
        } catch (Exception e) {
            return new ArrayList<>(); // Return empty if no table (e.g. no results)
        }
        
        List<WebElement> rows = driver.findElements(categoryRowsLocator);
        List<String> parents = new ArrayList<>();
        
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            // Assuming 3rd column is Parent Category (Index 2)
            if (cells.size() > 2) {
                String parentName = cells.get(2).getText().trim();
                // Check if valid parent name (not empty or dash)
                if (!parentName.isEmpty() && !parentName.equals("-") && !parents.contains(parentName)) {
                    parents.add(parentName);
                }
            }
        }
        return parents;
    }

    public void clickDeleteCategory(String categoryName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(categoryRowsLocator));
        
        List<WebElement> rows = driver.findElements(categoryRowsLocator);
        boolean found = false;
        for(WebElement row : rows) {
            if(row.getText().contains(categoryName)) {
                // Find delete button - usually a button or link with delete text/icon
                List<WebElement> buttons = row.findElements(By.xpath(".//button | .//a"));
                for(WebElement btn : buttons) {
                     String text = btn.getText().toLowerCase();
                     String title = btn.getAttribute("title");
                     String clazz = btn.getAttribute("class");
                     
                     if((text != null && (text.contains("delete") || text.contains("remove"))) ||
                        (title != null && title.toLowerCase().contains("delete")) ||
                        (clazz != null && clazz.contains("danger"))) { // Danger usually means delete
                         
                         btn.click();
                         found = true;
                         
                         // Handle potential JS confirmation
                         try {
                              if(wait.until(ExpectedConditions.alertIsPresent()) != null) {
                                   // do nothing here, let the test step handle validation or accept
                                   // or we can't assert the error message if we auto accept too fast?
                                   // Actually UI steps usually handle alerts if they are the 'result'.
                                   // But here if it's a confirmation "Are you sure?", we must accept to PROCEED to the deletion attempt.
                                   driver.switchTo().alert().accept();
                              }
                         } catch (Exception e) {}
                         
                         break;
                     }
                }
                if(found) break;
            }
        }
        if(!found) {
             // Debug info
             List<String> rowTexts = new ArrayList<>();
             for(WebElement r : rows) rowTexts.add(r.getText());
             throw new RuntimeException("Category delete button not found for: " + categoryName + ". Visible rows: " + rowTexts);
        }
    }
}
