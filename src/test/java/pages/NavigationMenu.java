package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class NavigationMenu {
    WebDriver driver;

    // Navigation Links
    @FindBy(xpath = "//a[contains(@href, '/ui/dashboard')]")
    WebElement dashboardLink;

    @FindBy(xpath = "//a[contains(@href, '/ui/categories')]")
    WebElement categoriesLink;

    @FindBy(xpath = "//a[contains(@href, '/ui/plants')]")
    WebElement plantsLink;

    @FindBy(xpath = "//a[contains(@href, '/ui/sales')]")
    WebElement salesLink;

    @FindBy(xpath = "//a[contains(text(), 'Inventory')]")
    WebElement inventoryLink;

    public NavigationMenu(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isLinkVisible(String linkName) {
        WebElement link = getLinkElement(linkName);
        return link != null && link.isDisplayed();
    }

    public void clickLink(String linkName) {
        WebElement link = getLinkElement(linkName);
        if (link != null) {
            link.click();
        } else {
            throw new RuntimeException("Link not found: " + linkName);
        }
    }

    public boolean isLinkActive(String linkName) {
        WebElement link = getLinkElement(linkName);
        if (link != null) {
            String classes = link.getAttribute("class");
            return classes != null && classes.contains("active");
        }
        return false;
    }

    public String getLinkClasses(String linkName) {
        WebElement link = getLinkElement(linkName);
        return (link != null) ? link.getAttribute("class") : "Link not found";
    }

    private WebElement getLinkElement(String linkName) {
        switch (linkName) {
            case "Dashboard":
                return dashboardLink;
            case "Category":
                return categoriesLink;
            case "Plants":
                return plantsLink;
            case "Sales":
                return salesLink;
            case "Inventory":
                return inventoryLink;
            default:
                return null;
        }
    }
}
