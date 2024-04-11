package webSocketMessages.userCommands;

public class ResignGameMessage extends UserGameCommand {
    Integer gameID;
    public ResignGameMessage(String authToken, Integer gameID) {
        super(authToken);
        super.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
