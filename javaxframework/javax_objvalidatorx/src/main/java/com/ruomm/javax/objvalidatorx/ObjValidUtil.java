/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 上午10:23:33
 */
package com.ruomm.javax.objvalidatorx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.objvalidatorx.annotation.*;
import com.ruomm.javax.objvalidatorx.helper.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjValidUtil {
    private final static Log log = LogFactory.getLog(ObjValidUtil.class);
    public final static String Valid_Object_NUll_Message = "验证失败，待验证的对象为空！";
    public final static String Valid_Exception_Message = "验证异常，请检查值是否正确！";
    public final static String Valid_Success_Message = "验证成功！";
    public final static String Valid_Fail_Message = "验证失败！";
    @SuppressWarnings("rawtypes")
    private static List<ObjValidHelper> listValidHelper = new ArrayList<ObjValidHelper>();

    static {
        listValidHelper.add(new ValidNotNullHelper(ValidNotNull.class));
        listValidHelper.add(new ValidNotEmptyHelper(ValidNotEmpty.class));
        listValidHelper.add(new ValidLengthHelper(ValidLength.class));
        listValidHelper.add(new ValidEnCn2StrHelper(ValidEnCn2Str.class));
        listValidHelper.add(new ValidBigDecimalHelper(ValidBigDecimal.class));
        listValidHelper.add(new ValidAllowStrHelper(ValidAllowStr.class));
        listValidHelper.add(new ValidRegexHelper(ValidRegex.class));
        listValidHelper.add(new ValidDateStrHelper(ValidDateStr.class));
        listValidHelper.add(new ValidConditionHelper(ValidCondition.class));
    }

    public static ObjValidResult verify(Object obj) {
        return verify(obj, "99");
    }

    public static ObjValidResult verify(Object obj, String defaultErrorCode) {
        ObjValidResult validResult = verifyObject(obj);
        if (!validResult.isValid()) {
            if (null != validResult.getMessage() && validResult.getMessage().contains(":")
                    && !validResult.getMessage().endsWith(":") && !validResult.getMessage().startsWith(":")) {
                int index = validResult.getMessage().indexOf(":");
                if (index > 0 && index < 7) {
                    String errCode = validResult.getMessage().substring(0, index);
                    String errMsg = validResult.getMessage().substring(index + 1);
                    validResult.setErrCode(errCode);
                    validResult.setErrMsg(null == errMsg || errMsg.length() <= 0 ? Valid_Fail_Message : errMsg);
                } else {
                    String errMsg = validResult.getMessage();
                    validResult.setErrCode(
                            null == defaultErrorCode || defaultErrorCode.length() <= 0 ? "99" : defaultErrorCode);
                    validResult.setErrMsg(null == errMsg || errMsg.length() <= 0 ? Valid_Fail_Message : errMsg);
                }
            } else if (null == obj) {
                String errMsg = validResult.getMessage();
                validResult.setErrCode(
                        null == defaultErrorCode || defaultErrorCode.length() <= 0 ? "99" : defaultErrorCode);
                validResult.setErrMsg(null == errMsg || errMsg.length() <= 0 ? Valid_Fail_Message : errMsg);
            } else {
                String errMsg = validResult.getMessage();
                validResult.setErrCode(
                        null == defaultErrorCode || defaultErrorCode.length() <= 0 ? "99" : defaultErrorCode);
                validResult.setErrMsg(null == errMsg || errMsg.length() <= 0 ? Valid_Fail_Message : errMsg);
            }
        }
        return validResult;
    }

    public static ObjValidResult verifyObject(Object obj) {

        if (null == obj) {
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(false);
            validResult.setMessage(Valid_Object_NUll_Message);
            return validResult;
        }
        Class<?> validCls = obj.getClass();
        while (true) {
            if (null == validCls) {
                break;
            }
            if (validCls != obj.getClass()) {
                DefObjValid defValid = validCls.getAnnotation(DefObjValid.class);
                if (null == defValid || !defValid.isValid()) {
                    break;
                }
            }
            Field[] fields = validCls.getDeclaredFields();
            if (null != fields && fields.length > 0) {
                for (Field tmpFeField : fields) {
                    ObjValidResult tmpResult = verifyField(tmpFeField, obj);
                    if (!tmpResult.isValid()) {
                        return tmpResult;
                    }
                }
            }
            Method[] methods = validCls.getDeclaredMethods();
            if (null != methods && methods.length > 0) {
                for (Method tmpMethod : methods) {
                    ObjValidResult tmpResult = verifyMethod(tmpMethod, obj);
                    if (!tmpResult.isValid()) {
                        return tmpResult;
                    }
                    ObjValidResult tmpResult2 = validMethodOnly(tmpMethod, obj);
                    if (!tmpResult2.isValid()) {
                        return tmpResult2;
                    }
                }
            }
            validCls = validCls.getSuperclass();
        }

        ObjValidResult validResult = new ObjValidResult();
        validResult.setValid(true);
        validResult.setMessage(Valid_Success_Message);
        return validResult;

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ObjValidResult verifyField(Field field, Object obj) {
        List<ObjValidHelper> fieldValidHelprs = new ArrayList<ObjValidHelper>();
        for (ObjValidHelper validHelper : listValidHelper) {
            try {
                Annotation fieldAnnotation = field.getAnnotation(validHelper.getHelperClass());
                if (null != fieldAnnotation) {
                    fieldValidHelprs.add(validHelper);
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.debug("Error:verifyField", e);
            }
        }
        if (fieldValidHelprs.size() <= 0) {
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(true);
            return validResult;
        }
        Object fieldObj = null;
        try {
            field.setAccessible(true);
            fieldObj = field.get(obj);
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:verifyField", e);
        }
        String fieldStr = null;
        try {
            if (null == fieldObj) {
                fieldStr = null;
            } else if (fieldObj instanceof String) {
                fieldStr = (String) fieldObj;
            } else if (fieldObj instanceof List<?>) {
                List<?> listItems = (List<?>) fieldObj;
                if (null == listItems || listItems.size() <= 0) {
                    fieldStr = "";
                } else {
                    fieldStr = String.valueOf(fieldObj);
                }
            } else if (fieldObj instanceof Map<?, ?>) {
                Map<?, ?> mapItemsObj = (Map<?, ?>) fieldObj;
                if (null == mapItemsObj || mapItemsObj.size() <= 0) {
                    fieldStr = "";
                } else {
                    fieldStr = String.valueOf(fieldObj);
                }
            } else {
                fieldStr = String.valueOf(fieldObj);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:verifyField", e);
        }
        ObjValidResult validResult = null;
        for (ObjValidHelper validHelper : fieldValidHelprs) {
            ObjValidResult tmpResult = null;
            try {
                Annotation fieldAnnotation = field.getAnnotation(validHelper.getHelperClass());
                tmpResult = validHelper.verify(obj, fieldAnnotation, field.getName(), fieldObj, fieldStr);
                if (null == tmpResult) {
                    tmpResult = new ObjValidResult();
                    tmpResult.setValid(false);
                    tmpResult.setMessage(field.getName() + Valid_Exception_Message);
                }
            } catch (Exception e) {
                // TODO: handle exception
                tmpResult = new ObjValidResult();
                tmpResult.setValid(false);
                tmpResult.setMessage(field.getName() + Valid_Exception_Message);
                log.debug("Error:verifyField", e);
            }
            if (!tmpResult.isValid()) {
                validResult = tmpResult;
                break;
            }
        }
        if (null == validResult) {
            validResult = new ObjValidResult();
            validResult.setValid(true);
        }
        return validResult;

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ObjValidResult verifyMethod(Method method, Object obj) {
        List<ObjValidHelper> fieldValidHelprs = new ArrayList<ObjValidHelper>();
        for (ObjValidHelper validHelper : listValidHelper) {
            try {
                Annotation fieldAnnotation = method.getAnnotation(validHelper.getHelperClass());
                if (null != fieldAnnotation) {
                    fieldValidHelprs.add(validHelper);
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.debug("Error:verifyMethod", e);
            }

        }
        if (fieldValidHelprs.size() <= 0) {
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(true);
            return validResult;
        }
        Object fieldObj = null;
        try {
            method.setAccessible(true);
            fieldObj = method.invoke(obj);
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:verifyMethod", e);
        }
        String fieldStr = null;
        try {
            if (null == fieldObj) {
                fieldStr = null;
            } else if (fieldObj instanceof String) {
                fieldStr = (String) fieldObj;
            } else {
                fieldStr = String.valueOf(fieldObj);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:verifyField", e);
        }
        ObjValidResult validResult = null;
        for (ObjValidHelper validHelper : fieldValidHelprs) {
            ObjValidResult tmpResult = null;
            try {
                Annotation fieldAnnotation = method.getAnnotation(validHelper.getHelperClass());
                tmpResult = validHelper.verify(obj, fieldAnnotation, method.getName(), fieldObj, fieldStr);
                if (null == tmpResult) {
                    tmpResult = new ObjValidResult();
                    tmpResult.setValid(false);
                    tmpResult.setMessage(method.getName() + Valid_Exception_Message);
                }
            } catch (Exception e) {
                // TODO: handle exception
                tmpResult = new ObjValidResult();
                tmpResult.setValid(false);
                tmpResult.setMessage(method.getName() + Valid_Exception_Message);
                log.debug("Error:verifyMethod", e);
            }
            if (!tmpResult.isValid()) {
                validResult = tmpResult;
                break;
            }
        }
        if (null == validResult) {
            validResult = new ObjValidResult();
            validResult.setValid(true);
        }
        return validResult;

    }

    private static ObjValidResult validMethodOnly(Method method, Object obj) {
        ValidMethod validMethod = null;
        try {
            validMethod = method.getAnnotation(ValidMethod.class);
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:validMethodOnly", e);
        }
        if (null == validMethod) {
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(true);
            return validResult;
        }
        Object fieldObj = null;
        try {
            method.setAccessible(true);
            fieldObj = method.invoke(obj);
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:validMethodOnly", e);
        }
        String msg = null == validMethod.message() || validMethod.message().length() <= 0
                ? "validMethod校验失败，验证方法为：" + method.getName()
                : validMethod.message();
        if (null == fieldObj) {
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(false);
            validResult.setMessage(msg);
            return validResult;
        } else if (fieldObj instanceof Boolean) {
            boolean result = (boolean) fieldObj;
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(result);
            if (!result) {
                validResult.setMessage(msg);
            }
            return validResult;
        } else if (fieldObj instanceof ObjValidResult) {
            ObjValidResult validResult = (ObjValidResult) fieldObj;
            return validResult;
        } else if (fieldObj instanceof String) {
            String result = (String) fieldObj;
            boolean is_pass = false;
            for (String tmp : ValidMethod.TAGS_OF_VALID_SUCCESS.split(",")) {
                if (tmp.equalsIgnoreCase(result)) {
                    is_pass = true;
                    break;
                }
            }
            if (result.length() <= 0) {
                is_pass = true;
            }
            if (is_pass) {
                ObjValidResult validResult = new ObjValidResult();
                validResult.setValid(true);
                return validResult;
            } else {
                ObjValidResult validResult = new ObjValidResult();
                validResult.setValid(false);
                validResult.setMessage(msg);
                return validResult;
            }
        } else {
            ObjValidResult validResult = new ObjValidResult();
            validResult.setValid(false);
            validResult.setMessage(msg);
            return validResult;
        }
    }
}
