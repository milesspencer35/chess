package service;

import dataAccess.*;

public class ClearService {
    public void clearApplication() {
        UserDAO userDao = new MemoryUserDAO();
        userDao.clear();
        AuthDAO authDao = new MemoryAuthDAO();
        authDao.clear();
        GameDAO gameDao = new MemoryGameDAO();
        gameDao.clear();
    }
}
