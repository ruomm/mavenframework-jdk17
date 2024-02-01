/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月4日 上午11:57:58
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.StringUtils;

import java.util.Map;

public class PropertyReaderCommon implements PropertyReaderCommonHelper {
    PropertyReaderCommonHelper propertyReaderCommonHelper = null;

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @paramDefault charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCommon(String loadPath) {
        super();
        if (StringUtils.isEmpty(loadPath)) {
            propertyReaderCommonHelper = new PropertyReaderCharset(loadPath);
        } else {
            String loadPathLowerCase = loadPath.toLowerCase();
            if (loadPathLowerCase.endsWith(".xml")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetXml(loadPath);
            } else if (loadPathLowerCase.endsWith(".json")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetJson(loadPath);
            } else if (loadPathLowerCase.endsWith(".yml")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetSpring(loadPath);
            } else if (loadPathLowerCase.endsWith(".properties")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetSpring(loadPath);
            } else {
                propertyReaderCommonHelper = new PropertyReaderCharset(loadPath);
            }
        }
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCommon(String loadPath, String charsetName) {
        super();
        if (StringUtils.isEmpty(loadPath)) {
            propertyReaderCommonHelper = new PropertyReaderCharset(loadPath, charsetName);
        } else {
            String loadPathLowerCase = loadPath.toLowerCase();
            if (loadPathLowerCase.endsWith(".xml")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetXml(loadPath, charsetName);
            } else if (loadPathLowerCase.endsWith(".json")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetJson(loadPath, charsetName);
            } else if (loadPathLowerCase.endsWith(".yml")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetSpring(loadPath, charsetName);
            } else if (loadPathLowerCase.endsWith(".properties")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetSpring(loadPath, charsetName);
            } else {
                propertyReaderCommonHelper = new PropertyReaderCharset(loadPath, charsetName);
            }
        }
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @param emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCommon(String loadPath, String charsetName, boolean emptyAsNull) {
        super();
        if (StringUtils.isEmpty(loadPath)) {
            propertyReaderCommonHelper = new PropertyReaderCharset(loadPath, charsetName, emptyAsNull);
        } else {
            String loadPathLowerCase = loadPath.toLowerCase();
            if (loadPathLowerCase.endsWith(".xml")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetXml(loadPath, charsetName, emptyAsNull);
            } else if (loadPathLowerCase.endsWith(".json")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetJson(loadPath, charsetName, false);
            } else if (loadPathLowerCase.endsWith(".yml")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetSpring(loadPath, charsetName);
            } else if (loadPathLowerCase.endsWith(".properties")) {
                propertyReaderCommonHelper = new PropertyReaderCharsetSpring(loadPath, charsetName);
            } else {
                propertyReaderCommonHelper = new PropertyReaderCharset(loadPath, charsetName, emptyAsNull);
            }
        }
    }

    @Override
    public String getProperty(String key) {
        // TODO Auto-generated method stub
        return propertyReaderCommonHelper.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        // TODO Auto-generated method stub
        return propertyReaderCommonHelper.getProperty(key, defaultValue);
    }

    @Override
    public void loadProps(boolean isForce) {
        propertyReaderCommonHelper.loadProps(isForce);
    }

    @Override
    public String propertyToString() {
        // TODO Auto-generated method stub
        return propertyReaderCommonHelper.propertyToString();
    }

    @Override
    public Map<String, String> propertyToMap() {
        // TODO Auto-generated method stub
        return propertyReaderCommonHelper.propertyToMap();
    }

    @Override
    public boolean putProperty(String key, String value) {
        return propertyReaderCommonHelper.putProperty(key, value);
    }

    @Override
    public boolean removeProperty(String key) {
        return propertyReaderCommonHelper.removeProperty(key);
    }

    @Override
    public boolean storeProperty(String comments) {
        return propertyReaderCommonHelper.storeProperty(comments);
    }
}
