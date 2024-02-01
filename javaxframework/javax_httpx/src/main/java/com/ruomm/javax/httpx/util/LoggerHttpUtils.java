package com.ruomm.javax.httpx.util;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.loggingx.Log;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/14 14:12
 */
public class LoggerHttpUtils {
    private final static String callerFQCN = LoggerHttpUtils.class.getName();

    /**
     * 日志输出
     *
     * @param log         日志工厂类
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag   logger日志输出的标记，默认为类简称
     * @param msg         消息内容
     */
    public static void loggerMessage(Log log, HttpConfig.LoggerLevel loggerLevel, String loggerTag, String msg) {
        if (null == loggerLevel || HttpConfig.LoggerLevel.OFF == loggerLevel) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.nullStrToEmpty(loggerTag)).append(".");
        sb.append(StringUtils.nullStrToEmpty(msg));
        if (HttpConfig.LoggerLevel.INFO == loggerLevel) {
            log.infoFqcn(callerFQCN, sb.toString());
        } else {
            log.debugFqcn(callerFQCN, sb.toString());
        }
    }

    /**
     * 日志输出
     *
     * @param msgHeader 消息头
     * @param msg       消息内容
     */
    public static void loggerMessage(Log log, HttpConfig.LoggerLevel loggerLevel, String loggerTag, String msgHeader, String msg) {
        loggerMessage(log, loggerLevel, loggerTag, msgHeader, -99999999, msg);
    }

    /**
     * 日志输出
     *
     * @param msgHeader 消息头
     * @param status    消息状态
     * @param msg       消息内容
     */
    public static void loggerMessage(Log log, HttpConfig.LoggerLevel loggerLevel, String loggerTag, String msgHeader, int status, String msg) {
        if (null == loggerLevel || HttpConfig.LoggerLevel.OFF == loggerLevel) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.nullStrToEmpty(loggerTag));
        if (!StringUtils.isEmpty(msgHeader)) {
            sb.append(".");
            sb.append(msgHeader);
        }
        sb.append("->");
        if (status > -999999) {
            sb.append("status:");
            sb.append(status);
            sb.append(";msg:");
        }
        sb.append(StringUtils.nullStrToEmpty(msg));
        if (HttpConfig.LoggerLevel.INFO == loggerLevel) {
            log.infoFqcn(callerFQCN, sb.toString());
        } else {
            log.debugFqcn(callerFQCN, sb.toString());
        }
    }
}
