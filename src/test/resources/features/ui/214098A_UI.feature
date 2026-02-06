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
