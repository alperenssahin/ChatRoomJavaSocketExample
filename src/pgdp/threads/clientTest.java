package pgdp.threads;

public class clientTest implements Runnable {
    String clientName = "";

    public clientTest(String clientName) {
        this.clientName = clientName;
    }
    @Override
    public void run() {
        new ChatClient("127.0.0.1",3000,clientName);
    }
}
