/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月2日 下午5:28:53
 */
package com.ruomm.javax.httpx.okhttp;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.config.FileHttpListener;
import com.ruomm.javax.httpx.config.HttpCookieInterface;
import com.ruomm.javax.httpx.dal.HttpMultipartBuilder;
import com.ruomm.javax.httpx.dal.ResponseFile;
import com.ruomm.javax.httpx.parse.HttpParseUtil;
import com.ruomm.javax.httpx.platform.LooperHelper;
import com.ruomm.javax.httpx.util.LoggerHttpUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileOKHttp {
    private final static Log log = LogFactory.getLog(FileOKHttp.class);
    // 网络请求的路径
    private String Url = null;
    // 是否使用Post模式设置参数进行网络请求
    private boolean isPost = false;
    // 请求和响应的编码方式
//	private String charsetName = null;
    // 请求的Header
    private Map<String, String> reqeustHeaders = null;
    // 响应的Header
    private Map<String, String> responseHeaders = null;
    // 请求Body体，可以自己设置请求的RequestBody;
    private RequestBody mRequestBody = null;
    // 请求Call,可以调用cancleCall()来取消网络请求
    private Call mCall = null;
    // 同步请求响应结果
    private final ResponseFile mResponseFile;
    // 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。默认值：HttpConfig.getLoggerLevel()
    private HttpConfig.LoggerLevel loggerLevel = HttpConfig.getLoggerLevel();
    private String loggerTag = FileOKHttp.class.getSimpleName();
    // 成功的响应码
//		private int sucessCode = 200;
    // Cookie设置
    private HttpCookieInterface cookieInterface;
    // Cookie标识
    private String cookieTag = null;
    // 请求Domain,依据URL自动产生
    private String domain = null;
    // 请求使用的OkHttpClient
    private OkHttpClient okHttpClient = null;
    // 响应回调的FileHttpListener
    private FileHttpListener httpListener = null;
    // 是否异步请求
    private boolean isAjax = true;
    // 异步请求辅助类，主要兼容Android设置
    private LooperHelper looperHelper = null;
    // 是否强制回调到主线程
    private boolean isForceMainLooper = true;
    // 下载是否覆盖原来的文件
    private boolean isOverWrite = true;
    // 下载的文件路径
    private File file = null;
    // 下载的进度
    private long progresstime = 0;
    // 是否执行过回调
    private boolean isCallBack = false;
    // 否捕获回调异常
    private boolean isTryCallBack = true;
    // 建构请求时候是否出错
    private boolean isBodyErr = false;

    /**
     * 使用默认OkHttpClient构造FileOKHttp
     *
     * @return
     */
    public FileOKHttp() {
        this(null);
    }

    /**
     * 使用自定义OkHttpClient构造FileOKHttp
     *
     * @param okHttpClient 自定义的OkHttpClient，若是NULL使用默认OkHttpClient
     */
    public FileOKHttp(OkHttpClient okHttpClient) {
        this.Url = null;
        this.isPost = false;
        this.mResponseFile = new ResponseFile();
        this.mResponseFile.setStatus(HttpConfig.CODE_NO_SEND);
        if (null == okHttpClient) {
            this.okHttpClient = OkHttpConfig.getDefaultHttpClient();
        } else {
            this.okHttpClient = okHttpClient;
        }
        this.isAjax = HttpConfig.IS_ADNROID ? true : false;
    }

    /**
     * @param url 请求路径设置
     * @return
     */

    public FileOKHttp setUrl(String url) {
        this.Url = url;
        this.domain = HttpParseUtil.urlToDomain(url);
        return this;
    }

    /**
     * @param isPost 是Post请求还是Get请求
     * @return
     */
    public FileOKHttp setPost(boolean isPost) {
        this.isPost = isPost;
        return this;
    }

    /**
     * @param isAjax 是异步请求还是同步
     * @return
     */
    public FileOKHttp setAjax(boolean isAjax) {
        this.isAjax = isAjax;
        return this;
    }

    /**
     * 设置请求和相应的编码方式
     *
     * @param charsetName 编码方式
     * @return
     */
//	public FileOKHttp setCharset(String charsetName) {
//		this.charsetName = UtilHttp.parseRealCharsetName(charsetName);
//		return this;
//	}

    /**
     * 设置Cookie接口参数
     *
     * @param cookieInterface Cookie接口
     * @return
     */
    public FileOKHttp setCookieInterface(HttpCookieInterface cookieInterface) {
        this.cookieInterface = cookieInterface;
        return this;
    }

    /**
     * 设置Cookie标识，设置后cookieInterface使用此标识设置Cookie
     *
     * @param cookieTag Cookie标识
     * @return
     */
    public FileOKHttp setCookieTag(String cookieTag) {
        this.cookieTag = cookieTag;
        return this;
    }

    /**
     * 设置是下载时候是否覆盖已存在的文件，若是true则原文件存在则提示文件已存在，不进行下载
     *
     * @param isOverWrite 是否覆盖已存在的文件
     * @return
     */
    public FileOKHttp setOverWrite(boolean isOverWrite) {
        this.isOverWrite = isOverWrite;
        return this;
    }

    /**
     * 设置是否强制回调到主线程，true时候不管任何时候都回掉到Main UI Thread
     *
     * @param isForceMainLooper 否强制回调到主线程
     * @return
     */
    public FileOKHttp setForceMainLooper(boolean isForceMainLooper) {
        this.isForceMainLooper = isForceMainLooper;
        return this;
    }

    /**
     * 设置请求的Header信息
     *
     * @param reqeustHeaders 请求的Header信息
     * @return
     */
    public FileOKHttp setReqeustHeaders(Map<String, String> reqeustHeaders) {
        this.reqeustHeaders = reqeustHeaders;
        return this;
    }

    /**
     * 设置是否输出INFO日志
     *
     * @paramDefault loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag logger日志输出的标记，默认为类简称
     * @return
     */
    public FileOKHttp setLogger(String loggerTag) {
        return setLogger(null, loggerTag);
    }

    /**
     * 设置是否输出INFO日志
     *
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @paramDefault loggerTag logger日志输出的标记，默认为类简称
     * @return
     */
    public FileOKHttp setLogger(HttpConfig.LoggerLevel loggerLevel) {
        return setLogger(loggerLevel, null);
    }

    /**
     * 设置是否输出INFO日志
     *
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag logger日志输出的标记，默认为类简称
     * @return
     */
    public FileOKHttp setLogger(HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        if (null == loggerLevel) {
            if (null == this.loggerLevel) {
                this.loggerLevel = HttpConfig.getLoggerLevel();
            }
        } else {
            this.loggerLevel = loggerLevel;
        }
        if (null == loggerTag) {
            if (StringUtils.isEmpty(this.loggerTag)) {
                this.loggerTag = this.getClass().getSimpleName();
            }
        } else {
            this.loggerTag = StringUtils.isEmpty(loggerTag) ? this.getClass().getSimpleName() : loggerTag;
        }
        return this;
    }

    /**
     * 设置是否捕获回调异常
     *
     * @param isTryCallBack 是否捕获回调异常
     * @return
     */
    public FileOKHttp setTryCallBack(boolean isTryCallBack) {
        this.isTryCallBack = isTryCallBack;
        return this;
    }

    /**
     * 取消Http请求
     */
    public void cancleCall() {
        if (null != mCall) {
            mCall.cancel();
        }
    }

    /**
     * 设置请求RequestBody
     *
     * @param mRequestBody 请求RequestBody
     * @return
     */
    public FileOKHttp setRequestBody(RequestBody mRequestBody) {
        this.mRequestBody = mRequestBody;
        return this;
    }

    /**
     * 依据请求字符串postStr，设置请求RequestBody
     *
     * @param postStr 请求字符串
     * @return
     */
    public FileOKHttp setRequestBodyByCharset(String postStr) {
        if (null == postStr) {
            return this;
        }
        this.mRequestBody = UtilHttp.createRequestByString(postStr, null, null, loggerLevel, loggerTag);
        if (null == this.mRequestBody) {
            isBodyErr = true;
        }
        return this;
    }

    /**
     * 依据请求字符串postStr和编码方式，设置请求RequestBody
     *
     * @param postStr         请求字符串
     * @param bodyCharsetName 编码方式
     * @return
     */
    public FileOKHttp setRequestBodyByCharset(String postStr, String bodyCharsetName) {
        if (null == postStr) {
            return this;
        }
        this.mRequestBody = UtilHttp.createRequestByString(postStr, null, bodyCharsetName, loggerLevel, loggerTag);
        if (null == this.mRequestBody) {
            isBodyErr = true;
        }
        return this;
    }

    /**
     * 依据请求字符串postStr和Content-Type，设置请求RequestBody
     *
     * @param postStr     请求字符串
     * @param contentType Content-Type
     * @return
     */
    public FileOKHttp setRequestBodyByContentType(String postStr, String contentType) {
        if (null == postStr) {
            return this;
        }
        this.mRequestBody = UtilHttp.createRequestByString(postStr, contentType, null, loggerLevel, loggerTag);
        if (null == this.mRequestBody) {
            isBodyErr = true;
        }
        return this;
    }

    /**
     * 依据HttpMultipartBuilder，设置multipart/form-data的请求体
     *
     * @param httpMultipartBuilder multipart/form-data的构建参数
     * @return
     */
    public FileOKHttp setRequestBody(HttpMultipartBuilder httpMultipartBuilder) {
        if (null == httpMultipartBuilder) {
            return this;
        }
        try {
            this.mRequestBody = UtilHttp.createMultipartRequest(httpMultipartBuilder.builder(), null, loggerLevel, loggerTag);
        } catch (Exception e) {
            log.error("Error:setRequestBody-UtilHttp.createMultipartRequest", e);
            this.mRequestBody = null;
        }

        if (null == this.mRequestBody) {
            isBodyErr = true;
        }
        return this;
    }

    /**
     * 构建http请求Request
     *
     * @return
     */
    private Request buildRequestByRequestBody() {
        try {
            if (isPost) {

                if (null != mRequestBody) {
                    Builder builder = new Builder().url(this.Url);
                    addHeadersForRequestBuilder(builder);
                    return builder.post(this.mRequestBody).build();
                } else {
                    Builder builder = new Builder().url(this.Url);
                    addHeadersForRequestBuilder(builder);
                    return builder.build();
                }

            } else {
                if (null != mRequestBody) {
                    Builder builder = new Builder().url(this.Url);
                    addHeadersForRequestBuilder(builder);
                    return builder.put(this.mRequestBody).get().build();

                } else {
                    Builder builder = new Builder().url(this.Url);
                    addHeadersForRequestBuilder(builder);
                    return builder.get().build();
                }
            }
        } catch (Exception e) {
            log.error("Error:buildRequestByRequestBody", e);
            return null;
        }
    }

    /**
     * 构建请求时候添加Cookie信息和header头信息
     *
     * @param builder
     */
    private void addHeadersForRequestBuilder(Builder builder) {
        if (null != cookieInterface) {
            String cookieContent = cookieInterface.readCookie(domain,
                    StringUtils.isEmpty(cookieTag) ? cookieInterface.getCookieTag() : cookieTag);
            if (null != cookieContent && cookieContent.length() > 0) {
                builder.addHeader("cookie", cookieContent);
            }
        }
        if (null != reqeustHeaders) {
            for (String key : reqeustHeaders.keySet()) {
                builder.addHeader(key, reqeustHeaders.get(key));
            }
        }
    }

    /**
     * 解析下载结果为回调所需的数据
     *
     * @param response
     */
    private void parseResponse(Response response) {
        if (null == response) {
            createResultData(file, HttpConfig.CODE_FAIL);
            return;
        }
        int responseCode = response.code();
        if (!HttpConfig.isSuccessful(responseCode)) {
            String responseData = null;
            try {
                responseData = response.body().string();
            } catch (Exception e) {
                log.error("Error:parseResponse", e);
                responseData = null;
            }
            if (null != responseData) {
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求响应错误", responseData);
            }
            try {
                if (null != file && file.exists()) {
                    file.delete();
                }
            } catch (Exception filee) {
                log.error("Error:parseResponse", filee);
            }
            createResultData(file, responseCode);
            return;
        } else {
            Headers headers = response.headers();
            if (null != cookieInterface) {
                List<String> cookies = headers.values("Set-Cookie");
                if (null != cookies && cookies.size() > 0) {
                    String sessionData = cookies.get(0);
                    if (null != sessionData & sessionData.length() > 0) {
                        cookieInterface.writeCookie(domain,
                                StringUtils.isEmpty(cookieTag) ? cookieInterface.getCookieTag() : cookieTag,
                                sessionData);
                    }
                }
            }
            Set<String> headerKeys = headers.names();
            responseHeaders = new HashMap<>();
            for (String tmpKey : headerKeys) {
                String tmpVal = headers.get(tmpKey);
                if (null != tmpVal && tmpVal.length() > 0) {
                    responseHeaders.put(tmpKey, tmpVal);
                }
            }
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应Headers", responseHeaders.toString());
            String contentType = responseHeaders.get("Content-Type");
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应Content-Type", contentType);
            boolean flag = false;
            try {
                long totalSize = response.body().contentLength();
                flag = writeFile(file, response.body().byteStream(), false, totalSize);
                if (flag) {

                } else {
                    try {
                        if (null != file && file.exists()) {
                            file.delete();
                        }
                    } catch (Exception filee) {
                        log.error("Error:parseResponse", filee);

                    }
                }
            } catch (Exception e) {

                try {
                    if (null != file && file.exists()) {
                        file.delete();
                    }
                } catch (Exception filee) {
                    log.error("Error:parseResponse", filee);
                }
                flag = false;

            }
            if (flag) {
                createResultData(file, responseCode);
            } else {
                createResultData(file, HttpConfig.CODE_FAIL);
            }
            // 设置编码

        }
    }

    /**
     * 执行文件写入操作并在期间回调下载进度到主FileOkHttp的主线程
     *
     * @param fileResult 写入的文件
     * @param stream     请求的流
     * @param append     是否拼接。true时候下载内容拼接到原文件
     * @param totalSize  总大小
     * @return
     */
    private boolean writeFile(File fileResult, InputStream stream, boolean append, long totalSize) {
        OutputStream o = null;
        boolean flag = false;
        long bytesWritten = 0;
        try {
            FileUtils.makeDirs(fileResult.getAbsolutePath());
            o = new FileOutputStream(fileResult, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
                bytesWritten = bytesWritten + length;
                doHttpCallBackProgress(bytesWritten, totalSize);
            }
            o.flush();
            flag = true;
        } catch (Exception e) {
            log.error("Error:writeFile", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (Exception e) {
                    log.error("Error:writeFile", e);
                }
            }
        }
        return flag;
    }

    /**
     * 回调下载进度
     *
     * @param bytesWritten 下载大小
     * @param totalSize    总大小
     */
    public void doHttpCallBackProgress(final long bytesWritten, final long totalSize) {
        if (Math.abs(System.currentTimeMillis() - progresstime) < 500) {
            return;
        } else {
            progresstime = System.currentTimeMillis();
        }
        if (totalSize > 0) {
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(2);
            String perString = nt.format(bytesWritten * 1.0 / totalSize);
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "文件下载进度信息", bytesWritten + ":" + totalSize + "-" + perString);

        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "文件下载进度信息", bytesWritten + ":" + totalSize);
        }
        if (null == httpListener) {
            if (isAjax) {
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "文件下载进度回调", "httpListener没有实现，无法回调");
            }
            return;
        }
        final double valueProgress;
        if (totalSize > 0) {
            valueProgress = bytesWritten * 1.0 / totalSize;
        } else {
            valueProgress = 1;
        }
        if (null == looperHelper) {
            if (isTryCallBack) {
                try {
                    httpListener.httpCallBackProgress(bytesWritten, totalSize, valueProgress);
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:doHttpCallBackProgress", e);
                }
            } else {
                httpListener.httpCallBackProgress(bytesWritten, totalSize, valueProgress);
            }

        } else {
            if (isTryCallBack) {
                try {
                    looperHelper.doHttpCallBackProgress(httpListener, bytesWritten, totalSize, valueProgress);
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:doHttpCallBackProgress", e);
                }
            } else {
                looperHelper.doHttpCallBackProgress(httpListener, bytesWritten, totalSize, valueProgress);
            }
        }
    }

    /**
     * 回调网络请求结果
     *
     * @param fileResult 下载的文件
     * @param status     请求结果的状态
     */

    private void createResultData(File fileResult, int status) {
        if (isCallBack) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果回调错误", status, "重复回调响应结果");
            return;
        }
        isCallBack = true;
        if (HttpConfig.isSuccessful(status)) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果", status, "下载文件成功");

        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果", status, "下载文件失败");
        }
        mCall = null;
        mResponseFile.setFile(fileResult);
        mResponseFile.setStatus(status);
        mResponseFile.setHeaders(responseHeaders);
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果", mResponseFile.toString());
        if (null == httpListener) {
            if (isAjax) {
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果回调", "httpListener没有实现，无法回调");
            }
            // 退出并置空LooperHelper
            quitLooper();
            return;
        }
        if (null == looperHelper) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果回调", "主线程回调开始");
            if (isTryCallBack) {
                try {
                    httpListener.httpCallBackFile(mResponseFile.getFile(), mResponseFile.getHeaders(),
                            mResponseFile.getStatus());
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:createResultData", e);
                }
            } else {
                httpListener.httpCallBackFile(mResponseFile.getFile(), mResponseFile.getHeaders(),
                        mResponseFile.getStatus());
            }

        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果回调", "子线程转入主线程回调开始");
            if (isTryCallBack) {
                try {
                    looperHelper.doHttpCallBackFile(httpListener, mResponseFile.getFile(), mResponseFile.getHeaders(),
                            mResponseFile.getStatus());
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:createResultData", e);
                }
            } else {
                looperHelper.doHttpCallBackFile(httpListener, mResponseFile.getFile(), mResponseFile.getHeaders(),
                        mResponseFile.getStatus());
            }
        }
        // 退出并置空LooperHelper
        quitLooper();
    }

    /**
     * @param fileDown 判定目标下载文件是否合法
     * @return
     */
    private boolean isTrueFileRequest(File fileDown) {
        if (null == fileDown) {
            return false;
        }
        if (this.isOverWrite) {
            if (fileDown.isDirectory()) {
                return false;
            } else if (fileDown.exists()) {
                try {
                    return fileDown.delete();
                } catch (Exception e) {
                    return false;
                }

            } else {
                return true;
            }
        } else {
            if (fileDown.isDirectory()) {
                return false;
            } else if (fileDown.exists()) {
                return false;

            } else {
                return true;
            }
        }
    }

    public ResponseFile doHttpSync(File file) {
        setAjax(false);
        return doHttp(file, null);
    }

    /**
     * 执行网络请求
     *
     * @param file         网络目标下载文件
     * @param httpListener 网络请求回调监听
     * @return 同步请求返回请求结果，异步请求返回NULL
     */
    public ResponseFile doHttp(File file, FileHttpListener httpListener) {
        this.file = file;
        this.httpListener = httpListener;

        if (isAjax) {
//			new Thread() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					super.run();
//					doHttpCommon();
//				}
//			}.start();
            doHttpCommon();
            return null;
        } else {
            doHttpCommon();
            return this.mResponseFile;
        }
    }

    /**
     * 执行网络请求
     *
     */
    public void doHttpCommon() {
        if (isAjax) {
            looperHelper = LooperHelper.getInstance(isForceMainLooper);
            looperHelper.initLooper();
        }
        if (null == this.Url || this.Url.length() == 0) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求路径错误", "网络请求URL路径为空");
            createResultData(file, HttpConfig.CODE_NO_SEND);
            return;
        }
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求路径", this.Url);
        if (null == file) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求参数错误", "下载的目标文件不存在");
            createResultData(file, HttpConfig.CODE_FILE_ERR);
            return;
        }
        if (!isTrueFileRequest(file)) {
            if (isOverWrite) {
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求参数错误", "目标文件已经存在并且无法删除");
            } else {
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求参数错误", "目标文件已经存在不用重新下载");
            }
            createResultData(file, HttpConfig.CODE_FILE_EXIST);
            return;
        }
        if (null == this.mRequestBody && isBodyErr) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求参数错误", "无法创建RequestBody,可能MediaType的contentType类型不支持");
            createResultData(file, HttpConfig.CODE_NO_SEND);
            return;
        }
        if (null != mRequestBody && null != mRequestBody.contentType()) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求信息Content-Type", mRequestBody.contentType().toString());
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求信息编码方式", null == mRequestBody.contentType().charset() ? "null"
                    : mRequestBody.contentType().charset().name());
        }
        Request mRequest = buildRequestByRequestBody();
        if (null == mRequest) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求参数错误", "无法构建Request");
            createResultData(file, HttpConfig.CODE_NO_SEND);
            return;
        }
        try {
            mCall = okHttpClient.newCall(mRequest);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:doHttpCommon", e);
            mCall = null;
        }
        if (null == mCall) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求参数错误", "无法构建请求Call");
            createResultData(file, HttpConfig.CODE_NO_SEND);
            return;
        }
        mResponseFile.setStatus(HttpConfig.CODE_SEND_ERR);
        if (isAjax) {
            mCall.enqueue(getCallback());
//			try {
//				lopperHelper = new LopperHelper();
//				lopperHelper.initLopper();
//				mCall.enqueue(getCallback());
//			}
//			catch (Exception e) {
//				// TODO: handle exception
//				log.error("Error:doHttpCommon",e);
//				LoggerUtils.loggerMessage(log,this.loggerLevel,this.loggerTag,"响应结果:" + "status = " + HttpConfig.Code_SendError + "; msg = 失败");
//				createResultData(null, null, HttpConfig.Code_SendError);
//			}
        } else {
            Response response = null;
            try {
                response = mCall.execute();
                parseResponse(response);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("Error:doHttpCommon", e);
                if (!isCallBack) {
                    createResultData(file, HttpConfig.CODE_SEND_ERR);
                }
            } finally {
                mCall = null;
                try {
                    if (null != response) {
                        response.close();
                    }
                } catch (Exception e2) {
                    // TODO: handle exception
                    log.error("Error:doHttpCommon", e2);
                }
            }
            return;
        }
    }

    /**
     * 获取回调函数
     *
     * @return
     */
    private Callback getCallback() {
        return new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    parseResponse(response);
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:getCallback", e);
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "请求错误", "发生未知异常");
                    if (!isCallBack) {
                        createResultData(file, HttpConfig.CODE_SEND_ERR);
                    }
                } finally {
                    mCall = null;
                    try {
                        if (null != response) {
                            response.close();
                        }
                    } catch (Exception e2) {
                        // TODO: handle exception
                        log.error("Error:getCallback", e2);
                    }
                    // 退出并置空LooperHelper
                    quitLooper();
                }

            }

            @Override
            public void onFailure(Call call, IOException arg1) {
                try {
                    createResultData(file, HttpConfig.CODE_SEND_ERR);
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:getCallback", e);
                } finally {
                    mCall = null;
                    // 退出并置空LooperHelper
                    quitLooper();
                }
            }
        };
    }

    // 退出并置空LooperHelper
    private void quitLooper() {
        if (null != looperHelper) {
            looperHelper.quitLooper();
            looperHelper = null;
        }
    }

}