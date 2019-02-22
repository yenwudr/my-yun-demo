package com.yun.rpc;

import com.yun.rpc.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 11:47
 */
public class TCPTransport {
    private final static Logger logger = LoggerFactory.getLogger(TCPTransport.class);

    private String serviceAddress;

    public TCPTransport(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public Socket newSocket(){
        logger.info("创建一个链接");
        Socket socket=null;
        try {
            String[] address = serviceAddress.split(":");
            socket = new Socket(address[0],Integer.parseInt(address[1]));
            return socket;
        } catch (IOException e) {
            logger.error("创建链接失败：",e);
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    public Object send(RpcRequest request) {
        logger.info("进入发送方法，RpcRequest:"+request.toString());
        Socket socket =null;
        ObjectOutputStream objectOutputStream=null;
        ObjectInputStream objectInputStream =null;
        try {
            socket = newSocket();
            logger.debug("创建输出流，请求数据");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            logger.debug("创建输入流，接收数据");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object result = objectInputStream.readObject();
            return result;

        } catch (Exception e) {
            logger.error("连接失败，",e);
            e.printStackTrace();
        } finally {
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("关闭socket失败",e);
                    e.printStackTrace();
                }
            }
            if (objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    logger.error("关闭输出了失败",e);
                    e.printStackTrace();
                }
            }
            if (objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    logger.error("关闭输入流失败",e);
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
