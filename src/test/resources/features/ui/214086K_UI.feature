Feature: UI Tests (214086K)

  @M2-UI-01 @UI
  Scenario: Verify Admin Adding Main Category
    Given Admin is logged into the system
    When Navigate to Categories tab
    And Click on Add a Category Button
    And Enter category name "plantcat3"
    And Click on Save button
    Then Verify that the new category "plantcat3" is added to the list


