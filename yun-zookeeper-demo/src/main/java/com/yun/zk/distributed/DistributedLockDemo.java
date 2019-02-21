package com.yun.zk.distributed;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/21 14:34
 */
public class DistributedLockDemo {

    public static void main(String[] args) throws IOException {
        int count =10;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for(int i=0;i<=count;i++){
            new Thread(()->{
                try {
                    countDownLatch.await();
                    DistributedLock distributedLock = new DistributedLock();
                    distributedLock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Thread-"+i).start();
            countDownLatch.countDown();
        }
        System.in.read();
    }
}
