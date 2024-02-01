/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月11日 下午6:44:31
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

public class XMLParseCommon {
    private final static Log log = LogFactory.getLog(XMLParseCommon.class);
    final static boolean isJaxb;
    final static boolean isXstream;

    static {
        boolean isJ = false;
        try {
            Class<?> cls = Class.forName("javax.xml.bind.Unmarshaller");
            if (null != cls) {
                isJ = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:XMLParseCommon.static->JAXBContext not found!", e);
        }
        isJaxb = isJ;
        boolean isX = false;
        try {
            Class<?> cls = Class.forName("com.thoughtworks.xstream.XStream");
            if (null != cls) {
                isX = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:XMLParseCommon.static->XStream not found!", e);
        }
        isXstream = isX;
        if (!isJaxb && !isXstream) {
            log.error(
                    "Error:JSONParseCommon.static->XXML、XXMLParser初始化错误，没有XML序列化工具，请引入至少一种XML序列化工具(JAXBContext、XStream)。");
        }
    }

    public static <T> T parseTextToXML(String xmlString, Class<T> cls) {
        if (null == cls) {
            log.error("Error:parseTextToXML->" + "传入待解析类型cls为空，无法进行XML解析");
            throw new RuntimeException("Error:XMLParseCommon.parseTextToXML->传入待解析类型cls为空，无法进行XML解析");
        }
        if (null == xmlString || xmlString.length() <= 0) {
            log.error("Error:parseTextToXML->" + "传入待解析字符串xmlString为空，无法进行XML解析");
            throw new RuntimeException("Error:XMLParseCommon.parseTextToXML->传入待解析字符串xmlString为空，无法进行XML解析");
        }
        if (isJaxb) {
            return XMLParseJaxb.parseTextToXML(xmlString, cls);
        } else if (isXstream) {
            return XMlParseXStream.parseTextToXML(xmlString, cls);
        } else {
            log.error("Error:parseTextToXML->" + "没有XML序列化工具，请引入JAXBContext或XStream包");
            throw new RuntimeException("Error:XMLParseCommon.parseTextToXML->没有XML序列化工具，请引入JAXBContext或XStream包");
        }

    }
}
