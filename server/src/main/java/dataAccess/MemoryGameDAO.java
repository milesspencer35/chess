package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private static MemoryGameDAO instance;
    // private constructor to avoid client applications using the constructor
    private MemoryGameDAO(){}
    public static MemoryGameDAO getInstance() {
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
    }

    private Map<Integer,GameData> Games = new HashMap<>();
    private int nextGameID = 0;
    @Override
    public void clear() {
        Games.clear();
    }

    @Override
    public int createGame(String gameName) {
        nextGameID++;
        ChessGame chessGame = new ChessGame();
        GameData newGame = new GameData(nextGameID, null, null, gameName, chessGame);
        Games.put(nextGameID, newGame);
        return nextGameID;
    }

    @Override
    public Map<Integer,GameData> listGames() {
        return Games;
    }

    @Override
    public GameData getGame(int gameID) {
        return Games.get(gameID);
    }
}
