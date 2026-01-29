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

  @M2-UI-03 @UI
  Scenario: Verify Admin Adding Category with Invalid Data
    Given Admin is logged into the system
    When Navigate to Categories tab
    And Click on Add a Category Button
    And Enter category name ""
    And Click on Save button
    Then Verify "Category name is required" error message is displayed

  @M2-UI-04 @UI
  Scenario: Verify Validation: Category Name Length (lower limit validation)
    Given Admin is logged into the system
    When Navigate to Categories tab
    And Click on Add a Category Button
    And Enter category name "ab"
    And Click on Save button
    Then Verify "Category name must be between 3 and 10 characters" error message is displayed

  @M2-UI-05 @UI
  Scenario: Verify Validation: Category Name Length (Upper limit validation)
    Given Admin is logged into the system
    When Navigate to Categories tab
    And Click on Add a Category Button
    And Enter category name "ValidName1234"
    And Click on Save button
    Then Verify "Category name must be between 3 and 10 characters" error message is displayed





