package game.server;
import game.gamelogic.Game;
import game.gamelogic.GameImpl;
import game.gamelogic.Player;
import game.gamelogic.PlayerImpl;
import game.messages.*;
import game.common.Connection;
import game.common.Notifiable;
import game.common.RoomInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Room implements Notifiable {

    private final HashMap<Integer, Connection> connections;
    private final Server attachedServer;
    private final RoomInfo roomInfo;
    private Game game;

    public Room(int id, String name, int capacity,int currentSize, Server attachedServer) {
        roomInfo = new RoomInfo(id,name,capacity,currentSize);
        connections = new HashMap<>();
        this.attachedServer = attachedServer;
    }

    @Override
    public void notifyMessageReceived(Message message) throws IOException {
        switch (message.getType()){
            case LEAVE_ROOM:
            case DISCONNECT:
                removeConnection(message.getSenderId());
                break;
            case CHAT_MESSAGE:
                broadCastSend(new RoomChatMessage(connections.get(message.getSenderId()).getInformation().getName()+": "+((ChatMessage)message).getContent() + "\r\n",message.getSenderId()));
                break;
            case START_GAME_MESSAGE:
                Result res  = startGame();
                if(res!=null && !res.isSuccessful()){
                    sendToConnection(message.getSenderId(), new ServerMessage(res.getDescription(), message.getSenderId()));
                }
                break;
            case GAME_VOTE:
                if(game!=null){
                    GameVoteMessage resMessage = ((GameVoteMessage)message);
                    game.sendVote(resMessage.getSenderId(),resMessage.getContent());
                }
                break;
        }
    }

    public boolean isInGame(){
        return game != null;
    }

    public Result startGame(){
        if(game!=null)return null;
        if(connections.size()<4)return new Result(false,"Для старта игры нужно как минимум 4 игрока");
        game = new GameImpl();
        game.start(this,getPlayers());
        return new Result(true);
    }

    public void setGameNull(){
        this.game = null;
    }

    private HashMap<Integer,Player> getPlayers(){
        HashMap<Integer,Player> hashMap = new HashMap<>();
        Collection<Connection> col = connections.values();
        for(Connection connection :col){
            hashMap.put(connection.getId(), new PlayerImpl(connection.getId(), connection.getInformation().getName()));
        }
        return hashMap;
    }



    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void broadCastSend(Message message)  {
        Collection<Connection> connectionCollection = connections.values();
        Collection<Connection> removeConnectionsIndexes = new ArrayList<>();
        for (Connection con : connectionCollection) {
            try {
                if (con.isConnected()) con.send(message);
            } catch (IOException e) {
                removeConnectionsIndexes.add(con);
            }
        }
        for (Connection con : removeConnectionsIndexes) {
            attachedServer.removeConnection(con.getId());
        }
    }


    public Collection<Connection> getConnections() {
        return connections.values();
    }

    public void addConnection(Connection connection){
        connection.setNotifiable(this);
        connection.getInformation().setCurrentRoomId(roomInfo.getId());
        connections.put(connection.getId(),connection);
        sendToConnection(connection.getId(), new JoinRoomAnswerMessage(new Result<>(true, roomInfo),-1));
        roomInfo.addMember(connection.getInformation());
        roomInfo.setCurrentSize(roomInfo.getCurrentSize()+1);
        broadCastSend(new RoomUpdateInfoMessage(roomInfo, -1));
        broadCastSend(new ServerMessage("Room: Player "+connection.getId()+" присоединился к комнате "+roomInfo.getName(),-1));
        attachedServer.sendBroadcastRoomList();
    }

    public void sendToConnection(int connectionId, Message message){
        try {
            Connection con = connections.get(connectionId);
            if (con.isConnected()) con.send(message);
        } catch (IOException e) {
            removeConnection(connectionId);
        }
    }

    public void removeConnection(int connectionId)  {
        Connection connection = connections.get(connectionId);
        if(connection ==null)return;
        if(game!=null)game.playerDisconnected(connectionId);
        connection.setNotifiable(attachedServer);
        connections.remove(connectionId);
        roomInfo.setCurrentSize(roomInfo.getCurrentSize()-1);
        roomInfo.removeMember(connectionId);
        if(roomInfo.getCurrentSize()==0){
            attachedServer.removeRoom(roomInfo.getId());
            return;
        }
        broadCastSend(new RoomUpdateInfoMessage(roomInfo, -1));
        broadCastSend(new ServerMessage("Room: Player "+connection.getId() + "покинул комнату "+roomInfo.getName(),-1));
        attachedServer.sendBroadcastRoomList();
    }

}