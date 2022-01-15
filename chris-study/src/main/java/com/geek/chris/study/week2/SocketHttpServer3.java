package com.geek.chris.study.week2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketHttpServer3 {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8803);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(new Thread(() -> doProcService(socket)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       /* ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10000));
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                poolExecutor.execute(() -> doProcService(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    static void doProcService(Socket socket) {
        try {
            System.out.println(System.currentTimeMillis());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);//获取输出头
            pw.println("HTTP/1.1 200 OK");//协议http1.1 成功
            pw.println("Content-Type:text/html;charset=utf-8");
            String body = "hello, nio3";
            pw.println("Content-Length:" + body.getBytes("utf-8").length);
            pw.println();
            pw.println(body);
            pw.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
