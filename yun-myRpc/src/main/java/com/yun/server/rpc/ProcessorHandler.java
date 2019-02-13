package com.yun.server.rpc;

import com.yun.model.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:09
 */
public class ProcessorHandler implements Runnable {

    private Socket socket;
    private Object service;//服务端发布的服务

    public ProcessorHandler(Socket socket, Object service) {
        this.service=service;
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream=null;
        try {
            objectInputStream=new ObjectInputStream( socket.getInputStream());
            RpcRequest rpcRequest =(RpcRequest) objectInputStream.readObject();

            Object result = invoke(rpcRequest);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Object invoke(RpcRequest rpcRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object[] args = rpcRequest.getParameters();
        Class<?>[] types = new Class[args.length];
        for (int i=0;i<args.length;i++){
            types[i]=args[i].getClass();
        }
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), types);
        return method.invoke(service,args);

    }
}
