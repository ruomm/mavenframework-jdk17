/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月4日 上午10:55:30
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.jsonx.XJSON;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PropertyReaderCharsetJson implements PropertyReaderCommonHelper {
    private final static Log log = LogFactory.getLog(PropertyReaderCharsetJson.class);
    public final static boolean DEFAULT_FORCE_REPLACE = false;
    public final static String DEFAULT_ROOT_KEY = "_root";
    Map<String, Object> jsonPropertyMap = null;
    private String loadPath;
    private String loadCharsetName;
    private String savePath = null;
    private String saveCharsetName = null;
    private int lineSize = 0;
    private final static int COMMIT_HEADER_MAX_LINE = 10;
    private boolean forceReplace = DEFAULT_FORCE_REPLACE;
    private String appendsForKeyVal = null;


    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @paramDefault charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetJson(String loadPath) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = "UTF-8";
        this.forceReplace = DEFAULT_FORCE_REPLACE;
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetJson(String loadPath, String charsetName) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
        this.forceReplace = DEFAULT_FORCE_REPLACE;
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @param forceReplace 是否强制添加和删除节点。强制添加节点是Map时候会替换成String。强制删除的时候节点为Map会强制删除
     */
    public PropertyReaderCharsetJson(String loadPath, String charsetName, boolean forceReplace) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
        this.forceReplace = forceReplace;
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

    public int getLineSize() {
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    public void setAppendsForKeyVal(String appendsForKeyVal) {
        this.appendsForKeyVal = appendsForKeyVal;
    }

    public String getAppendsForKeyVal() {
        return this.appendsForKeyVal;
    }

    public void setAppendToMaoHao(boolean isMaoHao) {
        if (isMaoHao) {
            this.appendsForKeyVal = ":,：,=";
        } else {
            this.appendsForKeyVal = null;
        }
    }

    public void setAppendToCnMaoHao(boolean isMaoHao) {
        if (isMaoHao) {
            this.appendsForKeyVal = "：,:,=";
        } else {
            this.appendsForKeyVal = null;
        }
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (null == jsonPropertyMap) {
            loadProps(false);
        }
        if (null == jsonPropertyMap) {
            log.debug("配置获取失败，配置没有正确加载，返回默认值->" + "key: " + key + " ;value: " + defaultValue);
            return defaultValue;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("配置获取失败，输入的key为空，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        String[] keyArrays = realKey.split("\\.");
        int keyArrSize = keyArrays.length;
        if (keyArrSize <= 0) {
            log.debug("配置获取失败，输入的key不合法，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        Map<String, Object> dstMap = jsonPropertyMap;
        String dstKey = keyArrays[keyArrSize - 1];
        if (null == dstKey || dstKey.length() <= 0) {
            log.debug("配置获取失败，输入的key不合法，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        for (int i = 0; i < keyArrSize - 1; i++) {
            String tmpKey = keyArrays[i];
            if (null == tmpKey || tmpKey.length() <= 0) {
                log.debug("配置获取失败，输入的key不合法，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
                return defaultValue;
            }
            if (!dstMap.containsKey(tmpKey)) {
                log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
                return defaultValue;
            }
            Object object = dstMap.get(tmpKey);
            if (null == object) {
                log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
                return defaultValue;
            } else if (object instanceof String) {
                log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
                return defaultValue;
            } else if (object instanceof Map) {
                dstMap = (Map<String, Object>) object;
            } else {
                log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
                return defaultValue;
            }
        }
        if (!dstMap.containsKey(dstKey)) {
            log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        Object dstObj = dstMap.get(dstKey);
        if (null == dstObj) {
            log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        } else if (dstObj instanceof String) {
            String resultVal = (String) dstObj;
            log.debug("获取配置成功->" + "key: " + realKey + " ;value: " + resultVal);
            return resultVal;
        } else if (dstObj instanceof Map) {
            log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        } else {
            log.debug("没有找到key值为" + realKey + "的配置值，返回默认值：" + defaultValue);
            return defaultValue;
        }
    }

    @Override
    public String propertyToString() {
        if (null == jsonPropertyMap) {
            loadProps(false);
        }
        if (null == jsonPropertyMap) {
            return null;
        }
        return jsonPropertyMap.toString();
    }

    @Override
    public Map<String, String> propertyToMap() {
        if (null == jsonPropertyMap) {
            loadProps(false);
        }
        if (null == jsonPropertyMap) {
            return null;
        }
        try {
            Map<String, String> map = doPropertyToMap(jsonPropertyMap, null);
            log.debug("转换后的Map数据为->" + (null == map ? "NULL" : map.toString()));
            return map;
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharsetJson.propertyToMap", e);
            return null;
        }
    }

    private Map<String, String> doPropertyToMap(Map<String, Object> jsonMap, String rootKey) {
        Map<String, String> map = new HashMap<>();
        Set<String> keysets = jsonMap.keySet();
        for (String tmpKey : keysets) {
            if (null == tmpKey || tmpKey.length() <= 0) {
                continue;
            }
            String tmpMapKey = null;
            if (null == rootKey || rootKey.length() <= 0) {
                tmpMapKey = tmpKey;
            } else {
                tmpMapKey = rootKey + "." + tmpKey;
            }
            Object tmpObj = jsonMap.get(tmpKey);
            if (null == tmpObj) {
                continue;
            } else if (tmpObj instanceof String) {
                String tmpVal = (String) tmpObj;
                map.put(tmpMapKey, tmpVal);
            } else {
                Map<String, Object> tmpJsonMap = (Map<String, Object>) tmpObj;
                Map<String, String> tmpStringMap = doPropertyToMap(tmpJsonMap, tmpMapKey);
                if (null != tmpStringMap || tmpJsonMap.size() > 0) {
                    map.putAll(tmpStringMap);
                }
            }
        }
        return map;
    }

    /**
     * 读取配置文件
     *
     * @param isForce
     */
    @Override
    synchronized public void loadProps(boolean isForce) {
        if (null == loadPath || loadPath.length() == 0) {
            jsonPropertyMap = null;

        }
        if (!isForce && null != jsonPropertyMap) {
            return;
        }
        try {
            log.debug("开始加载配置文件->" + loadPath);
            String loadPathReal = WebUtils.parseRealPath(loadPath);
            File file = new File(loadPathReal);
            if (null == file || !file.exists()) {
                jsonPropertyMap = new HashMap<>();
                log.error("Error:PropertyReaderCharset.loadProps->加载配置文件失败，配置文件不存在！");
                return;
            }
            String jsonContent = FileUtils.readFile(loadPathReal, loadCharsetName, true);
            if (null == jsonContent || jsonContent.length() <= 0) {
                jsonPropertyMap = new HashMap<>();
                log.error("Error:PropertyReaderCharset.loadProps->加载配置文件失败，配置文件内容为空！");
                return;
            }
            jsonPropertyMap = XJSON.parseToPropertyMap(jsonContent);
            log.debug("加载配置文件成功->" + jsonPropertyMap.toString());
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharsetJson.loadProps", e);
            throw new RuntimeException("PropertyReaderCharsetJson.loadProps->加载配置文件出现异常！", e);
        }
    }

    @Override
    public boolean putProperty(String key, String value) {
        if (null == jsonPropertyMap) {
            loadProps(false);
        }
        if (null == jsonPropertyMap) {
            log.debug("添加配置失败，配置没有正确加载->" + "key: " + key + " ;value: " + value + " ;result: false");
            return false;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("添加配置失败，输入的key为空->" + "key: " + key + " ;value: " + value + " ;result: false");
            return false;
        }
        try {
            String[] keyArrays = realKey.split("\\.");
            int keyArrSize = keyArrays.length;
            if (keyArrSize <= 0) {
                log.debug("添加配置失败，输入的key不合法->" + "key: " + key + " ;value: " + value + " ;result: false");
                return false;
            }
            Map<String, Object> dstMap = jsonPropertyMap;
            String dstKey = keyArrays[keyArrSize - 1];
            if (null == dstKey || dstKey.length() <= 0) {
                log.debug("添加配置失败，输入的key不合法->" + "key: " + key + " ;value: " + value + " ;result: false");
                return false;
            }
            for (int i = 0; i < keyArrSize - 1; i++) {
                String tmpKey = keyArrays[i];
                if (null == tmpKey || tmpKey.length() <= 0) {
                    log.debug("添加配置失败，输入的key不合法->" + "key: " + key + " ;value: " + value + " ;result: false");
                    return false;
                }
                if (!dstMap.containsKey(tmpKey)) {
                    Map<String, Object> tmpJsonMap = new HashMap<>();
                    dstMap.put(tmpKey, tmpJsonMap);
                    dstMap = tmpJsonMap;
                    continue;
                }
                Object object = dstMap.get(tmpKey);
                if (null == object) {
                    dstMap.remove(tmpKey);
                    Map<String, Object> tmpJsonMap = new HashMap<>();
                    dstMap.put(tmpKey, tmpJsonMap);
                    dstMap = tmpJsonMap;
                    continue;
                } else if (object instanceof String) {
                    if (forceReplace) {
                        dstMap.remove(tmpKey);
                        Map<String, Object> tmpJsonMap = new HashMap<>();
                        dstMap.put(tmpKey, tmpJsonMap);
                        dstMap = tmpJsonMap;
                        log.debug("强制替换节点路径上的变量。->" + "key: " + key + " ;value: " + value);
                        continue;
                    } else {
                        log.debug("添加配置失败，节点根路径有值存在->" + "key: " + key + " ;value: " + value + " ;result: false");
                        return false;
                    }
                } else if (object instanceof Map) {
                    dstMap = (Map<String, Object>) object;
                    continue;
                } else {
                    log.debug("添加配置失败，无法正确找到节点->" + "key: " + key + " ;value: " + value + " ;result: false");
                    return false;
                }
            }
            //json模式只能放入非NULL的值
            if (!dstMap.containsKey(dstKey)) {
                dstMap.put(dstKey, null == value ? "" : value);
                log.debug("添加配置成功->" + "key: " + key + " ;value: " + value + " ;result: true");
                return true;
            }
            Object dstObj = dstMap.get(dstKey);
            if (null == dstObj) {
                dstMap.remove(dstKey);
                dstMap.put(dstKey, null == value ? "" : value);
                log.debug("添加配置成功->" + "key: " + key + " ;value: " + value + " ;result: true");
                return true;
            } else if (dstObj instanceof String) {
                dstMap.remove(dstKey);
                dstMap.put(dstKey, null == value ? "" : value);
                log.debug("添加配置成功->" + "key: " + key + " ;value: " + value + " ;result: true");
                return true;
            } else if (dstObj instanceof Map) {

                Map map = (Map) dstObj;
                //若是map为空或强制添加配置可以继续
                if (forceReplace || map.size() <= 0) {
                    dstMap.remove(dstKey);
                    dstMap.put(dstKey, null == value ? "" : value);
                    log.debug("添加配置成功->" + "key: " + key + " ;value: " + value + " ;result: true");
                    return true;
                } else {
                    log.debug("添加配置失败，无法正确找点节点->" + "key: " + key + " ;value: " + value + " ;result: false");
                    log.debug("添加配置失败，你可以删除该key的下一级所有节点后重新尝试！->" + "key: " + key + " ;value: " + value + " ;result: false");
                    return false;
                }
            } else {
                log.debug("添加配置失败，无法正确找点节点->" + "key: " + key + " ;value: " + value + " ;result: false");
                return false;
            }

        } catch (Exception e) {
            log.debug("添加配置失败，存在异常->" + key + " ;value: " + value + " ;result: false");
            log.debug("Error:PropertyReaderCharsetJson.putProperty", e);
            return false;
        }
    }

    @Override
    public boolean removeProperty(String key) {
        if (null == jsonPropertyMap) {
            loadProps(false);
        }
        if (null == jsonPropertyMap) {
            log.debug("删除配置失败，配置没有正确加载->" + "key: " + key + " ;result: false");
            return false;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("删除配置失败，输入的key为空->" + "key: " + key + " ;result: false");
            return false;
        }
        try {
            String[] keyArrays = realKey.split("\\.");
            int keyArrSize = keyArrays.length;
            if (keyArrSize <= 0) {
                log.debug("删除配置失败，输入的key不合法->" + "key: " + key + " ;result: false");
                return false;
            }
            Map<String, Object> dstMap = jsonPropertyMap;
            String dstKey = keyArrays[keyArrSize - 1];
            if (null == dstKey || dstKey.length() <= 0) {
                log.debug("删除配置失败，输入的key不合法->" + "key: " + key + " ;result: false");
                return false;
            }
            for (int i = 0; i < keyArrSize - 1; i++) {
                String tmpKey = keyArrays[i];
                if (null == tmpKey || tmpKey.length() <= 0) {
                    log.debug("删除配置失败，输入的key不合法->" + "key: " + key + " ;result: false");
                    return false;
                }
                if (!dstMap.containsKey(tmpKey)) {
                    log.debug("删除配置成功->" + "key: " + key + " ;result: true");
                    return true;
                }
                Object object = dstMap.get(tmpKey);
                if (null == object) {
                    dstMap.remove(tmpKey);
                    log.debug("删除配置成功->" + "key: " + key + " ;result: true");
                    return true;

                } else if (object instanceof String) {
                    log.debug("删除配置失败，无法正确找点节点->" + "key: " + key + " ;result: false");
                    return false;
                } else if (object instanceof Map) {
                    dstMap = (Map<String, Object>) object;
                    continue;
                } else {
                    log.debug("删除配置失败，无法正确找点节点->" + "key: " + key + " ;result: false");
                    return false;
                }
            }
            if (!dstMap.containsKey(dstKey)) {
                log.debug("删除配置成功->" + "key: " + key + " ;result: true");
                return true;
            }
            Object dstObj = dstMap.get(dstKey);
            if (null == dstObj) {
                dstMap.remove(dstKey);
                log.debug("删除配置成功->" + "key: " + key + " ;result: true");
                return true;
            } else if (dstObj instanceof String) {
                dstMap.remove(dstKey);
                log.debug("删除配置成功->" + "key: " + key + " ;result: true");
                return true;
            } else if (dstObj instanceof Map) {
                if (forceReplace) {
                    dstMap.remove(dstKey);
                    log.debug("删除配置成功,强制删除->" + "key: " + key + " ;result: true");
                    return true;
                } else {
                    log.debug("删除配置失败，无法正确找点节点->" + "key: " + key + " ;result: false");
                    return false;
                }
            } else {
                log.debug("删除配置失败，无法正确找点节点->" + "key: " + key + " ;result: false");
                return false;
            }
        } catch (Exception e) {
            log.debug("删除配置失败，存在异常->" + key + " ;result: false");
            log.debug("Error:PropertyReaderCharsetJson.removeProperty", e);
            return false;
        }
    }

    @Override
    public boolean storeProperty(String comments) {
        return storeProperty(savePath, saveCharsetName, lineSize);
    }

    private boolean storeProperty(String path, String charsetName, int pageSize) {
        if (null == jsonPropertyMap) {
            return false;
        }
        String outPath = null;
        if (null == path || path.length() <= 0) {
            outPath = WebUtils.parseRealPath(loadPath);
        } else {
            outPath = WebUtils.parseRealPath(path);
        }
        String outCharsetName = null;
        if (null == charsetName || charsetName.length() <= 0) {
            outCharsetName = loadCharsetName;
        } else {
            outCharsetName = charsetName;
        }
        return FileUtils.writeFile(outPath, XJSON.toJSONStringPretty(jsonPropertyMap), false, outCharsetName);
    }

}
