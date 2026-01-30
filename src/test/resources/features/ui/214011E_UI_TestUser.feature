@UI
Feature: Verify Test User UI Feature

    Background:
        Given Test User is logged into the system

    @M3-UI-06
    Scenario: Verify Add Plant Hidden for Test User
        When Navigate to Plant tab
        Then 'Add a Plant' button is not displayed

    @M3-UI-07
    Scenario: Verify Edit Plant Hidden for Test User
        When Navigate to Plant tab
        Then Edit icon is not displayed for any plant