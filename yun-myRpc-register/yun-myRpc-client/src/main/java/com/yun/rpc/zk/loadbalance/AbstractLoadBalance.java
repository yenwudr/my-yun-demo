package com.yun.rpc.zk.loadbalance;

import java.util.List;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 15:59
 */
public abstract class AbstractLoadBalance implements ILoadBanalce {

    @Override
    public String selectHost(List<String> repos) {
        if (repos==null||repos.size()==0){
            return null;
        }
        if (repos.size()==1){
            return repos.get(0);
        }
        return doSelect(repos);
    }

    protected abstract String doSelect(List<String>repos);
}
