package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
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
    public ArrayList<GameData> listGames() {
        ArrayList<GameData> arrayOfGames = new ArrayList<>();
        arrayOfGames.addAll(Games.values());
        return arrayOfGames;
    }

    @Override
    public GameData getGame(int gameID) {
        return Games.get(gameID);
    }

    @Override
    public void joinGame(Integer gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        GameData game = Games.get(gameID);
        Games.remove(gameID);
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        if (color == ChessGame.TeamColor.BLACK) {
            blackUsername = username;
        } else if (color == ChessGame.TeamColor.WHITE) {
            whiteUsername = username;
        }

        GameData newGame = new GameData(gameID, whiteUsername, blackUsername, game.gameName(), game.game());
        Games.put(gameID, newGame);
    }
}
