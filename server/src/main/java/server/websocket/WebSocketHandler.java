package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;
import webSocketMessages.userCommands.ObserveGameMessage;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(message);
            case JOIN_OBSERVER -> observe(message);
            case MAKE_MOVE -> move(message);
            case LEAVE -> leave(message);
            case RESIGN -> resign(message);
        }
    }

    private void join(String message) {
        JoinPlayerMessage command = new Gson().fromJson(message, JoinPlayerMessage.class);

    }

    private void observe(String message) {
        ObserveGameMessage command = new Gson().fromJson(message, ObserveGameMessage.class);

    }

    private void move(String message) {

    }

    private void leave(String message) {

    }

    private void resign(String message) {

    }

}
