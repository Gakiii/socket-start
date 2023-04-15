package base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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

    /**
     * 多次发送
     * @param
     */
    public void sendMessage2() throws Exception{
        this.outputStream = socket.getOutputStream();
        Scanner sc = new Scanner(System.in);
        String line = "";
        while (!(line = sc.nextLine()).equals( "EOF")) {
            send(line);
        }
    }

    public void send(String message) throws Exception{
        String sendMsg = message + "\n";
        this.outputStream.write(sendMsg.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据长度发送
     * @param
     * @throws IOException
     */
    public void sendMessage3() throws Exception {
        this.outputStream = socket.getOutputStream();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            sendWithLen(sc.nextLine());
        }
    }
    public void sendWithLen(String message) throws Exception{
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        this.outputStream.write(length >> 8);
        this.outputStream.write(length);
        this.outputStream.write(bytes);
    }

    public static void main(String[] args) throws IOException {
        BaseClient bc = new BaseClient("127.0.0.1", 9799);
        try {
            bc.connetServer();
//            bc.sendSingle("this is client");
//            bc.sendMessage("second send");
            // 多次发送
//            bc.sendMessage2();
            bc.sendMessage3();//根据长度发送
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
