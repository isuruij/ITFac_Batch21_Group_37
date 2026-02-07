Feature: API Tests (214077J)

  @M4-API-01 @API
  Scenario: Verify that updating a sub category parent to a non-existing parent is rejected
    Given Valid Admin token is available
    When I send a PUT request to update the sub-category "ChiAPI01" with parent ID "999999"
    Then The response status code is 404
    And The response should contain an error message

  @M4-API-02 @API
  Scenario: Verify Creating Sub-Category Without Parent should result in making a Main category
    Given Valid Admin token is available
    When I send a POST request to create a category with name "M4MainCat" without parent ID
    Then The response status code is 201

  @M4-API-04 @API
  Scenario: Verify Retrieve Main Categories Only, by Admin
    Given Valid Admin token is available
    When I send a GET request to fetch main categories
    Then The response status code is 200
    And The response should contain a list of main categories

  @M4-API-03 @API
  Scenario: Verify searching Category with a Two-Part Name
    Given Valid Admin token is available
    And A category with two-part name "Two Part" exists
    When I search for the category "Two Part"
    Then The response status code is 200
    And The response should contain the category "Two Part"

  @M4-API-05 @API
  Scenario: Verify Adding Duplicate Category Name Under Same Parent is Rejected - Admin
    Given Valid Admin token is available
    And I have a category "ParDup" and a sub-category "ChiDup"
    When I send a POST request to create the sub-category "ChiDup" under "ParDup" again
    Then The response status code is 400 or 409
    And The response should contain an error message

  @M4-API-06 @API
  Scenario: Verify searching Category with Case-Insensitive Name
    Given Valid Admin token is available
    And A category with name "MixCase" exists
    When I search for the category "mixcase"
    Then The response status code is 200
    And The response should contain the category "MixCase"
    When I search for the category "MIXCASE"
    Then The response status code is 200
    And The response should contain the category "MixCase"

  @M4-API-07 @API
  Scenario: Verify Get Category by Non-Existing ID
    Given Valid Admin token is available
    When I send a GET request to fetch a category with non-existing ID "999999"
    Then The response status code is 404
    And The response should contain an error message

  @M4-API-08 @API
  Scenario: Verify Search Categories with Pagination for non admin user
    Given Valid Admin token is available
    And A category with name "PageCatOne" exists
    And A category with name "PageCatTwo" exists
    And Valid Test User token is available
    When I search for categories with name "PageCat" page 0 and size 10
    Then The response status code is 200
    And The response should contain categories "PageCatOne" and "PageCatTwo"

  @M4-API-09 @API
  Scenario: Verify Search Categories with Sorting for test user
    Given Valid Admin token is available
    And A category with name "SortA" exists
    And A category with name "SortB" exists
    And Valid Test User token is available
    When I search for categories sorted by "name" in "desc" order
    Then The response status code is 200
    And The first category in list should be "SortB"
    And The second category in list should be "SortA"
