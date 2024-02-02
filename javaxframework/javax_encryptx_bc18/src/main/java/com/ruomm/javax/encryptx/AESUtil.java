/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月7日 上午10:55:01
 */
package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.encryptx.dal.EncryptHelper;
import com.ruomm.javax.encryptx.dal.EncryptHelperInterface;
import com.ruomm.javax.encryptx.dal.KeyIvStoreFormat;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AESUtil {
    private final static Log log = LogFactory.getLog(AESUtil.class);
    /**
     * 密钥算法
     */
    private final static String KEY_ALGORITHM = "AES";
    /**
     * 加密/解密算法/工作模式/填充方式
     */
    private final static String ECB_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    private final static String CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 随机生成工作密钥
     *
     * @return 工作密钥
     */

    public static String generateRandomKey(int keyLength) {
        return generateRandomKey(keyLength, null);
    }

    public static String generateRandomKey(int keyLength, EncryptHelperInterface helper) {
        if (keyLength == 128 || keyLength == 192 || keyLength == 256) {
            return EncryptHelper.getInstance(helper).generateRandomKey(keyLength / 8);
        } else {
            return EncryptHelper.getInstance(helper).generateRandomKey(16);
        }
    }

    /**
     * 随机生成IV向量
     *
     * @return IV向量
     */
    public static String generateRandomIVSpec() {
        return generateRandomIVSpec(null);
    }

    public static String generateRandomIVSpec(EncryptHelperInterface helper) {
        return EncryptHelper.getInstance(helper).generateRandomKey(16);
    }

    public static KeyIvStoreFormat generateKeyIvStoreFormat(int keyLength, EncryptHelperInterface helper) {
        String key = generateRandomKey(keyLength, helper);
        String iv = generateRandomIVSpec(helper);
        KeyIvStoreFormat keyIv = new KeyIvStoreFormat();
        keyIv.setKey(key);
        keyIv.setIv(iv);
        return keyIv;
    }

    public static KeyIvStoreFormat generateKeyIvStoreFormat(int keyLength) {
        return generateKeyIvStoreFormat(keyLength, null);
    }

    /**
     * 还原密钥
     *
     * @param keyByte
     * @return
     */
    public static SecretKey restoreKey(byte[] keyByte) {

        SecretKey secretKey = new SecretKeySpec(keyByte, KEY_ALGORITHM);
        return secretKey;
    }

    public static Key restoreKey(String keyStr) {
        // 实例化DES密钥材料
        return restoreKey(keyStr, null);
    }

    public static Key restoreKey(String keyStr, EncryptHelperInterface helper) {
        return restoreKey(EncryptHelper.getInstance(helper).decodeToByte(keyStr));
    }

    /**
     * AES ECB加密
     *
     * @param content 待加密的内容
     * @param keyData 加密密钥
     * @return 加密后的byte[]
     */
    public static byte[] encryptECB(byte[] content, byte[] keyData) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(keyData.length * 8);
//			kgen.init(128);
            Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, restoreKey(keyData));

            return cipher.doFinal(content);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptECB", e);
            return null;
        }
    }

    /**
     * AES ECB加密为Base64Encode
     *
     * @param content 待加密的内容
     * @param keyStr  加密密钥
     * @return 加密后的Base64Encode
     */
    public static String encryptECBString(String content, String keyStr, String charsetName,
                                          EncryptHelperInterface helper) {
        if (null == content) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return EncryptHelper.getInstance(helper).encodeToString(
                    encryptECB(content.getBytes(charset), EncryptHelper.getInstance(helper).restoreRandomKey(keyStr)));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptECBString", e);
            return null;
        }
    }

    public static String encryptECBString(String content, String keyStr, String charsetName) {
        return encryptECBString(content, keyStr, charsetName, null);
    }

    public static String encryptECBString(String content, String keyStr) {
        return encryptECBString(content, keyStr, null, null);
    }

    /**
     * AES ECB解密
     *
     * @param content 待解密的byte[]
     * @param keyData 解密密钥
     * @return 解密后的byte[]
     */
    public static byte[] decryptECB(byte[] content, byte[] keyData) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(128);
            kgen.init(keyData.length * 8);
            Cipher cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, restoreKey(keyData));
            byte[] decryptBytes = cipher.doFinal(content);
            return decryptBytes;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptECB", e);
            return null;
        }
    }

    /**
     * AES ECB解密
     *
     * @param content 待解密的内容
     * @param keyStr  解密密钥
     * @return 解密后的String
     */
    public static String decryptECBString(String content, String keyStr, String charsetName,
                                          EncryptHelperInterface helper) {
        if (null == content) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptECB(EncryptHelper.getInstance(helper).decodeToByte(content),
                    EncryptHelper.getInstance(helper).restoreRandomKey(keyStr)), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptECBString", e);
            return null;
        }
    }

    public static String decryptECBString(String content, String keyStr, String charsetName) {
        return decryptECBString(content, keyStr, charsetName, null);
    }

    public static String decryptECBString(String content, String keyStr) {
        return decryptECBString(content, keyStr, null, null);
    }

    /**
     * AES CNC加密
     *
     * @param contentRaw  待加密的内容
     * @param keyData     加密密钥
     * @param ivParameter 加密IVParameter
     * @return 加密后的byte[]
     */
    public static byte[] encryptCBC(byte[] contentRaw, byte[] keyData, byte[] ivParameter) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(keyData.length * 8);
