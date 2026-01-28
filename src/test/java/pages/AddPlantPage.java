package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddPlantPage {
    WebDriver driver;

    @FindBy(name = "name")
    WebElement plantNameInput;

    @FindBy(xpath = "//select") 
    WebElement categoryDropdown;

    @FindBy(name = "price")
    WebElement priceInput;

    @FindBy(name = "quantity")
    WebElement quantityInput;

    @FindBy(xpath = "//button[text()='Save']")
    WebElement saveBtn;

    public AddPlantPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterPlantName(String name) {
        plantNameInput.clear();
        plantNameInput.sendKeys(name);
    }

    public void selectCategory(String category) {
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(category);
    }

    public void enterPrice(String price) {
        priceInput.clear();
        priceInput.sendKeys(price);
    }

    public void enterQuantity(String quantity) {
        quantityInput.clear();
        quantityInput.sendKeys(quantity);
    }

    public void clickSave() {
        saveBtn.click();
    }
}
