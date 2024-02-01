package com.ruomm.javax.basex.network;

import java.net.InetAddress;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/6/15 16:34
 */
public class IpInetAddress {
    /**
     * 网卡名称
     */
    private String name;
    /**
     * 网卡显示名称
     */
    private String displayName;
    /**
     * ipV4地址
     */
    private InetAddress address;
    /**
     * ipV4广播地址
     */
    private InetAddress broadcast;
    /**
     * ipV4子网掩码
     */
    private short networkPrefixLength;

    /**
     * 是否wifi
     *
     * @return
     */
    private boolean wifi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public InetAddress getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(InetAddress broadcast) {
        this.broadcast = broadcast;
    }

    public short getNetworkPrefixLength() {
        return networkPrefixLength;
    }

    public void setNetworkPrefixLength(short networkPrefixLength) {
        this.networkPrefixLength = networkPrefixLength;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isWifi() {
        return wifi;
    }

    @Override
    public String toString() {
        return "IpInetAddress{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", address=" + address +
                ", broadcast=" + broadcast +
                ", networkPrefixLength=" + networkPrefixLength +
                ", wifi=" + wifi +
                '}';
    }
}
