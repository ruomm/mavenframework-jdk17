/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月9日 下午2:01:59
 */
package com.ruomm.webx.springservletx;

import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import jakarta.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SprServletHelper {
    private final static Log log = LogFactory.getLog(SprServletHelper.class);
    public final static String Key_Config_Path = "configPath";
    public final static String Default_Method = "doProgress";
    public final static String Mode_Ref = "ref";
    public final static String Mode_Interface = "interface";

    // 速度快的方式，只支持严格匹配，不区分大小写
    public static UriSprServletNode parseServlet(String uri, List<XmlSprServletSpace> lstServletSpaces) {
        if (isEmptyStr(uri)) {
            return null;
        }
        if (null == lstServletSpaces || lstServletSpaces.size() <= 0) {
            return null;
        }
        int indexUrlSeparator = uri.lastIndexOf("/");
        // 不是有效的uri直接返回
        if (indexUrlSeparator <= 0 || indexUrlSeparator >= uri.length() - 1) {
            return null;
        }
        String spaceName = uri.substring(0, indexUrlSeparator);
        String servletName = uri.substring(indexUrlSeparator + 1);
        UriSprServletNode node = null;
        for (XmlSprServletSpace tmpSpace : lstServletSpaces) {
            if (spaceName.equalsIgnoreCase(tmpSpace.getUrlSpace())) {
                if (null == tmpSpace.getLstServlets() || tmpSpace.getLstServlets().size() <= 0) {
                    node = new UriSprServletNode();
                    node.setServletName(servletName);
                    node.setSpringName(servletName);
                    node.setMethodName(tmpSpace.getMethodName());
                    node.setMode(tmpSpace.getMode());
                    node.setPackageSpace(tmpSpace.getPackageSpace());
                } else {
                    for (XmlSprServletNode tmpNode : tmpSpace.getLstServlets()) {
                        boolean isCom = true;
                        if (servletName.equalsIgnoreCase(tmpNode.getServletName())) {
                            node = new UriSprServletNode();
                            node.setServletName(tmpNode.getServletName());
                            node.setSpringName(tmpNode.getSpringName());
                            node.setMethodName(tmpSpace.getMethodName());
                            node.setMode(tmpSpace.getMode());
                            node.setPackageSpace(tmpSpace.getPackageSpace());
                            break;
                        }
                        if (tmpNode.getServletName().equalsIgnoreCase("*")
                                && tmpNode.getSpringName().equalsIgnoreCase("*")) {
                            isCom = true;
                        }
                        if (isCom && null == node) {
                            node = new UriSprServletNode();
                            node.setServletName(servletName);
                            node.setSpringName(servletName);
                            node.setMethodName(tmpSpace.getMethodName());
                            node.setMode(tmpSpace.getMode());
                            node.setPackageSpace(tmpSpace.getPackageSpace());
                        }
                    }
                }
                break;
            }
        }
        return node;
    }

//	private static XmlSprServlet loadSprServletByFile(String filePath) {
//
//		XmlSprServlet resp = null;
//		try {
//			String xmlFilePath = WebUtils.parseRealPath(filePath);
//			JAXBContext cxt = JAXBContext.newInstance(XmlSprServlet.class);
//			Unmarshaller unmarshaller = cxt.createUnmarshaller();
//			resp = (XmlSprServlet) unmarshaller.unmarshal(new File(xmlFilePath));
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//			log.error("Error:parseServlet",e);
//			return null;
//		}
//
//		return resp;
//
//	}

    /**
     * 加载配置文件
     *
     * @param filePath
     * @return
     */
    public static XmlSprServlet loadSprServlet(String filePath, ServletContext servletContext) {

        XmlSprServlet sprServlet = null;
        try {
            String xmlFilePath = WebUtils.parseRealPath(filePath);
            JAXBContext cxt = JAXBContext.newInstance(XmlSprServlet.class);
            Unmarshaller unmarshaller = cxt.createUnmarshaller();
            sprServlet = (XmlSprServlet) unmarshaller.unmarshal(new File(xmlFilePath));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:loadSprServlet", e);
            return null;
        }
        if (null == sprServlet || null == sprServlet.getListServletSpaces()
                || sprServlet.getListServletSpaces().size() <= 0) {
            return null;
        }
        List<XmlSprServletSpace> lstSpaces = new ArrayList<XmlSprServletSpace>();
        for (XmlSprServletSpace tmpSpace : sprServlet.getListServletSpaces()) {
            if (isEmptyStr(tmpSpace.getUrlSpace())) {
                continue;
            }
            String urlSpace = WebUtils.getRealUri(tmpSpace.getUrlSpace());
            if (isEmptyStr(urlSpace)) {
                continue;
            }
            XmlSprServletSpace space = new XmlSprServletSpace();
            space.setUrlSpace(urlSpace);
            if (!isEmptyStr(tmpSpace.getMethodName())) {
                space.setMethodName(tmpSpace.getMethodName());
            } else if (null != sprServlet.getConfig() && !isEmptyStr(sprServlet.getConfig().getMethodName())) {
                space.setMethodName(sprServlet.getConfig().getMethodName());
            } else {
                space.setMethodName(Default_Method);
            }
            String modeStr = null;
            if (!isEmptyStr(tmpSpace.getMode())) {
                modeStr = tmpSpace.getMode();
            } else if (null != sprServlet.getConfig() && !isEmptyStr(sprServlet.getConfig().getMode())) {
                modeStr = sprServlet.getConfig().getMode();
            } else {
                modeStr = null;
            }
            if (null != modeStr && modeStr.toLowerCase().equalsIgnoreCase(Mode_Ref)) {
                space.setMode(Mode_Ref);
            } else {
                space.setMode(Mode_Interface);
            }
            space.setPackageSpace(tmpSpace.getPackageSpace());
            if (null == tmpSpace.getLstServlets() || tmpSpace.getLstServlets().size() <= 0) {
                space.setLstServlets(null);
            } else {
                List<XmlSprServletNode> listNodes = new ArrayList<XmlSprServletNode>();
                for (XmlSprServletNode tmpNode : tmpSpace.getLstServlets()) {
                    String tmpServletName = trimPathSeparator(tmpNode.getServletName());
                    String tmpSpringName = trimPathSeparator(tmpNode.getSpringName());
                    XmlSprServletNode node = new XmlSprServletNode();
                    String simpleServletName = null;
                    if (isEmptyStr(tmpServletName) && isEmptyStr(tmpSpringName)) {
                        continue;
                    } else if (isEmptyStr(tmpServletName)) {
                        node.setServletName(tmpSpringName);
                        node.setSpringName(tmpSpringName);
                        if (tmpSpringName.endsWith("Servlet") && tmpSpringName.length() > 7) {
                            int st = tmpSpringName.length();
                            simpleServletName = tmpSpringName.substring(0, st - 7);
                        } else if (tmpSpringName.endsWith("Action") && tmpSpringName.length() > 6) {
                            int st = tmpSpringName.length();
                            simpleServletName = tmpSpringName.substring(0, st - 6);
                        } else if (tmpSpringName.endsWith("Service") && tmpSpringName.length() > 7) {
                            int st = tmpSpringName.length();
                            simpleServletName = tmpSpringName.substring(0, st - 7);
                        } else if (tmpSpringName.endsWith("Controller") && tmpSpringName.length() > 10) {
                            int st = tmpSpringName.length();
                            simpleServletName = tmpSpringName.substring(0, st - 10);
                        } else if (tmpSpringName.endsWith("Progress") && tmpSpringName.length() > 8) {
                            int st = tmpSpringName.length();
                            simpleServletName = tmpSpringName.substring(0, st - 8);
                        }
                    } else if (isEmptyStr(tmpSpringName)) {
                        node.setServletName(tmpServletName);
                        node.setSpringName(tmpServletName);
                    } else {
                        node.setServletName(tmpServletName);
                        node.setSpringName(tmpSpringName);
                    }
                    if (isEmptyStr(tmpNode.getMethodName())) {
                        node.setMethodName(space.getMethodName());
                    } else {
                        node.setMethodName(tmpNode.getMethodName());
                    }

                    if (isEmptyStr(tmpNode.getMode())) {
                        node.setMode(space.getMode());
                    } else {
                        if (tmpNode.getMode().equalsIgnoreCase(Mode_Ref)) {
                            node.setMode(Mode_Ref);
                        } else {
                            node.setMode(Mode_Interface);
                        }
                    }
                    if (null != simpleServletName && simpleServletName.length() > 0) {
                        XmlSprServletNode nodeSimple = new XmlSprServletNode();
                        nodeSimple.setServletName(simpleServletName);
                        nodeSimple.setSpringName(node.getSpringName());
                        nodeSimple.setMethodName(node.getMethodName());
                        nodeSimple.setMode(node.getMode());
                        listNodes.add(nodeSimple);
                    }
                    listNodes.add(node);
                }
                if (listNodes.size() > 0) {
                    space.setLstServlets(listNodes);
                }
            }
            lstSpaces.add(space);
        }
        if (lstSpaces.size() > 0) {
            // 配置更新
            XmlSprServletConfig servletConfig = new XmlSprServletConfig();
            if (null == sprServlet.getConfig()) {
                servletConfig.setParse(null);
                servletConfig.setMethodName(null);
                servletConfig.setUriLevel(parseServletContextUriLevel(servletContext, ""));
                servletConfig.setMode(null);
                servletConfig.setReloadUri(null);
            } else {
                servletConfig.setParse(sprServlet.getConfig().getParse());
                servletConfig.setMethodName(sprServlet.getConfig().getMethodName());
                servletConfig.setUriLevel(parseServletContextUriLevel(servletContext, sprServlet.getConfig().getUriLevel()));
                servletConfig.setMode(sprServlet.getConfig().getMode());
                servletConfig.setDebug(sprServlet.getConfig().getDebug());
                servletConfig.setReloadUri(WebUtils.getRealUri(sprServlet.getConfig().getReloadUri()));
            }
            XmlSprServlet sprServletResult = new XmlSprServlet();
            sprServletResult.setConfig(servletConfig);
            sprServletResult.setListServletSpaces(lstSpaces);
            return sprServletResult;
        } else {
            return null;
        }

    }

    private static String parseServletContextUriLevel(ServletContext servletContext, String uriLevelStr) {

        int uriLevel = -1;
        if (isEmptyStr(uriLevelStr)) {
            uriLevel = -1;
        } else {
            try {
                uriLevel = Integer.parseInt(uriLevelStr);
            } catch (Exception e) {
                log.error("Error:parseUriLevel", e);
                uriLevel = -1;
            }
        }
        if (uriLevel >= 0 && uriLevel <= 10) {
            return uriLevel + "";
        }
        if (null == servletContext) {
            return "1";
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
            return "0";
        } else {
            int counter = 0;
            for (int i = 0; i < tmpUri.length(); i++) {
                if (tmpUri.charAt(i) == '/') {
                    counter++;
                }
            }
            return counter + "";
        }


    }

    private static boolean isEmptyStr(String str) {
        return null == str || str.length() <= 0;
    }

    private static String trimPathSeparator(String str) {
        if (null == str || str.length() <= 0) {
            return str;
        }
        String resultStr = null;
        if (str.startsWith("/")) {
            resultStr = str.substring(1);
        } else {
            resultStr = str;
        }
        if (resultStr.endsWith("/")) {
            int tmpIndex = resultStr.length() - 1;
            resultStr = resultStr.substring(0, tmpIndex);
        }
        return resultStr;
    }
}