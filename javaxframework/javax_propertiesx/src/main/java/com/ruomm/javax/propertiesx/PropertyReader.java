/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月4日 上午10:55:30
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertyReader {
    private final static Log log = LogFactory.getLog(PropertyReader.class);
    private Properties props;
    private String loadPath;

    public PropertyReader(String loadPath) {
        super();
        this.loadPath = loadPath;
    }

    public String getLoadPath() {
        return loadPath;
    }

    public void setLoadPath(String loadPath) {
        this.loadPath = loadPath;
    }

    synchronized public void loadProps(boolean isForce) {
        if (null == loadPath || loadPath.length() == 0) {
            props = null;

        }
        if (!isForce && null != props) {
            return;
        }
        props = new Properties();
        InputStream in = null;
        try {
            String loadPathReal = WebUtils.parseRealPath(loadPath);
            in = new FileInputStream(loadPathReal);
            props.load(in);
        } catch (Exception e) {
            log.error("Error:loadProps", e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("Error:loadProps", e);
            }
        }
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps(false);
        }
        if (null == props) {
            return defaultValue;
        }
        return props.getProperty(key, defaultValue);
    }

    public boolean putProperty(String key, String value) {
        if (null == props) {
            loadProps(false);
        }
        if (null == props) {
            return false;
        }
        try {
            if (null == value) {
                props.put(key, "");
            } else {
                props.put(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Error:putProperty", e);
            return false;
        }

    }

    public String propertyToString() {
        if (null == props) {
            loadProps(false);
        }
        if (null == props) {
            return null;
        }
        return props.toString();
    }

    public boolean storeProperty(String comments) {
        if (null == props) {
            loadProps(true);
        }
        if (null == props) {
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            String loadPathReal = WebUtils.parseRealPath(loadPath);
            fileOutputStream = new FileOutputStream(loadPathReal);
            props.store(fileOutputStream, comments);
            return true;
        } catch (Exception e) {
            log.error("Error:storeProperty", e);
            return false;
        }
    }

    public Map<String, String> propertyToMap() {
        if (null == props) {
            loadProps(false);
        }
        if (null == props) {
            return null;
        }
        try {
            Map<String, String> map = new HashMap<String, String>();
            Set<Object> keySets = props.keySet();
            for (Object keyObject : keySets) {
                String key = (String) keyObject;
                String val = props.getProperty(key);
//				key = new String(key.getBytes("ISO-8859-1"), "utf-8");
//				val = new String(val.getBytes("ISO-8859-1"), "utf-8");
                map.put(key, val);
            }
            return map;
        } catch (Exception e) {
            log.error("Error:propertyToMap", e);
            return null;
        }

    }

}
