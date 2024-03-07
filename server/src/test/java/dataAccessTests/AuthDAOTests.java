package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthDAOTests {

    AuthDAO authDAO = new SQLAuthDAO();

    @AfterEach
    public void beforeEach() throws DataAccessException {
        authDAO.clear();
    }

    public AuthDAOTests() throws DataAccessException {
    }

    @Test
    public void createAuthCorrectly() throws DataAccessException {
        AuthData authData = authDAO.createAuth("Jack");
        AuthData getAuth = authDAO.getAuth(authData.authToken());

        //Test that you can get the auth from database
        Assertions.assertEquals("Jack", authData.username());
        Assertions.assertNotNull(authData.authToken());
        Assertions.assertNotNull(getAuth);
    }

    @Test
    public void createAuthForUserThatAlreadyHasAuth() throws DataAccessException {
        authDAO.createAuth("Aaron");

        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth("Aaron"));
    }

    @Test
    public void getAuthCorrectly() throws DataAccessException {
        AuthData authData = authDAO.createAuth("Harvey");

        AuthData getAuth = authDAO.getAuth(authData.authToken());
        Assertions.assertEquals(authData.authToken(), getAuth.authToken());
        Assertions.assertEquals(authData.username(), getAuth.username());
    }

    @Test
    public void getAuthThatDoesNotExist() throws DataAccessException {
        AuthData getAuth = authDAO.getAuth("123");

        Assertions.assertNull(getAuth);
    }

    @Test
    public void deleteAuthCorrectly() throws DataAccessException {
        AuthData authData = authDAO.createAuth("LJ");
        Assertions.assertNotNull(authDAO.getAuth(authData.authToken()));

        authDAO.deleteAuth(authData.authToken());
        Assertions.assertNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void deleteAuthThatDoesNotExist() throws DataAccessException {
        AuthData authData = authDAO.createAuth("Hinkley");

        authDAO.deleteAuth(authData.authToken()+ "1");
        Assertions.assertNotNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void clearCorrectly() throws DataAccessException {
        AuthData authData1 = authDAO.createAuth("Charlie");
        AuthData authData2 = authDAO.createAuth("Jovesa");

        Assertions.assertNotNull(authDAO.getAuth(authData1.authToken()));
        Assertions.assertNotNull(authDAO.getAuth(authData2.authToken()));

        authDAO.clear();

        Assertions.assertNull(authDAO.getAuth(authData1.authToken()));
        Assertions.assertNull(authDAO.getAuth(authData2.authToken()));
    }
}
