package service;

import dataAccess.*;
import model.AuthData;
import response.CreateGameResponse;

public class GameService {
    public CreateGameResponse createGame(String authToken, String gameName) {
        CreateGameResponse createGameResponse = null;
        if (authToken == null || gameName == null) {
            createGameResponse = new CreateGameResponse(null, "Error: bad request");
            return createGameResponse;
        }

        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();

            AuthData userAuthData = authDAO.getAuth(authToken);
            if (userAuthData == null) {
                createGameResponse = new CreateGameResponse(null, "Error: unauthorized");
                return createGameResponse;
            }

            GameDAO gameDAO = MemoryGameDAO.getInstance();
            int gameID = gameDAO.createGame(gameName);
            createGameResponse = new CreateGameResponse(gameID, null);
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        return createGameResponse;
    }
}
