package com.ruomm.assistx.cmdx.dal;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/6/4 11:42
 */
public class RouteIp {
    private String ip;
    private String mask;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public String toString() {
        return "RouteIp{" +
                "ip='" + ip + '\'' +
                ", mask='" + mask + '\'' +
                '}';
    }
}
