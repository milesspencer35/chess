package dataAccessTests;

import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertNotNull(user.password());
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
        Assertions.assertNotNull(user.password());
        Assertions.assertEquals("ty@gmail.com", user.email());
    }

    @Test
    public void getNonExistentUser() throws DataAccessException {
        UserData user = userDAO.getUser("Brandon");

        Assertions.assertNull(user);
    }

    @Test
    public void verifyUserCorrectly() throws DataAccessException {
        userDAO.createUser("Juan", "Juan123", "juan@gmail.com");
        UserData verifiedUser = userDAO.verifyUser("Juan", "Juan123");

        Assertions.assertEquals("Juan", verifiedUser.username());
        Assertions.assertNotNull(verifiedUser.password());
        Assertions.assertEquals("juan@gmail.com", verifiedUser.email());
    }

    @Test
    public void verifyUserWithWrongPassword() throws DataAccessException {
        userDAO.createUser("JuanPablo", "JuanPablo123", "juanpablo@gmail.com");
        UserData verifiedUser = userDAO.verifyUser("JuanPablo", "JohnPaul123");

        Assertions.assertNull(verifiedUser);
    }

    @Test
    public void clearTest( ) throws DataAccessException {
        userDAO.createUser("Ryan", "ryan123", "ryan@gmail.com" );
        userDAO.createUser("Ty", "ty123", "ty@gmail.com");
        UserData user1 = userDAO.getUser("Ryan");
        UserData user2 = userDAO.getUser("Ty");
        Assertions.assertNotNull(user1);
        Assertions.assertNotNull(user2);

        userDAO.clear();

        user1 = userDAO.getUser("Ryan");
        user2 = userDAO.getUser("Ty");
        Assertions.assertNull(user1);
        Assertions.assertNull(user2);
    }

}
