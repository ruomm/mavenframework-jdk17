package com.ruomm.javax.corex;

public class WebUtilsTest {
    public static void main(String[] args) {
        System.out.println(WebUtils.getRealUriToArray(WebUtils.getRealUriByUrl("https://xueshu.baidu.com:443/nihaodfa\\\\dajiadhaosd/?nihao=a"), 2)[0]);
    }
}
