package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerMessage extends UserGameCommand {
    private Integer gameID;
    private ChessGame.TeamColor playerColor;
    public JoinPlayerMessage(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        super.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
