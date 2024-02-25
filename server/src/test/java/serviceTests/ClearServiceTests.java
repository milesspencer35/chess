package serviceTests;


import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

public class ClearServiceTests {

    private ClearService clearService = new ClearService();
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    @BeforeEach
    public void setUp() {
        gameDAO = MemoryGameDAO.getInstance();
        userDAO = MemoryUserDAO.getInstance();
        authDAO = MemoryAuthDAO.getInstance();
    }

    @Test
    public void testClearApplication() {
        try {
            int game1 = gameDAO.createGame("myGame");
            int game2 = gameDAO.createGame("myGame2");
            Assertions.assertNotEquals(0, gameDAO.listGames().size(), "Assert there are games");

            userDAO.createUser("Miles", "Hello123", "miles@gmail.com");
            userDAO.createUser("Jamal", "BYU123", "jamal@gmail.com");
            Assertions.assertNotEquals(0, userDAO.numberOfUsers(), "Assert there are Users");

            authDAO.createAuth("Miles");
            authDAO.createAuth("Jamal");
            Assertions.assertNotEquals(0, authDAO.numberOfAuths(), "Assert there are Auths");

            clearService.clearApplication();
            Assertions.assertEquals(0, gameDAO.listGames().size(), "Assert number of games == 0");
            Assertions.assertEquals(0, userDAO.numberOfUsers(), "Assert number of Users == 0");
            Assertions.assertEquals(0, authDAO.numberOfAuths(), "Assert number of Auths == 0");
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
