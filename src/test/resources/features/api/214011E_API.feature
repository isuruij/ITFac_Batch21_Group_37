@API
Feature: Verify Admin API Features

    @M3-API-01
    Scenario: Verify that Admin can create plants via API
        Given Valid Admin token
        When Admin create a plant with name "Orchid" price "800.0" quantity "15" in category "4"
        Then Receive status code 201
        And The response should contain the plant name "Orchid"

    @M3-API-02
    Scenario: Verify that Admin can edit plants via API
        Given Valid Admin token
        When Admin create a plant with name "Orchid" price "500.0" quantity "10" in category "4"
        Then Receive status code 201
        When Admin updates the plant with name "Orchid" price "600.0" quantity "10" in category "4"
        Then Receive status code 200
        And The response should contain the plant name "Orchid"