package com.ruomm.javax.encryptx.test;

import com.alibaba.fastjson.JSON;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.RSAHelper;
import com.ruomm.javax.encryptx.dal.KeyPairStoreFormat;
import com.ruomm.javax.encryptx.guomi.SM2Helper;
import com.ruomm.javax.encryptx.guomi.SM2KeyHelper;
import com.ruomm.javax.encryptx.guomi.SM2SignHelper;
import com.ruomm.javax.encryptx.rsasms2.CommonEncryptHelper;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/28 13:42
 */
public class CommonEncryptHeperTest {

    public static KeyPairStoreFormat SM2_KEYPAIR = null;
    private final static String DEFAULT_SM2_USER_ID = "www.ruomm.com";

    private final static String KEY_STORE_PTAH_SM2 = "D:\\temp\\smtest\\key_sm2.json";
    private final static String KEY_STORE_PTAH_RSA = "D:\\temp\\smtest\\key_rsa.json";
    private final static String KEY_STORE_DIR = "D:\\temp\\smtest\\";
    static String FILE_PATH = "D:\\temp\\smtest\\sm2_test.txt";
    static String DATA = "文本，是指书面语言的表现形式，从文学角度说，通常是具有完整、系统含义（Message）的一个句子或多个句子的组合。一个文本可以是一个句子（Sentence）、一个段落（Paragraph）或者一个篇章（Discourse）。广义“文本”：任何由书写所固定下来的任何话语。（利科尔） 狭义“文本”：由语言文字组成的文学实体，代指“作品”，相对于作者、世界构成一个独立、自足的系统。";
    static EncoderHelper.EncoderMode keyMode = EncoderHelper.EncoderMode.HEX_LOWER;
    static EncoderHelper.EncoderMode dataMode = EncoderHelper.EncoderMode.HEX_UPPER;
    static boolean isC1C3C2 = false;

    public static SM2KeyHelper sm2KeyHelper = new SM2KeyHelper(keyMode);
    public static SM2Helper sm2Helper = new SM2Helper(isC1C3C2, keyMode, dataMode);
    public static SM2SignHelper sm2SignHelper = new SM2SignHelper(keyMode, dataMode);
    public static RSAHelper rsaHelper = new RSAHelper();

    public static void main(String[] args) {
        rsaHelper.setKeyEncoderMode(keyMode);
        rsaHelper.setDataEncoderMode(dataMode);
        KeyPairStoreFormat rsaKey = loadKeyRsa(true);
        KeyPairStoreFormat sm2Key = loadKeySm2(true);
        String dataEnc = testEncryptString(rsaKey, sm2Key);
        testDecryptString(rsaKey, sm2Key, dataEnc);

        String signEnc = testSignString(rsaKey, sm2Key);
        testVerifySignString(rsaKey, sm2Key, signEnc);


    }

    private static String testEncryptString(KeyPairStoreFormat rsaKey, KeyPairStoreFormat sm2Key) {
        CommonEncryptHelper commonEncryptHelper = new CommonEncryptHelper();
        commonEncryptHelper.configPrivateKey(sm2Key.getPrivateKey(), 1);
        commonEncryptHelper.configPublicKey(rsaKey.getPublicKey(), 0);
        commonEncryptHelper.configRsaHelper(rsaHelper);
        commonEncryptHelper.configSM2Helper(sm2Helper, sm2SignHelper);
        commonEncryptHelper.configRsaWithSign(true);
        String dataEnc = commonEncryptHelper.encryptString(DATA);
        System.out.println("加密结果：" + dataEnc);
        return dataEnc;
    }

