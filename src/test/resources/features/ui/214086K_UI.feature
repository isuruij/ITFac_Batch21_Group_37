Feature: UI Tests (214086K)

  @M2-UI-01 @UI
  Scenario: Verify Admin Adding Main Category
    Given Admin is logged into the system
    When Navigate to Categories tab
    And Click on Add a Category Button
    And Enter category name "maincat11"
    And Click on Save button
    Then Verify that the new category "maincat11" is added to the list

  @M2-UI-02 @UI
  Scenario: Verify Admin Adding Sub-Category
    Given Admin is logged into the system
    When Navigate to Categories tab
    And Click on Add a Category Button
    And Select "Indoor" from the parent category dropdown
    And Enter category name "subcat11"
    And Click on Save button
    Then Verify that the new category "subcat11" is added to the list


