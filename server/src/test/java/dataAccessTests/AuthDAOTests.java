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

        //Test that you can get the auth from database
        Assertions.assertEquals("Jack", authData.username());
        Assertions.assertNotNull(authData.authToken());

    }

}
