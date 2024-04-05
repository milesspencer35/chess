package client;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import ui.DrawChessBoard;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.*;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageObserver{
    private final ServerFacade server;
    private final String serverUrl;
    private Map<Integer, GameData> listOfGames = new HashMap<>();
    private String authToken = null;
    private WebsocketCommunicator ws;
    private boolean inGame = false;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl, this);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC51 Welcome to Chess! \uD83D\uDC51");
        System.out.print(menuStart());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (true) {
            printPrompt();
            String line = scanner.nextLine();

            if (authToken == null) {
                evalNotLoggedIn(line, scanner);
            } else if (inGame == false){
                evalLoggedIn(line, scanner);
            } else {
                evalInGame(line, scanner);
            }
        }
    }

    public void evalNotLoggedIn(String input, Scanner scanner) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "3";
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
                    
                        Enter (1) to register an account.
                        Enter (2) to login to your account.
                        Enter (3) for to view this help menu.
                        Enter (4) to quit the chess application.
                    """);
        } else {
            System.out.print(
                    """
                      
                    Enter (1) to create a new game.
                    Enter (2) to list the current games.
                    Enter (3) to join a game.
                    Enter (4) to enter a game as an observer.
                    Enter (5) to logout.
                    Enter (6) to display this help menu.
                    Enter (7) to quit the chess application.
                    """);
        }
    }

    private void quit() {
        System.out.println("Quiting chess");
        System.exit(0);
    }

    private void evalLoggedIn(String input, Scanner scanner) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "6";
            switch (cmd) {
                case "1" -> createGame(scanner);
                case "2" -> listGames();
                case "3" -> {
                    joinGame(scanner);
                    System.out.print(menuStart());
                }
                case "4" -> {
                    observe(scanner);
                    System.out.print(menuStart());
                }
                case "5" -> {
                    logout();
                    System.out.print(menuStart());
                }
                case "7" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            System.out.println(SET_TEXT_COLOR_RED + ex.getMessage());
            System.out.print(SET_TEXT_COLOR_WHITE + menuStart());
        }
    }

    private void createGame(Scanner scanner) throws ResponseException {
        System.out.println("Name the game: ");
        String gameName = scanner.nextLine();

        server.createGame(gameName, this.authToken);
    }

    private void listGames() throws ResponseException {
        ArrayList<GameData> gamesList = server.listGames(this.authToken);
        listOfGames.clear();
        for (int i = 0; i < gamesList.size(); i++) {
            listOfGames.put((i+1), gamesList.get(i));
        }

        for (Map.Entry<Integer, GameData> set : listOfGames.entrySet() ) {
            System.out.println("\t" + set.getKey() + ". game name: " + set.getValue().gameName()
                    + ", white player: " + set.getValue().whiteUsername()
                    + ", black player: " + set.getValue().blackUsername());
        }
    }

    private void joinGame(Scanner scanner) throws ResponseException {
        int gameID;
        try {
            System.out.println("Enter the id for the game you want to join.");
            gameID = Integer.parseInt(scanner.nextLine());
        } catch (Exception ex) {
            throw new ResponseException(500, "Please enter a valid game id or view the list of games to refresh games");
        }
        System.out.println("Would you like to be the (1) white player or (2) black player. Enter number.");
        String colorNum = scanner.nextLine();
        ChessGame.TeamColor color = null;
        if (colorNum.equals("1")) {
            color = ChessGame.TeamColor.WHITE;
        } else {
            color = ChessGame.TeamColor.BLACK;
        }

        GameData selectedGame = listOfGames.get(gameID);
        if (selectedGame == null) {
            throw new ResponseException(500, "Please enter a valid game id or view the list of games to refresh games");
        }

        server.joinGame(color, selectedGame.gameID(), authToken);
        ws = new WebsocketCommunicator(serverUrl, this);
        ws.joinGame(authToken, selectedGame.gameID(), color);
        DrawChessBoard.drawBoard(selectedGame.game(), color);
        inGame = true;
    }

    private void observe(Scanner scanner) throws ResponseException {
        int gameID;
        try {
            System.out.println("Enter the id for the game you want to observe.");
            gameID = Integer.parseInt(scanner.nextLine());
        } catch (Exception ex) {
            throw new ResponseException(500, "Please enter a valid game id or view the list of games to refresh games");
        }

        GameData selectedGame = listOfGames.get(gameID);
        if (selectedGame == null) {
            throw new ResponseException(500, "Please enter a valid game id or view the list of games to refresh games");
        }

        server.joinGame(null, selectedGame.gameID(), authToken);
        ws = new WebsocketCommunicator(serverUrl, this);
        ws.observeGame(authToken, selectedGame.gameID());
        DrawChessBoard.drawBoard(selectedGame.game(), ChessGame.TeamColor.WHITE);
        inGame = true;
    }

    private void logout() throws ResponseException {
        server.logout(this.authToken);
        this.authToken = null;
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
        } else if (inGame == false) {
            return """
                    
                    1. create a game
                    2. list games
                    3. join a game
                    4. observe a game
                    5. logout
                    6. help
                    7. quit
                """;
        } else {
            return """
                        1. make move
                        2. highlight legal moves
                        3. redraw board
                        4. resign
                        5. leave
                        6. help
                    """;
        }
    }

    private void evalInGame(String input, Scanner scanner) {
//        try {
//            var tokens = input.toLowerCase().split(" ");
//            var cmd = (tokens.length > 0) ? tokens[0] : "6";
//            switch (cmd) {
//                case "1" -> makeMove(scanner);
//                case "2" -> highlightLegalMoves(scanner);
//                case "3" -> redrawBoard(scanner);
//                case "4" -> resign(scanner);
//                case "5" -> leave(scanner);
//                default -> help();
//            };
//        } catch (ResponseException ex) {
//            System.out.println(SET_TEXT_COLOR_RED + ex.getMessage());
//            System.out.print(SET_TEXT_COLOR_WHITE + menuStart());
//        }
    }

    @Override
    public void notify(ServerMessage message) {
//        switch (message.getServerMessageType()) {
//            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
//            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
//            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
//        }
    }

}
