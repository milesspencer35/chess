package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import request.CreateGameRequest;
import request.JoinRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;

public class ServerFacade {

    private final String serverUrl;
    //private final ServerMessageObserver serverMessageObserver;

    public ServerFacade(String url) {
        serverUrl = url;
        //this.serverMessageObserver = serverMessageObserver;
    }

    public void clearApp() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public AuthData register(String username, String password, String email) throws ResponseException{
        UserData user = new UserData(username, password, email);
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws ResponseException {
        UserData user = new UserData(username, password, "");
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public int createGame(String gameName, String authToken) throws ResponseException {
        var path = "/game";
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        var createGameObj = this.makeRequest("POST", path, createGameRequest, CreateGameResponse.class, authToken);
        return createGameObj.gameID();
    }

    public void joinGame(ChessGame.TeamColor color, int gameID, String authToken) throws ResponseException {
        var path = "/game";
        JoinRequest joinRequest = new JoinRequest(color, gameID);
        this.makeRequest("PUT", path, joinRequest, null, authToken);
    }

    public ArrayList<GameData> listGames(String authToken) throws ResponseException {
        var path = "/game";
        ListGamesResponse resp = this.makeRequest("GET", path, null, ListGamesResponse.class, authToken);
        return resp.games();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            // Sets url
            URL url = (new URI(serverUrl + path)).toURL();
            // opens http connection
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            // sets the type of request (GET, POST,...)
            http.setRequestMethod(method);
            http.setDoOutput(true);

            setAuthToken(authToken, http);

            // adds the body to http request
            writeBody(request, http);
            http.connect();
            // throw error if not successful
            throwIfNotSuccessful(http);
            // read response body and return it
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void setAuthToken(String authToken, HttpURLConnection http) {
        if (authToken != null) {
            http.addRequestProperty("authorization", authToken);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream errorBody = http.getErrorStream()) {
                InputStreamReader reader = new InputStreamReader(errorBody);
                ErrorData errorMessage = new Gson().fromJson(reader, ErrorData.class);
                throw new ResponseException(status, "failure: " + status + " " + errorMessage.message());
            }
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
