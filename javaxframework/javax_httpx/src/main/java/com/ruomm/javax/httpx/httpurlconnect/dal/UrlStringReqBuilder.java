/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月16日 下午4:11:45
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.dal.HttpxMime;

public class UrlStringReqBuilder {
    public static UrlStringReqEntry create(String text) {
        return create(text, null, null);
    }

    public static UrlStringReqEntry create(String text, String mimeType) {
        return create(text, null, mimeType);
    }

    public static UrlStringReqEntry create(String text, String mimeType, String charset) {
        HttpxMime httpxMime = HttpConfig.parseRequestMimeNew(text, mimeType, charset);
        if (null == httpxMime) {
            return null;
        }
        UrlStringReqEntry requestEntry = new UrlStringReqEntry();
        requestEntry.setMimeType(httpxMime.getMimeType());
        requestEntry.setCharset(httpxMime.getCharset());
        requestEntry.setText(text);
        return requestEntry;
    }

}