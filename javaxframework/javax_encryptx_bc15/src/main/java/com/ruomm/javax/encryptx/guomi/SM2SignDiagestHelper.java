package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.InputStream;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/11/19 10:51
 */
public class SM2SignDiagestHelper {
    private final static Log log = LogFactory.getLog(SM2SignDiagestHelper.class);
    private final static boolean STREAM_AUTO_CLOSE = true;
    private final static boolean MODE_SOFT_CONFIG = false;
    private final static boolean MODE_HARD_CONFIG = true;

    private EncoderHelper dataEncoderHelper;
    private SM2KeyHelper sm2KeyHelper;
    public static SM2SignDiagestHelper gmHelper = new SM2SignDiagestHelper();

    public static SM2SignDiagestHelper getInstance() {
        return gmHelper;
    }

    public SM2SignDiagestHelper() {
        this(EncoderHelper.EncoderMode.BASE64, EncoderHelper.EncoderMode.BASE64);
    }

    public SM2SignDiagestHelper(EncoderHelper.EncoderMode keyEncoderMode, EncoderHelper.EncoderMode dataEncoderMode) {
        this.sm2KeyHelper = new SM2KeyHelper(keyEncoderMode);
        this.dataEncoderHelper = new EncoderHelper(dataEncoderMode);
    }

