/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruomm.javax.loggingx;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoLoggingImpl extends LoggerCommonImpl {

    private final static SimpleDateFormat NoLog_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    public NoLoggingImpl(String loggerName) {
        super(loggerName);
    }


    @Override
    public boolean isTraceEnabled() {
        return parseLoggerLevelEnable(LoggerConfig.LoggerLevel.TRACE);
    }

    @Override
    public boolean isDebugEnabled() {
        return parseLoggerLevelEnable(LoggerConfig.LoggerLevel.DEBUG);
    }

    @Override
    public boolean isInfoEnabled() {
        return parseLoggerLevelEnable(LoggerConfig.LoggerLevel.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return parseLoggerLevelEnable(LoggerConfig.LoggerLevel.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return parseLoggerLevelEnable(LoggerConfig.LoggerLevel.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return parseLoggerLevelEnable(LoggerConfig.LoggerLevel.FATAL);
    }

    @Override
    protected void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e) {
        doLoggerCounter(loggerLevel);
        LoggerConfig.DebugLevel tmpDebugLevel = LoggerConfig.getDebugLevel();
        if (!isLogEnabled(loggerLevel, tmpDebugLevel)) {
            return;
        }

// 依据级别设置日志标题信息
        StringBuilder sb = new StringBuilder();
        // 设置日志时间，android自带有时间，不用设置时间
        SimpleDateFormat logDateFormat = LoggerConfig.getLogDateFormat();
        if (null != logDateFormat) {
            sb.append(logDateFormat.format(new Date()));
            sb.append(" ");
        }
        // 设置日志级别信息，android自带有级别信息，无需设置
        sb.append("[");
        if (null == loggerLevel) {
            sb.append("INFO");
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            sb.append("TRACE");
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                sb.append("INFO");
            } else {
                sb.append("DEBUG");
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            sb.append("INFO");
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            sb.append("WARN");
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            sb.append("ERROR");
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            sb.append("FATAL");
        } else {
            sb.append("INFO");
        }
        sb.append("] ");
        // 依据通用设置获取日志打印选项
        boolean isLogClassName = LoggerConfig.getLogClassName();
        boolean isLogMethodName = LoggerConfig.getLogMethodName();
        boolean isLogLineNumber = LoggerConfig.getLogLineNumber();

        StringBuilder sbTag = new StringBuilder();
        if (isLogClassName || isLogMethodName || isLogLineNumber) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement stackTraceElement = FqcnUtil.getStackTraceElementByFqcn(stackTraceElements, parseFqcn(fqcn), this.loggerName);
            // java 依据HttpConfig里面的配置获取类全称或简称
            String logClassName = null;
            if (LoggerConfig.getLogPrintFullClass()) {
                logClassName = null == stackTraceElement ? null : stackTraceElement.getClassName();
            } else {
                logClassName = FqcnUtil.parseSimpleClassName(null == stackTraceElement ? null : stackTraceElement.getClassName());
            }
            String logMethodName = null == stackTraceElement ? null : stackTraceElement.getMethodName();
            int logLineNumber = null == stackTraceElement ? null : stackTraceElement.getLineNumber();
            // 设置日志类
            if (isLogClassName && null != logClassName && logClassName.length() > 0) {
                if (sbTag.length() > 0) {
                    sbTag.append(":");
                }
                sbTag.append(logClassName);
            }
            // 设置日志方法
            if (isLogMethodName && null != logMethodName && logMethodName.length() > 0) {
                if (sbTag.length() > 0) {
                    sbTag.append(":");
                }
                sbTag.append(logMethodName);
            }
            // 设置日志行数
            if (isLogLineNumber && logLineNumber > 0) {
                if (sbTag.length() > 0) {
                    sbTag.append(":");
                }
                sbTag.append(String.format("%04d", logLineNumber));
            }
        }
        // 拼接日志
        if (sbTag.length() > 0) {
            sb.append("[").append(sbTag.toString()).append("] ");
        }
        // 转换为日志标题信息
        String loggerTagStr = sb.toString();

        if (null == loggerLevel) {
            if (msg != null) {
                System.out.println(loggerTagStr + msg);
            }
            if (e != null) {
                e.printStackTrace();
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (msg != null) {
                System.out.println(loggerTagStr + msg);
            }
            if (e != null) {
                e.printStackTrace();
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                if (msg != null) {
                    System.out.println(loggerTagStr + msg);
                }
                if (e != null) {
                    e.printStackTrace();
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == tmpDebugLevel) {
                if (msg != null) {
                    System.out.println(loggerTagStr + msg);
                }
                if (e != null) {
                    e.printStackTrace();
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (msg != null) {
                System.out.println(loggerTagStr + msg);
            }
            if (e != null) {
                e.printStackTrace();
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (msg != null) {
                System.err.println(loggerTagStr + msg);
            }
            if (e != null) {
                e.printStackTrace();
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (msg != null) {
                System.err.println(loggerTagStr + msg);
            }
            if (e != null) {
                e.printStackTrace();
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (msg != null) {
                System.err.println(loggerTagStr + msg);
            }
            if (e != null) {
                e.printStackTrace();
            }
        }
    }
}
