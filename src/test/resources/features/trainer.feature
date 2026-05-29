Feature: Trainer Management

  @trainer @positive
  Scenario: Successfully register a new trainer
    When a new trainer registers with firstName "TrainerA" and lastName "TestTrainer" and specializationId 1
    Then the response status should be 201
    And the response should contain a username and password

  @trainer @negative
  Scenario: Register trainer with missing first name
    When a new trainer registers with firstName "" and lastName "TestTrainer" and specializationId 1
    Then the response status should be 400

  @trainer @positive
  Scenario: Get trainer profile successfully
    Given a registered trainer with firstName "GetTrainer" and lastName "TestGet" and specializationId 1
    And the trainer is authenticated
    When the client requests the trainer profile
    Then the response status should be 200
    And the response should contain firstName "GetTrainer"

  @trainer @positive
  Scenario: Update trainer profile successfully
    Given a registered trainer with firstName "UpdTrainer" and lastName "TestUpd" and specializationId 1
    And the trainer is authenticated
    When the client updates the trainer firstName to "UpdatedTrainer" and lastName to "TestUpd"
    Then the response status should be 200
    And the response should contain firstName "UpdatedTrainer"
