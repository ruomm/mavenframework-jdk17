/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月3日 上午11:30:15
 */
package com.ruomm.javax.httpx.config;

import com.ruomm.javax.httpx.dal.HttpCacheReader;

import java.util.Map;

public interface HttpCacheInterface {
    // 设置缓存tag
    public HttpCacheInterface setCacheTag(String cacheTag);

    // 设置缓存时间
    public HttpCacheInterface setCacheValidTime(long cacheValidTime);

    // 获取缓存tag
    public String getCacheTag();

    // 获取缓存时间
    public long getCacheValidTime();

    // 读取缓存
    public HttpCacheReader readCache(String domain, String cacheTag, long cacheTime, Class<?> cls);

    // 写入缓存
    public boolean writeCache(String domain, String cacheTag, byte[] responseData, String responseStr);

    // 缓存读取后的回调,返回false会继续执行Http请求
    public void cacheCallBack(HttpCacheReader httpCacheReader, Object resultObj, byte[] resultData,
                              Map<String, String> resultHeaders, int status);
}