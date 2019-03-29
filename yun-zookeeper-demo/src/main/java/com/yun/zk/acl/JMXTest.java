package com.yun.zk.acl;


import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/3/5 10:02
 */
public class JMXTest {


    public static void main(String[] args) {
        String hostName = "119.29.157.130";
        int portNum = 21811;
        String url = "service:jmx:rmi:///jndi/rmi://"+hostName+":"+portNum+"/jmxrmi";
        try {
            JMXServiceURL u = new JMXServiceURL(url);
            Map<String,Object> auth = new HashMap<>();
            auth.put(JMXConnector.CREDENTIALS,new String[]{"adminRole","Admin@123"});
            JMXConnector c = JMXConnectorFactory.connect(u,auth);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
