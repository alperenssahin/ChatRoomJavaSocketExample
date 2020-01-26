package pgdp.threads;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;

public class ChatRoom implements Runnable {
    private ServerSocket chatRoomServer = null;
    private Socket serverSocket = null;
    private String chatRoomNumber = "";
    private DataInputStream streamIn = null;


    public ChatRoom(String chatRoomNumber) {
        this.chatRoomNumber = chatRoomNumber;
    }

    @Override
    public void run() {
        try {
            chatRoomServer = new ServerSocket(3001);
            System.out.println("ChatRoomServer started: " + chatRoomServer);
            System.out.println("Waiting for chat server");
            serverSocket = chatRoomServer.accept();
            System.out.println("Chat server accepted: " + serverSocket);
            streamIn = new DataInputStream(new BufferedInputStream(serverSocket.getInputStream()));
            boolean done = false;
            while (!done) {
                try {
                    String line = streamIn.readUTF();
                    System.out.println(line);
                    if (line.contains("new_user")) {
                        chatLog(line.split("@")[1]+"  connected chat room!Say hey!");
                    }else {
                        String username = line.split("#username#")[0];
                        String message = line.split("#username#")[1];
                        if (message.equals("LOGOUT")) {
                            chatLog(username + "  logged out!");
                            logOut(username);
                        } else if (message.equals("PENGU")) {
                            sendMessage(username, "Penguins can't taste their food because they swallow their prey whole.");
                        } else if (message.equals("WHOIS")) {
                            sendMessage(username, allUser());
                        } else if (message.contains("@")) {
                            String target = message.split("@")[1].split(" ")[0];
                            String umessage = message.split("@")[1].split(" ")[1];
                            sendMessage(target, umessage);
                        } else {
                            chatLog(username + " : " + message);
                        }
                    }

                    done = line.equals(".stop");
                } catch (IOException ioe) {
                    System.out.println("errr");

                }
            }
            System.out.println("exit exit");
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
//        if (serverSocket != null) {
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (streamIn != null) {
//            try {
//                streamIn.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void chatLog(String message) throws IOException {
        for (CommunicationChannel c : ChatServer.user_list) {
            c.getStreamOut().writeUTF(LocalTime.now() + " : " + message);
        }
    }
    public String allUser(){
        String userlist = "";
        for(CommunicationChannel c : ChatServer.user_list) {
          userlist += c.getUserName()+"\n";
        }
        return userlist;
    }
    public void sendMessage(String userName,String message) throws IOException {
        for(CommunicationChannel c: ChatServer.user_list){
            if(c.getUserName().equals(userName)){
                c.getStreamOut().writeUTF(LocalTime.now() + " : " + message);
                return;
            }
        }
    }
    public static void logOut(String username){
        ChatServer.user_list.removeIf(c -> c.getUserName().equals(username));
    }
}
