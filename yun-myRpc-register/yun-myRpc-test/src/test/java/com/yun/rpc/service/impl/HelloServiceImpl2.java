package com.yun.rpc.service.impl;


import com.yun.anno.RpcAnnotation;
import com.yun.rpc.service.IHelloService;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 11:36
 */
@RpcAnnotation(value = IHelloService.class)
public class HelloServiceImpl2 implements IHelloService {
    @Override
    public String say(String msg) {
        return "[I'm 8081]Hello,"+msg;
    }
}
