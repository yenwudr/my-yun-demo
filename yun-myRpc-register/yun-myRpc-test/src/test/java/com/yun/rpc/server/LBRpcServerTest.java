package com.yun.rpc.server;

import com.yun.register.IRegisterCenter;
import com.yun.register.impl.RegisterCenterImpl;
import com.yun.rpc.Impl.RpcServer;
import com.yun.rpc.service.IHelloService;
import com.yun.rpc.service.impl.HelloServiceImpl;
import com.yun.rpc.service.impl.HelloServiceImpl2;
import org.junit.Test;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 10:47
 */
public class LBRpcServerTest {

    @Test
   public void serverTest(){
        IHelloService helloService = new HelloServiceImpl();

        IRegisterCenter registerCenter = new RegisterCenterImpl();

        RpcServer rpcServer = new RpcServer(registerCenter,"127.0.0.1:8080");
        rpcServer.bind(helloService);
        rpcServer.publisher();

   }
}
