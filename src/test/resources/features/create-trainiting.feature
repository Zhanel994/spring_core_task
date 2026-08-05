Feature: Create training

  Scenario: Successfully create training
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When I create a training with name "Basketball" and duration 60
    Then training should be created successfully

  Scenario: Cannot create training without duration
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When I create a training with name "Basketball" and duration 0
    Then training creation should fail