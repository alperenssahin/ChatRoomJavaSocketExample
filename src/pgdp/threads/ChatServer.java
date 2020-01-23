package pgdp.threads;

import java.net.*;
import java.io.*;
import java.time.LocalTime;
import java.util.LinkedList;

public class ChatServer extends ChatCommunication{
    public static LinkedList<CommunicationChannel> user_list = new LinkedList<>();
    private ServerSocket server = null;
    Thread chatRoomThread = null;
    ChatRoom chatRoom = null;
    Thread messageGetterThread = null;
    MessageGetter messageGetter = null;

    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            chatRoom = new ChatRoom("room_1");
            chatRoomThread = new Thread(chatRoom);
            chatRoomThread.start();
            System.out.println("ChatRoom started: at port 3001 ");
            while (!chatRoomConnection()){
                //it is wait to connect to chatRoom
                Thread.sleep(1000);
            }
            boolean done = false;
            while(!done){
                if(user_list.size() == 50){
                    System.out.println("Server is full");
                    Thread.sleep(1000);
                    continue;
                }
                System.out.println("Waiting for a client ...");
                Socket socket = server.accept();
                String username = new DataInputStream(new BufferedInputStream(socket.getInputStream())).readUTF();
                System.out.println("user request: "+username);
                if(!username.equals("") && !isUserExist(username)){
                    user_list.add(new CommunicationChannel(socket,username));
                    sendMessageChatServer("new_user@"+username);
                    System.out.println("Client accepted: " + socket);
                    messageGetter = new MessageGetter(socket,username);
                    messageGetterThread = new Thread(messageGetter);
                    System.out.println("total user: " + user_list.size());
                }else{
                    System.out.println("Invalid user request ");
                }
            }
//            boolean done = false;
//            while (!done) {
//                try {
//                    String line = streamIn.readUTF();
//                    System.out.println(line);
//                    done = line.equals(".bye");
//                } catch (IOException ioe) {
//                    done = true;
//                }
//            }
        } catch (IOException | InterruptedException ioe) {
            System.out.println(ioe);
        }
    }

    public static void main(String args[]) {
        ChatServer server = null;
        server = new ChatServer(3000);
    }
}