    public void setKeyEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.sm2KeyHelper = new SM2KeyHelper(encoderMode);
    }

    public void setDataEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.dataEncoderHelper = new EncoderHelper(encoderMode);
    }

    public SM2KeyHelper getKeyHelper() {
        return sm2KeyHelper;
    }

    private byte[] sm2SignCommon(byte[] sm3Diagest, byte[] privateKey, boolean hardMode) {
        byte[] signResult;
        try {
            SM2SignVO sm2SignVO = SM2SignVerUtils.Sign2SM2WithSM3(privateKey, sm3Diagest);
            if (hardMode) {
                signResult = Util.hexToByte(sm2SignVO.getSm2_signForHard());
            } else {
                signResult = Util.hexToByte(sm2SignVO.getSm2_signForSoft());
            }

        } catch (Exception e) {
            signResult = null;
            log.error("Error:sm2SignCommon", e);
        }
        return signResult;
    }

    @SuppressWarnings("unchecked")
    private boolean sm2VerifyCommon(byte[] sm3Diagest, byte[] signData, byte[] publicKey, boolean hardMode) {
        SM2SignVO sm2SignVO = SM2SignVerUtils.VerifySignSM2WithSM3(publicKey, sm3Diagest, signData, hardMode);
        return null == sm2SignVO ? false : sm2SignVO.isVerify();
    }

    public byte[] toSm3ByPrivateKey(byte[] userId, byte[] sourceData, byte[] privateKey) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPrivateKey(privateKey, userId, sourceData);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:toSm3ByPrivateKey", e);
        }
        return signResult;
    }

    public byte[] toSm3ByPublicKey(byte[] userId, byte[] sourceData, byte[] publicKey) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPublicKey(publicKey, userId, sourceData);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:toSm3ByPublicKey", e);
        }
        return signResult;
    }

    public byte[] toSm3ByPrivateKey(byte[] userId, InputStream inputStream, byte[] privateKey) {
        return toSm3ByPrivateKey(userId, inputStream, privateKey, STREAM_AUTO_CLOSE);
    }

    public byte[] toSm3ByPublicKey(byte[] userId, InputStream inputStream, byte[] publicKey) {
        return toSm3ByPublicKey(userId, inputStream, publicKey, STREAM_AUTO_CLOSE);
    }

    public byte[] toSm3ByPrivateKey(byte[] userId, InputStream inputStream, byte[] privateKey, boolean isAutoClose) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPrivateKeyStream(privateKey, userId, inputStream);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:toSm3ByPrivateKey", e);
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:toSm3ByPrivateKey", e);
            }
        }
        return signResult;
    }

    public byte[] toSm3ByPublicKey(byte[] userId, InputStream inputStream, byte[] publicKey, boolean isAutoClose) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPublicKeyStream(publicKey, userId, inputStream);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:toSm3ByPublicKey", e);
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:toSm3ByPublicKey", e);
            }
        }
        return signResult;
    }

    public byte[] toSm3ByPrivateKey(byte[] userId, byte[] sourceData, String privateKey) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPrivateKey(sm2KeyHelper.loadPrivateKey(privateKey), userId, sourceData);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:toSm3ByPrivateKey", e);
        }
        return signResult;
    }

    public byte[] toSm3ByPublicKey(byte[] userId, byte[] sourceData, String publicKey) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPublicKey(sm2KeyHelper.loadPublicKey(publicKey), userId, sourceData);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:toSm3ByPrivateKey", e);
        }
        return signResult;
    }

    public byte[] toSm3ByPrivateKey(byte[] userId, InputStream inputStream, String privateKey) {
        return toSm3ByPrivateKey(userId, inputStream, privateKey, STREAM_AUTO_CLOSE);
    }

    public byte[] toSm3ByPublicKey(byte[] userId, InputStream inputStream, String publicKey) {
        return toSm3ByPublicKey(userId, inputStream, publicKey, STREAM_AUTO_CLOSE);
    }

    public byte[] toSm3ByPrivateKey(byte[] userId, InputStream inputStream, String privateKey, boolean isAutoClose) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPrivateKeyStream(sm2KeyHelper.loadPrivateKey(privateKey), userId, inputStream);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:sm2SignStreamCommon", e);
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:sm2SignStreamCommon", e);
            }
        }
        return signResult;
    }

    public byte[] toSm3ByPublicKey(byte[] userId, InputStream inputStream, String publicKey, boolean isAutoClose) {
        byte[] signResult;
        try {
            signResult = SM2SignVerUtils.ToSm3MDByPublicKeyStream(sm2KeyHelper.loadPublicKey(publicKey), userId, inputStream);
        } catch (Exception e) {
            signResult = null;
            log.error("Error:sm2SignStreamCommon", e);
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:sm2SignStreamCommon", e);
            }
        }
        return signResult;
    }


    public byte[] signSoft(byte[] sm3Diagest, byte[] privateKey) {
        return sm2SignCommon(sm3Diagest, privateKey, MODE_SOFT_CONFIG);
    }

    @SuppressWarnings("unchecked")
    public boolean verifySoft(byte[] sm3Diagest, byte[] signData, byte[] publicKey) {
        return sm2VerifyCommon(sm3Diagest, signData, publicKey, MODE_SOFT_CONFIG);
    }

    public String signToStringSoft(String sm3Diagest, String privateKey) {
        return signToStringSoft(sm3Diagest, privateKey, null);
    }

    public String signToStringSoft(String sm3Diagest, String privateKey, String charsetName) {
        if (null == sm3Diagest) {
            return null;
        }
        String signStr = null;
        try {
            byte[] cipherText = sm2SignCommon(HexUtils.decodeHexStr(sm3Diagest), sm2KeyHelper.loadPrivateKey(privateKey), MODE_SOFT_CONFIG);
            signStr = dataEncoderHelper.encodeToString(cipherText);
        } catch (Exception e) {
            signStr = null;
            log.error("Error:signToStringSoft", e);
        }
        return signStr;
    }

    public boolean verifyByStringSoft(String sm3Diagest, String signData, String publicKey) {
        return verifyByStringSoft(sm3Diagest, signData, publicKey, null);
    }

    public boolean verifyByStringSoft(String sm3Diagest, String signData, String publicKey,
                                      String charsetName) {
        if (null == sm3Diagest) {
            if (signData == null) {
                return true;
            } else {
                return false;
            }
        }
        try {
            return sm2VerifyCommon(HexUtils.decodeHexStr(sm3Diagest), dataEncoderHelper.decode(signData),
                    sm2KeyHelper.loadPublicKey(publicKey), MODE_SOFT_CONFIG);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:verifyByStringSoft", e);
            return false;
        }
    }

    public byte[] signHard(byte[] sm3Diagest, byte[] privateKey) {
        return sm2SignCommon(sm3Diagest, privateKey, MODE_HARD_CONFIG);
    }

    @SuppressWarnings("unchecked")
    public boolean verifyHard(byte[] sm3Diagest, byte[] signData, byte[] publicKey) {
        return sm2VerifyCommon(sm3Diagest, signData, publicKey, MODE_HARD_CONFIG);
    }

    public String signToStringHard(String sm3Diagest, String privateKey) {
        return signToStringHard(sm3Diagest, privateKey, null);
    }

    public String signToStringHard(String sm3Diagest, String privateKey, String charsetName) {
        if (null == sm3Diagest) {
            return null;
        }
        String signStr = null;
        try {
            byte[] cipherText = sm2SignCommon(HexUtils.decodeHexStr(sm3Diagest), sm2KeyHelper.loadPrivateKey(privateKey), MODE_HARD_CONFIG);
            signStr = dataEncoderHelper.encodeToString(cipherText);
        } catch (Exception e) {
            signStr = null;
            log.error("Error:signToStringHard", e);
        }
        return signStr;
    }

    public boolean verifyByStringHard(String sm3Diagest, String signData, String publicKey) {
        return verifyByStringHard(sm3Diagest, signData, publicKey, null);
    }

    public boolean verifyByStringHard(String sm3Diagest, String signData, String publicKey,
                                      String charsetName) {
        if (null == sm3Diagest) {
            if (signData == null) {
                return true;
            } else {
                return false;
            }
        }
        try {
            return sm2VerifyCommon(HexUtils.decodeHexStr(sm3Diagest), dataEncoderHelper.decode(signData),
                    sm2KeyHelper.loadPublicKey(publicKey), MODE_HARD_CONFIG);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:verifyByStringHard", e);
            return false;
        }
    }
}
