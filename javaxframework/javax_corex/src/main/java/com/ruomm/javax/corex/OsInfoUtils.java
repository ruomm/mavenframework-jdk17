package com.ruomm.javax.corex;

import com.ruomm.javax.corex.dal.OsEnvDal;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/11/30 下午1:50
 */
public class OsInfoUtils {
    public final static boolean IS_ADNROID;

    public static enum OsInfoEnum {
        WIN(1, "windows", "win"), LINUX(2, "linux", "win"),
        MAC(3, "macosx", "mac"), ANDROID(4, "android", "android"),
        UNKNOWN(99, "unknown", "unknown");
        /**
         * 系统类型，1-windows  2-linux 3-macos 4.android 其他.未知
         */
        public int osType;

        /**
         * 系统名称
         */
        public String osName;
        /**
         * 系统名称简称
         */
        public String osShortName;

        OsInfoEnum(int osType, String osName, String osShortName) {
            this.osType = osType;
            this.osName = osName;
            this.osShortName = osShortName;
        }

        public int getOsType() {
            return osType;
        }

        public void setOsType(int osType) {
            this.osType = osType;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsShortName() {
            return osShortName;
        }

        public void setOsShortName(String osShortName) {
            this.osShortName = osShortName;
        }
    }

    static {
        boolean isAndroid = false;
        try {
            Class<?> cls1 = Class.forName("android.text.TextUtils");
            Class<?> cls = Class.forName("android.os.Build");
            if (null != cls && null != cls1) {
                cls1 = null;
                cls = null;
                isAndroid = true;

            }
        } catch (Exception e) {
//			log.error("Error:",e);
        }
        IS_ADNROID = isAndroid;
    }

    public static String getOSName() {
        if (IS_ADNROID) {
            return "android";
        }
        String osName = System.getProperty("os.name");
        return osName;
    }


    public static String getOSNameToLower() {
        if (IS_ADNROID) {
            return "android";
        }
        String osName = System.getProperty("os.name");
        if (null != osName) {
            return osName.toLowerCase();
        } else {
            return null;
        }
    }


    public static boolean isWindows(String osName) {
        if (null == osName || osName.length() <= 0) {
            return false;
        }
        String osNameLower = osName.toLowerCase();
        return osNameLower.indexOf(OsInfoEnum.WIN.osName) >= 0;
    }

    public static boolean isLinux(String osName) {
        if (null == osName || osName.length() <= 0) {
            return false;
        }
        String osNameLower = osName.toLowerCase();
        return osNameLower.indexOf(OsInfoEnum.LINUX.osName) >= 0;
    }

    public static boolean isMacOS(String osName) {
        if (null == osName || osName.length() <= 0) {
            return false;
        }
        String osNameLower = osName.toLowerCase();
        return osNameLower.toLowerCase().indexOf(OsInfoEnum.MAC.osShortName) >= 0 && osNameLower.toLowerCase().indexOf("os") > 0;
    }

    public static boolean isMacOSNoX(String osName) {
        if (StringUtils.isEmpty(osName)) {
            return false;
        }
        String osNameLower = osName.toLowerCase();
        return osNameLower.indexOf(OsInfoEnum.MAC.osShortName) >= 0 && osNameLower.indexOf("os") > 0 && osNameLower.indexOf("x") < 0;
    }

    public static boolean isMacOSHasX(String osName) {
        if (StringUtils.isEmpty(osName)) {
            return false;
        }
        String osNameLower = osName.toLowerCase();
        return osNameLower.indexOf(OsInfoEnum.MAC.osShortName) >= 0 && osNameLower.indexOf("os") > 0 && osName.indexOf("x") > 0;
    }


    public static boolean isAndroid(String osName) {
        if (null == osName || osName.length() <= 0) {
            return false;
        }
        String osNameLower = osName.toLowerCase();
        return osNameLower.indexOf(OsInfoEnum.ANDROID.osShortName) >= 0;
    }

