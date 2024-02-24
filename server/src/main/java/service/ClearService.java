package service;

import dataAccess.*;

public class ClearService {
    public void clearApplication() {
        UserDAO userDao = MemoryUserDAO.getInstance();
        userDao.clear();
        AuthDAO authDao = MemoryAuthDAO.getInstance();
        authDao.clear();
        GameDAO gameDao = MemoryGameDAO.getInstance();
        gameDao.clear();
    }
}
