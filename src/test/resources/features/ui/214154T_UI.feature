@UI
Feature: UI Tests (214154T)

  Background:
    Given Admin is logged into the system

  # Test Case: M1-UI-01
  @M1-UI-01 @UI
  Scenario: Verify that Admin can search Plants by Name
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "Rose_214154T"
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
    And Click 'Add a Plant' button
    And Fill a plant name "Tulip_214154T"
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
    And Click 'Add a Plant' button
    And Fill a plant name "Orchid_214154T"
    And Set price "3000"
    And Set quantity "10"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Lily_214154T"
    And Set price "2500"
    And Set quantity "50"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Daisy_214154T"
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
    Then I should see pagination controls
    When I click the Next button
    Then The relevant page of plants is displayed in the table
    When I click the Previous button
    Then The previous page of plants is displayed in the table
    And Delete all pagination test plants

  # Test Case: M1-UI-06
  @M1-UI-06 @UI
  Scenario: Verify that User can search Plants by keywords
    Given User is logged into the system
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "Sunflower_214154T"
    And Set price "1200"
    And Set quantity "40"
    And Click 'Save' button
    Then Plant "Sunflower_214154T" is added to the list
    When Enter plant name "Sunflower" in the search input box
    And Click Search button
    Then Show the plant list filtered by entered name "Sunflower_214154T"
    When Click delete icon on plant "Sunflower_214154T"
    Then Plant "Sunflower_214154T" is removed from the list

  # Test Case: M1-UI-07
  @M1-UI-07 @UI
  Scenario: Verify that User can filter Plants by Category
    Given User is logged into the system
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "Lavender_214154T"
    And Set price "1800"
    And Set quantity "60"
    And Click 'Save' button
    Then Plant "Lavender_214154T" is added to the list
    When Select the category "All" from the category selector
    And Click Search button
    Then Plant "Lavender_214154T" is added to the list
    When Click delete icon on plant "Lavender_214154T"
    Then Plant "Lavender_214154T" is removed from the list

  # Test Case: M1-UI-08
  @M1-UI-08 @UI
  Scenario: Verify that User can Sort plants by Price
    Given User is logged into the system
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "Jasmine_214154T"
    And Set price "3500"
    And Set quantity "25"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Peony_214154T"
    And Set price "1500"
    And Set quantity "35"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Hibiscus_214154T"
    And Set price "2500"
    And Set quantity "45"
    And Click 'Save' button
    When Click Price column header
    Then The plants list should be sorted by price
    When Click delete icon on plant "Jasmine_214154T"
    And Click delete icon on plant "Peony_214154T"
    And Click delete icon on plant "Hibiscus_214154T"
    Then Plant "Jasmine_214154T" is removed from the list

  # Test Case: M1-UI-09
  @M1-UI-09 @UI
  Scenario: Verify user can sort Plants by name
    Given User is logged into the system
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "Zinnia_214154T"
    And Set price "1000"
    And Set quantity "20"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Aster_214154T"
    And Set price "1100"
    And Set quantity "22"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Marigold_214154T"
    And Set price "1200"
    And Set quantity "24"
    And Click 'Save' button
    When Click Name column header
    Then The plants list should be sorted by name
    When Click delete icon on plant "Zinnia_214154T"
    And Click delete icon on plant "Aster_214154T"
    And Click delete icon on plant "Marigold_214154T"
    Then Plant "Zinnia_214154T" is removed from the list

  # Test Case: M1-UI-10
  @M1-UI-10 @UI
  Scenario: Verify that User can Sort by Quantity
    Given User is logged into the system
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "Carnation_214154T"
    And Set price "1400"
    And Set quantity "15"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Daffodil_214154T"
    And Set price "1600"
    And Set quantity "55"
    And Click 'Save' button
    And Click 'Add a Plant' button
    And Fill a plant name "Iris_214154T"
    And Set price "1700"
    And Set quantity "35"
    And Click 'Save' button
    When Click Quantity column header
    Then The plants list should be sorted by quantity
    When Click delete icon on plant "Carnation_214154T"
    And Click delete icon on plant "Daffodil_214154T"
    And Click delete icon on plant "Iris_214154T"
    Then Plant "Carnation_214154T" is removed from the list
