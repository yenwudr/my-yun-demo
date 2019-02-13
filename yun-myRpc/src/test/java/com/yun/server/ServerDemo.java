package com.yun.server;

import com.yun.server.rpc.RpcServer;
import com.yun.service.HelloServiceImpl;
import com.yun.service.IHelloService;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:49
 */
public class ServerDemo {

    public static void main(String[] args) {
        IHelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.publisher(helloService,8088);
        System.out.println("服务发布成功！");
    }
}
