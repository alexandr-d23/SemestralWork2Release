package game.messages.actions;

import game.gamelogic.Player;

import java.util.Collection;

public class DayStartedAction extends Action<Integer> {
    private Collection<Player> players;
    private Collection<String> messages;
    private Integer seconds;

    public DayStartedAction(Collection<Player> players, Collection<String> messages, Integer seconds) {
        this.players = players;
        this.messages = messages;
        this.seconds = seconds;
    }

    @Override
    public ActionTypes getType() {
        return ActionTypes.DAY_STARTED;
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
    public Integer getContent() {
        return seconds;
    }
}
