package com.ruomm.javax.encryptx.test;

import com.ruomm.javax.encryptx.PkcsUtils;
import com.ruomm.javax.encryptx.RSAHelper;

import java.security.KeyPair;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/5/13 16:10
 */
public class PkcsTest {
    public static void main(String[] args) {
        RSAHelper rsaHelper = new RSAHelper("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING", null);
        KeyPair keyPair = rsaHelper.generateRSAKeyPair(2048);
        byte[] pubKey = keyPair.getPublic().getEncoded();
        byte[] priKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey1 = PkcsUtils.pubKeyPkcs8To1(pubKey);
        byte[] priKey1 = PkcsUtils.priKeyPkcs8To1(priKey);
        byte[] pubKey8 = PkcsUtils.pubKeyPkcs1To8(pubKey1);
        byte[] priKey8 = PkcsUtils.priKeyPkcs1To8(priKey1);
    }
}
