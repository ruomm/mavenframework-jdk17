package com.ruomm.javax.encryptx.test;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.encryptx.guomi.SM3Utils;

import java.io.FileInputStream;

public class SM3UtilsTest {
    static String priStr = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgQRdCzUIwOwX/VyWo1AGcxnJVuOhshSrZ+/2/yebY/fGgCgYIKoEcz1UBgi2hRANCAAQACYMupY+hsggCIT6Q82DiJQJ3EmVKyHa0sfa4iAzFIJeJcRj4oCVdtggpVADt2km7OW067N+ly8sOcuY02qEf";
    static String pubStr = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEAAmDLqWPobIIAiE+kPNg4iUCdxJlSsh2tLH2uIgMxSCXiXEY+KAlXbYIKVQA7dpJuzltOuzfpcvLDnLmNNqhHw==";
    private final static boolean EXIST_VERIFY = true;
    private final static String KEY_STORE_DIR = "D:\\temp\\smtest\\";
    static String FILE_PATH = "D:\\temp\\smtest\\sm2_test.txt";
    static String DATA = "文本，是指书面语言的表现形式，从文学角度说，通常是具有完整、系统含义（Message）的一个句子或多个句子的组合。一个文本可以是一个句子（Sentence）、一个段落（Paragraph）或者一个篇章（Discourse）。广义“文本”：任何由书写所固定下来的任何话语。（利科尔） 狭义“文本”：由语言文字组成的文学实体，代指“作品”，相对于作者、世界构成一个独立、自足的系统。";


    public static void main(String[] args) {
        testSM3Data();
        testSM3File();
    }

    public static void testSM3Data() {
        System.out.println("原文:" + DATA);
        String dataEnc = SM3Utils.encodingSM3(DATA);
        System.out.println("SM3值dataEnc:" + dataEnc);
        String dataOld;
        if (EXIST_VERIFY) {
            dataOld = FileUtils.readFile(KEY_STORE_DIR + "testSM3Data.txt");
            System.out.println("SM3值dataEnc-byFile:" + dataEnc);
        } else {
            dataOld = dataEnc;
            FileUtils.writeFile(KEY_STORE_DIR + "testSM3Data.txt", dataEnc, false);
        }
        System.out.println("SM3值校验:" + dataOld.equalsIgnoreCase(dataEnc));
    }

    //    D:\temp\sm3test.txt
    public static void testSM3File() {
        try {
            System.out.println("文件路径:" + FILE_PATH);
            String dataEnc = SM3Utils.encodingInputStreamSM3(new FileInputStream(FILE_PATH));
            System.out.println("SM3值dataEnc:" + dataEnc);
            String dataOld;
            if (EXIST_VERIFY) {
                dataOld = FileUtils.readFile(KEY_STORE_DIR + "testSM3File.txt");
                System.out.println("SM3值dataEnc-byFile:" + dataEnc);
            } else {
                dataOld = dataEnc;
                FileUtils.writeFile(KEY_STORE_DIR + "testSM3File.txt", dataEnc, false);
            }
            System.out.println("SM3值校验:" + dataOld.equalsIgnoreCase(dataEnc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
