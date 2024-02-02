package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/13 10:20
 */
class DigestBCMode {
    private final static Log log = LogFactory.getLog(DigestBCMode.class);
    public final static String METHOD_MD2 = "MD2";
    public final static String METHOD_MD5 = "MD5";
    public final static String METHOD_SHA1 = "SHA-1";
    public final static String METHOD_SHA256 = "SHA-256";
    public final static String METHOD_SHA384 = "SHA-384";
    public final static String METHOD_SHA512 = "SHA-512";

    public static String getMessageDigestByBCMode(byte[] sourceData, String digestMethod, boolean RESULT_UPPER_CASE) {
        if (null == sourceData) {
            return null;
        }
        String resultData;
        try {
            Digest digest = null;
            if (isMethodEqual(METHOD_MD2, digestMethod)) {
                digest = new MD2Digest();
            } else if (isMethodEqual(METHOD_MD5, digestMethod)) {
                digest = new MD5Digest();
            } else if (isMethodEqual(METHOD_SHA1, digestMethod)) {
                digest = new SHA1Digest();
            } else if (isMethodEqual(METHOD_SHA256, digestMethod)) {
                digest = new SHA256Digest();
            } else if (isMethodEqual(METHOD_SHA384, digestMethod)) {
                digest = new SHA384Digest();
            } else if (isMethodEqual(METHOD_SHA512, digestMethod)) {
                digest = new SHA512Digest();
            } else {
                return null;
            }
            digest.update(sourceData, 0, sourceData.length);
            byte[] resultBytes = new byte[digest.getDigestSize()];
            digest.doFinal(resultBytes, 0);
            String resultTemp = HexUtils.encodeToString(resultBytes);
            if (RESULT_UPPER_CASE) {
                resultData = resultTemp.toUpperCase();
            } else {
                resultData = resultTemp;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:getMessageDigestByJDKMode", e);
            resultData = null;
        }
        return resultData;
    }

    public static String getMessageDigestForInputStreamByBCMode(InputStream inputStream, String saltStr,
                                                                String charsetName, String digestMethod, boolean RESULT_UPPER_CASE) {
        String resultData;
        try {
            if (null == inputStream) {
                return null;
            }

            byte[] saltData;
            if (null != saltStr && saltStr.length() > 0) {
                Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
                saltData = saltStr.getBytes(charset);
            } else {
                saltData = null;
            }

            Digest digest = null;
            if (isMethodEqual(METHOD_MD2, digestMethod)) {
                digest = new MD2Digest();
            } else if (isMethodEqual(METHOD_MD5, digestMethod)) {
                digest = new MD5Digest();
            } else if (isMethodEqual(METHOD_SHA1, digestMethod)) {
                digest = new SHA1Digest();
            } else if (isMethodEqual(METHOD_SHA256, digestMethod)) {
                digest = new SHA256Digest();
            } else if (isMethodEqual(METHOD_SHA384, digestMethod)) {
                digest = new SHA384Digest();
            } else if (isMethodEqual(METHOD_SHA512, digestMethod)) {
                digest = new SHA512Digest();
            }
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                digest.update(data, 0, data.length);
                byteArrayOutputStream.reset();
            }
            if (null != saltData && saltData.length > 0) {
                digest.update(saltData, 0, saltData.length);
            }
            byte[] resultBytes = new byte[digest.getDigestSize()];
            digest.doFinal(resultBytes, 0);
            String resultTemp = HexUtils.encodeToString(resultBytes);
            if (RESULT_UPPER_CASE) {
                resultData = resultTemp.toUpperCase();
            } else {
                resultData = resultTemp;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getMessageDigestForInputStreamByBCMode", e);
            resultData = null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:getMessageDigestForInputStreamByBCMode", e);
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
