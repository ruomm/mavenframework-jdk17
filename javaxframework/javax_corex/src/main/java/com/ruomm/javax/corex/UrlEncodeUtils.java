package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/27 22:53
 */
public class UrlEncodeUtils {

    private final static Log log = LogFactory.getLog(UrlEncodeUtils.class);

    /**
     * 字符串进行编码
     *
     * @param str         待编码的字符串
     * @param charsetName charset for Source String getByte
     * @return String 编码结果
     */
    public static String encodeString(String str, String charsetName) {
        if (null == str) {
            return null;
        }
        String encodeVal = null;
        try {
            String charset = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
            encodeVal = URLEncoder.encode(str, charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encodeString", e);
            encodeVal = null;
        }
        return encodeVal;
    }

    /**
     * 字符串进行编码
     *
     * @param str 待编码的字符串
     * @return String 编码结果
     * @paramDefault charsetName charset for Source String getByte
     */
    public static String encodeString(String str) {
        return encodeString(str, null);
    }


    /**
     * 字符串进行解码
     *
     * @param encodedStr  待解码的字符串
     * @param charsetName charset for byte Array to Destination String
     * @return String 解码结果
     */
    public static String decodeString(String encodedStr, String charsetName) {
        if (null == encodedStr) {
            return null;
        }
        String decodeStr = null;
        try {
            String decodeCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
            decodeStr = URLDecoder.decode(encodedStr, decodeCharsetName);
        } catch (Exception e) {
            log.error("Error:decodeString", e);
            decodeStr = null;
        }
        return decodeStr;
    }

    /**
     * 字符串进行解码
     *
     * @param encodedStr 待解码的字符串
     * @return String 解码结果
     * @paramDefault charsetName charset for byte Array to Destination String
     */
    public static String decodeString(String encodedStr) {
        return decodeString(encodedStr, null);
    }


    /**
     * 字节数组byte[]进行编码
     *
     * @param b 待编码的节数组byte[]
     * @return String 编码结果
     */
    public static String encodeToString(byte[] b) {
        if (null == b) {
            return null;
        }
        String encodeVal = null;
        try {
//            encodeVal = URLEncoder.encode(new String(b, "UTF-8"), "UTF-8");
            encodeVal = URLEncoder.encode(Base64Utils.encodeToString(b), "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:encodeToString", e);
            encodeVal = null;
        }
        return encodeVal;
    }

    /**
     * 字节数组byte[]进行解码
     *
     * @param src 待编码的字符串
     * @return src 解码结果
     */
    public static byte[] decode(String src) {
        if (null == src) {
            return null;
        }
        byte[] decodeByte = null;
        try {
            String decodeStr = URLDecoder.decode(src, "UTF-8").toUpperCase();
//            decodeByte = decodeStr.getBytes("UTF-8");
            decodeByte = Base64Utils.decode(decodeStr);
        } catch (Exception e) {
            log.error("Error:decodeByte", e);
            decodeByte = null;
        }
        return decodeByte;
    }
}
