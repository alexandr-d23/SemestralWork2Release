package game.messages;

import game.gamelogic.Player;

import java.util.Collection;

public class GameStartedMessage extends Message<Collection<Player>> {

    private int senderId;

    private Collection<Player> players;

    public GameStartedMessage(Collection<Player> players,int senderId) {
        this.players = players;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.GAME_STARTED;
    }

    @Override
    public Collection<Player> getContent() {
        return players;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}
