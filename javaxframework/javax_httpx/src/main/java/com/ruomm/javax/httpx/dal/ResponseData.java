/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2016年4月13日 上午9:39:58
 */
package com.ruomm.javax.httpx.dal;

import com.ruomm.javax.httpx.parse.HttpParseUtil;

import java.util.Arrays;
import java.util.Map;

public class ResponseData {

    /**
     * 响应状态
     */
    private int status;
    /**
     * 响应结果自动解析好的对象
     */
    private Object resultObject;
    /**
     * 响应结果原始数据
     */
    private byte[] resultData;
    /**
     * 响应的Headers
     */
    private Map<String, String> headers;
    /**
     * 缓存响应内容
     */
    private ResponseDataCache cacheResult;

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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public ResponseDataCache getCacheResult() {
        return cacheResult;
    }

    public void setCacheResult(ResponseDataCache cacheResult) {
        this.cacheResult = cacheResult;
    }

    @Override
    public String toString() {
        return "ResponseData [status=" + status + ", resultObject=" + resultObject + ", resultData="
                + Arrays.toString(resultData) + ", headers=" + headers + ", cacheResult=" + cacheResult + "]";
    }

    public String getPrintString(String charsetName) {
        return "ResponseData [status=" + status + ", resultObject=" + resultObject + ", resultData="
                + HttpParseUtil.byteToString(resultData, charsetName) + ", headers=" + headers + ", cacheResult="
                + (null == cacheResult ? "null" : cacheResult.getPrintString(charsetName)) + "]";
    }

}