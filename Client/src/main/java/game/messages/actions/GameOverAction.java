package game.messages.actions;

import game.gamelogic.Player;

import java.util.Collection;

public class GameOverAction extends Action<String> {

    private final Collection<Player> players;
    private final Collection<String> messages;
    private final String finalMessage;

    public GameOverAction(Collection<Player> players, Collection<String> messages, String finalMessage) {
        this.players = players;
        this.finalMessage = finalMessage;
        this.messages = messages;
    }

    @Override
    public ActionTypes getType() {
        return ActionTypes.GAME_OVER;
    }

    @Override
    public Collection<String> getSystemMessages() {
        return messages;
    }

    @Override
    public Collection<Player> getCurrentPlayers() {
        return players;
    }

    @Override
    public String getContent() {
        return finalMessage;
    }
}
