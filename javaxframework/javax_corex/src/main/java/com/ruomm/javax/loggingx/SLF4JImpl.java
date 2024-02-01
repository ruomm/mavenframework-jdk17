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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

public class SLF4JImpl extends LoggerCommonImpl {

    private final static Logger testLogger = LoggerFactory.getLogger(SLF4JImpl.class);

    static {
        // if the logger is not a LocationAwareLogger instance, it can not get correct stack StackTraceElement
        // so ignore this implementation.
        if (!(testLogger instanceof LocationAwareLogger)) {
            throw new UnsupportedOperationException(testLogger.getClass() + " is not a suitable logger");
        }
    }

    private LocationAwareLogger log;

    public SLF4JImpl(LocationAwareLogger log) {
        super(log.getName());
        this.log = log;
    }

    public SLF4JImpl(String loggerName) {
        super(loggerName);
        this.log = (LocationAwareLogger) LoggerFactory.getLogger(loggerName);
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
        return log.isErrorEnabled();
    }


    @Override
    protected void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e) {
        doLoggerCounter(loggerLevel);
        if (null == loggerLevel) {
            if (null == e) {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.INFO_INT, msg, null, null);
            } else {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.INFO_INT, msg, null, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            if (null == e) {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.TRACE_INT, msg, null, null);
            } else {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.TRACE_INT, msg, null, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            LoggerConfig.DebugLevel tmpDebugLevel = LoggerConfig.getDebugLevel();
            if (LoggerConfig.DebugLevel.INFO == tmpDebugLevel) {
                if (null == e) {
                    log.log(null, parseFqcn(fqcn), LocationAwareLogger.INFO_INT, msg, null, null);
                } else {
                    log.log(null, parseFqcn(fqcn), LocationAwareLogger.INFO_INT, msg, null, e);
                }
            } else if (LoggerConfig.DebugLevel.DEBUG == tmpDebugLevel) {
                if (null == e) {
                    log.log(null, parseFqcn(fqcn), LocationAwareLogger.DEBUG_INT, msg, null, null);
                } else {
                    log.log(null, parseFqcn(fqcn), LocationAwareLogger.DEBUG_INT, msg, null, e);
                }
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            if (null == e) {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.INFO_INT, msg, null, null);
            } else {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.INFO_INT, msg, null, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            if (null == e) {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.WARN_INT, msg, null, null);
            } else {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.WARN_INT, msg, null, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            if (null == e) {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.ERROR_INT, msg, null, null);
            } else {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.ERROR_INT, msg, null, e);
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            if (null == e) {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.ERROR_INT, msg, null, null);
            } else {
                log.log(null, parseFqcn(fqcn), LocationAwareLogger.ERROR_INT, msg, null, e);
            }
        }
    }

}
