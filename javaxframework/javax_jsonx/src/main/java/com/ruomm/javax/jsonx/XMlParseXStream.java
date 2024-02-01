/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月21日 上午11:29:57
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XMlParseXStream {
    private final static Log log = LogFactory.getLog(XMlParseXStream.class);

    @SuppressWarnings("unchecked")
    public static <T> T parseTextToXML(String xmlString, Class<T> cls) {
        if (null == cls) {
            log.error("Error:parseTextToXML->" + "传入待解析类型cls为空，无法进行XML解析");
            throw new RuntimeException("Error:XMlParseXStream.parseTextToXML->传入待解析类型cls为空，无法进行XML解析");
        }
        if (null == xmlString || xmlString.length() <= 0) {
            log.error("Error:parseTextToXML->" + "传入待解析字符串xmlString为空，无法进行XML解析");
            throw new RuntimeException("Error:XMlParseXStream.parseTextToXML->传入待解析字符串xmlString为空，无法进行XML解析");
        }
        try {

            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(cls); // 类加载 必须
            return (T) xStream.fromXML(xmlString);

        } catch (Exception e) {
            log.error("Error:parseTextToXML->" + "XML解析出现异常，请检查传入数据格式", e);
            throw new RuntimeException("Error:XMlParseXStream.parseTextToXML->XML解析出现异常，请检查传入数据格式");
        }

    }
}
