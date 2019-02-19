package com.yun.zk;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/19 16:14
 */
public class CuratorDemo {
    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework =CuratorFrameworkFactory.builder()
                .connectString("119.29.157.130:2181").sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("curator").build();
        curatorFramework.start();
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/zk/nodel","1".getBytes());

//        curatorFramework.delete().forPath("/zk/nodel");

        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath("/zk/nodel");

        curatorFramework.setData().withVersion(stat.getVersion()).forPath("/zk/nodel","yy".getBytes());
        curatorFramework.close();

    }
}
