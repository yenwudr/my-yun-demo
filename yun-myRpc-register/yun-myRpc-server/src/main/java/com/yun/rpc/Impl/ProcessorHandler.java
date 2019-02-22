package com.yun.rpc.Impl;

import com.google.common.base.Strings;
import com.yun.rpc.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 18:31
 */
public class ProcessorHandler implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(ProcessorHandler.class);

    private Socket socket;
    Map<String,Object> handlerMap ;

    public ProcessorHandler(Socket socket, Map<String,Object> handlerMap) {
        this.handlerMap=handlerMap;
        this.socket=socket;
    }

    /**
     * 线程运行：获取请求的数据，通过反射得到结果
     */
    @Override
    public void run() {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest request = (RpcRequest) inputStream.readObject();
            Object result = invoke(request);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = request.getMethodName();
        Object[] args = request.getParameters();
        Class<?>[] argType = new Class[args.length];
        for (int i=0;i<args.length;i++){
            argType[i]=args[i].getClass();
        }
        String serviceName = request.getClassName();
        String version = request.getVersion();
        if (!Strings.isNullOrEmpty(version)){
            serviceName+="-"+version;
        }
        Object service = handlerMap.get(serviceName);
        Method method = service.getClass().getMethod(methodName,argType);
        return method.invoke(service,args);
    }
}
