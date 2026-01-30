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

  @M2-API-05 @API
  Scenario: Creating category with missing name
    Given Valid Admin token available
    When I send a POST request to create a category with missing name
    Then The response status code should be 400

  @M2-API-06 @API
  Scenario: get all sub categories as Test User
    Given Valid User token available
    When I send a GET request to "/api/categories/sub-categories"
    Then The response status code should be 200

  @M2-API-07 @API
  Scenario: Get specific category by ID for Test User
    Given Valid User token available
    When I send a GET request to fetch category with name "apicat"
    Then The response status code should be 200

  @M2-API-08 @API
  Scenario: Test User Creating a Category
    Given Valid User token available
    When I send a POST request to create a category with name "testcat"
    Then The response status code should be 403

  @M2-API-09 @API
  Scenario: Test User Updating a Category
    Given Valid User token available
    When I send a PUT request to update the category "apicat"
    Then The response status code should be 403