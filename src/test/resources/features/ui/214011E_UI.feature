
@M3-UI-01 @UI
Feature: Verify Admin Add Plant
    Scenario: Verify that Admin can add plants
        Given Admin is logged into the system
        When Navigate to Plant tab
        And Click 'Add a Plant' button
        And Fill a plant name "Rose"
        And Select category "Indoor"
        And Set price "1500"
        And Set quantity "50"
        And Click 'Save' button
        Then Plant "Rose" is added to the list
