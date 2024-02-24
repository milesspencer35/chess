package serviceTests;

import dataAccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
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
        AuthData resultAuth = UserService.register(user);

        Assertions.assertNotNull(resultAuth);
        Assertions.assertNotNull(resultAuth.authToken());
        Assertions.assertEquals("George", resultAuth.username());
    }

    @Test
    public void RegisterDoesNotAddDuplicateUser() {
        UserData user1 = new UserData("Miles", "USA123", "America@gmail.com");
        UserData user2 = new UserData("Miles", "USA123", "America@gmail.com");
        AuthData resultAuth = UserService.register(user1);
        Assertions.assertNotNull(resultAuth);

        resultAuth = UserService.register(user2);
        Assertions.assertNull(resultAuth);
    }
}
