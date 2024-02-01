package com.ruomm.javax.httpx.util;

import com.ruomm.javax.corex.DigestByJdkUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/16 13:24
 */
public class JavaxHttpxUtils {
    private final static Log log = LogFactory.getLog(JavaxHttpxUtils.class);
    private static String TMP_UPLOAD_PATH = null;

    /**
     * 获取文件上传的临时文件路径
     *
     * @return Class容器路径绝对地址
     */
    public static String parseHttpxUploadPath(String uploadPath) {
        if (null != uploadPath && uploadPath.length() > 0) {
            if (uploadPath.endsWith("\\") || uploadPath.endsWith("/")) {
                return uploadPath;
            } else {
                String fileSeparator = FileUtils.parseFileSeparator(uploadPath);
                return uploadPath + fileSeparator;
            }
        } else {
            if (null == TMP_UPLOAD_PATH) {
                String tmpUploadPath = null;
                try {
                    String dir = JavaxHttpxUtils.class.getResource("/").getPath();
                    int size = dir.length();
                    String clsName = JavaxHttpxUtils.class.getName();
                    int length = JavaxHttpxUtils.class.getSimpleName().length();
                    String clsPath = clsName.substring(0, clsName.length() - length).replace(".", "/");
                    if (dir.endsWith(clsPath) || dir.endsWith(clsPath.replace("/", "\\"))) {
                        tmpUploadPath = dir.substring(0, size - clsPath.length()).replace("%20", " ");
                    } else {
                        tmpUploadPath = dir.replace("%20", " ");
                    }
                    tmpUploadPath = FileUtils.concatDirPath(tmpUploadPath, "temp\\tmp_javax_upload", true);
                } catch (Exception e) {
                    tmpUploadPath = "temp" + File.separator + "tmp_javax_upload" + File.separator;
                    log.error("Error:getClassesRoot", e);
                }
                TMP_UPLOAD_PATH = tmpUploadPath;
            }
            return TMP_UPLOAD_PATH;
        }
    }

    /**
     * 解析byte[]上传的文件
     */
    public static FileBytesForHttp parseFileBytesForHttp(String name, String fileName, byte[] fileBytes, String tmp_uploadPath) {
        if (null == fileBytes || fileBytes.length <= 0) {
            return null;
        }
        String realName;
        if (StringUtils.isEmpty(name)) {
            realName = "file";
        } else {
            realName = name;
        }
        String realFileName;
        if (StringUtils.isEmpty(fileName)) {
            realFileName = realName + ".tmp_upload";
        } else {
            realFileName = fileName;
        }
        File file = FileUtils.createFile(tmp_uploadPath + UUID.randomUUID().toString().trim().replaceAll("-", "") + ".tmp_upload");
        if (null == file) {
            return null;
        }
        boolean saveResult = FileUtils.writeFile(file, new ByteArrayInputStream(fileBytes), false);
        if(saveResult){
            FileBytesForHttp fileBytesForHttp = new FileBytesForHttp();
            fileBytesForHttp.setName(realName);
            fileBytesForHttp.setFileName(realFileName);
            fileBytesForHttp.setFile(file);
            return fileBytesForHttp;
        } else {
            return null;
        }
    }

    /**
     * 解析byte[]上传的文件
     */
    public static FileBytesForHttp parseFileBytesForHttp(String name, String fileName, byte[] fileBytes) {
        if (null == fileBytes || fileBytes.length <= 0) {
            return null;
        }
        String realName;
        if (StringUtils.isEmpty(name)) {
            realName = "file";
        } else {
            realName = name;
        }
        String realFileName;
        if (StringUtils.isEmpty(fileName)) {
            realFileName = realName + ".tmp_upload";
        } else {
            realFileName = fileName;
        }
        FileBytesForHttp fileBytesForHttp = new FileBytesForHttp();
        fileBytesForHttp.setName(realName);
        fileBytesForHttp.setFileName(realFileName);
        fileBytesForHttp.setFileBytes(fileBytes);
        return fileBytesForHttp;
    }

