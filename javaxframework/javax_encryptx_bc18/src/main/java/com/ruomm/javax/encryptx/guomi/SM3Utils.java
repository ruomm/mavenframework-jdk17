/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月1日 下午4:20:58
 */
package com.ruomm.javax.encryptx.guomi;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.bouncycastle.crypto.digests.SM3Digest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class SM3Utils {
    private final static Log log = LogFactory.getLog(SM3Utils.class);
    private final static boolean RESULT_UPPER_CASE = true;
    private final static int BUFFER_SIZE = 1024;

    public static String encodingSM3(String sourceString) {
        return encodingSM3(sourceString, null);
    }

    public static String encodingSM3(String sourceString, String charsetName) {
        if (null == sourceString) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return encodingDataSM3(sourceString.getBytes(charset));
        } catch (Exception e) {
            log.error("Error:encodingSM3", e);
            return null;
        }
    }

    public static String encodingDataSM3(byte[] sourceData) {
        if (null == sourceData) {
            return null;
        }
        String resultData;
        try {
            byte[] md = new byte[32];
            byte[] msg1 = sourceData;
            SM3Digest sm3 = new SM3Digest();
            sm3.update(msg1, 0, msg1.length);
            sm3.doFinal(md, 0);
            resultData = HexUtils.encodeHexStr(md, !RESULT_UPPER_CASE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            resultData = null;
            log.error("Error:encodingDataSM3", e);
        }
        return resultData;
    }

    public static String encodingInputStreamSM3(InputStream inputStream) {
        return encodingInputStreamSM3(inputStream, null, null, true);
    }

    public static String encodingInputStreamSM3(InputStream inputStream, String saltStr) {
        return encodingInputStreamSM3(inputStream, saltStr, null, true);
    }

    public static String encodingInputStreamSM3(InputStream inputStream, String saltStr, String charsetName) {
        return encodingInputStreamSM3(inputStream, saltStr, charsetName, true);
    }

    public static String encodingInputStreamSM3(InputStream inputStream, String saltStr, String charsetName, boolean isAutoClose) {
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
            byte[] md = new byte[32];
            SM3Digest sm3 = new SM3Digest();
            byte[] buffer = new byte[BUFFER_SIZE];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
                byte[] data = byteArrayOutputStream.toByteArray();
//				log.debug("--------");
//				log.debug(new String(data));
//				log.debug("--------");
                sm3.update(data, 0, data.length);
                byteArrayOutputStream.reset();
            }
            if (null != saltData && saltData.length > 0) {
                sm3.update(saltData, 0, saltData.length);
            }
            sm3.doFinal(md, 0);
            if (len < 0) {
                resultData = HexUtils.encodeHexStr(md, !RESULT_UPPER_CASE);
            } else {
                resultData = null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            resultData = null;
            log.error("Error:encodingInputStreamSM3", e);
        } finally {
            try {
                if (null != inputStream && isAutoClose) {
                    inputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:encodingInputStreamSM3", e);
            }
        }
        return resultData;
    }

}
