/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月4日 上午10:55:30
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class PropertyReaderCharset implements PropertyReaderCommonHelper {
    public final static boolean DEFAULT_EMPTY_AS_NULL = true;
    private final static Log log = LogFactory.getLog(PropertyReaderCharset.class);
    List<PropertyItem> lstProps = null;
    private String loadPath;
    private String loadCharsetName;
    private String savePath = null;
    private String saveCharsetName = null;
    private int lineSize = 0;
    private final static int COMMIT_HEADER_MAX_LINE = 10;
    private boolean emptyAsNull = DEFAULT_EMPTY_AS_NULL;
    private String appendsForKeyVal = null;

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @paramDefault charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharset(String loadPath) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = "UTF-8";
        this.emptyAsNull = DEFAULT_EMPTY_AS_NULL;
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharset(String loadPath, String charsetName) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
        this.emptyAsNull = DEFAULT_EMPTY_AS_NULL;
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @param emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharset(String loadPath, String charsetName, boolean emptyAsNull) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
        this.emptyAsNull = emptyAsNull;
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
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            log.debug("配置获取失败，配置没有正确加载，返回默认值->" + "key: " + key + " ;value: " + defaultValue);
            return defaultValue;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("配置获取失败，输入的key为空，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        String resultVal = null;
        for (PropertyItem item : lstProps) {
            if (item.getMode() != ItemMode.KEY_VAL) {
                continue;
            }
            if (item.getKey().equals(realKey)) {
                resultVal = item.getValue();
                break;
            }
        }
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
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            return null;
        }
        return lstProps.toString();
    }

    @Override
    public Map<String, String> propertyToMap() {
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            return null;
        }
        try {
            Map<String, String> map = new HashMap<String, String>();
            for (PropertyItem item : lstProps) {
                if (item.getMode() != ItemMode.KEY_VAL) {
                    continue;
                }
                map.put(item.getKey(), item.getValue());
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
            lstProps = null;

        }
        if (!isForce && null != lstProps) {
            return;
        }
        try {
            log.debug("开始加载配置文件->" + loadPath);
            String loadPathReal = WebUtils.parseRealPath(loadPath);
            File file = new File(loadPathReal);
            if (null == file || !file.exists()) {
                lstProps = new ArrayList<PropertyItem>();
                log.error("Error:PropertyReaderCharset.loadProps->加载配置文件失败，配置文件不存在！");
                return;
            }
            List<String> listData = FileUtils.readFileToList(loadPathReal, loadCharsetName, true, true);
            lstProps = Utilx.listStrToPropertyItemList(listData, appendsForKeyVal, emptyAsNull);
            log.debug("加载配置文件成功->" + lstProps);
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharset.loadProps", e);
            throw new RuntimeException("PropertyReaderCharset.loadProps->加载配置文件出现异常！", e);
        }
    }

    @Override
    public boolean putProperty(String key, String value) {
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            log.debug("添加配置失败，配置没有正确加载->" + "key: " + key + " ;value: " + value + " ;result: false");
            return false;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("添加配置失败，输入的key为空->" + "key: " + key + " ;value: " + value + " ;result: false");
            return false;
        }
        try {
            boolean isExist = false;
            for (PropertyItem item : lstProps) {
                if (null == item.getKey() || ItemMode.KEY_VAL != item.getMode()) {
                    continue;
                }
                if (item.getKey().equals(realKey)) {
                    isExist = true;
                    item.setValue(value);
                    break;
                }
            }
            if (!isExist) {
                PropertyItem item = new PropertyItem();
                item.setMode(ItemMode.KEY_VAL);
                item.setKey(realKey);
                if (emptyAsNull && StringUtils.isEmpty(value)) {
                    item.setValue(value);
                } else {
                    item.setValue(value);
                }
                item.setCommit(null);
                item.setLineStr(null);
                lstProps.add(item);
            }
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
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            log.debug("删除配置失败，配置没有正确加载->" + "key: " + key + " ;result: false");
            return false;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("删除配置失败，输入的key为空->" + "key: " + key + " ;result: false");
            return false;
        }
        try {
            List<PropertyItem> listRemove = new ArrayList<>();
            for (PropertyItem item : lstProps) {
                if (null == item.getKey() || ItemMode.KEY_VAL != item.getMode()) {
                    continue;
                }
                if (item.getKey().equals(realKey)) {
                    listRemove.add(item);
                }
            }
            for (PropertyItem delItem : listRemove) {
                lstProps.remove(delItem);
            }
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
        return storeProperty(savePath, comments, saveCharsetName, lineSize);
    }

    private boolean storeProperty(String path, String comments, String charsetName, int pageSize) {
        if (null == lstProps) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        if (null != comments && comments.length() > 0) {
            sb.append("# ").append("commit:").append(comments).append("\r\n");
        } else {
            String commitVal = getCommit(lstProps, "commit:", COMMIT_HEADER_MAX_LINE);
            if (null == commitVal || commitVal.length() <= 0) {
                commitVal = "propertiesx.PropertyReaderCharset";
            }
            sb.append("# ").append("commit:").append(commitVal).append("\r\n");
        }
        if (null != charsetName && charsetName.length() > 0) {
            sb.append("# ").append("charsetDefine:").append(charsetName).append("(User Define)").append("\r\n");
        } else {
            sb.append("# ").append("charsetDefine:").append("UTF-8").append("(Default define)").append("\r\n");
        }
        sb.append("# ").append("datetime:").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .append("\r\n");
        for (int i = 0; i < lstProps.size(); i++) {
            PropertyItem item = lstProps.get(i);
            if (null == item || null == item.getMode()) {
                continue;
            }
            if (ItemMode.COMMIT == item.getMode()) {
                String commitVal = item.getCommit();
                //监测是否需要跳过
                if (StringUtils.isEmpty(commitVal)) {
                    sb.append(Utilx.tranCommitHeader(item.getLineStr())).append(" ");
                    sb.append("\r\n");
                    continue;
                }
                String commitTrimLowerCase = commitVal.trim().toLowerCase();
                if ((commitTrimLowerCase.startsWith("commit:") || commitTrimLowerCase.startsWith("charsetdefine:")
                        || commitTrimLowerCase.startsWith("datetime:")) && i < COMMIT_HEADER_MAX_LINE) {
                    continue;
                }
                sb.append(Utilx.tranCommitHeader(item.getLineStr())).append(StringUtils.nullStrToEmpty(item.getCommit()));
                sb.append("\r\n");
            } else if (ItemMode.COMMIT_MULTI == item.getMode()) {
                String commitVal = item.getCommit();
                sb.append("/**").append(commitVal).append("*/");
                sb.append("\r\n");
            } else if (ItemMode.KEY_VAL == item.getMode()) {
                sb.append(item.getKey()).append(" " + Utilx.parseKeyValueAppendValue(appendsForKeyVal) + " ");
                String val = Utilx.parsePropertyPrint(item.getValue(), lineSize);
                sb.append(val);
                sb.append("\r\n");
            } else if (ItemMode.APPEND == item.getMode()) {
                sb.append(Utilx.BR_ONE).append(item.getValue());
                sb.append("\r\n");
            } else if (ItemMode.EMPTY == item.getMode()) {
                sb.append("\r\n");
            } else if (ItemMode.NO_DATA == item.getMode()) {
                sb.append("#").append(" ").append("解析错误，原始数据:").append(StringUtils.nullStrToEmpty(item.getLineStr()));
                sb.append("\r\n");
            }
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
        return FileUtils.writeFile(outPath, sb.toString(), false, outCharsetName);
    }

    private static String getCommit(List<PropertyItem> listPropertyItem, String commitHeader, int maxIndexCount) {
        if (null == listPropertyItem || listPropertyItem.size() <= 0) {
            return null;
        }
        if (null == commitHeader || commitHeader.length() <= 0) {
            return null;
        }
        String commitHeaderLowerCase = commitHeader.toLowerCase();
        for (int i = 0; i < maxIndexCount && i < listPropertyItem.size(); i++) {
            PropertyItem propertyItem = listPropertyItem.get(i);
            if (null == propertyItem || null == propertyItem.getMode() || ItemMode.COMMIT != propertyItem.getMode()) {
                continue;
            }
            String commit = propertyItem.getCommit();
            if (StringUtils.isEmpty(commit)) {
                continue;
            }
            String commitTrim = commit.trim();
            String commitTrimLowerCase = commitTrim.toLowerCase();
            if (commitTrimLowerCase.startsWith(commitHeaderLowerCase)) {
                return commitTrim.substring(commitHeader.length());
            }
        }
        return null;
    }

    private static List<String> getListByPage(String s, int pageSize) {
        if (null == s || s.length() <= 0) {
            return null;
        }
        int len = s.length();

        List<String> list = new ArrayList<String>();
        if (pageSize < 1 || pageSize >= len) {
            list.add(s);
            return list;
        }
        int pageCount = len / pageSize;
        for (int i = 0; i < pageCount; i++) {
            list.add(s.substring(i * pageSize, (i + 1) * pageSize));
        }
        if (len % pageSize > 0) {
            list.add(s.substring(pageCount * pageSize));
        }
        return list;
    }

}
