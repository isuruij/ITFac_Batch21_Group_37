# Automation Framework Assignment - Group 01

This project implements a unified test automation framework using Cucumber, Selenium, and Rest Assured.

## Tech Stack
- **UI Automation**: Selenium WebDriver
- **API Automation**: Rest Assured
- **BDD Framework**: Cucumber (Gherkin)
- **Test Runner**: TestNG
- **Reporting**: Allure
- **Language**: Java 11+
- **Build Tool**: Maven

## Project Structure
- `src/test/java/pages`: logic for Page Object Model
- `src/test/java/stepdefinitions`: Cucumber glue code (UI & API)
- `src/test/java/utils`: Helper classes (DriverFactory, APIUtils)
- `src/test/resources/features`: BDD scenarios

## How to Run
### Command Line
```bash
mvn clean test
```

### View Reports
```bash
allure serve target/allure-results
```
