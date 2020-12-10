package game.client;

import game.controllers.*;
import game.gamelogic.Player;
import game.common.RoomInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MyApplication extends Application implements ClientApplication {
    private CurrentRoomController currentRoomController;
    private ClientService client;
    private SetNickController setNickController;
    private MenuSceneController menuSceneController;
    private RoomsSceneController roomsController;
    private RoomCreationController roomCreationController;
    private GameController gameController;
    private RulesController rulesController;
    private ParametersOfRoomController parametersOfRoomController;
    private GameOverController gameOverController;
    private RoomInfo roomInfo;
    private String gameOverText;
    private Collection<Player> players = new ArrayList<>();
    private Collection<RoomInfo> currentRooms = new ArrayList<>();
    Stage stage;

    public void start(Stage stage) throws IOException {
        this.stage = stage;
        setScene(ScenesNames.START);
        this.stage.setResizable(false);
        this.stage.setOnCloseRequest(event -> exit());
        stage.show();
    }

    @Override
    public void startSession(String nickname){
        try {
            client = new ClientImpl(nickname, this);
            client.start();
            setScene(ScenesNames.MENU);
        }
        catch (IllegalStateException e){
            problem("Сервер не отвечает. Попробуйте позже");
        }
    }

    @Override
    public void exit(){
        disconnect();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void setScene(ScenesNames sceneName){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(sceneName.getTitle()));
            Parent root = loader.load();
            switch (sceneName){
                case START:
                    setNickController = loader.getController();
                    setNickController.setParent(this);
                    break;
                case MENU:
                    menuSceneController = loader.getController();
                    menuSceneController.setParent(this);
                    break;
                case ROOMS:
                    roomsController = loader.getController();
                    roomsController.setParent(this);
                    roomsController.setNickname(client.getNickname());
                    roomsController.updateRoomsList(currentRooms);
                    break;
                case CURRENT_ROOM:
                    currentRoomController = loader.getController();
                    currentRoomController.setParent(this);
                    currentRoomController.setNickname(client.getNickname());
                    currentRoomController.updateMembersList(roomInfo.getMembers());
                    break;
                case ROOM_CREATION:
                    roomCreationController = loader.getController();
                    roomCreationController.setParent(this);
                    break;
                case GAME:
                    gameController = loader.getController();
                    gameController.setParent(this);
                    gameController.updatePlayersList(players);
                    gameController.setPlayerRoleImage(client.getId());
                    break;
                case RULES:
                    rulesController = loader.getController();
                    rulesController.setParent(this);
                    break;
                case PARAMETERS:
                    parametersOfRoomController = loader.getController();
                    parametersOfRoomController.setParent(this);
                    parametersOfRoomController.setParameters(roomInfo);
                    break;
                case GAME_OVER:
                    gameOverController = loader.getController();
                    gameOverController.setParent(this);
                    gameOverController.setGameOverInformation(gameOverText);
                    break;
            }
            stage.setScene(new Scene(root));

        }
        catch (IOException exception){

        }
    }

    @Override
    public void sendChatMessage(String string){
        client.sendChatMessage(string);
    }

    @Override
    public void leaveRoom() {
        client.leaveRoom();
        setScene(ScenesNames.ROOMS);
    }

    @Override
    public void joinedRoom(boolean isSuccessful, RoomInfo info) {
        if(roomsController!=null){
            if(isSuccessful){
                roomInfo = info;
                setScene(ScenesNames.CURRENT_ROOM);
            }
            roomsController.answerGot();
        }
    }


    public void showAlertWithHeaderText(String information) {
        Platform.runLater(
                () ->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText(information);
                    alert.showAndWait();
                }
        );

    }

    @Override
    public void problem(String string){
        showAlertWithHeaderText(string);
    }

    @Override
    public void updateRoomInfo(RoomInfo info) {
        roomInfo = info;
        if(currentRoomController!=null){
            currentRoomController.updateMembersList(info.getMembers());
        }
    }

    @Override
    public void tryToStartGame() {
        client.startGame();
    }

    @Override
    public void gameStarted(Collection<Player> collection) {
        players = collection;
        setScene(ScenesNames.GAME);
        System.out.println("старт получено игроков " + collection.size());
    }

    @Override
    public void nightStarted(Collection<Player> players, int seconds) {
        if (gameController != null){
            gameController.nightStarted(players, seconds);
            System.out.println("ночью получено игроков " + players.size());
        }
    }

    @Override
    public void dayStarted(Collection<Player> players, int seconds) {
        if (gameController != null){
            gameController.dayStarted(players,seconds);
            System.out.println("Днём получено игроков " + players.size());
        }
    }

    @Override
    public void gameOver(Collection<Player> players, String finalMessage) {
        if (gameController != null) {
            gameOverText = finalMessage;
            gameController.gameOver(players, finalMessage);
        }
    }

    @Override
    public void sendVote(int chosenId) {
        client.sendVote(chosenId);
    }

    @Override
    public void serverDisconnected() {
        System.out.println("Сервер не отвечает");
    }

    @Override
    public void disconnect() {
        if(client==null)return;
        client.disconnect();

    }

    public void updateRoomList(Collection<RoomInfo> collection){
        currentRooms = collection;
        if(roomsController!=null){
            roomsController.updateRoomsList(currentRooms);
        }
    }

    @Override
    public void createRoom( String name, int capacity) {
        client.createRoom(name,capacity);
    }

    @Override
    public void joinRoom(int roomId) {
        client.joinRoom(roomId);
    }

    @Override
    public void receivedSystemMessage(String messageText) {
        if (gameController != null) {
            gameController.receiveSystemMessage(messageText);
        }
    }

    @Override
    public void receivedChatMessage(String messageText) {
        if(roomsController!=null){
            roomsController.getMessage(messageText);
        }
    }

    @Override
    public void receivedRoomChatMessage(String messageText) {
        if(currentRoomController!=null){
            currentRoomController.getMessage(messageText);
        }
        if (gameController != null) {
            gameController.getMessage(messageText);
        }
    }

    private void setNullToControllers() {
        currentRoomController = null;
        setNickController = null;
        menuSceneController = null;
        roomsController = null;
        roomCreationController = null;
        gameController = null;
    }

}
