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

public class LogcatAndroidImpl extends LoggerCommonImpl {
    private String loggerName;

    public LogcatAndroidImpl(String loggerName) {
        super(loggerName);
        this.loggerName = loggerName;
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
        StringBuilder sbAndroid = new StringBuilder();
        // 设置日志时间，android自带有时间，不用设置时间
        // 设置日志级别信息，android自带有级别信息，无需设置
        // 依据通用设置获取日志打印选项
        boolean isLogClassName = LoggerConfig.getLogClassName();
        boolean isLogMethodName = LoggerConfig.getLogMethodName();
        boolean isLogLineNumber = LoggerConfig.getLogLineNumber();
        if (isLogClassName || isLogMethodName || isLogLineNumber) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement stackTraceElement = FqcnUtil.getStackTraceElementByFqcn(stackTraceElements, parseFqcn(fqcn), this.loggerName);
            // android 设置为类名称简称
            String logClassName = FqcnUtil.parseSimpleClassName(null == stackTraceElement ? null : stackTraceElement.getClassName());
            String logMethodName = null == stackTraceElement ? null : stackTraceElement.getMethodName();
            int logLineNumber = null == stackTraceElement ? null : stackTraceElement.getLineNumber();
            // 设置日志类
            if (isLogClassName && null != logClassName && logClassName.length() > 0) {
                if (sbAndroid.length() > 0) {
                    sbAndroid.append(":");
                }
                sbAndroid.append(logClassName);
            }
            // 设置日志方法
            if (isLogMethodName && null != logMethodName && logMethodName.length() > 0) {
                if (sbAndroid.length() > 0) {
                    sbAndroid.append(":");
                }
                sbAndroid.append(logMethodName);
            }
            // 设置日志行数
            if (isLogLineNumber && logLineNumber > 0) {
                if (sbAndroid.length() > 0) {
                    sbAndroid.append(":");
                }
                sbAndroid.append(String.format("%04d", logLineNumber));
            }
        }
        // 拼接日志
        if (sbAndroid.length() <= 0) {
            if (null != this.loggerName && this.loggerName.length() > 0) {
                sbAndroid.append(FqcnUtil.parseSimpleClassName(this.loggerName));
            } else {
                if (null == loggerLevel) {
                    sbAndroid.append("INFO");
                } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
                    sbAndroid.append("TRACE");
                } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
                    if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                        sbAndroid.append("INFO");
                    } else {
                        sbAndroid.append("DEBUG");
                    }
                } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
                    sbAndroid.append("INFO");
                } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
                    sbAndroid.append("WARN");
                } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
                    sbAndroid.append("ERROR");
                } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
                    sbAndroid.append("FATAL");
                } else {
                    sbAndroid.append("INFO");
                }
            }
        }
        // 转换为日志标题信息
        String loggerTagStr = sbAndroid.toString();

        if (null == loggerLevel) {
            if (!isInfoEnabled()) {
                return;
            }
            if (msg != null && e != null) {
                android.util.Log.i(loggerTagStr, msg, e);
            } else if (msg != null) {
                android.util.Log.i(loggerTagStr, msg);
            } else if (e != null) {
                android.util.Log.i(loggerTagStr, "Throwable Error!", e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (!isTraceEnabled()) {
                return;
            }
            if (msg != null && e != null) {
                android.util.Log.v(loggerTagStr, msg, e);
            } else if (msg != null) {
                android.util.Log.v(loggerTagStr, msg);
            } else if (e != null) {
                android.util.Log.v(loggerTagStr, "Throwable Error!", e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                if (!isInfoEnabled()) {
                    return;
                }
                if (msg != null && e != null) {
                    android.util.Log.i(loggerTagStr, msg, e);
                } else if (msg != null) {
                    android.util.Log.i(loggerTagStr, msg);
                } else if (e != null) {
                    android.util.Log.i(loggerTagStr, "Throwable Error!", e);
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == tmpDebugLevel) {
                if (!isDebugEnabled()) {
                    return;
                }
                if (msg != null && e != null) {
                    android.util.Log.d(loggerTagStr, msg, e);
                } else if (msg != null) {
                    android.util.Log.d(loggerTagStr, msg);
                } else if (e != null) {
                    android.util.Log.d(loggerTagStr, "Throwable Error!", e);
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (!isInfoEnabled()) {
                return;
            }
            if (msg != null && e != null) {
                android.util.Log.i(loggerTagStr, msg, e);
            } else if (msg != null) {
                android.util.Log.i(loggerTagStr, msg);
            } else if (e != null) {
                android.util.Log.i(loggerTagStr, "Throwable Error!", e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (!isWarnEnabled()) {
                return;
            }
            if (msg != null && e != null) {
                android.util.Log.w(loggerTagStr, msg, e);
            } else if (msg != null) {
                android.util.Log.w(loggerTagStr, msg);
            } else if (e != null) {
                android.util.Log.w(loggerTagStr, "Throwable Error!", e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (!isErrorEnabled()) {
                return;
            }
            if (msg != null && e != null) {
                android.util.Log.e(loggerTagStr, msg, e);
            } else if (msg != null) {
                android.util.Log.e(loggerTagStr, msg);
            } else if (e != null) {
                android.util.Log.e(loggerTagStr, "Throwable Error!", e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (!isFatalEnabled()) {
                return;
            }
            if (msg != null && e != null) {
                android.util.Log.e(loggerTagStr, msg, e);
            } else if (msg != null) {
                android.util.Log.e(loggerTagStr, msg);
            } else if (e != null) {
                android.util.Log.e(loggerTagStr, "Throwable Error!", e);
            }
        }
    }

}
