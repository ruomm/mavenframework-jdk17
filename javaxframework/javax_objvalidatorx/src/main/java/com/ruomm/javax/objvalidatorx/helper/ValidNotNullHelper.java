/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 下午4:17:16
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.annotation.ValidNotNull;

public class ValidNotNullHelper extends ObjValidHelper<ValidNotNull> {

    public ValidNotNullHelper(Class<ValidNotNull> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "不能为NULL(ValidNotNull验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidNotNull validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        if (null == valueStr) {
            return createFailResult(validAnnotation, fieldTag);
        }
        return createSuccessResult();
    }

    @Override
    public String parseValidMessage(ValidNotNull validAnnotation) {
        // TODO Auto-generated method stub
        return validAnnotation.message();
    }
}
