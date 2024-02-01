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
import com.ruomm.javax.objvalidatorx.annotation.ValidEnCn2Str;

public class ValidEnCn2StrHelper extends ObjValidHelper<ValidEnCn2Str> {
    private final static Log log = LogFactory.getLog(ValidEnCn2StrHelper.class);

    public ValidEnCn2StrHelper(Class<ValidEnCn2Str> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "长度不合法(ValidCnTo2Length验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidEnCn2Str validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
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
        int strCn2Size = StringUtils.getLengthCnTo2Size(valueStr);
        if (validAnnotation.maxCn2() >= 0 && strCn2Size > validAnnotation.maxCn2()) {
            return createFailResult(validAnnotation, fieldTag);
        }
        if (validAnnotation.minCn2() >= 0 && strCn2Size < validAnnotation.minCn2()) {
            return createFailResult(validAnnotation, fieldTag);
        }
        String strType = StringUtils.nullStrToEmpty(validAnnotation.strType());
        boolean isStrTypeValid = true;
        if (ValidEnCn2Str.TYPE_CN.equalsIgnoreCase(strType)) {
            isStrTypeValid = StringUtils.isZhCNStringAll(valueStr);
        } else if (ValidEnCn2Str.TYPE_EN.equalsIgnoreCase(strType)) {
            isStrTypeValid = StringUtils.isEnStr(valueStr);
        }
        if (!isStrTypeValid) {
            return createFailResult(validAnnotation, fieldTag);
        }
        return createSuccessResult();
    }

    @Override
    public String parseValidMessage(ValidEnCn2Str validAnnotation) {
        // TODO Auto-generated method stub
        if (null == validAnnotation.message() || validAnnotation.message().length() <= 0) {
            return validAnnotation.message();
        }
        try {
            return validAnnotation.message().replace("{max}", validAnnotation.max() + "")
                    .replace("{min}", validAnnotation.min() + "").replace("{maxCn2}", validAnnotation.maxCn2() + "")
                    .replace("{minCn2}", validAnnotation.minCn2() + "")
                    .replace("{strType}", parseStrTypeMessage(validAnnotation.strType()) + "");
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:parseValidMessage", e);
            return validAnnotation.message();
        }
    }

    private String parseStrTypeMessage(String strType) {
        if (null == strType) {
            return "中英文字符";
        } else if (ValidEnCn2Str.TYPE_CN.equalsIgnoreCase(strType)) {
            return "纯中文字符";
        } else if (ValidEnCn2Str.TYPE_EN.equalsIgnoreCase(strType)) {
            return "纯英文字符";
        } else {
            return "中英文字符";
        }
    }
}