    /**
     * 解析操作系统类型
     *
     * @param defaultOsType 未知系统类型默认操作系统类型
     * @return 操作系统类型
     */
    public static OsEnvDal parseOsEnv(int defaultOsType) {
        String osName = OsInfoUtils.getOSName();
        String osNameLower = null == osName ? null : osName.toLowerCase();
        OsEnvDal osEnvDal = new OsEnvDal();
        osEnvDal.setOsName(osName);
        if (OsInfoUtils.isWindows(osNameLower)) {
            osEnvDal.setOsType(OsInfoEnum.WIN.osType);
            osEnvDal.setOsShortName(OsInfoEnum.WIN.osShortName);
        } else if (isLinux(osName)) {
            osEnvDal.setOsType(OsInfoEnum.LINUX.osType);
            osEnvDal.setOsShortName(OsInfoEnum.LINUX.osShortName);
        } else if (OsInfoUtils.isMacOS(osNameLower)) {
            osEnvDal.setOsType(OsInfoEnum.MAC.osType);
            osEnvDal.setOsShortName(OsInfoEnum.MAC.osShortName);
        } else if (isAndroid(osName)) {
            osEnvDal.setOsType(OsInfoEnum.ANDROID.osType);
            osEnvDal.setOsShortName(OsInfoEnum.ANDROID.osShortName);
        } else if (defaultOsType == OsInfoEnum.WIN.osType) {
            osEnvDal.setOsType(OsInfoEnum.WIN.osType);
            osEnvDal.setOsShortName(OsInfoEnum.WIN.osShortName);
        } else if (defaultOsType == OsInfoEnum.LINUX.osType) {
            osEnvDal.setOsType(OsInfoEnum.LINUX.osType);
            osEnvDal.setOsShortName(OsInfoEnum.LINUX.osShortName);
        } else if (defaultOsType == OsInfoEnum.MAC.osType) {
            osEnvDal.setOsType(OsInfoEnum.MAC.osType);
            osEnvDal.setOsShortName(OsInfoEnum.MAC.osShortName);
        } else if (defaultOsType == OsInfoEnum.ANDROID.osType) {
            osEnvDal.setOsType(OsInfoEnum.ANDROID.osType);
            osEnvDal.setOsShortName(OsInfoEnum.ANDROID.osShortName);
        } else {
            osEnvDal.setOsType(OsInfoEnum.UNKNOWN.osType);
            osEnvDal.setOsShortName(OsInfoEnum.UNKNOWN.osShortName);
        }
        return osEnvDal;
    }

    public static boolean isDev(String... detectDevPaths) {
        String projectDirOnDev = getProjectDirOnDev(detectDevPaths);
        if (StringUtils.isEmpty(projectDirOnDev)) {
            return false;
        } else {
            return true;
        }
    }

    public static String getProjectDirOnDev(String... detectDevPaths) {
        String webPath = WebUtils.getWwwroot();
        if (StringUtils.isEmpty(webPath)) {
            return null;
        }
        String webPathLower = webPath.toLowerCase();
        if ((webPathLower.contains("target/classes") || webPathLower.contains("target\\classes"))) {
            boolean decectResult;
            if (ListUtils.isEmpty(detectDevPaths)) {
                decectResult = true;
            } else {
                decectResult = true;
                for (String detectPath : detectDevPaths) {
                    if (StringUtils.isEmpty(detectPath)) {
                        continue;
                    } else if (!webPathLower.contains(detectPath.toLowerCase())) {
                        decectResult = false;
                        break;
                    }
                }
            }
            if (decectResult) {
                // 检测到target/classes和ideaprojects目录才认为是开发环境。
                int subIndex = webPathLower.length() - "target/classes".length();
                if (FileUtils.isEndWithFileSeparator(webPath)) {
                    subIndex--;
                }
                return webPath.substring(0, subIndex);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }


}
