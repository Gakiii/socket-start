package mutithread;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ListenThread implements Runnable{
    private Socket socket;
    private InputStream inputStream;
    public ListenThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() throws RuntimeException{
        try {
            this.inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        while (true) {
            try {
                int first = inputStream.read();
                if (first == -1) {
                    throw new RuntimeException("disconnect");
                }
                int second = inputStream.read();
                int length = (first << 8) + second;
                byte[] msg = new byte[length];
                this.inputStream.read(msg);
                System.out.println("message from [" + Thread.currentThread().getName()+ "]" + new String(msg, StandardCharsets.UTF_8) );
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
