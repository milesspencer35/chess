package dataAccess;

import model.GameData;
import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> Games;
    @Override
    public void clear() {
        Games.clear();
    }
}
