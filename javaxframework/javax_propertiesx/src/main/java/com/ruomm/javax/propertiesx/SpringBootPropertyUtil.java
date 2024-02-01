package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.io.FileSystemResource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/11/11 14:38
 */
public class SpringBootPropertyUtil {
    private final static Log log = LogFactory.getLog(SpringBootPropertyUtil.class);

    /**
     * 加载配置文件
     *
     * @param extraPath 配置文件路径
     * @return 加载配置文件结果
     */
    public static Properties load(String extraPath) {
        if (null == extraPath || extraPath.length() <= 0) {
            return null;
        } else if (extraPath.toLowerCase().endsWith(".yml")) {
            return loadExtraYaml(extraPath);
        } else {
            return loadExtraProperties(extraPath);
        }
    }

    /**
     * 加载Properties配置文件
     *
     * @param extraPath 配置文件路径
     * @return 加载配置文件结果
     */
    private static Properties loadExtraProperties(String extraPath) {
        InputStream in = null;
        Properties properties;
        try {
            properties = new Properties();
            in = new FileInputStream(WebUtils.parseRealPath(extraPath));
            properties.load(in);
        } catch (Exception e) {
            log.error("Error:loadProperty", e);
            properties = null;
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                log.error("Error:loadProperty", e);
            }
        }
        return properties;

    }

    /**
     * 加载Yaml配置文件
     *
     * @param extraPath 配置文件路径
     * @return 加载配置文件结果
     */
    private static Properties loadExtraYaml(String extraPath) {
        FileSystemResource fileSystemResource = null;
        Properties properties;
        try {
            YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            fileSystemResource = new FileSystemResource(WebUtils.parseRealPath(extraPath));
            yaml.setResources(fileSystemResource);
            properties = yaml.getObject();
        } catch (Exception e) {
            log.error("Error:loadYaml", e);
            properties = null;
        } finally {
            fileSystemResource = null;
        }
        return properties;
    }


    /**
     * 读取Properties为字符串
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @return 读取结果
     * @paramDefault defaultVal 默认值
     */
    public static String getPropertyString(Properties properties, String key) {
        return getPropertyString(properties, key, null);
    }

    /**
     * 读取Properties为字符串
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static String getPropertyString(Properties properties, String key, String defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                return val;
            }
        }
    }

    /**
     * 读取Properties为Integer
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @return 读取结果
     * @paramDefault defaultVal 默认值
     */
    public static Integer getPropertyInteger(Properties properties, String key) {
        return getPropertyInteger(properties, key, null);
    }

    /**
     * 读取Properties为Integer
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Integer getPropertyInteger(Properties properties, String key, Integer defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Integer parseVal;
                try {
                    parseVal = Integer.parseInt(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyInteger", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Long
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @return 读取结果
     * @paramDefault defaultVal 默认值
     */
    public static Long getPropertyLong(Properties properties, String key) {
        return getPropertyLong(properties, key, null);
    }

    /**
     * 读取Properties为Long
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Long getPropertyLong(Properties properties, String key, Long defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Long parseVal;
                try {
                    parseVal = Long.parseLong(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyLong", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Float
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @return 读取结果
     * @paramDefault defaultVal 默认值
     */
    public static Float getPropertyFloat(Properties properties, String key) {
        return getPropertyFloat(properties, key, null);
    }

    /**
     * 读取Properties为Float
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Float getPropertyFloat(Properties properties, String key, Float defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Float parseVal;
                try {
                    parseVal = Float.parseFloat(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyFloat", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Double
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @return 读取结果
     * @paramDefault defaultVal 默认值
     */
    public static Double getPropertyDouble(Properties properties, String key) {
        return getPropertyDouble(properties, key, null);
    }

    /**
     * 读取Properties为Double
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Double getPropertyDouble(Properties properties, String key, Double defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Double parseVal;
                try {
                    parseVal = Double.parseDouble(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyDouble", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Boolean
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @return 读取结果
     * @paramDefault defaultVal 默认值
     */
    public static Boolean getPropertyBoolean(Properties properties, String key) {
        return getPropertyBoolean(properties, key, null);
    }

    /**
     * 读取Properties为Boolean
     *
     * @param properties 配置文件
     * @param key        读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Boolean getPropertyBoolean(Properties properties, String key, Boolean defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                if ("true".equalsIgnoreCase(val) || "1".equalsIgnoreCase(val)) {
                    return true;
                } else if ("false".equalsIgnoreCase(val) || "0".equalsIgnoreCase(val)) {
                    return false;
                } else {
                    return defaultVal;
                }
            }
        }
    }
}
