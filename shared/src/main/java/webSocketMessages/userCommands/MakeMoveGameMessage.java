package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveGameMessage extends UserGameCommand {
    Integer gameID;
    ChessMove move;
    public MakeMoveGameMessage(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        super.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public Integer getGameID() {
        return gameID;
    }
    public ChessMove getMove() {return move;}
}
