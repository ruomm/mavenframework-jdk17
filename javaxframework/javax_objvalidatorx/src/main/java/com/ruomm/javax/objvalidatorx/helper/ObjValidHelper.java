/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 下午4:08:06
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.ObjValidUtil;

import java.lang.annotation.Annotation;

public abstract class ObjValidHelper<T extends Annotation> {
    private Class<T> clszz;

    public ObjValidHelper(Class<T> clazz) {
        this.clszz = clazz;
    }

    public Class<T> getHelperClass() {
        return this.clszz;
    }

    public abstract String getDefaultValidMessage();

    public String getValidMessage(String tag, String defaultMsg) {
        if (null != defaultMsg && defaultMsg.length() > 0) {
            return defaultMsg;
        }
        StringBuilder sb = new StringBuilder();
        if (null != tag && tag.length() > 0) {
            sb.append(tag);
        }
        if (null != getDefaultValidMessage() && getDefaultValidMessage().length() > 0) {
            sb.append(getDefaultValidMessage());
        }
        if (sb.length() <= 0) {
            sb.append(ObjValidUtil.Valid_Fail_Message);
        }
        return sb.toString();
    }
//
//	public void setHelperAnnotation(T t) {
//		this.t = t;
//	}

    public abstract ObjValidResult verify(Object object, T validAnnotation, String tag, Object fieldObj, String fieldStr);

    public abstract String parseValidMessage(T validAnnotation);

    public ObjValidResult createSuccessResult() {
        ObjValidResult validResult = new ObjValidResult();
        validResult.setValid(true);
        return validResult;
    }

    public ObjValidResult createFailResult(T validAnnotation, String fieldTag) {
        ObjValidResult validResult = new ObjValidResult();
        validResult.setValid(false);
        validResult.setMessage(getValidMessage(fieldTag, parseValidMessage(validAnnotation)));
        return validResult;
    }
}
