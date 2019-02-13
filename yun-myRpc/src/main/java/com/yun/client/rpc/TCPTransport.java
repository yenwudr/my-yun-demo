package com.yun.client.rpc;

import com.yun.model.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:20
 */
public class TCPTransport {

    private String host;
    private int port;

    public TCPTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Socket newSocket() throws  RuntimeException{
        System.out.println("创建一个连接");
        Socket socket ;
        try {
            socket = new Socket(host,port);
            return  socket;
        } catch (IOException e) {
            throw new RuntimeException("创建连接失败",e);
        }
    }

    public Object send(RpcRequest rpcRequest){
        Socket socket =null;
        ObjectOutputStream objectOutputStream=null;
        ObjectInputStream objectInputStream =null;
        try {
            socket = newSocket();
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();

            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object result = objectInputStream.readObject();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("发起远程调用异常",e);
        } finally {
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
}
