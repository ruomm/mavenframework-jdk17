/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月28日 上午10:29:34
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeHelper {
    private final static Log log = LogFactory.getLog(TimeHelper.class);
    public final static long VALUE_DAY_MilliSecond = 1000l * 3600l * 24l;
    public Locale locale = null;
    public TimeZone timeZone = null;
    private final static TimeHelper TIME_HELPER = new TimeHelper(TimeZone.getDefault(), Locale.getDefault());
//	private final static TimeHelper TIME_HELPER = new TimeHelper();

    public static TimeHelper getInstance() {
        return TIME_HELPER;
    }

    private Calendar getCalendarInstance() {
        Calendar cal = null;
        if (null != locale && null != timeZone) {
            cal = Calendar.getInstance(timeZone, locale);
        } else if (null != timeZone) {
            cal = Calendar.getInstance(timeZone);
        } else if (null != locale) {
            cal = Calendar.getInstance(locale);
        } else {
            cal = Calendar.getInstance();
        }
        return cal;
    }

    public TimeHelper() {
        super();
        this.locale = null;
        this.timeZone = null;
    }

    public TimeHelper(Locale locale) {
        super();
        this.locale = locale;
        this.timeZone = null;
    }

    public TimeHelper(TimeZone timeZone) {
        super();
        this.locale = null;
        this.timeZone = timeZone;
    }

    public TimeHelper(TimeZone timeZone, Locale locale) {
        super();
        this.locale = locale;
        this.timeZone = timeZone;
    }

    /**
     * 毫秒时间转换为格式化时间
     *
     * @param millis
     * @param dateFormat
     * @param isFormatLocale
     * @return 格式化时间
     */
//	public String formatTime(long timeInMillis, SimpleDateFormat dateFormat, boolean isFormatLocale) {
//		if (isFormatLocale) {
//			dateFormat.setCalendar(getCalendarInstance());
//		}
//		return dateFormat.format(new Date(timeInMillis));
//	}

    /**
     * 毫秒时间转换为格式化时间
     *
     * @param millis
     * @param dateFormat
     * @return 格式化时间
     */
    public String formatTime(long millis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(millis));
    }

    /**
     * 格式化时间转换为毫秒时间
     *
     * @param dateStr
     * @param dateFormat
     * @param isFormatLocale
     * @return 毫秒时间
     */
