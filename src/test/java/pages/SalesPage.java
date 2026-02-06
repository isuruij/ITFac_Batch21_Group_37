package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.Select;

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

    // Sell Plant Form Elements
    @FindBy(name = "plantId")
    WebElement plantDropdown;

    @FindBy(name = "quantity")
    WebElement quantityInput;

    @FindBy(xpath = "//button[contains(text(), 'Sell')]")
    WebElement sellButton;

    @FindBy(xpath = "//a[contains(text(), 'Cancel')]")
    WebElement cancelButton;

    @FindBy(xpath = "//form//*[contains(@class, 'text-danger')]")
    WebElement errorText;

    // Note: User's provided HTML doesn't explicitly show 'invalid-feedback'.
    // Usually standard validation is 'invalid-feedback' but sometimes explicit
    // text-danger divs.
    // The CSV says "Error message 'Quantity must be greater than 0' is shown".

    @FindBy(className = "invalid-feedback")
    WebElement invalidFeedback;

    @FindBy(xpath = "//ul[contains(@class, 'pagination')]")
    WebElement pagination;

    @FindBy(xpath = "//table/tbody/tr/td[4]")
    java.util.List<WebElement> soldDateCells;

    // Sort Headers
    @FindBy(css = "table thead tr th:nth-child(1) a")
    WebElement plantSortHeader;

    @FindBy(css = "table thead tr th:nth-child(2) a")
    WebElement quantitySortHeader;

    @FindBy(css = "table thead tr th:nth-child(3) a")
    WebElement totalPriceSortHeader;

    @FindBy(css = "table thead tr th:nth-child(4) a")
    WebElement soldAtSortHeader;

    // Column Data Helpers
    @FindBy(xpath = "//table/tbody/tr/td[1]")
    java.util.List<WebElement> plantNameCells;

    @FindBy(xpath = "//table/tbody/tr/td[2]")
    java.util.List<WebElement> quantityCells;

    @FindBy(xpath = "//table/tbody/tr/td[3]")
    java.util.List<WebElement> totalPriceCells;

    public SalesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isSellPlantButtonPresent() {
        return !driver.findElements(org.openqa.selenium.By.xpath("//a[contains(@href, '/ui/sales/new')]")).isEmpty();
    }

    public boolean isDeleteButtonPresent() {
        return !driver.findElements(org.openqa.selenium.By.xpath("//button[contains(@class, 'btn-outline-danger')]"))
                .isEmpty();
    }

    public void clickPlantSortHeader() {
        plantSortHeader.click();
    }

    public void clickQuantitySortHeader() {
        quantitySortHeader.click();
    }

    public void clickTotalPriceSortHeader() {
        totalPriceSortHeader.click();
    }

    public void clickSoldAtSortHeader() {
        soldAtSortHeader.click();
    }

    public java.util.List<String> getPlantNameList() {
        return plantNameCells.stream().map(WebElement::getText).collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Double> getQuantityList() {
        return quantityCells.stream()
                .map(e -> Double.parseDouble(e.getText().trim()))
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<String> getQuantityStringList() {
        return quantityCells.stream()
                .map(e -> e.getText().trim())
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Double> getTotalPriceList() {
        return totalPriceCells.stream()
                .map(e -> Double.parseDouble(e.getText().trim()))
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<String> getTotalPriceStringList() {
        return totalPriceCells.stream()
                .map(e -> e.getText().trim())
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean isPaginationDisplayed() {
        try {
            return pagination.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public java.util.List<String> getSoldDateList() {
        return soldDateCells.stream().map(WebElement::getText).collect(java.util.stream.Collectors.toList());
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

    public void selectPlantByIndex(int index) {
        Select select = new Select(plantDropdown);
        select.selectByIndex(index);
    }

    public void enterQuantity(String quantity) {
        quantityInput.clear();
        quantityInput.sendKeys(quantity);
    }

    public void clickSellButton() {
        sellButton.click();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }

    public String getErrorMessage() {
        // Try invalid-feedback first
        try {
            if (invalidFeedback.isDisplayed()) {
                return invalidFeedback.getText();
            }
        } catch (Exception e) {
        }

        // Try text-danger inside form
        try {
            if (errorText.isDisplayed()) {
                return errorText.getText();
            }
        } catch (Exception e) {
        }

        // Check HTML5 validation message on quantity input
        try {
            String valMsg = quantityInput.getAttribute("validationMessage");
            if (valMsg != null && !valMsg.isEmpty()) {
                return valMsg;
            }
        } catch (Exception e) {
        }

        return "";
    }
}
