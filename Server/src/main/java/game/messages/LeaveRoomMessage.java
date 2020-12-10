package game.messages;

public class LeaveRoomMessage extends Message<Integer>{
    private final int roomId;
    private final int senderId;

    public LeaveRoomMessage(int roomId, int senderId) {
        this.roomId = roomId;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.LEAVE_ROOM;
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
