package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.encryptx.dal.KeyPairStoreFormat;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/6/10 13:57
 */
public class SM2KeyHelper {
    private final static Log log = LogFactory.getLog(SM2KeyHelper.class);

    private EncoderHelper encoderHelper;
    public static SM2KeyHelper gmHelper = new SM2KeyHelper();

    public static SM2KeyHelper getInstance() {
        return gmHelper;
    }

    public SM2KeyHelper() {
        this(EncoderHelper.EncoderMode.BASE64);
    }

    public SM2KeyHelper(EncoderHelper.EncoderMode encoderMode) {
        this.encoderHelper = new EncoderHelper(encoderMode);
    }

    public void setEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.encoderHelper = new EncoderHelper(encoderMode);
    }

    // 生成随机秘钥对
    public KeyPairStoreFormat generateKeyPairStoreFormat() {
        SM2KeyVO sm2KeyVO = SM2EncDecUtils.generateKeyPair();
        KeyPairStoreFormat keyPairStoreFormat = new KeyPairStoreFormat();
        keyPairStoreFormat.setPublicKey(encoderHelper.encodeToString(sm2KeyVO.getPublicKey().getEncoded(false)));
        keyPairStoreFormat.setPrivateKey(encoderHelper.encodeToString(sm2KeyVO.getPrivateKey().toByteArray()));
        keyPairStoreFormat.setKeySize(256);
        if (SMConfig.isLog) {
            log.debug("generateKeyPairForStringFormat->" + "公钥: " + keyPairStoreFormat.getPublicKey());
            log.debug("generateKeyPairForStringFormat->" + "私钥: " + keyPairStoreFormat.getPrivateKey());
        }
        return keyPairStoreFormat;

    }

    public byte[] loadPublicKey(String pubkeyStr) throws Exception {
        byte[] keydata = encoderHelper.decode(pubkeyStr);
        if (null != keydata && keydata.length <= 65) {
            return keydata;
        }
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo
                .getInstance(keydata);

//        DERBitString publicKeyData = subjectPublicKeyInfo.getPublicKeyData();
        ASN1BitString publicKeyData = subjectPublicKeyInfo.getPublicKeyData();
        byte[] publicKey = publicKeyData.getEncoded();
        byte[] encodedPublicKey = publicKey;
//		byte[] ecP = new byte[64];
//		System.arraycopy(encodedPublicKey, 4, ecP, 0, ecP.length);
//
//		byte[] certPKX = new byte[32];
//		byte[] certPKY = new byte[32];
//		System.arraycopy(ecP, 0, certPKX, 0, 32);
//		System.arraycopy(ecP, 32, certPKY, 0, 32);
        byte[] ecP = new byte[65];
        System.arraycopy(encodedPublicKey, 3, ecP, 0, ecP.length);

        byte[] certPKX = new byte[32];
        byte[] certPKY = new byte[32];
        System.arraycopy(ecP, 1, certPKX, 0, 32);
        System.arraycopy(ecP, 33, certPKY, 0, 32);
        log.debug("公钥为：" + HexUtils.encodeToString(ecP));
        return ecP;
    }

    public byte[] loadPrivateKey(String privatekeyStr) throws Exception {
        byte[] keydata = encoderHelper.decode(privatekeyStr);
        if (null != keydata && keydata.length <= 33) {
            return keydata;
        }
        ASN1Sequence seq = ASN1Sequence.getInstance(keydata);
        ASN1Encodable asn1Encodable1 = seq.getObjectAt(2);
        DEROctetString derOctetString1 = (DEROctetString) asn1Encodable1;
        DLSequence dLSequence = (DLSequence) ASN1Sequence
                .fromByteArray(derOctetString1.getOctets());
        ASN1Encodable asn1Encodable2 = dLSequence.getObjectAt(1);
        DEROctetString derOctetString2 = (DEROctetString) asn1Encodable2;
        byte[] ecP = derOctetString2.getOctets();
        log.debug("私钥为：" + HexUtils.encodeToString(ecP));
        return ecP;
    }
}
