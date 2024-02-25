package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Map;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> listGames() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void joinGame(Integer gameID, ChessGame.TeamColor color, String username) throws DataAccessException;
}
