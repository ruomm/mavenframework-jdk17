/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年6月16日 下午3:17:02
 */
package com.ruomm.webx.requestwrapperx;

import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostRepeatReaderFilter implements Filter {
    private final static Log log = LogFactory.getLog(PostRepeatReaderFilter.class);
    private final static String LOG_TAG = PostRepeatReaderFilter.class.getSimpleName();
    private boolean isDebug = false;
    private int uriLevel = 1;
    private XmlWapperFilter commonWapperFilter = null;
    private List<XmlWapperFilter> lstWapperFilters = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isDebug) {
            log.debug("PostRepeatReaderFilter执行->首次执行，经过init(ServletConfig config)后调用doFilter");
        }
        PostRepeatReaderRequestWrapper requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
                String contentTypeReq = request.getContentType();
                String contentType = null == contentTypeReq ? "" : contentTypeReq.toLowerCase();
                String uri = WebUtils.getRealUri(httpServletRequest.getRequestURI(), uriLevel);
                boolean isPass = XmlWapperHelper.parseToRepeatWapper(commonWapperFilter, lstWapperFilters, uri,
                        contentType);
                if (isDebug) {
                    log.debug(LOG_TAG + "执行->uri：" + uri);
                    log.debug(LOG_TAG + "执行->contentType：" + contentType);
                    log.debug(LOG_TAG + "执行结果->isPass：" + isPass);
                }
                if (isPass) {
                    requestWrapper = new PostRepeatReaderRequestWrapper((HttpServletRequest) request);
                    if (isDebug) {
                        log.debug(LOG_TAG + "执行结果->请求内容：" + requestWrapper.getWrapperString(contentType));
                    }
                }
            }
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {

            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configPath = filterConfig.getInitParameter(XmlWapperHelper.Key_Config_Path);
        XmlWapper xmlWapper = null;
        if (null == configPath || configPath.length() <= 0) {
            String uriVals = filterConfig.getInitParameter(XmlWapperHelper.Key_INTI_URIS);
            String typeVals = filterConfig.getInitParameter(XmlWapperHelper.Key_INTI_TYPES);
            String uriLevelVal = filterConfig.getInitParameter(XmlWapperHelper.Key_INTI_URI_LEVEL);
            String debugVal = filterConfig.getInitParameter(XmlWapperHelper.Key_INTI_DEBUG);
            String uris = null == uriVals || uriVals.length() <= 0 ? "*" : uriVals;
            String types = null == typeVals || typeVals.length() <= 0
                    ? "application/json,application/xml,text/json,text/xml,text/plain"
                    : typeVals;
            String uriLevel = null == uriLevelVal || uriLevelVal.length() <= 0 ? "1" : uriLevelVal;
            String debug = (null != debugVal && debugVal.length() > 0) && "true".equalsIgnoreCase(debugVal) ? "true"
                    : "false";
            log.debug(LOG_TAG + "初始化->通过init-param中的配置参数(uris、types、uriLevel、debug)加载配置");
            log.debug(LOG_TAG + "初始化->参数" + "uris：" + uris);
            log.debug(LOG_TAG + "初始化->参数" + "types：" + types);
            log.debug(LOG_TAG + "初始化->参数" + "uriLevel：" + uriLevel);
            log.debug(LOG_TAG + "初始化->参数" + "debug：" + debug);
            xmlWapper = XmlWapperHelper.loadXmlWapper(filterConfig.getServletContext(), uris, types, uriLevel, debug);
        } else {
            log.debug(LOG_TAG + "初始化->通过init-param中的路径参数(configPath)加载配置");
            log.debug(LOG_TAG + "初始化->参数" + "configPath：" + configPath);
            xmlWapper = XmlWapperHelper.loadXmlWapper(filterConfig.getServletContext(), configPath);
        }
        if (null == xmlWapper) {
            log.debug(LOG_TAG + "初始化->加载配置失败！");
            return;
        }

        isDebug = "true".equals(xmlWapper.getConfig().getDebug()) ? true : false;
        uriLevel = Integer.valueOf(xmlWapper.getConfig().getUriLevel());
        log.debug(LOG_TAG + "初始化->加载配置成功！");
        log.debug(LOG_TAG + "初始化配置结果->isDebug：" + isDebug);
        log.debug(LOG_TAG + "初始化配置结果->uriLevel：" + uriLevel);
        for (XmlWapperFilter tmpFilter : xmlWapper.getListFilters()) {
            log.debug(LOG_TAG + "初始化配置结果->XmlWapperFilter：" + tmpFilter.toString());
            if ("/*".equalsIgnoreCase(tmpFilter.getUrlSpace()) || "*".equalsIgnoreCase(tmpFilter.getUrlSpace())) {
                commonWapperFilter = tmpFilter;
                continue;
            } else {
                if (null == lstWapperFilters) {
                    lstWapperFilters = new ArrayList<XmlWapperFilter>();
                }
                lstWapperFilters.add(tmpFilter);
            }
        }

    }

    @Override
    public void destroy() {

    }

}
