package webSocketMessages.userCommands;

public class LeaveGameMessage extends UserGameCommand{
    private Integer gameID;
    public LeaveGameMessage(String authToken, Integer gameID) {
        super(authToken);
        super.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
