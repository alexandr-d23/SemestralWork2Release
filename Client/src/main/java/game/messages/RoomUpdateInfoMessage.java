package game.messages;

import game.common.RoomInfo;

public class RoomUpdateInfoMessage extends Message<RoomInfo> {

    private final RoomInfo info;
    private final int senderId;

    public RoomUpdateInfoMessage(RoomInfo info, int senderId) {
        this.info = info;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.ROOM_UPDATE_INFO;
    }

    @Override
    public RoomInfo getContent() {
        return info;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
