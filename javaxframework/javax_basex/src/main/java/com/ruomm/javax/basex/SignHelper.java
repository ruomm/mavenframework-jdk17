/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月30日 下午1:04:49
 */
package com.ruomm.javax.basex;

import com.ruomm.javax.basex.annotation.DefSerializeField;
import com.ruomm.javax.basex.annotation.DefSerializeType;
import com.ruomm.javax.basex.annotation.DefSignMethod;
import com.ruomm.javax.corex.ListUtils;
import com.ruomm.javax.corex.ObjectUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.helper.ClassListHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

public class SignHelper {
    private final static Log log = LogFactory.getLog(SignHelper.class);
    private static boolean Is_Exception_Print = true;
    private String keyValAppend = "=";
    private String fieldAppend = "&";

    public SignHelper() {
        super();
    }

    public SignHelper(String keyValAppend, String fieldAppend) {
        super();
        this.keyValAppend = keyValAppend;
        this.fieldAppend = fieldAppend;
    }

    public String getKeyValAppend() {
        return keyValAppend;
    }

    public void setKeyValAppend(String keyValAppend) {
        this.keyValAppend = keyValAppend;
    }

    public String getFieldAppend() {
        return fieldAppend;
    }

    public void setFieldAppend(String fieldAppend) {
        this.fieldAppend = fieldAppend;
    }

    public SignHelper setAppendTag(String keyValAppend, String fieldAppend) {
        this.keyValAppend = keyValAppend;
        this.fieldAppend = fieldAppend;
        return this;
    }

