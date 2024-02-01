/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 下午4:17:16
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.annotation.ValidNotEmpty;

public class ValidNotEmptyHelper extends ObjValidHelper<ValidNotEmpty> {

    public ValidNotEmptyHelper(Class<ValidNotEmpty> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "不能为空(ValidNotEmpty验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidNotEmpty validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return createFailResult(validAnnotation, fieldTag);
        }
        if (validAnnotation.trim()) {
            String valTrim = valueStr.trim();
            if (null == valTrim || valTrim.length() <= 0) {
                return createFailResult(validAnnotation, fieldTag);
            }
        }
        return createSuccessResult();
    }

    @Override
    public String parseValidMessage(ValidNotEmpty validAnnotation) {
        // TODO Auto-generated method stub
        return validAnnotation.message();
    }
}
