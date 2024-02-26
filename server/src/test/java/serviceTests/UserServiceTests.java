package serviceTests;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import response.ErrorResponse;
import response.LoginResponse;
import response.RegisterResponse;
import service.UserService;

import java.lang.reflect.Field;

public class UserServiceTests {
    private UserService UserService = new UserService();

    @Test
    public void RegisterAddsUserCorrectly() {
        UserData user = new UserData("George", "USA123", "America@gmail.com");
        RegisterResponse response = UserService.register(user);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.authToken());
        Assertions.assertNull(response.message());
        Assertions.assertEquals("George", response.username());
    }

    @Test
    public void RegisterDoesNotAddDuplicateUser() {
        UserData user1 = new UserData("Miles", "USA123", "America@gmail.com");
        UserData user2 = new UserData("Miles", "USA123", "America@gmail.com");
        RegisterResponse response = UserService.register(user1);
        Assertions.assertNotNull(response);

        response = UserService.register(user2);
        Assertions.assertNull(response.authToken());
        Assertions.assertNull(response.username());
        Assertions.assertEquals("Error: already taken", response.message());
    }

    @Test
    public void RegisterSendsMessageForBadRequest() {
        UserData user = new UserData("Bill", "BillDawg", null);
        RegisterResponse response = UserService.register(user);

        Assertions.assertNull(response.authToken());
        Assertions.assertNull(response.username());
        Assertions.assertEquals("Error: bad request", response.message());
    }

    @Test
    public void UserIsLoggedIn() {
        UserData user = new UserData("William", "Will34", "will@gmail.com");
        RegisterResponse registerResponse = UserService.register(user);

        LoginResponse loginResponse = UserService.login(user);

        Assertions.assertNotNull(loginResponse.authToken());
        Assertions.assertEquals("William",loginResponse.username());
        Assertions.assertNull(loginResponse.message());
    }

    @Test
    public void WrongPasswordDoesNotLogin() {
        UserData user = new UserData("John", "JMan", "john@gmail.com");
        UserService.register(user);

        user = new UserData("John", "Will34", "john@gmail.com");
        LoginResponse loginResponse = UserService.login(user);

        Assertions.assertNull(loginResponse.authToken());
        Assertions.assertNull(loginResponse.username());
        Assertions.assertEquals("Error: unauthorized", loginResponse.message());
    }

    @Test
    public void NonExistUserDoesNotLogin() {
        UserData user = new UserData("Spiderman", "webs", "spiderman@gmail.com");
        LoginResponse loginResponse = UserService.login(user);

        Assertions.assertNull(loginResponse.authToken());
        Assertions.assertNull(loginResponse.username());
        Assertions.assertEquals("Error: unauthorized", loginResponse.message());
    }

    @Test
    public void SuccessfulLogout() {
        UserData user = new UserData("Peter", "pete123", "peter@gmail.com");
        UserService.register(user);
        LoginResponse loginResponse = UserService.login(user);

        ErrorResponse errorResponse = UserService.logout(loginResponse.authToken());
        Assertions.assertNull(errorResponse);
    }

    @Test
    public void UnauthorizedLogout() {
        UserData user = new UserData("Jimmy", "Jimbo58", "jimmy@gmail.com");
        UserService.register(user);
        LoginResponse loginResponse = UserService.login(user);

        ErrorResponse errorResponse = UserService.logout("123");
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("Error: unauthorized", errorResponse.message());
    }
}
