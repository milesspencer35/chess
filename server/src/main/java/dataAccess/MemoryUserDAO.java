package dataAccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    static private ArrayList<UserData> Users;

    @Override
    public void clear() {
        Users.clear();
    }
}
