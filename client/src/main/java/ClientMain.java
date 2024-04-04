import client.ChessClient;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:3030";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ChessClient(serverUrl).run();
    }
}
