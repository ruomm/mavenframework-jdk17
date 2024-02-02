package com.ruomm.javax.encryptx.test;

import com.ruomm.javax.encryptx.RSAHelper;
import com.ruomm.javax.encryptx.dal.KeyPairStoreFormat;
import com.ruomm.javax.encryptx.rsapadding.PaddingHelper;
import com.ruomm.javax.encryptx.rsapadding.RsaPaddingHelper;
import com.ruomm.javax.encryptx.test.utils.TokenHelper;

import java.security.KeyPair;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/5/13 16:10
 */
public class RSATest {
    public static TokenHelper tokenHelper_en = new TokenHelper(TokenHelper.TOKEN_NUMBER + TokenHelper.TOKEN_LETTER_LOWER_CASE);
    public static TokenHelper tokenHelper_cn = new TokenHelper(TokenHelper.TOKEN_ZANGHUAYIN);
    public static String NO_PADDING_ERROR_STRING = "侬把】；三\n" +
            "去年翼本被与忽愿芹渠渠难天自漂沟此去代宵梁鸟易无沾花何花消闻有者身游锄花无不半菲与人逼日三能有，陷魂年落女头淖啄言，\n" +
            "虽\n" +
            "尽满知月也，污黄，。\n" +
            "落有葬鲜垒落前无？头魂去一怜朝侬绣\n" +
            "发管漂花";

    public static void main(String[] args) {
        // NO_PADDING_ERROR_STRING ="";
        // RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING 66
        // RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING 60
        // RSA/ECB/PKCS1Padding 11
        // RSA/ECB/PKCS5Padding 11
        // RSA/ECB/NOPADDING 1 0的时候可能会出现Message is larger than modulus
        // RSA/NONE/PKCS1Padding 11
        // RSA/NONE/PKCS5Padding 11
        // RSA/NONE/NOPADDING 0
//        rsaTestCommonData("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING",66,0,null);
//        rsaTestCommonByte("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING", 66, 0,null);
//        rsaTestCommonByte("RSA/ECB/NOPADDING",1,0,new RsaECBNoPaddingHelper());
//        rsaTestCommonByte("RSA/ECB/NOPADDING",1,0,new RsaECBNoPaddingHelper());
//        rsaTestCommonData("RSA", null);
//        rsaTestCommonData("RSA", new RsaPaddingHelper(11, 0, true));
        rsaTestCommonData("RSA/ECB/NOPADDING", new RsaPaddingHelper(1, 0, false, true, false));
//        rsaTestCommonData("RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING", new RsaPaddingHelper(55, 0, false,false,false));
//        rsaTestCommonData("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING", null);
    }

    public static void rsaTestCommonData(String chiper_mode, PaddingHelper paddingHelper) {
//        String DATA = tokenHelper_cn.generateToken(100);
        String DATA = NO_PADDING_ERROR_STRING;
        RSAHelper rsaHelper = new RSAHelper(chiper_mode, paddingHelper);
//        rsaHelper.setPaddingHelper(new RsaECBNoPaddingHelper());
//        rsaHelper.setPaddingHelper(paddingHelper);
//        rsaHelper.setEncodeHelper(EncoderHelper.EncoderMode.HEX_LOWER);
//        rsaHelper.setKeyEncodeHelper(EncoderHelper.EncoderMode.HEX_LOWER);
//        KeyPair keyPair = rsaHelper.generateRSAKeyPair(2048);
        KeyPairStoreFormat keyPairStoreFormat = rsaHelper.generateKeyPairStoreFormat(2048);
        System.out.println("秘钥信息：" + keyPairStoreFormat.toString());
        KeyPair keyPair = new KeyPair(rsaHelper.loadPublicKey(keyPairStoreFormat.getPublicKey()), rsaHelper.loadPrivateKey(keyPairStoreFormat.getPrivateKey()));
        String data_enc = rsaHelper.encryptDataBigByPrivateKey(DATA, keyPair.getPrivate(), "UTF-8");
        String data_dec = rsaHelper.decryptDataBigByPublicKey(data_enc, keyPair.getPublic());
        System.out.println("私钥加密：" + data_enc);
        System.out.println("公钥解密：" + data_dec);
        String data_enc_r = rsaHelper.encryptDataBigByPublicKey(DATA, keyPair.getPublic());
        String data_dec_r = rsaHelper.decryptDataBigByPrivateKey(data_enc_r, keyPair.getPrivate());
        System.out.println("公钥加密：" + data_enc_r);
        System.out.println("私钥解密：" + data_dec_r);
        String sign_result = rsaHelper.getSignData(DATA, keyPair.getPrivate());
        System.out.println("加签名结果：" + sign_result);
        boolean signVerify = rsaHelper.verifySignData(DATA, sign_result, keyPair.getPublic());
        System.out.println("验证签名结果：" + signVerify);
        System.out.println("加密结果验证1：" + DATA.equals(data_dec));
        System.out.println("加密结果验证2：" + DATA.equals(data_dec_r));
    }

    public static void rsaTestCommonByte(String chiper_mode, PaddingHelper paddingHelper) {
        byte[] dataByte = tokenHelper_en.generateToken(600).getBytes();
        RSAHelper rsaHelper = new RSAHelper(chiper_mode, paddingHelper);
//        rsaHelper.setPaddingHelper(new RsaECBNoPaddingHelper());
        rsaHelper.setPaddingHelper(paddingHelper);
//        rsaHelper.setEncodeHelper(EncoderHelper.EncoderMode.HEX_LOWER);
//        rsaHelper.setKeyEncodeHelper(EncoderHelper.EncoderMode.HEX_LOWER);
        KeyPair keyPair = rsaHelper.generateRSAKeyPair(2048);
        byte[] data_enc = rsaHelper.encryptDataBig(dataByte, keyPair.getPrivate());
        byte[] data_dec = rsaHelper.decryptDataBig(data_enc, keyPair.getPublic());
        byte[] data_enc_r = rsaHelper.encryptDataBig(dataByte, keyPair.getPublic());
        byte[] data_dec_r = rsaHelper.decryptDataBig(data_enc_r, keyPair.getPrivate());
        System.out.println("私钥加密1：" + getByteLength(data_enc));
        System.out.println("公钥解密1：" + getByteLength(data_dec));
        System.out.println("公钥加密2：" + getByteLength(data_enc_r));
        System.out.println("私钥解密2：" + getByteLength(data_dec_r));
        System.out.println("加密结果验证1：" + isByteArrayEqual(dataByte, data_dec));
        System.out.println("加密结果验证2：" + isByteArrayEqual(dataByte, data_dec_r));
    }

    private static int getByteLength(byte[] data) {
        return null == data ? 0 : data.length;
    }

    public static boolean isByteArrayEqual(byte[] dataByte1, byte[] dataByte2) {
        if (null == dataByte1 && null == dataByte2) {
            return true;
        }
        if (null == dataByte1 || null == dataByte2) {
            return false;
        }
        if (dataByte1.length != dataByte2.length) {
            return false;
        }
        boolean isEqual = true;
        int byteSize = dataByte1.length;
        for (int i = 0; i < byteSize; i++) {
            byte byte1 = dataByte1[i];
            byte byte2 = dataByte2[i];
            if (byte1 != byte2) {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }
}
