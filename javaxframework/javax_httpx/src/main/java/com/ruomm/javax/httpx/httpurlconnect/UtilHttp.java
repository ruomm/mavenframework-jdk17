/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月5日 上午9:26:55
 */
package com.ruomm.javax.httpx.httpurlconnect;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.dal.HttpxMime;
import com.ruomm.javax.httpx.dal.HttpxMultipart;
import com.ruomm.javax.httpx.httpurlconnect.dal.UrlMultiReqBuiler;
import com.ruomm.javax.httpx.httpurlconnect.dal.UrlMultiReqEntry;
import com.ruomm.javax.httpx.httpurlconnect.dal.UrlStringReqEntry;
import com.ruomm.javax.httpx.util.FileBase64;
import com.ruomm.javax.httpx.util.FileBytesForHttp;
import com.ruomm.javax.httpx.util.JavaxHttpxUtils;
import com.ruomm.javax.httpx.util.LoggerHttpUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

class UtilHttp {
    private final static Log log = LogFactory.getLog(UtilHttp.class);

    /**
     * 依据字符串自动生成RequestBody </br>
     * 若是contentType不为空，依据contentType生产 </br>
     * 若是contentType为空，依据postStr类型和charsetName智能判断生成 </br>
     * 标准xml格式是application/xml，非标准格式xml为text/xml,JSON格式是application/json，其他格式为text/plain</br>
     * 若charsetName真实有效，contentType编码为charsetName的编码</br>
     * 若是charsetName不有效，</br>
     * json格式默认为utf-8编码 </br>
     * 标准xml格式取xml文本编码，若是取不到设置utf-8编码 ，取到且有效使用此编码，取到但是无效，不使用编码 </br>
     * 非标准xml格式默认为不使用编码 </br>
     * 其他格式默认为不使用编码
     *
     * @param postStr     请求字符串
     * @param charsetName 编码方式
     * @param contentType Content-Type类型
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag   logger日志输出的标记，默认为类简称
     * @return
     */
    public static UrlStringReqEntry createRequestByString(String postStr, String contentType, String charsetName,
                                                          HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        if (null == postStr) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息错误->请求内容postStr为空");
            return null;
        }
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息postStr->" + JavaxHttpxUtils.parseLogString(postStr));
        HttpxMime httpxMime = HttpConfig.parseRequestMimeNew(postStr, contentType, charsetName);

