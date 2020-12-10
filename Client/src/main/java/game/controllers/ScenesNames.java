package game.controllers;

public enum ScenesNames {
    START("set_nick.fxml"),
    MENU("menu.fxml"),
    ROOMS("rooms.fxml"),
    CURRENT_ROOM("current_room.fxml"),
    ROOM_CREATION("room_creation.fxml"),
    GAME("game.fxml"),
    RULES("rules.fxml"),
    PARAMETERS("parameters_of_room.fxml"),
    GAME_OVER("game_over.fxml");

    private String title;
    ScenesNames(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
