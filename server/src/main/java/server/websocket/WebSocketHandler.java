package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(message, session);
            case JOIN_OBSERVER -> observe(message, session);
            case MAKE_MOVE -> move(message);
            case LEAVE -> leave(message);
            case RESIGN -> resign(message);
        }
    }

    private void join(String message, Session session) throws DataAccessException, IOException {
        JoinPlayerMessage command = new Gson().fromJson(message, JoinPlayerMessage.class);
        GameDAO gameDAO = new SQLGameDAO();
        GameData gameData = gameDAO.getGame(command.getGameID());
        AuthDAO authDAO = new SQLAuthDAO();
        AuthData authData = authDAO.getAuth(command.getAuthString());
        connections.addConnection(command.getAuthString(), session);

        if (gameData == null) {
            ServerMessage errorMessage = new ErrorMessage("Error: Game doesn't exist");
            connections.broadcastToRoot(command.getAuthString(), errorMessage);
            return;
        } else if (authData == null) {
            ServerMessage errorMessage = new ErrorMessage("Error: Bad authToken");
            connections.broadcastToRoot(command.getAuthString(), errorMessage);
            return;
        } else if (gameData.whiteUsername() == null && gameData.blackUsername() == null) {
            ServerMessage errorMessage = new ErrorMessage("Error: Game doesn't exist");
            connections.broadcastToRoot(command.getAuthString(), errorMessage);
            return;
        } else if (command.getPlayerColor() == ChessGame.TeamColor.WHITE && !gameData.whiteUsername().equals(authData.username())
                || command.getPlayerColor() == ChessGame.TeamColor.BLACK && !gameData.blackUsername().equals(authData.username())) {
            ServerMessage errorMessage = new ErrorMessage("Error: Spot already taken");
            connections.broadcastToRoot(command.getAuthString(), errorMessage);
            return;
        }

        //connections.addConnection(command.getAuthString(), session);
        connections.addPlayer(command.getAuthString(), command.getGameID());

        ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
        connections.broadcastToRoot(command.getAuthString(), loadGameMessage);

        ServerMessage notificationMessage = new NotificationMessage(authData.username() + " joined the game");
        connections.broadcastToOthers(command.getAuthString(), notificationMessage, command.getGameID());
    }

    private void observe(String message, Session session) throws DataAccessException, IOException {
        ObserveGameMessage command = new Gson().fromJson(message, ObserveGameMessage.class);
        GameDAO gameDAO = new SQLGameDAO();
        GameData gameData = gameDAO.getGame(command.getGameID());
        AuthDAO authDAO = new SQLAuthDAO();
        AuthData authData = authDAO.getAuth(command.getAuthString());
        connections.addConnection(command.getAuthString(), session);

        if (gameData == null) {
            ServerMessage errorMessage = new ErrorMessage("Error: Game doesn't exist");
            connections.broadcastToRoot(command.getAuthString(), errorMessage);
            return;
        } else if (authData == null) {
            ServerMessage errorMessage = new ErrorMessage("Error: Bad authToken");
            connections.broadcastToRoot(command.getAuthString(), errorMessage);
            return;
        }

        connections.addPlayer(command.getAuthString(), command.getGameID());
        ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
        connections.broadcastToRoot(command.getAuthString(), loadGameMessage);
        ServerMessage notificationMessage = new NotificationMessage(authData.username() + " is observing the game");
        connections.broadcastToOthers(command.getAuthString(), notificationMessage, command.getGameID());
    }

    private void move(String message) throws DataAccessException, IOException {
        MakeMoveGameMessage command = new Gson().fromJson(message, MakeMoveGameMessage.class);
        SQLGameDAO gameDAO = new SQLGameDAO();
        GameData gameData = gameDAO.getGame(command.getGameID());
        AuthDAO authDAO = new SQLAuthDAO();
        AuthData authData = authDAO.getAuth(command.getAuthString());
        try {
            gameData.game().makeMove(command.getMove());
        } catch (InvalidMoveException ex) {
            System.out.print(ex);
        }

        gameDAO.updateGame(gameData.gameID(),
                gameData.whiteUsername() == null ? null : gameData.whiteUsername(),
                gameData.blackUsername() == null ? null : gameData.blackUsername(),
                gameData.game());

        LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
        connections.broadcastToOthers(null, loadGameMessage, gameData.gameID());
        NotificationMessage notificationMessage = new NotificationMessage
                (authData.username() + " moved piece at " + command.getStartPos() + " to " + command.getEndPos());
        connections.broadcastToOthers(command.getAuthString(), notificationMessage, command.getGameID());
    }

    private void leave(String message) throws DataAccessException, IOException {
        LeaveGameMessage command = new Gson().fromJson(message, LeaveGameMessage.class);
        AuthDAO authDAO = new SQLAuthDAO();
        AuthData authData = authDAO.getAuth(command.getAuthString());
        SQLGameDAO gameDAO = new SQLGameDAO();
        GameData gameData = gameDAO.getGame(command.getGameID());

        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(authData.username())) {
            gameDAO.updateGame(gameData.gameID(), null, gameData.blackUsername(), gameData.game());
        } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(authData.username())) {
            gameDAO.updateGame(gameData.gameID(), gameData.whiteUsername(), null, gameData.game());
        }
        ServerMessage notificationMessage = new NotificationMessage(authData.username() + " left the game");
        connections.broadcastToOthers(command.getAuthString(), notificationMessage, command.getGameID());
        connections.removeConnection(command.getAuthString());
        connections.removePlayer(command.getAuthString(), command.getGameID());
    }

    private void resign(String message) {
        ResignGameMessage command = new Gson().fromJson(message, ResignGameMessage.class);

    }

}
