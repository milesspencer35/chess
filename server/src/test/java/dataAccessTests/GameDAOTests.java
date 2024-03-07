package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDAOTests {

    GameDAO gameDAO = new SQLGameDAO();

    public GameDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    public void createGameCorrectly() throws DataAccessException {
        int gameID = gameDAO.createGame("THE GAME");
        Assertions.assertNotEquals(0, gameID);
    }

    @Test
    public void listGamesCorrectly() throws DataAccessException {
        gameDAO.createGame("Gambo");
        gameDAO.createGame("Game Time");

        ArrayList<GameData> games = gameDAO.listGames();
        Assertions.assertNotNull(games);
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void listGamesWhenThereAreNoGames() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.listGames();

        Assertions.assertEquals(0, games.size());
    }

    @Test
    public void getGameCorrectly() throws DataAccessException {
        int gameID = gameDAO.createGame("I love chess");
        GameData game = gameDAO.getGame(gameID);

        Assertions.assertNotNull(game);
        Assertions.assertEquals("I love chess", game.gameName());
    }

    @Test
    public void getGameThatDoesNotExist() throws DataAccessException {
        GameData game = gameDAO.getGame(10000);

        Assertions.assertNull(game);
    }

    @Test
    public void joinGameBlackPlayerCorrectly() throws DataAccessException {
        int gameID = gameDAO.createGame("Chess Time");

        Assertions.assertDoesNotThrow(() -> gameDAO.joinGame(gameID, ChessGame.TeamColor.BLACK, "Creg"));
    }

    @Test
    public void joinWithBadGameID() {
        Assertions.assertThrows(DataAccessException.class,
                () -> gameDAO.joinGame(10000, ChessGame.TeamColor.BLACK, "Jimmy"));
    } 
}
