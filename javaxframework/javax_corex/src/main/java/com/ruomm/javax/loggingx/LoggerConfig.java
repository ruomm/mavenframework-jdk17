package com.ruomm.javax.loggingx;

import java.text.SimpleDateFormat;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/15 9:16
 */
public class LoggerConfig {
    /**
     * 日志输出级别枚举类
     * <p>
     * ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF。 All:最低等级的，用于打开所有日志记录.
     * Trace:是追踪，就是程序推进一下. Debug:指出细粒度信息事件对调试应用程序是非常有帮助的. Info:消息在粗粒度级别上突出强调应用程序的运行过程.
     * Warn:输出警告及warn以下级别的日志. Error:输出错误信息日志. Fatal:输出每个严重的错误事件将会导致应用程序的退出的日志. OFF:最高等级的，用于关闭所有日志记录.
     */
    public enum LoggerLevel {
        ALL("ALL", 0),
        TRACE("ALL", 1),
        DEBUG("ALL", 2),
        INFO("ALL", 3),
        WARN("ALL", 4),
        ERROR("ALL", 5),
        FATAL("ALL", 6),
        OFF("ALL", 7);
        private String name;
        private int level;

        LoggerLevel(String name, int level) {
            this.name = name;
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }

    ;

    public static enum DebugLevel {
        INFO,
        DEBUG,
        OFF
    }

    ;

    public static DebugLevel COMMON_DEBUG_LEVEL = DebugLevel.DEBUG;

    public static LoggerLevel COMMON_LOGGER_LEVEL = LoggerLevel.INFO;

    /**
     * 通用日志输出级别设置
     *
     * @param loggerLevel 日志输出级别设置。
     */
    public static void configLoggerLevel(LoggerLevel loggerLevel) {
        COMMON_LOGGER_LEVEL = loggerLevel;
    }

    /**
     * 通用日志输出级别获取
     *
     * @return 通用日志输出级别
     */
    public static LoggerLevel getLoggerLevel() {
        return COMMON_LOGGER_LEVEL;
    }

    /**
     * 通用日志Debug级别设置
     */
    public static void configDebugLevel(DebugLevel debugLevel) {
        if (null == debugLevel) {
            COMMON_DEBUG_LEVEL = DebugLevel.DEBUG;
        } else {
            COMMON_DEBUG_LEVEL = debugLevel;
        }
    }

    /**
     * 通用日志Debug级别获取
     */
    public static DebugLevel getDebugLevel() {
        if (null == COMMON_DEBUG_LEVEL) {
            return DebugLevel.DEBUG;

        } else {
            return COMMON_DEBUG_LEVEL;
        }
    }

    // System.out 和 Android Logcat日志输出时候控制选项
    private static boolean LOG_PRINT_FULL_CLASS = true;
    private static boolean LOG_CLASS_NAME = true;
    private static boolean LOG_METHOD_NAME = true;
    private static boolean LOG_LINE_NUMBER = true;
    private static SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 设置通用日志是否打印类全称，true打印类全称，false打印类简称
     *
     * @param logPrintFullClass
     */
    public static void setLogPrintFullClass(boolean logPrintFullClass) {
        LOG_PRINT_FULL_CLASS = logPrintFullClass;
    }

    /**
     * 获取通用日志是否打印类全称，true打印类全称，false打印类简称
     *
     * @return LOG_PRINT_FULL_CLASS
     */
    public static boolean getLogPrintFullClass() {
        return LOG_PRINT_FULL_CLASS;
    }

    /**
     * 设置通用日志是输出类名称，true输出，false不输出
     *
     * @param logClassName
     */
    public static void setLogClassName(boolean logClassName) {
        LOG_CLASS_NAME = logClassName;
    }

    /**
     * 获取通用日志是输出类名称，true输出，false不输出
     *
     * @return LOG_CLASS_NAME
     */
    public static boolean getLogClassName() {
        return LOG_CLASS_NAME;
    }

    /**
     * 设置通用日志是输出方法名称，true输出，false不输出
     *
     * @param logMethodName
     */
    public static void setLogMethodName(boolean logMethodName) {
        LOG_METHOD_NAME = logMethodName;
    }

    /**
     * 获取通用日志是输出方法名称，true输出，false不输出
     *
     * @return LOG_METHOD_NAME
     */
    public static boolean getLogMethodName() {
        return LOG_METHOD_NAME;
    }

    /**
     * 设置通用日志是输出行号，true输出，false不输出
     *
     * @param logLineNumber
     */
    public static void setLogLineNumber(boolean logLineNumber) {
        LOG_LINE_NUMBER = logLineNumber;
    }

    /**
     * 获取通用日志是输出行号，true输出，false不输出
     *
     * @return LOG_LINE_NUMBER
     */
    public static boolean getLogLineNumber() {
        return LOG_LINE_NUMBER;
    }

    /**
     * 设置通用日志输出时间格式，不为空按照格式输出时间，为空不输出时间
     *
     * @param logDateFormat
     */
    public static void setLogDateFormat(String logDateFormat) {
        if (null == logDateFormat || logDateFormat.length() <= 0) {
            LOG_DATE_FORMAT = null;
        } else {
            try {
                LOG_DATE_FORMAT = new SimpleDateFormat(logDateFormat);
            } catch (Exception e) {
                LOG_DATE_FORMAT = null;
            }
        }

    }

    /**
     * 获取通用日志输出时间格式，不为空按照格式输出时间，为空不输出时间
     *
     * @return LOG_DATE_FORMAT
     */
    public static SimpleDateFormat getLogDateFormat() {
        return LOG_DATE_FORMAT;
    }

}
