/**
 * @copyright wanruome-2017
 * @author 牛牛-wanruome@163.com
 * @create 2017年3月30日 上午9:26:35
 */
package com.ruomm.assistx.cmdx;

public class IPUtils {
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
                val = Integer.valueOf(data);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                val = -1;

            }
            if (val < 0 || val > 255) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否ipV4子网掩码
     * @param ipStr ip子网掩码
     * @return 是否ipV4子网掩码
     */
    public static boolean isIpV4Mask(String ipStr) {
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
        int dataLength = ipStrArrays.length;
        boolean isZeroEnd = false;
        for (int i = dataLength - 1; i >= 0; i--) {
            String data = ipStrArrays[i];
            if (null == data || data.length() <= 0 || data.length() > 3) {
                return false;
            }
            int val = 0;
            try {
                val = Integer.valueOf(data);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                val = -1;

            }
            if (val < 0 || val > 255) {
                return false;
            }
            if (isZeroEnd) {
                if (val != 255) {
                    return false;
                }
            }
            if (i == 0 && val < 128) {
                return false;
            }
            if (val > 0) {
                isZeroEnd = true;
            }
        }
        return true;
    }

    /**
     * 解析为真实的ipV4地址
     * @param ipStr ip地址
     * @return 真实的ipV4地址
     */
    public static String parseIpV4(String ipStr) {
        if (!isIpV4(ipStr)) {
            return null;
        }
        StringBuilder ipSb = new StringBuilder();
        String[] ipStrArrays = ipStr.split("\\.");
        for (String data : ipStrArrays) {
            int val = 0;
            try {
                val = Integer.valueOf(data);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                val = -1;

            }
            if (val < 0 || val > 255) {
                return null;
            }
            if (ipSb.length() > 0) {
                ipSb.append(".");
            }
            ipSb.append(String.valueOf(val));
        }
        return ipSb.toString();
    }

    /**
     * 解析为真实的ipV4子网掩码
     * @param ipStr ip子网掩码
     * @return 真实的ipV4子网掩码
     */
    public static String parseIpV4Mask(String ipStr) {
        if (!isIpV4Mask(ipStr)) {
            return null;
        }
        StringBuilder ipSb = new StringBuilder();
        String[] ipStrArrays = ipStr.split("\\.");
        for (String data : ipStrArrays) {
            int val = 0;
            try {
                val = Integer.valueOf(data);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                val = -1;

            }
            if (val < 0 || val > 255) {
                return null;
            }
            if (ipSb.length() > 0) {
                ipSb.append(".");
            }
            ipSb.append(String.valueOf(val));
        }
        return ipSb.toString();
    }

    public static void main(String[] args) {
        System.out.println(isIpV4Mask("224.12.0.0"));
    }
}
