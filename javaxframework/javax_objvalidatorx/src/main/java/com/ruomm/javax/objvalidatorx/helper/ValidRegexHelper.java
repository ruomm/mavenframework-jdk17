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
import com.ruomm.javax.objvalidatorx.annotation.ValidRegex;

public class ValidRegexHelper extends ObjValidHelper<ValidRegex> {
    private final static Log log = LogFactory.getLog(ValidRegexHelper.class);

    public ValidRegexHelper(Class<ValidRegex> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "规则验证不通过(ValidRegex验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidRegex validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return createSuccessResult();
        }
        if (null == validAnnotation.regex() || validAnnotation.regex().length() <= 0) {
            return createSuccessResult();
        }
        boolean isMatch = false;
        try {
            isMatch = valueStr.matches(validAnnotation.regex());
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:verify", e);
            isMatch = false;
        }
        if (!isMatch) {

            return createFailResult(validAnnotation, fieldTag);
        } else {
            return createSuccessResult();
        }
    }

    @Override
    public String parseValidMessage(ValidRegex validAnnotation) {
        // TODO Auto-generated method stub
        if (null == validAnnotation.message() || validAnnotation.message().length() <= 0) {
            return validAnnotation.message();
        }
        try {
            return validAnnotation.message().replace("{regex}", StringUtils.nullStrToEmpty(validAnnotation.regex()));
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:parseValidMessage", e);
            return validAnnotation.message();
        }
    }

}
