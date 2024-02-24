package dataAccess;

import model.AuthData;
import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    static private ArrayList<AuthData> Auths;

    @Override
    public void clear() {
        Auths.clear();
    }
}
