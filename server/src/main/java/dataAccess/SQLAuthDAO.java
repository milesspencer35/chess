package dataAccess;

import model.AuthData;

public class SQLAuthDAO extends DAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
