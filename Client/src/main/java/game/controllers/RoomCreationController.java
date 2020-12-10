package game.controllers;

import game.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RoomCreationController {

    public TextField room_creation_text_roomName;
    public TextField room_creation_text_maxPlayers;
    public Button room_creation_btn_confirm;
    public Button room_creation_btn_exit;
    private ClientApplication parent;

    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    @FXML
    private void room_creation_btn_confirmOnClick(ActionEvent actionEvent) {
        try {
            int numberOfPlayers = Integer.parseInt(room_creation_text_maxPlayers.getText());
            if (numberOfPlayers > 3 && numberOfPlayers < 17) {
                parent.createRoom(room_creation_text_roomName.getText(), numberOfPlayers);
            } else {
                showErrorAlert();
            }
        } catch (NumberFormatException ex) {
            showErrorAlert();
        }
    }

    public void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка!");
        alert.setHeaderText(null);
        alert.setContentText("Максимальное количество игроков должно быть целым числом в интервале от 4 до 16 включительно.");
        alert.showAndWait();
    }

    @FXML
    private void room_creation_btn_exitOnClick(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.ROOMS);
    }

}
