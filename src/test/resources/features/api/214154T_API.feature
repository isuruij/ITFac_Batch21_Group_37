

  @M1-API-01
  Scenario: Verify that can be taken sorted Plants by Quantity (For Admin)
    Given Admin has a valid authorization token
    When Admin sends a GET request to "/api/plants/paged?page=0&size=10&sort=quantity,ASC"
    Then The API should return a status code of 200
    And The plant list should be sorted by Quantity in ascending order
