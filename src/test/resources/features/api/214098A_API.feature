Feature: API Tests (214098A)

  @M5-API-02 @API
  Scenario: Get Sale details by ID
    Given I have a valid Admin token for Sales
    When I send a GET request to fetch the sale with ID 2
    Then I verify the sales response status code is 200
