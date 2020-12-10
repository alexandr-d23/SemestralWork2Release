package game.gamelogic;

import game.server.Room;

import java.util.HashMap;

public interface Game {
    void start(Room room, HashMap<Integer,Player> collection);
    void playerDisconnected(int id);
    void sendVote(int senderId,int chosenId);
}
