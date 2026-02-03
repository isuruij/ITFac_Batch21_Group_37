Feature: API Test Scenarios for M4-API-01

  @M4-API-01
  Scenario: Verify that updating a sub category parent to a non-existing parent is rejected
    Given Admin user is authenticated and has a valid JWT bearer token
    And A sub-category already exists in the system
    When I prepare a PUT request to update the sub-category with a non-existing parent category ID
    And I send the request with the Admin authorization bearer token
    Then The API rejects the request with status code 400 or 404
    And The response body contains a meaningful error message
