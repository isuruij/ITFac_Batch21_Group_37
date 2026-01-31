package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.DriverFactory;

public class Hooks {

    @Before("@UI")
    public void setUp() {
        DriverFactory.initDriver();
    }

    @After("@UI")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failed Screenshot");
        }
        DriverFactory.quitDriver();
    }

    @After("@M3-UI-01 or @M3-UI-02 or @M3-UI-03 or @M3-UI-04 or @M3-API-01 or @M3-API-02 or @M3-API-07 or @M3-API-08")
    public void tearDownAndCleanup() {
        // 1. Login to get token
        java.util.Map<String, String> credentials = new java.util.HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");
        
        io.restassured.response.Response loginResponse = utils.APIUtils.post("/api/auth/login", credentials, null);

        String token = loginResponse.jsonPath().getString("token");

        // 2. Search for the plant to get its ID
        if (token != null) {
            io.restassured.response.Response listResponse = utils.APIUtils.get("/api/plants", token);
            java.util.List<java.util.Map<String, Object>> plants = listResponse.jsonPath().getList(""); 
            // Note: Adjust "content" path if API returns array directly or different structure

            if (plants != null) {
                for (java.util.Map<String, Object> plant : plants) {
                    if (plant != null) {
                        String name = (String) plant.get("name");
                        if ("Rose".equals(name) || "Orchid".equals(name)) {
                            Integer plantId = (Integer) plant.get("id");
                            // 3. Delete the plant
                            utils.APIUtils.delete("/api/plants/" + plantId, token);
                            System.out.println("Cleaned up plant: " + name + " (ID: " + plantId + ")");
                        }
                    }
                }
            }
        }
    }
}
