package com.yun.client.rpc;

import com.yun.model.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:17
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;
    private int port;

    public RemoteInvocationHandler(String host, int port) {
        this.host=host;
        this.port=port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);

        TCPTransport tcpTransport = new TCPTransport(this.host,this.port);
        return  tcpTransport.send(request);
    }
}
