/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年1月26日 下午3:25:02
 */
package com.ruomm.assistx.curltoolsx.core;

public class ResponseText {
    /**
     * 请求结果的自动解析好的对象
     */
    public Object resultObject;
    /**
     * 请求结果的原始数据
     */
    public String resultString;
    /**
     *
     */
    public int status;

    @Override
    public String toString() {
        return "ResponseText [resultObject=" + resultObject + ", resultString=" + resultString + ", status=" + status
                + "]";
    }

}
