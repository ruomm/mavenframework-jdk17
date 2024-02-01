package com.ruomm.javax.encryptx.guomi;


import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SM2EncDecUtils {
    private final static Log log = LogFactory.getLog(SM2EncDecUtils.class);

    /**
     * 随机生成秘钥对
     *
     * @return
     */
    public static SM2KeyVO generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = null;
        while (true) {
            key = sm2.ecc_key_pair_generator.generateKeyPair();
            if (((ECPrivateKeyParameters) key.getPrivate()).getD().toByteArray().length == 32) {
                break;
            }
        }
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        SM2KeyVO sm2KeyVO = new SM2KeyVO();
        sm2KeyVO.setPublicKey(publicKey);
        sm2KeyVO.setPrivateKey(privateKey);
        if (SMConfig.isLog) {
            log.debug("generateKeyPair->公钥(Hex)：" + Util.byteToHex(publicKey.getEncoded()));
            log.debug("generateKeyPair->私钥(Hex)：" + Util.byteToHex(privateKey.toByteArray()));
            log.debug("generateKeyPair->公钥(base64)：" + Base64Utils.encodeToString(publicKey.getEncoded()));
            log.debug("generateKeyPair->私钥(base64)：" + Base64Utils.encodeToString(privateKey.toByteArray()));
        }
        return sm2KeyVO;
    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密后的数据
     * @paramDefault isC1C3C2 是否C1C3C2排列。true:C1C3C2 false:C1C2C3排列。默认false
     */
    public static byte[] encrypt(byte[] publicKey, byte[] data) {
        return encrypt(publicKey, data, false, false);
    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @param isC1C3C2  是否C1C3C2排列。true:C1C3C2 false:C1C2C3排列。默认false
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] publicKey, byte[] data, boolean isC1C3C2) {
        return encrypt(publicKey, data, isC1C3C2, false);
    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @param isC1C3C2  是否C1C3C2排列。true:C1C3C2 false:C1C2C3排列。默认false
     * @param isHard    是否硬件加密。默认false
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] publicKey, byte[] data, boolean isC1C3C2, boolean isHard) {
        if (publicKey == null || publicKey.length == 0) {
            return null;
        }

        if (data == null || data.length == 0) {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
//        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);
        ECPoint userKey = getPublicKeyECPoint(sm2.ecc_curve, publicKey);
        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);
        byte[] c1Result = c1.getEncoded();
        if (isHard) {
            byte[] c1ResultHard = new byte[c1Result.length - 1];
            System.arraycopy(c1Result, 1, c1ResultHard, 0, c1Result.length - 1);
            c1Result = c1ResultHard;
        }
        if (SMConfig.isLog) {
            log.debug("encrypt->C1(Hex)：" + Util.byteToHex(c1Result));
            log.debug("encrypt->C2(Hex)：" + Util.byteToHex(source));
            log.debug("encrypt->C3(Hex)：" + Util.byteToHex(c3));
            log.debug("encrypt->C1(base64)：" + Base64Utils.encodeToString(c1Result));
            log.debug("encrypt->C2(base64)：" + Base64Utils.encodeToString(source));
            log.debug("encrypt->C3(base64)：" + Base64Utils.encodeToString(c3));
        }

        if (isC1C3C2) {
            // C1 C3 C2拼装成加密字串
            // return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(c3) + Util.byteToHex(source);
            // C1 | C3 | C2
            byte[] encryptResult = new byte[c1Result.length + source.length + c3.length];
            System.arraycopy(c1Result, 0, encryptResult, 0, c1Result.length);
            System.arraycopy(c3, 0, encryptResult, c1Result.length, c3.length);
            System.arraycopy(source, 0, encryptResult, c1Result.length + c3.length, source.length);
            return encryptResult;
        } else {
            // C1 C2 C3拼装成加密字串
            // return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);
            // C1 | C2 | C3
            byte[] encryptResult = new byte[c1Result.length + source.length + c3.length];
            System.arraycopy(c1Result, 0, encryptResult, 0, c1Result.length);
            System.arraycopy(source, 0, encryptResult, c1Result.length, source.length);
            System.arraycopy(c3, 0, encryptResult, c1Result.length + source.length, c3.length);
            return encryptResult;
        }
    }

    /**
     * 数据解密
     *
     * @param privateKey    私钥
     * @param encryptedData 加密的数据
     * @return 解密后的数据
     * @paramDefault isC1C3C2 是否C1C3C2排列。true:C1C3C2 false:C1C2C3排列。默认false
     */
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) {
        return decrypt(privateKey, encryptedData, false);
    }

    /**
     * 数据解密
     *
     * @param privateKey    私钥
     * @param encryptedData 加密的数据
     * @param isC1C3C2      是否C1C3C2排列。true:C1C3C2 false:C1C2C3排列。默认false
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData, boolean isC1C3C2) {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }

        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Util.byteToHex(encryptedData);
        int c2Len = encryptedData.length - 97;
//        byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
//        byte[] c3 = Util.hexToByte(data.substring(130,130 + 64));
//        byte[] c2 = Util.hexToByte(data.substring(194,194 + 2 * c2Len));
//        byte[] c3 = null;
//        byte[] c2 = null;
//        if(isC1C3C2)
//        {
//            c3 = Util.hexToByte(data.substring(130,194));
//            c2 = Util.hexToByte(data.substring(194,194 + 2 * c2Len));
//        }
//        else{
//            c2 = Util.hexToByte(data.substring(130,130 + 2 * c2Len));
//            c3 = Util.hexToByte(data.substring(130 + 2 * c2Len,194 + 2 * c2Len));
//        }
        String c1Str = null;
        String c2Str = null;
        String c3Str = null;
        c1Str = data.substring(0, 130);
        if (isC1C3C2) {
            c3Str = data.substring(130, 194);
            c2Str = data.substring(194, 194 + 2 * c2Len);
        } else {
            c2Str = data.substring(130, 130 + 2 * c2Len);
            c3Str = data.substring(130 + 2 * c2Len, 194 + 2 * c2Len);
        }
        if (SMConfig.isLog) {
            log.debug("decrypt->C1(Hex)：" + c1Str);
            log.debug("decrypt->C2(Hex)：" + c2Str);
            log.debug("decrypt->C3(Hex)：" + c3Str);
        }
        byte[] c1Bytes = Util.hexToByte(c1Str);
        byte[] c2 = Util.hexToByte(c2Str);
        byte[] c3 = Util.hexToByte(c3Str);

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);
        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);
        //返回解密结果
        return c2;
    }

    private static ECPoint getPublicKeyECPoint(ECCurve ecc_curve, byte[] publicKey) {
        if (publicKey.length == 68) {
            int length = 32;
            byte[] x = new byte[length];
            byte[] y = new byte[length];
            System.arraycopy(publicKey, 4, x, 0, length);
            System.arraycopy(publicKey, 36, y, 0, length);
            return ecc_curve.createPoint(new BigInteger(1, x), new BigInteger(1, y), false);
        } else if (publicKey.length == 64) {
            int length = 32;
            byte[] x = new byte[length];
            byte[] y = new byte[length];
            System.arraycopy(publicKey, 0, x, 0, length);
            System.arraycopy(publicKey, 32, y, 0, length);
            return ecc_curve.createPoint(new BigInteger(1, x), new BigInteger(1, y), false);
        } else {
            return ecc_curve.decodePoint(publicKey);
        }
    }
}