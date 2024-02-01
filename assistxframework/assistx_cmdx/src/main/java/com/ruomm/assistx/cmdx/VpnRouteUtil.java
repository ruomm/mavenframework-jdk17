package com.ruomm.assistx.cmdx;

import com.ruomm.assistx.cmdx.dal.RouteIp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/6/4 10:20
 */
public class VpnRouteUtil {
    public static void main(String[] args) {
//        String[] mArgs=null==args||args.length<=0?new String[]{"172.16.2.","10.10.0.0:255.255.0.0.0","10.66.0.0:255.255.0.0.0"}:args;
        String[] mArgs = args;
        String argInput = null != mArgs && mArgs.length > 0 ? mArgs[0] : null;
        if (null != argInput && ("-h".equalsIgnoreCase(argInput) || "/h".equalsIgnoreCase(argInput) || "-help".equalsIgnoreCase(argInput) || "/help".equalsIgnoreCase(argInput))) {
            System.out.println("VPN自定义路由设置，请先开启VPN路由的 禁用基于类的路由 添加功能。");
            System.out.println("参数1为空或不是有效IP开头的时候，默认IP开头172.16.2.的路由是自定义VPN的路由。");
            System.out.println("其他参数为路由的子网IP和掩码，以:符号拼接。默认为10.0.0.0:255.0.0.0");
            System.out.println("找到自定义VPN，添加指定网段路由到自定义VPN链接上。");
            System.out.println("没有找到自定义VPN，删除指定网段路由链接。");
            return;
        }
        // 判断第一个参数是否合法
        boolean isPass = true;
        if (null != argInput && argInput.length() > 0 && argInput.contains(".")) {
            try {
                String[] tmpList = argInput.split("\\.");
                if (null == tmpList || tmpList.length < 1 || tmpList.length > 4) {
                    System.out.println("参数不合法，不是有效的IP开头");
                    isPass = false;
                } else {
                    for (String tmp : tmpList) {
                        int tmpInt = Integer.parseInt(tmp);
                        if (tmpInt < 0 || tmpInt > 255) {
                            isPass = false;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                isPass = false;
                e.printStackTrace();
                System.out.println("参数不合法，不是有效的IP开头");
            }
        } else {
            isPass = false;
        }
        String vpnIPHeader = null;
        if (isPass) {
            vpnIPHeader = argInput;
        } else {
            vpnIPHeader = "172.16.2.";
        }
        List<RouteIp> listRouteIps = new ArrayList<>();
        if (null != mArgs && mArgs.length > 0) {
            for (int i = 0; i < mArgs.length; i++) {
                try {
                    String[] tmpIpAndmsk = mArgs[i].split(":");
                    if (null == tmpIpAndmsk || tmpIpAndmsk.length != 2) {
                        continue;
                    }
                    String tmpIp = IPUtils.parseIpV4(tmpIpAndmsk[0]);
                    String tmpMask = IPUtils.parseIpV4Mask(tmpIpAndmsk[1]);
                    if (null == tmpIp || tmpIp.length() <= 0 || null == tmpMask || tmpMask.length() <= 0) {
                        continue;
                    }
                    if (tmpIp.startsWith("0.") || tmpIp.startsWith("127.") || tmpIp.startsWith("255.") || tmpIp.startsWith("224.")) {
                        continue;
                    }
                    int ipFirst = Integer.valueOf(tmpIp.split("\\.")[0]);
                    if (ipFirst == 0 || ipFirst == 127 || ipFirst == 255 || (ipFirst >= 224 && ipFirst <= 239)) {
                        continue;
                    }
                    RouteIp routeIp = new RouteIp();
                    routeIp.setIp(tmpIp);
                    routeIp.setMask(tmpMask);
                    listRouteIps.add(routeIp);

                } catch (Exception e) {

                }
            }
        }
        if (listRouteIps.size() <= 0) {
            RouteIp routeIp = new RouteIp();
            routeIp.setIp("10.0.0.0");
            routeIp.setMask("255.0.0.0");
            listRouteIps.add(routeIp);
        }
        System.out.println("VPN链接IP开头为：" + vpnIPHeader);
        System.out.println("VPN链接IP路由列表为：" + listRouteIps);
        CmdxHelper cmdxHelper = new CmdxHelper();
        List<String> pList = cmdxHelper.doCmdListBack("route print");
        System.out.println(pList);
        if (null == pList || pList.size() <= 0) {
            System.out.println("route 命令失败");
            return;
        }
        System.out.println("----------------");
        System.out.println("命令" + "route print" + " 执行结果：");
        for (String tmp : pList) {
            System.out.println(tmp);
        }
        System.out.println("----------------");
        // 判断自定义路由是否存在
        List<RouteIp> routeIpDel = new ArrayList<>();
        String ipVpn = null;
        for (String tmp : pList) {
            if (null != tmp && tmp.startsWith(vpnIPHeader) && tmp.contains("255.255.255.255")) {
                try {
                    String tmpIpVpn = tmp.split(" ")[0];
                    if (IPUtils.isIpV4(tmpIpVpn)) {
                        ipVpn = tmpIpVpn;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        for (RouteIp routeIp : listRouteIps) {
            boolean isDel = false;
            for (String tmp : pList) {
                if (null == tmp || tmp.length() <= 0) {
                    continue;
                }
                if (tmp.startsWith(routeIp.getIp()) && tmp.contains(routeIp.getMask()) && tmp.contains(vpnIPHeader)) {
                    isDel = true;
                    break;
                }
            }
            String cmpDelStr = "route delete " + routeIp.getIp();
            if (!isDel) {
                System.out.println("----------------");
                System.out.println("命令 " + cmpDelStr + " 无需执行：");
                System.out.println("----------------");
            } else {
                String routeDelResult = cmdxHelper.doCmd(cmpDelStr);
                System.out.println("----------------");
                System.out.println("命令 " + cmpDelStr + " 执行结果：");
                System.out.println(routeDelResult);
                System.out.println("----------------");
            }
        }
        if (null == ipVpn || ipVpn.length() <= 0) {
            // 删除路由
            System.out.println("没有找到自定义VPN相关链接，不设置路由");
            return;
        }
        for (RouteIp routeIp : listRouteIps) {
            System.out.println("找到自定义VPN相关链接，链接IP为：" + ipVpn + "，开始设置路由");
            String routeAddStr = "route add " + routeIp.getIp() + " mask " + routeIp.getMask() + " " + ipVpn;
            String routeAddResult = cmdxHelper.doCmd(routeAddStr);
            System.out.println("----------------");
            System.out.println("命令" + routeAddStr + " 执行结果：");
            System.out.println(routeAddResult);
            System.out.println("----------------");
        }
    }
}
