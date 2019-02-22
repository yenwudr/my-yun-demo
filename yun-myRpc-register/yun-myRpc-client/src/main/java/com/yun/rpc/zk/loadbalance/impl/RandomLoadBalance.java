package com.yun.rpc.zk.loadbalance.impl;

import com.yun.rpc.zk.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 16:03
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> repos) {
        int size = repos.size();
        Random random = new Random();
        return repos.get(random.nextInt(size));
    }
}
