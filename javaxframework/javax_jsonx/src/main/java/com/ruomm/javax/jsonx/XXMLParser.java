/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月5日 下午3:04:30
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

public class XXMLParser {
    private final static Log log = LogFactory.getLog(XXMLParser.class);
    private XXMLParserHelper parserHelper = null;

    public XXMLParser(XXMLParserHelper parserHelper) {
        super();
        this.parserHelper = parserHelper;
    }

    public XXMLParserHelper getParserHelper() {
        return parserHelper;
    }

    public void setParserHelper(XXMLParserHelper parserHelper) {
        this.parserHelper = parserHelper;
    }

    public <T> T parseTextToXML(String xmlString, Class<T> cls) {
        if (null == parserHelper) {

            try {
                return XMLParseCommon.parseTextToXML(xmlString, cls);
            } catch (Exception e) {
                log.error("Error:parseTextToXML->" + "XML解析出现异常，请检查传入数据格式", e);
                return null;
            }
        } else {
            try {
                return parserHelper.parseTextToXML(xmlString, cls);
            } catch (Exception e) {
                log.error("Error:parseTextToXML->" + "XML解析出现异常，请检查传入数据格式", e);
                return null;
            }
        }

    }

}
