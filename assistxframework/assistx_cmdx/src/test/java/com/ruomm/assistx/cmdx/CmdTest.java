package com.ruomm.assistx.cmdx;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2022/1/21 16:07
 */
public class CmdTest {
    public static void main(String[] args) {
        CmdxHelper cmdxHelper = new CmdxHelper();
        String result = cmdxHelper.doCmd("D:\\GreenSoft\\Java\\jdk\\bin\\java.exe -jar \"D:\\GreenSoft\\VpnRouteUtil\\VpnRouteUtil.jar\" 172.16.2. 10.10.0.0:255.255.0.0 10.66.0.0:255.255.0.0 124.251.17.36:255.255.255.255");
        System.out.println(result);
    }
}
