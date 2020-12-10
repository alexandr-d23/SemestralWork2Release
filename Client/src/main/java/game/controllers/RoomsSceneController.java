package game.controllers;

import game.client.ClientApplication;
import game.common.RoomInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.Collection;

public class RoomsSceneController {

    public Button rooms_btn_parameters;
    public Text rooms_text_nickName;
    private boolean waitingForAnswer;
    public Button rooms_btn_goToRoom;
    private ObservableList<RoomInfo> usersData = FXCollections.observableArrayList();
    public TableView<RoomInfo> rooms_table_listOfRooms;
    public TableColumn<RoomInfo, Integer> idColumn;
    public TableColumn<RoomInfo, String> nameColumn;
    public TableColumn<RoomInfo, Integer> capacityColumn;
    public TableColumn<RoomInfo, Integer> currentSizeColumn;
    private ClientApplication parent;
    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    public void getMessage(String string){
        rooms_text_chat.appendText(string + "\n");
    }

    @FXML
    public TextArea rooms_text_chat;

    @FXML
    public TextField rooms_editText_textMessage;

    @FXML
    public Button rooms_sendMessage;

    @FXML
    private void joinRoom(){
        if (!waitingForAnswer) {
            try {
                parent.joinRoom(rooms_table_listOfRooms.getSelectionModel().getSelectedItem().getId());
                waitingForAnswer = true;
            } catch (NullPointerException ignored) { }
        }
    }

    public void answerGot(){
        waitingForAnswer = false;
    }

    @FXML
    public void clickItem(MouseEvent event)
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            joinRoom();
        }
    }

    public void setNickname(String nickname) {
        rooms_text_nickName.setText(nickname);
    }

    public void updateRoomsList(Collection<RoomInfo> collection){
        usersData.clear();
        usersData.addAll(collection);
        idColumn.setCellValueFactory(new PropertyValueFactory<RoomInfo,Integer>("Id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<RoomInfo,String>("name"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<RoomInfo,Integer>("capacity"));
        currentSizeColumn.setCellValueFactory(new PropertyValueFactory<RoomInfo,Integer>("currentSize"));
        rooms_table_listOfRooms.setItems(usersData);
    }

    @FXML
    private void rooms_sendMessageOnClicked(){
        parent.sendChatMessage(rooms_editText_textMessage.getText());
        rooms_editText_textMessage.clear();
    }

    @FXML
    private void rooms_btn_createRoomOnClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.ROOM_CREATION);
    }

    @FXML
    private void rooms_btn_exitOnClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.MENU);
    }
}
