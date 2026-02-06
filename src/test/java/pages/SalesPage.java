package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SalesPage {
    WebDriver driver;

    // Sidebar Link
    @FindBy(xpath = "//a[contains(@href, '/ui/sales')]")
    WebElement salesSidebarLink;

    // Sell Plant Button
    @FindBy(xpath = "//a[contains(@href, '/ui/sales/new')]")
    WebElement sellPlantButton;

    // Delete Button (First one found)
    @FindBy(xpath = "//button[contains(@class, 'btn-outline-danger')]")
    WebElement deleteButton;

    public SalesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickSalesTab() {
        salesSidebarLink.click();
    }

    public boolean isSellPlantButtonVisible() {
        try {
            return sellPlantButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSellPlantButton() {
        sellPlantButton.click();
    }

    public boolean isDeleteButtonVisible() {
        try {
            return deleteButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDeleteButton() {
        deleteButton.click();
    }

    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }
}
