package game.messages;

public class JoinRoomMessage extends Message<Integer> {

    private final int roomId;
    private final int senderId;

    public JoinRoomMessage(int roomId, int senderId) {
        this.roomId = roomId;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.JOIN_ROOM;
    }

    @Override
    public Integer getContent() {
        return roomId;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }


}
