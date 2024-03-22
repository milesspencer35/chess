package serviceTests;


import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import response.CreateGameResponse;
import response.RegisterResponse;
import service.ClearService;
import service.GameService;
import service.UserService;

public class ClearServiceTests {

    private ClearService clearService = new ClearService();
    private UserService userService = new UserService();
    private GameService gameService = new GameService();

    @BeforeEach
    public void setup() {
        clearService.clearApplication();
    }

    @Test
    public void testClearApplication() {

        UserData user1 = new UserData("Miles", "Hello123", "miles@gmail.com");
        UserData user2 = new UserData("Jamal", "BYU123", "jamal@gmail.com");
        RegisterResponse registerResponse1 = userService.register(user1);
        RegisterResponse registerResponse2 = userService.register(user2);
        Assertions.assertNotNull(userService.login(user1).authToken(), "User1 exists");
        Assertions.assertNotNull(userService.login(user2).authToken(), "User2 exists");

        CreateGameResponse createGameResponse1 = gameService.createGame(registerResponse1.authToken(), "GameTime!");
        CreateGameResponse createGameResponse2 = gameService.createGame(registerResponse2.authToken(), "GameTime2");
        Assertions.assertNotNull(gameService.listGames(registerResponse1.authToken()).games(), "There are games");

        clearService.clearApplication();
        Assertions.assertNull(userService.login(user1).authToken());
        Assertions.assertNull(userService.login(user2).authToken());
        Assertions.assertNull(gameService.listGames(registerResponse1.authToken()).games());

    }
}
