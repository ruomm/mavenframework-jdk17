/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月4日 上午10:55:30
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyReaderCharsetSpring implements PropertyReaderCommonHelper {
    public final static boolean DEFAULT_EMPTY_AS_NULL = true;
    private final static Log log = LogFactory.getLog(PropertyReaderCharsetSpring.class);
    Properties mProperties = null;
    private String loadPath;
    private String loadCharsetName;
    private String savePath = null;
    private String saveCharsetName = null;

    /**
     * @param loadPath 配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @paramDefault charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetSpring(String loadPath) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = "UTF-8";
    }

    /**
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetSpring(String loadPath, String charsetName) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
    }

    public String getLoadPath() {
        return loadPath;
    }

    public void setLoadPath(String loadPath) {
        this.loadPath = loadPath;
    }

    public String getLoadCharsetName() {
        return loadCharsetName;
    }

    public void setLoadCharsetName(String charsetName) {
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSaveCharsetName() {
        return saveCharsetName;
    }

    public void setSaveCharsetName(String saveCharsetName) {
        this.saveCharsetName = saveCharsetName;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (null == mProperties) {
            loadProps(false);
        }
        if (null == mProperties) {
            log.debug("配置获取失败，配置没有正确加载，返回默认值->" + "key: " + key + " ;value: " + defaultValue);
            return defaultValue;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("配置获取失败，输入的key为空，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        String resultVal = mProperties.getProperty(realKey);
        if (null == resultVal) {
            log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        } else {
            log.debug("获取配置成功->" + "key: " + realKey + " ;value: " + resultVal);
            return resultVal;
        }
    }

    @Override
    public String propertyToString() {
        if (null == mProperties) {
            loadProps(false);
        }
        if (null == mProperties) {
            return null;
        }
        return mProperties.toString();
    }

    @Override
    public Map<String, String> propertyToMap() {
        if (null == mProperties) {
            loadProps(false);
        }
        if (null == mProperties) {
            return null;
        }
        try {
            Map<String, String> map = new HashMap<String, String>();
            for (Object itemKeyObj : mProperties.keySet()) {
                if (null == itemKeyObj) {
                    continue;
                }
                String itemKeyStr = itemKeyObj instanceof String ? (String) itemKeyObj : String.valueOf(itemKeyObj);
                if (StringUtils.isEmpty(itemKeyStr)) {
                    continue;
                }
                map.put(itemKeyStr, mProperties.getProperty(itemKeyStr));
            }
            log.debug("转换后的Map数据为->" + (null == map ? "NULL" : map.toString()));
            return map;
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharset.propertyToMap", e);
            return null;
        }
    }

    /**
     * 读取配置文件
     *
     * @param isForce
     */
    @Override
    synchronized public void loadProps(boolean isForce) {
        if (null == loadPath || loadPath.length() == 0) {
            mProperties = null;

        }
        if (!isForce && null != mProperties) {
            return;
        }
        try {
            log.debug("开始加载配置文件->" + loadPath);
            String loadPathReal = WebUtils.parseRealPath(loadPath);
            File file = new File(loadPathReal);
            if (null == file || !file.exists()) {
                mProperties = new Properties();
                log.error("Error:PropertyReaderCharset.loadProps->加载配置文件失败，配置文件不存在！");
                return;
            }
            mProperties = SpringBootPropertyUtil.load(loadPath);
            log.debug("加载配置文件成功->" + mProperties);
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharset.loadProps", e);
            throw new RuntimeException("PropertyReaderCharset.loadProps->加载配置文件出现异常！", e);
        }
    }

    @Override
    public boolean putProperty(String key, String value) {
        if (null == mProperties) {
            loadProps(false);
        }
        if (null == mProperties) {
            log.debug("添加配置失败，配置没有正确加载->" + "key: " + key + " ;value: " + value + " ;result: false");
            return false;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("添加配置失败，输入的key为空->" + "key: " + key + " ;value: " + value + " ;result: false");
            return false;
        }
        try {
            mProperties.put(realKey, value);
            log.debug("添加配置成功->" + "key: " + key + " ;value: " + value + " ;result: true");
            return true;
        } catch (Exception e) {
            log.debug("添加配置失败，存在异常->" + key + " ;value: " + value + " ;result: false");
            log.debug("Error:PropertyReaderCharset.putProperty", e);
            return false;
        }
    }

    @Override
    public boolean removeProperty(String key) {
        if (null == mProperties) {
            loadProps(false);
        }
        if (null == mProperties) {
            log.debug("删除配置失败，配置没有正确加载->" + "key: " + key + " ;result: false");
            return false;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("删除配置失败，输入的key为空->" + "key: " + key + " ;result: false");
            return false;
        }
        try {
            mProperties.remove(realKey);
            log.debug("删除配置成功->" + "key: " + key + " ;result: true");
            return true;
        } catch (Exception e) {
            log.debug("删除配置失败，存在异常->" + key + " ;result: false");
            log.debug("Error:PropertyReaderCharset.removeProperty", e);
            return false;
        }

    }

    @Override
    public boolean storeProperty(String comments) {
        return storeProperty(savePath, comments, saveCharsetName);
    }

    private boolean storeProperty(String path, String comments, String charsetName) {
        if (null == mProperties) {
            return false;
        }
        String outPath = null;
        if (null == path || path.length() <= 0) {
            outPath = WebUtils.parseRealPath(loadPath);
        } else {
            outPath = WebUtils.parseRealPath(path);
        }
//        String outCharsetName = null;
//        if (null == charsetName || charsetName.length() <= 0) {
//            outCharsetName = loadCharsetName;
//        } else {
//            outCharsetName = charsetName;
//        }
        if (StringUtils.isEmpty(outPath)) {
            return false;
        }
        boolean storeResult = false;
        BufferedOutputStream bufOut = null;
        try {

            String realComments;
            if (StringUtils.isEmpty(comments)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                realComments = "www.ruomm.com - " + sdf.format(new Date());
            } else {
                realComments = comments;
            }
            bufOut = new BufferedOutputStream(new FileOutputStream(outPath));
            mProperties.store(bufOut, realComments);
            bufOut.flush();
            storeResult = true;
        } catch (Exception e) {
            log.error("Error:storeProperty", e);
        } finally {
            try {
                if (null != bufOut) {
                    bufOut.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:storeProperty", e);
            }
        }
        return storeResult;
    }

}
