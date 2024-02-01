/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月4日 下午8:51:21
 */
package com.ruomm.javax.httpx.dal;

public class ResponseTextCache {
    /**
     * 缓存响应状态
     */
    private int status;
    /**
     * 缓存响应结果自动解析好的对象
     */
    private Object resultObject;
    /**
     * 缓存响应结果原始数据
     */
    private String resultData;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "ResponseTextCache [status=" + status + ", resultObject=" + resultObject + ", resultData=" + resultData
                + "]";
    }

    public String getPrintString() {
        return "ResponseTextCache [status=" + status + ", resultObject=" + resultObject + ", resultData=" + resultData
                + "]";
    }

}