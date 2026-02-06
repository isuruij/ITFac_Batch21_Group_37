Feature: API Tests (214077J)

  @M4-API-01 @API
  Scenario: Verify that updating a sub category parent to a non-existing parent is rejected
    Given Valid Admin token is available
    And I have a category "ParAPI01" and a sub-category "ChiAPI01"
    When I send a PUT request to update the sub-category "ChiAPI01" with parent ID "999999"
    Then The response status code is 404
    And The response should contain an error message
    # Cleanup
    And I delete the category "ChiAPI01" through API
    And I delete the category "ParAPI01" through API

  @M4-API-04 @API
  Scenario: Verify Retrieve Main Categories Only, by Admin
    Given Valid Admin token is available
    When I send a GET request to fetch main categories
    Then The response status code is 200
    And The response should contain a list of main categories
