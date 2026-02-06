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

  @M5-API-04 @API
  Scenario: Sale validation rules
    Given I have a valid Admin token for Sales
    When I send a POST request to create a sale for plant 2 with quantity 0
    Then I verify the sales response status code is 400

  @M5-API-05 @API
  Scenario: Sale API role restriction - Create
    Given I have a valid User token for Sales
    When I send a POST request to create a sale for plant 2 with quantity 5
    Then I verify the sales response status code is 403

  @M5-API-05 @API
  Scenario: Sale API role restriction - Delete
    Given I have a valid User token for Sales
    When I send a DELETE request to delete the sale with ID 2
    Then I verify the sales response status code is 403

  @M5-API-06 @API
  Scenario: Get all sales as User
    Given I have a valid User token for Sales
    When I send a GET request to fetch all sales
    Then I verify the sales response status code is 200

  @M5-API-07 @API
  Scenario: Get sale by ID API
    Given I have a valid User token for Sales
    When I send a GET request to fetch the sale with ID 13
    Then I verify the sales response status code is 200

  @M5-API-08 @API
  Scenario: Sales pagination and sorting API
    Given I have a valid User token for Sales
    When I send a GET request to fetch sales with page 1 size 1 and sort "id"
    Then I verify the sales response status code is 200
