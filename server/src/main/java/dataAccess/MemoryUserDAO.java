package dataAccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    static ArrayList<UserData> Users;

    public void clear() {
        Users.clear();
    }
}
