package game.common;

import game.messages.Message;

import java.io.*;
import java.net.Socket;

public class PlayerConnection implements Connection{
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private Information userInformation;
    private Notifiable notifiable;

    public PlayerConnection(Socket socket, Notifiable notifiable, int id){
        this.socket = socket;
        this.notifiable = notifiable;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            receiveUserInformation();
            userInformation.setName(userInformation.getName()+"#"+id);
            userInformation.setId(id);
            sendId(id);
            monitoring();
        }
        catch (IOException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    private void receiveUserInformation() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(in);
            userInformation = (game.common.Information)inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalStateException("Server connection : receiveInformation throw exception"+e.getMessage());
        }
    }

    private void sendId(int id) throws IOException {
        DataOutputStream dataOutputStream= new DataOutputStream(out);
        dataOutputStream.writeInt(id);
    }

    private void monitoring(){
        Thread thread = new Thread(() -> {
            try {
                while (isConnected()) {
                    int amount = in.available();
                    if (amount != 0) {
                        ObjectInputStream objIn = new ObjectInputStream(in);
                        Message msg = (Message) objIn.readObject();
                        notifiable.notifyMessageReceived(msg);
                    }
                    else {
                        Thread.sleep(200);
                    }
                }
            }
            catch (IOException | ClassNotFoundException | InterruptedException e){
                throw new IllegalStateException(e.getMessage());
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    @Override
    public void send(Message message) throws IOException {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(message);
            objOut.flush();
    }

    @Override
    public void setNotifiable(Notifiable notifiable) {
        this.notifiable = notifiable;
    }

    @Override
    public Information getInformation() {
        return userInformation;
    }

    @Override
    public void close() {
        try {
            socket.close();
        }
        catch (IOException ignored) {

        }
    }

    @Override
    public int getId() {
        return userInformation.getId();
    }

    @Override
    public boolean isConnected() {
        return !socket.isClosed();
    }
}
