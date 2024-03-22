package client;

import exception.ResponseException;

import java.util.Scanner;
import static ui.EscapeSequences.*;



public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    String authToken = null;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to Chess! Sign in to start.");
        System.out.print(menuStart());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                // result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLACK + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

//    public String evalNotLoggedIn(String input) {
//        try {
//            var tokens = input.toLowerCase().split(" ");
//            var cmd = (tokens.length > 0) ? tokens[0] : "3";
////            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
//            return switch (cmd) {
//                case "1" -> register();
//                case "2" -> login();
//                case "3" -> help();
//                case "4" -> quit();
//                default -> help();
//            };
//        } catch (ResponseException ex) {
//            return ex.getMessage();
//        }
//    }

//    public String evalLoggedIn(String input) {
//        try {
//            var tokens = input.toLowerCase().split(" ");
//            var cmd = (tokens.length > 0) ? tokens[0] : "6";
////            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
//            return switch (cmd) {
//                case "1" -> createGame();
//                case "2" -> listGames();
//                case "3" -> joinGame;
//                case "4" -> observe;
//                case "5" -> logout();
//                case "6" -> help();
//                case "7" -> quit();
//                default -> help();
//            };
//        } catch (ResponseException ex) {
//            return ex.getMessage();
//        }
//    }


    private void printPrompt() {
        System.out.print("\n" + "\u001B[0m" + ">>> " + SET_TEXT_COLOR_WHITE);
    }

    public String menuStart() {
        if (authToken == null) {
            return """
                    
                    1. register
                    2. login
                    3. help
                    4. quit
                    """;
        }
        return """
                1. create a game
                2. list games
                3. join a game
                4. observe a game
                5. logout
                6. help
                7. quit
                """;
    }

}
