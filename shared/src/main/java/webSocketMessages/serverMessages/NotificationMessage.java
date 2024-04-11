package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    String message;
    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
