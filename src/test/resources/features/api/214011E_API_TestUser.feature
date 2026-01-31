@API
Feature: Verify Admin API Features

    Background:
        Given Valid Test User token

    @M3-API-06
    Scenario: Verify that Test users can't create plants with API
        When Admin create a plant with name "Orchid" price "100.0" quantity "10" in category "4"
        Then Receive status code 403