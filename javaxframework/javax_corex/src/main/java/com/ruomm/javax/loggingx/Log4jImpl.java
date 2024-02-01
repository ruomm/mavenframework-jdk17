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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jImpl extends LoggerCommonImpl {


    private Logger log;

    /**
     * @param log
     * @since 0.2.21
     */
    public Log4jImpl(Logger log) {
        super(log.getName());
        this.log = log;
    }

    public Log4jImpl(String loggerName) {
        super(loggerName);
        log = Logger.getLogger(loggerName);
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isEnabledFor(Level.TRACE);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isEnabledFor(Level.DEBUG);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isEnabledFor(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabledFor(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isEnabledFor(Level.ERROR);
    }

    @Override
    public boolean isFatalEnabled() {
        return log.isEnabledFor(Level.FATAL);
    }

    public Logger getLog() {
        return log;
    }

    @Override
    protected void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e) {
        doLoggerCounter(loggerLevel);
        if (null == loggerLevel) {
            if (null == e) {
                log.log(parseFqcn(fqcn), Level.INFO, msg, e);
            } else {
                log.log(parseFqcn(fqcn), Level.INFO, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (null == e) {
                log.log(parseFqcn(fqcn), Level.TRACE, msg, e);
            } else {
                log.log(parseFqcn(fqcn), Level.TRACE, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            LoggerConfig.DebugLevel tmpDebugLevel = LoggerConfig.getDebugLevel();
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                if (null == e) {
                    log.log(parseFqcn(fqcn), Level.INFO, msg, e);
                } else {
                    log.log(parseFqcn(fqcn), Level.INFO, msg, e);
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == tmpDebugLevel) {
                if (null == e) {
                    log.log(parseFqcn(fqcn), Level.DEBUG, msg, e);
                } else {
                    log.log(parseFqcn(fqcn), Level.DEBUG, msg, e);
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (null == e) {
                log.log(parseFqcn(fqcn), Level.INFO, msg, e);
            } else {
                log.log(parseFqcn(fqcn), Level.INFO, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (null == e) {
                log.log(parseFqcn(fqcn), Level.WARN, msg, e);
            } else {
                log.log(parseFqcn(fqcn), Level.WARN, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (null == e) {
                log.log(parseFqcn(fqcn), Level.ERROR, msg, e);
            } else {
                log.log(parseFqcn(fqcn), Level.ERROR, msg, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (null == e) {
                log.log(parseFqcn(fqcn), Level.FATAL, msg, e);
            } else {
                log.log(parseFqcn(fqcn), Level.FATAL, msg, e);
            }
        }
    }

}
