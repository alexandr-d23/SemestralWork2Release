package game.common;

import java.io.Serializable;

public class Information implements Serializable {
    private String name;
    private int currentRoomId = -1;

    private int id;

    public Information(String  name,int id){
        this.name = name;
        this.id = id;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(int currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public Information(String name){
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
