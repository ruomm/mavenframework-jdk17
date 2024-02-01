/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月8日 上午9:51:08
 */
package com.ruomm.javax.httpx.dal;

public class HttpxMime {
    private String mimeType;// MimeType
    private String charset;
    private String mimeTypeWithCharset;// MimeTypeWithCharset

    public String getMimeType() {
        return mimeType;
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

    public String getMimeTypeWithCharset() {
        return mimeTypeWithCharset;
    }

    public void setMimeTypeWithCharset(String mimeTypeWithCharset) {
        this.mimeTypeWithCharset = mimeTypeWithCharset;
    }

    @Override
    public String toString() {
        return "RequestContent [mimeType=" + mimeType + ", charset=" + charset + ", mimeTypeWithCharset="
                + mimeTypeWithCharset + "]";
    }

}