/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月4日 下午8:51:21
 */
package com.ruomm.javax.httpx.dal;

import com.ruomm.javax.httpx.parse.HttpParseUtil;

import java.util.Arrays;

public class ResponseDataCache {
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
    private byte[] resultData;

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

    public byte[] getResultData() {
        return resultData;
    }

    public void setResultData(byte[] resultData) {
        this.resultData = resultData;
    }

    @Override
    public String toString() {
        return "ResponseDataCache [status=" + status + ", resultObject=" + resultObject + ", resultData="
                + Arrays.toString(resultData) + "]";
    }

    public String getPrintString(String charsetName) {
        return "ResponseDataCache [status=" + status + ", resultObject=" + resultObject + ", resultData="
                + HttpParseUtil.byteToString(resultData, charsetName) + "]";
    }

}