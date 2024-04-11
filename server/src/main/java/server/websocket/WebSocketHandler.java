package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

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
        connections.addConnection(command.getAuthString(), session);
        connections.addPlayer(command.getAuthString(), command.getGameID());

        GameDAO gameDAO = new SQLGameDAO();
        GameData gameData = gameDAO.getGame(command.getGameID());

        ServerMessage loadGameMessage = new LoadGameMessage(gameData.game());
        connections.broadcastToRoot(command.getAuthString(), loadGameMessage);

        UserDAO userDAO = new SQLUserDAO();

        ServerMessage notificationMessage = new NotificationMessage("User joined game");
    }

    private void observe(String message, Session session) {
        ObserveGameMessage command = new Gson().fromJson(message, ObserveGameMessage.class);
        connections.addConnection(command.getAuthString(), session);
        connections.addPlayer(command.getAuthString(), command.getGameID());
    }

    private void move(String message) {
        MakeMoveGameMessage command = new Gson().fromJson(message, MakeMoveGameMessage.class);

    }

    private void leave(String message) {
        LeaveGameMessage command = new Gson().fromJson(message, LeaveGameMessage.class);
        connections.removeConnection(command.getAuthString());
        connections.removePlayer(command.getAuthString(), command.getGameID());

    }

    private void resign(String message) {
        ResignGameMessage command = new Gson().fromJson(message, ResignGameMessage.class);

    }

}
