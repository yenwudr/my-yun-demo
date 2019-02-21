package java.com.yun.rpc.Impl;

import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 18:31
 */
public class ProcessorHandler implements Runnable {

    private Socket socket;
    private Object service;

    public ProcessorHandler(Socket socket, Object service) {
        this.service=service;
        this.socket=socket;
    }

    @Override
    public void run() {
        ObjectOutputStream outputStream = null;
    }
}
