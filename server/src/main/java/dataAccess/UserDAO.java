package dataAccess;

import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createUser(String username, String password, String email) throws DataAccessException;
    UserData verifyUser(String username, String password) throws DataAccessException;
    int numberOfUsers();
}
