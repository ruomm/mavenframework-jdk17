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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;
import java.security.Key;

/**
 * 3DES加密
 *
 * @author
 * @version 1.0
 */
public abstract class DesUtil192 {
    private final static Log log = LogFactory.getLog(DesUtil192.class);

    static {
        BCProviderLoadUtil.loadProvider();
    }

    /**
     * 密钥算法
     */
    public final static String KEY_ALGORITHM = "DESede";
    /**
     * 加密/解密算法/工作模式/填充方式
     */
    private final static String ECB_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";
    private final static String CBC_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
    private final static String CIPHER_NAMESPACE = "BC";

    /**
     * 随机生成工作密钥
     *
     * @return 工作密钥
     */
    public static String generateRandomKey() {
        return generateRandomKey(null);
    }

    public static String generateRandomKey(EncryptHelperInterface helper) {
        return EncryptHelper.getInstance(helper).generateRandomKey(32);
    }

    /**
     * 随机生成IV向量
     *
     * @return IV向量
     */
    public static String generateRandomIVSpec(EncryptHelperInterface helper) {
        return EncryptHelper.getInstance(helper).generateRandomKey(8);
    }

    public static String generateRandomIVSpec() {
        return generateRandomIVSpec(null);
    }

    public static KeyIvStoreFormat generateKeyIvStoreFormat(EncryptHelperInterface helper) {
        String key = generateRandomKey(helper);
        String iv = generateRandomIVSpec(helper);
        KeyIvStoreFormat keyIv = new KeyIvStoreFormat();
        keyIv.setKey(key);
        keyIv.setIv(iv);
        return keyIv;
    }

    public static KeyIvStoreFormat generateKeyIvStoreFormat() {
        return generateKeyIvStoreFormat(null);
    }

    /**
     * 还原密钥
     *
     * @param keyByte
     * @return
     */
    public static Key restoreKey(byte[] keyByte) {
        // 实例化DES密钥材料
        Key keyReal = null;
        try {
            DESedeKeySpec dks;
            dks = new DESedeKeySpec(keyByte);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM, CIPHER_NAMESPACE);
            // 生成秘密密钥
            keyReal = keyFactory.generateSecret(dks);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:restoreKey", e);
        }
        return keyReal;

    }

    public static Key restoreKey(String keyStr) {
        // 实例化DES密钥材料
        return restoreKey(keyStr, null);
    }

    public static Key restoreKey(String keyStr, EncryptHelperInterface helper) {
        return restoreKey(EncryptHelper.getInstance(helper).decodeToByte(keyStr));
    }

    /**
     * DES ECB加密
     *
     * @param content 待加密的内容
     * @param keyData 加密密钥
     * @return 加密后的byte[]
     */
    public static byte[] encryptECB(byte[] content, byte[] keyData) {
        byte[] dataDes = null;
        try {
            // 还原密钥
            Key k = restoreKey(keyData);
            /**
             * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
             */
            Cipher cipher;
            cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM, CIPHER_NAMESPACE);
            // 初始化，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, k);
            // 执行操作
            dataDes = cipher.doFinal(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:encryptECB", e);
        }
        return dataDes;
    }

    /**
     * DES ECB加密为Base64Encode
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
     * DES ECB解密
     *
     * @param content 待解密的byte[]
     * @param keyData 解密密钥
     * @return 解密后的byte[]
     */
    public static byte[] decryptECB(byte[] content, byte[] keyData) {
        byte[] dataDes = null;
        try {
            // 还原密钥
            Key k = restoreKey(keyData);
            /**
             * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
             */
            Cipher cipher;
            cipher = Cipher.getInstance(ECB_CIPHER_ALGORITHM, CIPHER_NAMESPACE);
            // 初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, k);
            // 执行操作
            dataDes = cipher.doFinal(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:decryptECB", e);
        }
        return dataDes;

    }

    /**
     * DES ECB解密
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
     * DES CBC加密
     *
     * @param content     待加密的内容
     * @param keyData     加密密钥
     * @param ivParameter 加密IVParameter
     * @return 加密后的byte[]
     */
    public static byte[] encryptCBC(byte[] content, byte[] keyData, byte[] ivParameter) {
        byte[] dataDes = null;
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter);
            // 还原密钥
            Key k = restoreKey(keyData);
            /**
             * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
             */
            Cipher cipher;
            cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, CIPHER_NAMESPACE);
            // 初始化，设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, k, ivParameterSpec);
            // 执行操作
            dataDes = cipher.doFinal(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:encryptCBC", e);
        }
        return dataDes;
    }

    /**
     * DES ECB加密为Base64Encode
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
     * DES CBC解密
     *
     * @param content     待解密的byte[]
     * @param keyData     解密密钥
     * @param ivParameter 解密IVParameter
     * @return 解密后的byte[]
     */
    public static byte[] decryptCBC(byte[] content, byte[] keyData, byte[] ivParameter) {
        byte[] dataDes = null;
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter);
            // 还原密钥
            Key k = restoreKey(keyData);
            /**
             * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
             */
            Cipher cipher;
            cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM, CIPHER_NAMESPACE);
            // 初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, k, ivParameterSpec);
            // 执行操作
            dataDes = cipher.doFinal(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:decryptCBC", e);
        }
        return dataDes;

    }

    /**
     * DES CBC解密
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

    /**
     * 生成密钥
     *
     * @return byte[] 二进制密钥
     */
    public static byte[] initKey() {
        byte[] dataDes = null;
        try {
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
            kg = KeyGenerator.getInstance(KEY_ALGORITHM, CIPHER_NAMESPACE);
            kg.init(192);
            // 生成秘密密钥
            SecretKey secretKey = kg.generateKey();
            // 获得密钥的二进制编码形式
            dataDes = secretKey.getEncoded();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:initKey", e);
        }
        return dataDes;
    }

    public static String initKeyToString() {
        return initKeyToString(null);
    }

    public static String initKeyToString(EncryptHelperInterface helper) {
        String data = EncryptHelper.getInstance(helper).encodeToString(initKey());
        return data;
    }
}
