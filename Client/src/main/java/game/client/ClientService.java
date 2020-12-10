package game.client;

import game.messages.Message;

public interface ClientService {

    void start() throws IllegalStateException;

    void sendMessage(Message message);

    void sendChatMessage(String string);

    void startGame();

    void joinRoom(int roomId);

    void createRoom(String name, int capacity);

    int getId();

    void leaveRoom();

    void sendVote(int chosenId);

    String getNickname();

    void disconnect();
}
