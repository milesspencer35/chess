package dataAccess;

import model.UserData;

public interface UserDAO {
    void clear();
    UserData getUser(String username);
    void createUser(String username, String password, String email);
    int numberOfUsers();

}
