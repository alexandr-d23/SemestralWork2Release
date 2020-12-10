package game.client;

import game.controllers.ScenesNames;
import game.gamelogic.Player;
import game.common.RoomInfo;

import java.util.Collection;

public interface ClientApplication {

    //При вызове этого метода с бэка должно показываться окошка с сообщением String
    void problem(String string);

    //Получено системное сообщение (Вызывается с сервера)
    void receivedSystemMessage(String messageText);

    //Получено чат-сообщение (Вызывается с сервера)
    void receivedChatMessage(String messageText);

    //Получено чат-сообщение внутри комнаты(Вызывается с сервера)
    void receivedRoomChatMessage(String messageText);

    //обновляет список комнат (Вызывается с сервера)
    void updateRoomList(Collection<RoomInfo> collection);

    //Вызов этого метода отправит на сервер запрос на создание и присоединение к комнате(Вызывается из интерфейса)
    void createRoom(String name, int capacity);

    //вызов этого метода отправит на сервер запрос на присоединение к комнате с определённым id (Вызывается из интерфейса)
    void joinRoom(int roomId);

    //Отправляет сообщение на серввер (Вызывается из интерфейса)
    void sendChatMessage(String string);

    //Выйти из комнаты (Вызывается из интерфейса)
    void leaveRoom();

    //Ответ удалось ли просоединиться к комнате  (Вызывается из сервера)
    void joinedRoom(boolean isSuccessful, RoomInfo info);

    //Обновляет информацию о комнате (вызывается при присоединении или выходе других игроков) (Вызывается из интерфейса)
    void updateRoomInfo(RoomInfo info);

    //Запрос на запуск игры (Вызывается из интерфейса, необходимо нахождение в комнате)
    void tryToStartGame();

    //Игра началась, получаем список всех пользоваетелей с ролями
    void gameStarted(Collection<Player> collection);

    void nightStarted(Collection<Player> players, int seconds);

    void dayStarted(Collection<Player> players, int seconds);

    void gameOver(Collection<Player> players, String finalMessage);

    void sendVote(int chosenId);

    void serverDisconnected();

    void disconnect();

    void setScene(ScenesNames name);

    void exit();

    void startSession(String nickname);
}
