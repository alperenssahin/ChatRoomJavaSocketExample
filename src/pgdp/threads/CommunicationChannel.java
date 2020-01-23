package pgdp.threads;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class CommunicationChannel {
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut =null;
    private Socket socket = null;
    private String userName = "";
    public CommunicationChannel(Socket socket,String userName) throws IOException {
        this.socket = socket;
        this.userName = userName;
        this.streamIn =  new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        this.streamOut = new DataOutputStream(this.socket.getOutputStream());
    }

    public DataInputStream getStreamIn() {
        return streamIn;
    }

    public DataOutputStream getStreamOut() {
        return streamOut;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
        if (streamIn != null) streamIn.close();
        if (streamOut != null) streamOut.close();
    }

}
