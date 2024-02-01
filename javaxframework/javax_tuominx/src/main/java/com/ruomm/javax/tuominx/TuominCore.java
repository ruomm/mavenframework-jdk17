/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月23日 上午10:38:22
 */
package com.ruomm.javax.tuominx;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.tuominx.annotation.DefTuominField;
import com.ruomm.javax.tuominx.dal.TuominFieldConfig;
import com.ruomm.javax.tuominx.dal.TuominHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TuominCore {
    // 2021-08-06 修改点 若是没有声明suffix并且DefTuominField注解的字段也为空则不进行注释
    private final static Log log = LogFactory.getLog(TuominCore.class);
    private final static boolean CONFIG_PUBLIC_SEARCH = true;
    private final static boolean CONFIG_PARENT_SEARCH = true;
    private final static int CONFIG_PARENT_LEVEL = 5;
    private ConcurrentHashMap<String, List<TuominFieldConfig>> tuominFieldConfigMap;
    private boolean throwException;

    public TuominCore() {
        this.throwException = true;
        this.tuominFieldConfigMap = new ConcurrentHashMap<>();
    }

    public TuominCore(boolean throwException, boolean useCache) {
        this.throwException = throwException;
        if (useCache) {
            this.tuominFieldConfigMap = new ConcurrentHashMap<>();
        } else {
            this.tuominFieldConfigMap = null;
        }
    }

    /**
     * 执行脱敏
     *
     * @param obj           需要脱敏的对象的类
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     */
    public void doTuomin(Object obj, String clearSuffix, String maskSuffix, String encryptSuffix, String digestSuffix,
                         TuominHelper tuominHelper) {
        doTuomin(null, obj, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper);
    }

    /**
     * 执行脱敏
     *
     * @param fieldNames    需要脱敏的Field名称数组
     * @param obj           需要脱敏的对象的类
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     */
    public void doTuomin(String[] fieldNames, Object obj, String clearSuffix, String maskSuffix,
                         String encryptSuffix, String digestSuffix, TuominHelper tuominHelper) {
        if (null == obj) {
            return;
        }
        List<TuominFieldConfig> listConfig = null == fieldNames || fieldNames.length <= 0
                ? generateFieldConfigByObject(obj, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper)
                : generateFieldConfig(fieldNames, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper);

        doTuomin(listConfig, obj);
    }

    /**
     * 执行反脱敏
     *
     * @param obj           需要反脱敏的对象的类
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     */
    public void undoTuomin(Object obj, String clearSuffix, String maskSuffix, String encryptSuffix, String digestSuffix,
                           TuominHelper tuominHelper) {
        undoTuomin(null, obj, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper);
    }

    /**
     * 执行反脱敏
     *
     * @param fieldNames    需要反脱敏的Field名称数组
     * @param obj           需要反脱敏的对象的类
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     */
    public void undoTuomin(String[] fieldNames, Object obj, String clearSuffix, String maskSuffix,
                           String encryptSuffix, String digestSuffix, TuominHelper tuominHelper) {
        if (null == obj) {
            return;
        }
        List<TuominFieldConfig> listConfig = null == fieldNames || fieldNames.length <= 0
                ? generateFieldConfigByObject(obj, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper)
                : generateFieldConfig(fieldNames, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper);
        undoTuomin(listConfig, obj, false);
    }

    /**
     * 执行反脱敏加掩码
     *
     * @param obj           需要反脱敏的对象的类
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     */
    public void undoTuominWithMask(Object obj, String clearSuffix, String maskSuffix, String encryptSuffix, String digestSuffix,
                                   TuominHelper tuominHelper) {
        undoTuominWithMask(null, obj, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper);
    }

    /**
     * 执行反脱敏加掩码
     *
     * @param fieldNames    需要反脱敏的Field名称数组
     * @param obj           需要反脱敏的对象的类
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     */
    public void undoTuominWithMask(String[] fieldNames, Object obj, String clearSuffix, String maskSuffix,
                                   String encryptSuffix, String digestSuffix, TuominHelper tuominHelper) {
        if (null == obj) {
            return;
        }
        List<TuominFieldConfig> listConfig = null == fieldNames || fieldNames.length <= 0
                ? generateFieldConfigByObject(obj, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper)
                : generateFieldConfig(fieldNames, clearSuffix, maskSuffix, encryptSuffix, digestSuffix, tuominHelper);
        undoTuomin(listConfig, obj, true);
    }

    /**
     * 执行脱敏
     *
     * @param listConfig 脱敏解析配置列表
     * @param obj        需要脱敏的对象
     */
    public void doTuomin(List<TuominFieldConfig> listConfig, Object obj) {
        if (null == obj) {
            return;
        }
        if (null == listConfig || listConfig.size() <= 0) {
            return;
        }
        for (TuominFieldConfig fieldConfig : listConfig) {
            String valClear = null;
            Field clearField = null;
            try {
                clearField = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldClear());
                clearField.setAccessible(true);
                Object objClear = clearField.get(obj);
                if (null == objClear) {
                    valClear = null;
                } else {
                    valClear = (String) objClear;
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:doTuomin", e);
                valClear = null;
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "脱敏时候无法获取(" + fieldConfig.getFieldClear() + ")字段明文信息。", e);
                }
            }
            if (null == clearField) {
                continue;
            }
            if (StringUtils.isEmpty(valClear) && !fieldConfig.isEmptyTuomi()) {
                continue;
            }
            if (!StringUtils.isEmpty(fieldConfig.getFieldMask()) && (null != fieldConfig.getFieldHelper() || !StringUtils.isEmpty(fieldConfig.getMethodMask()))) {
                try {
                    Field fieldCore = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldMask());
                    fieldCore.setAccessible(true);
                    if (!StringUtils.isEmpty(fieldConfig.getMethodMask())) {
                        int methodParamNums = 2;
                        Method method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodMask(), String.class,
                                String.class);

                        if (null == method) {
                            methodParamNums = 1;
                            method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodMask(), String.class);
                        }
                        if (null != method) {
                            method.setAccessible(true);
                            Object objResult = null;
                            if (methodParamNums == 2) {
                                objResult = method.invoke(obj, fieldConfig.getTag(), valClear);
                            } else {
                                objResult = method.invoke(obj, valClear);
                            }
                            String valResult = null == objResult ? null : (String) objResult;
                            fieldCore.set(obj, valResult);
                        }
                    } else if (null != fieldConfig.getFieldHelper()) {
                        String valResult = fieldConfig.getFieldHelper().mask(fieldConfig.getTag(), valClear);
                        fieldCore.set(obj, valResult);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    if (!(e instanceof NoSuchFieldException)) {
                        log.error("Error:doTuomin", e);
                    }
                    if (throwException) {
                        throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "脱敏时候无法设置(" + fieldConfig.getFieldMask() + ")字段掩码信息。", e);
                    }
                }
            }
            if (!StringUtils.isEmpty(fieldConfig.getFieldEncrypt()) && (null != fieldConfig.getFieldHelper() || !StringUtils.isEmpty(fieldConfig.getMethodEncrypt()))) {
                try {
                    Field fieldCore = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldEncrypt());
                    fieldCore.setAccessible(true);
                    if (!StringUtils.isEmpty(fieldConfig.getMethodEncrypt())) {
                        int methodParamNums = 2;
                        Method method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodEncrypt(), String.class,
                                String.class);

                        if (null == method) {
                            methodParamNums = 1;
                            method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodEncrypt(), String.class);
                        }
                        if (null != method) {
                            method.setAccessible(true);
                            Object objResult = null;
                            if (methodParamNums == 2) {
                                objResult = method.invoke(obj, fieldConfig.getTag(), valClear);
                            } else {
                                objResult = method.invoke(obj, valClear);
                            }
                            String valResult = null == objResult ? null : (String) objResult;
                            fieldCore.set(obj, valResult);
                        }
                    } else if (null != fieldConfig.getFieldHelper()) {
                        String valResult = fieldConfig.getFieldHelper().encrypt(fieldConfig.getTag(), valClear);
                        fieldCore.set(obj, valResult);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    if (!(e instanceof NoSuchFieldException)) {
                        log.error("Error:doTuomin", e);
                    }
                    if (throwException) {
                        throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "脱敏时候无法设置(" + fieldConfig.getFieldEncrypt() + ")字段密文信息。", e);
                    }
                }
            }
            if (!StringUtils.isEmpty(fieldConfig.getFieldDigest()) && (null != fieldConfig.getFieldHelper() || !StringUtils.isEmpty(fieldConfig.getMethodDigest()))) {
                try {
                    Field fieldCore = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldDigest());
                    fieldCore.setAccessible(true);
                    if (!StringUtils.isEmpty(fieldConfig.getMethodDigest())) {
                        int methodParamNums = 2;
                        Method method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDigest(), String.class,
                                String.class);

                        if (null == method) {
                            methodParamNums = 1;
                            method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDigest(), String.class);
                        }
                        if (null != method) {
                            method.setAccessible(true);
                            Object objResult = null;
                            if (methodParamNums == 2) {
                                objResult = method.invoke(obj, fieldConfig.getTag(), valClear);
                            } else {
                                objResult = method.invoke(obj, valClear);
                            }
                            String valResult = null == objResult ? null : (String) objResult;
                            fieldCore.set(obj, valResult);
                        }
                    } else if (null != fieldConfig.getFieldHelper()) {
                        String valResult = fieldConfig.getFieldHelper().digest(fieldConfig.getTag(), valClear);
                        fieldCore.set(obj, valResult);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    if (!(e instanceof NoSuchFieldException)) {
                        log.error("Error:doTuomin", e);
                    }
                    if (throwException) {
                        throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "脱敏时候无法设置(" + fieldConfig.getFieldDigest() + ")字段指纹信息。", e);
                    }

                }
            }

        }
    }

    /**
     * 执行反脱敏
     *
     * @param listConfig 脱敏解析配置列表
     * @param obj        需要反脱敏的对象
     */
    public void undoTuomin(List<TuominFieldConfig> listConfig, Object obj, boolean isWithMsk) {
        if (null == obj) {
            return;
        }
        if (null == listConfig || listConfig.size() <= 0) {
            return;
        }
        for (TuominFieldConfig fieldConfig : listConfig) {

            Field fieldEncOrMask = null;
            String valEncOrMask = null;
            String valDigest = null;
            try {
                if (fieldConfig.isDecryptWithDigest()) {
                    Field tmpFieldDigest = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldDigest());
                    tmpFieldDigest.setAccessible(true);
                    Object tmpValDigest = tmpFieldDigest.get(obj);
                    if (null == tmpValDigest) {
                        valDigest = null;
                    } else {
                        valDigest = (String) tmpValDigest;
                    }
                }
                if (isWithMsk && !fieldConfig.isIgnoreDecryptWithMask()) {
                    fieldEncOrMask = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldMask());
                } else {
                    fieldEncOrMask = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldEncrypt());
                }
                fieldEncOrMask.setAccessible(true);
                Object tmpValEncOrMask = fieldEncOrMask.get(obj);
                if (null == tmpValEncOrMask) {
                    valEncOrMask = null;
                } else {
                    valEncOrMask = (String) tmpValEncOrMask;
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:undoTuomin", e);
                fieldEncOrMask = null;
                if (throwException) {
                    if (isWithMsk) {
                        throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "反脱敏时候无法获取(" + fieldConfig.getFieldEncrypt() + "、" + fieldConfig.getFieldEncrypt() + ")字段密文信息。", e);
                    } else {
                        throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "反脱敏时候无法获取(" + fieldConfig.getFieldEncrypt() + ")字段密文信息。", e);
                    }
                }
            }
            if (null == fieldEncOrMask) {
                continue;
            }
            if (StringUtils.isEmpty(valEncOrMask) && !fieldConfig.isEmptyTuomi()) {
                continue;
            }
            try {
                Field fieldCore = UtilJavax.parseFieldByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getFieldClear());
                fieldCore.setAccessible(true);
                if (!isWithMsk && !StringUtils.isEmpty(fieldConfig.getMethodDecrypt())) {
                    int methodParamNums = 3;
                    Method method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecrypt(),
                            String.class, String.class, String.class);
                    if (null == method) {
                        methodParamNums = 2;
                        method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecrypt(),
                                String.class, String.class);
                    }
                    if (null == method) {
                        methodParamNums = 1;
                        method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecrypt(),
                                String.class);
                    }
                    if (null != method) {
                        method.setAccessible(true);
                        Object objResult = null;
                        if (methodParamNums == 3) {
                            objResult = method.invoke(obj, fieldConfig.getTag(), valEncOrMask, valDigest);
                        } else if (methodParamNums == 2) {
                            objResult = method.invoke(obj, fieldConfig.getTag(), valEncOrMask);
                        } else {
                            objResult = method.invoke(obj, valEncOrMask);
                        }
                        String valResult = null == objResult ? null : (String) objResult;
                        fieldCore.set(obj, valResult);
                    }
                } else if (isWithMsk && !StringUtils.isEmpty(fieldConfig.getMethodDecryptWithMask())) {
                    int methodParamNums = 4;
                    Method method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecryptWithMask(),
                            boolean.class, String.class, String.class, String.class);
                    if (null == method) {
                        methodParamNums = 3;
                        method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecryptWithMask(),
                                boolean.class, String.class, String.class);
                    }
                    if (null == method) {
                        methodParamNums = 2;
                        method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecryptWithMask(),
                                String.class, String.class);
                    }
                    if (null == method) {
                        methodParamNums = 1;
                        method = UtilJavax.parseMethodByObject(obj, CONFIG_PUBLIC_SEARCH, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, fieldConfig.getMethodDecryptWithMask(),
                                String.class);
                    }
                    if (null != method) {
                        method.setAccessible(true);
                        Object objResult = null;
                        if (methodParamNums == 4) {
                            objResult = method.invoke(obj, fieldConfig.getTag(), fieldConfig.isIgnoreDecryptWithMask(), valEncOrMask, valDigest);
                        } else if (methodParamNums == 3) {
                            objResult = method.invoke(obj, fieldConfig.getTag(), fieldConfig.isIgnoreDecryptWithMask(), valEncOrMask);
                        } else if (methodParamNums == 2) {
                            objResult = method.invoke(obj, fieldConfig.getTag(), valEncOrMask);
                        } else {
                            objResult = method.invoke(obj, valEncOrMask);
                        }
                        String valResult = null == objResult ? null : (String) objResult;
                        fieldCore.set(obj, valResult);
                    }
                } else if (null != fieldConfig.getFieldHelper()) {
                    String valResult = isWithMsk ? fieldConfig.getFieldHelper().decryptWithMask(fieldConfig.getTag(), fieldConfig.isIgnoreDecryptWithMask(), valEncOrMask, valDigest) : fieldConfig.getFieldHelper().decrypt(fieldConfig.getTag(), valEncOrMask, valDigest);
                    fieldCore.set(obj, valResult);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (!(e instanceof NoSuchFieldException)) {
                    log.error("Error:undoTuomin", e);
                }
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "反脱敏时候无法设置(" + fieldConfig.getFieldClear() + ")字段明文信息。", e);
                }
            }

        }
    }

    /**
     * 获取脱敏解析配置列表
     *
     * @param obj           需要脱敏的对象
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     * @return 脱敏解析配置列表
     */
    public List<TuominFieldConfig> generateFieldConfigByObject(Object obj, String clearSuffix, String maskSuffix,
                                                               String encryptSuffix, String digestSuffix, TuominHelper tuominHelper) {
        String key = null;
        if (null != tuominFieldConfigMap) {
            StringBuilder sb = new StringBuilder();
            sb.append("cls:");
            sb.append(obj.getClass().getName());
            sb.append("-");
            if (null != tuominHelper) {
                sb.append(tuominHelper.getClass().getName());
            }
            key = sb.toString();
            List<TuominFieldConfig> list = tuominFieldConfigMap.get(key);
            if (null != list) {
                return list;
            }
        }
        Map<String, TuominHelper> fieldHelperMaps = new HashMap<String, TuominHelper>();
        List<Field> listFields = UtilJavax.parseTuominFieldsByObject(obj, CONFIG_PARENT_SEARCH, CONFIG_PARENT_LEVEL, DefTuominField.class);
        if (null == listFields || listFields.size() <= 0) {
            return null;
        }
        List<TuominFieldConfig> listConfig = new ArrayList<TuominFieldConfig>();
        for (Field tmpField : listFields) {
            DefTuominField defTuominField = tmpField.getAnnotation(DefTuominField.class);
            if (null == defTuominField) {
                continue;
            }
            // 解析基础字段名称
            String baseFieldName = parseBaseFieldNameByClear(tmpField.getName(), defTuominField.fieldClear(), clearSuffix);
            if (StringUtils.isEmpty(baseFieldName)) {
                continue;
            }
            // 解析明文字段真实名称
            String clearFieldName = parseClearRealFieldName(tmpField.getName(), defTuominField.fieldClear(), clearSuffix);
            if (StringUtils.isEmpty(clearFieldName)) {
                continue;
            }
            TuominFieldConfig tuominFieldConfig = new TuominFieldConfig();
            tuominFieldConfig.setTag(parseTuoMinTag(obj, baseFieldName, defTuominField));
            String tag = parseTuoMinTag(obj, baseFieldName, defTuominField);
            tuominFieldConfig.setTag(tag);
            // 设置明文字段真实名称
            tuominFieldConfig.setFieldClear(clearFieldName);
            // 设置掩码字段真实名称
            tuominFieldConfig.setFieldMask(parseRealFieldName(baseFieldName, defTuominField.fieldMask(), maskSuffix));
            // 设置加密字段真实名称
            tuominFieldConfig.setFieldEncrypt(parseRealFieldName(baseFieldName, defTuominField.fieldEncrypt(), encryptSuffix));
            // 设置散列字段真实名称
            tuominFieldConfig.setFieldDigest(parseRealFieldName(baseFieldName, defTuominField.fieldDigest(), digestSuffix));
            // 设置掩码方法
            if (!StringUtils.isEmpty(defTuominField.methodMask())) {
                tuominFieldConfig.setMethodMask(defTuominField.methodMask());
            }
            // 设置加密方法
            if (!StringUtils.isEmpty(defTuominField.methodEncrypt())) {
                tuominFieldConfig.setMethodEncrypt(defTuominField.methodEncrypt());
            }
            // 设置解密方法
            if (!StringUtils.isEmpty(defTuominField.methodDecrypt())) {
                tuominFieldConfig.setMethodDecrypt(defTuominField.methodDecrypt());
            }
            // 设置解密方法加掩码
            if (!StringUtils.isEmpty(defTuominField.methodDecryptWithMask())) {
                tuominFieldConfig.setMethodDecryptWithMask(defTuominField.methodDecryptWithMask());
            }
            // 设置散列方法
            if (!StringUtils.isEmpty(defTuominField.methodDigest())) {
                tuominFieldConfig.setMethodDigest(defTuominField.methodDigest());
            }
            // 设置解密方法加掩码时候无需加掩码
            tuominFieldConfig.setIgnoreDecryptWithMask(defTuominField.ignoreDecryptWithMask());
            // 设置解密方法时候是否加散列值
            tuominFieldConfig.setDecryptWithDigest(defTuominField.decryptWithDigest());
            // 设置脱密辅助类方法
            TuominHelper fieldTuominHelper = null;
            if (!StringUtils.isEmpty(defTuominField.fieldHelper())) {
                if (fieldHelperMaps.containsKey(defTuominField.fieldHelper())) {
                    fieldTuominHelper = fieldHelperMaps.get(defTuominField.fieldHelper());
                } else {
                    try {
                        Class<?> clsHelper = Class.forName(defTuominField.fieldHelper());
                        TuominHelper newFieldHelper = (TuominHelper) clsHelper.newInstance();
                        if (null != newFieldHelper) {
                            fieldHelperMaps.put(defTuominField.fieldHelper(), newFieldHelper);
                            fieldTuominHelper = newFieldHelper;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        log.error("Error:generateFieldConfig", e);
                        if (throwException) {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "脱敏时候无法依据注解构造脱敏辅助类(TuominHelper)。", e);
                        }
                    }
                }
            }
            if (null == fieldTuominHelper) {
                fieldTuominHelper = tuominHelper;
            }
            tuominFieldConfig.setFieldHelper(fieldTuominHelper);
            // 设置NULL或EMPTY是否使用脱敏
            tuominFieldConfig.setEmptyTuomi(parseEmptyTuomi(defTuominField, fieldTuominHelper));
            // 添加对象
            listConfig.add(tuominFieldConfig);
        }
        fieldHelperMaps.clear();
        fieldHelperMaps = null;
        if (null != tuominFieldConfigMap) {
            tuominFieldConfigMap.put(key, listConfig);
        }
        return listConfig;
    }

    /**
     * 获取脱敏解析配置列表
     *
     * @param fieldNames    需要脱敏的Field名称数组
     * @param clearSuffix   明文Field后缀拼接字符串
     * @param maskSuffix    掩码Field后缀拼接字符串
     * @param encryptSuffix 加密Field后缀拼接字符串
     * @param digestSuffix  散列Field后缀拼接字符串
     * @param tuominHelper  脱敏的辅助类
     * @return 脱敏解析配置列表
     */
    public List<TuominFieldConfig> generateFieldConfig(String[] fieldNames, String clearSuffix,
                                                       String maskSuffix, String encryptSuffix, String digestSuffix, TuominHelper tuominHelper) {
        String key = null;
        if (null != tuominFieldConfigMap) {
            StringBuilder sb = new StringBuilder();
            sb.append("nm:");
            for (String tmp : fieldNames) {
                if (null != tmp) {
                    sb.append(tmp);
                }
                sb.append("-");
            }
            if (null != tuominHelper) {
                sb.append(tuominHelper.getClass().getName());
            }
            key = sb.toString();
            List<TuominFieldConfig> list = tuominFieldConfigMap.get(key);
            if (null != list) {
                return list;
            }
        }
        List<TuominFieldConfig> listConfig = new ArrayList<TuominFieldConfig>();
        for (String tmpFieldStr : fieldNames) {
            if (StringUtils.isEmpty(tmpFieldStr)) {
                continue;
            }
            // 解析基础字段名称
            String baseFieldName = parseBaseFieldNameByClear(tmpFieldStr, null, clearSuffix);
            if (StringUtils.isEmpty(baseFieldName)) {
                continue;
            }
            // 解析明文字段真实名称
            String clearFieldName = parseClearRealFieldName(tmpFieldStr, null, clearSuffix);
            if (StringUtils.isEmpty(clearFieldName)) {
                continue;
            }
            TuominFieldConfig tuominFieldConfig = new TuominFieldConfig();
            tuominFieldConfig.setTag(baseFieldName);
            // 设置明文字段真实名称
            tuominFieldConfig.setFieldClear(clearFieldName);
            // 设置掩码字段真实名称
            tuominFieldConfig.setFieldMask(parseRealFieldName(baseFieldName, null, maskSuffix));
            // 设置加密字段真实名称
            tuominFieldConfig.setFieldEncrypt(parseRealFieldName(baseFieldName, null, encryptSuffix));
            // 设置散列字段真实名称
            tuominFieldConfig.setFieldDigest(parseRealFieldName(baseFieldName, null, digestSuffix));
            // 设置脱密辅助类方法
            tuominFieldConfig.setFieldHelper(tuominHelper);
            // 设置NULL或EMPTY是否使用脱敏
            tuominFieldConfig.setEmptyTuomi(parseEmptyTuomi(null, tuominHelper));
            // 添加对象
            listConfig.add(tuominFieldConfig);
        }
        if (null != tuominFieldConfigMap) {
            tuominFieldConfigMap.put(key, listConfig);
        }
        return listConfig;
    }

    /**
     * 解析NULL或EMPTY是否使用脱敏
     *
     * @param defTuominField 脱敏注解
     * @param tuominHelper   脱密辅助类
     * @return NULL或EMPTY是否使用脱敏
     */
    private boolean parseEmptyTuomi(DefTuominField defTuominField, TuominHelper tuominHelper) {
        if (null != defTuominField && defTuominField.emptyTuomi()) {
            return true;
        } else if (null != tuominHelper && tuominHelper.emptyTuomi()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析脱敏的Tag标识
     *
     * @param obj            对象
     * @param baseFieldName  基础字段名称
     * @param defTuominField 脱敏注解
     * @return 脱敏的Tag标识
     */
    private String parseTuoMinTag(Object obj, String baseFieldName, DefTuominField defTuominField) {
        String tagByMethod = null;
        if (!StringUtils.isEmpty(defTuominField.tagMethod())) {
            try {
                Method method = obj.getClass().getDeclaredMethod(defTuominField.tagMethod());
                method.setAccessible(true);
                tagByMethod = (String) method.invoke(obj);
            } catch (Exception e) {

            }

        }
        if (!StringUtils.isEmpty(tagByMethod)) {
            return tagByMethod;
        } else if (!StringUtils.isEmpty(defTuominField.tag())) {
            return defTuominField.tag();
        } else {
            return baseFieldName;
        }
    }

    /**
     * 解析基础字段名称
     *
     * @param clearNameByCls 类中明文字段名称
     * @param clearNameByDef 注解定义的明文字段名称
     * @param clearSuffix    方法声明的明文字段拼接名称
     * @return 基础字段名称
     */
    private String parseBaseFieldNameByClear(String clearNameByCls, String clearNameByDef, String clearSuffix) {
        if (StringUtils.isEmpty(clearNameByDef) && StringUtils.isEmpty(clearSuffix)) {
            return clearNameByCls;
        } else if (StringUtils.isEmpty(clearSuffix)) {
            return clearNameByDef;
        } else if (StringUtils.isEmpty(clearNameByDef)) {
            if (StringUtils.isEmpty(clearNameByCls)) {
                return null;
            } else {
                if (clearNameByCls.length() > clearSuffix.length() && clearNameByCls.endsWith(clearSuffix)) {
                    int subIndex = clearNameByCls.length() - clearSuffix.length();
                    return clearNameByCls.substring(0, subIndex);
                } else {
                    return clearNameByCls;
                }
            }
        } else {
            if (clearNameByDef.length() > clearSuffix.length() && clearNameByDef.endsWith(clearSuffix)) {
                int subIndex = clearNameByDef.length() - clearSuffix.length();
                return clearNameByDef.substring(0, subIndex);
            } else {
                return clearNameByDef;
            }
        }
    }

    /**
     * 解析明文字段真实名称
     *
     * @param clearNameByCls 类中明文字段名称
     * @param clearNameByDef 注解定义的明文字段名称
     * @param clearSuffix    方法声明的明文字段拼接名称
     * @return 明文字段真实名称
     */
    private String parseClearRealFieldName(String clearNameByCls, String clearNameByDef, String clearSuffix) {
        if (StringUtils.isEmpty(clearNameByDef) && StringUtils.isEmpty(clearSuffix)) {
            return clearNameByCls;
        } else if (StringUtils.isEmpty(clearSuffix)) {
            return clearNameByDef;
        } else if (StringUtils.isEmpty(clearNameByDef)) {
            if (StringUtils.isEmpty(clearNameByCls)) {
                return null;
            } else {
                if (clearNameByCls.length() > clearSuffix.length() && clearNameByCls.endsWith(clearSuffix)) {
                    return clearNameByCls;
                } else {
                    return clearNameByCls + clearSuffix;
                }
            }
        } else {
            if (clearNameByDef.length() > clearSuffix.length() && clearNameByDef.endsWith(clearSuffix)) {
                return clearNameByDef;
            } else {
                return clearNameByDef + clearSuffix;
            }
        }
    }

    /**
     * 解析字段真实名称
     *
     * @param baseFieldName 基础字段名称
     * @param defFieldName  注解定义的字段名称
     * @param suffix        方法声明的字段拼接名称
     * @return 字段真实名称
     */
    private String parseRealFieldName(String baseFieldName, String defFieldName, String suffix) {
        if (StringUtils.isEmpty(baseFieldName)) {
            return null;
        }
        if (StringUtils.isEmpty(defFieldName) && StringUtils.isEmpty(suffix)) {
            return null;
        } else if (StringUtils.isEmpty(suffix)) {
            return defFieldName;
        } else if (StringUtils.isEmpty(defFieldName)) {
            return baseFieldName + suffix;
        } else {
            if (defFieldName.length() > suffix.length() && defFieldName.endsWith(suffix)) {
                return defFieldName;
            } else {
                return defFieldName + suffix;
            }
        }
    }
}
