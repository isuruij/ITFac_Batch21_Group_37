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

public class PlantsPage {
    WebDriver driver;

    // Sidebar Link
    @FindBy(xpath = "//a[contains(@href, '/ui/plants')]")
    WebElement plantsSidebarLink;

    // Search Form
    @FindBy(name = "name")
    WebElement searchInput;

    @FindBy(xpath = "//button[text()='Search']")
    WebElement searchBtn;

    // Category Selector
    @FindBy(name = "category")
    WebElement categoryDropdown;

    // Dynamic Locators
    By plantRowsLocator = By.xpath("//table[contains(@class, 'table')]//tbody//tr/td[1]");
    By plantCategoryLocator = By.xpath("//table[contains(@class, 'table')]//tbody//tr/td[3]");

    public PlantsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickPlantsTab() {
        plantsSidebarLink.click();
    }

    public void enterPlantName(String name) {
        searchInput.clear();
        searchInput.sendKeys(name);
    }

    public void clickSearch() {
        searchBtn.click();
    }

    public boolean isPlantInList(String plantName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for table to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(plantRowsLocator));

        // Retry logic for StaleElementReferenceException
        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> plants = driver.findElements(plantRowsLocator);
                for (WebElement element : plants) {
                    if (element.getText().toLowerCase().contains(plantName.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        return false;
    }

    public void selectCategory(String category) {
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(category);
    }

    public boolean isPlantListFilteredByCategory(String category) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for table to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(plantRowsLocator));

        // Retry logic for StaleElementReferenceException
        int attempts = 0;
        while (attempts < 3) {
            try {
                List<WebElement> categoryElements = driver.findElements(plantCategoryLocator);

                // If no plants found, return false
                if (categoryElements.isEmpty()) {
                    return false;
                }

                // Check if all plants have the selected category
                for (WebElement element : categoryElements) {
                    String plantCategory = element.getText().trim();
                    if (!plantCategory.equalsIgnoreCase(category)) {
                        return false; // Found a plant with different category
                    }
                }
                return true; // All plants match the category
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        return false;
    }
}
