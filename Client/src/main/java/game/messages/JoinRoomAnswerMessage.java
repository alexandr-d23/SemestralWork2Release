package game.messages;

import game.common.RoomInfo;

public class JoinRoomAnswerMessage extends Message<Result>{

    private final Result<RoomInfo> result;
    private final int senderId;

    public JoinRoomAnswerMessage(Result<RoomInfo> isSuccessful, int senderId) {
        this.result = isSuccessful;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.JOIN_ROOM_ANSWER;
    }

    @Override
    public Result<RoomInfo> getContent() {
        return result;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }

}
