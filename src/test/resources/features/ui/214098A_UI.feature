Feature: Sales Management (214098A)

  @M5-UI-01 @UI
  Scenario: Sell Plant button visible for Admin
    Given Admin is logged into the system
    When Navigate to the Sales page
    Then Observe the action buttons on the page
    And Sell Plant button is visible only for Admin users
    When Click the Sell Plant button
    Then Clicking the button navigates to the Sell Plant page successfully

  @M5-UI-02 @UI
  Scenario: Admin delete sale with confirmation
    Given Admin is logged into the system
    And At least one sale exists
    When Navigate to the Sales page
    Then Delete option is available only to Admin users
    When Click the Delete button for a sale record
    Then Observe the confirmation popup
    When Click Confirm
    Then Sale record is deleted successfully after confirmation

  @M5-UI-03 @UI
  Scenario: Plant dropdown and quantity validation
    Given Admin is logged into the system
    When Navigate to the Sell Plant page
    And Open the Plant dropdown
    And Select a plant
    And Enter quantity "0"
    And Click Save
    Then Error message "Quantity must be greater than 0" is shown

  @M5-UI-04 @UI
  Scenario: Redirect after successful sale
    Given Admin is logged into the system
    When Navigate to the Sell Plant page
    And Select a plant
    And Enter quantity "1"
    And Click Save
    Then Admin user is redirected to the Sales List page

  @M5-UI-05 @UI
  Scenario: Cancel navigation from Sell Plant
    Given Admin is logged into the system
    When Navigate to the Sell Plant page
    And Click the Cancel button
    Then Admin user is redirected back to the Sales List page

  @M5-UI-06 @UI
  Scenario: User sales list pagination and default sorting
    Given Test User is logged into the system
    And At least one sale exists
    When Navigate to the Sales page
    Then Sales list is displayed with pagination
    And Default sorting is by Sold Date Descending

  @M5-UI-07 @UI
  Scenario: User sales sorting
    Given Test User is logged into the system
    And At least one sale exists
    When Navigate to the Sales page
    
    When Click Plant Name column header
    Then Sales list sorts correctly for Plant Name
    
    When Click Quantity column header
    Then Sales list sorts correctly for Quantity

    When Click Total Price column header
    Then Sales list sorts correctly for Total Price
    
    When Click Sold Date column header
    Then Sales list sorts correctly for Sold Date

  @M5-UI-08 @UI
  Scenario: Restricted actions for User
    Given Test User is logged into the system
    When Navigate to the Sales page
    Then Observe the action buttons on the page
    And Sell Plant button is not visible for User
    And Delete action is not available for User

  @M5-UI-09 @UI
  Scenario: No sales found message
    Given Test User is logged into the system
    And No sales exist
    When Navigate to the Sales page
    Then 'No sales found' message is displayed

  @M5-UI-10 @UI
  Scenario: User access restriction to Sell Plant page
    Given Test User is logged into the system
    When Access /ui/sales/new directly via browser URL
    Then Access is denied or user is redirected
    And Sell Plant page is not accessible
