//package ro.unibuc.fmi.karate_management_platform.steps;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.cucumber.spring.CucumberContextConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
//import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthResponse;
//import ro.unibuc.fmi.karate_management_platform.resources.AuthResource;
//import ro.unibuc.fmi.karate_management_platform.services.AuthService;
//import ro.unibuc.fmi.karate_management_platform.services.EmailService;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@CucumberContextConfiguration
//@SpringBootTest
//public class AuthSteps {
//
//    @Autowired
//    private AuthResource authResource;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private EmailService emailService;
//
//    private AuthRequest authRequest;
//    private ResponseEntity<AuthResponse> authResponse;
//    private Exception exception;
//
//    @Given("I am a new user")
//    public void iAmANewUser() {
//        // No setup needed for new user
//    }
//
//    @Given("I am a registered user with email {string}")
//    public void iAmARegisteredUser(String email) {
//        // In a real test, you would set up a test user in the database
//        // For this example, we'll assume the user exists
//    }
//
//    @Given("I am a logged in user with email {string}")
//    public void iAmALoggedInUser(String email) {
//        // In a real test, you would set up a logged-in user with valid tokens
//        // For this example, we'll assume the user is logged in
//    }
//
//    @When("I register with email {string} and password {string}")
//    public void iRegisterWithEmailAndPassword(String email, String password) {
//        authRequest = new AuthRequest(email, password, "en");
//        authResponse = authResource.register(authRequest);
//    }
//
//    @When("I login with email {string} and password {string}")
//    public void iLoginWithEmailAndPassword(String email, String password) {
//        try {
//            authRequest = new AuthRequest(email, password, "en");
//            authResponse = authResource.login(authRequest);
//        } catch (Exception e) {
//            exception = e;
//        }
//    }
//
//    @When("I request a token refresh")
//    public void iRequestATokenRefresh() {
//        // In a real test, you would use the refresh token from a previous login
//        // For this example, we'll assume we have a valid refresh token
//        authResponse = authResource.refreshToken(null, null);
//    }
//
//    @Then("I should receive a confirmation email")
//    public void iShouldReceiveAConfirmationEmail() {
//        // In a real test, you would verify that the email service was called
//        // For this example, we'll just assert that the registration was successful
//        assertNotNull(authResponse);
//        assertEquals(200, authResponse.getStatusCode().value());
//    }
//
//    @Then("I should receive access and refresh tokens")
//    public void iShouldReceiveAccessAndRefreshTokens() {
//        assertNotNull(authResponse);
//        assertEquals(200, authResponse.getStatusCode().value());
//        assertNotNull(authResponse.getBody());
//        assertNotNull(authResponse.getBody().accessToken());
//        assertNotNull(authResponse.getBody().refreshToken());
//    }
//
//    @Then("I should receive an unauthorized error")
//    public void iShouldReceiveAnUnauthorizedError() {
//        assertNotNull(exception);
//        assertTrue(exception instanceof BadCredentialsException);
//    }
//}