package com.ruomm.javax.basex.network;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/6/15 16:11
 */
public class NetworkUtils {
    private final static Log log = LogFactory.getLog(NetworkUtils.class);

    /**
     * 获取IPv4地址
     *
     * @return IPv4地址，没有找到返回空
     */
    public static InetAddress getIPV4Address() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress()
                            && !intf.getName().equals("dummy0")
                            && addr instanceof Inet4Address) {
                        log.debug("获取IPv4地址成功，地址为：" + addr.getHostAddress());
                        return addr;
                    }
                }
            }
            log.error("获取IPv4地址失败，没有找到有效的IPv4地址");
            return null;
        } catch (SocketException e) {
            log.error("获取IPv4地址时候发生异常", e);
            return null;
        }
    }

    /**
     * 获取IPv4地址列表
     *
     * @return IPv4地址列表，没有找到返回空
     */
    public static List<IpInetAddress> getIpV4List() {
        return getIpV4List(true);
    }

    /**
     * 获取IPv4地址列表
     *
     * @param removeVirtualNet 是否移除虚拟网卡
     * @return IPv4地址列表，没有找到返回空
     */
    public static List<IpInetAddress> getIpV4List(boolean removeVirtualNet) {
        List<IpInetAddress> listIpInetAddress = new ArrayList<>();
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                for (InterfaceAddress interfaceAddress : intf.getInterfaceAddresses()) {
                    InetAddress addr = interfaceAddress.getAddress();
                    if (!addr.isLoopbackAddress()
                            && !intf.getName().equals("dummy0")
                            && addr instanceof Inet4Address) {
                        IpInetAddress ipInetAddress = new IpInetAddress();
                        ipInetAddress.setName(intf.getName());
                        ipInetAddress.setDisplayName(intf.getDisplayName());
                        ipInetAddress.setAddress(addr);
                        ipInetAddress.setBroadcast(interfaceAddress.getBroadcast());
                        ipInetAddress.setNetworkPrefixLength(interfaceAddress.getNetworkPrefixLength());
                        ipInetAddress.setWifi(isWifiInetAddress(ipInetAddress.getName(), ipInetAddress.getDisplayName()));
                        if (isVirtualNet(ipInetAddress.getDisplayName())) {
                            continue;
                        }
                        listIpInetAddress.add(ipInetAddress);
                    }
                }
            }
        } catch (SocketException e) {
            log.error("IPv4地址列表时候发生异常", e);
        }
        if (listIpInetAddress.isEmpty()) {
            log.error("IPv4地址列表失败，没有找到有效的IPv4地址");
        } else {
            log.debug("获取IPv4地址列表成功，地址列表为：" + listIpInetAddress);
        }
        return listIpInetAddress;
    }

    /**
     * 获取wifi连接的IPv4地址
     *
     * @param emptyToLan 没有找到时候是否获取非wifi连接的IPv4地址
     * @return IPv4地址
     */
    public static IpInetAddress getIpV4ByWifi(boolean emptyToLan) {
        List<IpInetAddress> ipV4List = getIpV4List();
        if (ipV4List.isEmpty()) {
            return null;
        }
        IpInetAddress ipInfoResult = null;
        for (IpInetAddress ipInfo : ipV4List) {
            if (ipInfo.isWifi()) {
                ipInfoResult = ipInfo;
                break;
            }
        }
        if (null == ipInfoResult && emptyToLan) {
            ipInfoResult = ipV4List.get(0);
        }
        return ipInfoResult;
    }

    /**
     * 获取wifi连接的IPv4地址
     *
     * @param emptyToLan 没有找到时候是否获取非wifi连接的IPv4地址
     * @return IPv4地址
     */
    public static IpInetAddress getIpV4ByWifi(boolean emptyToLan, String headerName) {
        List<IpInetAddress> ipV4List = getIpV4List();
        if (ipV4List.isEmpty()) {
            return null;
        }
        IpInetAddress ipInfoResult = null;
        for (IpInetAddress ipInfo : ipV4List) {

            if (StringUtils.isEmpty(headerName)) {
                if (ipInfo.isWifi()) {
                    ipInfoResult = ipInfo;
                    break;
                }
            } else {
                if (ipInfo.isWifi() && ipInfo.getAddress().getHostAddress().startsWith(headerName)) {
                    ipInfoResult = ipInfo;
                    break;
                }
            }
        }
        if (null == ipInfoResult && emptyToLan) {
            ipInfoResult = ipV4List.get(0);
        }
        return ipInfoResult;
    }

    /**
     * 获取lan连接的IPv4地址
     *
     * @param emptyToWifi 没有找到时候是否获取wifi连接的IPv4地址
     * @return IPv4地址
     */
    public static IpInetAddress getIpV4ByLan(boolean emptyToWifi) {
        List<IpInetAddress> ipV4List = getIpV4List();
        if (ipV4List.isEmpty()) {
            return null;
        }
        IpInetAddress ipInfoResult = null;
        for (IpInetAddress ipInfo : ipV4List) {
            if (!ipInfo.isWifi()) {
                ipInfoResult = ipInfo;
                break;
            }
        }
        if (null == ipInfoResult && emptyToWifi) {
            ipInfoResult = ipV4List.get(0);
        }
        return ipInfoResult;
    }

    /**
     * 获取lan连接的IPv4地址
     *
     * @param emptyToWifi 没有找到时候是否获取wifi连接的IPv4地址
     * @return IPv4地址
     */
    public static IpInetAddress getIpV4ByLan(boolean emptyToWifi, String headerName) {
        List<IpInetAddress> ipV4List = getIpV4List();
        if (ipV4List.isEmpty()) {
            return null;
        }
        IpInetAddress ipInfoResult = null;
        for (IpInetAddress ipInfo : ipV4List) {
            if (StringUtils.isEmpty(headerName)) {
                if (!ipInfo.isWifi()) {
                    ipInfoResult = ipInfo;
                    break;
                }
            } else {
                if (!ipInfo.isWifi() && ipInfo.getAddress().getHostAddress().startsWith(headerName)) {
                    ipInfoResult = ipInfo;
                    break;
                }
            }
        }
        if (null == ipInfoResult && emptyToWifi) {
            ipInfoResult = ipV4List.get(0);
        }
        return ipInfoResult;
    }

    /**
     * 判断网卡是否wifi
     *
     * @param name        网卡名称
     * @param displayName 网卡显示名称
     * @return 网卡是否wifi
     */
    private static boolean isWifiInetAddress(String name, String displayName) {
        if (!StringUtils.isBlank(name)) {
            String lowerString = name.toLowerCase();
            if (lowerString.contains("wlan") || lowerString.contains("wifi") || lowerString.contains("wi-fi")) {
                return true;
            }
        }
        if (!StringUtils.isBlank(displayName)) {
            String lowerString = displayName.toLowerCase();
            if (lowerString.contains("wlan") || lowerString.contains("wifi") || lowerString.contains("wi-fi") || lowerString.contains("mhz")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断网卡是否虚拟网卡
     *
     * @param displayName 网卡显示名称
     * @return 网卡是否wifi
     */
    private static boolean isVirtualNet(String displayName) {
        if (StringUtils.isBlank(displayName)) {
            return false;
        }
        String lowerString = displayName.toLowerCase();
        if (lowerString.contains("virtual") || lowerString.contains("vmware") || lowerString.contains("虚拟")) {
            return true;
        }
        return false;
    }
}
