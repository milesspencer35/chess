package service;

import dataAccess.*;

public class ClearService {
    public void clearApplication() {
        try {
            UserDAO userDao = MemoryUserDAO.getInstance();
            userDao.clear();
            AuthDAO authDao = MemoryAuthDAO.getInstance();
            authDao.clear();
            GameDAO gameDao = MemoryGameDAO.getInstance();
            gameDao.clear();
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
