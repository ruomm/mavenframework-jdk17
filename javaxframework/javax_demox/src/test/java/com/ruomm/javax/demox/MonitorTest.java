package com.ruomm.javax.demox;

import com.ruomm.javax.httpx.dal.ResponseText;
import com.ruomm.javax.httpx.httpurlconnect.TextUrlConnect;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/9/1 13:49
 */
public class MonitorTest {
    public static void main(String[] args) {

        ResponseText responseText = new TextUrlConnect().setUrl("http://192.168.100.104/bdss/monitor.jsp").setPost(false).doHttpSync(String.class);
        System.out.println(responseText);
    }
}
