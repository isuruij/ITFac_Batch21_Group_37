Feature: Sales Management (214098A)

  @M5-UI-01 @UI
  Scenario: Sell Plant button visible for Admin
    Given Admin is logged into the system
    When Navigate to the Sales page
    Then Observe the action buttons on the page
    And Sell Plant button is visible only for Admin users
    When Click the Sell Plant button
    Then Clicking the button navigates to the Sell Plant page successfully
