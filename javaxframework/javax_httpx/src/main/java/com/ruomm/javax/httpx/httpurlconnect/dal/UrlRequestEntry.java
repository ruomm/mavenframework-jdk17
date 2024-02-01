/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月12日 下午2:14:41
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import java.net.HttpURLConnection;

public abstract class UrlRequestEntry {
    private String mimeType = null;
    private String charset = null;
    private boolean isCancle = false;
//	private String userAgent = null;
//	private boolean keepAlive = false;
//	private String acceptType = null;
//	private boolean callCancel = false;

    public String getMimeType() {
        return mimeType;
    }

    public boolean isCancle() {
        return isCancle;
    }

    public void setCancle(boolean isCancle) {
        this.isCancle = isCancle;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

//	public String getUserAgent() {
//		return userAgent;
//	}
//
//	public void setUserAgent(String userAgent) {
//		this.userAgent = userAgent;
//	}
//
//	public boolean isKeepAlive() {
//		return keepAlive;
//	}
//
//	public void setKeepAlive(boolean keepAlive) {
//		this.keepAlive = keepAlive;
//	}
//
//	public String getAcceptType() {
//		return acceptType;
//	}

//	public void setAcceptType(String acceptType) {
//		this.acceptType = acceptType;
//	}
//
//	public boolean isCallCancel() {
//		return callCancel;
//	}
//
//	public void setCallCancel(boolean callCancel) {
//		this.callCancel = callCancel;
//	}

    public abstract boolean doOutputStream(HttpURLConnection conn);

    public abstract boolean setHttpContentType(HttpURLConnection conn);

    @Override
    public String toString() {
        return "UrlRequestEntry [mimeType=" + mimeType + ", charset=" + charset + "]";
    }

}