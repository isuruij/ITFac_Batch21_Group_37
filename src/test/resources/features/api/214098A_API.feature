Feature: API Tests (214098A)

  @M5-API-01 @API
  Scenario: Create a sale for a plant
    Given I have a valid Admin token for Sales
    When I send a POST request to create a sale for the newly created plant with quantity 5
    Then I verify the sales response status code is 201

  @M5-API-02 @API
  Scenario: Get Sale details by ID
    Given I have a valid Admin token for Sales
    When I send a GET request to fetch the sale with ID 2
    Then I verify the sales response status code is 200

  @M5-API-03 @API
  Scenario: Invalid Delete sale request validation
    Given I have a valid Admin token for Sales
    When I send a DELETE request to delete the sale with ID 5
    Then I verify the sales response status code is 404
