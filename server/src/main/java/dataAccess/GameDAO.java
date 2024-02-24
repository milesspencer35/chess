package dataAccess;

import model.GameData;
import java.util.Map;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    Map<Integer,GameData> listGames();
    GameData getGame(int gameID);
}
