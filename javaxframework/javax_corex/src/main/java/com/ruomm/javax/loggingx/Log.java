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

public interface Log {
    void resetStat();

    public boolean isLogEnabled(LoggerConfig.LoggerLevel loggerLevel, LoggerConfig.DebugLevel debugLevel);

    boolean isTraceEnabled();

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isWarnEnabled();

    boolean isErrorEnabled();

    boolean isFatalEnabled();

    int getTraceCount();

    int getDebugCount();

    int getInfoCount();

    int getWarnCount();

    int getErrorCount();

    int getFatalCount();

    void trace(String msg);

    void trace(String msg, Throwable e);

    void debug(String msg);

    void debug(String msg, Throwable e);

    void info(String msg);

    void info(String msg, Throwable e);

    void warn(String msg);

    void warn(String msg, Throwable e);


    void error(String msg);

    void error(String msg, Throwable e);

    void fatal(String msg);

    void fatal(String msg, Throwable e);

    void traceFqcn(String fqcn, String msg);

    void traceFqcn(String fqcn, String msg, Throwable e);

    void debugFqcn(String fqcn, String msg);

    void debugFqcn(String fqcn, String msg, Throwable e);

    void infoFqcn(String fqcn, String msg);

    void infoFqcn(String fqcn, String msg, Throwable e);

    void warnFqcn(String fqcn, String msg);

    void warnFqcn(String fqcn, String msg, Throwable e);

    void errorFqcn(String fqcn, String msg);

    void errorFqcn(String fqcn, String msg, Throwable e);

    void fatalFqcn(String fqcn, String msg);

    void fatalFqcn(String fqcn, String msg, Throwable e);

}
