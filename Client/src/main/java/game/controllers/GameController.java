package game.controllers;

import game.client.ClientApplication;
import game.gamelogic.Player;
import game.gamelogic.Role;
import game.gamelogic.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;

public class GameController {

    public GridPane game_pane_allPlayers;
    public ChoiceBox<String> game_choiseBox_choosePlayer;
    public Button game_btn_confirmVote;
    public TextArea game_text_chat;
    public TextField game_editText_sendMessage;
    public TextArea game_text_eventsOfGame;
    public Button game_btn_send;
    public AnchorPane game_anchor_vote;
    public Text game_text_hint;
    public AnchorPane game_anchor_time;
    public Text game_text_minutes;
    public Text game_text_seconds;
    public ImageView game_iv_playerRole;
    public TextArea game_text_descriptionOfRole;
    private Collection<Player> playersCollection = new ArrayList<>();
    private ObservableList<String> playersNames = FXCollections.observableArrayList();

    private ClientApplication parent;

    public void setParent(ClientApplication parent) {
        this.parent = parent;
    }

    public void getMessage(String string) {
        game_text_chat.appendText(string + "\n");
    }

    public void receiveSystemMessage(String string) {
        game_text_eventsOfGame.appendText(string + "\n");
    }

    @FXML
    private void game_btn_sendOnClick(ActionEvent actionEvent) {
        parent.sendChatMessage(game_editText_sendMessage.getText());
        game_editText_sendMessage.clear();
    }

    public void setPlayerRoleImage(int id) {
        for (Player player : playersCollection) {
            if (id == player.getId()) {
                if (player.getRole() == Role.KILLER) {
                    game_iv_playerRole.setImage(new Image("killing.jpg"));
                    game_text_descriptionOfRole.setText(Role.KILLER.getDescription());
                }
                if (player.getRole() == Role.RESCUER) {
                    game_iv_playerRole.setImage(new Image("saving.jpg"));
                    game_text_descriptionOfRole.setText(Role.RESCUER.getDescription());
                }
                if (player.getRole() == Role.CONSTABLE) {
                    game_iv_playerRole.setImage(new Image("checking.jpg"));
                    game_text_descriptionOfRole.setText(Role.CONSTABLE.getDescription());
                }
                if (player.getRole() == Role.CITIZEN) {
                    game_iv_playerRole.setImage(new Image("citizen.jpg"));
                    game_text_descriptionOfRole.setText(Role.CITIZEN.getDescription());
                }
                break;
            }
        }
    }

    public void dayStarted(Collection<Player> players, int seconds) {
        updatePlayersList(players);
        playersNames.clear();
        for (Player player : playersCollection) {
            if (player.getStatus() == Status.ALIVE) {
                playersNames.add(player.getName());
            }
        }
        game_choiseBox_choosePlayer.setItems(playersNames);
        startTimer(seconds);
    }

    public void nightStarted(Collection<Player> players, int seconds) {
        updatePlayersList(players);
        playersNames.clear();
        for (Player player : playersCollection) {
            if (player.getStatus() == Status.ALIVE) {
                playersNames.add(player.getName());
            }
        }
        game_choiseBox_choosePlayer.setItems(playersNames);
        startTimer(seconds);
    }

    public void gameOver(Collection<Player> players, String finalMessage) {
        updatePlayersList(players);
        receiveSystemMessage(finalMessage);
        parent.setScene(ScenesNames.GAME_OVER);
    }

    public void updatePlayersList(Collection<Player> collection) {
        playersCollection.clear();
        playersCollection.addAll(collection);
        game_pane_allPlayers.getChildren().clear();
        game_pane_allPlayers.getColumnConstraints().add(new ColumnConstraints(110, 110, 110));
        int i = 0;
        int j = 0;
        for (Player player : playersCollection) {
            GridPane playerCard = new GridPane();
            playerCard.setAlignment(Pos.CENTER);
            Text text = new Text();
            text.setWrappingWidth(100);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setText(player.getName());
            ImageView imageView = new ImageView();
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            if (player.getStatus() == Status.ALIVE) {
                imageView.setImage(new Image("alive_player.jpg"));
            }
            if (player.getStatus() == Status.DEAD) {
                imageView.setImage(new Image("dead_player.jpg"));
            }
            playerCard.add(text, 0, 0);
            playerCard.add(imageView, 0, 1);
            game_pane_allPlayers.add(playerCard, i, j);
            i++;
            if (i % 4 == 0) {
                j++;
                i = 0;
            }
        }
    }

    @FXML
    private void game_btn_confirmVoteOnClick(ActionEvent actionEvent) {
        String name = game_choiseBox_choosePlayer.getValue();
        for (Player player : playersCollection) {
            if (name.equals(player.getName())) {
                parent.sendVote(player.getId());
                game_text_hint.setText("Вам еще не дали право голосовать.");
                turnOffVisibility();
                break;
            }
        }
    }

    private void startTimer(int seconds) {
        if (seconds > 0) {
            turnOnVisibility();
            int[] time = {seconds};
            game_text_minutes.setText("0" + time[0] / 60);
            int sec = time[0] % 60;
            if (sec < 10) {
                game_text_seconds.setText("0" + sec);
            } else {
                game_text_seconds.setText("" + sec);
            }
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(1000),
                            ae -> {
                                time[0]--;
                                game_text_minutes.setText("0" + time[0] / 60);
                                int s = time[0] % 60;
                                if (s < 10) {
                                    game_text_seconds.setText("0" + s);
                                } else {
                                    game_text_seconds.setText("" + s);
                                }
                            }
                    )
            );
            timeline.setCycleCount(seconds);
            timeline.play();
            timeline.setOnFinished(event -> Platform.runLater(this::turnOffVisibility));
        }
    }

    private void turnOnVisibility() {
        game_anchor_vote.setVisible(true);
        game_anchor_time.setVisible(true);
        game_text_hint.setText("Можете выбрать из списка кого угодно. Мы никому не скажем. Честно.");

    }

    private void turnOffVisibility() {
        game_anchor_vote.setVisible(false);
        game_anchor_time.setVisible(false);
        game_text_hint.setText("Вам еще не дали право голосовать.");
    }

    @FXML
    private void game_btn_leaveGameOnClicked(ActionEvent actionEvent) {
        parent.leaveRoom();
    }

}
