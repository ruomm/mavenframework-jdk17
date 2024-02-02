package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.dal.KeyPairStoreFormat;
import com.ruomm.javax.encryptx.rsapadding.PaddingHelper;
import com.ruomm.javax.encryptx.rsapadding.RsaPaddingHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

/**
 * @author Mr.Zheng
 * @date 2014年8月22日 下午1:44:23
 */
public final class RSAHelper {
    private final static Log log = LogFactory.getLog(RSAHelper.class);

    static {
        BCProviderLoadUtil.loadProvider();
    }

    private final static String RSA = "RSA";
    private final static int BUFFER_SIZE = 1024;
    private String chiper_mode = null;
    private PaddingHelper paddingHelper = null;
    private EncoderHelper dataEncoderHelper;
    private EncoderHelper keyEncoderHeper;
    private static RSAHelper rsaHelper = new RSAHelper();

    public static RSAHelper getInstance() {
        return rsaHelper;
    }

    public RSAHelper() {
        this("RSA", null);
    }

    public RSAHelper(String chiper_mode) {
        this(chiper_mode, null);
    }

    public RSAHelper(String chiper_mode, PaddingHelper paddingHelper) {
        super();
        if (null != chiper_mode && chiper_mode.length() > 0) {
            this.chiper_mode = chiper_mode;
        } else {
            this.chiper_mode = "RSA";
        }
        this.dataEncoderHelper = new EncoderHelper(EncoderHelper.EncoderMode.BASE64);
        this.keyEncoderHeper = new EncoderHelper(EncoderHelper.EncoderMode.BASE64);
        setPaddingHelper(paddingHelper);
    }

