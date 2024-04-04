package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

        switch (command.getCommandType()) {
//            case JOIN_PLAYER -> join(command.getAuthString(), command.gameID, command.playerColor);
//            case JOIN_OBSERVER -> observe(command.getAuthString(), command.gameID);
//            case MAKE_MOVE -> move(command.getAuthString(), command.gameID, command.move));
//            case LEAVE -> leave(command.getAuthString(), command.gameID);
//            case RESIGN -> resign(command.getAuthString(), command.gameID);
        }
    }



}
