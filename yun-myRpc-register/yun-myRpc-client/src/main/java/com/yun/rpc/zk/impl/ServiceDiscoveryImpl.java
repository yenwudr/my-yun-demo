package com.yun.rpc.zk.impl;

import com.yun.rpc.conf.ZkConfig;
import com.yun.rpc.zk.IServiceDiscovery;
import com.yun.rpc.zk.loadbalance.ILoadBanalce;
import com.yun.rpc.zk.loadbalance.impl.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @Project: yun-myRpc-register
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/22 15:48
 */
public class ServiceDiscoveryImpl implements IServiceDiscovery {

    private final static Logger logger = LoggerFactory.getLogger(ServiceDiscoveryImpl.class);
    private List<String> repos;
    private CuratorFramework curatorFramework;
    private String zkAddress;


    public ServiceDiscoveryImpl(String zkAddress) {
        this.zkAddress = zkAddress;
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,5))
                .build();
        curatorFramework.start();
    }

    @Override
    public String discovery(String serviceName) {
        String path = ZkConfig.ZK_REGISTER_PATH+"/"+serviceName;
        try {
            repos=curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取子节点的异常，path:"+path+"，原因："+e);
        }
        registerWatcher(path);
        //负载均衡机制
        ILoadBanalce loadBanalce = new RandomLoadBalance();

        return loadBanalce.selectHost(repos);
    }

    private void registerWatcher(String path){
        PathChildrenCache cache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                repos = curatorFramework.getChildren().forPath(path);
            }
        };
        cache.getListenable().addListener(listener);
        try {
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("注册PathChildrenCacheListener 异常，"+e);
        }
    }
}
