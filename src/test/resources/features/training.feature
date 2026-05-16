Feature: Training Management

  @training @positive
  Scenario: Successfully add a training
    Given a registered trainee with firstName "TrainA" and lastName "TestTrn"
    And a registered trainer with firstName "CoachA" and lastName "TestCoach" and specializationId 1
    And the user is authenticated
    When the client creates a training with name "Morning Session" and duration 60 and date "2025-06-15"
    Then the response status should be 200

  @training @positive
  Scenario: Get trainee trainings list
    Given a registered trainee with firstName "ListTr" and lastName "TestList"
    And a registered trainer with firstName "CoachList" and lastName "TestCL" and specializationId 1
    And the user is authenticated
    And a training exists with name "Evening Session" and duration 45 and date "2025-06-20"
    When the client requests trainee trainings for the registered trainee
    Then the response status should be 200

  @training @positive
  Scenario: Get trainer trainings list
    Given a registered trainee with firstName "TrnList" and lastName "TestTL"
    And a registered trainer with firstName "CoachTL" and lastName "TestCTL" and specializationId 1
    And the user is authenticated
    And a training exists with name "Strength Session" and duration 30 and date "2025-07-10"
    When the client requests trainer trainings for the registered trainer
    Then the response status should be 200

  @training @negative
  Scenario: Add training without authentication returns 401
    When the client creates a training without auth
    Then the response status should be 401
