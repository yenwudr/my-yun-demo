package com.yun.server.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:06
 */
public class RpcServer {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void publisher(final Object service , int port){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("进入一个连接");
                System.out.println(Thread.activeCount());
                executorService.execute(new ProcessorHandler(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket !=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
