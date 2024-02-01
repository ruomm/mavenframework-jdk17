package com.ruomm.javax.basex;


import java.util.Set;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/11/19 17:24
 */
public class ClassUtilsTest {
    public static void main(String[] args) {
        Set<Class<?>> classSet = ClassUtils.getClassSet("com.ruomm.javax.basex");
        System.out.println(classSet);
    }
}
