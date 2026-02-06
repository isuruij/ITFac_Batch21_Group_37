package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {
    WebDriver driver;

    // Categories Card
    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Categories')]]")
    WebElement categoriesCard;

    // Plants Card
    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Plants')]]")
    WebElement plantsCard;

    // Sales Card
    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Sales')]]")
    WebElement salesCard;

    // Inventory Card
    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Inventory')]]")
    WebElement inventoryCard;

    // Counts
    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Categories')]]//div[contains(text(),'Main')]/preceding-sibling::div")
    WebElement mainCategoryCount;

    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Categories')]]//div[contains(text(),'Sub')]/preceding-sibling::div")
    WebElement subCategoryCount;

    @FindBy(xpath = "//div[contains(@class,'card')][.//h6[contains(text(),'Plants')]]//div[contains(text(),'Total')]/preceding-sibling::div")
    WebElement totalPlantCount;
    
    // Dashboard Header or verification element
    // Assuming there might be a dashboard title, but relying on cards for now.

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isCategoriesCardDisplayed() {
        return categoriesCard.isDisplayed();
    }

    public boolean isPlantsCardDisplayed() {
        return plantsCard.isDisplayed();
    }

    public boolean isSalesCardDisplayed() {
        return salesCard.isDisplayed();
    }

    public boolean isInventoryCardDisplayed() {
        return inventoryCard.isDisplayed();
    }

    public String getMainCategoryCount() {
        return mainCategoryCount.getText();
    }

    public String getSubCategoryCount() {
        return subCategoryCount.getText();
    }

    public String getTotalPlantCount() {
        return totalPlantCount.getText();
    }
}
