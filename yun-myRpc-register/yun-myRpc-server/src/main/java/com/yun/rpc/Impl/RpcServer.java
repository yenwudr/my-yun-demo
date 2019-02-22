package com.yun.rpc.Impl;

import com.yun.anno.RpcAnnotation;
import com.yun.register.IRegisterCenter;
import com.yun.rpc.IRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 18:26
 */
public class RpcServer implements IRpcServer {

    private final static Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private IRegisterCenter registerCenter;
    private String serviceAddress;

    Map<String,Object> handlerMap = new HashMap<String, Object>();

    public RpcServer(IRegisterCenter registerCenter, String serviceAddress) {
        this.registerCenter = registerCenter;
        this.serviceAddress = serviceAddress;
    }

    /**
     * 绑定服务名称和对象
     * @param services
     */
    public void bind(Object... services){
        for (Object service:services){
            RpcAnnotation annotation = service.getClass().getAnnotation(RpcAnnotation.class);
            String serviceName = annotation.value().getName();
            String version = annotation.version();
            if (version!=null&&!"".equals(version)){
                serviceName=serviceName+"-"+version;
            }
            handlerMap.put(serviceName,service);//绑定服务接口名称对应的服务
        }

    }

    @Override
    public void publisher() {
        ServerSocket serverSocket = null;
//        logger.info("服务启动成功，port:"+port);
        try {
            String[] address = serviceAddress.split(":");
            serverSocket = new ServerSocket(Integer.valueOf(address[1]));
            for (String interfaceName:handlerMap.keySet()){
                registerCenter.register(interfaceName,serviceAddress);
                logger.info("服务注册成功："+interfaceName+"->"+serviceAddress);
            }
            while (true){
                Socket accept = serverSocket.accept();
//                logger.info("收到一个连接");
                executorService.execute(new ProcessorHandler(accept,handlerMap));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.error("关闭socket失败,"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

}
