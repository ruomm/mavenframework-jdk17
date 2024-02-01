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

import java.util.logging.Level;
import java.util.logging.Logger;

public class Jdk14LoggingImpl extends LoggerCommonImpl {
    //	SEVERE (highest value)
//	WARNING
//	INFO
//	CONFIG
//	FINE
//	FINER
//	FINEST (lowest value)
    private Logger log;
    private String loggerName;
    private LoggerConfig.LoggerLevel commLevel = null;

    public Jdk14LoggingImpl(Logger log) {
        super(null);
        this.log = log;
    }

    public Jdk14LoggingImpl(String loggerName) {
        super(loggerName);
        this.log = Logger.getLogger(loggerName);
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isLoggable(Level.FINEST);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isLoggable(Level.FINE);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isLoggable(Level.SEVERE);
    }


    @Override
    public boolean isFatalEnabled() {
        return log.isLoggable(Level.SEVERE);
    }

    @Override
    protected void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e) {
        doLoggerCounter(loggerLevel);
        //依据级别设置log
        LoggerConfig.LoggerLevel tmpLevel = LoggerConfig.getLoggerLevel();
        if (null == tmpLevel || null == commLevel || tmpLevel != commLevel) {
            commLevel = tmpLevel;
            if (null == commLevel) {
                this.log.setLevel(Level.INFO);
            } else if (commLevel == LoggerConfig.LoggerLevel.ALL) {
                this.log.setLevel(Level.FINEST);
            } else if (commLevel == LoggerConfig.LoggerLevel.TRACE) {
                this.log.setLevel(Level.FINEST);
            } else if (commLevel == LoggerConfig.LoggerLevel.DEBUG) {
                this.log.setLevel(Level.FINE);
            } else if (commLevel == LoggerConfig.LoggerLevel.INFO) {
                this.log.setLevel(Level.INFO);
            } else if (commLevel == LoggerConfig.LoggerLevel.WARN) {
                this.log.setLevel(Level.WARNING);
            } else if (commLevel == LoggerConfig.LoggerLevel.ERROR) {
                this.log.setLevel(Level.SEVERE);
            } else if (commLevel == LoggerConfig.LoggerLevel.FATAL) {
                this.log.setLevel(Level.SEVERE);
            } else {
                this.log.setLevel(Level.INFO);
            }
        }
        LoggerConfig.DebugLevel tmpDebugLevel = LoggerConfig.getDebugLevel();
        // 级别没有激活时候不输出日志
        if (!isLogEnabled(loggerLevel, tmpDebugLevel)) {
            return;
        }
        // 日志关闭时候不输出日志
        if (null != commLevel && commLevel == LoggerConfig.LoggerLevel.OFF) {
            return;
        }
        //依据FNCQ获取StackTraceElement
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        StackTraceElement stackTraceElement = FqcnUtil.getStackTraceElementByFqcn(stackTraceElements, parseFqcn(fqcn), this.loggerName);

        String sourceClass = null == stackTraceElement ? null : stackTraceElement.getClassName();
        String sourceMethod = null == stackTraceElement ? null : stackTraceElement.getMethodName();
        if (null == loggerLevel) {
            if (null == e) {
//				log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), msg, e);
                log.logp(Level.INFO, sourceClass, sourceMethod, msg);
            } else {
                log.logp(Level.INFO, sourceClass, sourceMethod, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (null == e) {
                log.logp(Level.FINEST, sourceClass, sourceMethod, msg);
            } else {
                log.logp(Level.FINEST, sourceClass, sourceMethod, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                if (null == e) {
                    log.logp(Level.INFO, sourceClass, sourceMethod, msg);
                } else {
                    log.logp(Level.INFO, sourceClass, sourceMethod, msg, e);
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == tmpDebugLevel) {
                if (null == e) {
                    log.logp(Level.FINE, sourceClass, sourceMethod, msg);
                } else {
                    log.logp(Level.FINE, sourceClass, sourceMethod, msg, e);
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (null == e) {
                log.logp(Level.INFO, sourceClass, sourceMethod, msg);
            } else {
                log.logp(Level.INFO, sourceClass, sourceMethod, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (null == e) {
                log.logp(Level.WARNING, sourceClass, sourceMethod, msg);
            } else {
                log.logp(Level.WARNING, sourceClass, sourceMethod, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (null == e) {
                log.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
            } else {
                log.logp(Level.SEVERE, sourceClass, sourceMethod, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (null == e) {
                log.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
            } else {
                log.logp(Level.SEVERE, sourceClass, sourceMethod, msg, e);
            }
        }
    }

}
