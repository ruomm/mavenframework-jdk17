package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.helper.EncoderHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.nio.charset.Charset;

public class SM2Helper {
    private final static Log log = LogFactory.getLog(SM2Helper.class);
    private final static int BUFFER_SIZE = 1024;
    private boolean isC1C3C2;

    private EncoderHelper dataEncoderHelper;
    private SM2KeyHelper sm2KeyHelper;
    public static SM2Helper gmHelper = new SM2Helper();

    public static SM2Helper getInstance() {
        return gmHelper;
    }

    public SM2Helper() {
        this(false, EncoderHelper.EncoderMode.BASE64, EncoderHelper.EncoderMode.BASE64);
    }

    public SM2Helper(boolean isC1C3C2) {

        this(isC1C3C2, EncoderHelper.EncoderMode.BASE64, EncoderHelper.EncoderMode.BASE64);
    }

    public SM2Helper(boolean isC1C3C2, EncoderHelper.EncoderMode keyEncoderMode, EncoderHelper.EncoderMode dataEncoderMode) {
        this.isC1C3C2 = isC1C3C2;
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

    public byte[] encryptData(byte[] data, byte[] publicKey) {
        return SM2EncDecUtils.encrypt(publicKey, data, isC1C3C2);
    }

    public byte[] encryptDataHard(byte[] data, byte[] publicKey) {
        return SM2EncDecUtils.encrypt(publicKey, data, isC1C3C2, true);
    }

    public byte[] decryptData(byte[] encryptedData, byte[] privateKey) {
        return SM2EncDecUtils.decrypt(privateKey, encryptedData, isC1C3C2);
    }

//    public byte[] sign(byte[] userId, byte[] sourceData, byte[] privateKey) {
//        byte[] signResult;
//        try {
//            SM2SignVO sm2SignVO = SM2SignVerUtils.Sign2SM2(privateKey, userId, sourceData);
//            signResult = Util.hexToByte(sm2SignVO.getSm2_signForSoft());
//        } catch (Exception e) {
//            signResult = null;
//            log.error("Error:signByInputStream", e);
//        }
//        return signResult;
//
//
//    }
//
//    public byte[] signByInputStream(byte[] userId, InputStream inputStream, byte[] privateKey,
//                                           boolean isAutoClose) {
//        byte[] signResult;
//        try {
//            SM2SignVO sm2SignVO = SM2SignVerUtils.Sign2SM2ByInputStream(privateKey, userId, inputStream);
//            signResult = Util.hexToByte(sm2SignVO.getSm2_signForSoft());
//        } catch (Exception e) {
//            signResult = null;
//            log.error("Error:signByInputStream", e);
//        } finally {
//            try {
//                if (null != inputStream && isAutoClose) {
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                log.error("Error:verifySign", e);
//            }
//        }
//        return signResult;
//
//    }
//
//    @SuppressWarnings("unchecked")
//    public boolean verifySign(byte[] userId, byte[] sourceData, byte[] signData, byte[] publicKey) {
//        SM2SignVO sm2SignVO = SM2SignVerUtils.VerifySignSM2(publicKey, userId, sourceData, signData);
//        return null == sm2SignVO ? false : sm2SignVO.isVerify();
//    }
//
//    @SuppressWarnings("unchecked")
//    public boolean verifySignByInputStream(byte[] userId, InputStream inputStream, byte[] signData, byte[] publicKey,
//                                                  boolean isAutoClose) {
//        boolean isPassed;
//        try {
//            SM2SignVO sm2SignVO = SM2SignVerUtils.VerifySignSM2ByInputStream(publicKey, userId, inputStream, signData);
//            isPassed = null == sm2SignVO ? false : sm2SignVO.isVerify();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            isPassed = false;
//            log.error("Error:verifySign", e);
//        } finally {
//            try {
//                if (null != inputStream && isAutoClose) {
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//                log.error("Error:verifySign", e);
//            }
//        }
//        return isPassed;
//    }

    public String encryptDataByPublicKey(String sourceData, String pubkS, String charsetName) {
        if (null == sourceData) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] cipherText = encryptData(sourceData.getBytes(charset), sm2KeyHelper.loadPublicKey(pubkS));
            return dataEncoderHelper.encodeToString(cipherText);
        } catch (Exception e) {
            log.error("Error:encryptDataByPublicKey", e);
            return null;
        }
    }

    public String encryptDataByPublicKey(String sourceData, String pubkS) {
        return encryptDataByPublicKey(sourceData, pubkS, null);
    }

    public String encryptDataByPublicKeyHard(String sourceData, String pubkS, String charsetName) {
        if (null == sourceData) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] cipherText = encryptDataHard(sourceData.getBytes(charset), sm2KeyHelper.loadPublicKey(pubkS));
            return dataEncoderHelper.encodeToString(cipherText);
        } catch (Exception e) {
            log.error("Error:encryptDataByPublicKey", e);
            return null;
        }
    }

    public String encryptDataByPublicKeyHard(String sourceData, String pubkS) {
        return encryptDataByPublicKeyHard(sourceData, pubkS, null);
    }

    public String decryptDataByPrivateKey(String sourceData, String prikS, String charsetName) {
        if (null == sourceData) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] cipherText = decryptData(dataEncoderHelper.decode(sourceData), sm2KeyHelper.loadPrivateKey(prikS));
            return new String(cipherText, charset);
        } catch (Exception e) {
            log.error("Error:decryptDataByPrivateKey", e);
            return null;
        }
    }

    public String decryptDataByPrivateKey(String sourceData, String prikS) {
        return decryptDataByPrivateKey(sourceData, prikS, null);
    }

