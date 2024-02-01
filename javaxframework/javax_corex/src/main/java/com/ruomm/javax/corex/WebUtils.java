/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月10日 上午11:58:01
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;

public class WebUtils {
    private final static Log log = LogFactory.getLog(WebUtils.class);
    public final static String PATH_TAG_CLASS = "class:";
    public final static String PATH_TAG_CLASSPATH = "classpath:";
    public final static String PATH_TAG_FILE = "file:";
    public final static String PATH_TAG_FILEURI = "file://";
    public final static String PATH_TAG_WEBROOT = "webroot:";
    public final static String PATH_TAG_WEBDIR = "webdir:";
    public final static String PATH_TAG_USERDIR = "userdir:";

    private static String wwwWoot = null;
    private static String classesRoot = null;

    private static boolean LOG_DEBUG = false;

    private WebUtils() {
        super();
    }

    public static void configLogDebug(boolean logDebug) {
        LOG_DEBUG = logDebug;
    }

    /**
     * 获取Web容器路径绝对地址,地址文件夹连接符号(/)结尾
     *
     * @return Web容器路径绝对地址, 地址文件夹连接符号(/)结尾
     */
    public static String getWwwroot() {
        if (null == wwwWoot) {
            try {
                String dir = WebUtils.class.getResource("/").getPath();
                int size = dir.length();
                if (dir.toLowerCase().endsWith("web-inf/classes/")
                        || dir.toLowerCase().endsWith("web-inf\\classes\\")) {
                    wwwWoot = dir.substring(0, size - 16).replace("%20", " ");
                } else {
                    String clsName = WebUtils.class.getName();
                    int length = WebUtils.class.getSimpleName().length();
                    String clsPath = "web-inf/classes/"
                            + clsName.substring(0, clsName.length() - length).replace(".", "/");
                    int index = dir.toLowerCase().indexOf(clsPath);
                    if (index < 0) {
                        index = dir.toLowerCase().indexOf(clsPath.replace("/", "\\"));
                    }
                    if (index > 0) {
                        wwwWoot = dir.substring(0, index).replace("%20", " ");
                    } else {
                        wwwWoot = dir;
                    }
                }
            } catch (Exception e) {
                if (LOG_DEBUG) {
                    log.error("Error:getWwwroot", e);
                }
            }
        }
        return wwwWoot;
    }

    /**
     * 获取Class容器路径绝对地址,地址文件夹连接符号(/)结尾
     *
     * @return Class容器路径绝对地址
     */
    public static String getClassesRoot() {

        if (null == classesRoot) {
            try {
                String dir = WebUtils.class.getResource("/").getPath();
                int size = dir.length();
                String clsName = WebUtils.class.getName();
                int length = WebUtils.class.getSimpleName().length();
                String clsPath = clsName.substring(0, clsName.length() - length).replace(".", "/");
                if (dir.endsWith(clsPath) || dir.endsWith(clsPath.replace("/", "\\"))) {
                    classesRoot = dir.substring(0, size - clsPath.length()).replace("%20", " ");
                } else {
                    classesRoot = dir.replace("%20", " ");
                }

            } catch (Exception e) {
                classesRoot = null;
                if (LOG_DEBUG) {
                    log.error("Error:getClassesRoot-getResource", e);
                }
            }
        }

        if (null == classesRoot) {
            try {
                String clsPathByProperty = System.getProperty("java.class.path");
                String pathSeparator = System.getProperty("path.separator");
                String resultPath;
                if (null == pathSeparator || pathSeparator.length() <= 0) {
                    resultPath = null;
                } else {
                    String clsPath;
                    if (!clsPathByProperty.contains(pathSeparator)) {
                        clsPath = clsPathByProperty;
                    } else {
                        String tmpClsPath = null;
                        String[] tmpPaths = clsPathByProperty.split(pathSeparator);
                        for (String tmpPath : tmpPaths) {
                            if (null == tmpPath || tmpPath.length() <= 0) {
                                continue;
                            } else if (tmpPath.toLowerCase().endsWith(".jar")) {
                                continue;
                            } else if (tmpPath.toLowerCase().replace("\\", "/").endsWith("target/classes")) {
                                tmpClsPath = tmpPath;
                                break;
                            } else {
                                tmpClsPath = tmpPath;
                                break;
                            }
                        }
                        if (null == tmpClsPath || tmpClsPath.length() <= 0) {
                            tmpClsPath = tmpPaths[0];
                        }
                        clsPath = tmpClsPath;
                    }
                    if (clsPath.toLowerCase().endsWith(".jar")) {
                        int filePosi1 = clsPath.lastIndexOf("\\");
                        int filePosi2 = clsPath.lastIndexOf("/");
                        int filePosiSub;
                        if (filePosi1 < 0 && filePosi2 < 0) {
                            filePosiSub = 0;
                        } else {
                            filePosiSub = filePosi1 > filePosi2 ? filePosi1 : filePosi2;
                        }
                        resultPath = clsPath.substring(0, filePosiSub);
                    } else {
                        resultPath = clsPath;
                    }
                    if (null != resultPath && resultPath.length() > 0 && !resultPath.endsWith("\\") && !resultPath.endsWith("/")) {
                        resultPath = resultPath + File.separator;
                    }
                }
//                if (null != resultPath && resultPath.length() > 0) {
//                    classesRoot = resultPath;
//                }
                classesRoot = resultPath;

            } catch (Exception e) {
                classesRoot = null;
                if (LOG_DEBUG) {
                    log.error("Error:getClassesRoot-java.class.path", e);
                }
            }
        }

        return classesRoot;
    }

