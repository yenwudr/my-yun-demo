package com.yun.rpc.zk.loadbalance;

import java.util.List;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 15:58
 */
public interface ILoadBanalce {

    String selectHost(List<String>repos);
}
