package com.yun.rpc.client;

import com.yun.rpc.Impl.RpcServer;
import com.yun.rpc.RpcClientProxy;
import com.yun.rpc.conf.ZkConfig;
import com.yun.rpc.service.IHelloService;
import com.yun.rpc.zk.IServiceDiscovery;
import com.yun.rpc.zk.impl.ServiceDiscoveryImpl;
import org.junit.Before;
import org.junit.Test;
import sun.misc.OSEnvironment;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 14:25
 */
public class RpcClientTest {


    private RpcClientProxy rpcClientProxy;
    private IServiceDiscovery iServiceDiscovery;

    @Before
    public void createObj(){
        iServiceDiscovery = new ServiceDiscoveryImpl(ZkConfig.CONNECTION_STR);
        rpcClientProxy = new RpcClientProxy(iServiceDiscovery);
    }

    @Test
    public void send(){

        for (int i=0;i<10;i++){
            IHelloService helloService = rpcClientProxy.clientProxy(IHelloService.class,"");
            System.out.println(helloService.say("yun"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
