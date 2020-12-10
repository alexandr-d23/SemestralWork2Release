package game.messages;

public class GameVoteMessage extends Message<Integer>{

    public int senderId;
    public int chosenId;

    public GameVoteMessage(int chosenId,int senderId) {
        this.senderId = senderId;
        this.chosenId = chosenId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.GAME_VOTE;
    }

    @Override
    public Integer getContent() {
        return chosenId;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
