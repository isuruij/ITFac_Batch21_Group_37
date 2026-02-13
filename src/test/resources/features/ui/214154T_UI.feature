
Feature: Plant Management UI Tests for 214154T

  # Test Case: M1-UI-01
  @M1-UI-01 @UI
  Scenario: Verify that Admin can search Plants by Name
    Given Admin is logged into the system
    And Existing plants are already added in the system
    When Navigate to Plants tab using the side bar
    And Enter plant name in the search input box
    And Click Search button
    Then Show the plant list filtered by entered name

  # Test Case: M1-UI-02
  @M1-UI-02 @UI
  Scenario: Verify Admin can filter plants by Category
    Given Admin is logged into the system
    And Existing plants are already added in the system
    And Existing categories are already added in the system
    When Navigate to Plants tab using the side bar
    And Select the needed category from the category selector
    And Click Search button
    Then Show the plant list filtered by the selected category

  # Test Case: M1-UI-03
  @M1-UI-03 @UI
  Scenario: Verify that Admin can sort plants by Quantity
    Given Admin is logged into the system
    And Existing plants are already added in the system
    When Navigate to Plants tab using the side bar
    And Click Quantity column header to sort by quantity
    Then Show the sorted plants list by quantity


