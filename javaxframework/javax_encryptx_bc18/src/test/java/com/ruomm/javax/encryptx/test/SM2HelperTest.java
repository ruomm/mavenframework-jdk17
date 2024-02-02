package com.ruomm.javax.encryptx.test;

import com.alibaba.fastjson.JSON;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.dal.KeyPairStoreFormat;
import com.ruomm.javax.encryptx.guomi.SM2Helper;
import com.ruomm.javax.encryptx.guomi.SM2KeyHelper;
import com.ruomm.javax.encryptx.guomi.SM2SignDiagestHelper;
import com.ruomm.javax.encryptx.guomi.SM2SignHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SM2HelperTest {
    public static KeyPairStoreFormat SM2_KEYPAIR = null;
    private final static String DEFAULT_SM2_USER_ID = "www.ruomm.com";

    private final static String KEY_STORE_PTAH = "D:\\temp\\smtest\\sm2_key.json";
    private final static String KEY_STORE_DIR = "D:\\temp\\smtest\\";
    static String FILE_PATH = "D:\\temp\\smtest\\sm2_test.txt";
    static String DATA = "文本，是指书面语言的表现形式，从文学角度说，通常是具有完整、系统含义（Message）的一个句子或多个句子的组合。一个文本可以是一个句子（Sentence）、一个段落（Paragraph）或者一个篇章（Discourse）。广义“文本”：任何由书写所固定下来的任何话语。（利科尔） 狭义“文本”：由语言文字组成的文学实体，代指“作品”，相对于作者、世界构成一个独立、自足的系统。";

    static {
        SM2_KEYPAIR = new KeyPairStoreFormat();
        boolean isZhifuBao = false;
        if (!isZhifuBao) {
            SM2_KEYPAIR.setPrivateKey("UY4R7o3b470Ebd6uCROgui1puccEIK6z7xhAE0U5VXQ=");
            SM2_KEYPAIR.setPublicKey("BGANgYSF6dEBsgTsoOARQO1yIbRQZ4zadXRBul/u1FP4Z1fI3Wft0yUyOTUpKEHlW7xa1Olww/U2cuI2hCdiggg=");
        } else {
            SM2_KEYPAIR.setPrivateKey("MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgBkqqEuT9eheHMPqxtphHUC+jmToJtkl4jrmgdoUWL52gCgYIKoEcz1UBgi2hRANCAARacuVBihzTxPHSGkCXLtCjbtPlDzm+o2i1GxJxChRbI6KmcofLu6Tv7Td3lyTeY84YKSoPqRQHE80BFRRQbylb");
            SM2_KEYPAIR.setPublicKey("MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEWnLlQYoc08Tx0hpAly7Qo27T5Q85vqNotRsScQoUWyOipnKHy7uk7+03d5ck3mPOGCkqD6kUBxPNARUUUG8pWw==");
        }
        System.out.println("国密公钥->" + SM2_KEYPAIR.getPublicKey());
        System.out.println("国密私钥->" + SM2_KEYPAIR.getPrivateKey());
    }

    private final static boolean EXIST_VERIFY = true;
    static EncoderHelper.EncoderMode keyMode = EncoderHelper.EncoderMode.BASE64;
    static EncoderHelper.EncoderMode dataMode = EncoderHelper.EncoderMode.BASE64;
    static boolean isC1C3C2 = false;

    public static SM2KeyHelper sm2KeyHelper = new SM2KeyHelper(keyMode);
    public static SM2Helper sm2Helper = new SM2Helper(isC1C3C2, keyMode, dataMode);
    public static SM2SignDiagestHelper sm2SignDiagestHelper = new SM2SignDiagestHelper(keyMode, dataMode);
    public static SM2SignHelper sm2SignHelper = new SM2SignHelper(keyMode, dataMode);


    public static void main(String[] args) {
        SM2_KEYPAIR = generateKeyPairForStringFormat(false);
        try {
            testSM2();
            testSM2VerifyString();
            testSM2VerifyFile();
            testSM2VerifyStringNew();
            testSM2VerifyFileNew();
            testSM2VerifyStringDiagest();
            testSM2VerifyFileDiagest();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static KeyPairStoreFormat generateKeyPairForStringFormat(boolean force) {

        if (force) {
            KeyPairStoreFormat keyPairStoreFormat =
                    sm2KeyHelper.generateKeyPairStoreFormat();
            System.out.println("generateKeyPairForStringFormat->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            System.out.println("generateKeyPairForStringFormat->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
            FileUtils.writeFile(KEY_STORE_PTAH, JSON.toJSONString(keyPairStoreFormat), false);
            return keyPairStoreFormat;
        } else {
            String keyJSON = FileUtils.readFile(KEY_STORE_PTAH);
            if (StringUtils.isEmpty(keyJSON)) {
                return generateKeyPairForStringFormat(true);
            }
            KeyPairStoreFormat keyPairStoreFormat = JSON.parseObject(keyJSON, KeyPairStoreFormat.class);
            System.out.println("generateKeyPairForStringFormat->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            System.out.println("generateKeyPairForStringFormat->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
            return keyPairStoreFormat;
        }
    }

    public static void testSM2() {
        System.out.println("原文Data:" + DATA);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;
        String dataEnc = sm2Helper.encryptDataByPublicKey(DATA, sm2KeyPair.getPublicKey());
        System.out.println("加密dataEnc:" + dataEnc);
        if (EXIST_VERIFY) {
            dataEnc = FileUtils.readFile(KEY_STORE_DIR + "testSM2.txt");
            System.out.println("加密dataEnc-byFile:" + dataEnc);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2.txt", dataEnc, false);
        }
        String dataDec = sm2Helper.decryptDataByPrivateKey(dataEnc, sm2KeyPair.getPrivateKey());
        System.out.println("解密dataDec:" + dataDec);
        System.out.println("加密验证结果：" + dataDec.equals(DATA));
    }

    public static void testSM2VerifyString() {
        System.out.println("原文Data:" + DATA);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;
        String dataSign = sm2SignHelper.signToStringSoft(DEFAULT_SM2_USER_ID, DATA, sm2KeyPair.getPrivateKey());
        System.out.println("签名dataSign:" + dataSign);
        if (EXIST_VERIFY) {
            dataSign = FileUtils.readFile(KEY_STORE_DIR + "testSM2VerifyString.txt");
            System.out.println("签名dataSign-byFile:" + dataSign);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2VerifyString.txt", dataSign, false);
        }
        boolean dataDec = sm2SignHelper.verifyByStringSoft(DEFAULT_SM2_USER_ID, DATA, dataSign, sm2KeyPair.getPublicKey());
        System.out.println("验签:" + dataDec);
    }

    public static void testSM2VerifyFile() throws FileNotFoundException {
        System.out.println("源文件FILE_PATH:" + FILE_PATH);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;
        String dataSign = sm2SignHelper.signToStringSoft(DEFAULT_SM2_USER_ID, new FileInputStream(FILE_PATH), sm2KeyPair.getPrivateKey());
        System.out.println("签名dataSign:" + dataSign);
        if (EXIST_VERIFY) {
            dataSign = FileUtils.readFile(KEY_STORE_DIR + "testSM2VerifyFile.txt");
            System.out.println("签名dataSign-byFile:" + dataSign);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2VerifyFile.txt", dataSign, false);
        }
        boolean signVerify = sm2SignHelper.verifyByStringSoft(DEFAULT_SM2_USER_ID, new FileInputStream(FILE_PATH), dataSign, sm2KeyPair.getPublicKey());
        System.out.println("验签signVerify:" + signVerify);
    }

    public static void testSM2VerifyStringNew() {
        System.out.println("原文Data:" + DATA);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;
        String dataSign = sm2SignHelper.signToStringHard(DEFAULT_SM2_USER_ID, DATA, sm2KeyPair.getPrivateKey());
        System.out.println("签名dataSign:" + dataSign);
        if (EXIST_VERIFY) {
            dataSign = FileUtils.readFile(KEY_STORE_DIR + "testSM2VerifyStringNew.txt");
            System.out.println("签名dataSign-byFile:" + dataSign);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2VerifyStringNew.txt", dataSign, false);
        }
        boolean dataDec = sm2SignHelper.verifyByStringHard(DEFAULT_SM2_USER_ID, DATA, dataSign, sm2KeyPair.getPublicKey());
        System.out.println("验签:" + dataDec);
    }

    public static void testSM2VerifyFileNew() throws FileNotFoundException {
        System.out.println("源文件FILE_PATH:" + FILE_PATH);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;
        String dataSign = sm2SignHelper.signToStringHard(DEFAULT_SM2_USER_ID, new FileInputStream(FILE_PATH), sm2KeyPair.getPrivateKey());
        System.out.println("签名dataSign:" + dataSign);
        if (EXIST_VERIFY) {
            dataSign = FileUtils.readFile(KEY_STORE_DIR + "testSM2VerifyFileNew.txt");
            System.out.println("签名dataSign-byFile:" + dataSign);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2VerifyFileNew.txt", dataSign, false);
        }
        boolean signVerify = sm2SignHelper.verifyByStringHard(DEFAULT_SM2_USER_ID, new FileInputStream(FILE_PATH), dataSign, sm2KeyPair.getPublicKey());
        System.out.println("验签signVerify:" + signVerify);
    }

    public static void testSM2VerifyStringDiagest() {
        System.out.println("原文Data:" + DATA);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;

        String sm3Diagest = HexUtils.encodeHexStr(sm2SignDiagestHelper.toSm3ByPrivateKey(DEFAULT_SM2_USER_ID.getBytes(), DATA.getBytes(), sm2KeyPair.getPrivateKey()));
        System.out.println("toSm3ByPrivateKey结果:" + sm3Diagest);
        String dataSign = sm2SignDiagestHelper.signToStringHard(sm3Diagest, sm2KeyPair.getPrivateKey());
        System.out.println("签名dataSign:" + dataSign);
        if (EXIST_VERIFY) {
            dataSign = FileUtils.readFile(KEY_STORE_DIR + "testSM2VerifyStringDiagest.txt");
            System.out.println("签名dataSign-byFile:" + dataSign);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2VerifyStringDiagest.txt", dataSign, false);
        }
        boolean dataDec = sm2SignHelper.verifyByStringHard(DEFAULT_SM2_USER_ID, DATA, dataSign, sm2KeyPair.getPublicKey());
        System.out.println("验签:" + dataDec);
    }

    public static void testSM2VerifyFileDiagest() throws FileNotFoundException {
        System.out.println("源文件FILE_PATH:" + FILE_PATH);
        KeyPairStoreFormat sm2KeyPair = SM2_KEYPAIR;
        String sm3Diagest = HexUtils.encodeHexStr(sm2SignDiagestHelper.toSm3ByPrivateKey(DEFAULT_SM2_USER_ID.getBytes(), new FileInputStream(FILE_PATH), sm2KeyPair.getPrivateKey()));
        System.out.println("toSm3ByPrivateKey结果:" + sm3Diagest);
        String dataSign = sm2SignDiagestHelper.signToStringSoft(sm3Diagest, sm2KeyPair.getPrivateKey());
        System.out.println("签名dataSign:" + dataSign);
        if (EXIST_VERIFY) {
            dataSign = FileUtils.readFile(KEY_STORE_DIR + "testSM2VerifyFileDiagest.txt");
            System.out.println("签名dataSign-byFile:" + dataSign);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM2VerifyFileDiagest.txt", dataSign, false);
        }
        boolean signVerify = sm2SignDiagestHelper.verifyByStringSoft(sm3Diagest, dataSign, sm2KeyPair.getPublicKey());
        System.out.println("验签signVerify:" + signVerify);
    }
}
