
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
