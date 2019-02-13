package com.yun.client;

import com.yun.client.rpc.RpcClientProxy;
import com.yun.service.IHelloService;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:48
 */
public class ClientDemo {

    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        IHelloService helloService = rpcClientProxy.clientProxy(IHelloService.class,"localhost",8088);
        for (int i=0;i<20;i++){
            helloService.say("yun"+i);
        }

    }
}
