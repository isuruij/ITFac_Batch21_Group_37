@API
Feature: Verify Admin API Features

    Background:
        Given Valid Admin token

    @M3-API-01
    Scenario: Verify that Admin can create plants via API
        When Admin create a plant with name "Orchid" price "800.0" quantity "15" in category "4"
        Then Receive status code 201
        And The response should contain the plant name "Orchid"

    @M3-API-02
    Scenario: Verify that Admin can edit plants via API
        When Admin create a plant with name "Orchid" price "500.0" quantity "10" in category "4"
        Then Receive status code 201
        When Admin updates the plant with name "Orchid" price "600.0" quantity "10" in category "4"
        Then Receive status code 200
        And The response should contain the plant name "Orchid"

    @M3-API-03
    Scenario: Verify that Admin can delete plants via API
        When Admin create a plant with name "Orchid" price "500.0" quantity "10" in category "4"
        Then Receive status code 201
        When Admin deletes the plant
        Then Receive status code 204

    @M3-API-04
    Scenario: Verify that Admin cannot create plants with negative price
        When Admin create a plant with name "Orchid" price "-5.0" quantity "10" in category "4"
        Then Receive status code 400

    @M3-API-05
    Scenario: Verify that Admin cannot create plants with missing name
        When Admin create a plant with name "" price "100.0" quantity "10" in category "4"
        Then Receive status code 400

    