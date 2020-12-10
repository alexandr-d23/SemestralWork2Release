package game.controllers;

import game.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class GameOverController {

    public Text game_over_text;
    public ImageView game_over_iv;

    private ClientApplication parent;

    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    public void setGameOverInformation(String text) {
        game_over_text.setText(game_over_text.getText() + "\n" + text);
        game_over_iv.setImage(new Image("game_over.jpg"));
    }

    @FXML
    private void game_over_btn_confirmOnClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.CURRENT_ROOM);
    }

}
