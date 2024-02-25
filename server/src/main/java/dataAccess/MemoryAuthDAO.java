package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static MemoryAuthDAO instance;
    // private constructor to avoid client applications using the constructor
    private MemoryAuthDAO(){}
    public static MemoryAuthDAO getInstance() {
        if (instance == null) {
            instance = new MemoryAuthDAO();
        }
        return instance;
    }

    private Map<String,AuthData> Auths = new HashMap<>();

    @Override
    public void clear() {
        Auths.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        Auths.put(authToken, newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return Auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        Auths.remove(authToken);
    }

    @Override
    public int numberOfAuths() {
        return Auths.size();
    }
}
