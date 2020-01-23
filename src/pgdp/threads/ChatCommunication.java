package pgdp.threads;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatCommunication {
    Socket chatRoomSocket = null;
    DataOutputStream chatRoomOutputStream = null;

    public boolean chatRoomConnection() throws IOException {
        try{
            chatRoomSocket = new Socket("127.0.0.1",3001);
            chatRoomOutputStream = new DataOutputStream(new BufferedOutputStream(chatRoomSocket.getOutputStream()));
            return true;
        }catch (Exception ex){
            return false;
        }
    }
    public void sendMessageChatServer(String message){
        try
        {
            chatRoomOutputStream.writeUTF(message);
            chatRoomOutputStream.flush();
        }
        catch(IOException ioe)
        {  System.out.println("Sending error: " + ioe.getMessage());
        }
    }
    public  boolean isUserExist(String name){

        boolean state = false;
        if(ChatServer.user_list.size() == 0){
            return false;
        }
        for(CommunicationChannel c:ChatServer.user_list){
            if(c.getUserName().equals(name)){
                state = true;
                break;
            }
        }
        return state;
    }
}