//			kgen.init(128);
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, restoreKey(keyData), ivParameterSpec);

            return cipher.doFinal(contentRaw);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptCBC", e);
            return null;
        }
    }

    /**
     * AES CBC加密为Base64Encode
     *
     * @param content        待加密的内容
     * @param keyStr         加密密钥
     * @param ivParameterStr 解密ivParameterStr
     * @return 加密后的Base64Encode
     */
    public static String encryptCBCString(String content, String keyStr, String ivParameterStr, String charsetName,
                                          EncryptHelperInterface helper) {
        if (null == content) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return EncryptHelper.getInstance(helper)
                    .encodeToString(encryptCBC(content.getBytes(charset),
                            EncryptHelper.getInstance(helper).restoreRandomKey(keyStr),
                            EncryptHelper.getInstance(helper).restoreRandomKey(ivParameterStr)));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptCBCString", e);
            return null;
        }
    }

    public static String encryptCBCString(String content, String keyStr, String ivParameterStr, String charsetName) {
        return encryptCBCString(content, keyStr, ivParameterStr, charsetName, null);
    }

    public static String encryptCBCString(String content, String keyStr, String ivParameterStr) {
        return encryptCBCString(content, keyStr, ivParameterStr, null, null);
    }

    /**
     * AES CBC解密
     *
     * @param contentRaw  待解密的byte[]
     * @param keyData     解密密钥
     * @param ivParameter 解密IVParameter
     * @return 解密后的byte[]
     */
    public static byte[] decryptCBC(byte[] contentRaw, byte[] keyData, byte[] ivParameter) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//			kgen.init(128);
            kgen.init(keyData.length * 8);
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, restoreKey(keyData), ivParameterSpec);
            byte[] decryptBytes = cipher.doFinal(contentRaw);
            return decryptBytes;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptCBC", e);
            return null;
        }
    }

    /**
     * AES CBC解密
     *
     * @param content        待解密的内容
     * @param keyStr         解密密钥
     * @param ivParameterStr 解密ivParameterStr
     * @return 解密后的String
     */
    public static String decryptCBCString(String content, String keyStr, String ivParameterStr, String charsetName,
                                          EncryptHelperInterface helper) {
        if (null == content) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptCBC(EncryptHelper.getInstance(helper).decodeToByte(content),
                    EncryptHelper.getInstance(helper).restoreRandomKey(keyStr),
                    EncryptHelper.getInstance(helper).restoreRandomKey(ivParameterStr)), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptCBCString", e);
            return null;
        }
    }

    public static String decryptCBCString(String content, String keyStr, String ivParameterStr, String charsetName) {
        return decryptCBCString(content, keyStr, ivParameterStr, charsetName, null);
    }

    public static String decryptCBCString(String content, String keyStr, String ivParameterStr) {
        return decryptCBCString(content, keyStr, ivParameterStr, null, null);
    }

    public static byte[] initKey(int keyLength) {
        byte[] dataDes = null;
        try {
            int keySize = keyLength;
            if (keyLength == 128 || keyLength == 192 || keyLength == 256) {
                keySize = keyLength;
            } else {
                keySize = 128;
            }
            /**
             * 实例化 使用128位或192位长度密钥 KeyGenerator.getInstance(KEY_ALGORITHM,"BC");
             */
            KeyGenerator kg;

            // kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //
            // /**
            // * 初始化 使用128位或192位长度密钥，按如下代码实现 kg.init(128); kg.init(192);
            // */
            // kg.init(168);
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(keySize);
            // 生成秘密密钥
            SecretKey secretKey = kg.generateKey();
            // 获得密钥的二进制编码形式
            dataDes = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            log.error("Error:initKey", e);
        }
        return dataDes;
    }

    public static String initKeyToString(int keyLength) {
        return initKeyToString(keyLength, null);
    }

    public static String initKeyToString(int keyLength, EncryptHelperInterface helper) {
        String data = EncryptHelper.getInstance(helper).encodeToString(initKey(keyLength));
        return data;
    }
}
