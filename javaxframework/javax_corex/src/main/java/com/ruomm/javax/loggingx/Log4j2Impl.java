package com.ruomm.javax.loggingx;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;

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
public class Log4j2Impl extends LoggerCommonImpl {
    private Logger log;

    /**
     * @param log
     * @since 0.2.21
     */
    public Log4j2Impl(Logger log) {
        super(log.getName());
        this.log = log;

    }

    public Log4j2Impl(String loggerName) {
        super(loggerName);
        log = LogManager.getLogger(loggerName);
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return log.isFatalEnabled();
    }


    public Logger getLog() {
        return log;
    }

    @Override
    public String toString() {
        return log.toString();
    }

    @Override
    protected void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e) {
        doLoggerCounter(loggerLevel);
        AbstractLogger absLog = (AbstractLogger) this.log;
        if (null == loggerLevel) {
            if (null == e) {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.INFO, null, msg);
            } else {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.INFO, null, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (null == e) {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.TRACE, null, msg);
            } else {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.TRACE, null, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            LoggerConfig.DebugLevel tmpDebugLevel = LoggerConfig.getDebugLevel();
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                if (null == e) {
                    absLog.logIfEnabled(parseFqcn(fqcn), Level.INFO, null, msg);
                } else {
                    absLog.logIfEnabled(parseFqcn(fqcn), Level.INFO, null, msg, e);
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == tmpDebugLevel) {
                if (null == e) {
                    absLog.logIfEnabled(parseFqcn(fqcn), Level.DEBUG, null, msg);
                } else {
                    absLog.logIfEnabled(parseFqcn(fqcn), Level.DEBUG, null, msg, e);
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (null == e) {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.INFO, null, msg);
            } else {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.INFO, null, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (null == e) {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.WARN, null, msg);
            } else {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.WARN, null, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (null == e) {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.ERROR, null, msg);
            } else {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.ERROR, null, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (null == e) {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.FATAL, null, msg);
            } else {
                absLog.logIfEnabled(parseFqcn(fqcn), Level.FATAL, null, msg, e);
            }
        }
    }
}
