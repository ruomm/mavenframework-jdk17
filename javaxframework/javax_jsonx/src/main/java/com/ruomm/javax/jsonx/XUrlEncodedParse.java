/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月31日 下午2:33:09
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class XUrlEncodedParse {
    private final static Log log = LogFactory.getLog(XUrlEncodedParse.class);

    /**
     * 解析JSON字符串为对象类型，无法解析抛出异常
     *
     * @param str         待解析JSON字符串
     * @param charsetName 待解析类型
     * @return
     */

    @SuppressWarnings("deprecation")
    public static Map<String, String> parseToMap(String str, String charsetName) {
        if (null == str || str.length() <= 0) {
            return null;
        }
        String[] paramsStr = str.split("&");
        if (null == paramsStr || paramsStr.length <= 0) {
            return null;
        }
        String realCharsetName = parseRealCharsetName(charsetName, null);
        Map<String, String> map = new HashMap<String, String>();
        for (String tmp : paramsStr) {
            try {
                if (null == tmp || tmp.length() <= 0) {
                    continue;
                }
                int appendIndex = tmp.indexOf("=");
                if (appendIndex <= 0 || appendIndex >= tmp.length() - 1) {
                    continue;
                }
                String key = tmp.substring(0, appendIndex);
                String val = tmp.substring(appendIndex + 1);
                if (null == realCharsetName || realCharsetName.length() <= 0) {
                    map.put(key, URLDecoder.decode(val));
                } else {
                    map.put(key, URLDecoder.decode(val, realCharsetName));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return map;
    }

    /**
     * 获取真实的String格式字符编码，若是找不到返回默认的String格式字符编码(defaultCharsetName)
     *
     * @param charsetName        输入的字符编码名称
     * @param defaultCharsetName 默认的String格式字符编码。默认值为null;
     * @return String格式字符编码
     */
    private static String parseRealCharsetName(String charsetName, String defaultCharsetName) {
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
}