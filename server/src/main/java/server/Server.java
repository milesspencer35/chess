package server;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import model.GameData;
import model.UserData;
import request.JoinRequest;
import response.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.LinkedList;

public class Server {
    private final ClearService clearService = new ClearService();
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApp);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearApp(Request req, Response res) {
        clearService.clearApplication();
        res.status(200);
        return "";
    }

    private Object registerUser(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        RegisterResponse registerResponse = userService.register(user);
        res.status(determineStatusCode(registerResponse.message()));
        return new Gson().toJson(registerResponse);
    }

    private Object loginUser(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        LoginResponse loginResponse = userService.login(user);
        res.status(determineStatusCode(loginResponse.message()));
        return new Gson().toJson(loginResponse);
    }

    private Object logoutUser(Request req, Response res) {
        String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        ErrorResponse logOutErrorResponse = userService.logout(authToken);
        if (logOutErrorResponse == null) {
            res.status(200);
        } else {
            res.status(determineStatusCode(logOutErrorResponse.message()));
        }
        return new Gson().toJson(logOutErrorResponse);
    }

    private Object createGame(Request req, Response res) {
        String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        CreateGameResponse createGameResponse = gameService.createGame(authToken, game.gameName());
        res.status(determineStatusCode(createGameResponse.message()));
        return new Gson().toJson(createGameResponse);
    }

    private Object joinGame(Request req, Response res) {
        String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        JoinRequest joinInfo = new Gson().fromJson(req.body(), JoinRequest.class);
        ErrorResponse joinGameErrorResponse = gameService.joinGame(authToken, joinInfo.playerColor(), joinInfo.gameID());
        if (joinGameErrorResponse == null) {
            res.status(200);
        } else {
            res.status(determineStatusCode(joinGameErrorResponse.message()));
        }
        return new Gson().toJson(joinGameErrorResponse);
    }

    private Object listGames(Request req, Response res) {
        String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        ListGamesResponse listGamesResponse = gameService.listGames(authToken);
        res.status(determineStatusCode(listGamesResponse.message()));
        return new Gson().toJson(listGamesResponse);
    }

    private int determineStatusCode(String message) {
        switch(message){
            case null:
                return 200;
            case "Error: bad request":
                return 400;
            case "Error: unauthorized":
                return 401;
            case "Error: already taken":
                return 403;
            default:
                return 500;
        }
    }
}
