Feature: Create training

  Scenario: Successfully create training
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When I create a training with name "Basketball" and duration 60
    Then training should be created successfully

  Scenario: Cannot create training with zero duration
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When I create a training with name "Basketball" and duration 0
    Then training creation should fail

  Scenario: Cannot create training without training name
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When I create a training with name "" and duration 60
    Then training creation should fail

  Scenario: Cannot create training without trainer
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When trainer is missing
    Then training creation should fail

  Scenario: Cannot create training without trainee
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When trainee is missing
    Then training creation should fail

  Scenario: Cannot create training without training type
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When training type is missing
    Then training creation should fail

  Scenario: Cannot create training without training date
    Given trainee "John.Doe" and trainer "Michael.Jordan" exist
    When training date is missing
    Then training creation should fail