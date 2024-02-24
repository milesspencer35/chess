package service;

import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;

public class ClearService {
    public void clearApplication() {
        UserDAO userDao = new MemoryUserDAO();
        userDao.clear();

        
    }

}
