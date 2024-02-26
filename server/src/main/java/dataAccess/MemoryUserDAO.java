package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    // Implementing singleton principle
    private static MemoryUserDAO instance;
    // private constructor to avoid client applications using the constructor
    private MemoryUserDAO(){}
    public static MemoryUserDAO getInstance() {
        if (instance == null) {
            instance = new MemoryUserDAO();
        }
        return instance;
    }

    private Map<String,UserData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        users.put(username, newUser);
    }

    @Override
    public UserData verifyUser(String username, String password) {
        UserData user = users.get(username);
        if (user != null && Objects.equals(user.password(), password)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public int numberOfUsers() {
        return users.size();
    }
}
