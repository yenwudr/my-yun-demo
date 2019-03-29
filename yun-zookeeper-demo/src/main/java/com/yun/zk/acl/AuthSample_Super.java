package com.yun.zk.acl;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.Date;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/2/25 17:45
 */
public class AuthSample_Super {

    final static String PATH="/aaa";

    public static void main(String[] args) throws Exception{


        ZooKeeper zooKeeper1 = new ZooKeeper("119.29.157.130:2181"
                , 4000, null);
        System.out.println(zooKeeper1.getData(PATH,false,null));

        zooKeeper1.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper1.create(PATH,"init".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.EPHEMERAL);
        ZooKeeper zooKeeper2 = new ZooKeeper("119.29.157.130:2181"
                , 4000, null);
        zooKeeper2.addAuthInfo("digest","foo:zk-book".getBytes());
        System.out.println(zooKeeper2.getData(PATH,false,null));
        ZooKeeper zooKeeper3 = new ZooKeeper("119.29.157.130:2181"
                , 4000, null);
        zooKeeper2.addAuthInfo("digest","foo:false".getBytes());
        System.out.println(zooKeeper3.getData(PATH,false,null));
        System.in.read();
    }
}
