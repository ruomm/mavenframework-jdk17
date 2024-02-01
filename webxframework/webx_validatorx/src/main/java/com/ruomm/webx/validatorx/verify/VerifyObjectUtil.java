/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月15日 上午11:16:02
 */
package com.ruomm.webx.validatorx.verify;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.webx.validatorx.verify.annotation.DefVerifyObject;

import java.lang.reflect.Method;
import java.util.List;

public class VerifyObjectUtil {
    private final static Log log = LogFactory.getLog(VerifyObjectUtil.class);

    public static VerifyObjectResult verifyRequestMessage(Object object, String tag, Object... paramObject) {
        VerifyObjectResult verifyResult = new VerifyObjectResult();
        if (null == object) {
            verifyResult.setPass(false);
            return verifyResult;
        }
        verifyResult.setPass(true);
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            boolean isExcept = false;
            Object resultObject = null;
            try {
                com.ruomm.webx.validatorx.verify.annotation.DefVerifyObject injectRequestVerify = method
                        .getAnnotation(DefVerifyObject.class);
                if (null == injectRequestVerify) {
                    continue;
                }
                boolean isExcute = false;
                if (StringUtils.isEmpty(tag)) {
                    isExcute = true;
                } else if (StringUtils.isEmpty(injectRequestVerify.verifyTag())) {
                    isExcute = true;
                } else {
                    isExcute = false;
                    List<String> lstVerifyTag = StringUtils.getListString(injectRequestVerify.verifyTag(), ",", false);
                    for (String tmp : lstVerifyTag) {
                        if (tmp.equalsIgnoreCase(DefVerifyObject.TAG_ALL)) {
                            isExcute = true;
                            break;
                        } else if (tmp.equalsIgnoreCase(tag)) {
                            isExcute = true;
                            break;
                        }
                    }
                }
                if (!isExcute) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                int countParamMethod = null == parameterTypes ? 0 : parameterTypes.length;
//				int countParamMethod = method.getParameterCount();
                if (countParamMethod > 0) {
                    int countParamObject = null == paramObject ? 0 : paramObject.length;
                    Object[] paramMethod = new Object[countParamMethod];
                    for (int i = 0; i < countParamMethod; i++) {
                        if (i < countParamObject) {
                            paramMethod[i] = paramObject[i];
                        } else {
                            paramMethod[i] = null;
                        }
                    }
                    method.setAccessible(true);
                    resultObject = method.invoke(object, paramMethod);
                } else {
                    method.setAccessible(true);
                    resultObject = method.invoke(object);
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:verifyRequestMessage", e);
                isExcept = true;
            }
            if (isExcept) {
                verifyResult.setPass(false);
                verifyResult.setResultObject(null);
                break;
            } else if (null != resultObject && resultObject instanceof Boolean) {
                boolean pass = (Boolean) resultObject;
                if (!pass) {
                    verifyResult.setPass(false);
                    verifyResult.setResultObject(resultObject);
                    break;
                }
            } else if (null != resultObject && resultObject instanceof VerifyObjectResult) {
                VerifyObjectResult tmpResult = (VerifyObjectResult) resultObject;
                if (!tmpResult.isPass()) {
                    verifyResult = tmpResult;
                    break;
                }
            } else if (null != resultObject) {
                verifyResult.setPass(false);
                verifyResult.setResultObject(resultObject);
                break;
            } else {
                continue;
            }
        }
        return verifyResult;

    }
}