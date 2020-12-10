package game.messages;

public class ServerMessage extends Message<String> {
    private final String content;
    private final int senderId;

    public ServerMessage(String content, int sender){
        this.content = content;
        this.senderId = sender;
    }

    public MessageTypes getType() {
        return MessageTypes.SYSTEM_MESSAGE;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
