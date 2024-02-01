/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月3日 下午12:15:02
 */
package com.ruomm.javax.propertiesx.loader;

import com.ruomm.javax.propertiesx.PropertyReaderCharset;

import java.util.Map;

public class PropertyLoadUtil {
    public Map<String, String> loadProperty(String loadFile) {
        return loadProperty(loadFile, null, null, PropertyReaderCharset.DEFAULT_EMPTY_AS_NULL);
    }

    public Map<String, String> loadProperty(String loadFile, PropertyLoaderConfigHelper loaderConfigHelper) {
        return loadProperty(loadFile, loaderConfigHelper, null, PropertyReaderCharset.DEFAULT_EMPTY_AS_NULL);
    }

    public Map<String, String> loadProperty(String loadFile, PropertyLoaderConfigHelper loaderConfigHelper,
                                            String loaderCharsetName) {
        return loadProperty(loadFile, loaderConfigHelper, loaderCharsetName,
                PropertyReaderCharset.DEFAULT_EMPTY_AS_NULL);
    }

    public Map<String, String> loadProperty(String loadFile, PropertyLoaderConfigHelper loaderConfigHelper,
                                            String loaderCharsetName, boolean emptyAsNull) {
        PropertyLoaderConfigHelper realHelper = null;
        if (null == loaderConfigHelper) {
            realHelper = new PropertyLoaderConfig();
        } else {
            realHelper = loaderConfigHelper;
        }
        String realCharsetName = null;
        if (null == loaderCharsetName || loaderCharsetName.length() <= 0) {
            realCharsetName = "UTF-8";
        } else {
            realCharsetName = loaderCharsetName;
        }
        PropertyLoaderReuslt propertyLoaderReuslt = PropertyLoadCoreUtil.loadProperty(loadFile, realHelper,
                realCharsetName, emptyAsNull);
        return null == propertyLoaderReuslt ? null : propertyLoaderReuslt.getProperyMap();
    }
}