    public Map<String, Object> parseObjectToMap(Object object) {
        return parseObjectToMap(object, null);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> parseObjectToMap(Object srcObj, String tag) {
        if (null == srcObj) {
            return null;
        }
        ClassListHelper classListHelper = new ClassListHelper() {
            @Override
            public boolean isBreakList(boolean isSuperClass, Class<?> classzz) {
                if (!isSuperClass) {
                    return false;
                } else {
                    DefSerializeType defSerializeType = classzz.getAnnotation(DefSerializeType.class);
                    if (null == defSerializeType) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };
        List<Class<?>> listClass = ObjectUtils.listClassByObject(srcObj, ObjectUtils.CLASS_DEEP_COUNT, classListHelper);
        if (null == listClass || listClass.size() <= 0) {
            return null;
        }
        Map<String, Object> mapObject = new HashMap<String, Object>();
        for (int i = 0; i < listClass.size(); i++) {
            Class<?> srcCls = listClass.get(i);
            boolean isSerialize = true;
            boolean isNullSerialize = false;
            String noSerializeTag = "";
            boolean defaultSerialize = true;
            String serializeMethod = "";
            if (i == 0) {
                DefSerializeType antSerializeType = srcCls.getAnnotation(DefSerializeType.class);
                if (null == antSerializeType) {
                    isSerialize = true;
                    isNullSerialize = false;
                    noSerializeTag = "";
                    defaultSerialize = true;
                    serializeMethod = "";
                } else {
                    isSerialize = antSerializeType.isSerialize();
                    isNullSerialize = antSerializeType.isNullSerialize();
                    noSerializeTag = antSerializeType.noSerializeTag();
                    defaultSerialize = antSerializeType.defaultSerialize();
                    serializeMethod = antSerializeType.serializeMethod();
                }
            } else {
                DefSerializeType antSerializeType = srcCls.getAnnotation(DefSerializeType.class);
                if (null == antSerializeType) {
                    isSerialize = false;
                    break;
                } else {
                    isSerialize = antSerializeType.isSerialize();
                    isNullSerialize = antSerializeType.isNullSerialize();
                    noSerializeTag = antSerializeType.noSerializeTag();
                    defaultSerialize = antSerializeType.defaultSerialize();
                    serializeMethod = antSerializeType.serializeMethod();
                }
            }
            if (!isSerialize) {
                continue;
            }
            isSerialize = isSerialize(noSerializeTag, tag);
            if (!isSerialize) {
                continue;
            }

            if (null != serializeMethod && serializeMethod.length() > 0) {
                try {
                    Map<String, Object> itemMap = null;
                    if (null == tag || tag.length() <= 0) {
                        Method method = srcCls.getDeclaredMethod(serializeMethod);
                        method.setAccessible(true);
                        itemMap = (Map<String, Object>) method.invoke(srcObj);
                    } else {
                        Method method = srcCls.getDeclaredMethod(serializeMethod, String.class);
                        method.setAccessible(true);
                        itemMap = (Map<String, Object>) method.invoke(srcObj, new Object[]{tag});
                    }
                    if (null != itemMap && itemMap.size() > 0) {
                        mapObject.putAll(itemMap);
                    }
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                         | InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    if (Is_Exception_Print) {
                        log.error("Error:parseObjectToMap", e);
                    } else {
                        log.debug("Error:parseObjectToMap", e);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    if (Is_Exception_Print) {
                        log.error("Error:parseObjectToMap", e);
                    } else {
                        log.debug("Error:parseObjectToMap", e);
                    }
                }
            } else {
                Field[] fields = srcCls.getDeclaredFields();
                for (Field srcItemField : fields) {
                    try {
                        // 常量跳过
                        if (Modifier.isFinal(srcItemField.getModifiers())) {
                            continue;
                        }
                        // 静态跳过
                        if (Modifier.isStatic(srcItemField.getModifiers())) {
                            continue;
                        }
                        boolean itemIsSerialize = true;
                        boolean itemIsNullSerialize = isNullSerialize;
                        String itemSerializeName = "";
                        String itemSerializeMethod = "";
                        String itemNoSerializeTag = "";
                        DefSerializeField antSerializeField = srcItemField.getAnnotation(DefSerializeField.class);
                        if (null != antSerializeField) {
                            itemIsSerialize = antSerializeField.isSerialize();
                            itemSerializeName = antSerializeField.serializeName();
                            itemSerializeMethod = antSerializeField.serializeMethod();
                            itemNoSerializeTag = antSerializeField.noSerializeTag();
                            itemIsNullSerialize = antSerializeField.isNullSerialize();
                        } else if (!defaultSerialize) {
                            itemIsSerialize = false;
                        }
                        // 检测是否需要序列化
                        if (!itemIsSerialize) {
                            continue;
                        }
                        itemIsSerialize = isSerialize(itemNoSerializeTag, tag);
                        if (!itemIsSerialize) {
                            continue;
                        }
                        String dstItemMapKey = null == itemSerializeName || itemSerializeName.length() <= 0
                                ? srcItemField.getName()
                                : itemSerializeName;
                        // 检测序列化的方法
                        Object itemObject = null;

                        if (null == itemSerializeMethod || itemSerializeMethod.length() <= 0) {
                            srcItemField.setAccessible(true);
                            itemObject = srcItemField.get(srcObj);
                        } else {
                            if (null == tag || tag.length() <= 0) {
                                Method method = srcCls.getDeclaredMethod(itemSerializeMethod);
                                method.setAccessible(true);
                                itemObject = method.invoke(srcObj);
                            } else {
                                Method method = srcCls.getDeclaredMethod(itemSerializeMethod, String.class);
                                method.setAccessible(true);
                                itemObject = method.invoke(srcObj, new Object[]{tag});
                            }
                        }
                        if (itemIsNullSerialize) {
                            mapObject.put(dstItemMapKey, itemObject);
                        } else if (null != itemObject) {
                            mapObject.put(dstItemMapKey, itemObject);
                        }
                    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
                             | IllegalArgumentException | InvocationTargetException e) {
                        // TODO: handle exception
                        if (Is_Exception_Print) {
                            log.error("Error:parseObjectToMap", e);
                        } else {
                            log.debug("Error:parseObjectToMap", e);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        if (Is_Exception_Print) {
                            log.error("Error:parseObjectToMap", e);
                        } else {
                            log.debug("Error:parseObjectToMap", e);
                        }
                    }
                }
            }
        }
        return mapObject;
    }

    public String getSignStrByObject(Object object, String... signKeys) {
        return getSignStrByObject(object, false, null, signKeys);
    }

    public String getSignStrByObject(Object object, boolean isPutEmpty, String serializeTag, String... signKeys) {
        Map<String, Object> mapObject = parseObjectToMap(object, serializeTag);
        return getSignStrByMap(mapObject, isPutEmpty, signKeys);

    }

    public String getSignStrByMap(Map<String, ?> mapObject, String... signKeys) {
        return getSignStrByMap(mapObject, false, signKeys);
    }

    public String getSignStrByMap(Map<String, ?> mapObject, boolean isPutEmpty, String... signKeys) {
        if (null == mapObject || mapObject.size() <= 0) {
            return "";
        }
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        if (null != mapObject && mapObject.size() > 0) {
            // 先将参数以其参数名的字典序升序进行排序
            TreeMap<String, Object> sortedParams = new TreeMap<String, Object>(mapObject);
            Set<Entry<String, Object>> entrys = sortedParams.entrySet();
            for (Entry<String, Object> entryItem : entrys) {
                String key = entryItem.getKey();
                String valStr = null;
//				if (isJsonMode) {
//					valStr = getItemSignZjMode(entryItem.getValue());
//				}
//				else {
//					valStr = getItemSignZjMode(entryItem.getValue());
//				}
                valStr = getItemSignZjModeNew(entryItem.getValue(), isPutEmpty);
                if (StringUtils.isEmpty(valStr) && !isPutEmpty) {
                    continue;
                }
                boolean isUnSign = false;
                if (null != signKeys && signKeys.length > 0) {
                    for (String signKey : signKeys) {
                        if (!StringUtils.isEmpty(signKey) && signKey.equals(key)) {
                            isUnSign = true;
                            break;
                        }
                    }
                }
                if (isUnSign) {
                    continue;
                }
                if (basestring.length() > 0 && !StringUtils.isEmpty(fieldAppend)) {
                    basestring.append(fieldAppend);
                }
                basestring.append(key + StringUtils.nullStrToEmpty(keyValAppend) + StringUtils.nullStrToEmpty(valStr));
            }
        }
        return basestring.length() > 0 ? basestring.toString() : "";
    }

    public String getVerifySignStrByObject(Object srcObj, String signKey) {


        ClassListHelper classListHelper = new ClassListHelper() {
            @Override
            public boolean isBreakList(boolean isSuperClass, Class<?> classzz) {
                if (!isSuperClass) {
                    return false;
                } else {
                    DefSerializeType antSerializeType = classzz.getAnnotation(DefSerializeType.class);
                    if (null == antSerializeType) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };
        List<Class<?>> listClass = ObjectUtils.listClassByObject(srcObj, ObjectUtils.CLASS_DEEP_COUNT, classListHelper);
        if (null == listClass || listClass.size() <= 0) {
            return null;
        }
        String verifySignStr = null;
        for (int i = 0; i < listClass.size(); i++) {
            Class<?> srcCls = listClass.get(i);
            try {
                if (i == 0) {
                    Field field = srcCls.getDeclaredField(signKey);
                    if (null != field) {
                        field.setAccessible(true);
                        Object verifySignObj = field.get(srcObj);
                        if (null != verifySignObj && verifySignObj instanceof String) {
                            verifySignStr = (String) verifySignObj;
                        } else if (null != verifySignObj && verifySignObj instanceof CharSequence) {
                            CharSequence charSequence = (CharSequence) verifySignObj;
                            verifySignStr = null == charSequence ? null : charSequence.toString();
                        } else if (null != verifySignObj) {
                            verifySignStr = String.valueOf(verifySignObj);
                        }
                    }
                } else {
                    DefSerializeType antSerializeType = srcCls.getAnnotation(DefSerializeType.class);
//					if (null == antSerializeType || !antSerializeType.isSerialize()) {
//						break;
//					}
                    if (null == antSerializeType) {
                        break;
                    }
                    Field field = srcCls.getDeclaredField(signKey);
                    if (null != field) {
                        field.setAccessible(true);
                        Object verifySignObj = field.get(srcObj);
                        if (null != verifySignObj && verifySignObj instanceof String) {
                            verifySignStr = (String) verifySignObj;
                        } else if (null != verifySignObj && verifySignObj instanceof CharSequence) {
                            CharSequence charSequence = (CharSequence) verifySignObj;
                            verifySignStr = null == charSequence ? null : charSequence.toString();
                        } else if (null != verifySignObj) {
                            verifySignStr = String.valueOf(verifySignObj);
                        }
                    }
                }
                if (null != verifySignStr && verifySignStr.length() > 0) {
                    continue;
                }
            } catch (Exception e) {
                if (Is_Exception_Print) {
                    log.error("Error:getVerifySignStrByObject", e);
                } else {
                    log.debug("Error:getVerifySignStrByObject", e);
                }
            }
        }
        return verifySignStr;
    }

    public String getVerifySignStrByMap(Map<String, ?> mapObject, String signKey) {
        if (null == mapObject) {
            return null;
        }
        String verifySignStr = null;

        Object verifySignObj = mapObject.get(signKey);
        if (null != verifySignObj && verifySignObj instanceof String) {
            verifySignStr = (String) verifySignObj;
        } else if (null != verifySignObj && verifySignObj instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) verifySignObj;
            verifySignStr = null == charSequence ? null : charSequence.toString();
        } else if (null != verifySignObj) {
            verifySignStr = String.valueOf(verifySignObj);
        }
        return verifySignStr;
    }

//	private String getItemSignJSONMode(Object signItem) {
//		try {
//			if (null == signItem) {
//				return "";
//			}
//			if (signItem instanceof String) {
//				return (String) signItem;
//			}
//			else if (signItem instanceof CharSequence) {
//				CharSequence charSequence = (CharSequence) signItem;
//				return charSequence == null ? "" : charSequence.toString();
//			}
//			else if (signItem instanceof List<?>) {
//
//				return JSON.toJSONString(signItem);
//			}
//			else if (signItem instanceof Map<?, ?>) {
//				return JSON.toJSONString(signItem);
//			}
//			else {
//				return JSON.toJSONString(signItem);
////				return String.valueOf(signItem);
//			}
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//			log.error("Error:getItemSignJSONMode",e);
//			return "";
//		}
//
//	}

    private String getItemSignZjModeNew(Object signItem, boolean isPutEmpty) {
        if (null == signItem) {
            return "";
        }
        boolean isUserThisSign = false;
        String signMethodName = null;
        try {
            if (signItem instanceof List<?>) {
                List<?> listItems = (List<?>) signItem;
                if (!ListUtils.isEmpty(listItems)) {
                    for (Object tmpVal : listItems) {
                        if (null == tmpVal) {
                            continue;
                        }
                        DefSerializeType defSerializeType = tmpVal.getClass().getAnnotation(DefSerializeType.class);
                        if (null != defSerializeType && defSerializeType.isSerialize()) {
                            isUserThisSign = true;
                        } else {
                            DefSignMethod antSignMethod = tmpVal.getClass().getAnnotation(DefSignMethod.class);
                            if (null != antSignMethod) {
                                signMethodName = antSignMethod.value();
                            }
                            break;
                        }
                    }
                }
            } else if (signItem instanceof Map<?, ?>) {
                Map<?, ?> mapItemsObj = (Map<?, ?>) signItem;
                if (null != mapItemsObj && mapItemsObj.size() > 0) {
                    for (Object key : mapItemsObj.keySet()) {
                        if (null == key) {
                            continue;
                        }
                        Object tmpVal = mapItemsObj.get(key);
                        if (null == tmpVal) {
                            continue;
                        }
                        DefSerializeType defSerializeType = tmpVal.getClass().getAnnotation(DefSerializeType.class);
                        if (null != defSerializeType && defSerializeType.isSerialize()) {
                            isUserThisSign = true;
                        } else {
                            DefSignMethod antSignMethod = tmpVal.getClass().getAnnotation(DefSignMethod.class);
                            if (null != antSignMethod) {
                                signMethodName = antSignMethod.value();
                            }
                            break;
                        }
                    }
                }
            } else if (!isBaseDataType(signItem)) {
                DefSerializeType defSerializeType = signItem.getClass().getAnnotation(DefSerializeType.class);
                if (null != defSerializeType && defSerializeType.isSerialize()) {
                    isUserThisSign = true;
                } else {
                    DefSignMethod antSignMethod = signItem.getClass().getAnnotation(DefSignMethod.class);
                    if (null != antSignMethod) {
                        signMethodName = antSignMethod.value();
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            signMethodName = null;
            log.error("Error:getItemSignZjMode", e);
        }

        if (signItem instanceof String) {
            return (String) signItem;
        } else if (signItem instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) signItem;
            return charSequence == null ? "" : charSequence.toString();
        } else if (isBaseDataType(signItem)) {
            return String.valueOf(signItem);
        } else if (signItem instanceof List<?>) {
            List<?> listItems = (List<?>) signItem;
            if (ListUtils.isEmpty(listItems)) {
                return "";
            }
            int listItemsSize = listItems.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < listItemsSize; i++) {
                Object tmpItem = listItems.get(i);
                String tmpVal = null;
                if (null == tmpItem) {
                    tmpVal = "";
                } else if (tmpItem instanceof String) {
                    tmpVal = (String) tmpItem;
                } else if (tmpItem instanceof CharSequence) {
                    CharSequence charSequence = (CharSequence) tmpItem;
                    tmpVal = charSequence.toString();
                } else if (isBaseDataType(tmpItem)) {
                    tmpVal = String.valueOf(tmpItem);
                } else if (isUserThisSign) {
                    tmpVal = getSignStrByObject(tmpItem, isPutEmpty, null);
                } else if (null != tmpItem && !StringUtils.isEmpty(signMethodName)) {
                    String reflectVal = "";
                    try {
                        Method itemMethod = tmpItem.getClass().getMethod(signMethodName);
                        itemMethod.setAccessible(true);
                        reflectVal = (String) itemMethod.invoke(tmpItem);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        reflectVal = "";
                        log.error("Error:getItemSignZjMode", e);
                    }
                    tmpVal = reflectVal;
                } else {
                    tmpVal = String.valueOf(tmpItem);
                }
                sb.append(tmpVal);
                if (i < listItemsSize - 1) {
                    sb.append(",");
                }
            }
            String listItemsString = sb.length() <= 0 ? "" : sb.toString();
            return "[" + listItemsString + "]";
        } else if (signItem instanceof Map<?, ?>) {
            Map<String, String> mapItemsString = new HashMap<String, String>();
            Map<?, ?> mapItemsObj = (Map<?, ?>) signItem;
            if (null == mapItemsObj || mapItemsObj.size() <= 0) {
                return "";
            }
            for (Object key : mapItemsObj.keySet()) {
                if (null == key) {
                    continue;
                }
                String tmpKey = null;
                if (key instanceof String) {
                    tmpKey = (String) key;
                } else {
                    tmpKey = String.valueOf(key);
                }
                if (StringUtils.isEmpty(tmpKey)) {
                    continue;
                }
                Object tmpItem = mapItemsObj.get(key);
                String tmpVal = null;
                if (null == tmpItem) {
                    tmpVal = "";
                } else if (tmpItem instanceof String) {
                    tmpVal = (String) tmpItem;
                } else if (tmpItem instanceof CharSequence) {
                    CharSequence charSequence = (CharSequence) tmpItem;
                    tmpVal = charSequence.toString();
                } else if (isBaseDataType(tmpItem)) {
                    tmpVal = String.valueOf(tmpItem);
                } else if (isUserThisSign) {
                    tmpVal = getSignStrByObject(tmpItem, isPutEmpty, null);
                } else if (null != tmpItem && !StringUtils.isEmpty(signMethodName)) {
                    String reflectVal = "";
                    try {
                        Method itemMethod = tmpItem.getClass().getMethod(signMethodName);
                        itemMethod.setAccessible(true);
                        reflectVal = (String) itemMethod.invoke(tmpItem);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        reflectVal = "";
                        log.error("Error:getItemSignZjMode", e);
                    }
                    tmpVal = reflectVal;
                } else {
                    tmpVal = String.valueOf(tmpItem);
                }
                mapItemsString.put(tmpKey, tmpVal);
            }
            if (null == mapItemsString || mapItemsString.size() <= 0) {
                return "";
            }
            TreeMap<String, String> treeMapItemsString = new TreeMap<String, String>(mapItemsString);
            Set<Entry<String, String>> entrys = treeMapItemsString.entrySet();
            StringBuilder sb = new StringBuilder();
            for (Entry<String, String> tempEntry : entrys) {

                String tmpKey = tempEntry.getKey();
                String tmpVal = tempEntry.getValue();
                if (StringUtils.isEmpty(tmpVal) && !isPutEmpty) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(StringUtils.nullStrToEmpty(tmpKey)).append(this.keyValAppend);
                sb.append(StringUtils.nullStrToEmpty(tmpVal));
            }
            return "{" + sb.toString() + "}";

        } else if (isUserThisSign) {
            return getSignStrByObject(signItem, isPutEmpty, null);
        } else if (!StringUtils.isEmpty(signMethodName)) {
            String signItemValString = "";
            try {
                Method itemMethod = signItem.getClass().getMethod(signMethodName);
                itemMethod.setAccessible(true);
                signItemValString = (String) itemMethod.invoke(signItem);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                signItemValString = "";
                log.error("Error:getItemSignZjMode", e);
            }
            return signItemValString;
        } else {
            return String.valueOf(signItem);
        }

    }

    private boolean isSerialize(String noSerializeTag, String tag) {
        if (null == noSerializeTag || noSerializeTag.length() <= 0) {
            return true;
        }
        String[] seriTags = noSerializeTag.split(",");
        if (null == seriTags || seriTags.length <= 0) {
            return false;
        }
        String tmpTag = null == tag || tag.length() <= 0 ? "NULL" : tag;
        boolean isSerialize = true;
        for (String tmp : seriTags) {
            if (null == tmp) {
                continue;
            }
            if (tmpTag.equalsIgnoreCase(tmp)) {
                isSerialize = false;
                break;
            }
        }
        return isSerialize;
    }

    private static boolean isBaseDataType(Object value) {
        if (null == value) {
            return false;
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long
                || value instanceof Float || value instanceof Double || value instanceof Character
                || value instanceof Boolean || value instanceof String || value instanceof CharSequence) {
            return true;
        }
        return false;
    }
}
