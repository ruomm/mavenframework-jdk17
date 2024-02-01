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
import com.ruomm.javax.objvalidatorx.annotation.ValidBigDecimal;

import java.math.BigDecimal;

public class ValidBigDecimalHelper extends ObjValidHelper<ValidBigDecimal> {
    private final static Log log = LogFactory.getLog(ValidBigDecimalHelper.class);

    public ValidBigDecimalHelper(Class<ValidBigDecimal> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "值不在合理范围内(ValidBigDecimal验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidBigDecimal validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub

//		ObjValidResult validResult = new ObjValidResult();
        if (null == valueStr || valueStr.length() <= 0) {
            return createSuccessResult();
        }
        BigDecimal valueBigDecimal;
        boolean isZhengshu = false;
        String valueStrForNum;
        if (fieldObj instanceof Integer) {
            valueBigDecimal = new BigDecimal((int) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
            isZhengshu = true;
        } else if (fieldObj instanceof Long) {
            valueBigDecimal = new BigDecimal((long) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
            isZhengshu = true;
        } else if (fieldObj instanceof Float) {
            valueBigDecimal = new BigDecimal((float) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
        } else if (fieldObj instanceof Double) {
            valueBigDecimal = new BigDecimal((double) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
        } else if (fieldObj instanceof Short) {
            valueBigDecimal = new BigDecimal((short) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
            isZhengshu = true;
        } else if (fieldObj instanceof Character) {
            valueBigDecimal = new BigDecimal((char) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
            isZhengshu = true;
        } else if (fieldObj instanceof Byte) {
            valueBigDecimal = new BigDecimal((byte) fieldObj);
            valueStrForNum = valueBigDecimal.toPlainString();
            isZhengshu = true;
        } else {
            // 正则校验
            String regexVal = "^((\\d{1,32})||(\\d{0,32}\\.\\d{1,32}))$";
            if (!valueStr.matches(regexVal)) {
                return createFailResult(validAnnotation, fieldTag);
            }
            BigDecimal tmpBigDecimal = new BigDecimal(valueStr);
            try {
                tmpBigDecimal = new BigDecimal(valueStr);
            } catch (Exception e) {
                // TODO: handle exception
                tmpBigDecimal = null;
                log.debug("Error:verify", e);
            }
            if (null == tmpBigDecimal) {
                return createFailResult(validAnnotation, fieldTag);
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
                    return createFailResult(validAnnotation, fieldTag);
                }
            }
            valueBigDecimal = tmpBigDecimal;
            valueStrForNum = valueBigDecimal.toPlainString();
        }
        if (StringUtils.isEmpty(validAnnotation.max()) && StringUtils.isEmpty(validAnnotation.min())
                && validAnnotation.pointDigits() <= 0) {
            return createSuccessResult();
        }
        if (validAnnotation.maxLength() > 0) {
            if (valueStrForNum.length() > validAnnotation.maxLength()) {
                return createFailResult(validAnnotation, fieldTag);
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
                return createFailResult(validAnnotation, fieldTag);
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
                return createFailResult(validAnnotation, fieldTag);
            }
        }
        if (validAnnotation.pointDigits() == 0) {
            int counter;
            if (isZhengshu) {
                counter = 0;
            } else {
                counter = StringUtils.counterAppearNum(valueStrForNum, '.');
            }
            if (counter > 0) {
                return createFailResult(validAnnotation, fieldTag);
            }
        } else if (validAnnotation.pointDigits() > 0) {
            boolean isValid = true;
            try {
                int pointSize = 0;
                if (isZhengshu) {
                    isValid = true;
                } else {
                    int index = valueStrForNum.indexOf(".");
                    if (index >= 0) {
                        pointSize = valueStrForNum.length() - index - 1;
                        if (pointSize <= 0 || pointSize > validAnnotation.pointDigits()) {
                            isValid = false;
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                isValid = false;
                log.debug("Error:verify", e);
            }
            if (!isValid) {
                return createFailResult(validAnnotation, fieldTag);
            }

        }

        return createSuccessResult();
    }

    @Override
    public String parseValidMessage(ValidBigDecimal validAnnotation) {
        // TODO Auto-generated method stub
        if (null == validAnnotation.message() || validAnnotation.message().length() <= 0) {
            return validAnnotation.message();
        }
        try {
            return validAnnotation.message().replace("{max}", StringUtils.nullStrToEmpty(validAnnotation.max()))
                    .replace("{min}", StringUtils.nullStrToEmpty(validAnnotation.min()))
                    .replace("{pointDigits}", validAnnotation.pointDigits() + "");
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:parseValidMessage", e);
            return validAnnotation.message();
        }
    }
}
