/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月4日 下午1:27:49
 */
package com.ruomm.javax.httpx.config;

import java.util.Map;

public interface TextHttpListener {
    /**
     * 请求回调
     *
     * @param resultObj 请求结果的自动解析好的对象
     * @param resultStr  请求结果的原始数据
     * @param status       请求结果的状态
     */
    public void httpCallBack(Object resultObj, String resultStr, Map<String, String> resultHeaders, int status);

    /**
     * 请求回调过滤器，
     *
     * @param resultStr 请求结果的原始数据
     * @param status      请求结果的状态
     * @return 返回true表示过滤器成功，不会执行httpCallBack，否则执行httpCallBack回调
     */
    public boolean httpCallBackFilter(String resultStr, Map<String, String> resultHeaders, int status);

    /**
     * 请求回掉过滤成功后是否继续执行httpCallBack
     *
     * @return
     */
    public boolean httpCallBackAfterFilterTrue();
}