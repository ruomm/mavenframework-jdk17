/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年7月10日 上午9:55:22
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;

public class PropertySeqHelper {
    private final static Log log = LogFactory.getLog(PropertySeqHelper.class);
    public final static String DEFAULT_SEQ_KEY = "propertyseq";
    public final static String DEFAULT_SEQ_FILE_NAME = "propertyseq.config.txt";
    public final static int DEFAULT_START_SEQ = 1;
    private String realPath = null;
    private String defaultKey = null;
    private String defaultTag = null;
    private int formatSize = 0;

    public PropertySeqHelper(String path) {
        this(path, null, null, 0);
    }

    public PropertySeqHelper(String path, String defalultKey, int seqSize) {
        this(path, defalultKey, null, seqSize);
    }

    /**
     *
     * @param path        配置文件路径
     * @param defalultKey 默认配置的序列Key值
     * @param defaultTag  默认配置的序列tag值，若是defaultTag不为空则取值时候检测tag相同自动加1，若是tag不同的重新生成
     * @param formatSize  默认配置的序列格式化成字符串的长度
     */
    public PropertySeqHelper(String path, String defalultKey, String defaultTag, int formatSize) {
        super();
        this.formatSize = formatSize;
        if (null == defalultKey || StringUtils.isEmpty(defalultKey)) {
            this.defaultKey = DEFAULT_SEQ_KEY;
        } else {
            this.defaultKey = defalultKey;
        }
        if (formatSize <= 0) {
            this.formatSize = 0;
        } else {
            this.formatSize = formatSize;
        }
        this.defaultTag = defaultTag;

        String configPath = WebUtils.parseRealPath(path);
        File file = FileUtils.createFile(configPath);
        if (null != file && file.isDirectory()) {
            configPath = configPath + DEFAULT_SEQ_FILE_NAME;
            file = FileUtils.createFile(configPath);
        }

        if (null == file) {
            throw new RuntimeException("Error:PropertySeqHelper.instanceMethod->序列文件路径配置错误，无法获取文件！");
        } else if (!file.exists()) {
            StringBuilder sb = new StringBuilder().append("# ").append("commit:")
                    .append("propertiesx.PropertyReaderCharset->PropertySeqHelper").append("\r\n");
            boolean flag = FileUtils.writeFile(file, sb.toString(), false, "UTF-8");
            if (!flag) {
                throw new RuntimeException("Error:PropertySeqHelper.instanceMethod->序列文件路径配置错误，文件创建写入错误！");
            }

        } else if (!file.isFile()) {
            throw new RuntimeException("Error:PropertySeqHelper.instanceMethod->序列文件路径配置错误，配置的不是文件！");
        }
        this.realPath = file.getPath();
    }

    /**
     * 获取序列当前值
     *
     * @paramInput key    获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @paramInput length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列当前值
     */
    public String currentSeq() {
        return currentSeq(this.defaultKey, this.defaultTag, this.formatSize);
    }

    /**
     * 获取序列当前值
     *
     * @param key    获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @paramInput length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列当前值
     */
    public String currentSeq(String key) {
        return currentSeq(key, this.defaultTag, this.formatSize);
    }

    /**
     * 获取序列当前值
     *
     * @param key    获取序列时候的key值，为空则取默认的defalultKey。
     * @param tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @paramInput length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列当前值
     */
    public String currentSeq(String key, String tag) {
        return currentSeq(key, tag, this.formatSize);
    }

    /**
     * 获取序列当前值
     *
     * @param key    获取序列时候的key值，为空则取默认的defalultKey。
     * @param tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @param length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列当前值
     */
    public String currentSeq(String key, String tag, int length) {
        return Utilx.fillZero(getCurrentSeq(key, tag), length);
    }

    /**
     * 获取序列下个值
     *
     * @paramInput key    获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @paramInput length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列下个值
     */
    public String nextSeq() {
        return nextSeq(this.defaultKey, this.defaultTag, this.formatSize);
    }

    /**
     * 获取序列下个值
     *
     * @param key    获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @paramInput length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列下个值
     */
    public String nextSeq(String key) {
        return nextSeq(key, this.defaultTag, this.formatSize);
    }

    /**
     * 获取序列下个值
     *
     * @param key    获取序列时候的key值，为空则取默认的defalultKey。
     * @param tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @paramInput length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列下个值
     */
    public String nextSeq(String key, String tag) {
        return nextSeq(key, tag, this.formatSize);
    }

    /**
     * 获取序列下个值
     *
     * @param key    获取序列时候的key值，为空则取默认的defalultKey。
     * @param tag    获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @param length 序列格式化成字符串的时候补充0的长度，若是该参数为空则取默认的formatSize。
     * @return 序列下个值
     */
    public String nextSeq(String key, String tag, int length) {
        return Utilx.fillZero(getNextSeq(key, tag), length);
    }

