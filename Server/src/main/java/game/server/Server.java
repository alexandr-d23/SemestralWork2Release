package game.server;

import game.common.*;
import game.messages.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Server implements Notifiable {

    private final HashMap<Integer, Connection> connections;
    private final HashMap<Integer,Room>rooms;
    private final ServerSocket serverSocket;
    private int lastUserId = 1;
    private int lastRoomId = 0;

    public Server() throws IllegalStateException{
        connections = new HashMap<>();
        rooms = new HashMap<>();
        try {
            serverSocket = new ServerSocket(Properties.port);
        }
        catch (IOException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void start(){
        System.out.println("Server started");
        while (true){
            try {
                System.out.println("Ожидаю");
                Socket socket = serverSocket.accept();
                System.out.println("Соединил");
                addConnection(socket);
            } catch (IOException e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }

    public Collection<RoomInfo> getRoomInfoCollection(){
        return rooms.values().stream().map(Room::getRoomInfo).collect(Collectors.toList());
    }

    public void addConnection(Socket socket){
        PlayerConnection connection = new PlayerConnection(socket, this, lastUserId++);
        connections.put(connection.getId(),connection);
        ListRoomMessage message = new ListRoomMessage(getRoomInfoCollection(),connection.getId());
        sendToConnection(connection.getId(),message);
        System.out.println("Connected user Id: " +(connection.getId()+ "Nickname: " + connection.getInformation().getName()));
    }

    @Override
    public void notifyMessageReceived(Message message) throws IOException {
        System.out.println("Получил сообщение с типом "+ message.getType().name());
        switch (message.getType()){
            case JOIN_ROOM:
                joinRoom((JoinRoomMessage)message);
                break;
            case CHAT_MESSAGE:
              broadcastSend(new ChatMessage(connections.get(message.getSenderId()).getInformation().getName()+": "+((ChatMessage)message).getContent() + "\r\n",message.getSenderId()));
                break;
            case CREATE_ROOM:
                int roomId = createRoom((CreateRoomMessage)message);
                joinRoom(new JoinRoomMessage(roomId,message.getSenderId()));
                break;
            case DISCONNECT:
                removeConnection(message.getSenderId());
                break;
        }

    }

    public void sendBroadcastRoomList(){
        broadcastSend(new ListRoomMessage(getRoomInfoCollection(),0));
    }

    private void updateRoomList(){
        for(Room room : rooms.values()){
            RoomInfo info = room.getRoomInfo();
            System.out.println("Id: " + info.getId() + " name: " + info.getName() + " capacity: " + info.getCapacity() + " currentSize: " + info.getCurrentSize()  );
            System.out.println("Пользователи в комнате: ");
            for (Connection connection : room.getConnections()){
                System.out.println("        ID: "+connection.getId()+" name: "+connection.getInformation().getName() );
            }
        }
    }

    private int createRoom(CreateRoomMessage message){
        RoomInfo info = message.getContent();
        int roomId = lastRoomId++;
        Room room = new Room(roomId, info.getName(), info.getCapacity(), info.getCurrentSize(),this);
        rooms.put(roomId,room);
        return room.getRoomInfo().getId();
    }

    public void removeRoom(int roomId) {
        rooms.remove(roomId);
        sendBroadcastRoomList();
    }

    private void sendToConnection(int connectionId, Message message){
        try {
            Connection con = connections.get(connectionId);
            if (con.isConnected()) con.send(message);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void joinRoom(JoinRoomMessage message) throws IOException {
        Room room = rooms.get(message.getContent());
        if(room.isInGame()){
            JoinRoomAnswerMessage answerMessage = new JoinRoomAnswerMessage(new Result<>(false,"Извините, игра уже началась"),-1);
            sendToConnection(message.getSenderId(), answerMessage);
        }
        else if(room.getRoomInfo().getCurrentSize()<room.getRoomInfo().getCapacity()) {
            int senderId = message.getSenderId();
            Connection connection = connections.get(senderId);
            room.addConnection(connection);
        }
        else {
            JoinRoomAnswerMessage answerMessage = new JoinRoomAnswerMessage(new Result<>(false,"Извините, комната заполнена"),-1);
            sendToConnection(message.getSenderId(), answerMessage);
        }
        updateRoomList();
    }

    public void removeConnection(int id){
        Connection connection = connections.get(id);
        if(connection == null)return;
        Room room =rooms.get(connection.getInformation().getCurrentRoomId());
        if(room!=null) {
            room.removeConnection(id);
        }
        connections.remove(id);
        System.out.println("Пользователь ID:" + connection.getInformation().getId() + " name: " + connection.getInformation().getName() + "отсоединился");
    }

    public void broadcastSend(Message message) {
        Collection<Connection> connectionCollection = connections.values();
        Collection<Connection> removeConnectionsIndexes = new ArrayList<>();
        for(Connection con : connectionCollection){
            try {
                if (con.isConnected()) con.send(message);
            }
            catch (IOException e){
                removeConnectionsIndexes.add(con);
            }
        }
        for(Connection con : removeConnectionsIndexes){
            removeConnection(con.getId());
        }
    }
}
