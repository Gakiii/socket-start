package mutithread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatSocket {
    private String host;
    private int port;
    private Socket socket;
    private ServerSocket serverSocket;
    private OutputStream outputStream;

    public void runAsServer(int port) throws IOException{
        this.serverSocket = new ServerSocket(port);
        System.out.println("[log] server start at port:" + port);
        this.socket = this.serverSocket.accept();
        System.out.println("[log] servet receive info form" + socket.getInetAddress());

        Thread listenThread = new Thread(new ListenThread(this.socket));
        listenThread.start();
        waitAndSend();
    }
    
    public void runAsClient(String host, int port) throws IOException{
        this.socket = new Socket(host, port);
        Thread thread = new Thread(new ListenThread(this.socket));
        thread.start();
        waitAndSend();
    }

    private void waitAndSend() throws IOException{
        this.outputStream = this.socket.getOutputStream();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            sendMessage(sc.nextLine());
        }
    }

    private void sendMessage(String message) throws IOException {
        byte[] msgBytes = message.getBytes("UTF-8");
        int length = msgBytes.length;
        outputStream.write(length>>8);
        outputStream.write(length);
        outputStream.write(msgBytes);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChatSocket chatSocket = new ChatSocket();
        System.out.println("1. server vs 2. client");
        int i = sc.nextInt();
        sc.nextLine();
        if (i == 1) {
            int port = 7797;
            try {
                chatSocket.runAsServer(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (i == 2) {
            String host = "127.0.0.1";
            int port = 7797;
            try {
                chatSocket.runAsClient(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
