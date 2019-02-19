package com.yun.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/18 11:03
 */
public class WatcherDemo1 {

    public static void main(String[] args) {
       // ZooKeeper zooKeeper = null;
        try {
            CountDownLatch downLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper("119.29.157.130:2181", 4000, new Watcher() {
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

            //通过exists 绑定事件
            Stat stat = new Stat();
            Watcher watcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println(watchedEvent.getType()+"->"+watchedEvent.getPath());
                    try {
                        zooKeeper.exists(watchedEvent.getPath(),this::process);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Watcher subWatcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("sub->  "+watchedEvent.getType()+"->"+watchedEvent.getPath());
                    try {
                        zooKeeper.exists(watchedEvent.getPath(),this::process);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Watcher getWatcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("我正在监听："+watchedEvent.getType()+"->"+watchedEvent.getPath());
                    try {
                        zooKeeper.exists(watchedEvent.getPath(),this::process);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            Watcher getChildWatcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("我正在监听sub->"+watchedEvent.getType()+"->"+watchedEvent.getPath());
                    try {
                        zooKeeper.exists(watchedEvent.getPath(),this::process);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            zooKeeper.exists("/yun-zk-demo", watcher);
            zooKeeper.exists("/yun-zk-demo/sub-zk", subWatcher);

            zooKeeper.create("/yun-zk-demo","1".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            // exists
            byte[] data = zooKeeper.getData("/yun-zk-demo", getWatcher, stat);
            List<String> children = zooKeeper.getChildren("/yun-zk-demo", getChildWatcher, stat);
            System.out.println(new String(data));
            stat = zooKeeper.setData("/yun-zk-demo", "2".getBytes(), stat.getVersion());

            Thread.sleep(1000);

            Stat subStat = new Stat();
            zooKeeper.create("/yun-zk-demo/sub-zk","qq".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            Thread.sleep(1000);
            byte[] subdata = zooKeeper.getData("/yun-zk-demo/sub-zk",null,subStat);
            System.out.println(new String(subdata));

            subStat = zooKeeper.setData("/yun-zk-demo/sub-zk", "cc".getBytes(), subStat.getVersion());
            Thread.sleep(1000);
            zooKeeper.delete("/yun-zk-demo/sub-zk",subStat.getVersion());
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
