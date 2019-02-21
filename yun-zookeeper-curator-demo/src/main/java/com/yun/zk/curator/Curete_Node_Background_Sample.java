package com.yun.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.sql.ClientInfoStatus;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Project: yun-zookeeper-curator-demo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 15:44
 */
public class Curete_Node_Background_Sample {

    private static  String path ="/zk-book";//默认根路径
    private static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("119.29.157.130:2182")
            .sessionTimeoutMs(4000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    private static CountDownLatch countDownLatch = new CountDownLatch(2);
    private static ExecutorService tp = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        client.start();
        System.out.println("Main thread:"+Thread.currentThread().getName());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                        System.out.println("event[code:"+curatorEvent.getResultCode() +",type:"+curatorEvent.getType()+"]");
                        System.out.println("Thread of processResult:"+Thread.currentThread().getName());
                        countDownLatch.countDown();
                    }
                },tp).forPath(path,"0".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .inBackground((CuratorFramework curatorFramework, CuratorEvent curatorEvent) -> {
                    System.out.println("event[code:"+curatorEvent.getResultCode() +",type:"+curatorEvent.getType()+"]");
                    System.out.println("Thread of processResult:"+Thread.currentThread().getName());
                    countDownLatch.countDown();
                }).forPath(path, "0".getBytes());
        countDownLatch.await();
        tp.shutdown();
    }

}
