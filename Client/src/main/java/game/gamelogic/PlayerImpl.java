package game.gamelogic;

public class PlayerImpl extends Player {
    public int id;
    public String name;
    public Role role;
    public Status status;


    public PlayerImpl(int id, String name) {
        this.id = id;
        this.name = name;
        this.role = Role.CITIZEN;
        this.status = Status.ALIVE;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }
}