        if (null == httpxMime || null == httpxMime.getMimeType() || httpxMime.getMimeType().length() <= 0) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->RequestBody无法创建,无法解析contentType和charsetName");
            return null;
        }
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息Content-Type->" + httpxMime.getMimeType());
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息Content-Type With Charset->" + httpxMime.getMimeTypeWithCharset());
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息编码方式->" + httpxMime.getCharset());
        UrlStringReqEntry requestEntry = new UrlStringReqEntry();
        requestEntry.setMimeType(httpxMime.getMimeType());
        requestEntry.setCharset(httpxMime.getCharset());
        requestEntry.setText(postStr);
        return requestEntry;
    }

    public static byte[] readInputStream(InputStream inputStream) {
        byte[] resultBytes = null;
        BufferedInputStream bufIn = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bufIn = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = bufIn.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            resultBytes = baos.toByteArray();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:readInputStream", e);
            resultBytes = null;
        } finally {
            try {
                if (null != bufIn) {
                    bufIn.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                log.error("Error:readInputStream", e2);
            }
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                log.error("Error:readInputStream", e2);
            }
        }
        return resultBytes;
    }

    /**
     * 构建multipart/form-data请求体
     *
     * @param lstHttpxMultipart 信息类
     * @param charsetName       编码方式
     * @param loggerLevel       日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag         logger日志输出的标记，默认为类简称
     * @return UrlMultiReqEntry
     */
    public static UrlMultiReqEntry createMultipartRequest(List<HttpxMultipart> lstHttpxMultipart, String charsetName,
                                                          HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        if (null == lstHttpxMultipart || lstHttpxMultipart.size() <= 0) {
            throw new RuntimeException("multipart/form-data没有传入任何信息，无法构建");
        }
        UrlMultiReqBuiler urlMultiReqBuiler = new UrlMultiReqBuiler(CharsetUtils.parseRealCharsetName(charsetName, "UTF-8"), false);
        for (HttpxMultipart httpxMultipart : lstHttpxMultipart) {
            if (null != httpxMultipart.getFile()) {
                String filePathVal = httpxMultipart.getFile().getPath();
                String name = StringUtils.isEmpty(httpxMultipart.getName()) ? FileUtils.getFileNameWithoutExtension(filePathVal) : httpxMultipart.getName();
                String fileName = StringUtils.isEmpty(httpxMultipart.getFileName()) ? FileUtils.getFileName(filePathVal) : httpxMultipart.getFileName();
                if (StringUtils.isEmpty(name)) {
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数名称不能为空");
                    throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
                }
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + name + ":" + fileName + ":" + filePathVal);
                urlMultiReqBuiler.add(name, fileName, filePathVal);//文件名,请求体里的文件
            } else if (null != httpxMultipart.getFileBytes() && httpxMultipart.getFileBytes().length > 0) {
                FileBytesForHttp fileBytesForHttp = JavaxHttpxUtils.parseFileBytesForHttp(httpxMultipart.getName(), httpxMultipart.getFileName(), httpxMultipart.getFileBytes());
                if (null == fileBytesForHttp) {
                    continue;
                }
                String urlName = null;
                try {
                    urlName = URLEncoder.encode(fileBytesForHttp.getName(), CharsetUtils.parseRealCharsetName(charsetName, "UTF-8"));
                } catch (Exception e) {
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数名称无法URLEncoder");
                    throw new RuntimeException("multipart/form-data添加的参数名称无法URLEncoder");
                }
                String urlFileName = null;
                try {
                    urlFileName = URLEncoder.encode(fileBytesForHttp.getFileName(), CharsetUtils.parseRealCharsetName(charsetName, "UTF-8"));
                } catch (Exception e) {
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数文件名称无法URLEncoder");
                    throw new RuntimeException("multipart/form-data添加的参数名称无法URLEncoder");
                }
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + fileBytesForHttp.getName() + ":" + fileBytesForHttp.getFileName() + ":文件byte[]编码长度" + fileBytesForHttp.getFileBytes().length);
                urlMultiReqBuiler.addBytes(fileBytesForHttp.getName(), fileBytesForHttp.getFileName(), fileBytesForHttp.getFileBytes());
            } else if (null != httpxMultipart.getBase64Content() && httpxMultipart.getBase64Content().length() > 0) {
                List<FileBase64> fileBase64List = JavaxHttpxUtils.parseFileBase64ByContent(httpxMultipart.getName(), httpxMultipart.getFileName(), httpxMultipart.getBase64Content());
                if (null == fileBase64List || fileBase64List.size() <= 0) {
                    continue;
                }
                for (FileBase64 fileBase64 : fileBase64List) {
                    String urlName = null;
                    try {
                        urlName = URLEncoder.encode(fileBase64.getName(), CharsetUtils.parseRealCharsetName(charsetName, "UTF-8"));
                    } catch (Exception e) {
                        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数名称无法URLEncoder");
                        throw new RuntimeException("multipart/form-data添加的参数名称无法URLEncoder");
                    }
                    String urlFileName = null;
                    try {
                        urlFileName = URLEncoder.encode(fileBase64.getFileName(), CharsetUtils.parseRealCharsetName(charsetName, "UTF-8"));
                    } catch (Exception e) {
                        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数文件名称无法URLEncoder");
                        throw new RuntimeException("multipart/form-data添加的参数名称无法URLEncoder");
                    }
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + fileBase64.getName() + ":" + fileBase64.getFileName() + ":文件base64编码长度" + fileBase64.getBase64Content().length());
                    urlMultiReqBuiler.addBase64Content(fileBase64.getName(), fileBase64.getFileName(), fileBase64.getBase64Content());
                }
            } else {
                String name = httpxMultipart.getName();
                String value = StringUtils.isEmpty(httpxMultipart.getValue()) ? "" : httpxMultipart.getValue();
                if (StringUtils.isEmpty(name)) {
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数名称不能为空");
                    throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
                }
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + name + ":" + JavaxHttpxUtils.parseLogString(value));
                urlMultiReqBuiler.add(name, value);
            }
        }
        return urlMultiReqBuiler.build();
    }
}