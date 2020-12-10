package game.gamelogic;

import game.messages.GameActionMessage;
import game.messages.GameStartedMessage;
import game.messages.RoomChatMessage;
import game.messages.actions.DayStartedAction;
import game.messages.actions.GameOverAction;
import game.messages.actions.NightStartedAction;
import game.server.Room;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class GameImpl implements Game,Runnable{

    private Room room;
    private HashMap<Integer,Player> players;
    private List<String> gameMessages;
    private final int NIGHT_TIME_SECONDS = 15000;
    private final int DAY_TIME_SECONDS = 15000;
    private final int SLEEP_TIME = 10000;
    private List<Pair<Integer,Integer>>votes;

    @Override
    public void start(Room room, HashMap<Integer,Player> collection) {
        this.room = room;
        this.players = collection;
        gameMessages = new ArrayList<>();
        votes = new ArrayList<>();
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            distributeRoles();
            ArrayList<Player> sentPlayers = new ArrayList<>(players.values());
            room.broadCastSend(new GameStartedMessage(sentPlayers,-1));
            Thread.sleep(SLEEP_TIME);
            while (countOfAlivePlayers()>1 && countOfKillers()>0){
                nightStarted();
                Thread.sleep(NIGHT_TIME_SECONDS+SLEEP_TIME);
                applyNightVotes();
                if(!(countOfAlivePlayers()>1 && countOfKillers()>0)){
                    break;
                }
                dayStarted();
                Thread.sleep(DAY_TIME_SECONDS+SLEEP_TIME);
                applyDayVotes();
            }
            gameOver();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            throw new IllegalStateException(exception.getMessage());
        }
    }

    private void killPlayer(int id){
        System.out.println("Убиваем " + players.get(id).getName());
        players.get(id).setStatus(Status.DEAD);
    }

    private void gameOver(){
        String finalMessage;
        if(countOfKillers()>0){
            finalMessage = "Победили убийцы";
        }
        else{
            finalMessage = "Победили мирные жители";
        }
        ArrayList<String> messages = new ArrayList<>(getGameMessages());
        ArrayList<Player> playerList = new ArrayList<>(players.values());
        GameActionMessage message = new GameActionMessage(new GameOverAction(playerList,messages,finalMessage),-1);
        room.broadCastSend(message);
        room.setGameNull();
    }

    private void dayStarted(){
        ArrayList<String> messages = new ArrayList<>(getGameMessages());
        ArrayList<Player> playerList = new ArrayList<>(players.values());
        GameActionMessage message = new GameActionMessage(new DayStartedAction(playerList,messages,DAY_TIME_SECONDS/1000),-1);
        GameActionMessage messageWithoutVote = new GameActionMessage(new DayStartedAction(playerList, messages,0),-1);
        for(Player player : playerList){
            if(player.getStatus().equals(Status.ALIVE)){
                room.sendToConnection(player.getId(), message);
            }
            else{
                room.sendToConnection(player.getId(), messageWithoutVote);
            }
        }
    }

    public void nightStarted(){
        ArrayList<String> messages = new ArrayList<>(getGameMessages());
        ArrayList<Player> playerList = new ArrayList<>(players.values());
        GameActionMessage message = new GameActionMessage(new NightStartedAction(playerList,messages,NIGHT_TIME_SECONDS/1000),-1);
        GameActionMessage messageWithoutVote = new GameActionMessage(new NightStartedAction(playerList, messages,0),-1);
        for(Player player : playerList){
            if(!player.getRole().equals(Role.CITIZEN) && player.getStatus().equals(Status.ALIVE)){
                room.sendToConnection(player.getId(), message);
            }
            else{
                room.sendToConnection(player.getId(), messageWithoutVote);
            }
        }
    }

    private void applyDayVotes(){
        List<Pair<Integer,Integer>> currentVotes = getVotes();
        Map<Integer,Long> res = currentVotes.stream().collect(Collectors.groupingBy(Pair<Integer,Integer>::getValue,Collectors.counting()));
        long max = -1;
        long value;
        int id = 1 + new Random().nextInt(countOfAlivePlayers()-1);
        for(int key : res.keySet()){
            value = res.get(key);
            if(value>max){
                max = value;
                id = key;
            }
        }
        killPlayer(id);
        gameMessages.add("Игрок + " + players.get(id).getName() + " был казнён");
    }


    private void applyNightVotes(){
        List<Integer> deadPlayers = new ArrayList<>();
        List<Pair<Integer,Integer>> currentVotes = getVotes();
        for(Pair<Integer,Integer> vote : currentVotes){
            int senderId = vote.getKey();
            int aim = vote.getValue();
            if(players.get(senderId).getRole().equals(Role.KILLER)){
                deadPlayers.add(aim);
            }
        }
        for(Pair<Integer,Integer> vote : currentVotes){
            int senderId = vote.getKey();
            int aim = vote.getValue();
            if(players.get(senderId).getRole().equals(Role.RESCUER)){
                deadPlayers = deadPlayers.stream().filter( i -> i!=aim).collect(Collectors.toList());
            }
            else if(players.get(senderId).getRole().equals(Role.CONSTABLE)){
                if(players.get(aim).getRole().equals(Role.KILLER)){
                    room.sendToConnection(senderId,new RoomChatMessage("Данный игрок - убийца", -1));
                }
                else{
                    room.sendToConnection(senderId,new RoomChatMessage("Данный игрок - не убийца", -1));
                }
            }
        }
        for(Integer i : deadPlayers){
            killPlayer(i);
            gameMessages.add("Ночью игрок " + players.get(i).getName() + " был жестоко убит маньяком");
        }
    }

    private List<Pair<Integer,Integer>> getVotes(){
        List<Pair<Integer,Integer>> currentVotes = new ArrayList<>(votes);
        votes.clear();
        return currentVotes;
    }

    @Override
    public void playerDisconnected(int id){
        if(players.get(id)==null)return;
        gameMessages.add("Пользователь " + players.get(id).getName() + "покинул игру");
        players.remove(id);
    }

    private Collection<String> getGameMessages(){
        List<String> collection = new ArrayList<>(gameMessages);
        gameMessages.clear();
        return collection;
    }



    @Override
    public void sendVote(int senderId, int choiceId){
        votes.add(new Pair<>(senderId,choiceId));
        System.out.println(senderId+" " + choiceId);
    }

    public int countOfAlivePlayers(){
        int count =0;
        Collection<Player> playersList = players.values();
        for(Player player : playersList){
            if(player.getStatus().equals(Status.ALIVE))count++;
        }
        return count;
    }

    public int countOfKillers(){
        int count =0;
        Collection<Player> playersList = players.values();
        for(Player player : playersList){
            if(player.getRole().equals(Role.KILLER) && player.getStatus().equals(Status.ALIVE))count++;
        }
        return count;
    }


    public void distributeRoles(){
        ArrayList<Player> playerCollection = new ArrayList<>(players.values());
        int countOfKillers = 0;
        int countOfSavers = 0;
        int countOfCheckers = 0;
        Random random = new Random();
        int count = playerCollection.size();
        if(count>3 && count <= 7){
            countOfKillers = 1;
            countOfCheckers = 1;
            countOfSavers = 0;
        }
        if(count > 7 && count <= 10){
            countOfKillers = 2;
            countOfSavers = 1;
            countOfCheckers = 1;
        }
        if(count> 10 && count <= 13){
            countOfKillers = 3;
            countOfSavers = 3;
            countOfCheckers = 2;
        }
        if(count >13){
            countOfKillers = 3;
            countOfSavers = 2;
            countOfCheckers = 2;
        }
        int index;
        for(int i = 0; i < countOfKillers; i++){
            index = random.nextInt(playerCollection.size());
            playerCollection.get(index).setRole(Role.KILLER);
            System.out.println(playerCollection.get(index).getName() + " Мафия");
            playerCollection.remove(index);
        }
        for(int i = 0; i < countOfSavers; i++){
            index = random.nextInt(playerCollection.size());
            playerCollection.get(index).setRole(Role.RESCUER);
            System.out.println(playerCollection.get(index).getName() + " Доктор");
            playerCollection.remove(index);
        }
        for(int i = 0; i < countOfCheckers; i++){
            index = random.nextInt(playerCollection.size());
            playerCollection.get(index).setRole(Role.CONSTABLE);
            System.out.println(playerCollection.get(index).getName() + " Шериф");
            playerCollection.remove(index);
        }
    }


}
