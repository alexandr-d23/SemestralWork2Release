package game.messages;

import game.common.RoomInfo;

public class CreateRoomMessage extends Message<RoomInfo> {

    private final RoomInfo content;
    private final int userId;

    public CreateRoomMessage(RoomInfo content, int userId) {
        this.content = content;
        this.userId = userId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.CREATE_ROOM;
    }

    @Override
    public RoomInfo getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return userId;
    }


}
