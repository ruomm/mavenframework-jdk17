/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月2日 下午6:37:48
 */
package com.ruomm.javax.propertiesx.loader;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.propertiesx.PropertyReaderCharset;

import java.util.Map;

public class PropertyLoaderHelper {
    private final static Log log = LogFactory.getLog(PropertyLoadCoreUtil.class);
    private final static String TAG_CLS = PropertyLoaderHelper.class.getSimpleName() + ".";
    private String loadFile;
    private PropertyLoaderConfigHelper loaderConfigHelper;
    private String loaderCharsetName;
    private Map<String, String> propertyMaps = null;
    private String active = null;
    private String append = null;
    private String inclues = null;
    private boolean emptyAsNull = false;

    public PropertyLoaderHelper(String loadFile) {
        this(loadFile, null, null, PropertyReaderCharset.DEFAULT_EMPTY_AS_NULL);
    }

    public PropertyLoaderHelper(String loadFile, PropertyLoaderConfigHelper loaderConfigHelper) {
        this(loadFile, loaderConfigHelper, null, PropertyReaderCharset.DEFAULT_EMPTY_AS_NULL);
    }

    public PropertyLoaderHelper(String loadFile, PropertyLoaderConfigHelper loaderConfigHelper, String charsetName) {
        this(loadFile, loaderConfigHelper, charsetName, PropertyReaderCharset.DEFAULT_EMPTY_AS_NULL);
    }

    public PropertyLoaderHelper(String loadFile, PropertyLoaderConfigHelper loaderConfigHelper,
                                String loaderCharsetName, boolean emptyAsNull) {
        super();
        this.loadFile = loadFile;
        if (null == loaderConfigHelper) {
            this.loaderConfigHelper = new PropertyLoaderConfig();
        } else {
            this.loaderConfigHelper = loaderConfigHelper;
        }
        if (null == loaderCharsetName || loaderCharsetName.length() <= 0) {
            this.loaderCharsetName = "UTF-8";
        } else {
            this.loaderCharsetName = loaderCharsetName;
        }
        this.emptyAsNull = emptyAsNull;
    }

    synchronized public void loadProps(boolean isForce) {
        if (isForce || null == propertyMaps) {
            // 还原数据
            this.propertyMaps = null;
            if (null != this.loadFile && this.loadFile.length() > 0) {
                loadProperty();
            }
        }
        if (null == propertyMaps) {
            log.debug(TAG_CLS + "loadProps->" + "配置文件读取失败，数据为空");
        } else {
            log.debug(TAG_CLS + "loadProps->" + "配置文件读取成功");
        }
    }

    public String getActiveMode() {
        if (null == propertyMaps) {
            loadProps(false);
        }
        return this.active;
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        if (null == propertyMaps) {
            loadProps(false);
        }
        if (null == propertyMaps) {
            return defaultValue;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            return defaultValue;
        }
        String resultVal = propertyMaps.get(realKey);
        if (null == resultVal) {
            return defaultValue;
        } else {
            return resultVal;
        }
    }

    public Map<String, String> propertyToMap() {
        if (null == propertyMaps) {
            loadProps(false);
        }
        return propertyMaps;
    }

    private void loadProperty() {
        PropertyLoaderReuslt propertyLoaderReuslt = PropertyLoadCoreUtil.loadProperty(loadFile, loaderConfigHelper,
                loaderCharsetName, emptyAsNull);
        if (null == propertyLoaderReuslt) {
            return;
        }
        this.active = propertyLoaderReuslt.getActive();
        this.append = propertyLoaderReuslt.getAppend();
        this.inclues = propertyLoaderReuslt.getInclues();
        this.propertyMaps = propertyLoaderReuslt.getProperyMap();
    }
}
