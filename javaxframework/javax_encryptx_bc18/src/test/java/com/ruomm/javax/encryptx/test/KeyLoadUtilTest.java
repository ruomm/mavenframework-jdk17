package com.ruomm.javax.encryptx.test;

import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.encryptx.KeyLoadUtil;

import java.security.KeyPair;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/7/14 14:03
 */
public class KeyLoadUtilTest {
    public static String KEY_PWD = "qaz1234567";

    public static void main(String[] args) {
//        KeyPair keyPair = KeyLoadUtil.loadKeyPair(KEY_PATH,KEY_PWD,null,"PKCS12",null);
        String keyPath = "D:\\opt\\bdss_config\\devlocal\\d2dtestkey\\10000002_test_pri.pfx";
        String storePwd = "1234567";
        String keyPwd = "1234567";
        String keyAlias = "";
        KeyPair keyPair = KeyLoadUtil.loadKeyPairByJksP12PfxFile(keyPath, storePwd, keyPwd);
        Map<String, String> keyMap = KeyLoadUtil.loadKeyMapByJksP12PfxFile(keyPath, storePwd, keyPwd);

        System.out.println("公钥：" + Base64Utils.encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println("私钥：" + Base64Utils.encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println("公钥：" + keyMap.get(KeyLoadUtil.PUBLIC_KEY));
        System.out.println("私钥：" + keyMap.get(KeyLoadUtil.PRIVATE_KEY));
    }
}
