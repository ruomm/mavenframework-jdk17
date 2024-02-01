/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月21日 上午11:29:42
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class XMLParseJaxb {
    private final static Log log = LogFactory.getLog(XMLParseJaxb.class);

    @SuppressWarnings("unchecked")
    public static <T> T parseTextToXML(String xmlString, Class<T> cls) {
        if (null == cls) {
            log.error("Error:parseTextToXML->" + "传入待解析类型cls为空，无法进行XML解析");
            throw new RuntimeException("Error:XMLParseJaxb.parseTextToXML->传入待解析类型cls为空，无法进行XML解析");
        }
        if (null == xmlString || xmlString.length() <= 0) {
            log.error("Error:parseTextToXML->" + "传入待解析字符串xmlString为空，无法进行XML解析");
            throw new RuntimeException("Error:XMLParseJaxb.parseTextToXML->传入待解析字符串xmlString为空，无法进行XML解析");
        }
        try {

            StringReader stringReader = new StringReader(xmlString);
            JAXBContext cxt = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = cxt.createUnmarshaller();
//				unmarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
//				unmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
//				if (PARSE_FORMAT_XML_NOHEADER.equals(injectHttpParseFormat.format())) {
//					unmarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); // 是否省略xml头声明信息
//				}
            return (T) unmarshaller.unmarshal(stringReader);
        } catch (Exception e) {
            log.error("Error:parseTextToXML->" + "XML解析出现异常，请检查传入数据格式", e);
            throw new RuntimeException("Error:XMLParseJaxb.parseTextToXML->XML解析出现异常，请检查传入数据格式");
        }

    }
}
