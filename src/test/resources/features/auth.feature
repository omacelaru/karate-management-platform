Feature: User Authentication
  As a user
  I want to be able to authenticate
  So that I can access protected resources

  Scenario: Successful user registration
    Given I am a new user
    When I register with email "test@example.com" and password "Test123!"
    Then I should receive a confirmation email
    And I should receive access and refresh tokens

  Scenario: Successful user login
    Given I am a registered user with email "test@example.com"
    When I login with email "test@example.com" and password "Test123!"
    Then I should receive access and refresh tokens

  Scenario: Failed login with invalid credentials
    Given I am a registered user with email "test@example.com"
    When I login with email "test@example.com" and password "WrongPassword123!"
    Then I should receive an unauthorized error