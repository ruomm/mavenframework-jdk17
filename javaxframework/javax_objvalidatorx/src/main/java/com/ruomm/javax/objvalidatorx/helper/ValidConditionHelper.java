/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 下午4:17:16
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.annotation.ValidCondition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ValidConditionHelper extends ObjValidHelper<ValidCondition> {
    private final static Log log = LogFactory.getLog(ValidConditionHelper.class);
    private boolean isNotEmpty = true;

    public ValidConditionHelper(Class<ValidCondition> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "不能为空(ValidNotEmpty验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidCondition validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        String conditionNotEmpty = validAnnotation.conditionNotEmpty();
        String conditionEmpty = validAnnotation.conditionEmpty();
        if (null != conditionNotEmpty && conditionNotEmpty.length() > 0) {
            isNotEmpty = true;
            //  解析非空条件
            int index = conditionNotEmpty.indexOf("=");
            if (index > 0 && index < conditionNotEmpty.length() - 1) {
                // 解析非空条件
                String conditionField = conditionNotEmpty.substring(0, index);
                String conditionString = conditionNotEmpty.substring(index + 1);
                boolean conditionRegex = parseConditionFieldRegex(object, validAnnotation, conditionField, conditionString);
                if (conditionRegex && StringUtils.isEmpty(valueStr)) {
                    return createFailResult(validAnnotation, fieldTag);
                }
            } else {
                boolean conditionRegex = parseConditionMethodRegex(object, validAnnotation, conditionNotEmpty);
                if (conditionRegex && StringUtils.isEmpty(valueStr)) {
                    return createFailResult(validAnnotation, fieldTag);
                }
            }
        }
        if (null != conditionEmpty && conditionEmpty.length() > 0) {
            isNotEmpty = false;
            //  解析非空条件
            int index = conditionEmpty.indexOf("=");
            if (index > 0 && index < conditionEmpty.length() - 1) {
                // 解析非空条件
                String conditionField = conditionEmpty.substring(0, index);
                String conditionString = conditionEmpty.substring(index + 1);
                boolean conditionRegex = parseConditionFieldRegex(object, validAnnotation, conditionField, conditionString);
                if (conditionRegex && !StringUtils.isEmpty(valueStr)) {
                    return createFailResult(validAnnotation, fieldTag);
                }
            } else {
                boolean conditionRegex = parseConditionMethodRegex(object, validAnnotation, conditionEmpty);
                if (conditionRegex && !StringUtils.isEmpty(valueStr)) {
                    return createFailResult(validAnnotation, fieldTag);
                }
            }
        }
        return createSuccessResult();
    }

    public boolean parseConditionFieldRegex(Object object, ValidCondition validAnnotation, String conditionField, String conditionString) {
        boolean result = false;
        try {
            Field field = object.getClass().getDeclaredField(conditionField);
            field.setAccessible(true);
            Object fieldObj = field.get(object);
            String fieldStr = null;
            if (null == fieldObj) {
                fieldStr = null;
            } else if (fieldObj instanceof String) {
                fieldStr = (String) fieldObj;
            } else {
                fieldStr = String.valueOf(fieldObj);
            }
            String defineNull = StringUtils.isEmpty(validAnnotation.defineNull()) ? "NULL" : validAnnotation.defineNull();
            String defineEmpty = StringUtils.isEmpty(validAnnotation.defineEmpty()) ? "EMPTY" : validAnnotation.defineEmpty();
            String splitTag = StringUtils.isEmpty(validAnnotation.splitTag()) ? "," : validAnnotation.splitTag();
            String[] allowStrs = conditionString.split(splitTag);
            if (null != allowStrs && allowStrs.length > 0) {
                for (String allowStr : allowStrs) {
                    if (StringUtils.isEmpty(allowStr)) {
                        continue;
                    }
                    if (null == fieldStr) {
                        if (defineNull.equals(allowStr) || defineEmpty.equals(allowStr)) {
                            result = true;
                        }
                    } else if (fieldStr.length() <= 0) {
                        if (defineEmpty.equals(allowStr)) {
                            result = true;
                        }
                    } else {
                        if (fieldStr.equals(allowStr)) {
                            result = true;
                        }
                    }
                    if (result) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            result = false;
            log.debug("Error:parseConditionRegex", e);
        }
        return result;
    }

    public boolean parseConditionMethodRegex(Object object, ValidCondition validAnnotation, String conditionMethod) {
        boolean result = false;
        try {
            Method method = object.getClass().getDeclaredMethod(conditionMethod);
            method.setAccessible(true);
            Object methodObj = method.invoke(object);
            if (null == methodObj) {
                result = false;
            } else if (methodObj instanceof Boolean) {
                result = (boolean) methodObj;
            } else if (methodObj instanceof String) {
                String tmpResult = (String) methodObj;
                for (String tmp : ValidCondition.TAGS_OF_VALID_SUCCESS.split(",")) {
                    if (tmp.equalsIgnoreCase(tmpResult)) {
                        result = true;
                        break;
                    }
                }
            } else {
                result = false;
            }

        } catch (Exception e) {
            result = false;
            log.debug("Error:parseConditionMethodRegex", e);
        }
        return result;

    }

    @Override
    public String parseValidMessage(ValidCondition validAnnotation) {
        // TODO Auto-generated method stub
        if (isNotEmpty) {
            return validAnnotation.messageNotEmpty();
        } else {
            return validAnnotation.messageEmpty();
        }
    }
}
