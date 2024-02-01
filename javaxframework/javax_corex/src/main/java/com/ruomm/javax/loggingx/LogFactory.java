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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class LogFactory {
    public final static String LOG_CONFIG_NAME = "javax.logging.logType";
    public final static String log_type_slf4j = "slf4j";
    public final static String log_type_log4j = "log4j";
    public final static String log_type_log4j2 = "log4j2";
    public final static String log_type_commonsLog = "commonsLog";
    public final static String log_type_jdkLog = "jdkLog";
    public final static String log_type_sysout = "sysout";
    private static Constructor logConstructor;

    private static void initLog() {

        logConstructor = null;
        // 判断是否安卓
        boolean isAndroid = false;
        try {

            Class<?> clsLog = Class.forName("android.util.Log");
            Class<?> clsIntent = Class.forName("android.content.Intent");
            if (null != clsLog && null != clsIntent) {
                clsLog = null;
                isAndroid = true;

            }
        } catch (Exception e) {
        }
        // 如果是安卓调用logcat方法
        if (isAndroid) {
            if (logConstructor == null) {
                try {
                    logConstructor = LogcatAndroidImpl.class.getConstructor(String.class);
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
        if (null != logConstructor) {
            return;
        }
        String logPackageName = null;
        try {
            logPackageName = LogFactory.class.getPackage().getName() + ".";
        } catch (Exception e) {
            // TODO: handle exception
        }
        String logTypeInput = null;
        List<String> list = readInputStreamToList(LogFactory.class.getResourceAsStream("/" + LOG_CONFIG_NAME), "utf-8",
                false);
        if (null != list && list.size() > 0) {
            logTypeInput = list.get(0);
        }
        String logTypeSys = System.getProperty(LOG_CONFIG_NAME);

        if (logTypeInput != null) {
            if (logTypeInput.equalsIgnoreCase(log_type_slf4j)) {
                tryImplementation("org.slf4j.Logger", logPackageName + "SLF4JImpl");
            } else if (logTypeInput.equalsIgnoreCase(log_type_log4j)) {
                tryImplementation("org.apache.log4j.Logger", logPackageName + "Log4jImpl");
            } else if (logTypeInput.equalsIgnoreCase(log_type_log4j2)) {
                tryImplementation("org.apache.logging.log4j.Logger", logPackageName + "Log4j2Impl");
            } else if (logTypeInput.equalsIgnoreCase(log_type_commonsLog)) {
                tryImplementation("org.apache.commons.logging.LogFactory",
                        logPackageName + "JakartaCommonsLoggingImpl");
            } else if (logTypeInput.equalsIgnoreCase(log_type_jdkLog)) {
                tryImplementation("java.util.logging.Logger", logPackageName + "Jdk14LoggingImpl");
            } else if (logTypeInput.equalsIgnoreCase(log_type_sysout)) {
                try {
                    logConstructor = NoLoggingImpl.class.getConstructor(String.class);
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }
        if (logTypeSys != null) {
            if (logTypeSys.equalsIgnoreCase(log_type_slf4j)) {
                tryImplementation("org.slf4j.Logger", logPackageName + "SLF4JImpl");
            } else if (logTypeSys.equalsIgnoreCase(log_type_log4j)) {
                tryImplementation("org.apache.log4j.Logger", logPackageName + "Log4jImpl");
            } else if (logTypeSys.equalsIgnoreCase(log_type_log4j2)) {
                tryImplementation("org.apache.logging.log4j.Logger", logPackageName + "Log4j2Impl");
            } else if (logTypeSys.equalsIgnoreCase(log_type_commonsLog)) {
                tryImplementation("org.apache.commons.logging.LogFactory",
                        logPackageName + "JakartaCommonsLoggingImpl");
            } else if (logTypeSys.equalsIgnoreCase(log_type_jdkLog)) {
                tryImplementation("java.util.logging.Logger", logPackageName + "Jdk14LoggingImpl");
            } else if (logTypeSys.equalsIgnoreCase(log_type_sysout)) {
                try {
                    logConstructor = NoLoggingImpl.class.getConstructor(String.class);
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }

        // 优先选择log4j,而非Apache Common Logging. 因为后者无法设置真实Log调用者的信息
        tryImplementation("org.slf4j.Logger", logPackageName + "SLF4JImpl");
        tryImplementation("org.apache.log4j.Logger", logPackageName + "Log4jImpl");
        tryImplementation("org.apache.logging.log4j.Logger", logPackageName + "Log4j2Impl");
        tryImplementation("org.apache.commons.logging.LogFactory", logPackageName + "JakartaCommonsLoggingImpl");
        tryImplementation("java.util.logging.Logger", logPackageName + "Jdk14LoggingImpl");

        if (logConstructor == null) {
            try {
                logConstructor = NoLoggingImpl.class.getConstructor(String.class);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public static List<String> readInputStreamToList(InputStream is, String charsetName, boolean isPutEmptyLine) {
        if (null == is) {
            return null;
        }
        BufferedReader bufreader = null;
        List<String> realContent = null;
        try {
            List<String> fileContent = new ArrayList<String>();
            Charset charset = null == charsetName || charsetName.length() <= 0 || !Charset.isSupported(charsetName)
                    ? Charset.forName("UTF-8")
                    : Charset.forName(charsetName);
            bufreader = new BufferedReader(new InputStreamReader(is, charset));
            String lineStr = null;
            boolean isFirst = true;
            while ((lineStr = bufreader.readLine()) != null) {
                boolean isBom = false;
                if (isFirst) {
                    isFirst = false;
                    if (lineStr.length() >= 1 && true) {
                        int charCode = lineStr.charAt(0);
                        if (charCode == 65279) {
                            isBom = true;
                        }
                    }
                }
                String tmpLineStr = isBom ? lineStr.substring(1) : lineStr;
                if (isPutEmptyLine) {
                    fileContent.add(tmpLineStr);
                } else {
                    if (tmpLineStr.length() > 0) {
                        fileContent.add(tmpLineStr);
                    }
                }

            }
            realContent = fileContent;

        } catch (Exception e) {
        } finally {
            if (null != bufreader) {
                try {
                    bufreader.close();
                } catch (Exception e) {
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }

        }
        return realContent;
    }

    static {
        initLog();
    }

    @SuppressWarnings("unchecked")
    private static void tryImplementation(String testClassName, String implClassName) {
        if (logConstructor != null) {
            return;
        }

        try {
            Resources.classForName(testClassName);
            Class implClass = Resources.classForName(implClassName);
            logConstructor = implClass.getConstructor(new Class[]{String.class});

            Class<?> declareClass = logConstructor.getDeclaringClass();
            if (!Log.class.isAssignableFrom(declareClass)) {
                logConstructor = null;
            }

            try {
                if (null != logConstructor) {
                    logConstructor.newInstance(LogFactory.class.getName());
                }
            } catch (Throwable t) {
                logConstructor = null;
            }

        } catch (Throwable t) {
            // skip
        }
    }

    public static Log getLog(Class clazz) {
        return getLog(clazz.getName());
    }

    public static Log getLog(String loggerName) {
        try {
            Log log = (Log) logConstructor.newInstance(loggerName);
            return log;
        } catch (Throwable t) {
            throw new RuntimeException("Error creating logger for logger '" + loggerName + "'.  Cause: " + t, t);
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized void selectLog4JLogging() {
        try {
            String logPackageName = null;
            try {
                logPackageName = LogFactory.class.getPackage().getName() + ".";
            } catch (Exception e) {
                // TODO: handle exception
            }
            Resources.classForName("org.apache.log4j.Logger");
            Class implClass = Resources.classForName(logPackageName + "Log4jImpl");
            logConstructor = implClass.getConstructor(new Class[]{String.class});
        } catch (Throwable t) {
            // ignore
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized void selectJavaLogging() {
        try {
            String logPackageName = null;
            try {
                logPackageName = LogFactory.class.getPackage().getName() + ".";
            } catch (Exception e) {
                // TODO: handle exception
            }
            Resources.classForName("java.util.logging.Logger");
            Class implClass = Resources.classForName(logPackageName + "Jdk14LoggingImpl");
            logConstructor = implClass.getConstructor(new Class[]{String.class});
        } catch (Throwable t) {
            // ignore
        }
    }
}
