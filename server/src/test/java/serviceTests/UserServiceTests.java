package serviceTests;

import dataAccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import response.RegisterResponse;
import service.UserService;

import java.lang.reflect.Field;

public class UserServiceTests {
    private UserService UserService = new UserService();
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

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

}
