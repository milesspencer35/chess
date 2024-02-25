package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Map;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
}
