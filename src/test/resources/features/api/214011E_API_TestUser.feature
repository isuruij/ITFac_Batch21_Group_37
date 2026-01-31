@API
Feature: Verify Admin API Features

    # Background:
        

    @M3-API-06
    Scenario: Verify that Test users can't create plants with API
        Given Valid Test User token
        When Admin create a plant with name "Orchid" price "100.0" quantity "10" in category "4"
        Then Receive status code 403

    @M3-API-07
    Scenario: Verify that Test users can't edit plants with API
        Given Valid Admin token
        When Admin create a plant with name "Orchid" price "100.0" quantity "10" in category "4"
        Then Receive status code 201
        Given Valid Test User token
        When Admin updates the plant with name "Orchid" price "200.0" quantity "20" in category "4"
        Then Receive status code 403