package com.yun.register.impl;

import com.yun.register.IRegisterCenter;
import com.yun.rpc.Impl.RpcServer;
import com.yun.rpc.conf.ZkConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 15:00
 */
public class RegisterCenterImpl implements IRegisterCenter {

    private final static Logger logger = LoggerFactory.getLogger(RegisterCenterImpl.class);
    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.CONNECTION_STR)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,5))
                .build();
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) {

        String servicePath = ZkConfig.ZK_REGISTER_PATH+"/"+serviceName;
        try {
            if (curatorFramework.checkExists().forPath(servicePath)==null){
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath,"0".getBytes());
            }
            String addressPath = servicePath+"/"+serviceAddress;
            String s = curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL).forPath(addressPath, "0".getBytes());
            logger.info("服务注册成功"+s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }
}
