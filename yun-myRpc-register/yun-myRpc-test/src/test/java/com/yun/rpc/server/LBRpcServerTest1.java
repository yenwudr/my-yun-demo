package com.yun.rpc.server;

import com.yun.register.IRegisterCenter;
import com.yun.register.impl.RegisterCenterImpl;
import com.yun.rpc.Impl.RpcServer;
import com.yun.rpc.service.IHelloService;
import com.yun.rpc.service.impl.HelloServiceImpl;
import com.yun.rpc.service.impl.HelloServiceImpl2;
import org.junit.Test;

import java.util.UUID;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 10:47
 */
public class LBRpcServerTest1 {

    @Test
   public void serverTest(){
        IHelloService helloService1 = new HelloServiceImpl2();

        IRegisterCenter registerCenter = new RegisterCenterImpl();

        RpcServer rpcServer = new RpcServer(registerCenter,"127.0.0.1:8081  ");
        rpcServer.bind(helloService1);
        rpcServer.publisher();
        UUID uuid = UUID.randomUUID();
        uuid.toString();


    }
}
