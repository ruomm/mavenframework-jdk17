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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JakartaCommonsLoggingImpl extends LoggerCommonImpl {
    private Log log;

    /**
     * @param log
     * @since 0.2.1
     */
    public JakartaCommonsLoggingImpl(Log log) {
        super(null);
        this.log = log;
    }

    public JakartaCommonsLoggingImpl(String loggerName) {
        super(loggerName);
        log = LogFactory.getLog(loggerName);
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

    @Override
    protected void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e) {
        doLoggerCounter(loggerLevel);
        if (null == loggerLevel) {
            if (null == e) {
                log.info(msg);
            } else {
                log.info(msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (null == e) {
                log.trace(msg);
            } else {
                log.trace(msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            LoggerConfig.DebugLevel debugLevel = LoggerConfig.getDebugLevel();
            if (LoggerConfig.DebugLevel.INFO == debugLevel) {
                if (null == e) {
                    log.info(msg);
                } else {
                    log.info(msg, e);
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == debugLevel) {
                if (null == e) {
                    log.debug(msg);
                } else {
                    log.debug(msg, e);
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (null == e) {
                log.info(msg);
            } else {
                log.info(msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (null == e) {
                log.warn(msg);
            } else {
                log.warn(msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (null == e) {
                log.error(msg);
            } else {
                log.error(msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (null == e) {
                log.fatal(msg);
            } else {
                log.fatal(msg, e);
            }
        }
    }


}
