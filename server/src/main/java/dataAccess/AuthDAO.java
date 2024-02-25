package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    int numberOfAuths();
}
