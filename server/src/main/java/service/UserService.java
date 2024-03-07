package service;

import dataAccess.*;
import dataAccess.memoryDao.MemoryAuthDAO;
import dataAccess.memoryDao.MemoryUserDAO;
import model.*;
import response.ErrorResponse;
import response.LoginResponse;
import response.RegisterResponse;

public class UserService {
    public RegisterResponse register(UserData user) {
        RegisterResponse newResponse = null;

        if (user.username() == null || user.password() == null || user.email() == null) {
            newResponse = new RegisterResponse(null, null, "Error: bad request");
            return newResponse;
        }

        try {
            UserDAO userDao = new SQLUserDAO();
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            UserData preExistingUser = userDao.getUser(user.username());
            if (preExistingUser == null) {
                userDao.createUser(user.username(), user.password(), user.email());
                AuthData newAuth = authDAO.createAuth(user.username());
                newResponse = new RegisterResponse(newAuth.authToken(), newAuth.username(), null);
            } else {
                newResponse = new RegisterResponse(null, null, "Error: already taken");
            }
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        return newResponse;
    }

    public LoginResponse login(UserData user) {
        LoginResponse loginResponse = null;
        try {
            UserDAO userDAO = MemoryUserDAO.getInstance();
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            UserData verifiedUser = userDAO.verifyUser(user.username(), user.password());
            if (verifiedUser == null) {
                return new LoginResponse(null, null, "Error: unauthorized");
            }

            AuthData newAuth = authDAO.createAuth(verifiedUser.username());
            loginResponse = new LoginResponse(newAuth.authToken(), newAuth.username(), null);

        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return loginResponse;
    }
    public ErrorResponse logout(String authToken) {
        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            AuthData userAuthData = authDAO.getAuth(authToken);
            if (userAuthData == null) {
                ErrorResponse errorResponse = new ErrorResponse("Error: unauthorized");
                return errorResponse;
            }

            authDAO.deleteAuth(userAuthData.authToken());
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
