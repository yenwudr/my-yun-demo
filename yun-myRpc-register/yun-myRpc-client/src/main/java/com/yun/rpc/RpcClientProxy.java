package com.yun.rpc;

import com.yun.rpc.zk.IServiceDiscovery;

import java.lang.reflect.Proxy;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 14:17
 */
public class RpcClientProxy {
    private IServiceDiscovery serviceDiscovery;

    public RpcClientProxy(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T clientProxy(Class<T> internaceCls,String version){
        return (T) Proxy.newProxyInstance(internaceCls.getClassLoader(),
                new Class[]{internaceCls},
                new RemoteInvocationHandler(serviceDiscovery,version));
    }
}
