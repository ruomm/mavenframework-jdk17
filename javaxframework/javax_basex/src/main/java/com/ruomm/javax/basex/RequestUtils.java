package com.ruomm.javax.basex;

import com.ruomm.javax.basex.imagex.ImageCompressUtil;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/29 10:45
 */
public class RequestUtils {

    private final static Log log = LogFactory.getLog(RequestUtils.class);

    public static int parseServletContextUriLevel(ServletContext servletContext) {
        if (null == servletContext) {
            return 0;
        }
        String tmpUri = servletContext.getContextPath();
        if (null != tmpUri && tmpUri.length() > 0) {
            tmpUri = tmpUri.replaceAll("\\\\", "/").replaceAll("[/]+", "/");
            if (!tmpUri.startsWith("/")) {
                tmpUri = "/" + tmpUri;
            }
            if (tmpUri.endsWith("/")) {
                int st = tmpUri.length();
                tmpUri = tmpUri.substring(0, st - 1);
            }
        }
        if (null == tmpUri || tmpUri.length() <= 0 || tmpUri.equalsIgnoreCase("/")) {
            return 0;
        } else {
            int counter = 0;
            for (int i = 0; i < tmpUri.length(); i++) {
                if (tmpUri.charAt(i) == '/') {
                    counter++;
                }
            }
            return counter;
        }
    }

    public static boolean isOkHttpRequest(HttpServletRequest request) {
        boolean isOkHttp = false;
        try {
            String requstUserAgent = request.getHeader("user-agent");
            if (null != requstUserAgent && requstUserAgent.toLowerCase().contains("okhttp")) {
                isOkHttp = true;
            }
        } catch (Exception e) {
            log.error("判断请求是否OKhttp时候出错", e);
            isOkHttp = false;
        }
        return isOkHttp;
    }

    /**
     * 把输入流写入文件
     *
     * @param filePath           要打开用于写入的文件路径。
     * @param httpServletRequest 输入流
     * @param maxSize            最大写入限制，maxSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeFile(String filePath, HttpServletRequest httpServletRequest, long maxSize) {
        InputStream stream = null;
        try {
            stream = httpServletRequest.getInputStream();
        } catch (Exception e) {
            stream = null;
            log.error("Error:writeFile,HttpServletRequest获取输入流失败", e);

        }
        if (null == stream) {
            return "写入失败，HttpServletRequest获取输入流失败";
        }

        return writeFile(FileUtils.createFile(filePath), stream, false, maxSize);
    }

    /**
     * 把输入流写入文件
     *
     * @param file               要打开用于写入的文件。
     * @param httpServletRequest 输入流
     * @param maxSize            最大写入限制，maxSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeFile(File file, HttpServletRequest httpServletRequest, long maxSize) {
        InputStream stream = null;
        try {
            stream = httpServletRequest.getInputStream();
        } catch (Exception e) {
            stream = null;
            log.error("Error:writeFile,HttpServletRequest获取输入流失败", e);

        }
        if (null == stream) {
            return "写入失败，HttpServletRequest获取输入流失败";
        }
        return writeFile(file, stream, false, maxSize);
    }


    /**
     * 把输入流写入文件
     *
     * @param filePath 要打开用于写入的文件路径。
     * @param stream   输入流
     * @param maxSize  最大写入限制，maxSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeFile(String filePath, InputStream stream, long maxSize) {
        return writeFile(FileUtils.createFile(filePath), stream, false, maxSize);
    }

    /**
     * 把输入流写入文件
     *
     * @param filePath 要打开用于写入的文件路径。
     * @param stream   输入流
     * @param append   是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @param maxSize  最大写入限制，maxSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeFile(String filePath, InputStream stream, boolean append, long maxSize) {
        return writeFile(FileUtils.createFile(filePath), stream, append, maxSize);
    }


    /**
     * 把输入流写入文件
     *
     * @param file    要打开用于写入的文件。
     * @param stream  输入流
     * @param maxSize 最大写入限制，maxSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeFile(File file, InputStream stream, long maxSize) {
        return writeFile(file, stream, false, maxSize);
    }

    /**
     * 把输入流写入文件
     *
     * @param file    要打开用于写入的文件。
     * @param stream  输入流
     * @param append  是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @param maxSize 最大写入限制，maxSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeFile(File file, InputStream stream, boolean append, long maxSize) {
        if (null == file || file.isDirectory()) {
            return "写入失败，目标文件不存在";
        }
        BufferedOutputStream outStream = null;
        String resultFlag = "写入失败，发生读写异常";
        try {
            long totalSize = 0;
            outStream = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                totalSize = totalSize + length;
                if (maxSize > 0 && totalSize > maxSize) {
                    break;
                }
                outStream.write(data, 0, length);
            }
            outStream.flush();
            if (maxSize > 0 && totalSize > maxSize) {
                resultFlag = "写入失败，超过最大" + NumberStringUtil.formatSize(maxSize) + "限制";
            } else {
                resultFlag = "";
            }
        } catch (Exception e) {
            resultFlag = "写入失败，发生读写异常";
            log.error("Error:writeFile", e);
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (Exception e) {
                    log.error("Error:writeFile", e);
                }
            }
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:writeFile", e);
                }
            }
        }
        return resultFlag;
    }

    /**
     * 把图片文件输入流写入文件
     *
     * @param filePath    要打开用于写入的文件路径。
     * @param stream      输入流
     * @param maxSize     图片宽度和高度最大值，maxSize大于0时候才起左右，小于等于0不限制。
     * @param maxFileSize 目标图片最大值，maxFileSize大于0时候才起左右，小于等于0不限制。
     * @return 返回写入结果信息，若是失败返回失败原因，成功返回空字符串。
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static String writeImageFile(String filePath, InputStream stream, int maxSize, long maxFileSize) {
        String tmpFilePath = null == filePath || filePath.length() <= 0 ? null : filePath + ".tmp";
        String result = writeFile(tmpFilePath, stream, false, 0);
        if (null != result && result.length() > 0) {
            return result;
        }
        boolean imageResult = ImageCompressUtil.compressImageMaxFileSize(tmpFilePath, filePath, maxSize, maxFileSize, 0);
        try {
            File fileTemp = new File(tmpFilePath);
            fileTemp.delete();
        } catch (Exception e) {
            log.error("Error:writeImageFile->临时文件删除失败", e);
        }
        if (!imageResult) {
            return "图片压缩失败，此文件可能不是图片文件，或者此图片无法被压缩到目标大小。";
        } else {

            return "";
        }

    }
}
