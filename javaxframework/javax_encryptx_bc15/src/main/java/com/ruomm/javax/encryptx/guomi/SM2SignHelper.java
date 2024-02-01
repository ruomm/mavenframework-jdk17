package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/11/19 10:51
 */
public class SM2SignHelper {
    private final static Log log = LogFactory.getLog(SM2SignHelper.class);
    private final static boolean STREAM_AUTO_CLOSE = true;
    private final static boolean MODE_SOFT_CONFIG = false;
    private final static boolean MODE_HARD_CONFIG = true;

    private EncoderHelper dataEncoderHelper;
    private SM2KeyHelper sm2KeyHelper;
    public static SM2SignHelper gmHelper = new SM2SignHelper();

    public static SM2SignHelper getInstance() {
        return gmHelper;
    }

    public SM2SignHelper() {
        this(EncoderHelper.EncoderMode.BASE64, EncoderHelper.EncoderMode.BASE64);
    }

    public SM2SignHelper(EncoderHelper.EncoderMode keyEncoderMode, EncoderHelper.EncoderMode dataEncoderMode) {
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

    private byte[] sm2SignCommon(byte[] userId, byte[] sourceData, byte[] privateKey, boolean hardMode) {
        byte[] signResult;
        try {
            SM2SignVO sm2SignVO = SM2SignVerUtils.Sign2SM2(privateKey, userId, sourceData);
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

    private byte[] sm2SignStreamCommon(byte[] userId, InputStream inputStream, byte[] privateKey,
                                       boolean isAutoClose, boolean hardMode) {
        byte[] signResult;
        try {
            SM2SignVO sm2SignVO = SM2SignVerUtils.Sign2SM2ByInputStream(privateKey, userId, inputStream);
            if (hardMode) {
                signResult = Util.hexToByte(sm2SignVO.getSm2_signForHard());
            } else {
                signResult = Util.hexToByte(sm2SignVO.getSm2_signForSoft());
            }
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

    @SuppressWarnings("unchecked")
    private boolean sm2VerifyCommon(byte[] userId, byte[] sourceData, byte[] signData, byte[] publicKey, boolean hardMode) {
        SM2SignVO sm2SignVO = SM2SignVerUtils.VerifySignSM2(publicKey, userId, sourceData, signData, hardMode);
        return null == sm2SignVO ? false : sm2SignVO.isVerify();
    }

    @SuppressWarnings("unchecked")
    private boolean sm2VerifyStreamCommon(byte[] userId, InputStream inputStream, byte[] signData, byte[] publicKey,
                                          boolean isAutoClose, boolean hardMode) {
        boolean isPassed;
        try {
            SM2SignVO sm2SignVO = SM2SignVerUtils.VerifySignSM2ByInputStream(publicKey, userId, inputStream, signData, hardMode);
            isPassed = null == sm2SignVO ? false : sm2SignVO.isVerify();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            isPassed = false;
            log.error("Error:sm2VerifyStreamCommon", e);
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:sm2VerifyStreamCommon", e);
            }
        }
        return isPassed;
    }

    public byte[] signSoft(byte[] userId, byte[] sourceData, byte[] privateKey) {
        return sm2SignCommon(userId, sourceData, privateKey, MODE_SOFT_CONFIG);


    }

    public byte[] signSoft(byte[] userId, InputStream inputStream, byte[] privateKey,
                           boolean isAutoClose) {
        return sm2SignStreamCommon(userId, inputStream, privateKey, isAutoClose, MODE_SOFT_CONFIG);

    }

    @SuppressWarnings("unchecked")
    public boolean verifySoft(byte[] userId, byte[] sourceData, byte[] signData, byte[] publicKey) {
        return sm2VerifyCommon(userId, sourceData, signData, publicKey, MODE_SOFT_CONFIG);
    }

    @SuppressWarnings("unchecked")
    public boolean verifySoft(byte[] userId, InputStream inputStream, byte[] signData, byte[] publicKey,
                              boolean isAutoClose) {
        return sm2VerifyStreamCommon(userId, inputStream, signData, publicKey, isAutoClose, MODE_SOFT_CONFIG);
    }

    public String signToStringSoft(String userId, String sourceData, String privateKey) {
        return signToStringSoft(userId, sourceData, privateKey, null);
    }

    public String signToStringSoft(String userId, String sourceData, String privateKey, String charsetName) {
        if (null == sourceData) {
            return null;
        }
        String signStr = null;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] cipherText = sm2SignCommon(userId.getBytes(charset), sourceData.getBytes(charset), sm2KeyHelper.loadPrivateKey(privateKey), MODE_SOFT_CONFIG);
            signStr = dataEncoderHelper.encodeToString(cipherText);
        } catch (Exception e) {
            signStr = null;
            log.error("Error:signToStringSoft", e);
        }
        return signStr;
    }

    public String signToStringSoft(String userId, InputStream inputStream, String privateKey) {
        return signToStringSoft(userId, inputStream, privateKey, null);
    }

    public String signToStringSoft(String userId, InputStream inputStream, String privateKey,
                                   String charsetName) {
        String signStr = null;
        try {
            if (null == inputStream) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            // 得到base64编码的签名后的字段
            signStr = dataEncoderHelper.encodeToString(
                    sm2SignStreamCommon(userId.getBytes(charset), inputStream, sm2KeyHelper.loadPrivateKey(privateKey), STREAM_AUTO_CLOSE, MODE_SOFT_CONFIG));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            signStr = null;
            log.error("Error:signToStringSoft", e);
        }
        return signStr;
    }

    public boolean verifyByStringSoft(String userId, String sourceData, String signData, String publicKey) {
        return verifyByStringSoft(userId, sourceData, signData, publicKey, null);
    }

    public boolean verifyByStringSoft(String userId, String sourceData, String signData, String publicKey,
                                      String charsetName) {
        if (null == sourceData) {
            if (signData == null) {
                return true;
            } else {
                return false;
            }
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return sm2VerifyCommon(userId.getBytes(charset), sourceData.getBytes(charset), dataEncoderHelper.decode(signData),
                    sm2KeyHelper.loadPublicKey(publicKey), MODE_SOFT_CONFIG);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:verifyByStringSoft", e);
            return false;
        }
    }

    public boolean verifyByStringSoft(String userId, InputStream inputStream, String signData,
                                      String publicKey) {
        return verifyByStringSoft(userId, inputStream, signData, publicKey, null);
    }

    public boolean verifyByStringSoft(String userId, InputStream inputStream, String signData,
                                      String publicKey, String charsetName) {
        boolean signResult = false;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            signResult = sm2VerifyStreamCommon(userId.getBytes(charset), inputStream, dataEncoderHelper.decode(signData),
                    sm2KeyHelper.loadPublicKey(publicKey), STREAM_AUTO_CLOSE, MODE_SOFT_CONFIG);
        } catch (Exception e) {
            // TODO: handle exception
            signResult = false;
            log.error("Error:verifyByStringSoft", e);
        }
        return signResult;
    }

    public byte[] signHard(byte[] userId, byte[] sourceData, byte[] privateKey) {
        return sm2SignCommon(userId, sourceData, privateKey, MODE_HARD_CONFIG);


    }

    public byte[] signHard(byte[] userId, InputStream inputStream, byte[] privateKey,
                           boolean isAutoClose) {
        return sm2SignStreamCommon(userId, inputStream, privateKey, isAutoClose, MODE_HARD_CONFIG);

    }

    @SuppressWarnings("unchecked")
    public boolean verifyHard(byte[] userId, byte[] sourceData, byte[] signData, byte[] publicKey) {
        return sm2VerifyCommon(userId, sourceData, signData, publicKey, MODE_HARD_CONFIG);
    }

    @SuppressWarnings("unchecked")
    public boolean verifyHard(byte[] userId, InputStream inputStream, byte[] signData, byte[] publicKey,
                              boolean isAutoClose) {
        return sm2VerifyStreamCommon(userId, inputStream, signData, publicKey, isAutoClose, MODE_HARD_CONFIG);
    }

    public String signToStringHard(String userId, String sourceData, String privateKey) {
        return signToStringHard(userId, sourceData, privateKey, null);
    }

    public String signToStringHard(String userId, String sourceData, String privateKey, String charsetName) {
        if (null == sourceData) {
            return null;
        }
        String signStr = null;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] cipherText = sm2SignCommon(userId.getBytes(charset), sourceData.getBytes(charset), sm2KeyHelper.loadPrivateKey(privateKey), MODE_HARD_CONFIG);
            signStr = dataEncoderHelper.encodeToString(cipherText);
        } catch (Exception e) {
            signStr = null;
            log.error("Error:signToStringHard", e);
        }
        return signStr;
    }

    public String signToStringHard(String userId, InputStream inputStream, String privateKey) {
        return signToStringHard(userId, inputStream, privateKey, null);
    }

    public String signToStringHard(String userId, InputStream inputStream, String privateKey,
                                   String charsetName) {
        String signStr = null;
        try {
            if (null == inputStream) {
                return null;
            }
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            // 得到base64编码的签名后的字段
            signStr = dataEncoderHelper.encodeToString(
                    sm2SignStreamCommon(userId.getBytes(charset), inputStream, sm2KeyHelper.loadPrivateKey(privateKey), STREAM_AUTO_CLOSE, MODE_HARD_CONFIG));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            signStr = null;
            log.error("Error:signToStringHard", e);
        }
        return signStr;
    }

    public boolean verifyByStringHard(String userId, String sourceData, String signData, String publicKey) {
        return verifyByStringHard(userId, sourceData, signData, publicKey, null);
    }

    public boolean verifyByStringHard(String userId, String sourceData, String signData, String publicKey,
                                      String charsetName) {
        if (null == sourceData) {
            if (signData == null) {
                return true;
            } else {
                return false;
            }
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return sm2VerifyCommon(userId.getBytes(charset), sourceData.getBytes(charset), dataEncoderHelper.decode(signData),
                    sm2KeyHelper.loadPublicKey(publicKey), MODE_HARD_CONFIG);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:verifyByStringHard", e);
            return false;
        }
    }

    public boolean verifyByStringHard(String userId, InputStream inputStream, String signData,
                                      String publicKey) {
        return verifyByStringHard(userId, inputStream, signData, publicKey, null);
    }

    public boolean verifyByStringHard(String userId, InputStream inputStream, String signData,
                                      String publicKey, String charsetName) {
        boolean signResult = false;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            signResult = sm2VerifyStreamCommon(userId.getBytes(charset), inputStream, dataEncoderHelper.decode(signData),
                    sm2KeyHelper.loadPublicKey(publicKey), STREAM_AUTO_CLOSE, MODE_HARD_CONFIG);
        } catch (Exception e) {
            // TODO: handle exception
            signResult = false;
            log.error("Error:verifyByStringHard", e);
        }
        return signResult;
    }
}
