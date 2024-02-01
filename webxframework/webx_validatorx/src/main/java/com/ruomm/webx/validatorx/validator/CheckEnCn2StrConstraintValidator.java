/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月19日 上午10:58:37
 */
package com.ruomm.webx.validatorx.validator;

import com.ruomm.javax.corex.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckEnCn2StrConstraintValidator implements ConstraintValidator<CheckEnCn2Str, String> {
    private CheckEnCn2Str validAnnotation;

    @Override
    public void initialize(CheckEnCn2Str constraintAnnotation) {
        this.validAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String valueStr, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return true;
        }
        int strSize = valueStr.length();
        if (validAnnotation.max() >= 0 && strSize > validAnnotation.max()) {
            return false;
        }
        if (validAnnotation.min() >= 0 && strSize < validAnnotation.min()) {
            return false;
        }
        int strCn2Size = StringUtils.getLengthCnTo2Size(valueStr);
        if (validAnnotation.maxCn2() >= 0 && strCn2Size > validAnnotation.maxCn2()) {
            return false;
        }
        if (validAnnotation.minCn2() >= 0 && strCn2Size < validAnnotation.minCn2()) {
            return false;
        }
        String strType = StringUtils.nullStrToEmpty(validAnnotation.strType());
        boolean isStrTypeValid = true;
        if (CheckEnCn2Str.TYPE_CN.equalsIgnoreCase(strType)) {
            isStrTypeValid = StringUtils.isZhCNStringAll(valueStr);
        } else if (CheckEnCn2Str.TYPE_EN.equalsIgnoreCase(strType)) {
            isStrTypeValid = StringUtils.isEnStr(valueStr);
        }
        if (!isStrTypeValid) {
            return false;
        }
        return true;
    }

}