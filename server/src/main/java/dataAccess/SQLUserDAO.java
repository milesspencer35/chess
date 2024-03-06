package dataAccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public UserData verifyUser(String username, String password) throws DataAccessException {
        return null;
    }
}
