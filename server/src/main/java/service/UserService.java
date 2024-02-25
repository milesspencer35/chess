package service;

import dataAccess.*;
import model.*;
import response.RegisterResponse;

public class UserService {
    public RegisterResponse register(UserData user) {
        RegisterResponse newResponse = null;

        if (user.username() == null || user.password() == null || user.email() == null) {
            newResponse = new RegisterResponse(null, null, "Error: bad request");
            return newResponse;
        }

        try {
            UserDAO userDao = MemoryUserDAO.getInstance();
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            UserData preExistingUser = userDao.getUser(user.username());
            if (preExistingUser == null) {
                userDao.createUser(user.username(), user.password(), user.password());
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

    AuthData login(UserData user) {return null;}
    void logout(AuthData authToken) {}

}
