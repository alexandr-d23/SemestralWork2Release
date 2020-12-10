package game.messages;

import game.messages.actions.Action;

public class GameActionMessage extends Message<Action> {

    private final Action content;
    private int senderId;

    public GameActionMessage(Action content, int senderId) {
        this.content = content;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.GAME_ACTION;
    }

    @Override
    public Action getContent() {
        return content;
    }

    @Override
    public int getSenderId() {
        return -1;
    }
}
