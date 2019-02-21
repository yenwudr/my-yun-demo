package com.yun.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Project: yun-zookeeper-curator-demo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 15:44
 */
public class NodeCache_Sample {

    private static  String path ="/nodecache";//默认根路径
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("119.29.157.130:2182")
            .sessionTimeoutMs(4000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .namespace("zk-book")
            .build();


    public static void main(String[] args) throws Exception {
        client.start();

        NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("Node data update , new data:"+new String(nodeCache.getCurrentData().getData()));
            }
        });

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(path,"0".getBytes());
        Thread.sleep(1000);
        client.setData().forPath(path,"u".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        System.in.read();
    }

}
