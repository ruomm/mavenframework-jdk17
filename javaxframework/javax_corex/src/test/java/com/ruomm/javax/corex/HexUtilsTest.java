package com.ruomm.javax.corex;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/30 16:55
 */
public class HexUtilsTest {

    public static void main(String[] args) {
        byte[] byteShort = HexUtils.shortToBytes((short) -25678);
        System.out.println(HexUtils.bytesToShort(byteShort));
        byte[] byteInt = HexUtils.intToBytes(-456465654);
        System.out.println(HexUtils.bytesToInt(byteInt));
        byte[] byteLong = HexUtils.longToBytes(-4564656544654656l);
        System.out.println(HexUtils.bytesToLong(byteLong));
    }

}
