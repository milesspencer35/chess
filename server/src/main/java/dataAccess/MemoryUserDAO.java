package dataAccess;

import model.UserData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String,UserData> Users = new HashMap<>();

    @Override
    public void clear() {
        Users.clear();
    }

    @Override
    public UserData getUser(String username) {
        return Users.get(username);
    }

    @Override
    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        Users.put(username, newUser);
    }

    @Override
    public UserData verifyUser(String username, String password) {
        UserData user = Users.get(username);
        if (user != null && user.password() == password) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public int numberOfUsers() {
        return Users.size();
    }
}
