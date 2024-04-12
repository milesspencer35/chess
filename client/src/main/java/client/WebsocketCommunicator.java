package client;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
    Session session;
    ServerMessageObserver serverMessageObserver;

    public WebsocketCommunicator(String url, ServerMessageObserver serverMessageObserver) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    serverMessageObserver.notify(serverMessage, message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws ResponseException {
        try {
            var msg = new JoinPlayerMessage(authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(msg));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void observeGame(String authToken, Integer gameID) throws ResponseException {
        try {
            var msg = new ObserveGameMessage(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(msg));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move, String startPos, String endPos) throws ResponseException {
        try {
            var msg = new MakeMoveGameMessage(authToken, gameID, move, startPos, endPos);
            this.session.getBasicRemote().sendText(new Gson().toJson(msg));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws ResponseException {
        try {
            var msg = new LeaveGameMessage(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(msg));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            var msg = new ResignGameMessage(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(msg));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
