/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月3日 上午9:18:44
 */
package com.ruomm.javax.httpx.dal;

import java.util.Arrays;

public class HttpCacheReader {
    public boolean isCacheOk;
    public byte[] cacheData;
    public String cacheStr;
    public Object cacheObj;
    public long cacheTime;
    public long cacheValidTime;
    public String cacheTag;
    public boolean isDoHttp;
    public boolean isCacheExecuteToHttpCallBack;

    public boolean isCacheOk() {
        return isCacheOk;
    }

    public void setCacheOk(boolean isCacheOk) {
        this.isCacheOk = isCacheOk;
    }

    public byte[] getCacheData() {
        return cacheData;
    }

    public void setCacheData(byte[] cacheData) {
        this.cacheData = cacheData;
    }

    public String getCacheStr() {
        return cacheStr;
    }

    public void setCacheStr(String cacheStr) {
        this.cacheStr = cacheStr;
    }

    public Object getCacheObj() {
        return cacheObj;
    }

    public void setCacheObj(Object cacheObj) {
        this.cacheObj = cacheObj;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public long getCacheValidTime() {
        return cacheValidTime;
    }

    public void setCacheValidTime(long cacheValidTime) {
        this.cacheValidTime = cacheValidTime;
    }

    public String getCacheTag() {
        return cacheTag;
    }

    public void setCacheTag(String cacheTag) {
        this.cacheTag = cacheTag;
    }

    public boolean isDoHttp() {
        return isDoHttp;
    }

    public void setDoHttp(boolean isDoHttp) {
        this.isDoHttp = isDoHttp;
    }

    public boolean isCacheExecuteToHttpCallBack() {
        return isCacheExecuteToHttpCallBack;
    }

    public void setCacheExecuteToHttpCallBack(boolean isCacheExecuteToHttpCallBack) {
        this.isCacheExecuteToHttpCallBack = isCacheExecuteToHttpCallBack;
    }

    @Override
    public String toString() {
        return "HttpCacheReader [isCacheOk=" + isCacheOk + ", cacheData=" + Arrays.toString(cacheData) + ", cacheStr="
                + cacheStr + ", cacheObj=" + cacheObj + ", cacheTime=" + cacheTime + ", cacheValidTime="
                + cacheValidTime + ", cacheTag=" + cacheTag + ", isDoHttp=" + isDoHttp
                + ", isCacheExecuteToHttpCallBack=" + isCacheExecuteToHttpCallBack + "]";
    }

}