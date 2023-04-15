package base;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BaseClient {
    private String serverHost;
    private int serverPort;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public BaseClient(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
    }

    public void connetServer() throws Exception {
        this.socket = new Socket(this.serverHost, this.serverPort);
        this.outputStream = socket.getOutputStream();
    }

    public void sendSingle(String message) throws Exception {
        try {
            this.outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.outputStream.close();
        this.socket.close();
    }

    public void sendMessage(String message) throws Exception {
        this.outputStream = socket.getOutputStream();
        this.outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        this.socket.shutdownOutput();
        this.inputStream = socket.getInputStream();
        String receipt = new String(inputStream.readAllBytes(), "UTF-8");
        System.out.println(receipt);
        this.inputStream.close();
        this.socket.close();
    }

    public static void main(String[] args) {
        BaseClient bc = new BaseClient("127.0.0.1", 9799);
        try {
            bc.connetServer();
//            bc.sendSingle("this is client");
            bc.sendMessage("second send");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
