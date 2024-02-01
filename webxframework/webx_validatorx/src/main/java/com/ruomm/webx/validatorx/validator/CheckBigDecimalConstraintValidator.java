/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月19日 上午10:58:37
 */
package com.ruomm.webx.validatorx.validator;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class CheckBigDecimalConstraintValidator implements ConstraintValidator<CheckBigDecimal, String> {
    private final static Log log = LogFactory.getLog(CheckBigDecimalConstraintValidator.class);
    private CheckBigDecimal validAnnotation;

    @Override
    public void initialize(CheckBigDecimal constraintAnnotation) {
        this.validAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String valueStr, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return true;
        }
        if (StringUtils.isEmpty(validAnnotation.max()) && StringUtils.isEmpty(validAnnotation.min())
                && validAnnotation.pointDigits() <= 0) {
            return true;
        }
        // 正则校验
        String regexVal = "^((\\d{1,32})||(\\d{0,32}\\.\\d{1,32}))$";
        if (!valueStr.matches(regexVal)) {
            return false;
        }
        // 检测小数位数是否在最后，若是最后则校验不通过
//		int index = valueStr.lastIndexOf(".");
//		if (index == valueStr.length() - 1) {
//			return false;
//		}
        // 转换BigDecimal
        BigDecimal valueBigDecimal = new BigDecimal(valueStr);
        try {
            valueBigDecimal = new BigDecimal(valueStr);
        } catch (Exception e) {
            // TODO: handle exception
            valueBigDecimal = null;
            log.debug("Error:verify", e);
        }
        if (null == valueBigDecimal) {
            return false;
        }
        if (!validAnnotation.zeroStartAllow()) {
            String tmpValStr = null;
            // 截取整数位数
            int tmpIndex = valueStr.indexOf(".");
            if (tmpIndex < 0) {
                tmpValStr = valueStr;
            } else if (tmpIndex == 0) {
                tmpValStr = "0";
            } else {
                tmpValStr = valueStr.substring(0, tmpIndex);
            }
            if (tmpValStr.length() > 1 && tmpValStr.startsWith("0")) {
                return false;
            }
        }
        if (!StringUtils.isEmpty(validAnnotation.min())) {
            boolean isValid = false;
            try {
                BigDecimal regDecimal = new BigDecimal(validAnnotation.min());
                int result = valueBigDecimal.compareTo(regDecimal);
                if (result >= 0) {
                    isValid = true;
                }
            } catch (Exception e) {
                // TODO: handle exception
                isValid = false;
                log.debug("Error:verify", e);
            }
            if (!isValid) {
                return false;
            }
        }
        if (!StringUtils.isEmpty(validAnnotation.max())) {
            boolean isValid = false;
            try {
                BigDecimal regDecimal = new BigDecimal(validAnnotation.max());
                int result = valueBigDecimal.compareTo(regDecimal);
                if (result <= 0) {
                    isValid = true;
                }
            } catch (Exception e) {
                // TODO: handle exception
                isValid = false;
                log.debug("Error:verify", e);
            }
            if (!isValid) {
                return false;
            }
        }
        if (validAnnotation.pointDigits() == 0) {
            int counter = StringUtils.counterAppearNum(valueStr, '.');
            if (counter > 0) {
                return false;
            }
        } else if (validAnnotation.pointDigits() > 0) {
            boolean isValid = true;
            try {
                int pointSize = 0;
                int index = valueStr.indexOf(".");
                if (index >= 0) {
                    pointSize = valueStr.length() - index - 1;
                    if (pointSize <= 0 || pointSize > validAnnotation.pointDigits()) {
                        isValid = false;
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                isValid = false;
                log.debug("Error:verify", e);
            }
            if (!isValid) {
                return false;
            }

        }

        return true;
    }

}