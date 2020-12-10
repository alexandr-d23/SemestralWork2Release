package game.messages;

public class ChatMessage extends Message<String> {
    private final String content;
    private final int senderId;

    public ChatMessage(String content, int sender){
        this.content = content;
        this.senderId = sender;
    }

    public MessageTypes getType() {
        return MessageTypes.CHAT_MESSAGE;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
