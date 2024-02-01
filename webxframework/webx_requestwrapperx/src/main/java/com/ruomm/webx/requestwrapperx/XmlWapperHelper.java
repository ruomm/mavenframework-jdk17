/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月17日 下午5:02:31
 */
package com.ruomm.webx.requestwrapperx;

import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class XmlWapperHelper {
    private final static Log log = LogFactory.getLog(XmlWapperHelper.class);
    public final static String Key_Config_Path = "configPath";
    public final static String Key_INTI_URIS = "uris";
    public final static String Key_INTI_TYPES = "types";
    public final static String Key_INTI_URI_LEVEL = "uriLevel";
    public final static String Key_INTI_DEBUG = "debug";

    public static boolean parseToRepeatWapper(XmlWapperFilter commonFilter, List<XmlWapperFilter> lstWapperFilters,
                                              String uri, String contentType) {
        if (null == commonFilter && null == lstWapperFilters) {
            return false;
        }
        if (null == uri || uri.length() <= 0) {
            return false;
        }
        String realUri = uri.toLowerCase();
        if (null == contentType || contentType.length() <= 0) {
            return false;
        }
        String[] contentTypes = contentType.split(";");
        String realContentType = null == contentTypes || contentTypes.length < 1 ? null : contentTypes[0];
        if (null == realContentType || realContentType.length() <= 0) {
            return false;
        }
        realContentType = realContentType.toLowerCase();
        boolean isPass = false;
        boolean isUriMatch = false;
        if (null != lstWapperFilters) {
            for (XmlWapperFilter tmpFilter : lstWapperFilters) {

                if (realUri.startsWith(tmpFilter.getUrlSpace())) {
                    isUriMatch = true;
                    for (String tmp : tmpFilter.getLstContentTypes()) {
                        if (realContentType.equals(tmp) || realContentType.contains(tmp)) {
                            isPass = true;
                            break;
                        }
                    }
                    if (isPass) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        if (!isUriMatch && null != commonFilter) {
            for (String tmp : commonFilter.getLstContentTypes()) {
                if (realContentType.equals(tmp) || realContentType.contains(tmp)) {
                    isPass = true;
                    break;
                }
            }
        }
        return isPass;
    }

    public static XmlWapper loadXmlWapper(ServletContext servletContext, String uris, String types, String uriLevel, String debugVal) {
        String transUris = isEmptyStr(uris) ? "*" : uris;
        String transTypes = isEmptyStr(types) ? "application/json,application/xml,text/json,text/xml,text/plain"
                : types;
        String transDebug = !isEmptyStr(debugVal) && "true".equalsIgnoreCase(debugVal) ? "true" : "false";
        String uriLevelResult = parseServletContextUriLevel(servletContext, uriLevel);
        XmlWapperConfig wapperConfig = new XmlWapperConfig();
        wapperConfig.setUriLevel(uriLevelResult);
        wapperConfig.setDebug(transDebug);
        String[] uriArray = transUris.split(",");
        String[] typeArray = transTypes.split(",");
        List<XmlWapperFilter> lstFilters = new ArrayList<XmlWapperFilter>();
        for (String tmpUrl : uriArray) {
            if (null == tmpUrl || tmpUrl.length() <= 0) {
                continue;
            }
            String realUrl = WebUtils.getRealUri(tmpUrl);
            if (null == realUrl || realUrl.length() <= 0) {
                continue;
            }
            if (realUrl.equals("/*")) {
                realUrl = "*";
            } else {
                realUrl = realUrl + "/";
            }
            List<String> lstContentTypes = new ArrayList<String>();
            for (String tmp : typeArray) {
                if (null == tmp || tmp.length() <= 0) {
                    continue;
                }
                lstContentTypes.add(tmp.toLowerCase());
            }
            if (lstContentTypes.size() <= 0) {
                continue;
            }
            XmlWapperFilter filter = new XmlWapperFilter();
            filter.setUrlSpace(realUrl.toLowerCase());
            filter.setLstContentTypes(lstContentTypes);
            lstFilters.add(filter);
        }
        if (lstFilters.size() <= 0) {
            return null;
        }
        XmlWapper wapperResult = new XmlWapper();
        wapperResult.setConfig(wapperConfig);
        wapperResult.setListFilters(lstFilters);
        return wapperResult;
    }

    public static XmlWapper loadXmlWapper(ServletContext servletContext, String filePath) {
        if (null == filePath || filePath.length() <= 0) {
            return null;
        }
        XmlWapper xmlWapper = null;
        try {
            String xmlFilePath = WebUtils.parseRealPath(filePath);
            JAXBContext cxt = JAXBContext.newInstance(XmlWapper.class);
            Unmarshaller unmarshaller = cxt.createUnmarshaller();
            xmlWapper = (XmlWapper) unmarshaller.unmarshal(new File(xmlFilePath));
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:loadXmlWapper", e);
            return null;
        }
        if (null == xmlWapper || null == xmlWapper.getListFilters() || xmlWapper.getListFilters().size() < 0) {
            return null;
        }

        List<XmlWapperFilter> lstFilters = new ArrayList<XmlWapperFilter>();
        for (XmlWapperFilter itemFilter : xmlWapper.getListFilters()) {
            if (null == itemFilter.getLstContentTypes() || itemFilter.getLstContentTypes().size() <= 0) {
                continue;
            }
            String realUrl = WebUtils.getRealUri(itemFilter.getUrlSpace());
            if (null == realUrl || realUrl.length() <= 0) {
                continue;
            }
            if (realUrl.equals("/*")) {
                realUrl = "*";
            } else {
                realUrl = realUrl + "/";
            }
            List<String> lstContentTypes = new ArrayList<String>();
            for (String tmp : itemFilter.getLstContentTypes()) {
                if (null == tmp || tmp.length() <= 0) {
                    continue;
                }
                lstContentTypes.add(tmp.toLowerCase());
            }
            if (lstContentTypes.size() <= 0) {
                continue;
            }
            XmlWapperFilter filter = new XmlWapperFilter();
            filter.setUrlSpace(realUrl.toLowerCase());
            filter.setLstContentTypes(lstContentTypes);
            lstFilters.add(filter);

        }
        if (lstFilters.size() <= 0) {
            return null;
        }

        XmlWapperConfig wapperConfig = new XmlWapperConfig();

        String uriLevelResult = parseServletContextUriLevel(servletContext, null == xmlWapper.getConfig() ? null : xmlWapper.getConfig().getUriLevel());
        wapperConfig.setUriLevel(uriLevelResult);

        if (null != xmlWapper.getConfig() && null != xmlWapper.getConfig().getDebug()
                && "true".equalsIgnoreCase(xmlWapper.getConfig().getDebug())) {
            wapperConfig.setDebug("true");
        } else {
            wapperConfig.setDebug("false");
        }

        XmlWapper wapperResult = new XmlWapper();
        wapperResult.setConfig(wapperConfig);
        wapperResult.setListFilters(lstFilters);
        return wapperResult;

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
}