    /**
     * 获取序列当前值
     *
     * @paramInput key 获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag 获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @return 序列当前值
     */
    public int getCurrentSeq() {
        return getCurrentSeq(this.defaultKey, this.defaultTag);
    }

    /**
     * 获取序列当前值
     *
     * @param key 获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag 获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @return 序列当前值
     */
    public int getCurrentSeq(String key) {
        return getCurrentSeq(key, this.defaultTag);
    }

    /**
     * 获取序列下个值
     *
     * @paramInput key 获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag 获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @return 序列下个值
     */
    public int getNextSeq() {
        return getNextSeq(this.defaultKey, this.defaultTag);
    }

    /**
     * 获取序列下个值
     *
     * @param key 获取序列时候的key值，为空则取默认的defalultKey。
     * @paramInput tag 获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @return 序列下个值
     */
    public int getNextSeq(String key) {
        return getNextSeq(key, this.defaultTag);
    }

    /**
     * 获取序列当前值
     *
     * @param key 获取序列时候的key值，为空则取默认的defalultKey。
     * @param tag 获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回当前值，不同则重新生成值，若是tag为空返回当前值。
     * @return 序列当前值
     */
    public int getCurrentSeq(String key, String tag) {
        String realKey = StringUtils.isEmpty(key) ? this.defaultKey : key;
        PropertyReaderCharset propertyReaderCharset = new PropertyReaderCharset(this.realPath);
        propertyReaderCharset.loadProps(false);
        String valTemp = propertyReaderCharset.getProperty(realKey, null);
        String val = getTagStringCurrent(valTemp, tag);
        if (null == val || null == valTemp) {
            int valNext = DEFAULT_START_SEQ;
            propertyReaderCharset.putProperty(realKey, getTagStringForStore(valNext + "", tag));
            boolean saveResult = propertyReaderCharset.storeProperty(null);
            if (!saveResult) {
                throw new RuntimeException("Error:PropertySeqHelper.getNextSeq->存储序列时候失败");
            }
            return valNext;
        } else {
            if (val.length() <= 0) {
                throw new RuntimeException("Error:PropertySeqHelper.getSeq->获取序列失败");
            }
            Integer valInt = null;
            try {
                valInt = Integer.parseInt(val);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:getSeq", e);
                valInt = null;
            }
            if (null == valInt) {
                throw new RuntimeException("Error:PropertySeqHelper.getSeq->获取序列失败");
            }
            return valInt;
        }
    }

    /**
     * 获取序列下个值
     *
     * @param key 获取序列时候的key值，为空则取默认的defalultKey。
     * @param tag 获取序列时候的tag值，若是该参数为空则取默认的defalultTag。若是tag不为空则检查值的标记和tag是否相同，相同则返回下一个值，不同则重新生成值，若是tag为空返回下一个值。
     * @return 序列下个值
     */
    public int getNextSeq(String key, String tag) {
        String realKey = StringUtils.isEmpty(key) ? this.defaultKey : key;
        PropertyReaderCharset propertyReaderCharset = new PropertyReaderCharset(this.realPath);
        propertyReaderCharset.loadProps(false);
        String valTemp = propertyReaderCharset.getProperty(realKey, null);
        String val = getTagStringCurrent(valTemp, tag);
        if (null == val || null == valTemp) {
            int valNext = DEFAULT_START_SEQ;
            propertyReaderCharset.putProperty(realKey, getTagStringForStore(valNext + "", tag));
            boolean saveResult = propertyReaderCharset.storeProperty(null);
            if (!saveResult) {
                throw new RuntimeException("Error:PropertySeqHelper.getNextSeq->存储序列时候失败");
            }
            return valNext;
        } else {
            if (val.length() <= 0) {
                throw new RuntimeException("Error:PropertySeqHelper.getNextSeq->获取序列失败");
            }
            Integer valInt = null;
            try {
                valInt = Integer.parseInt(val);
            } catch (Exception e) {
                // TODO: handle exception
                valInt = null;
                log.error("Error:getNextSeq", e);

            }
            if (null == valInt) {
                throw new RuntimeException("Error:PropertySeqHelper.getNextSeq->获取序列失败");
            }
            int valNext = valInt + 1;
            propertyReaderCharset.putProperty(realKey, getTagStringForStore(valNext + "", tag));
            boolean saveResult = propertyReaderCharset.storeProperty(null);
            if (!saveResult) {
                throw new RuntimeException("Error:PropertySeqHelper.getNextSeq->存储序列时候失败");
            }
            return valNext;
        }

    }

    private static String getTagStringCurrent(String str, String tag) {
        if (StringUtils.isEmpty(tag) || StringUtils.isEmpty(str)) {
            return str;
        } else if (str.startsWith(tag + ":")) {
            return str.substring(tag.length() + 1);
        } else {
            return null;
        }
    }

    private static String getTagStringForStore(String str, String tag) {
        if (StringUtils.isEmpty(tag) || StringUtils.isEmpty(str)) {
            return str;
        } else {
            return tag + ":" + str;
        }
    }
}
