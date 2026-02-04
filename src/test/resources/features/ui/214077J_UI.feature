Feature: UI Test Scenarios for M4-UI-01

  @M4-UI-01 @UI
  Scenario Outline: Verify Dashboard displays summary including Categories, Plants, Sales, Inventory cards upon successful login
    Given I am on the login page
    When I login with valid credentials "<username>" and "<password>"
    Then I should see the Dashboard page
    And I should see the Categories card
    And I should see the Plants card
    And I should see the Sales card
    And I should see the Inventory card

    Examples:
      | username | password |
      | admin    | admin123 |
      | testuser | test123  |

  @M4-UI-02 @UI
  Scenario Outline: Verify Navigation Menu function and active page highlighting
    Given I am logged in as "<username>" with "<password>"
    Then I should see the following navigation links:
      | Dashboard |
      | Category  |
      | Plants    |
      | Sales     |
      | Inventory |
    When I click on the "Dashboard" navigation link
    Then The "Dashboard" navigation link should be active
    When I click on the "Category" navigation link
    Then The "Category" navigation link should be active
    When I click on the "Plants" navigation link
    Then The "Plants" navigation link should be active
    When I click on the "Sales" navigation link
    Then The "Sales" navigation link should be active
    When I click on the "Inventory" navigation link
    Then The "Inventory" navigation link should be active

    Examples:
      | username | password |
      | admin    | admin123 |
      | testuser | test123  |

  @M4-UI-03 @UI
  Scenario: Verify Dashboard displays correct "Main Category", "Sub Category" and "Total Plant" counts
    Given I am logged in as "testuser" with "test123"
    When I am on the Dashboard page
    Then I should see the Main Category count matches the actual system count
    And I should see the Sub Category count matches the actual system count
    And I should see the Total Plant count matches the actual system count

  @M4-UI-04 @UI
  Scenario: Verify Sorting Categories by ID, Name, and Parent Category
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Categories page
    And Multiple categories exist in the system
    When I sort the categories by "ID"
    Then The categories should be sorted by "ID" in ascending order
    When I sort the categories by "Name"
    Then The categories should be sorted by "Name" in alphabetical order
    When I sort the categories by "Parent Category"
    Then The categories should be sorted by "Parent Category" in alphabetical order

  @M4-UI-05 @UI
  Scenario: Verify Cancel Action in Edit Category Page as Admin
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Categories page
    And A category "ABC" exists
    When I click on the Edit button for category "ABC"
    And I enter category name "EditedABC"
    And I click the Cancel button on the Edit Category page
    Then I should be redirected to the Category list page
    And The category "EditedABC" should not be visible in the list
    And The category "ABC" should still be visible in the list

  @M4-UI-06 @UI
  Scenario: Verify Cancel Action in Add Plants Page as Admin
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Plants page
    When I click on the Add Plant button
    And I enter plant name "CancelNewPlant"
    And I click the Cancel button in Plant page
    Then I should be redirected to the Plants list page
    And The plant "CancelNewPlant" should not be visible in the list

  @M4-UI-07 @UI
  Scenario: Verify Pagination for Category
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Categories page
    # Ensure we have enough data for pagination
    Given Multiple categories exist in the system
    Then I should see pagination controls
    When I click on the Next page button
    Then I should be on page "2" of categories
    When I click on the Previous page button
    Then I should be on page "1" of categories

  @M4-UI-08 @UI
  Scenario: Verify duplicate category name under same Main Category is rejected
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Categories page
    And A category "DupCat" exists
    When I click on the Add Category button
    And I enter category name "DupCat"
    And I click the Save button
    Then I should see an error message indicating duplicate category
    And I should see that I am still on the Add Category page

  @M4-UI-09 @UI
  Scenario: Verify preventing deletion of category with sub-categories
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Categories page
    And I identify a category with a sub-category
    When I attempt to delete the identified parent category
    Then I should see an error message indicating deletion is not allowed
    And The identified parent category should still be visible in the list

  @M4-UI-10 @UI
  Scenario: Verify preventing deletion of category with linked plants
    Given I am logged in as "admin" with "admin123"
    And I navigate to the Categories page
    And A category "CategoryWithPlants" exists with a linked plant "PlantForCategoryDeletion"
    When I click on the Delete button for category "CategoryWithPlants"
    Then I should see an error message indicating deletion is not allowed
    And The category "CategoryWithPlants" should still be visible in the list
