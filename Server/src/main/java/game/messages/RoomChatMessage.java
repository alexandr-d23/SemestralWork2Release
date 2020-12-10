package game.messages;

public class RoomChatMessage extends Message<String>{

    private final String content;
    private final int senderId;

    public RoomChatMessage(String content, int sender){
        this.content = content;
        this.senderId = sender;
    }

    public MessageTypes getType() {
        return MessageTypes.ROOM_CHAT_MESSAGE;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
