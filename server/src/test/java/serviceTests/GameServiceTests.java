package serviceTests;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import response.CreateGameResponse;
import response.ErrorResponse;
import response.ListGamesResponse;
import response.RegisterResponse;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.utils.Assert;

import java.util.ArrayList;
import java.util.stream.Stream;

public class GameServiceTests {
    GameService gameService = new GameService();
    UserService userService = new UserService();
    ClearService clearService = new ClearService();

    @BeforeEach
    public void setup() {
        clearService.clearApplication();
    }


    @Test
    public void CreateGameCorrectly() {
        UserData user = new UserData("Jimbo", "Clemson24", "jimbo@gmail.com");
        RegisterResponse registerResponse = userService.register(user);

        CreateGameResponse createGameResponse = gameService.createGame(registerResponse.authToken(), "Game 1");
        Assertions.assertNotNull(createGameResponse);
        Assertions.assertNotNull(createGameResponse.gameID());
        Assertions.assertNull(createGameResponse.message());
    }

    @Test
    public void CreateGameSendsBadRequestMessage() {
        CreateGameResponse createGameResponse = gameService.createGame("1234", null);

        Assertions.assertNotNull(createGameResponse);
        Assertions.assertNull(createGameResponse.gameID());
        Assertions.assertEquals("Error: bad request", createGameResponse.message());
    }

    @Test
    public void NotAuthorizedToCreateGame() {
        CreateGameResponse createGameResponse = gameService.createGame("1234", "game2");

        Assertions.assertNotNull(createGameResponse);
        Assertions.assertNull(createGameResponse.gameID());
        Assertions.assertEquals("Error: unauthorized", createGameResponse.message());
    }

    @Test
    public void listGamesCorrectly() {
        UserData user = new UserData("K'naan", "WavinFlag", "knaan@gmail.com");
        RegisterResponse registerResponse = userService.register(user);

        gameService.createGame(registerResponse.authToken(), "Game 3");
        ListGamesResponse listGamesResponse = gameService.listGames(registerResponse.authToken());
        Assertions.assertNotNull(listGamesResponse);
        Assertions.assertNotNull(listGamesResponse.games());
        Assertions.assertNotEquals(0, listGamesResponse.games().size());
        Assertions.assertNull(listGamesResponse.message());
    }

    @Test
    public void NotAuthorizedToListGames() {
        ListGamesResponse listGamesResponse = gameService.listGames("1234");

        Assertions.assertNotNull(listGamesResponse);
        Assertions.assertNull(listGamesResponse.games());
        Assertions.assertNotNull(listGamesResponse.message());
        Assertions.assertEquals("Error: unauthorized", listGamesResponse.message());
    }

    @Test
    public void WhitePlayerJoinGame() {
        UserData user = new UserData("Michael", "BigMike", "michael@gmail.com");
        RegisterResponse registerResponse = userService.register(user);

        CreateGameResponse createGameResponse = gameService.createGame(registerResponse.authToken(), "Michael's game");
        ErrorResponse errorResponse = gameService.joinGame(registerResponse.authToken(),
                ChessGame.TeamColor.BLACK, createGameResponse.gameID());
        Assertions.assertNull(errorResponse);
        ListGamesResponse listGamesResponse = gameService.listGames(registerResponse.authToken());
        ArrayList<GameData> games = listGamesResponse.games();
        GameData updatedGame = (GameData) games.stream().filter(game -> game.gameID() == createGameResponse.gameID()).toArray()[0];
        Assertions.assertEquals("Michael", updatedGame.blackUsername());
    }

    @Test
    public void WhiteAndBlackPlayerJoinGame() {
        UserData user1 = new UserData("Brynn", "Brynncess", "brynn@gmail.com");
        UserData user2 = new UserData("Maren", "Maren123", "maren@gmail.com");
        RegisterResponse registerResponse1 = userService.register(user1);
        RegisterResponse registerResponse2 = userService.register(user2);

        CreateGameResponse createGameResponse =
                gameService.createGame(registerResponse1.authToken(), "BrynnvsMaren");
        ErrorResponse errorResponse1 = gameService.joinGame(registerResponse1.authToken(),
                ChessGame.TeamColor.BLACK, createGameResponse.gameID());
        ErrorResponse errorResponse2 = gameService.joinGame(registerResponse2.authToken(),
                ChessGame.TeamColor.WHITE, createGameResponse.gameID());

        Assertions.assertNull(errorResponse1);
        Assertions.assertNull(errorResponse2);

        ListGamesResponse listGamesResponse = gameService.listGames(registerResponse1.authToken());
        ArrayList<GameData> games = listGamesResponse.games();
        GameData updatedGame = (GameData) games.stream().filter(game -> game.gameID() == createGameResponse.gameID()).toArray()[0];


        Assertions.assertEquals("Brynn",updatedGame.blackUsername());
        Assertions.assertEquals("Maren", updatedGame.whiteUsername());
    }

    @Test
    public void TryJoinGameWithBadRequest() {
        ErrorResponse errorResponse = gameService.joinGame("1234", ChessGame.TeamColor.BLACK, null);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("Error: bad request",errorResponse.message());
    }

    @Test
    public void TryJoinGameWithoutAuthorization() {
        ErrorResponse errorResponse = gameService.joinGame("1234", ChessGame.TeamColor.WHITE, 1);
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("Error: unauthorized", errorResponse.message());
    }

    @Test
    public void TryJoinGameIntoSpotAlreadyTaken() {
        UserData user1 = new UserData("Calvin", "Megatron", "calvin@gmail.com");
        UserData user2 = new UserData("AJ", "Green", "aj@gmail.com");
        RegisterResponse registerResponse1 = userService.register(user1);
        RegisterResponse registerResponse2 = userService.register(user2);

        CreateGameResponse createGameResponse =
                gameService.createGame(registerResponse1.authToken(), "CalvinvsAJ");

        ErrorResponse errorResponse1 = gameService.joinGame(registerResponse1.authToken(),
                ChessGame.TeamColor.WHITE, createGameResponse.gameID());
        ErrorResponse errorResponse2 = gameService.joinGame(registerResponse2.authToken(),
                ChessGame.TeamColor.WHITE, createGameResponse.gameID());

        Assertions.assertNull(errorResponse1);
        Assertions.assertNotNull(errorResponse2);

        Assertions.assertEquals("Error: already taken", errorResponse2.message());
    }


}
