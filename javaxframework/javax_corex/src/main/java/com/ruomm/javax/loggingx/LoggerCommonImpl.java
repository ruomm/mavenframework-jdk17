package com.ruomm.javax.loggingx;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/15 9:14
 */
public abstract class LoggerCommonImpl implements Log {
    protected String callerFQCN = LoggerCommonImpl.class.getName();
    protected String loggerName = null;
    protected int traceCount;
    protected int debugCount;
    protected int infoCount;
    protected int warnCount;
    protected int errorCount;
    protected int fatalCount;

    private LoggerCommonImpl() {
//        this.callerFQCN=this.getClass().getName();
    }

    public LoggerCommonImpl(String loggerName) {
//        this.callerFQCN=this.getClass().getName();
        this.loggerName = loggerName;
    }

    @Override
    public void resetStat() {
        this.traceCount = 0;
        this.debugCount = 0;
        this.infoCount = 0;
        this.warnCount = 0;
        this.errorCount = 0;
        this.fatalCount = 0;
    }

    @Override
    public int getTraceCount() {
        return this.traceCount;
    }

    @Override
    public int getDebugCount() {
        return this.debugCount;
    }

    @Override
    public int getInfoCount() {
        return this.infoCount;
    }

    @Override
    public int getWarnCount() {
        return this.warnCount;
    }

    @Override
    public int getErrorCount() {
        return this.errorCount;
    }

    @Override
    public int getFatalCount() {
        return this.fatalCount;
    }

    @Override
    public void trace(String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.TRACE, parseFqcn(null), msg, null);
    }

    @Override
    public void trace(String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.TRACE, parseFqcn(null), msg, e);
    }

    @Override
    public void debug(String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.DEBUG, parseFqcn(null), msg, null);
    }

    @Override
    public void debug(String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.DEBUG, parseFqcn(null), msg, e);
    }

    @Override
    public void info(String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.INFO, parseFqcn(null), msg, null);
    }

    @Override
    public void info(String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.INFO, parseFqcn(null), msg, e);
    }

    @Override
    public void warn(String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.WARN, parseFqcn(null), msg, null);
    }

    @Override
    public void warn(String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.WARN, parseFqcn(null), msg, e);
    }

    @Override
    public void error(String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.ERROR, parseFqcn(null), msg, null);
    }

    @Override
    public void error(String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.ERROR, parseFqcn(null), msg, e);
    }

    @Override
    public void fatal(String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.FATAL, parseFqcn(null), msg, null);
    }

    @Override
    public void fatal(String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.FATAL, parseFqcn(null), msg, e);
    }

    @Override
    public void traceFqcn(String fqcn, String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.TRACE, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void traceFqcn(String fqcn, String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.TRACE, parseFqcn(fqcn), msg, e);
    }

    @Override
    public void debugFqcn(String fqcn, String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.DEBUG, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void debugFqcn(String fqcn, String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.DEBUG, parseFqcn(fqcn), msg, e);
    }

    @Override
    public void infoFqcn(String fqcn, String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.INFO, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void infoFqcn(String fqcn, String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.INFO, parseFqcn(fqcn), msg, e);
    }

    @Override
    public void warnFqcn(String fqcn, String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.WARN, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void warnFqcn(String fqcn, String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.WARN, parseFqcn(fqcn), msg, e);
    }

    @Override
    public void errorFqcn(String fqcn, String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.ERROR, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void errorFqcn(String fqcn, String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.ERROR, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void fatalFqcn(String fqcn, String msg) {
        loggerMessage(LoggerConfig.LoggerLevel.FATAL, parseFqcn(fqcn), msg, null);
    }

    @Override
    public void fatalFqcn(String fqcn, String msg, Throwable e) {
        loggerMessage(LoggerConfig.LoggerLevel.FATAL, parseFqcn(fqcn), msg, e);
    }

    @Override
    public boolean isLogEnabled(LoggerConfig.LoggerLevel loggerLevel, LoggerConfig.DebugLevel debugLevel) {
        LoggerConfig.LoggerLevel level = null == loggerLevel ? LoggerConfig.LoggerLevel.INFO : loggerLevel;
        if (null == loggerLevel) {
            return isInfoEnabled();
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            return isTraceEnabled();
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            if (LoggerConfig.DebugLevel.INFO == debugLevel) {
                return isInfoEnabled();
            } else if (LoggerConfig.DebugLevel.DEBUG == debugLevel) {
                return isDebugEnabled();
            } else {
                return false;
            }
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            return isInfoEnabled();
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            return isWarnEnabled();
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            return isErrorEnabled();
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            return isFatalEnabled();
        } else if (loggerLevel == LoggerConfig.LoggerLevel.OFF) {
            return false;
        } else {
            return true;
        }
    }

    protected void doLoggerCounter(LoggerConfig.LoggerLevel loggerLevel) {
        if (null == loggerLevel) {
            infoCount++;
        } else if (loggerLevel == LoggerConfig.LoggerLevel.TRACE) {
            traceCount++;
        } else if (loggerLevel == LoggerConfig.LoggerLevel.DEBUG) {
            debugCount++;
        } else if (loggerLevel == LoggerConfig.LoggerLevel.INFO) {
            infoCount++;
        } else if (loggerLevel == LoggerConfig.LoggerLevel.WARN) {
            warnCount++;
        } else if (loggerLevel == LoggerConfig.LoggerLevel.ERROR) {
            errorCount++;
        } else if (loggerLevel == LoggerConfig.LoggerLevel.FATAL) {
            fatalCount++;
        }
    }

    /**
     * 依据级别判定日志是否开启
     *
     * @param loggerLevel
     * @return
     */
    protected boolean parseLoggerLevelEnable(LoggerConfig.LoggerLevel loggerLevel) {
        LoggerConfig.LoggerLevel inputLevel = null == loggerLevel ? LoggerConfig.LoggerLevel.ALL : loggerLevel;
        LoggerConfig.LoggerLevel tmpLevel = LoggerConfig.getLoggerLevel();
        LoggerConfig.LoggerLevel commonLevel = null == tmpLevel ? LoggerConfig.LoggerLevel.ALL : tmpLevel;
        return loggerLevel.getLevel() >= commonLevel.getLevel();
    }

    /**
     * 解析日志FQCN
     *
     * @param fqcn 输入的FAQCN
     * @return
     * @paramDefault commFqcn 默认的FQCN-callerFQCN
     */
    public String parseFqcn(String fqcn) {
        if (null == fqcn || fqcn.length() <= 0) {
            return this.callerFQCN;
        } else {
            return fqcn;
        }
    }


    protected abstract void loggerMessage(LoggerConfig.LoggerLevel loggerLevel, String fqcn, String msg, Throwable e);


}
