package base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BaseServer {
    private ServerSocket server;
    private Socket socket;
    private int port;
    private InputStream inputStream;
    private OutputStream outputStream;
    private static final  int MAX_BUFFER_SIZE = 1024;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BaseServer(int port) {
        this.port = port;
    }

    public void  runServerSingle() throws Exception {
        this.server = new ServerSocket(this.port);
        System.out.println("[base socket server started]");
        this.socket = server.accept();
        this.inputStream = socket.getInputStream();

        byte[] readBytes = new byte[MAX_BUFFER_SIZE];
        int msgLen = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while((msgLen = inputStream.read(readBytes)) != -1) {
            stringBuilder.append(new String(readBytes, 0, msgLen, "UTF-8"));
        }
        System.out.println("get message from client [" + stringBuilder + "]");
        inputStream.close();
        socket.close();
        server.close();
    }

    public void runServer() throws Exception{
        this.server = new ServerSocket(this.port);
        this.socket = server.accept();
        this.inputStream = socket.getInputStream();
        String message = new String(inputStream.readAllBytes(), "UTF-8");
        System.out.println("received message: + [" + message + "]");
        this.socket.shutdownInput();
        this.outputStream = socket.getOutputStream();
        String receipt = "We received your message: [" + message + "]";
        outputStream.write(receipt.getBytes(StandardCharsets.UTF_8));
        this.outputStream.close();
        this.socket.close();
    }

    /**
     * 多次对话
     * @throws Exception
     */
    public void runServer2() throws Exception {
        this.server = new ServerSocket(this.port);
        System.out.println("server has been started.");
        this.socket = server.accept();
        this.inputStream = socket.getInputStream();
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()) {
            System.out.println("get info from client : [" + sc.nextLine() + "]");
        }
        this.inputStream.close();
        socket.close();
    }


    /**
     * 根据长度读取
     * @throws IOException
     */
    public void runServer3() throws Exception {
        this.server = new ServerSocket(this.port);
        this.socket = server.accept();
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        byte[] bytes;
        while (true) {
            int first = inputStream.read();
            if (first == -1) {
                System.out.println("This is no data, close socket");
                this.socket.close();
                break;
            }
            int second = inputStream.read();
            int length = (first << 8) + second;
            bytes = new byte[length];
            inputStream.read(bytes);
            System.out.println("Received msg form client: [" + new String(bytes, "UTF-8") + "]");
        }
    }

    public static void main(String[] args) {
        BaseServer bs = new BaseServer(9799);
        try {
//            bs.runServerSingle();
//            bs.runServer();
            //多次接受
//            bs.runServer2();
            bs.runServer3(); // 根据长度读取
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
