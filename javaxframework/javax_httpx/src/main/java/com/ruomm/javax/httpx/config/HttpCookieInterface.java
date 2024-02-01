/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月3日 上午10:11:07
 */
package com.ruomm.javax.httpx.config;

public interface HttpCookieInterface {
    // 设置缓存tag
    public HttpCookieInterface setCookieTag(String cookieTag);

    public String getCookieTag();

    public void writeCookie(String domain, String tag, String cookieData);

    public String readCookie(String domain, String tag);
}