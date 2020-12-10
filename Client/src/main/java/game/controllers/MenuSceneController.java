package game.controllers;

import game.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuSceneController {

    private ClientApplication parent;

    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    @FXML
    private void menu_btn_roomsClicked(){
        parent.setScene(ScenesNames.ROOMS);
    }


    @FXML
    private void menu_btn_rulesClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.RULES);
    }

    @FXML
    private void menu_btn_exitClicked(ActionEvent actionEvent) {
        parent.exit();
    }
}
