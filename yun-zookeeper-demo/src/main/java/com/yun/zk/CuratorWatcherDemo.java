package com.yun.zk;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/19 16:14
 */
public class CuratorWatcherDemo {
    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework =CuratorFrameworkFactory.builder()
                .connectString("119.29.157.130:2181").sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("curator").build();
        curatorFramework.start();

//        addListerWithNodeCache(curatorFramework,"/zk");
//        addListerWithPathChildrenCache(curatorFramework,"/zk");
        addListerWithTreeCache(curatorFramework,"/zk");

    }

    /**
     * PathChildrenCache 监听一个节点下的子节点的创建、删除、更新
     *
     * TreeCache 综合PathChildrenCache 和 NodeCache的特性
     */
    /**
     *
     * @param curatorFramework
     * @param path
     * @throws Exception
     */
    private  static void addListerWithPathChildrenCache(CuratorFramework curatorFramework ,String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(curatorFramework,path,false);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println("event->"+event.getType());
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();

        System.in.read();
    }

    /**
     *
     * @param curatorFramework
     * @param path
     */
    private  static void addListerWithNodeCache(CuratorFramework curatorFramework ,String path) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,path,false);
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("event"+nodeCache.getCurrentData().getPath());
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();

        System.in.read();
    }

    private  static void addListerWithTreeCache(CuratorFramework curatorFramework ,String path) throws Exception {
        TreeCache cache = new TreeCache(curatorFramework,path);
        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println("event->"+event.getType());
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();
        System.in.read();
    }
}
