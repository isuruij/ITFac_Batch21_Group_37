@API
Feature: API Tests (214154T)

  # Test Case: M1-API-01
  @M1-API-01 @API
  Scenario: Verify that can be taken sorted Plants by Quantity (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=10&sort=quantity,ASC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Quantity in ascending order

  # Test Case: M1-API-02
  @M1-API-02 @API
  Scenario: Verify that can be taken sorted Plants by Quantity (For User)
    Given User has a valid authorization token
    When User sends a GET request to "/api/plants/paged?page=0&size=10&sort=quantity,ASC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Quantity in ascending order

  # Test Case: M1-API-03
  @M1-API-03 @API
  Scenario: Filter plants by category (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=10&categoryId=1"
    Then The API should return a status code of 200
    And Only plants from category ID 1 are returned

  # Test Case: M1-API-04
  @M1-API-04 @API
  Scenario: Get plants page by page (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=5"
    Then The API should return a status code of 200
    And The response contains the first page of plant data
    When Admin sends a GET request to "/api/plants/paged?page=1&size=5"
    Then The API should return a status code of 200
    And The response contains the next set of plant records

  # Test Case: M1-API-05
  @M1-API-05 @API
  Scenario: Sort plants by price (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=10&sort=price,DESC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Price in descending order

  # Test Case: M1-API-06
  @M1-API-06 @API
  Scenario: Sort plants by price (For User)
    Given User has a valid authorization token
    When User sends a GET request to "/api/plants/paged?page=0&size=10&sort=price,DESC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Price in descending order

  # Test Case: M1-API-07
  @M1-API-07 @API
  Scenario: Verify that can be taken sorted Plants by Quantity (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=10&sort=quantity,ASC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Quantity in ascending order

  # Test Case: M1-API-08
  @M1-API-08 @API
  Scenario: Verify that can be taken sorted Plants by Name (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=10&sort=name,ASC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Name in ascending order

  # Test Case: M1-API-09
  @M1-API-09 @API
  Scenario: Filter plants by category (For User)
    Given User has a valid authorization token
    When User sends a GET request to "/api/plants/paged?page=0&size=10&categoryId=1"
    Then The API should return a status code of 200
    And Only plants from category ID 1 are returned

  # Test Case: M1-API-10
  @M1-API-10 @API
  Scenario: Get plants page by page (For User)
    Given User has a valid authorization token
    When User sends a GET request to "/api/plants/paged?page=0&size=5"
    Then The API should return a status code of 200
    And The response contains the first page of plant data
    When User sends a GET request to "/api/plants/paged?page=1&size=5"
    Then The API should return a status code of 200
    And The response contains the next set of plant records
