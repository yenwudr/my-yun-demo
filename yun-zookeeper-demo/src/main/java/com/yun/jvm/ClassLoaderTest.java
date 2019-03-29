package com.yun.jvm;

/**
 * @Project: zookeeperdemo
 * @Description:
 * @Author: wudr
 * @Date: 2019/3/7 17:41
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        System.out.println(new ClassLoaderTest().getClass().getClassLoader().getParent().getParent());
        System.out.println(new ClassLoaderTest().getClass().getClassLoader().getParent());
        System.out.println(new ClassLoaderTest().getClass().getClassLoader());
        System.out.println(new Object().getClass().getClassLoader());
    }
}
