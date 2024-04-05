package webSocketMessages.userCommands;

public class ObserveGameMessage extends UserGameCommand {
    private Integer gameID;
    public ObserveGameMessage(String authToken, Integer gameID) {
        super(authToken);
        super.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
