package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.*;

public class UserService {
    AuthData register(UserData user) {
        UserDAO userDao = MemoryUserDAO.getInstance();
        AuthDAO authDAO = MemoryAuthDAO.getInstance();
        UserData preExistingUser = userDao.getUser(user.username());
        if (preExistingUser == null) {
            userDao.createUser(user.username(), user.password(), user.password());
            AuthData newAuth = authDAO.createAuth(user.username());
            return newAuth;
        } else {
            // Already taken, how do I do the error in here?
            return null;
        }
    }
    AuthData login(UserData user) {return null;}
    void logout(AuthData authToken) {}

}
