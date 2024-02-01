package com.ruomm.javax.httpx.platform;

import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.config.DataHttpListener;
import com.ruomm.javax.httpx.config.FileHttpListener;
import com.ruomm.javax.httpx.config.TextHttpListener;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Map;

public abstract class LooperHelper {
    @SuppressWarnings("rawtypes")
    public static LooperHelper getInstance(boolean isForceMainLooper) {
        String clsName = HttpConfig.IS_ADNROID ? "com.ruomm.javax.httpx.platform.LooperHelperAndroid"
                : "com.ruomm.javax.httpx.platform.LooperHelperJava";
        try {
            Class<?> clazz = Class.forName(clsName);
            Constructor c = clazz.getConstructor(boolean.class); // 获取有参构造
            // c.setAccessible(true);
            LooperHelper looperHelperAndroid = (LooperHelper) c.newInstance(true); // 通过有参构造创建对象
            return looperHelperAndroid;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error:LooperHelper.getInstance->" + " create fail, LooperHelper must not Null!");
        }
    }

    public abstract void initLooper();

    public abstract void quitLooper();

    public abstract void doHttpCallBack(final DataHttpListener httpListener, final Object resultObject,
                                        final byte[] resultData, final Map<String, String> resultHeaders, final int status);

    public abstract void doHttpCallBack(final TextHttpListener httpListener, final Object resultObj,
                                        final String resultStr, final Map<String, String> resultHeaders, final int status);

    public abstract void doHttpCallBackFile(final FileHttpListener httpListener, final File file,
                                            final Map<String, String> resultHeaders, final int status);

    public abstract void doHttpCallBackProgress(final FileHttpListener httpListener, final long bytesWritten,
                                                final long totalSize, final double valueProgress);
}