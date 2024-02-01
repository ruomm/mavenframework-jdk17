/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月17日 下午1:04:00
 */
package com.ruomm.javax.httpx.cookie;

import com.ruomm.javax.httpx.config.HttpCookieInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpCommonCookie implements HttpCookieInterface {
    public static Map<String, String> cookieMap = new ConcurrentHashMap<String, String>();
    private String tag;

    @Override
    public HttpCookieInterface setCookieTag(String cookieTag) {
        // TODO Auto-generated method stub
        this.tag = cookieTag;
        return this;
    }

    @Override
    public String getCookieTag() {
        // TODO Auto-generated method stub
        return this.tag;
    }

    @Override
    public void writeCookie(String domain, String tag, String cookieData) {
        // TODO Auto-generated method stub
        if (null == cookieData || cookieData.length() <= 0) {
            return;
        }
        String cookieKey = getCookieKey(domain, tag);
        if (null == cookieKey || cookieKey.length() <= 0) {
            return;
        }
        if (null == cookieMap) {
            cookieMap = new ConcurrentHashMap<String, String>();
        }
        cookieMap.put(cookieKey, cookieData);
    }

    @Override
    public String readCookie(String domain, String tag) {
        // TODO Auto-generated method stub
        if (null == cookieMap) {
            return null;
        }
        String cookieKey = getCookieKey(domain, tag);
        if (null == cookieKey || cookieKey.length() <= 0) {
            return null;
        }
        return cookieMap.get(cookieKey);
    }

    private String getCookieKey(String domain, String tag) {
        if ((null == domain || domain.length() <= 0) && (null == tag || tag.length() <= 0)) {
            return null;
        } else if (null == domain || domain.length() <= 0) {
            return tag;
        } else if (null == tag || tag.length() <= 0) {
            return domain;
        } else {
            return domain + "_" + tag;
        }
    }

}