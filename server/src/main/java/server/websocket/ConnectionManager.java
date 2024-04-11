package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    // ConcurrentHashMap is multi thread safe
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Integer> games = new ConcurrentHashMap<>();

    public void addConnection(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void removeConnection(String authToken) {
        connections.remove(authToken);
    }

    public void addPlayer(String authToken, Integer gameID) {
        games.put(authToken, gameID);
    }

    public void removePlayer(String authToken, Integer gameID) {
        games.remove(authToken);
    }

    public void broadcastToOthers(String excludeAuthToken, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(serverMessage.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void broadcastToRoot(String rootAuthToken, ServerMessage serverMessage) throws IOException {
        //var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.authToken.equals(rootAuthToken)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            }
        }

        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.authToken);
//        }
    }
}
