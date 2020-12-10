package game.controllers;

import game.client.ClientApplication;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SetNickController {
    private ClientApplication parent;



    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    @FXML
    private TextField set_nick_TextField;

    public void onEnterNameClicked(){
        parent.startSession(set_nick_TextField.getText());
    }
}
