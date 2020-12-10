package game.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class RoomInfo implements Serializable {
    private int id;
    private final String name;
    private final int capacity;
    private int currentSize;
    private final HashMap<Integer,Information> members;

    public RoomInfo(int id, String name, int capacity,int currentSize){
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.currentSize = currentSize;
        members = new HashMap<>();
    }

    public RoomInfo(String name, int capacity){
        this.name = name;
        this.currentSize = 0;
        this.capacity = capacity;
        members = new HashMap<>();
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public Collection<Information> getMembers(){
        return members.values();
    }

    public void addMember(Information information){
        members.put(information.getId(),information);
    }

    public void removeMember(int id){
        members.remove(id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
