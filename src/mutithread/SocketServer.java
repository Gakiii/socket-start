package mutithread;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public static int count = 0;
    public static void main(String args[]) throws Exception {
        // 监听指定的端口
        int port = 7797;

        ServerSocket server = new ServerSocket(port);
        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");

        //如果使用多线程，那就需要线程池，防止并发过高时创建过多线程耗尽资源
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        while (true) {
            Socket socket = server.accept();
            ++count;
            System.out.println("count现在是 --->" + count);
            Runnable runnable=()->{
                try {
                    while (true) {
                        InputStream inputStream = socket.getInputStream();
                        int first = inputStream.read();
                        if (first == -1) {
                            throw new RuntimeException("disconnect");
                        }
                        int second = inputStream.read();
                        int length = (first << 8) + second;
                        byte[] msg = new byte[length];
                        inputStream.read(msg);
                        System.out.println("message from [" + Thread.currentThread().getName() + "]" + new String(msg, StandardCharsets.UTF_8));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            };
            threadPool.submit(runnable);
        }

    }
}