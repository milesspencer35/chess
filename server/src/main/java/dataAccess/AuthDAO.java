package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(String username);
    AuthData getAuth(String authToken);
    int numberOfAuths();
}
