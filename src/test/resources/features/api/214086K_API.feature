Feature: API Tests (214086K)

  @M2-API-01 @API
  Scenario: Create category as Admin
    Given Valid Admin token available
    When I send a POST request to create a category with name "apimaincat"
    Then The response status code should be 201

  @M2-API-02 @API
  Scenario: Edit category as Admin
    Given Valid Admin token available
    When I send a PUT request to update the category to "officenew"
    Then The response status code should be 200

  @M2-API-03 @API
  Scenario: Delete category as Admin
    Given Valid Admin token available
    When I send a DELETE request to delete the category "Herbs"
    Then The response status code should be 204

  @M2-API-04 @API
  Scenario: Creating category with less than 3 letters for name
    Given Valid Admin token available
    When I send a POST request to create a category with name "A"
    Then The response status code should be 400

