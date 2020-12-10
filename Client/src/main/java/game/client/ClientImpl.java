package game.client;


import game.messages.*;
import game.messages.actions.Action;
import game.messages.actions.DayStartedAction;
import game.messages.actions.GameOverAction;
import game.messages.actions.NightStartedAction;
import game.common.*;
import javafx.application.Platform;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientImpl implements ClientService,Notifiable {
    Connection connection;
    private int id;
    private final String nickname;
    ClientApplication application;


    public ClientImpl(String nickname, ClientApplication application){
        this.nickname = nickname;
        this.application = application;
    }

    @Override
    public void start() throws IllegalStateException {
        try {
            connection = new PlayerConnection(new Socket(InetAddress.getByName(Properties.host), Properties.port), this, new Information(nickname));
            System.out.println(connection.getInformation().getName() + " сonnected to server");
            this.id = connection.getId();
        }
        catch (IOException e){
            application.serverDisconnected();
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if (connection.isConnected()) {
                connection.send(message);
            }
        }
        catch (IOException e){
            application.serverDisconnected();
        }
    }

    @Override
    public void sendChatMessage(String string){
        ChatMessage message = new ChatMessage(string, getId());
        sendMessage(message);
    }

    @Override
    public void startGame(){
        StartGameMessage message = new StartGameMessage(id);
        sendMessage(message);
    }


    private void joinedRoom(JoinRoomAnswerMessage message){
        if(message.getContent().isSuccessful()) application.joinedRoom(true,message.getContent().getContent());
        else {
            application.joinedRoom(false,null);
            application.problem(message.getContent().getDescription());
        }
    }

    @Override
    public void joinRoom(int roomId){
        sendMessage(new JoinRoomMessage(roomId,id));
    }

    @Override
    public void createRoom(String name, int capacity) {
        RoomInfo info = new RoomInfo(name, capacity);
        CreateRoomMessage message = new CreateRoomMessage(info, id);
        sendMessage(message);
    }

    @Override
    public int getId() {
        return connection.getInformation().getId();
    }

    @Override
    public void leaveRoom(){
        int currentRoom = -1;
        sendMessage(new LeaveRoomMessage(currentRoom,id));
    }

    @Override
    public void notifyMessageReceived(Message message) {
        Platform.runLater(
                () -> {
                    switch (message.getType()) {
                        case SYSTEM_MESSAGE:
                            application.receivedSystemMessage(((ServerMessage) message).getContent());
                            break;
                        case CHAT_MESSAGE:
                            application.receivedChatMessage(((ChatMessage) message).getContent());
                            break;
                        case ROOM_CHAT_MESSAGE:
                            application.receivedRoomChatMessage(((RoomChatMessage) message).getContent());
                            break;
                        case SEND_ROOM_LIST:
                            application.updateRoomList(((ListRoomMessage) message).getContent());
                            break;
                        case JOIN_ROOM_ANSWER:
                            joinedRoom(((JoinRoomAnswerMessage) message));
                            break;
                        case ROOM_UPDATE_INFO:
                            application.updateRoomInfo(((RoomUpdateInfoMessage) message).getContent());
                            break;
                        case GAME_STARTED:
                            application.gameStarted(((GameStartedMessage) message).getContent());
                            break;
                        case GAME_ACTION:
                            gameActionHandler(((GameActionMessage) message).getContent());
                    }
                }
        );

    }

    public void gameActionHandler(Action action){
        switch (action.getType()){
            case NIGHT_STARTED:
                NightStartedAction resAction = (NightStartedAction)action;
                resAction.getSystemMessages().forEach(s -> application.receivedSystemMessage(s));
                application.nightStarted(resAction.getCurrentPlayers(),resAction.getContent());
                break;
            case DAY_STARTED:
                DayStartedAction dayAction = (DayStartedAction)action;
                dayAction.getSystemMessages().forEach(s -> application.receivedSystemMessage(s));
                application.dayStarted(dayAction.getCurrentPlayers(),dayAction.getContent());
                break;
            case GAME_OVER:
                GameOverAction overAction = (GameOverAction)action;
                overAction.getSystemMessages().forEach(s -> application.receivedSystemMessage(s));
                application.gameOver(overAction.getCurrentPlayers(), overAction.getContent());
                break;
        }
    }

    @Override
    public void sendVote(int chosenId){
        GameVoteMessage message = new GameVoteMessage(chosenId,id);
        sendMessage(message);
    }

    @Override
    public String getNickname() {
        return connection.getInformation().getName();
    }

    @Override
    public void disconnect(){
        if(connection==null)return;
        sendMessage(new DisconnectMessage(connection.getId()));
    }

}
