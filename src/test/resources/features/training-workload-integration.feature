Feature: Training and trainer workload integration

  Scenario: Creating training updates trainer workload

    Given trainer "Michael.Jordan" has no workload
    When I create training for trainer "Michael.Jordan" with duration 60
    Then trainer workload should be updated to 60