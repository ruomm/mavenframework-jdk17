/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月19日 上午10:58:37
 */
package com.ruomm.webx.validatorx.validator;

import com.ruomm.javax.corex.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class CheckAllowStrConstraintValidator implements ConstraintValidator<CheckAllowStr, String> {
    private CheckAllowStr validAnnotation;

    @Override
    public void initialize(CheckAllowStr constraintAnnotation) {
        this.validAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String valueStr, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return true;
        }
        if (null == validAnnotation.allowStr() || validAnnotation.allowStr().length() <= 0) {
            return true;
        }
        String splitTag = null == validAnnotation.splitTag() || validAnnotation.splitTag().length() <= 0 ? ","
                : validAnnotation.splitTag();
        List<String> allowStrLst = StringUtils.getListString(validAnnotation.allowStr(), splitTag, false);
        if (null == allowStrLst || allowStrLst.size() <= 0) {
            return false;
        }
        if (validAnnotation.isSingleSelect()) {
            boolean isContainer = false;
            for (String tmp : allowStrLst) {
                boolean tmpFlag = false;
                if (validAnnotation.ignoreCase()) {
                    tmpFlag = valueStr.equalsIgnoreCase(tmp);
                } else {
                    tmpFlag = valueStr.equals(tmp);
                }
                if (tmpFlag) {
                    isContainer = tmpFlag;
                    break;
                }
            }
            if (!isContainer) {
                return false;
            } else {
                return true;
            }
        } else {
            String[] fieldStrs = valueStr.split(splitTag);
            if (null == fieldStrs || fieldStrs.length <= 0) {
                return false;
            }
            boolean isValid = true;
            for (String subFieldStr : fieldStrs) {
                if (null == subFieldStr || subFieldStr.length() <= 0) {
                    isValid = false;
                    break;
                }
                boolean isContainer = false;
                for (String tmp : allowStrLst) {
                    if (null == tmp || tmp.length() <= 0) {
                        continue;
                    }
                    boolean tmpFlag = false;
                    if (validAnnotation.ignoreCase()) {
                        tmpFlag = subFieldStr.equalsIgnoreCase(tmp);
                    } else {
                        tmpFlag = subFieldStr.equals(tmp);
                    }
                    if (tmpFlag) {
                        isContainer = tmpFlag;
                        break;
                    }
                }
                if (!isContainer) {
                    isValid = false;
                    break;
                }
            }

            if (!validAnnotation.isSameSelect() && isValid) {
                List<String> tmpList = new ArrayList<String>();
                for (String subFieldStr : fieldStrs) {
                    if (null == subFieldStr || subFieldStr.length() <= 0) {
                        continue;
                    }
                    String tmpStr = validAnnotation.ignoreCase() ? subFieldStr.toLowerCase() : subFieldStr;
                    if (!tmpList.contains(tmpStr)) {
                        tmpList.add(tmpStr);
                    }
                }
                if (tmpList.size() != fieldStrs.length) {
                    isValid = false;
                }
            }
            if (!isValid) {
                return false;
            } else {
                return true;
            }
        }
    }
}