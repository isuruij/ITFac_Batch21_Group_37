@UI
Feature: UI Tests (214154T)

  Background:
    Given Admin is logged into the system

  # Test Case: M1-UI-01
  @M1-UI-01 @UI
  Scenario: Verify that Admin can search Plants by Name
    When Navigate to Plants tab using the side bar
    And I click the Add a Plant button
    And Fill a plant name "Rose_214154T"
    And Select category "sri lankan"
    And Set price "1500"
    And Set quantity "50"
    And Click 'Save' button
    Then Plant "Rose_214154T" is added to the list
    When Enter plant name "Rose_214154T" in the search input box
    And Click Search button
    Then Show the plant list filtered by entered name "Rose_214154T"
    When Click delete icon on plant "Rose_214154T"
    Then Plant "Rose_214154T" is removed from the list

  # Test Case: M1-UI-02
  @M1-UI-02 @UI
  Scenario: Verify Admin can filter plants by Category
    When Navigate to Plants tab using the side bar
    And I click the Add a Plant button
    And Fill a plant name "Tulip_214154T"
    And Select category "sri lankan"
    And Set price "2000"
    And Set quantity "100"
    And Click 'Save' button
    Then Plant "Tulip_214154T" is added to the list
    When Select the category "All" from the category selector
    And Click Search button
    Then Plant "Tulip_214154T" is added to the list
    When Click delete icon on plant "Tulip_214154T"
    Then Plant "Tulip_214154T" is removed from the list

  # Test Case: M1-UI-03
  @M1-UI-03 @UI
  Scenario: Verify that Admin can sort plants by Quantity
    When Navigate to Plants tab using the side bar
    And I click the Add a Plant button
    And Fill a plant name "Orchid_214154T"
    And Select category "sri lankan"
    And Set price "3000"
    And Set quantity "10"
    And Click 'Save' button
    And I click the Add a Plant button
    And Fill a plant name "Lily_214154T"
    And Select category "sri lankan"
    And Set price "2500"
    And Set quantity "50"
    And Click 'Save' button
    And I click the Add a Plant button
    And Fill a plant name "Daisy_214154T"
    And Select category "sri lankan"
    And Set price "1800"
    And Set quantity "30"
    And Click 'Save' button
    When Click Quantity column header
    Then The plants list should be sorted by quantity
    When Click delete icon on plant "Orchid_214154T"
    And Click delete icon on plant "Lily_214154T"
    And Click delete icon on plant "Daisy_214154T"
    Then Plant "Orchid_214154T" is removed from the list

  # Test Case: M1-UI-04
  @M1-UI-04 @UI
  Scenario: Verify that display "No plants found" message to the Admin when no plants exist
    When Navigate to Plants tab using the side bar
    And Enter plant name "NonExistentPlant_214154T" in the search input box
    And Click Search button
    Then "No plants found" message is displayed

  # Test Case: M1-UI-05
  @M1-UI-05 @UI
  Scenario: Verify that show pagination for table in Plants page to the Admin
    When Navigate to Plants tab using the side bar
    And Create 15 test plants for pagination
    Then I should see plants pagination controls
    When I click the Next button
    Then The relevant page of plants is displayed in the table
    When I click the Previous button
    Then The previous page of plants is displayed in the table
    And Delete all pagination test plants


