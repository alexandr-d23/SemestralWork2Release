package game.messages;

public class DisconnectMessage extends Message {

    private int senderId;

    public DisconnectMessage(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.DISCONNECT;
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
