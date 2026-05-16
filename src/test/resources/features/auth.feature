Feature: Authentication

  @auth @positive
  Scenario: Successful login with valid credentials
    Given a registered trainee with firstName "John" and lastName "TestDoe"
    When the user logs in with correct credentials
    Then the response status should be 200
    And the response should contain a JWT token

  @auth @negative
  Scenario: Failed login with wrong password
    Given a registered trainee with firstName "Jane" and lastName "TestDoe"
    When the user logs in with wrong password "wrongpassword"
    Then the response status should be 401

  @auth @negative
  Scenario: User gets blocked after 3 failed login attempts
    Given a registered trainee with firstName "Block" and lastName "TestUser"
    When the user fails to login 3 times with wrong password
    Then the response status should be 401
    And the user should be temporarily blocked
