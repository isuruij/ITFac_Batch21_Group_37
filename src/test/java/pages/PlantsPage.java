package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    // Dynamic Locator
    By plantRowsLocator = By.xpath("//table[contains(@class, 'table')]//tbody//tr/td[1]");

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
}
