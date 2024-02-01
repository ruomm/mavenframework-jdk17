package com.ruomm.javax.httpx.util;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.*;
import java.util.Base64;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/7/12 16:43
 */
public class HttpxFileBase64Utils {
    private final static Log log = LogFactory.getLog(HttpxFileBase64Utils.class);
    private final static int INOUT_BUFF_SIZE = 1024;

    /**
     * 读取文件为BASE64编码格式
     *
     * @param filePath 文件路径
     * @return 文件BASE64编码格式内容
     * @paramDefault addFileName 是否添加文件名称头，如添加名称和内容之间以:分割，默认值true
     */
    public static String parseFileToBase64(String filePath) {
        return parseFileToBase64(filePath, true);
    }

    /**
     * 读取文件为BASE64编码格式
     *
     * @param filePath    文件路径
     * @param addFileName 是否添加文件名称头，如添加名称和内容之间以:分割
     * @return 文件BASE64编码格式内容
     */
    public static String parseFileToBase64(String filePath, boolean addFileName) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        byte[] data = readFileToByte(filePath);
        if (null == data || data.length <= 0) {
            return null;
        }
        if (addFileName) {
            String fileName = FileUtils.getFileName(filePath);
            if (StringUtils.isEmpty(filePath)) {
                return Base64.getEncoder().encodeToString(data);
            } else {
                return fileName + ":" + Base64.getEncoder().encodeToString(data);
            }
        } else {
            return Base64.getEncoder().encodeToString(data);
        }
    }

    /**
     * 读取文件列表为BASE64编码格式，多个文件以;分割
     *
     * @param filePathList 文件路径列表
     * @return 文件列表BASE64编码格式内容，多个文件以;分割
     * @paramDefault addFileName 是否添加文件名称头，如添加名称和内容之间以:分割，默认值true
     */
    public static String parseFileListToBase64(List<String> filePathList) {
        return parseFileListToBase64(filePathList, true);
    }

    /**
     * 读取文件列表为BASE64编码格式，多个文件以;分割
     *
     * @param filePathList 文件路径列表
     * @param addFileName  是否添加文件名称头，如添加名称和内容之间以:分割
     * @return 文件列表BASE64编码格式内容，多个文件以;分割
     */
    public static String parseFileListToBase64(List<String> filePathList, boolean addFileName) {
        if (null == filePathList || filePathList.size() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String tmpFilePath : filePathList) {
            String tmpBase64 = parseFileToBase64(tmpFilePath, addFileName);
            if (null == tmpBase64 || tmpBase64.length() <= 0) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(tmpBase64);
        }
        if (sb.length() <= 0) {
            return null;
        } else {
            return sb.toString();
        }
    }

    /**
     * 读取文件为byte字节数组
     *
     * @param filePath -文件路径
     * @return byte字节数组
     */
    public static byte[] readFileToByte(String filePath) {
        byte[] buf = null;
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                buf = readInputStreamToByte(new FileInputStream(file), true);
            } else {
                buf = null;
            }
        } catch (Exception e) {
            buf = null;
            log.error("Error:readFileToByte", e);
        }
        return buf;
    }

    /**
     * 读取输入流为byte字节数组
     *
     * @param is       输入流
     * @param isBuffer 是否使用Buffer模式，默认true
     * @return 读取输入流为byte字节数组结果
     */
    public static byte[] readInputStreamToByte(InputStream is, boolean isBuffer) {
        if (null == is) {
            return null;
        }
        if (isBuffer) {
            byte[] resultByte = null;
            BufferedInputStream in = null;
            ByteArrayOutputStream bos = null;
            try {
                in = new BufferedInputStream(is);
                bos = new ByteArrayOutputStream(is.available());
                byte[] buffer = new byte[INOUT_BUFF_SIZE];
                int len = 0;
                while (-1 != (len = in.read(buffer, 0, INOUT_BUFF_SIZE))) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                resultByte = bos.toByteArray();
            } catch (Exception e) {
                log.error("Error:readInputStreamToByte", e);
            } finally {
                try {
                    if (null != in) {
                        in.close();
                    }
                } catch (IOException e) {
                    log.error("Error:readInputStreamToByte", e);
                }
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.error("Error:readInputStreamToByte", e);
                }
                try {
                    if (null != bos) {
                        bos.close();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:readInputStreamToByte", e);
                }
            }
            return resultByte;
        } else {
            byte[] resultByte = null;
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream(is.available());
                byte[] buffer = new byte[INOUT_BUFF_SIZE];
                int len = 0;
                while (-1 != (len = is.read(buffer, 0, INOUT_BUFF_SIZE))) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                resultByte = bos.toByteArray();
            } catch (Exception e) {
                log.error("Error:readInputStreamToByte", e);
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.error("Error:readInputStreamToByte", e);
                }
                try {
                    if (null != bos) {
                        bos.close();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:readInputStreamToByte", e);
                }

            }
            return resultByte;
        }
    }
}