//    public String getSignData(String userId, String sourceData, String privateKey) {
//        return getSignData(userId, sourceData, privateKey, null);
//    }
//
//    public String getSignData(String userId, String sourceData, String privateKey, String charsetName) {
//        if (null == sourceData) {
//            return null;
//        }
//        String signStr;
//        try {
//            Charset charset = CharsetUtils.parseRealCharset(charsetName,Charset.forName("UTF-8"));
//            byte[] cipherText = sign(userId.getBytes(charset), sourceData.getBytes(charset), SM2KeyUtils.loadPrivateKey(privateKey));
//            signStr = encoderHelper.encodeToString(cipherText);
//        } catch (Exception e) {
//            signStr = null;
//            log.error("Error:getSignData", e);
//        }
//        return signStr;
//    }
//
//    public String getSignByInputStream(String userId, InputStream inputStream, String privateKey) {
//        return getSignByInputStream(userId, inputStream, privateKey, null);
//    }
//
//    public String getSignByInputStream(String userId, InputStream inputStream, String privateKey,
//                                              String charsetName) {
//        String signStr;
//        try {
//            if (null == inputStream) {
//                return null;
//            }
//            Charset charset = CharsetUtils.parseRealCharset(charsetName,Charset.forName("UTF-8"));
//            // 得到base64编码的签名后的字段
//            signStr = encoderHelper.encodeToString(
//                    signByInputStream(userId.getBytes(charset), inputStream, SM2KeyUtils.loadPrivateKey(privateKey), true));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            signStr = null;
//            log.error("Error:getSignByInputStream", e);
//        }
//        return signStr;
//    }
//
//    public boolean verifySignData(String userId, String sourceData, String signData, String publicKey) {
//        return verifySignData(userId, sourceData, signData, publicKey, null);
//    }
//
//    public boolean verifySignData(String userId, String sourceData, String signData, String publicKey,
//                                         String charsetName) {
//        if (null == sourceData) {
//            if (signData == null) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        try {
//            Charset charset = CharsetUtils.parseRealCharset(charsetName,Charset.forName("UTF-8"));
//            return verifySign(userId.getBytes(charset), sourceData.getBytes(charset), encoderHelper.decode(signData),
//                    SM2KeyUtils.loadPublicKey(publicKey));
//        } catch (Exception e) {
//            // TODO: handle exception
//            log.error("Error:verifySignData", e);
//            return false;
//        }
//    }
//
//    public boolean verifySignInputStream(String userId, InputStream inputStream, String signData,
//                                                String publicKey) {
//        return verifySignInputStream(userId, inputStream, signData, publicKey, null);
//    }
//
//    public boolean verifySignInputStream(String userId, InputStream inputStream, String signData,
//                                                String publicKey, String charsetName) {
//        boolean signResult;
//        try {
//            Charset charset = CharsetUtils.parseRealCharset(charsetName,Charset.forName("UTF-8"));
//            signResult = verifySignByInputStream(userId.getBytes(charset), inputStream, encoderHelper.decode(signData),
//                    SM2KeyUtils.loadPublicKey(publicKey), true);
//        } catch (Exception e) {
//            // TODO: handle exception
//            signResult = false;
//            log.error("Error:verifySignInputStream", e);
//        }
//        return signResult;
//    }

}
