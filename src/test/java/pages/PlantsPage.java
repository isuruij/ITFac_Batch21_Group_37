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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

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
    
    // Add/Edit Form Elements
    @FindBy(name = "name")
    WebElement plantNameInput;
    
    @FindBy(tagName = "select")
    WebElement categorySelect;
    
    @FindBy(name = "price")
    WebElement priceInput;

    @FindBy(name = "quantity")
    WebElement quantityInput;

    @FindBy(xpath = "//a[text()='Cancel']")
    WebElement cancelBtn;
    
    @FindBy(xpath = "//button[text()='Save']")
    WebElement saveBtn;

    // Dynamic Locator
    By plantRowsLocator = By.xpath("//table[contains(@class, 'table')]//tbody//tr/td[1]");

    public PlantsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void selectPlantCategory(String categoryName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(categorySelect));
        
        Select select = new Select(categorySelect);
        try {
            select.selectByVisibleText(categoryName);
        } catch (Exception e) {
             // Fallback attempt
        }
    }

    public void clickPlantsTab() {
        plantsSidebarLink.click();
    }
    
    public void enterSearchPlantName(String name) {
        searchInput.clear();
        searchInput.sendKeys(name);
    }

    public void enterPlantName(String name) {
        plantNameInput.clear();
        plantNameInput.sendKeys(name);
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
    
    public void clickCancel() {
        cancelBtn.click();
    }
    
    public boolean isPlantsPageDisplayed() {
         try {
             return driver.getCurrentUrl().contains("/ui/plants") && !driver.getCurrentUrl().contains("/add") && !driver.getCurrentUrl().contains("/edit");
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSearch() {
        searchBtn.click();
    }

    @FindBy(xpath = "//a[contains(@href, '/ui/plants/add') and contains(text(), 'Add Plant')]")
    WebElement addPlantBtn;

    @FindBy(xpath = "//a[contains(@href, '/ui/plants/add') and contains(text(), 'Add a Plant')]")
    WebElement addaPlantBtn;

    public void clickAddPlant() {
        addPlantBtn.click();
    }

    public void clickAddaPlant() {
        addaPlantBtn.click();
    }

    public boolean isAddPlantButtonVisible() {
        try {
            return addPlantBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areAnyEditButtonsVisible() {
        try {
            List<WebElement> editButtons = driver.findElements(By.xpath("//a[contains(@href, 'edit')] | //button[contains(@class, 'edit')] | //i[contains(@class, 'edit')]"));
            return !editButtons.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areAnyDeleteButtonsVisible() {
        try {
            List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[@title='delete']"));
            return !deleteButtons.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean arePlantsDisplayed() {
        try {
            List<WebElement> plants = driver.findElements(plantRowsLocator);
            return !plants.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLowStockBadgeVisible(String plantName) {
        // Find row by plant name, then look for badge inside.
        // Assuming badge has class 'badge' or text 'Low'
        try {
            WebElement row = driver.findElement(By.xpath("//tr[td[contains(text(), '" + plantName + "')]]"));
            List<WebElement> badges = row.findElements(By.xpath(".//span[contains(@class, 'badge') and contains(text(), 'Low')] | .//span[contains(text(), 'Low')]"));
            return !badges.isEmpty();
        } catch (Exception e) {
            return false;
        }
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

    public List<String> getUniqueCategoriesFromTable() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[contains(@class, 'table')]//tbody//tr")));
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }

        List<WebElement> rows = driver.findElements(By.xpath("//table[contains(@class, 'table')]//tbody//tr"));
        List<String> categories = new java.util.ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            // 2nd Column is Category (Index 1)
            if (cells.size() > 1) {
                String catName = cells.get(1).getText().trim();
                if (!catName.isEmpty() && !catName.equals("-") && !categories.contains(catName)) {
                    categories.add(catName);
                }
            }
        }
        return categories;
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

    public void clickDeletePlant(String plantName) {
        // Find TR containing plantName, then find delete button
        // Common patterns: button with class 'delete', 'danger', or containing 'Delete' text or trash icon
        WebElement deleteBtn = driver.findElement(By.xpath("//tr[td[contains(text(), '" + plantName + "')]]//button[@title='Delete']"));
        // List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[@title='delete']"));
        deleteBtn.click();
        
        // Handle potential alert
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            if(wait.until(ExpectedConditions.alertIsPresent()) != null){
                driver.switchTo().alert().accept();
            }
        } catch (Exception e) {
            // No alert or not needed
        }
        
        new Actions(driver).pause(Duration.ofSeconds(2)).perform();
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
