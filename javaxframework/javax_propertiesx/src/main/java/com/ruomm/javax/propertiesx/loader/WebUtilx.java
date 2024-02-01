/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月10日 上午11:58:01
 */
package com.ruomm.javax.propertiesx.loader;

class WebUtilx {
    private final static String PATH_TAG_CLASS = "class:";
    private final static String PATH_TAG_CLASSPATH = "classpath:";
    private final static String PATH_TAG_FILE = "file:";
    private final static String PATH_TAG_FILEURI = "file://";
    private final static String PATH_TAG_WEBROOT = "webroot:";
    private final static String PATH_TAG_WEBDIR = "webdir:";

    public static boolean isAbsolutePath(String filePath) {
        if (null == filePath || filePath.length() == 0) {
            return false;
        }
        String pathLower = filePath.toLowerCase();
        if (pathLower.startsWith(PATH_TAG_CLASSPATH)) {
            return true;
        } else if (pathLower.startsWith(PATH_TAG_CLASS)) {
            return true;
        } else if (pathLower.startsWith(PATH_TAG_WEBROOT)) {
            return true;
        } else if (pathLower.startsWith(PATH_TAG_WEBDIR)) {
            return true;
        } else if (pathLower.startsWith(PATH_TAG_FILEURI)) {
            return true;
        } else if (pathLower.startsWith(PATH_TAG_FILE)) {
            return true;
        } else if (pathLower.startsWith("\\") || pathLower.startsWith("/")) {
            return true;
        } else if (pathLower.startsWith(":")) {
            return true;
        } else if (pathLower.length() >= 2 && pathLower.substring(1, 2).equalsIgnoreCase(":")) {
            return true;
        } else {
            int index = pathLower.indexOf(":");
            if (index >= 0 && index < 12) {
                return true;
            } else {
                return false;
            }
        }
    }
}
