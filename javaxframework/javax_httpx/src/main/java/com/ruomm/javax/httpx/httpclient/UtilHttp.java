/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月5日 上午9:26:55
 */
package com.ruomm.javax.httpx.httpclient;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.dal.HttpxMime;
import com.ruomm.javax.httpx.dal.HttpxMultipart;
import com.ruomm.javax.httpx.util.FileBase64;
import com.ruomm.javax.httpx.util.FileBytesForHttp;
import com.ruomm.javax.httpx.util.JavaxHttpxUtils;
import com.ruomm.javax.httpx.util.LoggerHttpUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.net.URLEncoder;
import java.nio.charset.Charset;
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
    public static HttpEntity createRequestByString(String postStr, String contentType, String charsetName,
                                                   HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        if (null == postStr) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息错误->请求内容postStr为空");
            return null;
        }
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息postStr->" + JavaxHttpxUtils.parseLogString(postStr));
        HttpxMime httpxMime = HttpConfig.parseRequestMimeNew(postStr, contentType, charsetName);

        if (null == httpxMime || null == httpxMime.getMimeType() || httpxMime.getMimeType().length() <= 0) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->HttpEntity无法创建,无法解析contentType和charsetName");
            return null;
        }
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息Content-Type->" + httpxMime.getMimeType());
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息Content-Type With Charset->" + httpxMime.getMimeTypeWithCharset());
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息编码方式->" + httpxMime.getCharset());
        StringEntity httpStringEntity = null;
        try {
            ContentType mContentType = null;
            if (null == httpxMime.getCharset() || httpxMime.getCharset().length() <= 0) {
                mContentType = ContentType.create(httpxMime.getMimeType());
            } else {
                mContentType = ContentType.create(httpxMime.getMimeType(), httpxMime.getCharset());
            }
            httpStringEntity = new StringEntity(postStr, mContentType);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:", e);
            httpStringEntity = null;
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->HttpEntity无法创建,可能ContentType的contentType类型不支持或charset编码错误");
        }
        return httpStringEntity;
    }

    /**
     * 构建multipart/form-data请求体
     *
     * @param lstHttpxMultipart 信息类
     * @param charsetName       编码方式
     * @param loggerLevel       日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag         logger日志输出的标记，默认为类简称
     * @return HttpEntity
     */
    public static HttpEntity createMultipartRequest(List<HttpxMultipart> lstHttpxMultipart, String charsetName,
                                                    HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        if (null == lstHttpxMultipart || lstHttpxMultipart.size() <= 0) {
            throw new RuntimeException("multipart/form-data没有传入任何信息，无法构建");
        }
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
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
                FileBody fundFileBin = new FileBody(httpxMultipart.getFile(), ContentType.MULTIPART_FORM_DATA, fileName);
                multipartEntity.addPart(name, fundFileBin);
            } else if (null != httpxMultipart.getFileBytes() && httpxMultipart.getFileBytes().length > 0) {
                FileBytesForHttp fileBytesForHttp = JavaxHttpxUtils.parseFileBytesForHttp(httpxMultipart.getName(), httpxMultipart.getFileName(), httpxMultipart.getFileBytes(), HttpConfig.parseUploadPath());
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
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + fileBytesForHttp.getName() + ":" + fileBytesForHttp.getFileName() + ":" + fileBytesForHttp.getFile().getPath());
                FileBody fundFileBin = new FileBody(fileBytesForHttp.getFile(), ContentType.MULTIPART_FORM_DATA, fileBytesForHttp.getFileName());
                multipartEntity.addPart(fileBytesForHttp.getName(), fundFileBin);
                httpxMultipart.addDeleteFiles(fileBytesForHttp.getFile());
            } else if (null != httpxMultipart.getBase64Content() && httpxMultipart.getBase64Content().length() > 0) {
                List<FileBase64> fileBase64List = JavaxHttpxUtils.parseFileBase64ByFile(httpxMultipart.getName(), httpxMultipart.getFileName(), httpxMultipart.getBase64Content(), HttpConfig.parseUploadPath());
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
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + fileBase64.getName() + ":" + fileBase64.getFileName() + ":" + fileBase64.getFile().getPath());
                    FileBody fundFileBin = new FileBody(fileBase64.getFile(), ContentType.MULTIPART_FORM_DATA, fileBase64.getFileName());
                    multipartEntity.addPart(fileBase64.getName(), fundFileBin);
                    httpxMultipart.addDeleteFiles(fileBase64.getFile());
                }
            } else {
                String name = httpxMultipart.getName();
                String value = StringUtils.isEmpty(httpxMultipart.getValue()) ? "" : httpxMultipart.getValue();
                ContentType contentType = ContentType.create("text/plain", CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8")));
                if (StringUtils.isEmpty(name)) {
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求创建错误->multipart/form-data添加的参数名称不能为空");
                    throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
                }
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求信息multipart/form-data->" + name + ":" + JavaxHttpxUtils.parseLogString(value));
                multipartEntity.addPart(name, new StringBody(value, contentType));//传递键值对参数
            }
        }
        return multipartEntity.build();
    }
}