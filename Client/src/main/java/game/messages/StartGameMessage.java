package game.messages;

public class StartGameMessage extends Message{

    private int senderId;

    public StartGameMessage(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.START_GAME_MESSAGE;
    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
