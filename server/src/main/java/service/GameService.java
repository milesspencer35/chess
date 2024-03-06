package service;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.memoryDao.MemoryAuthDAO;
import dataAccess.memoryDao.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import response.CreateGameResponse;
import response.ErrorResponse;
import response.ListGamesResponse;

import java.util.ArrayList;

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

    public ListGamesResponse listGames(String authToken) {
        ListGamesResponse listGamesResponse = null;

        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            AuthData userAuthData = authDAO.getAuth(authToken);
            if (userAuthData == null) {
                listGamesResponse = new ListGamesResponse(null, "Error: unauthorized");
                return listGamesResponse;
            }

            GameDAO gameDAO = MemoryGameDAO.getInstance();
            ArrayList<GameData> games = gameDAO.listGames();
            listGamesResponse = new ListGamesResponse(games, null);
        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        return listGamesResponse;
    }

    public ErrorResponse joinGame(String authToken, ChessGame.TeamColor color, Integer gameID) {
        ErrorResponse errorResponse;
        if (authToken == null || gameID == null) {
            errorResponse = new ErrorResponse("Error: bad request");
            return errorResponse;
        }

        try {
            AuthDAO authDAO = MemoryAuthDAO.getInstance();
            AuthData userAuthData = authDAO.getAuth(authToken);
            if (userAuthData == null) {
                errorResponse = new ErrorResponse("Error: unauthorized");
                return errorResponse;
            }

            GameDAO gameDAO = MemoryGameDAO.getInstance();
            GameData game = gameDAO.getGame(gameID);
            if (game == null) {
                errorResponse = new ErrorResponse("Error: bad request");
                return errorResponse;
            }
            if ((color == ChessGame.TeamColor.BLACK && game.blackUsername() != null)
                    || (color == ChessGame.TeamColor.WHITE && game.whiteUsername() != null)) {
                errorResponse = new ErrorResponse("Error: already taken");
                return errorResponse;
            }

            if (color != null) {
                gameDAO.joinGame(gameID, color, userAuthData.username());
            }

        } catch (DataAccessException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }
}