    /**
     * 解析自定义协议路径为真实路径，支持class:、classpath:、webroot:、webdir:、file:、file://、userdir:
     *
     * @param filePath 相对地址 如class:xxxx、classpath:xxxx、webroot:xxxx、webdir:xxxx、file:xxxx、file://xxxx、userdir:xxxx
     * @return
     */
    public static String parseRealPath(String filePath) {
        if (null == filePath || filePath.length() == 0) {
            return filePath;
        }
        String filePathLower = filePath.toLowerCase();
        if (filePathLower.startsWith(PATH_TAG_CLASSPATH)) {
            return StringUtils.nullStrToEmpty(getClassesRoot()) + filePath.substring(PATH_TAG_CLASSPATH.length());
        } else if (filePathLower.startsWith(PATH_TAG_CLASS)) {
            return StringUtils.nullStrToEmpty(getClassesRoot()) + filePath.substring(PATH_TAG_CLASS.length());
        } else if (filePathLower.startsWith(PATH_TAG_WEBROOT)) {
            return StringUtils.nullStrToEmpty(getWwwroot()) + filePath.substring(PATH_TAG_WEBROOT.length());
        } else if (filePathLower.startsWith(PATH_TAG_WEBDIR)) {
            return StringUtils.nullStrToEmpty(getWwwroot()) + filePath.substring(PATH_TAG_WEBDIR.length());
        } else if (filePathLower.startsWith(PATH_TAG_FILEURI)) {
            return filePath.substring(PATH_TAG_FILEURI.length());
        } else if (filePathLower.startsWith(PATH_TAG_FILE)) {
            return filePath.substring(PATH_TAG_FILE.length());
        } else if (filePathLower.startsWith(PATH_TAG_USERDIR)) {
            return filePath.substring(PATH_TAG_USERDIR.length());
        } else {
            return filePath;
        }
    }

    /**
     * 解析自定义协议路径为真实文件夹，结尾包含文件分隔符，支持class:、classpath:、webroot:、webdir:、file:、file://、userdir:
     *
     * @param filePath 相对地址 如class:xxxx、classpath:xxxx、webroot:xxxx、webdir:xxxx、file:xxxx、file://xxxx、userdir:xxxx
     * @return
     */
    public static String parseRealDir(String filePath) {
        return parseRealDir(filePath, true);
    }

    /**
     * 解析自定义协议路径为真实文件夹，支持class:、classpath:、webroot:、webdir:、file:、file://、userdir:
     *
     * @param filePath               相对地址 如class:xxxx、classpath:xxxx、webroot:xxxx、webdir:xxxx、file:xxxx、file://xxxx、userdir:xxxx
     * @param isEndWithFileSeparator 结尾是否含文件分隔符，默认true
     * @return
     */
    public static String parseRealDir(String filePath, boolean isEndWithFileSeparator) {
        String realPath = parseRealPath(filePath);

        if (null == realPath || realPath.length() == 0) {
            return realPath;
        }
        if (isEndWithFileSeparator) {
            if (FileUtils.isEndWithFileSeparator(realPath)) {
                return realPath;
            } else {
                return realPath + FileUtils.parseFileSeparator(realPath);
            }
        } else {
            if (FileUtils.isEndWithFileSeparator(realPath)) {
                int subSize = realPath.length() - 1;
                return realPath.substring(0, subSize);
            } else {
                return realPath;
            }
        }
    }


