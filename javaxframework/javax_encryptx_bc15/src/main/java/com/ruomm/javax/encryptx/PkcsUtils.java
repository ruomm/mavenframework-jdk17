package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.corex.HexUtils;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/9/28 10:50
 */
public class PkcsUtils {
    static {
        BCProviderLoadUtil.loadProvider();
    }

    /**
     * 加载PKCS1格式公钥
     *
     * @param encodeByte PKCS1格式公钥编码
     * @return 公钥信息
     */
    public static PrivateKey loadPrivateKey(byte[] encodeByte) {
        try {
            RSAPrivateKeyStructure pkcs1Key = RSAPrivateKeyStructure.getInstance(ASN1Sequence.fromByteArray(encodeByte));
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(pkcs1Key.getModulus(), pkcs1Key.getPrivateExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加载PKCS1格式私钥
     *
     * @param encodeByte PKCS1格式私钥编码
     * @return 私钥信息
     */
    public static PublicKey loadPublicKey(byte[] encodeByte) {
        try {
            RSAPublicKeyStructure pkcs1Key = RSAPublicKeyStructure.getInstance(ASN1Sequence.fromByteArray(encodeByte));
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(pkcs1Key.getModulus(), pkcs1Key.getPublicExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥PKCS1格式转换为PKCS8格式
     *
     * @param encodeByte pkcs1格式
     * @return pkcs8格式
     */
    public static byte[] pubKeyPkcs1To8(byte[] encodeByte) {
        try {
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
            ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
            SubjectPublicKeyInfo privKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, asn1Object);
            byte[] pkcs8Bytes = privKeyInfo.getEncoded();
            return pkcs8Bytes; // 直接一行字符串输出
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥PKCS8格式转换为PKCS1格式
     *
     * @param encodeByte pkcs8格式
     * @return pkcs1格式
     */
    public static byte[] pubKeyPkcs8To1(byte[] encodeByte) {
        try {
            SubjectPublicKeyInfo pkey = SubjectPublicKeyInfo.getInstance(encodeByte);
            RSAPublicKeyStructure pkcs1Key = RSAPublicKeyStructure.getInstance(pkey.getPublicKey());
            byte[] pkcs1Bytes = pkcs1Key.getEncoded();
            return pkcs1Bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥PKCS1格式转换为PKCS8格式
     *
     * @param encodeByte pkcs1格式
     * @return pkcs8格式
     */
    public static byte[] priKeyPkcs1To8(byte[] encodeByte) {
        try {
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
            ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
            PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Object);
            byte[] pkcs8Bytes = privKeyInfo.getEncoded();
            return pkcs8Bytes; // 直接一行字符串输出
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥PKCS8格式转换为PKCS1格式
     *
     * @param encodeByte pkcs8格式
     * @return pkcs1格式
     */
    public static byte[] priKeyPkcs8To1(byte[] encodeByte) {
        try {

            PrivateKeyInfo pkey = PrivateKeyInfo.getInstance(encodeByte);
            RSAPrivateKeyStructure pkcs1Key = RSAPrivateKeyStructure.getInstance(pkey.parsePrivateKey());
            byte[] pkcs1Bytes = pkcs1Key.getEncoded();
            return pkcs1Bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 公钥PKCS1格式转换为PKCS8格式
     *
     * @param rawKey pkcs1格式
     * @return pkcs8格式
     */
    public static String pubKeyBase64Pkcs1To8(String rawKey) {
        try {
            byte[] encodeByte = Base64Utils.decode(rawKey);
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
            ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
            SubjectPublicKeyInfo privKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, asn1Object);
            byte[] pkcs8Bytes = privKeyInfo.getEncoded();
            return Base64Utils.encodeToString(pkcs8Bytes); // 直接一行字符串输出
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥PKCS8格式转换为PKCS1格式
     *
     * @param rawKey pkcs8格式
     * @return pkcs1格式
     */
    public static String pubKeyBase64Pkcs8To1(String rawKey) {
        try {
            byte[] encodeByte = Base64Utils.decode(rawKey);
            SubjectPublicKeyInfo pkey = SubjectPublicKeyInfo.getInstance(encodeByte);
            RSAPublicKeyStructure pkcs1Key = RSAPublicKeyStructure.getInstance(pkey.getPublicKey());
            byte[] pkcs1Bytes = pkcs1Key.getEncoded();
            return Base64Utils.encodeToString(pkcs1Bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥PKCS1格式转换为PKCS8格式
     *
     * @param rawKey pkcs1格式
     * @return pkcs8格式
     */
    public static String priKeyBase64Pkcs1to8(String rawKey) {
        try {
            byte[] encodeByte = Base64Utils.decode(rawKey);
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
            ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
            PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Object);
            byte[] pkcs8Bytes = privKeyInfo.getEncoded();
            return Base64Utils.encodeToString(pkcs8Bytes); // 直接一行字符串输出
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥PKCS8格式转换为PKCS1格式
     *
     * @param rawKey pkcs8格式
     * @return pkcs1格式
     */
    public static String priKeyBase64Pkcs8to1(String rawKey) {
        try {
            byte[] encodeByte = Base64Utils.decode(rawKey);
            PrivateKeyInfo pkey = PrivateKeyInfo.getInstance(encodeByte);
            RSAPrivateKeyStructure pkcs1Key = RSAPrivateKeyStructure.getInstance(pkey.parsePrivateKey());
            byte[] pkcs1Bytes = pkcs1Key.getEncoded();
            return Base64Utils.encodeToString(pkcs1Bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 公钥PKCS1格式转换为PKCS8格式
     *
     * @param rawKey pkcs1格式
     * @return pkcs8格式
     */
    public static String pubKeyHexPkcs1To8(String rawKey) {
        try {
            byte[] encodeByte = HexUtils.decodeHexStr(rawKey);
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
            ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
            SubjectPublicKeyInfo privKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, asn1Object);
            byte[] pkcs8Bytes = privKeyInfo.getEncoded();
            return HexUtils.encodeHexStr(pkcs8Bytes); // 直接一行字符串输出
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥PKCS8格式转换为PKCS1格式
     *
     * @param rawKey pkcs8格式
     * @return pkcs1格式
     */
    public static String pubKeyHexPkcs8To1(String rawKey) {
        try {
            byte[] encodeByte = HexUtils.decodeHexStr(rawKey);
            SubjectPublicKeyInfo pkey = SubjectPublicKeyInfo.getInstance(encodeByte);
            RSAPublicKeyStructure pkcs1Key = RSAPublicKeyStructure.getInstance(pkey.getPublicKey());
            byte[] pkcs1Bytes = pkcs1Key.getEncoded();
            return HexUtils.encodeHexStr(pkcs1Bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥PKCS1格式转换为PKCS8格式
     *
     * @param rawKey pkcs1格式
     * @return pkcs8格式
     */
    public static String priKeyHexPkcs1To8(String rawKey) {
        try {
            byte[] encodeByte = HexUtils.decodeHexStr(rawKey);
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
            ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
            PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Object);
            byte[] pkcs8Bytes = privKeyInfo.getEncoded();
            return HexUtils.encodeHexStr(pkcs8Bytes); // 直接一行字符串输出
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥PKCS8格式转换为PKCS1格式
     *
     * @param rawKey pkcs8格式
     * @return pkcs1格式
     */
    public static String priKeyHexPkcs8To1(String rawKey) {
        try {
            byte[] encodeByte = HexUtils.decodeHexStr(rawKey);
            PrivateKeyInfo pkey = PrivateKeyInfo.getInstance(encodeByte);
            RSAPrivateKeyStructure pkcs1Key = RSAPrivateKeyStructure.getInstance(pkey.parsePrivateKey());
            byte[] pkcs1Bytes = pkcs1Key.getEncoded();
            return HexUtils.encodeHexStr(pkcs1Bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
