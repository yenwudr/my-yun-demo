package com.yun.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import sun.java2d.SurfaceDataProxy;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/18 11:03
 */
public class WatcherDemo {

    public static void main(String[] args) {
       // ZooKeeper zooKeeper = null;
        try {
            CountDownLatch downLatch = new CountDownLatch(1);
           final ZooKeeper zooKeeper = new ZooKeeper("119.29.157.130:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("默认事件："+watchedEvent.getType());
                    if (watchedEvent.getState()== Event.KeeperState.SyncConnected){
                        //收到zk服务器的事件通知，连接成功
                        downLatch.countDown();
                    }
                }
            });
            downLatch.await();
            System.out.println(zooKeeper.getState());

            zooKeeper.create("/yun-zk-demo","1".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            // exists
            //通过exists 绑定事件
            ZooKeeper finalZooKeeper = zooKeeper;
            Stat stat = zooKeeper.exists("/yun-zk-demo", new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println(watchedEvent.getType()+"->"+watchedEvent.getPath());
//                    try {
//                        zooKeeper.exists(watchedEvent.getPath(),true);
//                    } catch (KeeperException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            stat = zooKeeper.setData("/yun-zk-demo", "2".getBytes(), stat.getVersion());
            Thread.sleep(1000);
            zooKeeper.delete("/yun-zk-demo",stat.getVersion());
            System.in.read();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            if (zooKeeper!=null){
//                try {
//                    zooKeeper.close();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
