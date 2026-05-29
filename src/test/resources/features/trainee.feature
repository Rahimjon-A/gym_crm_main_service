Feature: Trainee Management

  @trainee @positive
  Scenario: Successfully register a new trainee
    When a new trainee registers with firstName "Alice" and lastName "TestTrainee"
    Then the response status should be 201
    And the response should contain a username and password

  @trainee @negative
  Scenario: Register trainee with missing first name
    When a new trainee registers with firstName "" and lastName "TestTrainee"
    Then the response status should be 400

  @trainee @positive
  Scenario: Get trainee profile successfully
    Given a registered trainee with firstName "Profile" and lastName "TestGet"
    And the user is authenticated
    When the client requests the trainee profile
    Then the response status should be 200
    And the response should contain firstName "Profile"

  @trainee @negative
  Scenario: Get non-existing trainee profile returns 404
    Given a registered trainee with firstName "Any" and lastName "TestUser"
    And the user is authenticated
    When the client requests profile for username "nonexistent.user"
    Then the response status should be 404

  @trainee @positive
  Scenario: Update trainee profile successfully
    Given a registered trainee with firstName "Update" and lastName "TestUpdate"
    And the user is authenticated
    When the client updates the trainee firstName to "UpdatedName" and lastName to "TestUpdate"
    Then the response status should be 200
    And the response should contain firstName "UpdatedName"

  @trainee @positive
  Scenario: Delete trainee profile successfully
    Given a registered trainee with firstName "Delete" and lastName "TestDelete"
    And the user is authenticated
    When the client deletes the trainee profile
    Then the response status should be 200

  @trainee @negative
  Scenario: Access trainee profile without authentication returns 401
    When the client requests profile for username "any.user" without auth
    Then the response status should be 401
