package game.messages.actions;

import game.gamelogic.Player;

import java.io.Serializable;
import java.util.Collection;

public abstract class Action<T> implements Serializable {
    public abstract ActionTypes getType();
    public abstract Collection<String> getSystemMessages();
    public abstract Collection<Player> getCurrentPlayers();
    public abstract  T getContent();
}
