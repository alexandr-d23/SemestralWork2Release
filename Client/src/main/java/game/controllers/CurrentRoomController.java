package game.controllers;

import game.client.ClientApplication;
import game.common.Information;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.Collection;

public class CurrentRoomController {
    public Text current_room_text_nickName;
    public TextArea current_room_text_chat;
    public TextField current_room_editText_textMessage;
    public Button current_room_sendMessage;
    public Button current_room_btn_exit;
    public TableView<Information> current_room_table_listOfPlayers;
    public Button current_room_btn_parameters;
    public Button current_room_btn_play;
    private ObservableList<Information> players = FXCollections.observableArrayList();
    public TableColumn<Information, String> nameColumn;

    private ClientApplication parent;
    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    public void getMessage(String string){
        current_room_text_chat.appendText(string+ "\n");
    }

    public void current_room_sendMessageOnClicked(){
        parent.sendChatMessage(current_room_editText_textMessage.getText());
        current_room_editText_textMessage.clear();
    }

    public void setNickname(String nickname) {
        current_room_text_nickName.setText(nickname);
    }

    @FXML
    private void leaveRoom(){
        parent.leaveRoom();
    }

    public void updateMembersList(Collection<Information> collection) {
        players.clear();
        players.addAll(collection);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Information,String>("name"));
        current_room_table_listOfPlayers.setItems(players);
    }

    @FXML
    private void current_room_btn_playOnClick(ActionEvent actionEvent) {
        parent.tryToStartGame();
    }

    public void current_room_btn_parametersOnClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.PARAMETERS);
    }
}
