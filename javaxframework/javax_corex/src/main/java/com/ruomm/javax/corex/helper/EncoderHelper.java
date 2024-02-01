package com.ruomm.javax.corex.helper;

import com.ruomm.javax.corex.Base64Utils;
import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.UrlEncodeUtils;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/27 21:52
 */
public class EncoderHelper {

    public static enum EncoderMode {
        BASE64, HEX_LOWER, HEX_UPPER, URL_ENCODE
    }

    private EncoderMode encoderMode;

    public EncoderHelper(EncoderMode encoderMode) {
        this.encoderMode = encoderMode;
    }

    /**
     * 字符串进行编码
     *
     * @param str         待编码的字符串
     * @param charsetName charset for Source String getByte
     * @return String 编码结果
     */
    public String encodeString(String str, String charsetName) {
        if (EncoderMode.BASE64 == this.encoderMode) {
            return Base64Utils.encodeString(str, charsetName);
        } else if (EncoderMode.HEX_UPPER == this.encoderMode) {
            return StringUtils.toUpperCase(HexUtils.encodeString(str, charsetName));
        } else if (EncoderMode.HEX_LOWER == this.encoderMode) {
            return HexUtils.encodeString(str, charsetName);
        } else if (EncoderMode.URL_ENCODE == this.encoderMode) {
            return UrlEncodeUtils.encodeString(str, charsetName);
        } else {
            return Base64Utils.encodeString(str, charsetName);
        }
    }

    /**
     * 字符串进行编码
     *
     * @param str 待编码的字符串
     * @return String 编码结果
     * @paramDefault charsetName charset for Source String getByte
     */
    public String encodeString(String str) {
        if (EncoderMode.BASE64 == this.encoderMode) {
            return Base64Utils.encodeString(str);
        } else if (EncoderMode.HEX_UPPER == this.encoderMode) {
            return StringUtils.toUpperCase(HexUtils.encodeString(str));
        } else if (EncoderMode.HEX_LOWER == this.encoderMode) {
            return HexUtils.encodeString(str);
        } else if (EncoderMode.URL_ENCODE == this.encoderMode) {
            return UrlEncodeUtils.encodeString(str);
        } else {
            return Base64Utils.encodeString(str);
        }
    }


    /**
     * 字符串进行解码
     *
     * @param encodedStr  待解码的字符串
     * @param charsetName charset for byte Array to Destination String
     * @return String 解码结果
     */
    public String decodeString(String encodedStr, String charsetName) {
        if (EncoderMode.BASE64 == this.encoderMode) {
            return Base64Utils.decodeString(encodedStr, charsetName);
        } else if (EncoderMode.HEX_UPPER == this.encoderMode) {
            return HexUtils.decodeString(encodedStr, charsetName);
        } else if (EncoderMode.HEX_LOWER == this.encoderMode) {
            return HexUtils.decodeString(encodedStr, charsetName);
        } else if (EncoderMode.URL_ENCODE == this.encoderMode) {
            return UrlEncodeUtils.decodeString(encodedStr, charsetName);
        } else {
            return Base64Utils.decodeString(encodedStr, charsetName);
        }
    }

    /**
     * 字符串进行解码
     *
     * @param encodedStr 待解码的字符串
     * @return String 解码结果
     * @paramDefault charsetName charset for byte Array to Destination String
     */
    public String decodeString(String encodedStr) {
        if (EncoderMode.BASE64 == this.encoderMode) {
            return Base64Utils.decodeString(encodedStr);
        } else if (EncoderMode.HEX_UPPER == this.encoderMode) {
            return HexUtils.decodeString(encodedStr);
        } else if (EncoderMode.HEX_LOWER == this.encoderMode) {
            return HexUtils.decodeString(encodedStr);
        } else if (EncoderMode.URL_ENCODE == this.encoderMode) {
            return UrlEncodeUtils.decodeString(encodedStr);
        } else {
            return Base64Utils.decodeString(encodedStr);
        }
    }


    /**
     * 字节数组byte[]进行编码
     *
     * @param b 待编码的节数组byte[]
     * @return String 编码结果
     */
    public String encodeToString(byte[] b) {
        if (EncoderMode.BASE64 == this.encoderMode) {
            return Base64Utils.encodeToString(b);
        } else if (EncoderMode.HEX_UPPER == this.encoderMode) {
            return StringUtils.toUpperCase(HexUtils.encodeToString(b));
        } else if (EncoderMode.HEX_LOWER == this.encoderMode) {
            return HexUtils.encodeToString(b);
        } else if (EncoderMode.URL_ENCODE == this.encoderMode) {
            return UrlEncodeUtils.encodeToString(b);
        } else {
            return Base64Utils.encodeToString(b);
        }
    }

    /**
     * 字节数组byte[]进行解码
     *
     * @param src 待编码的字符串
     * @return src 解码结果
     */
    public byte[] decode(String src) {

        if (EncoderMode.BASE64 == this.encoderMode) {
            return Base64Utils.decode(src);
        } else if (EncoderMode.HEX_UPPER == this.encoderMode) {
            return HexUtils.decode(src);
        } else if (EncoderMode.HEX_LOWER == this.encoderMode) {
            return HexUtils.decode(src);
        } else if (EncoderMode.URL_ENCODE == this.encoderMode) {
            return UrlEncodeUtils.decode(src);
        } else {
            return Base64Utils.decode(src);
        }
    }

}
