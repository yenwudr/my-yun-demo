package com.yun.rpc;

import com.sun.org.apache.regexp.internal.RE;
import com.yun.rpc.model.RpcRequest;
import com.yun.rpc.zk.IServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 14:20
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private IServiceDiscovery serviceDiscovery;
    private String version;

    public RemoteInvocationHandler(IServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version=version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setVersion(version);

        String serviceAddress = serviceDiscovery.discovery(rpcRequest.getClassName());
        TCPTransport tcpTransport = new TCPTransport(serviceAddress);

        return tcpTransport.send(rpcRequest);
    }
}
