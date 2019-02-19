package com.yun.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/18 10:34
 */
public class ConnectionDemo {

    public static void main(String[] args) {
//        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //添加监听事件
            Watcher watcher = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
                        //如果收到服务的响应事件，连接成功
                        countDownLatch.countDown();
                    }
                }
            };
//            zooKeeper = new ZooKeeper("119.29.157.130:2181",4000, watcher);
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",4000, watcher);
            countDownLatch.await();
            System.out.println(zooKeeper.getState());
            Stat stat = new Stat();
            //添加节点
//            zooKeeper.create("/yun-zk-demo2","0".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//            Thread.sleep(1000);

            Scanner scanner = new Scanner(System.in);
            boolean result =true;
            while (result){
                stat= zooKeeper.exists("/yun-zk-demo", new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        System.out.println(watchedEvent.getType()+"->"+watchedEvent.getPath());
                        try {
                            zooKeeper.exists("/yun-zk-demo",this::process);
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                String s = scanner.nextLine();


                zooKeeper.setData("/yun-zk-demo","1".getBytes(),stat.getVersion());
                byte[] data = zooKeeper.getData("/yun-zk-demo", null, stat);
                System.out.println(new String(data));
                if ("0".equals(s)){
                    result=false;
                }
            }

            byte[] data = zooKeeper.getData("/yun-zk-demo", null, stat);
            System.out.println(new String(data));
            zooKeeper.setData("/yun-zk-demo","1".getBytes(),stat.getVersion());
            byte[] data1 = zooKeeper.getData("/yun-zk-demo", null, stat);
            System.out.println(new String(data1));
            zooKeeper.delete("/yun-zk-demo",stat.getVersion());
            zooKeeper.close();
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
