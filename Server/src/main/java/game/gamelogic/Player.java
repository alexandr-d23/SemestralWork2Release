package game.gamelogic;

import java.io.Serializable;

public abstract class Player implements Serializable {

    public abstract int getId();

    public abstract String getName();

    public abstract Role getRole();

    public abstract Status getStatus();

    public abstract void setRole(Role role);

    public abstract void setStatus(Status status);
}