//	public long parseTime(String dateStr, SimpleDateFormat dateFormat, boolean isFormatLocale) {
//		try {
//			if (isFormatLocale) {
//				dateFormat.setCalendar(getCalendarInstance());
//			}
//			Date dt2 = dateFormat.parse(dateStr);
//			return dt2.getTime();
//		}
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			log.error("Error:parseTime",e);
//			return 0;
//		}
//	}

    /**
     * 格式化时间转换为毫秒时间
     *
     * @param dateStr
     * @param dateFormat
     * @return 毫秒时间
     */
    public long parseTime(String dateStr, SimpleDateFormat dateFormat) {
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
    public long getTimeMillisOfYear(long millis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();

    }

    public long getTimeMillisOfMonth(long millis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();

    }

    public long getTimeMillisOfDay(long millis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获取特定时间的年月日信息
     *
     * @param millis
     * @return
     */
    public int getValueOfYear(long millis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.YEAR);
    }

    public int getValueOfMonth(long millis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getValueOfDay(long millis) {
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(millis);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getValueOfYear(Date date) {
        return getValueOfDay(date.getTime());
    }

    public int getValueOfMonth(Date date) {
        return getValueOfMonth(date.getTime());
    }

    public int getValueOfDay(Date date) {
        return getValueOfDay(date.getTime());
    }

    /**
     * 获取当前时间（毫秒）
     *
     * @return
     */
    public long getCurrentTimeMillis() {
        return getCalendarInstance().getTimeInMillis();
    }

    public String getCurrentTimeString(SimpleDateFormat dateFormat) {
        return formatTime(getCurrentTimeMillis(), dateFormat);
    }

//	public String getCurrentTimeString(SimpleDateFormat dateFormat, boolean isFormatLocale) {
//		return formatTime(getCurrentTimeMillis(), dateFormat, isFormatLocale);
//	}

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
    public String getHourAndMin(long millis) {
        Calendar calendar = getCalendarInstance();
        calendar.setTimeInMillis(millis);
        int hourVal = calendar.get(Calendar.HOUR_OF_DAY);
        int minVal = calendar.get(Calendar.MINUTE);
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

    public String getHourAndMin(Date date) {
        return getHourAndMin(date.getTime());
    }

    /**
     * 依据生日获取周岁
     *
     * @param birthMillis
     * @return
     */
    public int getAge(long birthMillis) {
        try {
            return getAge(birthMillis, new Date().getTime(), 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAge", e);
            throw new RuntimeException("Error:TimeHelper.getAge->获取年龄失败！");
        }
    }

    public int getAge(Date birthDay) {
        try {
            return getAge(birthDay.getTime(), new Date().getTime(), 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAge", e);
            throw new RuntimeException("Error:TimeHelper.getAge->获取年龄失败！");
        }
    }

    public int getAgeHalfToOne(long birthTimeInMillis) {
        try {
            return getAge(birthTimeInMillis, new Date().getTime(), 6);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAgeHalfToOnee", e);
            throw new RuntimeException("Error:TimeHelper.getAgeHalfToOne->获取年龄失败！");
        }

    }

    public int getAgeHalfToOne(Date birthDay) {
        try {
            return getAge(birthDay.getTime(), new Date().getTime(), 6);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:getAgeHalfToOnee", e);
            throw new RuntimeException("Error:TimeHelper.getAgeHalfToOne->获取年龄失败！");
        }
    }

    public int getAge(long birthMillis, long nowMillis, int monthOffset) {
        if (nowMillis < birthMillis) {
            log.error("Error:getAge->出生时间大于当前时间!");
            throw new IllegalArgumentException("Error:TimeHelper.getAge->出生时间大于当前时间!");
        }
        int tmpMonthOffset = monthOffset > 0 && monthOffset <= 12 ? monthOffset : 0;
        Calendar cal = getCalendarInstance();
        cal.setTimeInMillis(nowMillis);
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;// 注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(birthMillis);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
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
    public Boolean isLeapyear(int year) {
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
    public String getWeek_CnName(int day_of_week) {
        String[] weeksname = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return getWeek_CnName(day_of_week, weeksname);
    }

    public String getWeek_CnName(int day_of_week, String[] weeksname) {
        if (day_of_week < 0 || day_of_week > 6) {
            return "未知";
        } else {
            return weeksname[day_of_week];
        }
    }

    // 获取一个月有多少天
    public int getDayCountByMonth(int year, int month) {
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
    public Long parseStrToTimeMillis(String timeStr, Long defaultMillis) {
        if (StringUtils.isEmpty(timeStr)) {
            return defaultMillis;
        }
        Long value = null;
        try {
            if (timeStr.toLowerCase().endsWith("s")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000;
            } else if (timeStr.toLowerCase().endsWith("m")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000 * 60;
            } else if (timeStr.toLowerCase().endsWith("h")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000 * 3600;
            } else if (timeStr.toLowerCase().endsWith("d")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000 * 3600 * 24;
            } else if (timeStr.toLowerCase().endsWith("w")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000 * 3600 * 24 * 7;
            } else if (timeStr.toLowerCase().endsWith("mon")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 3)) * 1000 * 3600 * 24 * 30;
            } else if (timeStr.toLowerCase().endsWith("y")) {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000 * 3600 * 365;
            } else {
                value = Long.valueOf(timeStr.substring(0, timeStr.length() - 1)) * 1000;
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
}
