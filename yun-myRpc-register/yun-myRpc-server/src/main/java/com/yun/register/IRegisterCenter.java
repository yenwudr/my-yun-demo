package com.yun.register;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 14:58
 */
public interface IRegisterCenter {

    void register(String serviceName,String serviceAddress);//注册服务名称和地址
}
