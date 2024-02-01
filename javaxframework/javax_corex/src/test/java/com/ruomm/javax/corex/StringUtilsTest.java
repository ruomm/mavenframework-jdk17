package com.ruomm.javax.corex;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/18 9:06
 */
public class StringUtilsTest {

    public static void main(String[] args) {
        testSubString("你好中国化肥的骄傲家");
    }

    public static void testSubString(String data) {
        String tmpData = StringUtils.substring(data, 7, 15);
        System.out.println(tmpData);
    }
}
