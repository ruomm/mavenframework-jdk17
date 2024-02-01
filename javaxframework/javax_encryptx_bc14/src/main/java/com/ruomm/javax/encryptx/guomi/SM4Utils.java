/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月6日 上午11:02:56
 */
package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.encryptx.dal.EncryptHelper;
import com.ruomm.javax.encryptx.dal.EncryptHelperInterface;
import com.ruomm.javax.encryptx.dal.KeyIvStoreFormat;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Utils {
    private final static Log log = LogFactory.getLog(SM4Utils.class);

    /**
     * 随机生成工作密钥
     *
     * @return 工作密钥
     */

    public static String generateRandomKey(EncryptHelperInterface helper) {
        return EncryptHelper.getInstance(helper).generateRandomKey(16);
    }

    public static String generateRandomKey() {
        return generateRandomKey(null);
    }

    /**
     * 随机生成IV向量
     *
     * @return IV向量
     */
    public static String generateRandomIVSpec(EncryptHelperInterface helper) {
        return EncryptHelper.getInstance(helper).generateRandomKey(16);
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
     * @param keyStr
     * @return
     */

    public static byte[] restoreKey(String keyStr) {
        // 实例化DES密钥材料
        return restoreKey(keyStr, null);
    }

    public static byte[] restoreKey(String keyStr, EncryptHelperInterface helper) {
        return EncryptHelper.getInstance(helper).decodeToByte(keyStr);
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
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyData);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, content);
            return encrypted;
        } catch (Exception e) {
            log.error("Error:encryptECB", e);
            return null;
        }
    }

    public static String encryptECBString(String content, String keyStr, String charsetName,
                                          EncryptHelperInterface helper) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes = EncryptHelper.getInstance(helper).restoreRandomKey(keyStr);
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, content.getBytes(charset));
            String cipherText = EncryptHelper.getInstance(helper).encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
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
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyData);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, content);
            return decrypted;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptECB", e);
            return null;
        }
    }

    public static String decryptECBString(String content, String keyStr, String charsetName,
                                          EncryptHelperInterface helper) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes = EncryptHelper.getInstance(helper).restoreRandomKey(keyStr);

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, EncryptHelper.getInstance(helper).decodeToByte(content));
            return new String(decrypted, charset);
        } catch (Exception e) {
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
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyData);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivParameter, contentRaw);
            return encrypted;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptCBC", e);
            return null;
        }
    }

    public static String encryptCBCString(String content, String keyStr, String ivParameterStr, String charsetName,
                                          EncryptHelperInterface helper) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes = EncryptHelper.getInstance(helper).restoreRandomKey(keyStr);
            byte[] ivBytes = EncryptHelper.getInstance(helper).restoreRandomKey(ivParameterStr);
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, content.getBytes(charset));
            String cipherText = EncryptHelper.getInstance(helper).encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
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
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyData);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivParameter, contentRaw);
            return decrypted;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptCBC", e);
            return null;
        }
    }

    public static String decryptCBCString(String content, String keyStr, String ivParameterStr, String charsetName,
                                          EncryptHelperInterface helper) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes = EncryptHelper.getInstance(helper).restoreRandomKey(keyStr);
            byte[] ivBytes = EncryptHelper.getInstance(helper).restoreRandomKey(ivParameterStr);

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, EncryptHelper.getInstance(helper).decodeToByte(content));
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decrypted, charset);
        } catch (Exception e) {
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

    public static byte[] initKey() {
        int randomKeySize = 16;
        Random random = new Random();
        byte[] keyData = new byte[randomKeySize];
        for (int i = 0; i < randomKeySize; i++) {
            keyData[i] = (byte) (random.nextInt(256) - 128);
        }
        return keyData;
    }

    public static String initKeyToString() {
        return initKeyToString(null);
    }

    public static String initKeyToString(EncryptHelperInterface helper) {
        String data = EncryptHelper.getInstance(helper).encodeToString(initKey());
        return data;
    }
}
