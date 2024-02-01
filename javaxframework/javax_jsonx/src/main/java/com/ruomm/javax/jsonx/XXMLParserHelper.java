/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月5日 下午3:04:30
 */
package com.ruomm.javax.jsonx;

public interface XXMLParserHelper {
    public <T> T parseTextToXML(String xmlString, Class<T> cls);
}
