package pgdp.threads;

import java.io.DataInputStream;
import java.io.IOException;

class PersonalChatRoom implements Runnable {
    DataInputStream streamIn = null;
    public PersonalChatRoom(DataInputStream streamIn) {
        this.streamIn = streamIn;
    }

    @Override
    public void run() {
        boolean done = false;
        while (!done){
            try
            {  String line = streamIn.readUTF();
                System.out.println(line);
                done = line.equals("LOGOUT");
            }
            catch(IOException ioe)
            {  done = true;
            }
        }
        try {
            streamIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
