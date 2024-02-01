/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月4日 下午1:16:40
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.TimeUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.annotation.ValidDateStr;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidDateStrHelper extends ObjValidHelper<ValidDateStr> {
    private final static Log log = LogFactory.getLog(ValidDateStrHelper.class);

    public ValidDateStrHelper(Class<ValidDateStr> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "日期时间验证不通过(ValidDateStr验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidDateStr validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(valueStr)) {
            return createSuccessResult();
        }
        if (StringUtils.isEmpty(validAnnotation.after()) && StringUtils.isEmpty(validAnnotation.before())
                && StringUtils.isEmpty(validAnnotation.afterByFormat()) && StringUtils.isEmpty(validAnnotation.beforeByFormat())) {
            return createSuccessResult();
        }
        Long filedTime = parseFormatTime(validAnnotation, valueStr);
        if (null == filedTime) {
            return createFailResult(validAnnotation, fieldTag);
        }
        long nowTime = new Date().getTime();
        if (!isAfterByTime(validAnnotation, nowTime, filedTime)
                || !isBeforeByTime(validAnnotation, nowTime, filedTime)) {
            return createFailResult(validAnnotation, fieldTag);
        }
        if (!isAfterByFormat(validAnnotation, filedTime) || !isBeforeByFormat(validAnnotation, filedTime)) {
            return createFailResult(validAnnotation, fieldTag);
        } else {
            return createSuccessResult();
        }
    }

    @Override
    public String parseValidMessage(ValidDateStr validAnnotation) {
        // TODO Auto-generated method stub
        if (null == validAnnotation.message() || validAnnotation.message().length() <= 0) {
            return validAnnotation.message();
        }
        try {
            return validAnnotation.message().replace("{after}", StringUtils.nullStrToEmpty(validAnnotation.after()))
                    .replace("{before}", StringUtils.nullStrToEmpty(validAnnotation.before()))
                    .replace("{afterByFormat}", StringUtils.nullStrToEmpty(validAnnotation.afterByFormat()))
                    .replace("{beforeByFormat}", StringUtils.nullStrToEmpty(validAnnotation.beforeByFormat()));
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:parseValidMessage", e);
            return validAnnotation.message();
        }
    }

    private Long parseFormatTime(ValidDateStr dateStrAnnotation, String value) {
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

    private boolean isAfterByTime(ValidDateStr dateStrAnnotation, long nowTime, long filedTime) {
        if (StringUtils.isEmpty(dateStrAnnotation.after())) {
            return true;
        }
        Long offsetTime = TimeUtils.parseStrToTimeMillis(dateStrAnnotation.after(), null);
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

    private boolean isBeforeByTime(ValidDateStr dateStrAnnotation, long nowTime, long filedTime) {
        if (StringUtils.isEmpty(dateStrAnnotation.before())) {
            return true;
        }
        Long offsetTime = TimeUtils.parseStrToTimeMillis(dateStrAnnotation.before(), null);
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

    private boolean isAfterByFormat(ValidDateStr dateStrAnnotation, long filedTime) {
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

    private boolean isBeforeByFormat(ValidDateStr dateStrAnnotation, long filedTime) {
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
