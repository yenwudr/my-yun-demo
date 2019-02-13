package com.yun.client.rpc;

import java.lang.reflect.Proxy;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:13
 */
public class RpcClientProxy {

    public <T> T clientProxy(final Class<T> internaceCls,final String host,final int port){
        return (T) Proxy.newProxyInstance(internaceCls.getClassLoader(),
                new Class[]{internaceCls},
                new RemoteInvocationHandler(host,port) );
    }
}
