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

public class CategoriesPage {
    WebDriver driver;

    // Sidebar Link
    @FindBy(xpath = "//a[contains(@href, '/ui/categories')]")
    WebElement categoriesSidebarLink;

    // Add Button
    @FindBy(xpath = "//a[contains(@href, '/ui/categories/add')]")
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

    // Table
    By categoryRowsLocator = By.xpath("//tbody/tr");

    public CategoriesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickCategoriesTab() {
        categoriesSidebarLink.click();
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
            System.out.println("Could not select by text: " + parentName + ". Trying value...");
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

    // Error Feedback
    @FindBy(className = "invalid-feedback")
    WebElement invalidFeedback;

    public String getInvalidFeedbackText() {
        return invalidFeedback.getText();
    }
}
