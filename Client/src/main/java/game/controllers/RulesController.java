package game.controllers;

import game.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class RulesController {

    public TextArea rules_text_rulesText;
    private ClientApplication parent;



    public void setParent(ClientApplication parent) {
        this.parent = parent;
        setText();
    }

    private void setText(){
        rules_text_rulesText.setText("В игре есть четыре основные роли - \"Мафия\", \"Мирный житель\", \"Лекарь\" и \"Констебль\". Раздача ролей происходит наугад, количество ролей зависит от количества игроков.\n" +
                "Игровой процесс разделён на две фазы — «день» и «ночь». В дневную фазу происходит общение в чате и голосование, на котором горожане пытаются вычислить мафию. В фазу ночи происходит убийство, совершенное маньяком. Все события, произошедшие в игре отображаются на панельке во время игры.");
    }

    @FXML
    private void rules_btn_confirmClicked(ActionEvent actionEvent) {
        parent.setScene(ScenesNames.MENU);
    }
}
