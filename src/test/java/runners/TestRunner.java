package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features", glue = "stepdefinitions", plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-html-report.html",
        "json:target/cucumber-reports/cucumber.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
}, tags = "@M1-UI-01 or @M1-UI-02 or @M1-UI-03 or @M1-UI-04 or @M1-UI-05 or @M1-UI-06 or @M1-UI-07 or @M1-UI-08 or @M1-UI-09 or @M1-UI-10 or @M1-API-01 or @M1-API-02 or @M1-API-03 or @M1-API-04 or @M1-API-05 or @M1-API-06 or @M1-API-07 or @M1-API-08 or @M1-API-09 or @M1-API-10", monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}