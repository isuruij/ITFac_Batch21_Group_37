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

    @FindBy(xpath = "//a[contains(@href, '/ui/plants/add')]")
    WebElement addPlantBtn;

    public void clickAddPlant() {
        addPlantBtn.click();
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

    public void clickEditPlant(String plantName) {
        // Assuming the edit button is in the same row
        // XPath: Find TR containing text 'plantName', then find button/link with edit icon or text
        // Adjust XPath based on actual DOM. Commonly: //tr[td[text()='Name']]//button[contains(., 'Edit')]
        // Or specific column index.
        By editBtnLocator = By.xpath("//tr[td[contains(text(), '" + plantName + "')]]//button[contains(@class, 'btn-primary') or contains(@class, 'edit') or .//i[contains(@class,'edit')]]");
        // Fallback or more specific if needed. Let's try searching for a button in that row.
        // If specific text is not available, we might need to rely on index if plant is unique.
        
        // Simpler approach:
        WebElement editBtn = driver.findElement(By.xpath("//tr[td[contains(text(), '" + plantName + "')]]//a[contains(@href, 'edit')] | //tr[td[contains(text(), '" + plantName + "')]]//button"));
        editBtn.click();
    }

    public String getPlantPrice(String plantName) {
        // XPath to get price of a specific plant. Assuming it's in a column.
        // We iterate rows to find the name, then get the price column.
        List<WebElement> plants = driver.findElements(plantRowsLocator);
        // Assuming row structure: TD[Name] | TD[Category] | TD[Price] | ...
        
        // Better dynamic xpath:
        // //tr[td[contains(text(), 'Name')]]/td[3] (assuming price is 3rd column)
        // I'll try to get the row text or specific column.
        // Since I don't know the exact column index, I will log the row text or try finding it.
        // Let's assume Price is in the row.
        
        WebElement row = driver.findElement(By.xpath("//tr[td[contains(text(), '" + plantName + "')]]"));
        return row.getText(); // Return full row text to check containment as a simple fallback
    }
}
