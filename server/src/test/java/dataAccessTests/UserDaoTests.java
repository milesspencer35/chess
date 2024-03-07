package dataAccessTests;

import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTests {
    UserDAO userDAO = new SQLUserDAO();

    public UserDaoTests() throws DataAccessException {
    }

    @AfterEach
    public void clearDatabase() throws DataAccessException {
        userDAO.clear();
    }


    @Test
   public void createUserCorrectly() throws DataAccessException {
       userDAO.createUser("Johnny", "johnny123", "johnny@gmail.com");
       UserData user = userDAO.getUser("Johnny");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("Johnny", user.username());
        Assertions.assertEquals("johnny123", user.password());
        Assertions.assertEquals("johnny@gmail.com", user.email());
    }

    @Test
    public void createUserMissingInformation() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class,
                () -> userDAO.createUser("Ryan", null, "ryan@gmail.com" ));
    }

    @Test
    public void getUserTest() {

    }

    @Test
    public void clearTest( ) {

    }

}
