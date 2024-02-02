/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月28日 上午10:29:34
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private final static Log log = LogFactory.getLog(TimeUtils.class);
    public final static long VALUE_DAY_MilliSecond = 1000l * 3600l * 24l;

    /**
     * 毫秒时间转换为格式化时间
     *
     * @param millis
     * @param dateFormat
     * @return 格式化时间
     */
    public static String formatTime(long millis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(millis));
    }

    /**
     * 格式化时间转换为毫秒时间
     *
     * @param dateStr
     * @param dateFormat
     * @return 毫秒时间
     */
    public static long parseTime(String dateStr, SimpleDateFormat dateFormat) {

        try {
            Date dt2 = dateFormat.parse(dateStr);
            return dt2.getTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:parseTime", e);
            return 0;
        }
    }

    /**
     * 获取一段时间内总计的毫秒
     *
     * @param millis
     * @return 总计的毫秒
     */
    @SuppressWarnings("deprecation")
    public static long getTimeMillisOfYear(long millis) {
        Date dateTemp = new Date(millis);
        dateTemp.setHours(0);
        dateTemp.setMinutes(0);
        dateTemp.setSeconds(0);
        dateTemp.setDate(1);
        dateTemp.setMonth(0);
        long valueTime = dateTemp.getTime() / 1000;
        return valueTime * 1000;

    }

    @SuppressWarnings("deprecation")
    public static long getTimeMillisOfMonth(long millis) {
        Date dateTemp = new Date(millis);
        dateTemp.setHours(0);
        dateTemp.setMinutes(0);
        dateTemp.setSeconds(0);
        dateTemp.setDate(1);
        long valueTime = dateTemp.getTime() / 1000;
        return valueTime * 1000;

    }

    @SuppressWarnings("deprecation")
    public static long getTimeMillisOfDay(long millis) {
        Date dateTemp = new Date(millis);
        dateTemp.setHours(0);
        dateTemp.setMinutes(0);
        dateTemp.setSeconds(0);
        long valueTime = dateTemp.getTime() / 1000;
        return valueTime * 1000;
    }

    /**
     * 获取特定时间的年月日信息
     *
     * @param millis
     * @return
     */
    public static int getValueOfYear(long millis) {
        return getValueOfYear(new Date(millis));
    }

    public static int getValueOfMonth(long millis) {
        return getValueOfMonth(new Date(millis));
    }

    public static int getValueOfDay(long millis) {
        return getValueOfDay(new Date(millis));
    }

    @SuppressWarnings("deprecation")
    public static int getValueOfYear(Date date) {
        if (null == date) {
            return -1;
        } else {
            return date.getYear() + 1900;
        }
    }

    @SuppressWarnings("deprecation")
    public static int getValueOfMonth(Date date) {
        if (null == date) {
            return -1;
        } else {
            return date.getMonth() + 1;
        }
    }

    @SuppressWarnings("deprecation")
    public static int getValueOfDay(Date date) {
        if (null == date) {
            return -1;
        } else {
            return date.getDate();
        }
    }

    /**
     * 获取当前时间（毫秒）
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return new Date().getTime();
    }

    public static String getCurrentTimeString(SimpleDateFormat dateFormat) {
        return formatTime(getCurrentTimeMillis(), dateFormat);
    }

    /**
     * 判断是否为合法的日期时间字符串
     *
     * @param str_input
     * @param rDateFormat
     * @return boolean;符合为true,不符合为false
     */
    public static boolean isDate(String str_input, String rDateFormat) {
        if (!StringUtils.isEmpty(rDateFormat)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取小时和分钟
     *
     * @param millis
     * @return
     */
    public static String getHourAndMin(long millis) {
        return getHourAndMin(new Date(millis));
    }

    @SuppressWarnings("deprecation")
    public static String getHourAndMin(Date date) {
        int hourVal = date.getHours();
        int minVal = date.getMinutes();
        String hourStr = null;
        String minStr = null;

        if (hourVal < 10) {
            hourStr = "0" + hourVal;
        } else {
            hourStr = "" + hourVal;
        }

        if (minVal < 10) {
            minStr = "0" + minVal;
        } else {
            minStr = "" + minVal;
        }
        return hourStr + ":" + minStr;
    }

    /**
     * 依据生日获取周岁
     *
     * @param birthMillis
     * @return
     */
    public static int getAge(long birthMillis) {
        try {
            return getAge(birthMillis, new Date().getTime(), 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAge", e);
            throw new RuntimeException("Error:TimeUtils.getAge->获取年龄失败！");
        }
    }

    public static int getAge(Date birthDay) {
        try {
            return getAge(birthDay.getTime(), new Date().getTime(), 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAge", e);
            throw new RuntimeException("Error:TimeUtils.getAge->获取年龄失败！");
        }
    }

    public static int getAgeHalfToOne(long birthMillis) {
        try {
            return getAge(birthMillis, new Date().getTime(), 6);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAgeHalfToOnee", e);
            throw new RuntimeException("Error:TimeUtils.getAgeHalfToOne->获取年龄失败！");
        }
    }

    public static int getAgeHalfToOne(Date birthDay) {
        try {
            return getAge(birthDay.getTime(), new Date().getTime(), 6);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAgeHalfToOnee", e);
            throw new RuntimeException("Error:TimeUtils.getAgeHalfToOne->获取年龄失败！");
        }
    }

    @SuppressWarnings("deprecation")
    public static int getAge(long birthMillis, long nowMillis, int monthOffset) {
        if (nowMillis < birthMillis) {
            log.error("Error:getAge->出生时间大于当前时间!");
            throw new IllegalArgumentException("Error:TimeUtils.getAge->出生时间大于当前时间!");
        }
        int tmpMonthOffset = monthOffset > 0 && monthOffset <= 12 ? monthOffset : 0;
        Date dateNow = new Date(nowMillis);
        int yearNow = dateNow.getYear() + 1900;
        int monthNow = dateNow.getMonth() + 1;// 注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = dateNow.getDate();
        Date dateBirth = new Date(birthMillis);
        int yearBirth = dateBirth.getYear() + 1900;
        int monthBirth = dateBirth.getMonth() + 1;// 注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthBirth = dateBirth.getDate();
        int totalMonthNow = yearNow * 12 + monthNow;
        int totalMonthBirth = yearBirth * 12 + monthBirth;
        // 修订
        if (monthBirth == 2 && monthNow == 2) {
            if (dayOfMonthBirth == 29 && dayOfMonthNow == 28) {
                if (isLeapyear(yearNow)) {
                    totalMonthNow--;
                }
            } else if (dayOfMonthNow < dayOfMonthBirth) {
                totalMonthNow--;
            }
        } else {
            if (dayOfMonthNow < dayOfMonthBirth) {
                totalMonthNow--;
            }
        }
        return (totalMonthNow + tmpMonthOffset - totalMonthBirth) / 12;

    }

    // 是否闰年
    public static Boolean isLeapyear(int year) {
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        } else if (year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化星期显示
     *
     * @param day_of_week
     * @return
     */
    public static String getWeek_CnName(int day_of_week) {
        String[] weeksname = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return getWeek_CnName(day_of_week, weeksname);
    }

    public static String getWeek_CnName(int day_of_week, String[] weeksname) {
        if (day_of_week < 0 || day_of_week > 6) {
            return "未知";
        } else {
            return weeksname[day_of_week];
        }
    }

    // 获取一个月有多少天
    public static int getDayCountByMonth(int year, int month) {
        int daysnumberinmonth = 30;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            daysnumberinmonth = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            daysnumberinmonth = 30;
        } else if (month == 2) {
            if (isLeapyear(year)) {
                daysnumberinmonth = 29;
            } else {
                daysnumberinmonth = 28;
            }
        }
        return daysnumberinmonth;
    }

    // 转换字符串为毫秒
    public static Long parseStrToTimeMillis(String timeStr, Long defaultMillis, boolean secondMode) {
        if (StringUtils.isEmpty(timeStr)) {
            return defaultMillis;
        }
        Long value = null;
        try {
            Double doubleVal = null;
            String timeStrLower = timeStr.trim().toLowerCase();
            if (timeStrLower.endsWith("ms")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim());
            } else if (timeStrLower.endsWith("s")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000;
            } else if (timeStrLower.endsWith("m")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 60;
            } else if (timeStrLower.endsWith("h")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600;
            } else if (timeStrLower.endsWith("d")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600 * 24;
            } else if (timeStrLower.endsWith("w")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600 * 24 * 7;
            } else if (timeStrLower.endsWith("mon")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 3).trim()) * 1000 * 3600 * 24 * 30;
            } else if (timeStrLower.endsWith("y")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600 * 24 * 365;
            } else if (secondMode) {
                doubleVal = Double.valueOf(timeStrLower.trim()) * 1000;
            } else {
                doubleVal = Double.valueOf(timeStrLower.trim());
            }
            if (null != doubleVal) {
                value = (long) (doubleVal + 0.5);
            }
        } catch (Exception e) {
            value = null;
            log.error("Error:parseStrToTimeMillis", e);
        }
        if (value == null) {
            return defaultMillis;
        } else {
            return value;
        }
    }

    // 转换字符串为秒
    public static Long parseStrToTimeSeconds(String timeStr, Long defaultSeconds, boolean secondMode) {
        if (StringUtils.isEmpty(timeStr)) {
            return defaultSeconds;
        }
        Long value = null;
        try {
            Double doubleVal = null;
            String timeStrLower = timeStr.trim().toLowerCase();
            if (timeStrLower.endsWith("ms")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) / 1000;
            } else if (timeStrLower.endsWith("s")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim());
            } else if (timeStrLower.endsWith("m")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 60;
            } else if (timeStrLower.endsWith("h")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 3600;
            } else if (timeStrLower.endsWith("d")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 3600 * 24;
            } else if (timeStrLower.endsWith("w")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 3600 * 24 * 7;
            } else if (timeStrLower.endsWith("mon")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 3).trim()) * 3600 * 24 * 30;
            } else if (timeStrLower.endsWith("y")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 3600 * 24 * 365;
            } else if (secondMode) {
                doubleVal = Double.valueOf(timeStrLower.trim());
            } else {
                doubleVal = Double.valueOf(timeStrLower.trim()) / 1000;
            }
            if (null != doubleVal) {
                value = (long) (doubleVal + 0.5);
            }
        } catch (Exception e) {
            value = null;
            log.error("Error:parseStrToTimeMillis", e);
        }
        if (value == null) {
            return defaultSeconds;
        } else {
            return value;
        }
    }

}
