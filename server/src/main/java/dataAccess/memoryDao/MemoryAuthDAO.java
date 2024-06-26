package dataAccess.memoryDao;

import dataAccess.AuthDAO;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static MemoryAuthDAO instance;
    // private constructor to avoid client applications using the constructor
    private MemoryAuthDAO(){}

    private Map<String,AuthData> auths = new HashMap<>();

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        auths.put(authToken, newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

}
