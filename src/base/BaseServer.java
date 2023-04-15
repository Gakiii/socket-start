package base;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BaseServer {
    private ServerSocket server;
    private Socket socket;
    private int port;
    private InputStream inputStream;
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

    public static void main(String[] args) {
        BaseServer bs = new BaseServer(9799);
        try {
            bs.runServerSingle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