    /**
     * 解析Base64上传的文件
     */
    public static List<FileBase64> parseFileBase64ByFile(String name, String fileName, String base64Content, String tmp_uploadPath) {
        if (null == base64Content || base64Content.length() <= 0) {
            return null;
        }
        String[] contents = null;
        if (base64Content.contains(";")) {
            contents = base64Content.split(";");
        } else {
            contents = new String[]{base64Content};
        }
        if (null == contents || contents.length <= 0) {
            return null;
        }
        List<FileBase64> fileBase64List = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            String content = contents[i];
            if (null == content || content.length() <= 0) {
                continue;
            }
            String realName = null;
            if (StringUtils.isEmpty(name)) {
                realName = "file";
            } else {
                realName = name;
            }
            String realFileName = null;
            String realContent = null;
            int index = content.indexOf(":");
            if (index > 0 && index < content.length()) {
                realFileName = content.substring(0, index);
                realContent = content.substring(index + 1);
            } else {
                realContent = content;
                if (StringUtils.isEmpty(fileName)) {
                    if (contents.length > 1) {
                        realFileName = realName + String.format("%02d", i + 1) + ".tmp_upload";
                    } else {
                        realFileName = realName + ".tmp_upload";
                    }
                } else {
                    if (contents.length > 0) {
                        int extenPosi = fileName.lastIndexOf(".");
                        if (extenPosi > 0 && extenPosi < fileName.length()) {
                            realFileName = fileName.substring(0, extenPosi) + String.format("%02d", i + 1) + fileName.substring(extenPosi);
                        } else {
                            realFileName = fileName + String.format("%02d", i + 1);
                        }
                    } else {
                        realFileName = fileName;
                    }
                }
            }
            FileBase64 fileBase64 = new FileBase64();
            fileBase64.setName(realName);
            fileBase64.setFileName(realFileName);
            File file = FileUtils.createFile(tmp_uploadPath + UUID.randomUUID().toString().trim().replaceAll("-", "") + ".tmp_upload");
            if (null == file) {
                continue;
            }
            byte[] base64Data = null;
            try {
                base64Data = Base64.getDecoder().decode(realContent);
            } catch (Exception e) {
                log.error("Error:parseFileBase64", e);
                base64Data = null;
            }
            FileUtils.writeFile(file, new ByteArrayInputStream(base64Data), false);
            fileBase64.setFile(file);
            fileBase64List.add(fileBase64);
        }
        return fileBase64List;
    }

    /**
     * 解析Base64上传的文件
     */
    public static List<FileBase64> parseFileBase64ByContent(String name, String fileName, String base64Content) {
        if (null == base64Content || base64Content.length() <= 0) {
            return null;
        }
        String[] contents = null;
        if (base64Content.contains(";")) {
            contents = base64Content.split(";");
        } else {
            contents = new String[]{base64Content};
        }
        if (null == contents || contents.length <= 0) {
            return null;
        }
        List<FileBase64> fileBase64List = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            String content = contents[i];
            if (null == content || content.length() <= 0) {
                continue;
            }
            String realName = null;
            if (StringUtils.isEmpty(name)) {
                realName = "file";
            } else {
                realName = name;
            }
            String realFileName = null;
            String realContent = null;
            int index = content.indexOf(":");
            if (index > 0 && index < content.length()) {
                realFileName = content.substring(0, index);
                realContent = content.substring(index + 1);
            } else {
                realContent = content;
                if (StringUtils.isEmpty(fileName)) {
                    if (contents.length > 1) {
                        realFileName = realName + String.format("%02d", i + 1) + ".tmp_upload";
                    } else {
                        realFileName = realName + ".tmp_upload";
                    }
                } else {
                    if (contents.length > 0) {
                        int extenPosi = fileName.lastIndexOf(".");
                        if (extenPosi > 0 && extenPosi < fileName.length()) {
                            realFileName = fileName.substring(0, extenPosi) + String.format("%02d", i + 1) + fileName.substring(extenPosi);
                        } else {
                            realFileName = fileName + String.format("%02d", i + 1);
                        }
                    } else {
                        realFileName = fileName;
                    }
                }
            }
            FileBase64 fileBase64 = new FileBase64();
            fileBase64.setName(realName);
            fileBase64.setFileName(realFileName);
            fileBase64.setBase64Content(realContent);
            fileBase64List.add(fileBase64);
        }
        return fileBase64List;
    }

    /**
     * 日志打印时候字符串显示内容解析
     *
     * @param str 原始字符串
     * @return 日志显示字符串
     */
    public static String parseLogString(String str) {
        int strLength = null == str ? 0 : str.length();
        if (strLength <= 0) {
            return str;
        }
        int showSize = HttpConfig.LOG_SIZE_SHOW_MAX;
        int md5Size = HttpConfig.LOG_SIZE_MD5_MAX;
        // 不限制显示长度
        if (showSize <= 0) {
            if (md5Size > 0 && strLength > md5Size) {
                return "md5(" + DigestByJdkUtils.encodingMD5(str) + ")";
            } else {
                return str;
            }
        } else {
            if (strLength <= showSize) {
                return str;
            } else {
                if (md5Size > showSize) {
                    if (strLength <= md5Size) {
                        return str.substring(0, showSize);
                    } else {
                        return "md5(" + DigestByJdkUtils.encodingMD5(str) + ")";
                    }
                } else {
                    return str.substring(0, showSize);
                }
            }
        }
    }
}
