package clientTests;

import client.ResponseException;
import client.ServerFacade;
import org.junit.jupiter.api.*;
import response.RegisterResponse;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setup() throws Exception {
        facade.clearApp();
    }


    @Test
    void registerCorrectly() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        Assertions.assertEquals(authData.username(), "player1");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerUserTwice() throws ResponseException {
        var authData = facade.register("Miles", "password", "miles@gmail.com");
        Assertions.assertEquals(authData.username(), "Miles");
        Assertions.assertTrue(authData.authToken().length() > 10);

        Assertions.assertThrows(ResponseException.class, () -> facade.register("Miles", "password", "miles@gmail.com"));
    }
}
