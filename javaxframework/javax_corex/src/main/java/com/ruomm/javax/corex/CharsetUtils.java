package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.nio.charset.Charset;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/16 14:08
 */
public class CharsetUtils {
    private final static Log log = LogFactory.getLog(CharsetUtils.class);
    // Charset转换类

    /**
     * 获取真实的String格式字符编码，若是找不到返回默认的String格式字符编码(defaultCharsetName)
     *
     * @param charsetName 输入的字符编码名称
     * @return String格式字符编码
     * @paramDefault defaultCharsetName 默认的String格式字符编码。默认值为null;
     */
    public static String parseRealCharsetName(String charsetName) {
        return parseRealCharsetName(charsetName, null);
    }

    /**
     * 获取真实的String格式字符编码，若是找不到返回默认的String格式字符编码(defaultCharsetName)
     *
     * @param charsetName        输入的字符编码名称
     * @param defaultCharsetName 默认的String格式字符编码。默认值为null;
     * @return String格式字符编码
     */
    public static String parseRealCharsetName(String charsetName, String defaultCharsetName) {
        String realCharsetName = null;
        try {
            if (null == charsetName || charsetName.length() <= 0 || !Charset.isSupported(charsetName)) {
                realCharsetName = null;
            } else {
                realCharsetName = charsetName;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parseRealCharsetName", e);
            realCharsetName = null;
        }
        if (null == realCharsetName) {
            return defaultCharsetName;
        }
        return realCharsetName;
    }

    /**
     * 获取真实的Charset格式字符编码，若是找不到返回默认的Charset格式字符编码(defaultCharset)
     *
     * @param charsetName 输入的字符编码名称
     * @return Charset格式字符编码
     * @paramDefault defaultCharset 默认的Charset格式字符编码。默认值为null;
     */
    public static Charset parseRealCharset(String charsetName) {
        return parseRealCharset(charsetName, null);
    }

    /**
     * 获取真实的Charset格式字符编码，若是找不到返回默认的Charset格式字符编码(defaultCharset)
     *
     * @param charsetName    输入的字符编码名称
     * @param defaultCharset 默认的Charset格式字符编码。默认值为null;
     * @return Charset格式字符编码
     */
    public static Charset parseRealCharset(String charsetName, Charset defaultCharset) {
        Charset charset = null;
        try {
            if (null == charsetName || charsetName.length() <= 0 || !Charset.isSupported(charsetName.trim())) {
                charset = null;
            } else {
                charset = Charset.forName(charsetName);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parseRealCharset", e);
            charset = null;
        }
        if (null == charset) {
            return defaultCharset;
        }
        return charset;
    }
}
