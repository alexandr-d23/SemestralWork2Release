package game.controllers;

import game.client.ClientApplication;
import game.common.RoomInfo;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

public class ParametersOfRoomController {

    public Text parameters_text_roomName;
    public Text parameters_text_maxPlayers;
    public Text parameters_text_currentCountOfPlayers;
    private ClientApplication parent;

    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    public void setParameters(RoomInfo info) {
        parameters_text_roomName.setText(info.getName());
        parameters_text_maxPlayers.setText(info.getCapacity() + "");
        parameters_text_currentCountOfPlayers.setText(info.getCurrentSize() + "");
    }

    public void parameters_btn_confirmOnClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.CURRENT_ROOM);
    }

}
