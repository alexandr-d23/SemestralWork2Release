package game.messages;

import game.common.RoomInfo;

import java.util.Collection;

public class ListRoomMessage extends Message<Collection<RoomInfo>> {
    private final Collection<RoomInfo> content;
    private final int senderId;

    public ListRoomMessage(Collection<RoomInfo> collection, int senderId) {
        this.content = collection;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.SEND_ROOM_LIST;
    }

    @Override
    public Collection<RoomInfo> getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