    /**
     * 判断路径是否可以转换为绝对地址
     *
     * @param filePath 判断路径是否可以转换为绝对地址，可以转换为绝对地址的使用parseRealPath后的地址为绝对地址，否则是相对地址
     * @return
     */
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
        } else if (pathLower.startsWith(PATH_TAG_USERDIR)) {
            return false;
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

    /**
     * 解析自定义协议路径为真实路径，支持class:、classpath:、webroot:、webdir:、file:、file://、userdir:
     *
     * @param filePath        相对地址 如class:xxxx、classpath:xxxx、webroot:xxxx、webdir:xxxx、file:xxxx、file://xxxx、userdir:
     * @param idDeaModuleName Idea工作空间名称
     * @return
     */
    public static String parseRealPathForIdea(String filePath, String idDeaModuleName) {
        if (null == filePath || filePath.length() == 0) {
            return filePath;
        }

        String filePathLower = filePath.toLowerCase();
        if (filePathLower.startsWith(PATH_TAG_USERDIR) && null != idDeaModuleName && idDeaModuleName.length() > 0 && isIntelliJIdea()) {
            return idDeaModuleName + "/" + filePath.substring(PATH_TAG_USERDIR.length());
        } else {
            return parseRealPath(filePath);
        }
    }

    /**
     * 判断工具是否IntelliJIdea
     *
     * @return 是否IntelliJIdea
     */
    public static boolean isIntelliJIdea() {
        String javaClassPath = System.getProperty("java.class.path");
        if (null != javaClassPath && javaClassPath.contains("idea_rt.jar")) {
            return true;
        }
        return false;
    }

    /**
     * 判断URL地址路径是否http或https协议地址
     *
     * @param url URL地址路径
     * @return 否http或https协议地址, URL地址路径以https:或http:开头返回true，其他返回false
     */
    public static boolean isHttpOrHttpsUrl(String url) {
        if (null == url || url.length() <= 0) {
            return false;
        }
        String urlLower = url.toLowerCase();
        if (urlLower.startsWith("http:")) {
            return true;
        } else if (urlLower.startsWith("https:")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断URL地址路径是否http协议地址
     *
     * @param url URL地址路径
     * @return 是否http协议地址，URL地址路径以http:开头返回true，其他返回false
     */
    public static boolean isHttpOnlyUrl(String url) {
        if (null == url || url.length() <= 0) {
            return false;
        }
        String urlLower = url.toLowerCase();
        if (urlLower.startsWith("http:")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断URL地址路径是否https协议地址
     *
     * @param url URL地址路径
     * @return 是否https协议地址，URL地址路径以https:开头返回true，其他返回false
     */
    public static boolean isHttpsOnlyUrl(String url) {
        if (null == url || url.length() <= 0) {
            return false;
        }
        String urlLower = url.toLowerCase();
        if (urlLower.startsWith("https:")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据级别获取URI地址
     *
     * @param uri 完整的URI地址
     * @return URI地址
     * @DefaultParam levelNum uri的级别。一般tomcat容器级别为1级，springboot容器为0级。
     * @DefaultParam separatorReplace 是否替换反斜杠符号为斜杠符号，默认true
     */

    public static String getRealUri(String uri) {
        return getRealUri(uri, 0, true);
    }

    /**
     * 根据级别获取URI地址
     *
     * @param uri      完整的URI地址
     * @param levelNum uri的级别。一般tomcat容器级别为1级，springboot容器为0级。
     * @return URI地址
     * @DefaultParam separatorReplace 是否替换反斜杠符号为斜杠符号，默认true
     */
    public static String getRealUri(String uri, int levelNum) {
        return getRealUri(uri, levelNum, true);
    }

    /**
     * 根据级别获取URI地址
     *
     * @param uri              完整的URI地址
     * @param levelNum         uri的级别。一般tomcat容器级别为1级，springboot容器为0级。
     * @param separatorReplace 是否替换反斜杠符号为斜杠符号
     * @return URI地址
     */
    public static String getRealUri(String uri, int levelNum, boolean separatorReplace) {
        if (null == uri || uri.length() <= 0) {
            return null;
        }
        String tmpUri;
        if (separatorReplace) {
            tmpUri = uri.replaceAll("\\\\", "/").replaceAll("[/]+", "/");
        } else {
            tmpUri = uri;
        }
        if (!tmpUri.startsWith("/")) {
            tmpUri = "/" + tmpUri;
        }
        if (tmpUri.endsWith("/")) {
            int st = tmpUri.length();
            tmpUri = tmpUri.substring(0, st - 1);
        }
        if (null == tmpUri || tmpUri.length() <= 0) {
            return null;
        }
        for (int i = 0; i < levelNum; i++) {
            if (null == tmpUri || tmpUri.length() <= 0) {
                break;
            }
            tmpUri = tmpUri.substring(1);
            int index = tmpUri.indexOf("/");
            if (index > 0) {
                tmpUri = tmpUri.substring(index);
            } else {
                tmpUri = null;
                break;
            }
        }
        return tmpUri;
    }

    /**
     * 转换URL地址为URI地址
     *
     * @param url URL地址
     * @return URI地址
     */
    public static String getRealUriByUrl(String url) {
        if (null == url || url.length() <= 0) {
            return null;
        }
        String realUrl = null;
        int indexW = url.indexOf("?");
        if (indexW > 0) {
            realUrl = url.substring(0, indexW);
        } else {
            realUrl = url;
        }
        realUrl = realUrl.replace("\\\\", "/").replaceAll("[/]+", "/").replaceFirst(":/", "://");
        String resultUri = null;
        if (realUrl.startsWith("http://")) {
            resultUri = realUrl.substring(7);
            int tmp = resultUri.indexOf("/");
            if (tmp < 0) {
                resultUri = null;
            } else {
                resultUri = resultUri.substring(tmp);
            }
        } else if (realUrl.startsWith("https://")) {
            resultUri = realUrl.substring(8);
            int tmp = resultUri.indexOf("/");
            if (tmp < 0) {
                resultUri = null;
            } else {
                resultUri = resultUri.substring(tmp);
            }
        } else {
            if (realUrl.startsWith("/")) {
                resultUri = realUrl;
            } else {
                resultUri = "/" + realUrl;
            }
        }
        if (null != resultUri && resultUri.endsWith("/")) {
            int st = resultUri.length();
            resultUri = resultUri.substring(0, st - 1);
        }
        if (null == resultUri || resultUri.length() <= 0) {
            return null;
        } else {
            return resultUri;
        }
    }

    /**
     * 根据级别转换URI地址为数组
     *
     * @param uri 完整的URI地址
     * @return 转换后的URI地址数组
     * @DefaultParam levelNum uri的级别。一般tomcat容器级别为1级，springboot容器为0级。
     * @DefaultParam separatorReplace 是否替换反斜杠符号为斜杠符号
     */
    public static String[] getRealUriToArray(String uri) {
        String realUri = getRealUri(uri, 0);
        if (null == realUri || realUri.length() <= 0) {
            return null;
        }
        if (realUri.startsWith("/")) {
            return realUri.substring(1).split("/");
        } else {
            return realUri.split("/");
        }
    }

    /**
     * 根据级别转换URI地址为数组
     *
     * @param uri      完整的URI地址
     * @param levelNum uri的级别。一般tomcat容器级别为1级，springboot容器为0级。
     * @return 转换后的URI地址数组
     * @DefaultParam separatorReplace 是否替换反斜杠符号为斜杠符号
     */
    public static String[] getRealUriToArray(String uri, int levelNum) {
        String realUri = getRealUri(uri, levelNum);
        if (null == realUri || realUri.length() <= 0) {
            return null;
        }
        if (realUri.startsWith("/")) {
            return realUri.substring(1).split("/");
        } else {
            return realUri.split("/");
        }
    }

    /**
     * 根据级别转换URI地址为数组
     *
     * @param uri              完整的URI地址
     * @param levelNum         uri的级别。一般tomcat容器级别为1级，springboot容器为0级。
     * @param separatorReplace 是否替换反斜杠符号为斜杠符号
     * @return 转换后的URI地址数组
     */
    public static String[] getRealUriToArray(String uri, int levelNum, boolean separatorReplace) {
        String realUri = getRealUri(uri, levelNum, separatorReplace);
        if (null == realUri || realUri.length() <= 0) {
            return null;
        }
        if (realUri.startsWith("/")) {
            return realUri.substring(1).split("/");
        } else {
            return realUri.split("/");
        }
    }

    public static int counterChar(String str, char c) {
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
}
