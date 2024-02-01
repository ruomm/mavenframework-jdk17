package com.ruomm.javax.basex;

import com.ruomm.javax.basex.dal.SrcCopy;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/29 15:48
 */
public class ObjectEncryptTest {
    public static void main(String[] args) {
        testEncrypt();
    }

    public static void testEncrypt() {
        SrcCopy srcCopy = generateSrcCopy();
        System.out.println("原始:" + srcCopy);
        ObjectEncryptUtils.encryptObj(srcCopy, objectEncryptHelper);
        System.out.println("加密:" + srcCopy);
        ObjectEncryptUtils.decryptObj(srcCopy, objectEncryptHelper);
        System.out.println("解密:" + srcCopy);

    }

    private static ObjectEncryptUtils.ObjectEncryptHelper objectEncryptHelper = new ObjectEncryptUtils.ObjectEncryptHelper() {
        @Override
        public String encrypt(String str) {
            if (null == str) {
                return null;
            }
            return "加密" + str;
        }

        @Override
        public String decrypt(String str) {
            if (null == str) {
                return null;
            }
            return str.substring(2);
        }
    };

    public static SrcCopy generateSrcCopy() {
        SrcCopy srcCopy = new SrcCopy();
        srcCopy.setAge(12);
        srcCopy.setId("1000");
        srcCopy.setName("张三");
        srcCopy.setP_sex("男");
        srcCopy.setP_cardNo("4103271989");
        srcCopy.setP_name("李四");
        srcCopy.setP_data("nihao");
        srcCopy.setP_age(36);
        return srcCopy;

    }
}
