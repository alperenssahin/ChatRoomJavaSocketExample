package pgdp.threads;
import java.net.*;
import java.io.*;

public class ChatClient {
    private Socket socket              = null;
    private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;
    private String userName ="";
    Thread personelChatRoomThread = null;
    PersonalChatRoom personalChatRoom = null;
    public ChatClient(String serverName, int serverPort,String userName)
    {  System.out.println("Establishing connection. Please wait ...");
    this.userName = userName;
        try
        {  socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            personalChatRoom = new PersonalChatRoom(new DataInputStream(new BufferedInputStream(socket.getInputStream())));
            personelChatRoomThread = new Thread(personalChatRoom);
            personelChatRoomThread.start();
            this.start();
            streamOut.writeUTF(this.userName);
            streamOut.flush();
        }
        catch(UnknownHostException uhe)
        {  System.out.println("Host unknown: " + uhe.getMessage());
        ChatRoom.logOut(userName);
        }
        catch(IOException ioe)
        {  System.out.println("Unexpected exception: " + ioe.getMessage());
            ChatRoom.logOut(userName);

        }
        String line = "";
        while (!line.equals(".LOGOUT"))
        {  try
        {  line = console.readLine();
        System.out.println(line+"this is user message");
            streamOut.writeUTF(line);
            streamOut.flush();
        }
        catch(IOException ioe)
        {  System.out.println("Sending error: " + ioe.getMessage());
            ChatRoom.logOut(userName);

        }
        }
    }
    public void start() throws IOException
    {  console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
    }
    public void stop()
    {  try
    {  if (console   != null)  console.close();
        if (streamOut != null)  streamOut.close();
        if (socket    != null)  socket.close();
    }
    catch(IOException ioe)
    {  System.out.println("Error closing ...");
    }
    }
    public static void main(String args[]) throws InterruptedException {
        clientTest c1 = new clientTest("client1");
        clientTest c2 = new clientTest("client2");

        Thread t1 = new Thread(c1);
        t1.start();
//        Thread.sleep(3000);
//        Thread t2 = new Thread(c2);
//        t2.start();
    }
}


