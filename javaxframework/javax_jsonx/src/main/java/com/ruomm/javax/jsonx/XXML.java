/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月5日 下午3:12:51
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

public class XXML {
    private final static Log log = LogFactory.getLog(XXML.class);

    public static <T> T parseTextToXML(String xmlString, Class<T> cls) {

        try {
            return XMLParseCommon.parseTextToXML(xmlString, cls);
        } catch (Exception e) {
            log.error("Error:parseTextToXML->" + "XML解析出现异常，请检查传入数据格式", e);
            return null;
        }
    }
}
