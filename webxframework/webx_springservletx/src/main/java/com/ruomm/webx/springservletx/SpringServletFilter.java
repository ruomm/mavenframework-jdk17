/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月9日 下午12:46:09
 */
package com.ruomm.webx.springservletx;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SpringServletFilter extends HttpServlet implements Filter {
    private final static Log log = LogFactory.getLog(SpringServletFilter.class);
    /**
     *
     */
    private final static long serialVersionUID = 9129485622503046818L;
    private final static String LOG_TAG = SpringServletFilter.class.getSimpleName();
    private List<XmlSprServletSpace> lstServletSpace;
    private SprSevletParam sprServletParam;

    @Override
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        super.init(config);
        if (sprServletParam.isDebug) {
            log.debug(LOG_TAG + "执行->首次执行，经过init(ServletConfig config)后调用doFilter");
        }

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest) && !(resp instanceof HttpServletResponse)) {
            if (sprServletParam.isDebug) {
                log.debug(LOG_TAG + "执行->不是HttpServletRequest请求，执行chain.doFilter放行该请求！");
            }
            chain.doFilter(req, resp);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String uri = request.getRequestURI();
        // TODO Auto-generated method stub
        String nodeUri = WebUtils.getRealUri(uri, sprServletParam.uriLevel);
//		nodeUri = WebUtils.getRealUri(uri, new Random().nextInt(5) - 1);
        if (null != sprServletParam.reloadUri && sprServletParam.reloadUri.equalsIgnoreCase(nodeUri)) {
            SprSevletParam reloadParam = reloadConfig(request.getServletContext());
            if (null != reloadParam) {
                sprServletParam = reloadParam;
                sendResponse(response, 200, "config  reload success!");
                return;
            } else {
                sendResponse(response, 200, "config  reload Fail!");
                return;
            }
        }
        if (sprServletParam.isDebug) {
            log.debug(LOG_TAG + "执行->HttpServletRequest请求的URI：" + uri);
            log.debug(LOG_TAG + "执行->去掉Web容器路径后的URI：" + nodeUri);
        }
        UriSprServletNode node = SprServletHelper.parseServlet(nodeUri, lstServletSpace);
        if (null == node) {
            if (sprServletParam.isDebug) {
                log.debug(LOG_TAG + "执行->没有找到对应的请求解析Map，执行chain.doFilter放行该请求！");
            }
            chain.doFilter(req, resp);
            return;
        }
        if (sprServletParam.isDebug) {
            log.debug(LOG_TAG + "执行->找到对应的请求解析Map，拦截该请求跳转到自定义Servlet中！");
        }
        progress(request, response, node);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        sprServletParam = new SprSevletParam();
        log.debug("初始化参数filterConfig");
        String configPath = filterConfig.getInitParameter(SprServletHelper.Key_Config_Path);
        if (null == configPath || configPath.length() <= 0) {
            log.debug(
                    LOG_TAG + "初始化->Filter初始化参数init-param没有" + SprServletHelper.Key_Config_Path + "参数，加载默认配置文件");
            configPath = "class:config/filterconfig/spring_servlet_config.xml";
        } else {
            log.debug(
                    LOG_TAG + "初始化->依据Filter初始化参数init-param中的" + SprServletHelper.Key_Config_Path + "值获取配置文件路径");
        }
        log.debug(LOG_TAG + "初始化->加载配置文件，路径：" + configPath);
        XmlSprServlet sprServlet = SprServletHelper.loadSprServlet(configPath, filterConfig.getServletContext());
        if (null == sprServlet) {
            log.debug(LOG_TAG + "初始化->加载配置文件失败！");
            sprServletParam.uriLevel = 1;
            sprServletParam.beanFactory = null;
            sprServletParam.isDebug = false;
            sprServletParam.reloadUri = null;
            sprServletParam.reloadPath = null;
            return;
        }
        log.debug(LOG_TAG + "初始化->加载配置文件成功！");
        log.debug(LOG_TAG + "初始化->配置：" + sprServlet.toString());
        lstServletSpace = sprServlet.getListServletSpaces();
        // 解析URILEVEL
        sprServletParam.uriLevel = Integer.valueOf(sprServlet.getConfig().getUriLevel());
        if (null != sprServlet.getConfig().getDebug() && "true".equalsIgnoreCase(sprServlet.getConfig().getDebug())) {
            sprServletParam.isDebug = true;
        } else {
            sprServletParam.isDebug = false;
        }
        sprServletParam.reloadUri = null == sprServlet.getConfig().getReloadUri()
                || sprServlet.getConfig().getReloadUri().length() <= 0 ? null : sprServlet.getConfig().getReloadUri();
        if (null != sprServletParam.reloadUri && sprServletParam.reloadUri.length() > 0) {
            sprServletParam.reloadPath = configPath;
        }
        log.debug(LOG_TAG + "初始化->uriLevel：" + sprServletParam.uriLevel);
        log.debug(LOG_TAG + "初始化->isDebug：" + sprServletParam.isDebug);
        log.debug(LOG_TAG + "初始化->reloadUri：" + sprServletParam.reloadUri);
        log.debug(LOG_TAG + "初始化->reloadPath：" + sprServletParam.reloadPath);
        String parse = null;
        // 解析parse
        if (null != sprServlet.getConfig().getParse() && sprServlet.getConfig().getParse().length() > 0) {
            parse = sprServlet.getConfig().getParse();
        } else {
            parse = null;
        }
        if (null != parse && parse.length() > 0) {
            try {
                sprServletParam.beanFactory = (SpringServletAware) Class.forName(parse).newInstance();
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:init", e);
            }
        }
        if (null == sprServletParam.beanFactory) {
            log.debug(LOG_TAG + "初始化->依据parse值设置beanFactory失败！");
        } else {

            log.debug(
                    LOG_TAG + "初始化->依据parse值设置beanFactory成功：" + sprServletParam.beanFactory.getClass().getName());
        }

    }

    private void progress(HttpServletRequest request, HttpServletResponse response, UriSprServletNode node)
            throws ServletException, IOException {
        if (null == sprServletParam.beanFactory) {
            sendResponse(response, 404, "The requested resource is not available.");
            return;
        }
        boolean isClass = false;
        Class<?> beanFactoryCls = null;
        int counter = StringUtils.counterAppearNum(node.getSpringName(), '.');
        if (counter > 1) {
            isClass = true;
            try {
                beanFactoryCls = Class.forName(node.getSpringName());
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:init", e);
                beanFactoryCls = null;
            }
        }
        if (isClass && null == beanFactoryCls) {
            sendResponse(response, 404, "The requested resource is not available.");
            return;
        }
        Object object = null;
        try {
            if (isClass) {
                object = sprServletParam.beanFactory.getBeanByClass(beanFactoryCls);
            } else {
                object = sprServletParam.beanFactory.getBeanByName(node.getSpringName());
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:init", e);
            object = null;
        }
        if (null == object) {
            sendResponse(response, 404, "The requested resource is not available.");
            return;
        }

        // 校验是否通过
        boolean isPackagePass = true;
        try {
            if (null != node.getPackageSpace() && node.getPackageSpace().length() > 0) {
                isPackagePass = object.getClass().getName().toLowerCase()
                        .startsWith(node.getPackageSpace().toLowerCase());
            } else {
                isPackagePass = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:init", e);
        }
        if (!isPackagePass) {
            sendResponse(response, 404, "The requested resource is not available.");
            return;
        }

        if (SprServletHelper.Mode_Ref.equals(node.getMode())) {
            // Spring管理,反射模式
            Method method = null;
            try {
                method = object.getClass().getDeclaredMethod(node.getMethodName(), HttpServletRequest.class,
                        HttpServletResponse.class);
            } catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
                // TODO: handle exception
                log.error("Error:init", e);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:init", e);
            }
            if (null == object || null == method) {
                sendResponse(response, 404, "The requested resource is not available.");
                return;
            }
            try {
                method.setAccessible(true);
                method.invoke(object, request, response);
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException e) {
                // TODO: handle exception
                log.error("Error:init", e);
                sendResponse(response, 400, "Bad Request:Method Invoke Error");
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:init", e);
                sendResponse(response, 400, "Bad Request:Method Invoke Error");
            }

        } else {
            SpringServletProgress ssProgress = null;
            if (null != object & object instanceof SpringServletProgress) {
                ssProgress = (SpringServletProgress) object;
            }
            if (null == object || null == ssProgress) {
                sendResponse(response, 404, "The requested resource is not available.");
                return;
            }
            try {
                ssProgress.doProgress(request, response);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:init", e);
                sendResponse(response, 400, "Bad Request:Method Invoke Error");
            }

        }

    }

    public SprSevletParam reloadConfig(ServletContext servletContext) {

        if (null == sprServletParam) {
            log.debug(LOG_TAG + "初始化->初始化时候失败，无法重新加载配置！");
            return null;
        }
        if (null == sprServletParam.reloadPath || sprServletParam.reloadPath.length() <= 0) {
            log.debug(LOG_TAG + "初始化->reloadPath为空，不能重新加载配置");
            return null;
        }

        String configPath = sprServletParam.reloadPath;
        log.debug(LOG_TAG + "初始化->加载配置文件，路径：" + configPath);
        XmlSprServlet sprServlet = SprServletHelper.loadSprServlet(configPath, servletContext);
        if (null == sprServlet) {
            log.debug(LOG_TAG + "初始化->加载配置文件失败！");
            return null;
        }
        SprSevletParam reloadParam = new SprSevletParam();
        reloadParam.uriLevel = 1;
        reloadParam.beanFactory = null;
        reloadParam.isDebug = false;
        reloadParam.reloadUri = null;
        reloadParam.reloadPath = null;
        log.debug(LOG_TAG + "初始化->加载配置文件成功！");
        log.debug(LOG_TAG + "初始化->配置：" + sprServlet.toString());
        lstServletSpace = sprServlet.getListServletSpaces();
        // 解析URILEVEL
        reloadParam.uriLevel = Integer.valueOf(sprServlet.getConfig().getUriLevel());
        if (null != sprServlet.getConfig().getDebug() && "true".equalsIgnoreCase(sprServlet.getConfig().getDebug())) {
            reloadParam.isDebug = true;
        } else {
            reloadParam.isDebug = false;
        }
        reloadParam.reloadUri = null == sprServlet.getConfig().getReloadUri()
                || sprServlet.getConfig().getReloadUri().length() <= 0 ? null : sprServlet.getConfig().getReloadUri();
        if (null != reloadParam.reloadUri && reloadParam.reloadUri.length() > 0) {
            reloadParam.reloadPath = configPath;
        }
        log.debug(LOG_TAG + "初始化->uriLevel：" + reloadParam.uriLevel);
        log.debug(LOG_TAG + "初始化->isDebug：" + reloadParam.isDebug);
        log.debug(LOG_TAG + "初始化->reloadUri：" + reloadParam.reloadUri);
        String parse = null;
        // 解析parse
        if (null != sprServlet.getConfig().getParse() && sprServlet.getConfig().getParse().length() > 0) {
            parse = sprServlet.getConfig().getParse();
        } else {
            parse = null;
        }
        if (null != parse && parse.length() > 0) {
            try {
                reloadParam.beanFactory = (SpringServletAware) Class.forName(parse).newInstance();
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:reloadConfig", e);
            }
        }
        if (null == reloadParam.beanFactory) {
            log.debug(LOG_TAG + "初始化->依据parse值设置beanFactory失败！");
        } else {

            System.out
                    .println(LOG_TAG + "初始化->依据parse值设置beanFactory成功：" + reloadParam.beanFactory.getClass().getName());
        }
        return reloadParam;
    }

    // 发送Http请求返回值
    public void sendResponse(HttpServletResponse response, int sc, String msg) {

        if (sc == 200) {
            PrintWriter respWriter = null;
            try {
                respWriter = response.getWriter();
                response.setContentType("text/plain");
                response.getWriter().write(msg);
                response.getWriter().close();

            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                try {
                    if (null != respWriter) {
                        respWriter.close();
                    }
                } catch (Exception e2) {
                    // TODO: handle exception
                    log.error("Error:sendResponse", e2);
                }
            }
        } else {
            try {
                response.sendError(sc, msg);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                log.error("Error:sendResponse", e1);
            }
        }
    }

}