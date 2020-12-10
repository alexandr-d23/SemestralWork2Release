package game.common;

import game.messages.Message;

import java.io.IOException;

public interface Connection {

    void send(Message message) throws IOException;

    Information getInformation();

    void close();

    int getId();

    void setNotifiable(Notifiable notifiable);

    boolean isConnected();

}
