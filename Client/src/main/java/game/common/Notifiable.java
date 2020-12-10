package game.common;

import game.messages.Message;

import java.io.IOException;

public interface Notifiable {
    void notifyMessageReceived(Message message) throws IOException;
}
