package java.com.yun.rpc.Impl;

import java.com.yun.rpc.IRpcServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 18:26
 */
public class RpcServer implements IRpcServer {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void publisher(Object service, int port) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket accept = serverSocket.accept();
                System.out.println("收到一个连接");
                executorService.execute(new ProcessorHandler(accept,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
