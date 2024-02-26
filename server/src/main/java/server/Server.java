package server;

import com.google.gson.Gson;
import model.UserData;
import response.LoginResponse;
import response.RegisterResponse;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {
    private final ClearService clearService = new ClearService();
    private final UserService userService = new UserService();


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.init();
        Spark.delete("/db", this::clearApp);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);

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
        if (registerResponse.message() == null) {
            res.status(200);
        } else {
            res.status(determineStatusCode(registerResponse.message()));
        }
        return new Gson().toJson(registerResponse);
    }

    private Object loginUser(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        LoginResponse loginResponse = userService.login(user);
        if (loginResponse.message() == null) {
            res.status(200);
        } else {
            res.status(determineStatusCode(loginResponse.message()));
        }
        return new Gson().toJson(loginResponse);
    }

    private int determineStatusCode(String message) {
        switch(message){
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
