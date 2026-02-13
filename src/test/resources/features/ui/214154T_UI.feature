Feature: UI Tests (214154T)
  # Test Case: M1-UI-01
  @M1-UI-01 @UI
  Scenario: Verify that Admin can search Plants by Name
    Given Admin is logged into the system
    When Navigate to Plants tab using the side bar
    And Click 'Add a Plant' button
    And Fill a plant name "TestPlant_Leeks_214154T"
    And Select category "Indoor"
    And Set price "1500"
    And Set quantity "50"
    And Click 'Save' button
    And Navigate to Plants tab using the side bar
    Then Plant "TestPlant_Leeks_214154T" is added to the list
    When Enter plant name "TestPlant_Leeks_214154T" in the search input box
    And Click Search button
    Then Show the plant list filtered by entered name "TestPlant_Leeks_214154T"
    When Click delete icon on plant "TestPlant_Leeks_214154T"
    Then Plant "TestPlant_Leeks_214154T" is removed from the list
