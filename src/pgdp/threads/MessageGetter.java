package pgdp.threads;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageGetter extends ChatCommunication implements Runnable {
    private DataInputStream streamIn = null;
    private Socket socket = null;
    private String userName = null;
    public MessageGetter(Socket socket,String username) throws IOException {
        this.socket = socket;
        this.userName = username;
        this.streamIn =new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }
    @Override
    public void run() {
        boolean done = false;
        while (true){
            try {
                if (chatRoomConnection()) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (!done){
            try {
                String line = streamIn.readUTF();
                sendMessageChatServer(userName+"#username#"+line);
                done = line.equals("LOGOUT");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            streamIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
