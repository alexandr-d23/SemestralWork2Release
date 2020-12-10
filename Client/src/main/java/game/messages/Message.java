package game.messages;

import java.io.Serializable;

public abstract class Message<T> implements Serializable {

    public abstract MessageTypes getType();

    public abstract T getContent();

    public abstract int getSenderId();
}
