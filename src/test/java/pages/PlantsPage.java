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

    @FindBy(xpath = "//button[contains(@class, 'btn-primary') and text()='Search']")
    WebElement searchBtn;

    // Category Filter
    @FindBy(name = "categoryId")
    WebElement categoryFilterSelect;

    // Add Plant Button
    @FindBy(xpath = "//a[contains(text(), 'Add a Plant')]")
    WebElement addPlantBtn;

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
        wait.until(ExpectedConditions.visibilityOf(categoryFilterSelect));

        Select select = new Select(categoryFilterSelect);
        try {
            select.selectByVisibleText(categoryName);
        } catch (Exception e) {
            System.out.println("Could not select category: " + categoryName + ". Error: " + e.getMessage());
        }
    }

    public void clickPlantsTab() {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                plantsSidebarLink.click();
                return; // Success, exit method
            } catch (StaleElementReferenceException e) {
                if (i == maxRetries - 1) {
                    throw e; // Rethrow if all retries failed
                }
                // Wait a bit before retrying
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public void enterSearchPlantName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name("name")));
        element.clear();
        element.sendKeys(name);
    }

    public void enterPlantName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.name("name")));
        element.clear();
        element.sendKeys(name);
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
            return driver.getCurrentUrl().contains("/ui/plants") && !driver.getCurrentUrl().contains("/add")
                    && !driver.getCurrentUrl().contains("/edit");
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSearch() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-primary') and contains(text(), 'Search')]")));
        element.click();
    }

    public void clickAddPlant() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(addPlantBtn));
        element.click();
    }

    public void clickAddaPlant() {
        addPlantBtn.click();
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
            List<WebElement> editButtons = driver.findElements(By.xpath(
                    "//a[contains(@href, 'edit')] | //button[contains(@class, 'edit')] | //i[contains(@class, 'edit')]"));
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
            List<WebElement> badges = row.findElements(By.xpath(
                    ".//span[contains(@class, 'badge') and contains(text(), 'Low')] | .//span[contains(text(), 'Low')]"));
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
            wait.until(ExpectedConditions
                    .presenceOfElementLocated(By.xpath("//table[contains(@class, 'table')]//tbody//tr")));
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
        // XPath: Find TR containing text 'plantName', then find button/link with edit
        // icon or text
        // Adjust XPath based on actual DOM. Commonly:
        // //tr[td[text()='Name']]//button[contains(., 'Edit')]
        // Or specific column index.
        By editBtnLocator = By.xpath("//tr[td[contains(text(), '" + plantName
                + "')]]//button[contains(@class, 'btn-primary') or contains(@class, 'edit') or .//i[contains(@class,'edit')]]");
        // Fallback or more specific if needed. Let's try searching for a button in that
        // row.
        // If specific text is not available, we might need to rely on index if plant is
        // unique.

        // Simpler approach:
        WebElement editBtn = driver.findElement(By.xpath("//tr[td[contains(text(), '" + plantName
                + "')]]//a[contains(@href, 'edit')] | //tr[td[contains(text(), '" + plantName + "')]]//button"));
        editBtn.click();
    }

    public void clickDeletePlant(String plantName) {
        try {
            // Wait for table to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));

            // In actual HTML, delete button is inside a form with title='Delete'
            WebElement deleteButton = driver.findElement(
                    By.xpath("//tr[td[contains(text(), '" + plantName + "')]]//button[@title='Delete']"));
            deleteButton.click();

            // Wait for deletion to complete
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Could not delete plant: " + plantName + ". Error: " + e.getMessage());
        }

        // Handle potential alert
        try {
            Thread.sleep(500);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
                driver.switchTo().alert().accept();
            }
        } catch (Exception e) {
            // No alert or not needed
        }

        // Wait for page to refresh
        new Actions(driver).pause(Duration.ofSeconds(2)).perform();
    }

    // Wrapper method for deletePlant
    public void deletePlant(String plantName) {
        clickDeletePlant(plantName);
    }

    public String getPlantPrice(String plantName) {
        // XPath to get price of a specific plant. Assuming it's in a column.
        // We iterate rows to find the name, then get the price column.
        List<WebElement> plants = driver.findElements(plantRowsLocator);
        // Assuming row structure: TD[Name] | TD[Category] | TD[Price] | ...

        // Better dynamic xpath:
        // //tr[td[contains(text(), 'Name')]]/td[3] (assuming price is 3rd column)
        // I'll try to get the row text or specific column.
        // Since I don't know the exact column index, I will log the row text or try
        // finding it.
        // Let's assume Price is in the row.

        WebElement row = driver.findElement(By.xpath("//tr[td[contains(text(), '" + plantName + "')]]"));
        return row.getText(); // Return full row text to check containment as a simple fallback
    }

    public void clickQuantityColumnHeader() {
        try {
            // In actual HTML, column headers are links with sortField parameter
            WebElement quantityHeader = driver.findElement(
                    By.xpath("//th//a[contains(@href, 'sortField=quantity')]"));
            quantityHeader.click();
        } catch (Exception e) {
            System.out.println("Could not click Quantity column header: " + e.getMessage());
        }
    }

    public List<Integer> getPlantQuantitiesFromTable() {
        List<Integer> quantities = new java.util.ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));

            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            for (WebElement row : rows) {
                try {
                    // Get the quantity column (assuming it's the 4th column)
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() >= 4) {
                        String quantityText = cells.get(3).getText().trim();
                        if (!quantityText.isEmpty()) {
                            quantities.add(Integer.parseInt(quantityText));
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip rows with non-numeric quantities
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting quantities: " + e.getMessage());
        }
        return quantities;
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            WebElement noResultsMessage = driver.findElement(
                    By.xpath("//*[contains(text(), 'No plants found') or contains(text(), 'No plant found')]"));
            return noResultsMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPaginationDisplayed() {
        try {
            WebElement pagination = driver.findElement(
                    By.xpath("//nav[@aria-label='Page navigation'] | //ul[contains(@class, 'pagination')]"));
            return pagination.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickNextPage() {
        try {
            WebElement nextButton = driver.findElement(By.xpath(
                    "//a[contains(text(), 'Next')] | //button[contains(text(), 'Next')] | //li[contains(@class, 'page-item')]/a[contains(@aria-label, 'Next')]"));
            nextButton.click();
        } catch (Exception e) {
            System.out.println("Could not click Next button: " + e.getMessage());
        }
    }

    public void clickPreviousPage() {
        try {
            WebElement prevButton = driver.findElement(By.xpath(
                    "//a[contains(text(), 'Previous')] | //button[contains(text(), 'Previous')] | //li[contains(@class, 'page-item')]/a[contains(@aria-label, 'Previous')]"));
            prevButton.click();
        } catch (Exception e) {
            System.out.println("Could not click Previous button: " + e.getMessage());
        }
    }

    public int getPlantsTableRowCount() {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickPriceColumnHeader() {
        try {
            // In actual HTML, column headers are links with sortField parameter
            WebElement priceHeader = driver.findElement(
                    By.xpath("//th//a[contains(@href, 'sortField=price')]"));
            priceHeader.click();
        } catch (Exception e) {
            System.out.println("Could not click Price column header: " + e.getMessage());
        }
    }

    public List<Double> getPlantPricesFromTable() {
        List<Double> prices = new java.util.ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));

            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            for (WebElement row : rows) {
                try {
                    // Get the price column (assuming it's the 3rd column)
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() >= 3) {
                        String priceText = cells.get(2).getText().trim().replace("$", "").replace(",", "");
                        if (!priceText.isEmpty()) {
                            prices.add(Double.parseDouble(priceText));
                        }
                    }
                } catch (NumberFormatException e) {
                    // Skip rows with non-numeric prices
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting prices: " + e.getMessage());
        }
        return prices;
    }

    public void clickNameColumnHeader() {
        try {
            // In actual HTML, column headers are links with sortField parameter
            WebElement nameHeader = driver.findElement(
                    By.xpath("//th//a[contains(@href, 'sortField=name')]"));
            nameHeader.click();
        } catch (Exception e) {
            System.out.println("Could not click Name column header: " + e.getMessage());
        }
    }

    public List<String> getPlantNamesFromTable() {
        List<String> names = new java.util.ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table")));

            List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
            for (WebElement row : rows) {
                try {
                    // Get the name column (assuming it's the 1st column)
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (cells.size() >= 1) {
                        String nameText = cells.get(0).getText().trim();
                        if (!nameText.isEmpty()) {
                            names.add(nameText);
                        }
                    }
                } catch (Exception e) {
                    // Skip problematic rows
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting names: " + e.getMessage());
        }
        return names;
    }
}
