package com.yun.service;

/**
 * @Project: yun-myRpc
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/13 16:47
 */
public class HelloServiceImpl implements IHelloService{
    @Override
    public String say(String msg) {
        return "hello,"+msg;
    }
}
