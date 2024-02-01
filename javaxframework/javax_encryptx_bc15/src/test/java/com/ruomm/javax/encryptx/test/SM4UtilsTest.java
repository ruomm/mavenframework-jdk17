package com.ruomm.javax.encryptx.test;

import com.alibaba.fastjson.JSON;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.dal.EncryptHelper;
import com.ruomm.javax.encryptx.dal.KeyIvStoreFormat;
import com.ruomm.javax.encryptx.guomi.SM4Utils;

public class SM4UtilsTest {

    private final static String KEY_STORE_PTAH = "D:\\temp\\smtest\\key_sm4_keyiv.txt";
    static EncoderHelper.EncoderMode keyMode = EncoderHelper.EncoderMode.BASE64;
    static EncoderHelper.EncoderMode dataMode = EncoderHelper.EncoderMode.BASE64;
    static EncryptHelper encryptHelper = new EncryptHelper(keyMode, dataMode);

    static KeyIvStoreFormat KEY_IV;
    private final static boolean EXIST_VERIFY = false;
    private final static String KEY_STORE_DIR = "D:\\temp\\smtest\\";
    static String FILE_PATH = "D:\\temp\\smtest\\sm2_test.txt";
    static String DATA = "文本，是指书面语言的表现形式，从文学角度说，通常是具有完整、系统含义（Message）的一个句子或多个句子的组合。一个文本可以是一个句子（Sentence）、一个段落（Paragraph）或者一个篇章（Discourse）。广义“文本”：任何由书写所固定下来的任何话语。（利科尔） 狭义“文本”：由语言文字组成的文学实体，代指“作品”，相对于作者、世界构成一个独立、自足的系统。";

    public static void main(String[] args) {
        KEY_IV = generateKeyIvStoreFormat(false);
        testSM4ECB();
        testSM4CBC();
    }

    public static KeyIvStoreFormat generateKeyIvStoreFormat(boolean force) {

        if (force) {
            KeyIvStoreFormat keyIvStoreFormat =
                    SM4Utils.generateKeyIvStoreFormat(encryptHelper);
            System.out.println("generateKeyIvStoreFormat->" + "KYE: " + keyIvStoreFormat.getKey());
            System.out.println("generateKeyIvStoreFormat->" + "IV: " + keyIvStoreFormat.getIv());
            FileUtils.writeFile(KEY_STORE_PTAH, JSON.toJSONString(keyIvStoreFormat), false);
            return keyIvStoreFormat;
        } else {
            String keyJSON = FileUtils.readFile(KEY_STORE_PTAH);
            if (StringUtils.isEmpty(keyJSON)) {
                return generateKeyIvStoreFormat(true);
            }
            KeyIvStoreFormat keyIvStoreFormat = JSON.parseObject(keyJSON, KeyIvStoreFormat.class);
            System.out.println("generateKeyIvStoreFormat->" + "KYE: " + keyIvStoreFormat.getKey());
            System.out.println("generateKeyIvStoreFormat->" + "IV: " + keyIvStoreFormat.getIv());
            return keyIvStoreFormat;
        }
    }

    public static void testSM4ECB() {

        String key = KEY_IV.getKey();
        String iv = KEY_IV.getIv();
        System.out.println("原文:" + DATA);
        System.out.println("key:" + key);
        System.out.println("iv:" + iv);
        String dataEnc = SM4Utils.encryptECBString(DATA, key, null, encryptHelper);
        System.out.println("加密dataEnc:" + dataEnc);
        if (EXIST_VERIFY) {
            dataEnc = FileUtils.readFile(KEY_STORE_DIR + "testSM4ECB.txt");
            System.out.println("加密dataEnc-byFile:" + dataEnc);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM4ECB.txt", dataEnc, false);
        }
        String dataDec = SM4Utils.decryptECBString(dataEnc, key, null, encryptHelper);
        System.out.println("解密:" + dataDec);
        System.out.println("加密验证结果ECB：" + dataDec.equals(DATA));
    }

    public static void testSM4CBC() {
        String key = KEY_IV.getKey();
        String iv = KEY_IV.getIv();
        System.out.println("原文:" + DATA);
        System.out.println("key:" + key);
        System.out.println("iv:" + iv);
        String dataEnc = SM4Utils.encryptCBCString(DATA, key, iv, null, encryptHelper);
        System.out.println("加密dataEnc:" + dataEnc);
        if (EXIST_VERIFY) {
            dataEnc = FileUtils.readFile(KEY_STORE_DIR + "testSM4CBC.txt");
            System.out.println("加密dataEnc-byFile:" + dataEnc);
        } else {
            FileUtils.writeFile(KEY_STORE_DIR + "testSM4CBC.txt", dataEnc, false);
        }
        String dataDec = SM4Utils.decryptCBCString(dataEnc, key, iv, null, encryptHelper);
        System.out.println("解密dataDec:" + dataDec);
        System.out.println("加密验证结果CBC：" + dataDec.equals(DATA));
    }
}
