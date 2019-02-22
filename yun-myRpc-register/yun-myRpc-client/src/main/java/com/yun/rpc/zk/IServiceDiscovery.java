package com.yun.rpc.zk;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 15:47
 */
public interface IServiceDiscovery {

    String discovery(String serviceName);
}
