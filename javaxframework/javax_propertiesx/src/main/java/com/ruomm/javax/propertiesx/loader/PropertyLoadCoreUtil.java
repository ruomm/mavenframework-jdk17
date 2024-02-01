/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月2日 下午3:18:09
 */
package com.ruomm.javax.propertiesx.loader;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.propertiesx.PropertyReaderCommon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PropertyLoadCoreUtil {
    private final static Log log = LogFactory.getLog(PropertyLoadCoreUtil.class);
    private final static String TAG_CLS = PropertyLoadCoreUtil.class.getSimpleName() + ".";

    public static PropertyLoaderReuslt loadProperty(String loadFile,
                                                    PropertyLoaderConfigHelper propertyLoaderConfigHelper, String charsetName, boolean emptyAsNull) {
        // 若是文件路径不存在则抛出异常
        if (StringUtils.isEmpty(loadFile)) {
            throw new RuntimeException(TAG_CLS + "loadProperty->入口配置文件路径为空");
        }
        log.debug(TAG_CLS + "loadProperty->" + "入口配置文件参数为：" + "activeKey:" + propertyLoaderConfigHelper.activeKey()
                + ",activePathKey:" + propertyLoaderConfigHelper.activePathKey() + ",incluesKey:"
                + propertyLoaderConfigHelper.incluesKey() + ",extensionKey:"
                + propertyLoaderConfigHelper.extensionKey());
        PropertyLoaderFileBean loaderFileBean = parseLoaderFile(loadFile);
        log.debug(TAG_CLS + "loadProperty->" + "入口配置文件文件属性解析结果:" + loaderFileBean.toString());
        // 读取主配置文件
        Map<String, String> parsePropertyMaps = new HashMap<String, String>();
        log.debug(TAG_CLS + "loadProperty->" + "开始读取入口配置文件:" + loadFile);
        PropertyLoaderBean loaderBeanMain = new PropertyLoaderBean();
        try {
            PropertyReaderCommon propertyReaderCommon = new PropertyReaderCommon(loadFile, charsetName, emptyAsNull);
            propertyReaderCommon.loadProps(true);
            Map<String, String> tmpMap = propertyReaderCommon.propertyToMap();
            if (!StringUtils.isEmpty(propertyLoaderConfigHelper.activeKey())) {
                String activeModeVal = parseActiveMode(tmpMap.get(propertyLoaderConfigHelper.activeKey()));
                loaderBeanMain.setActive(activeModeVal);
            }
            if (!StringUtils.isEmpty(propertyLoaderConfigHelper.activePathKey())) {
                loaderBeanMain.setActivePath(tmpMap.get(propertyLoaderConfigHelper.activePathKey()));
            }
            if (!StringUtils.isEmpty(propertyLoaderConfigHelper.incluesKey())) {
                String incluesVal = parseInclueValue(tmpMap.get(propertyLoaderConfigHelper.incluesKey()), null);
                loaderBeanMain.setInclues(incluesVal);
            }
            if (!StringUtils.isEmpty(propertyLoaderConfigHelper.extensionKey())) {
                loaderBeanMain.setExtension(tmpMap.get(propertyLoaderConfigHelper.extensionKey()));
            }
            if (null != tmpMap && tmpMap.size() > 0) {
                parsePropertyMaps.putAll(tmpMap);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug(TAG_CLS + "loadProperty->读取入口配置文件异常", e);
            throw new RuntimeException(TAG_CLS + "loadProperty->读取入口配置文件异常");
        }
//		PropertyLoaderBean loaderBeanActive = new PropertyLoaderBean();
        String activieLoadFile = parseActivePropertyPath(loaderBeanMain, loaderFileBean);
        log.debug(TAG_CLS + "loadProperty->" + "环境配置文件路径为:" + activieLoadFile);
        if (null == activieLoadFile) {
            log.debug(TAG_CLS + "loadProperty->" + "环境配置文件为空，无需读取");
        } else if (activieLoadFile.equalsIgnoreCase(loadFile)) {
            log.debug(TAG_CLS + "loadProperty->" + "环境配置文件和入口配置文件一致，无需读取:" + activieLoadFile);
        } else {
            log.debug(TAG_CLS + "loadProperty->" + "开始读取环境配置文件:" + activieLoadFile);
            try {
                PropertyReaderCommon propertyReaderCommon = new PropertyReaderCommon(activieLoadFile, charsetName,
                        emptyAsNull);
                propertyReaderCommon.loadProps(true);
                Map<String, String> tmpMap = propertyReaderCommon.propertyToMap();
                if (null != tmpMap && tmpMap.size() > 0) {
//					if (!StringUtils.isEmpty(propertyLoaderConfigHelper.loadActiveKey())) {
//						loaderBeanActive.setActive(tmpMap.get(propertyLoaderConfigHelper.loadActiveKey()));
//					}
//					if (!StringUtils.isEmpty(propertyLoaderConfigHelper.loadAppendKey())) {
//						loaderBeanActive.setAppend(tmpMap.get(propertyLoaderConfigHelper.loadAppendKey()));
//					}
//					if (!StringUtils.isEmpty(propertyLoaderConfigHelper.loadIncluesKey())) {
//						loaderBeanActive.setInclues(tmpMap.get(propertyLoaderConfigHelper.loadIncluesKey()));
//					}
                    for (String key : tmpMap.keySet()) {
                        if (StringUtils.isEmpty(key)) {
                            continue;
                        }
                        if (!StringUtils.isEmpty(propertyLoaderConfigHelper.activeKey())
                                && propertyLoaderConfigHelper.activeKey().equals(key)) {
                            continue;
                        } else if (!StringUtils.isEmpty(propertyLoaderConfigHelper.activePathKey())
                                && propertyLoaderConfigHelper.activePathKey().equals(key)) {
                            continue;
                        } else if (!StringUtils.isEmpty(propertyLoaderConfigHelper.extensionKey())
                                && propertyLoaderConfigHelper.extensionKey().equals(key)) {
                            continue;
                        } else if (!StringUtils.isEmpty(propertyLoaderConfigHelper.incluesKey())
                                && propertyLoaderConfigHelper.incluesKey().equals(key)) {
                            String incluesVal = parseInclueValue(parsePropertyMaps.get(key), tmpMap.get(key));
                            loaderBeanMain.setInclues(incluesVal);
                            if (null != incluesVal) {
                                parsePropertyMaps.put(key, incluesVal);
                            }
                        } else {
                            parsePropertyMaps.put(key, tmpMap.get(key));
                        }
                    }
                }
            } catch (Exception e) {
                log.debug(TAG_CLS + "loadProperty->读取环境配置文件异常", e);
                throw new RuntimeException(TAG_CLS + "loadProperty->读取环境配置文件异常");
            }
        }
        List<String> listInclues = new ArrayList<String>();
        if (!StringUtils.isEmpty(loaderBeanMain.getInclues())) {
            String[] incluesDatas = loaderBeanMain.getInclues().split(",");
            for (String tmp : incluesDatas) {
                String tmpVal = null == tmp ? tmp : tmp.trim();
                if (!StringUtils.isEmpty(tmpVal) && !listInclues.contains(tmpVal)) {
                    listInclues.add(tmpVal);
                }
            }
        }
//		if (!StringUtils.isEmpty(loaderBeanActive.getInclues())) {
//			String[] incluesDatas = loaderBeanActive.getInclues().split(",");
//			for (String tmp : incluesDatas) {
//				String tmpVal = null == tmp ? tmp : tmp.trim();
//				if (!StringUtils.isEmpty(tmpVal) && !listInclues.contains(tmpVal)) {
//					listInclues.add(tmpVal);
//				}
//			}
//		}
        log.debug(TAG_CLS + "loadProperty->" + "配置文件额外加载列表为:" + listInclues);
        for (String tmpPropertyFile : listInclues) {
            log.debug(TAG_CLS + "loadProperty->" + "开始读取额外配置文件:" + listInclues);
            try {
                PropertyReaderCommon propertyReaderCommon = new PropertyReaderCommon(tmpPropertyFile, charsetName,
                        emptyAsNull);
                propertyReaderCommon.loadProps(true);
                Map<String, String> tmpMap = propertyReaderCommon.propertyToMap();
                if (null == tmpMap || tmpMap.size() <= 0) {
                    continue;
                }
                parsePropertyMaps.putAll(tmpMap);
            } catch (Exception e) {
                log.debug(TAG_CLS + "loadProperty->读取额外配置文件异常", e);
                throw new RuntimeException(TAG_CLS + "loadProperty->读取额外配置文件异常");
            }

        }
        log.debug(TAG_CLS + "loadProps->" + "配置文件读取成功，总计配置条数为：" + parsePropertyMaps.size());
        PropertyLoaderReuslt propertyLoaderReuslt = new PropertyLoaderReuslt();
        propertyLoaderReuslt.setProperyMap(parsePropertyMaps);
        propertyLoaderReuslt.setActive(loaderBeanMain.getActive());
        propertyLoaderReuslt.setAppend(loaderBeanMain.getActivePath());
//		propertyLoaderReuslt.setInclues(parseInclueValue(loaderBeanMain.getInclues(), loaderBeanActive.getInclues()));
        propertyLoaderReuslt.setInclues(loaderBeanMain.getInclues());

        return propertyLoaderReuslt;

    }

    private static String parseActiveMode(String strMode) {
        if (StringUtils.isEmpty(strMode)) {
            return strMode;
        } else if ((strMode.startsWith("${") || strMode.startsWith("{")) && strMode.endsWith("}")) {
            return "dev";
        } else {
            return strMode;
        }
    }

    private static String parseInclueValue(String oldValue, String newValue) {
        if (StringUtils.isEmpty(oldValue) && StringUtils.isEmpty(newValue)) {
            return null == newValue ? oldValue : newValue;
        } else if (StringUtils.isEmpty(newValue)) {
            return oldValue;
        } else if (StringUtils.isEmpty(oldValue)) {
            return newValue;
        } else {
            return oldValue + "," + newValue;
        }
    }

    private static String parseActivePropertyPath(PropertyLoaderBean propertyLoaderConfigHelper,
                                                  PropertyLoaderFileBean loaderFileBean) {
        String activeAppend = null;
        if (StringUtils.isEmpty(propertyLoaderConfigHelper.getActive())) {
            activeAppend = "";
        } else if ((propertyLoaderConfigHelper.getActive().startsWith("${")
                || propertyLoaderConfigHelper.getActive().startsWith("{"))
                && propertyLoaderConfigHelper.getActive().endsWith("}")) {
            activeAppend = "-" + "dev";
        } else {
            activeAppend = "-" + propertyLoaderConfigHelper.getActive();
        }
        String activePathVal = propertyLoaderConfigHelper.getActivePath();
        String activeLoadPath = null;
        if (StringUtils.isEmpty(activePathVal)) {
            activeLoadPath = loaderFileBean.getPath();
        } else if (WebUtilx.isAbsolutePath(activePathVal)) {
            if (activePathVal.endsWith("\\") || activePathVal.endsWith("/")) {
                activeLoadPath = activePathVal;
            } else {
                activeLoadPath = activePathVal + File.separator;
            }
        } else if (activePathVal.endsWith("\\") || activePathVal.endsWith("/")) {
            activeLoadPath = loaderFileBean.getPath() + activePathVal;
        } else {
            activeLoadPath = loaderFileBean.getPath() + activePathVal + File.separator;
        }
        String activeExtension = null;
        if (StringUtils.isEmpty(propertyLoaderConfigHelper.getExtension())) {
            activeExtension = loaderFileBean.getFileExtension();
        } else {
            activeExtension = propertyLoaderConfigHelper.getExtension().startsWith(".")
                    ? propertyLoaderConfigHelper.getExtension()
                    : ("." + propertyLoaderConfigHelper.getExtension());
        }
        return activeLoadPath + loaderFileBean.getFileNameWithoutExtension() + activeAppend + activeExtension;
    }

    private static PropertyLoaderFileBean parseLoaderFile(String loadFile) {
        PropertyLoaderFileBean loaderFileBean = new PropertyLoaderFileBean();
        loaderFileBean.setLoadFile(loadFile);
        int index = lastIndexOfFileSeparator(loadFile);
        if (index >= 0) {
            loaderFileBean.setPath(loadFile.substring(0, index + 1));
            loaderFileBean.setFileName(loadFile.substring(index + 1));
        } else {
            loaderFileBean.setPath("");
            loaderFileBean.setFileName(loadFile);
        }
        index = loaderFileBean.getFileName().indexOf(".");
        if (index >= 0) {
            loaderFileBean.setFileNameWithoutExtension(loaderFileBean.getFileName().substring(0, index));
            loaderFileBean.setFileExtension(loaderFileBean.getFileName().substring(index));
        } else {
            loaderFileBean.setFileNameWithoutExtension(loaderFileBean.getFileName());
            loaderFileBean.setFileExtension("");
        }
        return loaderFileBean;
    }

    /**
     * 查找文件分割符号最后的位置
     * @param filePath 文件路径
     * @return 返回分割符号最后的位置，空返回-99，没有返回-1，
     */
    private static int lastIndexOfFileSeparator(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return -99;
        }
        int filePosi1 = filePath.lastIndexOf("\\");
        int filePosi2 = filePath.lastIndexOf("/");
        if (filePosi1 < 0 && filePosi2 < 0) {
            return -1;
        } else {
            return filePosi1 > filePosi2 ? filePosi1 : filePosi2;
        }
    }
}
