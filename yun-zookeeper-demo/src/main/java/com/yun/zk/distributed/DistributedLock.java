package com.yun.zk.distributed;

import com.sun.deploy.util.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.jboss.netty.util.internal.StringUtil;
import sun.swing.StringUIClientPropertyKey;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 11:16
 */
public class DistributedLock implements Watcher,Lock {

    private ZooKeeper zk = null;
    private String ROOT_LOCK="/locks";//根节点
    private String WAIT_LOCK;//等待的前一个锁
    private String CURRENT_LOCK;//当前锁
    private CountDownLatch countDownLatch;

    public DistributedLock() {
        try {
            this.zk = new ZooKeeper("119.29.157.130:2182",4000,this);
            Stat stat = zk.exists(ROOT_LOCK,false);
            if (stat==null){
                zk.create(ROOT_LOCK,"0".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tryLock() {
        try {
           CURRENT_LOCK=  zk.create(ROOT_LOCK+"/",
                    "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+" ,try lock");
            List<String> children = zk.getChildren(ROOT_LOCK, false);//获取根节点下的所有的子节点
            SortedSet<String> sortedSet = new TreeSet<>();
            for (String child : children){
                sortedSet.add(ROOT_LOCK+"/"+child);
            }
            String first = sortedSet.first();
            SortedSet<String> lessThenMe = ((TreeSet<String>) sortedSet).headSet(CURRENT_LOCK);
            if (CURRENT_LOCK.equals(first)){
                return true;
            }
            if (!lessThenMe.isEmpty()){
                WAIT_LOCK=lessThenMe.last();
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (this.countDownLatch!=null){
            countDownLatch.countDown();
        }
    }

    @Override
    public void lock() {
        if (this.tryLock()){
            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+",获取lock success");
            return ;
        }
        try {
            waitForLock(WAIT_LOCK);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean waitForLock(String prev) throws KeeperException, InterruptedException {
        Stat exists = zk.exists(prev, true);
        if (exists!=null){
            System.out.println(Thread.currentThread().getName()+"->等待锁"+prev+"释放");
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            System.out.println(Thread.currentThread().getName()+"获得锁成功");

        }
        return true;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

        System.out.println(Thread.currentThread().getName()+"->释放锁"+CURRENT_LOCK);
        try {
            zk.delete(ROOT_LOCK+"/"+CURRENT_LOCK,-1);
            CURRENT_LOCK=null;
            zk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
