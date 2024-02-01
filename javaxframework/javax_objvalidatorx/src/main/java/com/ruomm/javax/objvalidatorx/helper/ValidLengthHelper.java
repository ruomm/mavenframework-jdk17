/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 下午4:17:16
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.annotation.ValidLength;

public class ValidLengthHelper extends ObjValidHelper<ValidLength> {
    private final static Log log = LogFactory.getLog(ValidLengthHelper.class);

    public ValidLengthHelper(Class<ValidLength> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "长度不合法(ValidLength验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidLength validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return createSuccessResult();
        }
        int strSize = valueStr.length();
        if (validAnnotation.max() >= 0 && strSize > validAnnotation.max()) {
            return createFailResult(validAnnotation, fieldTag);
        }
        if (validAnnotation.min() >= 0 && strSize < validAnnotation.min()) {
            return createFailResult(validAnnotation, fieldTag);
        }
        return createSuccessResult();
    }

    @Override
    public String parseValidMessage(ValidLength validAnnotation) {
        // TODO Auto-generated method stub
        if (null == validAnnotation.message() || validAnnotation.message().length() <= 0) {
            return validAnnotation.message();
        }
        try {
            return validAnnotation.message().replace("{max}", validAnnotation.max() + "").replace("{min}",
                    validAnnotation.min() + "");
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:parseValidMessage", e);
            return validAnnotation.message();
        }
    }
}
