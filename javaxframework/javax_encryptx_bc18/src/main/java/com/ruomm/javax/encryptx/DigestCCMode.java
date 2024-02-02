package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/13 10:20
 */
class DigestCCMode {
    private final static Log log = LogFactory.getLog(DigestCCMode.class);
    public final static String METHOD_MD2 = "MD2";
    public final static String METHOD_MD5 = "MD5";
    public final static String METHOD_SHA1 = "SHA-1";
    public final static String METHOD_SHA256 = "SHA-256";
    public final static String METHOD_SHA384 = "SHA-384";
    public final static String METHOD_SHA512 = "SHA-512";
    public final static String METHOD_SHA3_256 = "SHA3-256";

    public static String encodingByMessageDigest(String sourceString, String digestMethod, boolean RESULT_UPPER_CASE) {
        return encodingByMessageDigest(sourceString, digestMethod, null, RESULT_UPPER_CASE);
    }

    public static String encodingByMessageDigest(String sourceString, String digestMethod, String charsetName, boolean RESULT_UPPER_CASE) {
        if (null == sourceString) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return getMessageDigestByCCMode(sourceString.getBytes(charset), digestMethod, RESULT_UPPER_CASE);
        } catch (Exception e) {
            log.error("Error:encodingByMessageDigest", e);
            return null;
        }
    }

    public static String getMessageDigestByCCMode(byte[] sourceData, String digestMethod, boolean RESULT_UPPER_CASE) {
        if (null == sourceData) {
            return null;
        }
        String resultData;
        try {
            if (isMethodEqual(METHOD_MD2, digestMethod)) {
                resultData = DigestUtils.md2Hex(sourceData);
            } else if (isMethodEqual(METHOD_MD5, digestMethod)) {
                resultData = DigestUtils.md5Hex(sourceData);
            } else if (isMethodEqual(METHOD_SHA1, digestMethod)) {
                resultData = DigestUtils.sha1Hex(sourceData);
            } else if (isMethodEqual(METHOD_SHA256, digestMethod)) {
                resultData = DigestUtils.sha256Hex(sourceData);
            } else if (isMethodEqual(METHOD_SHA384, digestMethod)) {
                resultData = DigestUtils.sha384Hex(sourceData);
            } else if (isMethodEqual(METHOD_SHA512, digestMethod)) {
                resultData = DigestUtils.sha512Hex(sourceData);
            } else if (isMethodEqual(METHOD_SHA3_256, digestMethod)) {
                resultData = DigestUtils.sha3_256Hex(sourceData);
            } else {
                return null;
            }
            if (RESULT_UPPER_CASE) {
                resultData = resultData.toUpperCase();
            } else {
                resultData = resultData.toLowerCase();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getMessageDigestByCCMode", e);
            resultData = null;
        }
        return resultData;
    }

    public static String getMessageDigestForInputStreamByCCMode(InputStream sourceInputStream, String digestMethod, boolean RESULT_UPPER_CASE) {
        String resultData;
        try {
            if (isMethodEqual(METHOD_MD2, digestMethod)) {
                resultData = DigestUtils.md2Hex(sourceInputStream);
            } else if (isMethodEqual(METHOD_MD5, digestMethod)) {
                resultData = DigestUtils.md5Hex(sourceInputStream);
            } else if (isMethodEqual(METHOD_SHA1, digestMethod)) {
                resultData = DigestUtils.sha1Hex(sourceInputStream);
            } else if (isMethodEqual(METHOD_SHA256, digestMethod)) {
                resultData = DigestUtils.sha256Hex(sourceInputStream);
            } else if (isMethodEqual(METHOD_SHA384, digestMethod)) {
                resultData = DigestUtils.sha384Hex(sourceInputStream);
            } else if (isMethodEqual(METHOD_SHA512, digestMethod)) {
                resultData = DigestUtils.sha512Hex(sourceInputStream);
            } else if (isMethodEqual(METHOD_SHA3_256, digestMethod)) {
                resultData = DigestUtils.sha3_256Hex(sourceInputStream);
            } else {
                return null;
            }
            if (RESULT_UPPER_CASE) {
                resultData = resultData.toUpperCase();
            } else {
                resultData = resultData.toLowerCase();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getMessageDigestForInputStreamByCCMode", e);
            resultData = null;
        } finally {
            try {
                if (null != sourceInputStream) {
                    sourceInputStream.close();
                }
            } catch (Exception e) {
                log.error("Error:getMessageDigestForInputStreamByCCMode", e);
            }
        }
        return resultData;
    }


    private static boolean isMethodEqual(String methodSystem, String methodUser) {
        if (null == methodUser || null == methodSystem || methodUser.length() <= 0 || methodSystem.length() <= 0) {
            return false;
        }
        String tempSystem = methodSystem.replace("-", "").toUpperCase();
        String tempUser = methodUser.replace("-", "").toUpperCase();
        boolean flag = tempSystem.equals(tempUser);
        return flag;
    }


}
