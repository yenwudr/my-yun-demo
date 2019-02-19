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
public class WatcherDemo2 {

    public static void main(String[] args) {
       int x=1,y=1;
       int count =0;
       while (x<=40){
           x=x+y;
           y=x+y;
           System.out.println(x+"   "+y);
           count++;
       }
        System.out.println(count);
    }
}
