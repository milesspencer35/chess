package serviceTests;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import response.CreateGameResponse;
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


}
