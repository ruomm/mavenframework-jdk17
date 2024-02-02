package com.ruomm.javax.encryptx.test;

import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.AESUtil;
import com.ruomm.javax.encryptx.dal.EncryptHelper;
import com.ruomm.javax.encryptx.test.utils.TokenHelper;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/28 2:35
 */
public class EncryptTest {
    public static TokenHelper tokenHelper_en = new TokenHelper(TokenHelper.TOKEN_NUMBER + TokenHelper.TOKEN_LETTER_LOWER_CASE);
    public static TokenHelper tokenHelper_cn = new TokenHelper(TokenHelper.TOKEN_ZANGHUAYIN);
    public static String DATA_TEST = "侬把】；三\n" +
            "去年翼本被与忽愿芹渠渠难天自漂沟此去代宵梁鸟易无沾花何花消闻有者身游锄花无不半菲与人逼日三能有，陷魂年落女头淖啄言，\n" +
            "虽\n" +
            "尽满知月也，污黄，。\n" +
            "落有葬鲜垒落前无？头魂去一怜朝侬绣\n" +
            "发管漂花";

    public static void main(String[] args) {
//        EncryptHelper encryptHelper = new EncryptHelper(EncoderHelper.EncoderMode.HEX_LOWER, EncoderHelper.EncoderMode.HEX_LOWER);
        EncryptHelper encryptHelper = new EncryptHelper(null, EncoderHelper.EncoderMode.HEX_LOWER);
        testECB(encryptHelper);
        testCBC(encryptHelper);
    }

    private static void testECB(EncryptHelper encryptHelper) {
        String key = AESUtil.generateRandomKey(192, encryptHelper);
        System.out.println("秘钥信息：" + key);
        String dataEnc = AESUtil.encryptECBString(DATA_TEST, key, null, encryptHelper);
        String dataDec = AESUtil.decryptECBString(dataEnc, key, null, encryptHelper);
        System.out.println("加密结果：" + dataEnc);
        System.out.println("解密结果：" + dataDec);
        System.out.println("校验结果：" + DATA_TEST.equals(dataDec));
    }


    private static void testCBC(EncryptHelper encryptHelper) {
        String key = AESUtil.generateRandomKey(192, encryptHelper);
        String iv = AESUtil.generateRandomIVSpec(encryptHelper);
        System.out.println("秘钥信息：" + key);
        String dataEnc = AESUtil.encryptCBCString(DATA_TEST, key, iv, null, encryptHelper);
        String dataDec = AESUtil.decryptCBCString(dataEnc, key, iv, null, encryptHelper);
        System.out.println("加密结果：" + dataEnc);
        System.out.println("解密结果：" + dataDec);
        System.out.println("校验结果：" + DATA_TEST.equals(dataDec));
    }
}
