/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月2日 下午5:28:53
 */
package com.ruomm.javax.httpx.okhttp;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.config.HttpCacheInterface;
import com.ruomm.javax.httpx.config.HttpCookieInterface;
import com.ruomm.javax.httpx.config.ResponseParseText;
import com.ruomm.javax.httpx.config.TextHttpListener;
import com.ruomm.javax.httpx.dal.HttpCacheReader;
import com.ruomm.javax.httpx.dal.HttpMultipartBuilder;
import com.ruomm.javax.httpx.dal.ResponseText;
import com.ruomm.javax.httpx.dal.ResponseTextCache;
import com.ruomm.javax.httpx.parse.HttpParseUtil;
import com.ruomm.javax.httpx.platform.LooperHelper;
import com.ruomm.javax.httpx.util.LoggerHttpUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TextOKHttp {
    private final static Log log = LogFactory.getLog(TextOKHttp.class);
    // 网络请求的路径
    private String Url = null;
    // 是否使用Post模式设置参数进行网络请求
    private boolean isPost = true;
    // 请求和响应的编码方式
    private String charsetName = null;
    // 请求的Header
    private Map<String, String> reqeustHeaders = null;
    // 响应的Header
    private Map<String, String> responseHeaders = null;
    // 请求Body体，可以自己设置请求的RequestBody;
    private RequestBody mRequestBody = null;
    // 请求Call,可以调用cancleCall()来取消网络请求
    private Call mCall = null;
    // 同步请求响应结果
    private ResponseText mResponseText;
    // 网络请求的目标解析对象的类型
    private Class<?> cls = null;
    // 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。默认值：HttpConfig.getLoggerLevel()
    private HttpConfig.LoggerLevel loggerLevel = HttpConfig.getLoggerLevel();
    private String loggerTag = TextOKHttp.class.getSimpleName();
    // 成功的响应码
//	private int sucessCode = 200;
    // Cookie设置
    private HttpCookieInterface cookieInterface = null;
    // Cookie标识
    private String cookieTag = null;
    // 响应结果解析接口
    private ResponseParseText responseParse = null;
    // 请求缓存类
    private HttpCacheInterface httpCacheInterface = null;
    // 缓存标识
    private String cacheTag = null;
    // 请求Domain,依据URL自动产生
    private String domain = null;
    // 请求使用的OkHttpClient
    private OkHttpClient okHttpClient = null;
    // 响应结果解析类型，目前取值为xml或json
    private String parseFormat = null;
    // 响应回调的DataHttpListener
    private TextHttpListener httpListener = null;
    // 是否异步请求
    private boolean isAjax = true;
    // 异步请求辅助类，主要兼容Android设置
    private LooperHelper looperHelper = null;
    // 是否强制回调到主线程
    private boolean isForceMainLooper = true;
    // 是否执行过回调
    private boolean isCallBack = false;
    // 否捕获回调异常
    private boolean isTryCallBack = true;
    // 响应信息编码字符集
//	private String respCharsetName = null;
    // 建构请求时候是否出错
    private boolean isBodyErr = false;

    /**
     * 使用默认OkHttpClient构造TextOKHttp
     *
     * @return
     */
    public TextOKHttp() {
        this(null);
    }

    /**
     * 使用自定义OkHttpClient构造TextOKHttp
     *
     * @param okHttpClient 自定义的OkHttpClient，若是NULL使用默认OkHttpClient
     */
    public TextOKHttp(OkHttpClient okHttpClient) {
        super();
        this.Url = null;
        this.isPost = true;
        this.mResponseText = new ResponseText();
        this.mResponseText.setStatus(HttpConfig.CODE_NO_SEND);
        this.responseParse = HttpParseUtil.getCommonResponseParseText();
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

    public TextOKHttp setUrl(String url) {
        this.Url = url;
        this.domain = HttpParseUtil.urlToDomain(url);
        return this;
    }

    /**
     * 设置解析接口
     *
     * @param responseParse 解析接口
     * @return
     */
    public TextOKHttp setResponseParse(ResponseParseText responseParse) {
        this.responseParse = responseParse;
        return this;
    }

    /**
     * @param isPost 是Post请求还是Get请求
     * @return
     */
    public TextOKHttp setPost(boolean isPost) {
        this.isPost = isPost;
        return this;
    }

    /**
     * @param isAjax 是异步请求还是同步
     * @return
     */
    public TextOKHttp setAjax(boolean isAjax) {
        this.isAjax = isAjax;
        return this;
    }

    /**
     * 设置响应信息的编码方式
     *
     * @param charsetName 编码方式
     * @return
     */
    public TextOKHttp setCharset(String charsetName) {
        this.charsetName = CharsetUtils.parseRealCharsetName(charsetName);
        return this;
    }

    /**
     * 设置解析的类型，目前支持xml和json
     */
    public TextOKHttp setParseFormat(String parseFormat) {
        this.parseFormat = parseFormat;
        return this;
    }

    /**
     * 设置Cookie接口参数
     *
     * @param cookieInterface Cookie接口
     * @return
     */
    public TextOKHttp setCookieInterface(HttpCookieInterface cookieInterface) {
        this.cookieInterface = cookieInterface;
        return this;
    }

    /**
     * 设置Cookie标识，设置后cookieInterface使用此标识设置Cookie
     *
     * @param cookieTag Cookie标识
     * @return
     */
    public TextOKHttp setCookieTag(String cookieTag) {
        this.cookieTag = cookieTag;
        return this;
    }

    /**
     * 设置请求缓存接口
     *
     * @param httpCacheInterface 请求缓存接口
     * @return
     */
    public TextOKHttp setHttpCacheInterface(HttpCacheInterface httpCacheInterface) {
        this.httpCacheInterface = httpCacheInterface;
        return this;
    }

    /**
     * 设置缓存标识，设置后httpCacheInterface使用此标识设置读取写入缓存
     *
     * @param cacheTag 缓存标识
     * @return
     */
    public TextOKHttp setCacheTag(String cacheTag) {
        this.cacheTag = cacheTag;
        return this;
    }

    /**
     * 设置是否强制回调到主线程，true时候不管任何时候都回掉到Main UI Thread
     *
     * @param isForceMainLooper 否强制回调到主线程
     * @return
     */
    public TextOKHttp setForceMainLooper(boolean isForceMainLooper) {
        this.isForceMainLooper = isForceMainLooper;
        return this;
    }

    /**
     * 设置请求的Header信息
     *
     * @param reqeustHeaders 请求的Header信息
     * @return
     */
    public TextOKHttp setReqeustHeaders(Map<String, String> reqeustHeaders) {
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
    public TextOKHttp setLogger(String loggerTag) {
        return setLogger(null, loggerTag);
    }

    /**
     * 设置是否输出INFO日志
     *
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @paramDefault loggerTag logger日志输出的标记，默认为类简称
     * @return
     */
    public TextOKHttp setLogger(HttpConfig.LoggerLevel loggerLevel) {
        return setLogger(loggerLevel, null);
    }

    /**
     * 设置是否输出INFO日志
     *
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     * @param loggerTag logger日志输出的标记，默认为类简称
     * @return
     */
    public TextOKHttp setLogger(HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
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
    public TextOKHttp setTryCallBack(boolean isTryCallBack) {
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
    public TextOKHttp setRequestBody(RequestBody mRequestBody) {
        this.mRequestBody = mRequestBody;
        return this;
    }

    /**
     * 依据请求字符串postStr，设置请求RequestBody
     *
     * @param postStr 请求字符串
     * @return
     */
    public TextOKHttp setRequestBody(String postStr) {
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
    public TextOKHttp setRequestBodyByCharset(String postStr, String bodyCharsetName) {
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
    public TextOKHttp setRequestBodyByContentType(String postStr, String contentType) {
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
    public TextOKHttp setRequestBody(HttpMultipartBuilder httpMultipartBuilder) {
        if (null == httpMultipartBuilder) {
            return this;
        }
        try {
            this.mRequestBody = UtilHttp.createMultipartRequest(httpMultipartBuilder.builder(), charsetName, loggerLevel, loggerTag);
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
            createResultData(null, null, HttpConfig.CODE_FAIL);
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
            createResultData(null, responseData, responseCode);
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
            // 设置编码
            if (StringUtils.isEmpty(charsetName)) {
//				String contentType = responseHeaders.get("Content-Type");
                String charsetTmp = HttpParseUtil.parseContentTypeCharset(contentType);
                charsetName = CharsetUtils.parseRealCharsetName(charsetTmp);
            }
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应信息编码字符集", charsetName);
            String respFormat = null;
            if (StringUtils.isEmpty(parseFormat)) {
//				String contentType = responseHeaders.get("Content-Type");
                String formatTmp = HttpParseUtil.parseContentTypeFormat(contentType);
                if (!StringUtils.isEmpty(formatTmp)) {
                    respFormat = formatTmp;
                }
            } else {
                respFormat = parseFormat;
            }
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应信息数据格式", respFormat);
            String responseData = null;
            try {
                responseData = response.body().string();
            } catch (Exception e) {
                log.error("Error:parseResponse", e);
                responseData = null;
            }
            // 执行过滤接口，过滤不必要的请求
            boolean tmpFlag = false;
            if (isTryCallBack) {
                try {
                    tmpFlag = null != httpListener
                            && httpListener.httpCallBackFilter(responseData, responseHeaders, responseCode);
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:parseResponse", e);
                    tmpFlag = false;
                }
            } else {
                tmpFlag = null != httpListener
                        && httpListener.httpCallBackFilter(responseData, responseHeaders, responseCode);
            }
            if (tmpFlag) {
                createResultDataFilter(null, responseData, HttpConfig.CODE_FILTER);
                return;
            }
            if (null == responseData || responseData.length() <= 0) {
                createResultData(null, null, responseCode);
            } else {

                // 设置缓存
                if (null != httpCacheInterface) {
                    try {
                        httpCacheInterface.writeCache(domain,
                                StringUtils.isEmpty(cacheTag) ? httpCacheInterface.getCacheTag() : cacheTag, null,
                                responseData);
                    } catch (Exception e) {
                        // TODO: handle exception
                        log.error("Error:parseResponse", e);
                    }

                }
                // 解析请求结果
                parseHttpCallBackSucess(responseData, responseCode, charsetName, respFormat);
            }

        }
    }

    /**
     * 解析网络请求结果为符合回调用的数据
     *
     * @param responseData
     */
    private void parseHttpCallBackSucess(final String responseData, int reponseCode, String respCharsetName,
                                         String respFormat) {

        // 对象解析开始
        Object object = null;
        if (null == responseParse) {
            object = HttpParseUtil.parseResponseText(responseData, cls, respCharsetName, respFormat, loggerLevel, loggerTag);
        } else {
            object = responseParse.parseResponseText(responseData, cls, respCharsetName, respFormat);
        }

        // 回调开始
        if (null == object) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "解析结果", "解析失败，请求成功了但无法解析为目标POJO类");
            createResultData(null, responseData, reponseCode);
        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "解析结果", "解析成功，请求成功了并解析为目标POJO类");
            createResultData(object, responseData, reponseCode);

        }
        // 回调结束

    }

    /**
     * 回调网络请求拦截结果
     *
     * @param resultObject 请求结果解析成的Object对象
     * @param resultData   请求结果的原始数据
     * @param status       请求结果的状态
     */
    private void createResultDataFilter(Object resultObject, String resultData, int status) {
        if (HttpConfig.CODE_FILTER != status) {

            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应拦截结果", status, "拦截失败");
            return;
        }
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应拦截结果", status, "拦截成功");
        boolean tmpFlag = false;
        if (isTryCallBack) {
            try {
                tmpFlag = null != httpListener && httpListener.httpCallBackAfterFilterTrue();
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:createResultDataFilter", e);
                tmpFlag = false;
            }
        } else {
            tmpFlag = null != httpListener && httpListener.httpCallBackAfterFilterTrue();
        }
        if (tmpFlag) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应拦截执行", "转入响应回调进行执行");
            createResultData(resultObject, resultData, status);
        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应拦截执行", "不转入响应回调进行执行");
            // 退出并置空LooperHelper
            quitLooper();
        }
    }

    /**
     * 回调网络请求结果
     *
     * @param resultObject 请求结果解析成的Object对象
     * @param resultData   请求结果的原始数据
     * @param status       请求结果的状态
     */
    private void createResultData(Object resultObject, String resultData, int status) {
        if (isCallBack) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果回调错误", status + "重复回调响应结果");
            return;
        }
        isCallBack = true;
        if (HttpConfig.isSuccessful(status)) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果", status, "成功");
        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果", status, "失败");
        }
        mCall = null;
        mResponseText.setResultObject(resultObject);
        mResponseText.setResultData(resultData);
        mResponseText.setStatus(status);
        mResponseText.setHeaders(responseHeaders);
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果", mResponseText.getPrintString());
//		mResponseData.setCache(false);
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
                    httpListener.httpCallBack(mResponseText.getResultObject(), mResponseText.getResultData(),
                            mResponseText.getHeaders(), mResponseText.getStatus());
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:createResultData", e);
                }
            } else {
                httpListener.httpCallBack(mResponseText.getResultObject(), mResponseText.getResultData(),
                        mResponseText.getHeaders(), mResponseText.getStatus());
            }
        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "响应结果回调", "子线程转入主线程回调开始");
            if (isTryCallBack) {
                try {
                    looperHelper.doHttpCallBack(httpListener, mResponseText.getResultObject(),
                            mResponseText.getResultData(), mResponseText.getHeaders(), mResponseText.getStatus());
                } catch (Exception e) {
                    log.error("Error:createResultData", e);
                }

            } else {
                looperHelper.doHttpCallBack(httpListener, mResponseText.getResultObject(),
                        mResponseText.getResultData(), mResponseText.getHeaders(), mResponseText.getStatus());
            }
        }
        // 退出并置空LooperHelper
        quitLooper();
    }

    /**
     * 回调缓存请求结果
     *
     * @param cacheReader 缓存请求结果
     */
    private void createResultDataCache(HttpCacheReader cacheReader) {
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "缓存响应结果", HttpConfig.CODE_CACHE_OK, "成功");
//		mResponseData.setResultObject(cacheReader.getCacheObj());
//		mResponseData.setResultData(cacheReader.getCacheData());
//		mResponseData.setStatus(HttpConfig.CODE_CACHE_OK);
//		mResponseData.setHeaders(null);
//		mResponseData.setCache(true);
        mResponseText.setCacheResult(new ResponseTextCache());
        mResponseText.getCacheResult().setResultObject(cacheReader.getCacheObj());
        mResponseText.getCacheResult().setResultData(cacheReader.getCacheStr());
        mResponseText.getCacheResult().setStatus(HttpConfig.CODE_CACHE_OK);
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "缓存响应结果", mResponseText.getCacheResult().getPrintString());
        if (!cacheReader.isCacheExecuteToHttpCallBack()) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "缓存响应结果回调", "不使用httpListener执行回调");
            return;
        }
        if (null == httpListener) {
            if (isAjax) {
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "缓存响应结果回调", "httpListener没有实现，无法回调");
            }
            return;
        }

        if (null == looperHelper) {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "缓存响应结果回调", "主线程回调开始");
            if (isTryCallBack) {
                try {
                    httpListener.httpCallBack(mResponseText.getCacheResult().getResultObject(),
                            mResponseText.getCacheResult().getResultData(), null,
                            mResponseText.getCacheResult().getStatus());
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:createResultDataCache", e);
                }
            } else {
                httpListener.httpCallBack(mResponseText.getCacheResult().getResultObject(),
                        mResponseText.getCacheResult().getResultData(), null,
                        mResponseText.getCacheResult().getStatus());
            }
        } else {
            LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "缓存响应结果回调", "子线程转入主线程回调开始");
            if (isTryCallBack) {
                try {
                    looperHelper.doHttpCallBack(httpListener, mResponseText.getCacheResult().getResultObject(),
                            mResponseText.getCacheResult().getResultData(), null,
                            mResponseText.getCacheResult().getStatus());
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:createResultDataCache", e);
                }
            } else {
                looperHelper.doHttpCallBack(httpListener, mResponseText.getCacheResult().getResultObject(),
                        mResponseText.getCacheResult().getResultData(), null,
                        mResponseText.getCacheResult().getStatus());
            }
        }

    }

    public ResponseText doHttpSync(Class<?> cls) {
        setAjax(false);
        return doHttp(cls, null);
    }

    /**
     * 执行网络请求
     *
     * @param cls          目标解析类
     * @param httpListener 网络请求回调监听
     * @return 同步请求返回请求结果，异步请求返回NULL
     */
    public ResponseText doHttp(Class<?> cls, TextHttpListener httpListener) {
        this.cls = cls;
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
            return this.mResponseText;
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
            createResultData(null, null, HttpConfig.CODE_NO_SEND);
            return;
        }
        LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求路径", this.Url);
        boolean isCacheRead = isCacheRead(cls);
        if (isCacheRead) {
            // 退出并置空LooperHelper
            quitLooper();
            return;
        }
        if (null == this.mRequestBody && isBodyErr) {
            createResultData(null, null, HttpConfig.CODE_NO_SEND);
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
            createResultData(null, null, HttpConfig.CODE_NO_SEND);
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
            createResultData(null, null, HttpConfig.CODE_NO_SEND);
            return;
        }
        mResponseText.setStatus(HttpConfig.CODE_SEND_ERR);
        if (isAjax) {
            mCall.enqueue(getCallback());
        } else {
            Response response = null;
            try {
                response = mCall.execute();
                parseResponse(response);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("Error:doHttpCommon", e);
                LoggerHttpUtils.loggerMessage(log, this.loggerLevel, this.loggerTag, "请求错误", "发生未知异常");
                if (!isCallBack) {
                    createResultData(null, null, HttpConfig.CODE_SEND_ERR);
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
     * 读取缓存结果
     *
     * @param cls 目标解析类
     * @return true 表示缓存生效不需要执行Http请求了，false表示缓存失效需要执行Http请求结果
     */
    public boolean isCacheRead(Class<?> cls) {
        try {
            if (null == httpCacheInterface) {
                return false;
            }
            HttpCacheReader cacheReader = httpCacheInterface.readCache(domain,
                    StringUtils.isEmpty(cacheTag) ? httpCacheInterface.getCacheTag() : cacheTag,
                    httpCacheInterface.getCacheValidTime(), cls);
            if (null == cacheReader) {
                return false;
            }
            if (!cacheReader.isCacheOk) {
                return false;
            }
//			if (null == cacheReader.getCacheObj()
//					|| (null == cacheReader.getCacheData() || cacheReader.getCacheData().length <= 0)) {
//				return false;
//			}
            httpCacheInterface.cacheCallBack(cacheReader, cacheReader.getCacheObj(), cacheReader.getCacheData(), null,
                    HttpConfig.CODE_CACHE_OK);
            if (!cacheReader.isCacheOk) {
                return false;
            }
            createResultDataCache(cacheReader);
            if (cacheReader.isDoHttp) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:isCacheRead", e);
            return false;
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
                        createResultData(null, null, HttpConfig.CODE_SEND_ERR);
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
                    createResultData(null, null, HttpConfig.CODE_SEND_ERR);
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