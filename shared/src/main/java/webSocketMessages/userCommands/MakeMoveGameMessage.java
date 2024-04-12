package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveGameMessage extends UserGameCommand {
    Integer gameID;
    ChessMove move;
    String startPos;
    String endPos;
    public MakeMoveGameMessage(String authToken, Integer gameID, ChessMove move, String startPos, String endPos) {
        super(authToken);
        super.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public Integer getGameID() {
        return gameID;
    }
    public ChessMove getMove() {return move;}
    public String getStartPos() {
        return startPos;
    }

    public String getEndPos() {
        return endPos;
    }
}
