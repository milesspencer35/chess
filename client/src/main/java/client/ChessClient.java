package client;

import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
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
        System.out.println("\uD83D\uDC51 Welcome to Chess! \uD83D\uDC51");
        System.out.print(menuStart());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            if (authToken == null) {
                evalNotLoggedIn(line, scanner);
            } else {
                evalLoggedIn(line, scanner);
            }
        }
        System.out.println();
    }

    public void evalNotLoggedIn(String input, Scanner scanner) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "3";
//            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "1" -> {
                    register(scanner);
                    System.out.print(menuStart());
                }
                case "2" -> {
                    login(scanner);
                    System.out.print(menuStart());
                }
                case "4" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            System.out.println(SET_TEXT_COLOR_RED + ex.getMessage());
            System.out.print(SET_TEXT_COLOR_WHITE + menuStart());
        }
    }

    private void register(Scanner scanner) throws ResponseException {
        System.out.println("Create a username: ");
        String username = scanner.nextLine();
        System.out.println("Create a password: ");
        String password = scanner.nextLine();
        System.out.println("Enter a valid email: ");
        String email = scanner.nextLine();

        AuthData authData = server.register(username, password, email);
        this.authToken = authData.authToken();
    }

    private void login(Scanner scanner) throws ResponseException {
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        AuthData authData = server.login(username, password);
        this.authToken = authData.authToken();
    }

    private void help() {
        if (authToken == null) {
            System.out.print(
                    """
                    
                        Enter '1' to register an account.
                        Enter '2' to login to your account.
                        Enter '3' for to view this help menu.
                        Enter '4' to quit chess application.
                    """);
        } else {
            System.out.print("help");
        }
    }

    private void quit() {
        System.out.println("Quiting chess");
        System.exit(0);
    }

    public void evalLoggedIn(String input, Scanner scanner) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "6";
//            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "1" -> {
                    createGame(scanner);
                    System.out.print(menuStart());
                }
                case "2" -> listGames();
//                case "3" -> joinGame;
//                case "4" -> observe;
//                case "5" -> logout();
                case "7" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            System.out.println(SET_TEXT_COLOR_RED + ex.getMessage());
            System.out.print(SET_TEXT_COLOR_WHITE + menuStart());
        }
    }

    public void createGame(Scanner scanner) throws ResponseException {
        System.out.println("Name the game: ");
        String gameName = scanner.nextLine();

        server.createGame(gameName, this.authToken);
    }

    public void listGames() throws ResponseException {
        ArrayList<GameData> gamesList = server.listGames(this.authToken);
        for (int i = 0; i < gamesList.size(); i++) {
            System.out.println( (i + 1) + ". game name: " + gamesList.get(i).gameName()
                    + ", white player: " + gamesList.get(i).whiteUsername()
                    + ", black player: " + gamesList.get(i).blackUsername());
        }
    }

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
