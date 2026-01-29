Feature: API Tests (214086K)

  @M2-API-01 @API
  Scenario: Create category as Admin
    Given Valid Admin token available
    When I send a POST request to "/api/categories" with valid category data
    Then The response status code should be 201