    private static void testDecryptString(KeyPairStoreFormat rsaKey, KeyPairStoreFormat sm2Key, String dataEnc) {
        CommonEncryptHelper commonEncryptHelper = new CommonEncryptHelper();
        commonEncryptHelper.configPrivateKey(rsaKey.getPrivateKey(), 0);
        commonEncryptHelper.configPublicKey(sm2Key.getPublicKey(), 1);
        commonEncryptHelper.configRsaHelper(rsaHelper);
        commonEncryptHelper.configSM2Helper(sm2Helper, sm2SignHelper);
        commonEncryptHelper.configRsaWithSign(true);
        String dataDec = commonEncryptHelper.decryptString(dataEnc);
        System.out.println("解密结果：" + dataDec);
        System.out.println("解密验证：" + DATA.equals(dataDec));
    }

    private static String testSignString(KeyPairStoreFormat rsaKey, KeyPairStoreFormat sm2Key) {
        CommonEncryptHelper commonEncryptHelper = new CommonEncryptHelper();
        commonEncryptHelper.configPrivateKey(sm2Key.getPrivateKey(), 1);
        commonEncryptHelper.configPublicKey(rsaKey.getPublicKey(), 0);
        commonEncryptHelper.configRsaHelper(rsaHelper);
        commonEncryptHelper.configSM2Helper(sm2Helper, sm2SignHelper);
        commonEncryptHelper.configRsaWithSign(true);
        String dataSign = commonEncryptHelper.signString(DATA);
        System.out.println("签名结果：" + dataSign);
        return dataSign;
    }

    private static void testVerifySignString(KeyPairStoreFormat rsaKey, KeyPairStoreFormat sm2Key, String dataSign) {
        CommonEncryptHelper commonEncryptHelper = new CommonEncryptHelper();
        commonEncryptHelper.configPrivateKey(rsaKey.getPrivateKey(), 0);
        commonEncryptHelper.configPublicKey(sm2Key.getPublicKey(), 1);
        commonEncryptHelper.configRsaHelper(rsaHelper);
        commonEncryptHelper.configSM2Helper(sm2Helper, sm2SignHelper);
        commonEncryptHelper.configRsaWithSign(true);
        boolean signResult = commonEncryptHelper.verifySignString(DATA, dataSign);
        System.out.println("签名验证：" + signResult);
    }


    public static KeyPairStoreFormat loadKeySm2(boolean force) {

        if (force) {
            KeyPairStoreFormat keyPairStoreFormat =
                    sm2KeyHelper.generateKeyPairStoreFormat();
            System.out.println("loadKeySm2->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            System.out.println("loadKeySm2->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
            FileUtils.writeFile(KEY_STORE_PTAH_SM2, JSON.toJSONString(keyPairStoreFormat), false);
            return keyPairStoreFormat;
        } else {
            String keyJSON = FileUtils.readFile(KEY_STORE_PTAH_SM2);
            if (StringUtils.isEmpty(keyJSON)) {
                return loadKeySm2(true);
            }
            KeyPairStoreFormat keyPairStoreFormat = JSON.parseObject(keyJSON, KeyPairStoreFormat.class);
            System.out.println("loadKeySm2->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            System.out.println("loadKeySm2->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
            return keyPairStoreFormat;
        }
    }

    public static KeyPairStoreFormat loadKeyRsa(boolean force) {

        if (force) {
            KeyPairStoreFormat keyPairStoreFormat =
                    rsaHelper.generateKeyPairStoreFormat();
            System.out.println("loadKeyRsa->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            System.out.println("loadKeyRsa->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
            FileUtils.writeFile(KEY_STORE_PTAH_RSA, JSON.toJSONString(keyPairStoreFormat), false);
            return keyPairStoreFormat;
        } else {
            String keyJSON = FileUtils.readFile(KEY_STORE_PTAH_RSA);
            if (StringUtils.isEmpty(keyJSON)) {
                return loadKeyRsa(true);
            }
            KeyPairStoreFormat keyPairStoreFormat = JSON.parseObject(keyJSON, KeyPairStoreFormat.class);
            System.out.println("loadKeyRsa->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            System.out.println("loadKeyRsa->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
            return keyPairStoreFormat;
        }
    }
}
