/**
 * @copyright wanruome-2017
 * @author 牛牛-wanruome@163.com
 * @create 2017年3月30日 上午9:26:35
 */
package com.ruomm.javax.basex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtils {
    private final static Log log = LogFactory.getLog(IPUtils.class);

    /**
     * 依据HttpServletRequest请求获取IP地址
     * @param request HttpServletRequest请求
     * @return ip地址
     */
    public static String getRequestIP(HttpServletRequest request) {
        String ip = null;

        try {
            ip = request.getHeader("x-forwarded-for");
            String localIP = "127.0.0.1";
            if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP))
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP))
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP))
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ip = null;
            log.error("Error:getRequestIP", e);
        }

        return ip;
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param s      源字符串
     * @param findText 要查找的字符串
     * @return
     */
    private static int counterAppearNum(String s, String findText) {
        if (null == s) {
            return 0;
        }
        int count = 0;
        try {
            Pattern p = Pattern.compile(findText);
            Matcher m = p.matcher(s);
            while (m.find()) {
                count++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:counterAppearNum", e);
        }
        return count;
    }

    /**
     * 获取指定字符出现的次数
     *
     * @param str 源字符串
     * @param c   要查找的字符
     * @return
     */
    private static int counterAppearNum(String str, char c) {
        if (null == str || str.length() <= 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * 判断是否ipV4地址
     * @param ipStr ip地址
     * @return 是否ipV4地址
     */
    public static boolean isIpV4(String ipStr) {
        if (null == ipStr || ipStr.length() <= 0) {
            return false;
        }
        int count = counterAppearNum(ipStr, '.');
        if (count != 3) {
            return false;
        }
        String[] ipStrArrays = ipStr.split("\\.");
        if (null == ipStrArrays || ipStrArrays.length != 4) {
            return false;
        }
        for (String data : ipStrArrays) {
            if (null == data || data.length() <= 0 || data.length() > 3) {
                return false;
            }
            int val = 0;
            try {
                val = NumberStringUtil.stringToInt(data, "0123456789");
            } catch (Exception e) {
                // TODO: handle exception
                val = -1;
                log.error("Error:isIpV4", e);
            }
            if (val < 0 || val > 255) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否ipV6地址
     * @param ipStr ip地址
     * @param isParseFullIpV6 是否解析缩写地址为完全地址
     * @return 是否ipV6地址
     */
    public static boolean isIpV6(String ipStr, boolean isParseFullIpV6) {
        String ipV6Str = null;
        try {
            if (isParseFullIpV6) {
                ipV6Str = parseToFullIpV6(ipStr);
            } else {
                ipV6Str = ipStr;
            }
        } catch (Exception e) {
            // TODO: handle exception
            ipV6Str = ipStr;
            log.error("Error:isIpV6", e);
        }
        if (null == ipV6Str || ipV6Str.length() <= 0) {
            return false;
        }
//		String ipV6Str = null;
//		try {
//			parseToFullIpV6(ipStr);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
        int count = counterAppearNum(ipV6Str, ':');
        if (count != 7) {
            return false;
        }
        String[] ipStrArrays = ipV6Str.split(":");
        if (null == ipStrArrays || ipStrArrays.length != 8) {
            return false;
        }
        for (String data : ipStrArrays) {
            if (null == data || data.length() <= 0 || data.length() > 4) {
                return false;
            }
            int val = 0;
            try {
                val = NumberStringUtil.stringToInt(data.toLowerCase(), "0123456789abcdef");
            } catch (Exception e) {
                // TODO: handle exception
                val = -1;
                log.error("Error:isIpV6", e);
            }
            if (val < 0 || val > 256 * 256 - 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * ipV6地址转换为ipV4地址
     * @param ipV6Str ipV6地址
     * @return ipV4地址
     */
    public static String ip6ToIp4(String ipV6Str) {
        if (null == ipV6Str || ipV6Str.length() <= 0) {
            return ipV6Str;
        }
        String[] ipStrArrays = ipV6Str.split(":");
        if (null == ipStrArrays || ipStrArrays.length != 2) {
            return ipV6Str;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (String data : ipStrArrays) {
            if (null == data || data.length() <= 0 || data.length() > 4) {
                return ipV6Str;
            }
            int val = 0;
            try {
                val = NumberStringUtil.stringToInt(data.toLowerCase(), "0123456789abcdef");
            } catch (Exception e) {
                // TODO: handle exception
                val = -1;
                log.error("Error:ip6ToIp4", e);
            }
            if (val < 0 || val > 256 * 256 - 1) {
                return ipV6Str;
            }
            list.add(val / 256);
            list.add(val % 256);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(i);
        }
        return sb.toString();
    }

    /**
     * ipV4地址转换为ipV6地址
     * @param ipV4Str ipV4地址
     * @return ipV6地址
     */
    public static String ip4ToIp6(String ipV4Str) {
        if (null == ipV4Str || ipV4Str.length() <= 0) {
            return ipV4Str;
        }
        String[] ipStrArrays = ipV4Str.split("\\.");
        if (null == ipStrArrays || ipStrArrays.length != 4) {
            return ipV4Str;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (String data : ipStrArrays) {
            if (null == data || data.length() <= 0 || data.length() > 3) {
                return ipV4Str;
            }
            int val = 0;
            try {
                val = NumberStringUtil.stringToInt(data.toLowerCase(), "0123456789");
            } catch (Exception e) {
                // TODO: handle exception
                val = -1;
                log.error("Error:ip4ToIp6", e);
            }
            if (val < 0 || val > 255) {
                return ipV4Str;
            }
            list.add(val);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(NumberStringUtil.intToString(list.get(0) * 256 + list.get(1), "0123456789abcdef"));
        sb.append(":");
        sb.append(NumberStringUtil.intToString(list.get(2) * 256 + list.get(3), "0123456789abcdef"));
        return sb.toString();
    }

    /**
     * ipV6缩写地址转换为ipV6完整地址
     * @param ipV6Str ipV6缩写地址
     * @return ipV6完整地址
     */
    public static String parseToFullIpV6(String ipV6Str) {
        if (null == ipV6Str || ipV6Str.length() <= 0) {
            return ipV6Str;
        }
        int count = counterAppearNum(ipV6Str, '.');
        if (count > 0 && count != 3) {
            return ipV6Str;
        }
        if (count == 3) {
            int indexDouhao = ipV6Str.indexOf(".");
            log.debug("ipV4分隔符(.)起始位置->" + indexDouhao);
            int indexMaohao = ipV6Str.lastIndexOf(":");
            log.debug("ipV6分隔符(:)末尾位置->" + indexDouhao);
            if (indexDouhao <= indexMaohao) {
                return ipV6Str;
            }
            if (indexMaohao <= 0) {
                return ipV6Str;
            }
            String ip4 = ipV6Str.substring(indexMaohao + 1);
            String ip6 = ipV6Str.substring(0, indexMaohao);
            if (ip6.endsWith(":")) {
                ip6 = ip6 + ":";
            }
            return parseToFullIpV6(ip6, 6) + ":" + ip4ToIp6(ip4);
        } else {
            return parseToFullIpV6(ipV6Str, 8);
        }

    }

    /**
     * ipV6缩写地址转换为ipV6完整地址私有调用方法
     * @param ipV6Str ipV6缩写地址
     * @param ipSize ipV6的地址长度6或8
     * @return ipV6完整地址
     */
    private static String parseToFullIpV6(String ipV6Str, int ipSize) {

        if (null == ipV6Str || ipV6Str.length() <= 0) {
            return ipV6Str;
        }
        int count = counterAppearNum(ipV6Str, "::");
        if (count != 1) {
            return ipV6Str;
        }
        count = counterAppearNum(ipV6Str, ":::");
        if (count >= 1) {
            return ipV6Str;
        }
        int index = ipV6Str.indexOf("::");
        String headerStr = index > 0 ? ipV6Str.substring(0, index) : null;
        String footerStr = index < ipV6Str.length() - 2 ? ipV6Str.substring(index + 2) : null;
        if (null != headerStr && (headerStr.startsWith(":") || headerStr.endsWith(":"))) {
            return ipV6Str;
        }
        if (null != footerStr && (footerStr.startsWith(":") || footerStr.endsWith(":"))) {
            return ipV6Str;
        }
        // 转换footer位list
        String[] headerArray = null == headerStr || headerStr.length() <= 0 ? null : headerStr.split(":");
        String[] footerArray = null == footerStr || footerStr.length() <= 0 ? null : footerStr.split(":");
        int headerSize = null == headerArray ? 0 : headerArray.length;
        int footerSize = null == footerArray ? 0 : footerArray.length;
        if (headerSize + footerSize >= ipSize) {
            return ipV6Str;
        }
        StringBuilder sb = new StringBuilder();
        if (null != headerArray) {
            for (String tmp : headerArray) {
                if (sb.length() > 0) {
                    sb.append(":");
                }
                sb.append(tmp);
            }
        }
        for (int i = 0; i < ipSize - headerSize - footerSize; i++) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append("0");
        }
        if (null != footerArray) {
            for (String tmp : footerArray) {
                if (sb.length() > 0) {
                    sb.append(":");
                }
                sb.append(tmp);
            }
        }
        return sb.toString();
    }

}
