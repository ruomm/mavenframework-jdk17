package com.ruomm.javax.httpx.platform;

import com.ruomm.javax.httpx.config.DataHttpListener;
import com.ruomm.javax.httpx.config.FileHttpListener;
import com.ruomm.javax.httpx.config.TextHttpListener;

import java.io.File;
import java.util.Map;

class LooperHelperJava extends LooperHelper {
    //	private boolean isForceMainLooper = false;
    private boolean isQuit = false;

    public LooperHelperJava(boolean isForceMainLooper) {
//		this.isForceMainLooper = isForceMainLooper;
    }

    @Override
    public void initLooper() {

    }

    @Override
    public void quitLooper() {
        this.isQuit = true;
    }

    @Override
    public void doHttpCallBack(final DataHttpListener httpListener, final Object resultObject, final byte[] resultData,
                               final Map<String, String> resultHeaders, final int status) {
        if (this.isQuit) {
            return;
        }
        httpListener.httpCallBack(resultObject, resultData, resultHeaders, status);
    }

    @Override
    public void doHttpCallBack(final TextHttpListener httpListener, final Object resultObj, final String resultStr,
                               final Map<String, String> resultHeaders, final int status) {
        if (this.isQuit) {
            return;
        }
        httpListener.httpCallBack(resultObj, resultStr, resultHeaders, status);
    }

    @Override
    public void doHttpCallBackFile(final FileHttpListener httpListener, final File file,
                                   final Map<String, String> resultHeaders, final int status) {
        if (this.isQuit) {
            return;
        }
        httpListener.httpCallBackFile(file, resultHeaders, status);
    }

    @Override
    public void doHttpCallBackProgress(final FileHttpListener httpListener, final long bytesWritten,
                                       final long totalSize, final double valueProgress) {
        if (this.isQuit) {
            return;
        }
        httpListener.httpCallBackProgress(bytesWritten, totalSize, valueProgress);
    }
}