package serviceTests;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.RegisterResponse;
import service.GameService;
import service.UserService;
import spark.utils.Assert;

public class GameServiceTests {
    GameService gameService = new GameService();
    UserService userService = new UserService();

    @Test
    public void CreateGameCorrectly() {
        UserData user = new UserData("Jimbo", "Clemson24", "jimbo@gmail.com");
        RegisterResponse registerResponse = userService.register(user);

        CreateGameResponse createGameResponse = gameService.createGame(registerResponse.authToken(), "Game 1");
        Assertions.assertNotNull(createGameResponse);
        Assertions.assertEquals(1, createGameResponse.gameID());
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

}