    public void setKeyEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.keyEncoderHeper = new EncoderHelper(encoderMode);
    }

    public void setDataEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.dataEncoderHelper = new EncoderHelper(encoderMode);
    }

    public void setPaddingHelper(PaddingHelper paddingHelper) {
        if (null == paddingHelper) {
            // RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING 66
            // RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING 60
            // RSA/ECB/PKCS1Padding 11
            // RSA/ECB/PKCS5Padding 11
            // RSA/ECB/NOPADDING 1 0的时候可能会出现Message is larger than modulus
            // RSA/NONE/PKCS1Padding 11
            // RSA/NONE/PKCS5Padding 11
            // RSA/NONE/NOPADDING 0
            String chiper_mode_upper = this.chiper_mode.toUpperCase();
            if (chiper_mode_upper.contains("ECB") && chiper_mode_upper.contains("OAEPWITHSHA-256ANDMGF1PADDING")) {
                this.paddingHelper = new RsaPaddingHelper(66, 0);
            } else if (chiper_mode_upper.contains("ECB") && chiper_mode_upper.contains("OAEPWITHSHA-1ANDMGF1PADDING")) {
                this.paddingHelper = new RsaPaddingHelper(60, 0);
            } else if (chiper_mode_upper.contains("ECB") && chiper_mode_upper.contains("PKCS1PADDING")) {
                this.paddingHelper = new RsaPaddingHelper(11, 0);
            } else if (chiper_mode_upper.contains("ECB") && chiper_mode_upper.contains("PKCS5PADDING")) {
                this.paddingHelper = new RsaPaddingHelper(11, 0);
            } else if (chiper_mode_upper.contains("ECB") && chiper_mode_upper.contains("NOPADDING")) {
                this.paddingHelper = new RsaPaddingHelper(1, 0, false, true, false);
            } else if (chiper_mode_upper.contains("NONE") && chiper_mode_upper.contains("PKCS1PADDING")) {
                this.paddingHelper = new RsaPaddingHelper(11, 0);
            } else if (chiper_mode_upper.contains("NONE") && chiper_mode_upper.contains("PKCS5PADDING")) {
                this.paddingHelper = new RsaPaddingHelper(11, 0);
            } else if (chiper_mode_upper.contains("NONE") && chiper_mode_upper.contains("NOPADDING")) {
                this.paddingHelper = new RsaPaddingHelper(1, 0, false, true, false);
            } else if (chiper_mode_upper.equalsIgnoreCase(RSA)) {
                this.paddingHelper = new RsaPaddingHelper(11, 0);
            } else {
                this.paddingHelper = new RsaPaddingHelper(11, 0);
            }
        } else {
            this.paddingHelper = paddingHelper;
        }
    }

    /**
     * 随机生成RSA密钥对(默认密钥长度为1024)
     *
     * @return
     */
    public KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048<br>
     *                  一般1024
     * @return
     */
    public KeyPair generateRSAKeyPair(int keyLength) {
        KeyPair mKeyPair = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            mKeyPair = kpg.genKeyPair();
        } catch (final Exception e) {
            log.error("Error:generateRSAKeyPair", e);
            mKeyPair = null;
        }
        return mKeyPair;
    }

    /**
     * 随机生成RSA密钥对(默认密钥长度为1024)
     *
     * @return
     */
    public KeyPairStoreFormat generateKeyPairStoreFormat() {
        return generateKeyPairStoreFormat(1024);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048<br>
     *                  一般1024
     * @return
     */
    public KeyPairStoreFormat generateKeyPairStoreFormat(int keyLength) {
        KeyPair mKeyPair = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(keyLength);
            mKeyPair = kpg.genKeyPair();
        } catch (final Exception e) {
            log.error("Error:generateKeyPairStoreFormat", e);
            mKeyPair = null;
        }
        KeyPairStoreFormat keyPairStoreFormat = new KeyPairStoreFormat();
        if (null == mKeyPair) {
            return null;
        }
        PublicKey pubKey = mKeyPair.getPublic();
        PrivateKey privKey = mKeyPair.getPrivate();
        byte[] pk = pubKey.getEncoded();
        byte[] privk = privKey.getEncoded();
        String strpk = keyEncoderHeper.encodeToString(pk);
        String strprivk = keyEncoderHeper.encodeToString(privk);
        keyPairStoreFormat.setPrivateKey(strprivk);
        keyPairStoreFormat.setPublicKey(strpk);
        keyPairStoreFormat.setKeyPair(mKeyPair);
        keyPairStoreFormat.setKeySize(keyLength);
        return keyPairStoreFormat;
    }

    public void generateRSAKeyPair(String keyStoreDir) {
        generateRSAKeyPair(keyStoreDir, "public_key.pem", "private_Key.pem", 1024);
    }

    public void generateRSAKeyPair(String keyStoreDir, int keyLength) {
        generateRSAKeyPair(keyStoreDir, "public_key.pem", "private_Key.pem", keyLength);
    }

    public void generateRSAKeyPair(String keyStoreDir, String publicStoreName, String privateStoreName, int keyLength) {
        KeyPair keyPair = generateRSAKeyPair(keyLength);
        if (null == keyPair) {
            return;
        } else {
            String namePublic = null == publicStoreName || publicStoreName.length() <= 0 ? "public_key.pem"
                    : publicStoreName;
            String namePrivate = null == privateStoreName || privateStoreName.length() <= 0 ? "private_Key.pem"
                    : privateStoreName;
            PublicKey pubKey = keyPair.getPublic();
            PrivateKey privKey = keyPair.getPrivate();
            byte[] pk = pubKey.getEncoded();
            byte[] privk = privKey.getEncoded();
            String strpk = keyEncoderHeper.encodeToString(pk);
            String strprivk = keyEncoderHeper.encodeToString(privk);
            File publucKeyFile = FileUtils.createFile(keyStoreDir + File.separator + namePublic);
            File privateKeyFile = FileUtils.createFile(keyStoreDir + File.separator + namePrivate);
            String strpkStore = formatKey(strpk, "PUBLIC KEY");
            String strprivkStore = formatKey(strprivk, "PRIVATE KEY");
            FileUtils.writeFile(publucKeyFile, strpkStore, false);
            FileUtils.writeFile(privateKeyFile, strprivkStore, false);
        }
    }

    public String formatKey(String strkey, String tag) {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("-----BEGIN " + tag + "-----");
        strBuf.append("\r\n");
        int length = null == strkey ? 0 : strkey.length();
        if (length == 0) {
            strBuf.append("Not Has " + tag + " Infomation");
            strBuf.append("\r\n");
        }
        int size = length / 64;
        for (int i = 0; i < size; i++) {
            strBuf.append(strkey.substring(i * 64, (i + 1) * 64));
            strBuf.append("\r\n");
        }
        if (length % 64 != 0) {
            strBuf.append(strkey.substring(size * 64, length));
            strBuf.append("\r\n");
        }
        strBuf.append("-----END " + tag + "-----");
        return new String(strBuf);
    }

    // public String formatKeyForHtml(String strkey, String tag) {
    // StringBuilder strBuf = new StringBuilder();
    // strBuf.append("-----BEGIN " + tag + "-----");
    // strBuf.append("<br/>");
    // int length = null == strkey ? 0 : strkey.length();
    // if (length == 0) {
    // strBuf.append("Not Has " + tag + " Infomation");
    // strBuf.append("<br/>");
    // }
    // int size = length / 64;
    // for (int i = 0; i < size; i++) {
    // strBuf.append(strkey.substring(i * 64, (i + 1) * 64));
    // strBuf.append("<br/>");
    // }
    // if (length % 64 != 0) {
    // strBuf.append(strkey.substring(size * 64, length));
    // strBuf.append("<br/>");
    // }
    // strBuf.append("-----END " + tag + "-----");
    // return new String(strBuf);
    // }
    public String readFormatKey(String strkey) {
        return readFormatKey(strkey, null);
    }

    public String readFormatKey(String strkey, String charsetName) {
        if (null == strkey || strkey.length() == 0) {
            return null;
        }
        InputStream in_nocode = new ByteArrayInputStream(strkey.getBytes());
        String key = UtilsApp.readKey(in_nocode, charsetName);
        if (null == key || key.length() <= 0) {
            return null;
        }
        String keyReal = key.replace(" ", "").replace("\t", "");

        return null == keyReal || keyReal.length() <= 0 ? null : keyReal;

    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去big_mode_offset(默认11)
     *
     * @param sourceData 需加密数据的byte数据
     * @param publicKey  公钥
     * @return 加密后的byte型数据
     */
    public byte[] encryptData(byte[] sourceData, PublicKey publicKey) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance(chiper_mode);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            result = cipher.doFinal(sourceData);
            if (null != paddingHelper) {
                result = paddingHelper.doPadding(sourceData, result);
            }
        } catch (Exception e) {
            log.error("Error:encryptData", e);

        }
        return result;
    }

    /**
     * 用私钥解密 <br>
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @return 解密后的byte型数据
     */
    public byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance(chiper_mode);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] tmpEncryptedData = null == paddingHelper ? encryptedData : paddingHelper.unPadding(encryptedData);
            result = cipher.doFinal(tmpEncryptedData);
            if (null != paddingHelper) {
                result = paddingHelper.doDecrypt(encryptedData, result);
            }
        } catch (Exception e) {
            log.error("Error:decryptData", e);
        }
        return result;
    }

    /**
     * 用私钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去big_mode_offset(默认11)
     *
     * @param sourceData 需加密数据的byte数据
     * @param privateKey 私钥
     * @return 加密后的byte型数据
     */
    public byte[] encryptData(byte[] sourceData, PrivateKey privateKey) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance(chiper_mode);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            // 传入编码数据并返回编码结果
            result = cipher.doFinal(sourceData);
            if (null != paddingHelper) {
                result = paddingHelper.doPadding(sourceData, result);
            }
        } catch (Exception e) {
            log.error("Error:encryptData", e);
        }
        return result;
    }

    /**
     * 用公钥解密 <br>
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param publicKey     公钥
     * @return 解密后的byte型数据
     */
    public byte[] decryptData(byte[] encryptedData, PublicKey publicKey) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance(chiper_mode);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] tmpEncryptedData = null == paddingHelper ? encryptedData : paddingHelper.unPadding(encryptedData);
            result = cipher.doFinal(tmpEncryptedData);
            if (null != paddingHelper) {
                result = paddingHelper.doDecrypt(encryptedData, result);
            }
        } catch (Exception e) {
            log.error("Error:decryptData", e);

        }
        return result;
    }

    /**
     * 用公钥加密
     */
    public byte[] encryptDataByPublicKey(byte[] sourceData, byte[] pubicKeyData) {
        PublicKey publicKey = getPublicKey(pubicKeyData);
        return encryptData(sourceData, publicKey);
    }

    /**
     * 用私钥解密
     */
    public byte[] decryptDataByPrivateKey(byte[] encryptedData, byte[] privateKeyData) {
        PrivateKey privateKey = getPrivateKey(privateKeyData);
        return decryptData(encryptedData, privateKey);

    }

    /**
     * 用私钥加密
     */
    public byte[] encryptDataByPrivateKey(byte[] sourceData, byte[] privateKeyData) {
        PrivateKey privateKey = getPrivateKey(privateKeyData);
        return encryptData(sourceData, privateKey);
    }

    /**
     * 用公钥解密
     */
    public byte[] decryptDataByPublicKey(byte[] encryptedData, byte[] pubicKeyData) {
        PublicKey publicKey = getPublicKey(pubicKeyData);
        return decryptData(encryptedData, publicKey);
    }

    public String encryptDataByPublicKey(String data, PublicKey publicKey) {
        return encryptDataByPublicKey(data, publicKey, null);
    }

    public String encryptDataByPublicKey(String data, PublicKey publicKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptData(data.getBytes(charset), publicKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataByPublicKey", e);
            return null;
        }
    }

    public String decryptDataByPrivateKey(String data, PrivateKey privateKey) {
        return decryptDataByPrivateKey(data, privateKey, null);

    }

    public String decryptDataByPrivateKey(String data, PrivateKey privateKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptData(dataEncoderHelper.decode(data), privateKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataByPrivateKey", e);
            return null;
        }

    }

    public String encryptDataByPrivateKey(String data, PrivateKey privateKey) {
        return encryptDataByPrivateKey(data, privateKey, null);
    }

    public String encryptDataByPrivateKey(String data, PrivateKey privateKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptData(data.getBytes(charset), privateKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataByPrivateKey", e);
            return null;
        }
    }

    public String decryptDataByPublicKey(String data, PublicKey publicKey) {
        return decryptDataByPublicKey(data, publicKey, null);
    }

    public String decryptDataByPublicKey(String data, PublicKey publicKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptData(dataEncoderHelper.decode(data), publicKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataByPublicKey", e);
            return null;
        }

    }

    public String encryptDataByPublicKey(String data, String publicKeyString) {
        return encryptDataByPublicKey(data, publicKeyString, null);
    }

    public String encryptDataByPublicKey(String data, String publicKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PublicKey publicKey = loadPublicKey(publicKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptData(data.getBytes(charset), publicKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataByPublicKey", e);
            return null;
        }
    }

    public String decryptDataByPrivateKey(String data, String privateKeyString) {
        return decryptDataByPrivateKey(data, privateKeyString, null);
    }

    public String decryptDataByPrivateKey(String data, String privateKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PrivateKey privateKey = loadPrivateKey(privateKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptData(dataEncoderHelper.decode(data), privateKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataByPrivateKey", e);
            return null;
        }
    }

    public String encryptDataByPrivateKey(String data, String privateKeyString) {
        return encryptDataByPrivateKey(data, privateKeyString, null);
    }

    public String encryptDataByPrivateKey(String data, String privateKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PrivateKey privateKey = loadPrivateKey(privateKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptData(data.getBytes(charset), privateKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataByPrivateKey", e);
            return null;
        }
    }

    public String decryptDataByPublicKey(String data, String publicKeyString) {
        return decryptDataByPublicKey(data, publicKeyString, null);
    }

    public String decryptDataByPublicKey(String data, String publicKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PublicKey publicKey = loadPublicKey(publicKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptData(dataEncoderHelper.decode(data), publicKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataByPublicKey", e);
            return null;
        }
    }

    /**
     * 用公钥加密 <br>
     * 按照密钥长度值(keyLength/8)减去big_mode_offset(默认11)分割数据源经行分段加密
     *
     * @param sourceData 需加密数据的byte数据
     * @param publicKey  公钥
     * @param keyLength  密钥长度，取值为keyLength/8
     * @return 加密后的byte型数据
     */
    public byte[] encryptDataBig(byte[] sourceData, PublicKey publicKey, int keyLength) {
        if (null == sourceData || keyLength < 512) {
            return null;
        }
        if (sourceData.length <= 0) {
            return encryptData(sourceData, publicKey);
        }
        byte[] newData = null;
        try {
            ArrayList<byte[]> listData = new ArrayList<byte[]>();
            int srcStep = null == paddingHelper ? keyLength / 8 - 11 : paddingHelper.blockSizeEncrypt(keyLength);
            int srcLength = sourceData.length;
            int srcCount = srcLength / srcStep;
            for (int i = 0; i < srcCount; i++) {
                byte[] tmp = new byte[srcStep];
                System.arraycopy(sourceData, i * srcStep, tmp, 0, srcStep);
                byte[] tmpResult = encryptData(tmp, publicKey);
                UtilsApp.verifyDataResult(tmpResult, true, true);
                listData.add(tmpResult);
            }
            if (srcLength % srcStep != 0) {
                int k = srcLength % srcStep;
                byte[] tmp = new byte[k];
                System.arraycopy(sourceData, srcCount * srcStep, tmp, 0, k);
                byte[] tmpResult = encryptData(tmp, publicKey);
                UtilsApp.verifyDataResult(tmpResult, true, true);
                listData.add(tmpResult);
            }
            newData = UtilsApp.arraysMerage(listData);
        } catch (Exception e) {
            newData = null;
            log.error("Error:encryptDataBig", e);
        }
        return newData;

    }

    /**
     * 用私钥解密 <br>
     * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @param keyLength     密钥长度，取值为keyLength/8
     * @return 解密后的byte型数据
     */
    public byte[] decryptDataBig(byte[] encryptedData, PrivateKey privateKey, int keyLength) {
        int eptStep = null == this.paddingHelper ? keyLength / 8 : paddingHelper.blockSizeDecrypt(keyLength);
        if (null == encryptedData || keyLength < 512 || encryptedData.length % (eptStep) != 0) {
            return null;
        }
        if (encryptedData.length <= 0) {
            return decryptData(encryptedData, privateKey);
        }
        byte[] newData = null;
        try {
            ArrayList<byte[]> listData = new ArrayList<byte[]>();
            int eptLength = encryptedData.length;
            int eptCount = eptLength / eptStep;
            boolean validNoEmpty = eptLength > eptStep;
            for (int i = 0; i < eptCount; i++) {
                byte[] tmp = new byte[eptStep];
                System.arraycopy(encryptedData, i * eptStep, tmp, 0, eptStep);
                byte[] tmpResult = decryptData(tmp, privateKey);
                UtilsApp.verifyDataResult(tmpResult, false, validNoEmpty);
                listData.add(tmpResult);
            }
            if (eptLength % eptStep != 0) {
                int k = eptLength % eptStep;
                byte[] tmp = new byte[k];
                System.arraycopy(encryptedData, eptCount * eptStep, tmp, 0, k);
                byte[] tmpResult = decryptData(tmp, privateKey);
                UtilsApp.verifyDataResult(tmpResult, false, validNoEmpty);
                listData.add(tmpResult);
            }
            newData = UtilsApp.arraysMerage(listData);
        } catch (Exception e) {
            newData = null;
        }
        return newData;

    }

    /**
     * 用私钥加密 <br>
     * 按照密钥长度值(keyLength/8)减去big_mode_offset(默认11)分割数据源经行分段加密
     *
     * @param sourceData 需加密数据的byte数据
     * @param privateKey 私钥
     * @param keyLength  密钥长度，取值为keyLength/8
     * @return 加密后的byte型数据
     */
    public byte[] encryptDataBig(byte[] sourceData, PrivateKey privateKey, int keyLength) {
        if (null == sourceData || keyLength < 512) {
            return null;
        }
        if (sourceData.length <= 0) {
            return encryptData(sourceData, privateKey);
        }
        byte[] newData = null;
        try {
            ArrayList<byte[]> listData = new ArrayList<byte[]>();
            int srcStep = null == paddingHelper ? keyLength / 8 - 11 : paddingHelper.blockSizeEncrypt(keyLength);
            int srcLength = sourceData.length;
            int srcCount = srcLength / srcStep;
            for (int i = 0; i < srcCount; i++) {
                byte[] tmp = new byte[srcStep];
                System.arraycopy(sourceData, i * srcStep, tmp, 0, srcStep);
                byte[] tmpResult = encryptData(tmp, privateKey);
                UtilsApp.verifyDataResult(tmpResult, true, true);
                listData.add(tmpResult);
            }
            if (srcLength % srcStep != 0) {
                int k = srcLength % srcStep;
                byte[] tmp = new byte[k];
                System.arraycopy(sourceData, srcCount * srcStep, tmp, 0, k);
                byte[] tmpResult = encryptData(tmp, privateKey);
                UtilsApp.verifyDataResult(tmpResult, true, true);
                listData.add(tmpResult);
            }
            newData = UtilsApp.arraysMerage(listData);
        } catch (Exception e) {
            newData = null;
        }
        return newData;
    }

    /**
     * 用公钥解密 <br>
     * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param publicKey     公钥
     * @param keyLength     密钥长度，取值为keyLength/8
     * @return 解密后的byte型数据
     */
    public byte[] decryptDataBig(byte[] encryptedData, PublicKey publicKey, int keyLength) {
        int eptStep = null == this.paddingHelper ? keyLength / 8 : paddingHelper.blockSizeDecrypt(keyLength);
        if (null == encryptedData || keyLength < 512 || encryptedData.length % (eptStep) != 0) {
            return null;
        }
        if (encryptedData.length <= 0) {
            return decryptData(encryptedData, publicKey);
        }
        byte[] newData = null;
        try {
            ArrayList<byte[]> listData = new ArrayList<byte[]>();
            int eptLength = encryptedData.length;
            int eptCount = eptLength / eptStep;
            boolean validNoEmpty = eptLength > eptStep;
            for (int i = 0; i < eptCount; i++) {
                byte[] tmp = new byte[eptStep];
                System.arraycopy(encryptedData, i * eptStep, tmp, 0, eptStep);
                byte[] tmpResult = decryptData(tmp, publicKey);
                UtilsApp.verifyDataResult(tmpResult, false, validNoEmpty);
                listData.add(tmpResult);
            }
            if (eptLength % eptStep != 0) {
                int k = eptLength % eptStep;
                byte[] tmp = new byte[k];
                System.arraycopy(encryptedData, eptCount * eptStep, tmp, 0, k);
                byte[] tmpResult = decryptData(tmp, publicKey);
                UtilsApp.verifyDataResult(tmpResult, false, validNoEmpty);
                listData.add(tmpResult);
            }
            newData = UtilsApp.arraysMerage(listData);
        } catch (Exception e) {
            newData = null;
        }
        return newData;
    }

    /**
     * 用公钥加密 <br>
     * 按照密钥长度值(keyLength/8)减去big_mode_offset(默认11)分割数据源经行分段加密
     *
     * @param sourceData 需加密数据的byte数据
     * @param publicKey  公钥
     * @return 加密后的byte型数据
     */
    public byte[] encryptDataBig(byte[] sourceData, PublicKey publicKey) {
        int keyLength = calPublicKeyLength(publicKey);
        return encryptDataBig(sourceData, publicKey, keyLength);

    }

    /**
     * 用私钥解密 <br>
     * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param privateKey    私钥
     * @return 解密后的byte型数据
     */
    public byte[] decryptDataBig(byte[] encryptedData, PrivateKey privateKey) {
        int keyLength = calPrivateKeyLength(privateKey);
        return decryptDataBig(encryptedData, privateKey, keyLength);

    }

    /**
     * 用私钥加密 <br>
     * 按照密钥长度值(keyLength/8)减去big_mode_offset(默认11)分割数据源经行分段加密
     *
     * @param sourceData 需加密数据的byte数据
     * @param privateKey 私钥
     * @return 加密后的byte型数据
     */
    public byte[] encryptDataBig(byte[] sourceData, PrivateKey privateKey) {
        int keyLength = calPrivateKeyLength(privateKey);
        return encryptDataBig(sourceData, privateKey, keyLength);
    }

    /**
     * 用公钥解密 <br>
     * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
     *
     * @param encryptedData 经过encryptedData()加密返回的byte数据
     * @param publicKey     公钥
     * @return 解密后的byte型数据
     */
    public byte[] decryptDataBig(byte[] encryptedData, PublicKey publicKey) {
        int keyLength = calPublicKeyLength(publicKey);
        return decryptDataBig(encryptedData, publicKey, keyLength);
    }

    /**
     * 用公钥加密
     */
    public byte[] encryptDataBigByPublicKey(byte[] sourceData, byte[] pubicKeyData) {
        PublicKey publicKey = getPublicKey(pubicKeyData);
        return encryptDataBig(sourceData, publicKey);
    }

    /**
     * 用私钥解密
     */
    public byte[] decryptDataBigByPrivateKey(byte[] encryptedData, byte[] privateKeyData) {
        PrivateKey privateKey = getPrivateKey(privateKeyData);
        return decryptDataBig(encryptedData, privateKey);

    }

    /**
     * 用私钥加密
     */
    public byte[] encryptDataBigByPrivateKey(byte[] sourceData, byte[] privateKeyData) {
        PrivateKey privateKey = getPrivateKey(privateKeyData);
        return encryptDataBig(sourceData, privateKey);
    }

    /**
     * 用公钥解密
     */
    public byte[] decryptDataBigByPublicKey(byte[] encryptedData, byte[] pubicKeyData) {
        PublicKey publicKey = getPublicKey(pubicKeyData);
        return decryptDataBig(encryptedData, publicKey);
    }

    public String encryptDataBigByPublicKey(String data, PublicKey publicKey) {
        return encryptDataBigByPublicKey(data, publicKey, null);
    }

    public String encryptDataBigByPublicKey(String data, PublicKey publicKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptDataBig(data.getBytes(charset), publicKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataBigByPublicKey", e);
            return null;
        }
    }

    public String encryptDataBigByPublicKey(String data, PublicKey publicKey, String charsetName, int keySize) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptDataBig(data.getBytes(charset), publicKey, keySize));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataBigByPublicKey", e);
            return null;
        }
    }

    public String decryptDataBigByPrivateKey(String data, PrivateKey privateKey) {
        return decryptDataBigByPrivateKey(data, privateKey, null);
    }

    public String decryptDataBigByPrivateKey(String data, PrivateKey privateKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptDataBig(dataEncoderHelper.decode(data), privateKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataBigByPrivateKey", e);
            return null;
        }

    }

    public String decryptDataBigByPrivateKey(String data, PrivateKey privateKey, String charsetName, int keySize) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptDataBig(dataEncoderHelper.decode(data), privateKey, keySize), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataBigByPrivateKey", e);
            return null;
        }

    }

    public String encryptDataBigByPrivateKey(String data, PrivateKey privateKey) {
        return encryptDataBigByPrivateKey(data, privateKey, null);
    }

    public String encryptDataBigByPrivateKey(String data, PrivateKey privateKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptDataBig(data.getBytes(charset), privateKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataBigByPrivateKey", e);
            return null;
        }

    }

    public String encryptDataBigByPrivateKey(String data, PrivateKey privateKey, String charsetName, int keySize) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptDataBig(data.getBytes(charset), privateKey, keySize));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataBigByPrivateKey", e);
            return null;
        }

    }

    public String decryptDataBigByPublicKey(String data, PublicKey publicKey) {
        return decryptDataBigByPublicKey(data, publicKey, null);
    }

    public String decryptDataBigByPublicKey(String data, PublicKey publicKey, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptDataBig(dataEncoderHelper.decode(data), publicKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataBigByPublicKey", e);
            return null;
        }

    }

    public String decryptDataBigByPublicKey(String data, PublicKey publicKey, String charsetName, int keySize) {
        try {
            if (null == data) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptDataBig(dataEncoderHelper.decode(data), publicKey, keySize), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataBigByPublicKey", e);
            return null;
        }

    }

    public String encryptDataBigByPublicKey(String data, String publicKeyString) {
        return encryptDataBigByPublicKey(data, publicKeyString, null);
    }

    public String encryptDataBigByPublicKey(String data, String publicKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PublicKey publicKey = loadPublicKey(publicKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptDataBig(data.getBytes(charset), publicKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataBigByPublicKey", e);
            return null;
        }
    }

    public String decryptDataBigByPrivateKey(String data, String privateKeyString) {
        return decryptDataBigByPrivateKey(data, privateKeyString, null);
    }

    public String decryptDataBigByPrivateKey(String data, String privateKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PrivateKey privateKey = loadPrivateKey(privateKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptDataBig(dataEncoderHelper.decode(data), privateKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataBigByPrivateKey", e);
            return null;
        }
    }

    public String encryptDataBigByPrivateKey(String data, String privateKeyString) {
        return encryptDataBigByPrivateKey(data, privateKeyString, null);
    }

    public String encryptDataBigByPrivateKey(String data, String privateKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PrivateKey privateKey = loadPrivateKey(privateKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return dataEncoderHelper.encodeToString(encryptDataBig(data.getBytes(charset), privateKey));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encryptDataBigByPrivateKey", e);
            return null;
        }
    }

    public String decryptDataBigByPublicKey(String data, String publicKeyString) {
        return decryptDataBigByPublicKey(data, publicKeyString, null);
    }

    public String decryptDataBigByPublicKey(String data, String publicKeyString, String charsetName) {
        try {
            if (null == data) {
                return null;
            }
            PublicKey publicKey = loadPublicKey(publicKeyString);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(decryptDataBig(dataEncoderHelper.decode(data), publicKey), charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:decryptDataBigByPublicKey", e);
            return null;
        }
    }

    public int calPrivateKeyLength(PrivateKey privateKey) {
        if (null == privateKey) {
            return 0;
        }
        int size = privateKey.getEncoded().length;
        if (size < 490) {
            return 512;
        } else if (size < 920) {
            return 1024;
        } else if (size < 1800) {
            return 2048;
        } else {
            return 4096;
        }
    }

    public int calPublicKeyLength(PublicKey publicKey) {
        if (null == publicKey) {
            return 0;
        }
        int size = publicKey.getEncoded().length;
        if (size < 128) {
            return 512;
        } else if (size < 228) {
            return 1024;
        } else if (size < 422) {
            return 2048;
        } else {
            return 4096;
        }
    }

    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey getPublicKey(byte[] keyBytes) {
        try {
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            log.error("Error:getPublicKey", e);
            return null;
        }

    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PrivateKey getPrivateKey(byte[] keyBytes) {
        try {
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            log.error("Error:getPrivateKey", e);
            return null;
        }

    }

    /**
     * 使用N、e值还原公钥
     *
     * @param modulus
     * @param publicExponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey getPublicKey(String modulus, String publicExponent) {
        try {
            final BigInteger bigIntModulus = new BigInteger(modulus);
            final BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
            final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            log.error("Error:getPublicKey", e);
            return null;
        }

    }

    /**
     * 使用N、d值还原私钥
     *
     * @param modulus
     * @param privateExponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PrivateKey getPrivateKey(String modulus, String privateExponent) {
        try {
            final BigInteger bigIntModulus = new BigInteger(modulus);
            final BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
            final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            log.error("Error:getPrivateKey", e);
            return null;
        }

    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @Throws Exception 加载公钥时产生的异常
     */
    public PublicKey loadPublicKey(String publicKeyStr) {
        try {
            final byte[] buffer = keyEncoderHeper.decode(publicKeyStr);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("Error:loadPublicKey", e);
            return null;
        }
    }

    /**
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @Throws Exception 加载公钥时产生的异常
     */
    public PublicKey loadPublicKey(InputStream in) {
        return loadPublicKey(in, null);
    }

    public PublicKey loadPublicKey(InputStream in, String charsetName) {
        try {
            return loadPublicKey(UtilsApp.readKey(in, charsetName));
        } catch (Exception e) {
            log.error("Error:loadPublicKey", e);
            return null;
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return
     * @Throws Exception
     */
    public PrivateKey loadPrivateKey(String privateKeyStr) {
        try {
            final byte[] buffer = keyEncoderHeper.decode(privateKeyStr);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("Error:loadPrivateKey", e);
            return null;
        }
    }

    public KeyPair loadKeyPair(String publicKeyStr, String privateKeyStr) {
        PublicKey mPublicKey = loadPublicKey(publicKeyStr);
        PrivateKey mPrivateKey = loadPrivateKey(privateKeyStr);
        KeyPair mKeyPair = new KeyPair(mPublicKey, mPrivateKey);
        return mKeyPair;
    }

    public KeyPairStoreFormat loadKeyPairStoreFormat(String publicKeyStr, String privateKeyStr) {
        PublicKey mPublicKey = loadPublicKey(publicKeyStr);
        PrivateKey mPrivateKey = loadPrivateKey(privateKeyStr);
        KeyPair mKeyPair = new KeyPair(mPublicKey, mPrivateKey);
        KeyPairStoreFormat keyPairStoreFormat = new KeyPairStoreFormat();
        keyPairStoreFormat.setPublicKey(publicKeyStr);
        keyPairStoreFormat.setPrivateKey(privateKeyStr);
        keyPairStoreFormat.setKeyPair(mKeyPair);
        return keyPairStoreFormat;
    }

    /**
     * 从文件中加载私钥
     *
     * @param in 私钥文件名
     * @return PrivateKey 私钥信息
     * @Throws Exception
     */
    public PrivateKey loadPrivateKey(InputStream in) {
        return loadPrivateKey(in, null);
    }

    public PrivateKey loadPrivateKey(InputStream in, String charsetName) {
        try {
            return loadPrivateKey(UtilsApp.readKey(in, charsetName));
        } catch (Exception e) {
            log.error("Error:loadPrivateKey", e);
            return null;
        }
    }


    public String getSignData(String data, PrivateKey privateKey) {
        return getSignData(data, privateKey, "MD5withRSA", null);
    }

    public String getSignData(String data, PrivateKey privateKey, String signMethod) {
        return getSignData(data, privateKey, signMethod, null);
    }

    /**
     * 签名可用的方法 MD2withRSA MD5andSHA1withRSA MD5withRSA NONEwithDSA NONEwithECDSA NONEwithRSA SHA1withDSA SHA1withECDSA
     * SHA1withRSA SHA224withDSA SHA224withECDSA SHA224withRSA SHA256withDSA SHA256withECDSA SHA256withRSA
     * SHA384withECDSA SHA384withRSA SHA512withECDSA SHA512withRSA
     */
    // RSA私钥签名
    public String getSignData(String data, PrivateKey privateKey, String signMethod, String charsetName) {
        // 设置签名加密方式
        if (null == data) {
            return null;
        }
        String signStr = null;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            // 得到base64编码的签名后的字段
            signStr = dataEncoderHelper.encodeToString(sign(data.getBytes(charset), privateKey, signMethod));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getSignData", e);
            signStr = null;
        }
        return signStr;
    }

    public byte[] sign(byte[] data, PrivateKey privateKey, String signMethod) {
        if (null == data) {
            return null;
        }
        byte[] sign = null;
        try {
            String sgMthd = (null == signMethod || signMethod.length() <= 0) ? "MD5withRSA" : signMethod;
            Signature signature = Signature.getInstance(sgMthd);
            signature.initSign(privateKey);// 设置私钥
            signature.update(data);
            // 签名后的字段
            sign = signature.sign();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:sign", e);
            sign = null;
        }
        return sign;
    }

    public String getSignByInputStream(InputStream inputStream, PrivateKey privateKey, String signMethod) {
        // 设置签名加密方式
        String signStr = null;
        try {
            if (null == inputStream) {
                return null;
            }
            // 得到base64编码的签名后的字段
            signStr = dataEncoderHelper.encodeToString(signByInputStream(inputStream, privateKey, signMethod, false));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getSignByInputStream", e);
            signStr = null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:getSignByInputStream", e);
            }
        }
        return signStr;
    }

    public byte[] signByInputStream(InputStream inputStream, PrivateKey privateKey, String signMethod,
                                    boolean isAutoClose) {
        byte[] signResult = null;
        try {
            if (null == inputStream || null == privateKey) {
                return null;
            }
            String sgMthd = (null == signMethod || signMethod.length() <= 0) ? "MD5withRSA" : signMethod;
            Signature signature = Signature.getInstance(sgMthd);
            signature.initSign(privateKey);// 设置私钥
            // 获取输入流
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                signature.update(data);
                byteArrayOutputStream.reset();
            }

            // 签名后的字段
            byte[] signTemp = signature.sign();
            if (len < 0) {
                signResult = signTemp;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:signByInputStream", e);
            signResult = null;
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:signByInputStream", e);
            }
        }
        return signResult;
    }

    public boolean verifySignData(String data, String signData, PublicKey publicKey) {
        return verifySignData(data, signData, publicKey, "MD5withRSA", null);
    }

    public boolean verifySignData(String data, String signData, PublicKey publicKey, String signMethod) {
        return verifySignData(data, signData, publicKey, signMethod, null);
    }

    public boolean verifySignData(String data, String signData, PublicKey publicKey, String signMethod,
                                  String charsetName) {
        if (null == data) {
            if (signData == null) {
                return true;
            } else {
                return false;
            }
        }
        boolean isPassed = false;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            isPassed = verifySign(data.getBytes(charset), dataEncoderHelper.decode(signData), publicKey, signMethod);
        } catch (Exception e) {
            log.error("Error:verifySignData", e);
            isPassed = false;
        }
        return isPassed;
    }

    public boolean verifySign(byte[] data, byte[] signData, PublicKey publicKey, String signMethod) {
        boolean isPassed = false;
        try {
            String sgMthd = (null == signMethod || signMethod.length() <= 0) ? "MD5withRSA" : signMethod;
            Signature signature = Signature.getInstance(sgMthd);
            signature.initVerify(publicKey);
            signature.update(data);
            // 验签结果
            isPassed = signature.verify(signData);
        } catch (Exception e) {
            log.error("Error:verifySign", e);
            isPassed = false;
        }
        return isPassed;
    }

    public boolean verifySignByInputStream(InputStream inputStream, String signData, PublicKey publicKey,
                                           String signMethod) {
        boolean signResult = false;
        try {
            signResult = verifySign(inputStream, dataEncoderHelper.decode(signData), publicKey, signMethod, false);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:verifySignByInputStream", e);
            signResult = false;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:verifySignByInputStream", e);
            }
        }
        return signResult;
    }

    public boolean verifySign(InputStream inputStream, byte[] signData, PublicKey publicKey, String signMethod,
                              boolean isAutoClose) {
        boolean isPassed = false;
        try {
            if (null == inputStream || null == publicKey) {
                return false;
            }
            String sgMthd = (null == signMethod || signMethod.length() <= 0) ? "MD5withRSA" : signMethod;
            Signature signature = Signature.getInstance(sgMthd);
            signature.initVerify(publicKey);// 设置公钥
            // 获取输入流

            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                signature.update(data);
                byteArrayOutputStream.reset();
            }

            // 签名后的字段
            boolean isTempPassed = signature.verify(signData);
            if (len < 0) {
                isPassed = isTempPassed;
            } else {
                isPassed = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:verifySign", e);
            isPassed = false;
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:verifySign", e);
            }
        }
        return isPassed;
    }

}
