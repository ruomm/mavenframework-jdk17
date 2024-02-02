/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月19日 上午10:58:37
 */
package com.ruomm.webx.validatorx.validator;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.TimeUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckDateStrConstraintValidator implements ConstraintValidator<CheckDateStr, String> {
    private final static Log log = LogFactory.getLog(CheckDateStrConstraintValidator.class);
    private CheckDateStr validAnnotation;

    @Override
    public void initialize(CheckDateStr constraintAnnotation) {
        this.validAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String valueStr, ConstraintValidatorContext context) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(valueStr)) {
            return true;
        }
        if (StringUtils.isEmpty(validAnnotation.after()) && StringUtils.isEmpty(validAnnotation.before())
                && StringUtils.isEmpty(validAnnotation.afterByFormat()) && StringUtils.isEmpty(validAnnotation.beforeByFormat())) {
            return true;
        }
        Long filedTime = parseFormatTime(validAnnotation, valueStr);
        if (null == filedTime) {
            return false;
        }
        long nowTime = new Date().getTime();
        if (!isAfterByTime(validAnnotation, nowTime, filedTime)
                || !isBeforeByTime(validAnnotation, nowTime, filedTime)) {
            return false;
        }
        if (!isAfterByFormat(validAnnotation, filedTime) || !isBeforeByFormat(validAnnotation, filedTime)) {
            return false;
        } else {
            return true;
        }
    }

    private Long parseFormatTime(CheckDateStr dateStrAnnotation, String value) {
        SimpleDateFormat simpleDateFormat = null;
        try {
            if (StringUtils.isEmpty(dateStrAnnotation.format())) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            } else {
                simpleDateFormat = new SimpleDateFormat(dateStrAnnotation.format());
            }
        } catch (Exception e) {
            // TODO: handle exception
            simpleDateFormat = null;
            log.debug("Error:parseFormatTime", e);
        }
        if (null == simpleDateFormat) {
            return null;
        }
        try {
            Date dt2 = simpleDateFormat.parse(value);
            return dt2.getTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.debug("Error:parseFormatTime", e);
            return null;
        }
    }

    private boolean isAfterByTime(CheckDateStr dateStrAnnotation, long nowTime, long filedTime) {
        if (StringUtils.isEmpty(dateStrAnnotation.after())) {
            return true;
        }
        Long offsetTime = TimeUtils.parseStrToTimeMillis(dateStrAnnotation.after(), null,true);
        if (null == offsetTime) {
            return false;
        }
        if (dateStrAnnotation.isDayMode()) {
            SimpleDateFormat comDateFormat = new SimpleDateFormat("yyyyMMdd");
            String cursorTime = comDateFormat.format(new Date(nowTime + offsetTime));
            String fileDateStr = comDateFormat.format(new Date(filedTime));
            int comVal = fileDateStr.compareTo(cursorTime);
            if (comVal >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (filedTime >= nowTime + offsetTime) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isBeforeByTime(CheckDateStr dateStrAnnotation, long nowTime, long filedTime) {
        if (StringUtils.isEmpty(dateStrAnnotation.before())) {
            return true;
        }
        Long offsetTime = TimeUtils.parseStrToTimeMillis(dateStrAnnotation.before(), null,true);
        if (null == offsetTime) {
            return false;
        }
        if (dateStrAnnotation.isDayMode()) {
            SimpleDateFormat comDateFormat = new SimpleDateFormat("yyyyMMdd");
            String cursorTime = comDateFormat.format(new Date(nowTime + offsetTime));
            String fileDateStr = comDateFormat.format(new Date(filedTime));
            int comVal = fileDateStr.compareTo(cursorTime);
            if (comVal <= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (filedTime <= nowTime + offsetTime) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isAfterByFormat(CheckDateStr dateStrAnnotation, long filedTime) {
        if (StringUtils.isEmpty(dateStrAnnotation.afterByFormat())) {
            return true;
        }
        Long cursorTime = parseFormatTime(dateStrAnnotation, dateStrAnnotation.afterByFormat());
        if (null == cursorTime) {
            return false;
        }

        if (dateStrAnnotation.isDayMode()) {
            SimpleDateFormat comDateFormat = new SimpleDateFormat("yyyyMMdd");
            String cursorDateStr = comDateFormat.format(new Date(cursorTime));
            String fileDateStr = comDateFormat.format(new Date(filedTime));
            int comVal = fileDateStr.compareTo(cursorDateStr);
            if (comVal >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (filedTime >= cursorTime) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isBeforeByFormat(CheckDateStr dateStrAnnotation, long filedTime) {
        if (StringUtils.isEmpty(dateStrAnnotation.beforeByFormat())) {
            return true;
        }
        Long cursorTime = parseFormatTime(dateStrAnnotation, dateStrAnnotation.beforeByFormat());
        if (null == cursorTime) {
            return false;
        }

        if (dateStrAnnotation.isDayMode()) {
            SimpleDateFormat comDateFormat = new SimpleDateFormat("yyyyMMdd");
            String cursorDateStr = comDateFormat.format(new Date(cursorTime));
            String fileDateStr = comDateFormat.format(new Date(filedTime));
            int comVal = fileDateStr.compareTo(cursorDateStr);
            if (comVal <= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            if (filedTime <= cursorTime) {
                return true;
            } else {
                return false;
            }
        }
    }

}