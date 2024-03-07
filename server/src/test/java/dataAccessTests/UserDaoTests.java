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
    public void getUserCorrectly() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> userDAO.createUser("Ty", "ty123", "ty@gmail.com"));
        UserData user = userDAO.getUser("Ty");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("Ty", user.username());
        Assertions.assertEquals("ty123", user.password());
        Assertions.assertEquals("ty@gmail.com", user.email());
    }

    @Test
    public void getNonExistentUser() throws DataAccessException {
        UserData user = userDAO.getUser("Brandon");

        Assertions.assertNull(user);
    }


    @Test
    public void clearTest( ) {

    }

}
