@UI
Feature: Verify Admin UI Feature

    Background:
        Given Admin is logged into the system

    @M3-UI-01
    Scenario: Verify that Admin can add plants
        When Navigate to Plant tab
        And Click 'Add a Plant' button
        And Fill a plant name "Rose"
        And Select category "Indoor"
        And Set price "1500"
        And Set quantity "50"
        And Click 'Save' button
        Then Plant "Rose" is added to the list

    @M3-UI-02
    Scenario: Verify that Admin can edit prices of added plants
        When Navigate to Plant tab
        And Click 'Add a Plant' button
        And Fill a plant name "Rose"
        And Select category "Indoor"
        And Set price "1500"
        And Set quantity "50"
        And Click 'Save' button
        Then Plant "Rose" is added to the list
        When Click edit icon on plant "Rose"
        And Set price "2000"
        And Click 'Save' button
        Then Plant "Rose" has price "2000"

    @M3-UI-03
    Scenario: Verify Price Validation
        When Navigate to Plant tab
        And Click 'Add a Plant' button
        And Fill a plant name "Rose"
        And Select category "Indoor"
        And Set price "1500"
        And Set quantity "50"
        And Click 'Save' button
        Then Plant "Rose" is added to the list
        When Click edit icon on plant "Rose"
        And Set price "-1"
        And Click 'Save' button
        Then Error message "Price must be greater than 0" is displayed

    @M3-UI-04
    Scenario: Verify Quantity Validation
        When Navigate to Plant tab
        And Click 'Add a Plant' button
        And Fill a plant name "Rose"
        And Select category "Indoor"
        And Set price "1500"
        And Set quantity "50"
        And Click 'Save' button
        Then Plant "Rose" is added to the list
        When Click edit icon on plant "Rose"
        And Set quantity "-1"
        And Click 'Save' button
        Then Error message "Quantity cannot be negative" is displayed

    @M3-UI-05
    Scenario: Verify that Admin can delete plants
        When Navigate to Plant tab
        And Click 'Add a Plant' button
        And Fill a plant name "Tulip"
        And Select category "Indoor"
        And Set price "500"
        And Set quantity "20"
        And Click 'Save' button
        Then Plant "Tulip" is added to the list
        When Click delete icon on plant "Tulip"
        Then Plant "Tulip" is removed from the list
