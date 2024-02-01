/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月3日 下午1:33:22
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyReaderCharsetXml implements PropertyReaderCommonHelper {
    public final static String DEF_SPACE_NAME_KEY = "spaceName";
    public final static String DEF_CONFIG_KEY = "config";
    public final static String DEF_NAME_KEY = "name";
    public final static String DEF_VALUE_KEY = "value";
    public final static String DEF_COMMIT_KEY = "commit";
    public final static boolean DEFAULT_EMPTY_AS_NULL = true;
    private final static Log log = LogFactory.getLog(PropertyReaderCharsetXml.class);
    List<PropertyItem> lstProps = null;
    private String loadPath;
    private String loadCharsetName;
    private boolean emptyAsNull = DEFAULT_EMPTY_AS_NULL;
    private String def_spaceNameKey = DEF_SPACE_NAME_KEY;
    private String def_configKey = DEF_CONFIG_KEY;
    private String def_nameKey = DEF_NAME_KEY;
    private String def_valueKey = DEF_VALUE_KEY;
    private String def_commitKey = DEF_COMMIT_KEY;

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @paramDefault charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetXml(String loadPath) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = "UTF-8";
        this.emptyAsNull = DEFAULT_EMPTY_AS_NULL;
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @paramDefault emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetXml(String loadPath, String charsetName) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
        this.emptyAsNull = DEFAULT_EMPTY_AS_NULL;
    }

    /**
     *
     * @param loadPath    配置文件路径，支持class:、classpath:、webroot:、webdir:路径
     * @param charsetName 文件编码方式，默认为UTF-8
     * @param emptyAsNull 属性为空是否读取为NULL，默认为true
     */
    public PropertyReaderCharsetXml(String loadPath, String charsetName, boolean emptyAsNull) {
        super();
        this.loadPath = loadPath;
        this.loadCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
        this.emptyAsNull = emptyAsNull;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            log.debug("配置获取失败，配置没有正确加载，返回默认值->" + "key: " + key + " ;value: " + defaultValue);
            return defaultValue;
        }
        String realKey = null == key ? null : key.trim();
        if (null == realKey || realKey.length() <= 0) {
            log.debug("配置获取失败，输入的key为空，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        }
        String resultVal = null;
        for (PropertyItem item : lstProps) {
            if (item.getMode() != ItemMode.KEY_VAL) {
                continue;
            }
            if (item.getKey().equals(realKey)) {
                resultVal = item.getValue();
                break;
            }
        }
        if (null == resultVal) {
            log.debug("配置获取失败，无法找到配置值，返回默认值->" + "key: " + realKey + " ;value: " + defaultValue);
            return defaultValue;
        } else {
            log.debug("获取配置成功->" + "key: " + realKey + " ;value: " + resultVal);
            return resultVal;
        }
    }

    @Override
    public String propertyToString() {
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            return null;
        }
        return lstProps.toString();
    }

    @Override
    public Map<String, String> propertyToMap() {
        if (null == lstProps) {
            loadProps(false);
        }
        if (null == lstProps) {
            return null;
        }
        try {
            Map<String, String> map = new HashMap<String, String>();
            for (PropertyItem item : lstProps) {
                if (item.getMode() != ItemMode.KEY_VAL) {
                    continue;
                }
                map.put(item.getKey(), item.getValue());
            }
            log.debug("转换后的Map数据为->" + (null == map ? "NULL" : map.toString()));
            return map;
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharsetXml.propertyToMap", e);
            return null;
        }
    }

    /**
     * 读取配置文件
     *
     * @param isForce
     */
    @Override
    synchronized public void loadProps(boolean isForce) {
        if (null == loadPath || loadPath.length() == 0) {
            lstProps = null;

        }
        if (!isForce && null != lstProps) {
            return;
        }
        try {
            log.debug("开始加载配置文件->" + loadPath);
            log.debug("开始加载配置文件->节点名:" + def_configKey + ",key属性名:" + def_nameKey
                    + ",value属性名:" + def_valueKey + ",spaceName属性名:" + def_spaceNameKey);
            String loadPathReal = WebUtils.parseRealPath(loadPath);
            File file = new File(loadPathReal);
            if (null == file || !file.exists()) {
                lstProps = new ArrayList<PropertyItem>();
                log.error("Error:PropertyReaderCharsetXml.loadProps->加载配置文件失败，配置文件不存在！");
                return;
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document d = builder.parse(file);
            // 获取根节点
            NodeList nodeList = d.getChildNodes();
            List<PropertyItem> listPrItems = new ArrayList<PropertyItem>();
            Node nodeRoot = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tmpNode = nodeList.item(i);
                if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                    nodeRoot = nodeList.item(i);
                }
            }
            parseConfig(nodeRoot);
            parseNode(listPrItems, nodeRoot, null);
            lstProps = listPrItems;
            log.debug("加载配置文件成功！" + lstProps);
        } catch (Exception e) {
            log.error("Error:PropertyReaderCharsetXml.loadProps", e);
            throw new RuntimeException("PropertyReaderCharsetXml.loadProps->加载配置文件出现异常！", e);
        }
    }

    public void parseConfig(Node rootNode) {
        NamedNodeMap rootNamedNodeMap = rootNode.getAttributes();
        String spaceNameKeyVal = parseNodeValue(rootNamedNodeMap, DEF_SPACE_NAME_KEY + "Key", null);
        String configKeyVal = parseNodeValue(rootNamedNodeMap, DEF_CONFIG_KEY + "Key", null);
        String nameKeyVal = parseNodeValue(rootNamedNodeMap, DEF_NAME_KEY + "Key", null);
        String valueKeyVal = parseNodeValue(rootNamedNodeMap, DEF_VALUE_KEY + "Key", null);
        String commitKeyVal = parseNodeValue(rootNamedNodeMap, DEF_COMMIT_KEY + "Key", null);
        this.def_spaceNameKey = StringUtils.isEmpty(spaceNameKeyVal) ? DEF_SPACE_NAME_KEY : spaceNameKeyVal;
        this.def_configKey = StringUtils.isEmpty(configKeyVal) ? DEF_CONFIG_KEY : configKeyVal;
        this.def_nameKey = StringUtils.isEmpty(nameKeyVal) ? DEF_NAME_KEY : nameKeyVal;
        this.def_valueKey = StringUtils.isEmpty(valueKeyVal) ? DEF_VALUE_KEY : valueKeyVal;
        this.def_commitKey = StringUtils.isEmpty(commitKeyVal) ? DEF_COMMIT_KEY : commitKeyVal;

    }

    public void parseNode(List<PropertyItem> lstPropertyItems, Node node, String superSpaceName) {
        if (null == node || !node.hasChildNodes()) {
            return;
        }
        NodeList nodeList = node.getChildNodes();
        String spaceName = null;
        NamedNodeMap parentNamedNodeMap = node.getAttributes();
        if (null != parentNamedNodeMap) {
            Node tmpNode = parentNamedNodeMap.getNamedItem(def_spaceNameKey);
            if (null != tmpNode) {
                spaceName = StringUtils.trim(tmpNode.getNodeValue());
            }
//			if (StringUtils.isEmpty(nameSpace) && def_configEcementName.equals(node.getNodeName())) {
//				Node tmpCfgNode = parentNamedNodeMap.getNamedItem(def_configKeyName);
//				if (null != tmpCfgNode) {
//					nameSpace = Utilx.trimString(tmpCfgNode.getNodeValue());
//				}
//			}
        }

        String fullSpaceName = parseNameSpace(superSpaceName, spaceName);
        log.debug("加载配置解析->命名空间：fullSpaceName:" + fullSpaceName
                + ",superSpaceName:" + superSpaceName + ",spaceName:" + spaceName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tmpNode = nodeList.item(i);
            if (tmpNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (def_configKey.equals(tmpNode.getNodeName())) {
                PropertyItem propertyItem = parseNodeToProperty(tmpNode, fullSpaceName);
                if (null != propertyItem && null != propertyItem.getMode()) {
                    log.debug("加载配置解析->parseNodeToProperty解析后的值为:" + propertyItem.toString());
                    lstPropertyItems.add(propertyItem);
                    Utilx.addPropertyItem(lstPropertyItems, propertyItem);
                }
                parseNode(lstPropertyItems, tmpNode, fullSpaceName);
            } else {
                parseNode(lstPropertyItems, tmpNode, fullSpaceName);
            }
        }
    }

    private PropertyItem parseNodeToProperty(Node node, String nameSpace) {
        NamedNodeMap namedNodeMap = node.getAttributes();
        if (null == namedNodeMap) {
            return null;
        }
        String key = parseNodeValue(namedNodeMap, def_nameKey, null);
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String val = parseNodeValue(namedNodeMap, def_valueKey, node);
        String commit = parseNodeValue(namedNodeMap, def_commitKey, null);

        PropertyItem propertyItem = new PropertyItem();
        if (StringUtils.isEmpty(nameSpace)) {
            propertyItem.setMode(ItemMode.KEY_VAL);
            propertyItem.setKey(key);
            propertyItem.setValue(val);
            propertyItem.setCommit(commit);
            propertyItem.setLineStr(null);
        } else {
            propertyItem.setMode(ItemMode.KEY_VAL);
            propertyItem.setKey(nameSpace + "." + key);
            propertyItem.setValue(val);
            propertyItem.setCommit(commit);
            propertyItem.setLineStr(null);
        }
        return propertyItem;
    }

    private String parseNodeValue(NamedNodeMap namedNodeMap, String nameItemKey, Node node) {
        if (null == namedNodeMap) {
            return null;
        }
        if (StringUtils.isEmpty(nameItemKey)) {
            return null;
        }
        String val = null;
        Node valueNode = namedNodeMap.getNamedItem(nameItemKey);
        if (null != valueNode) {
            val = valueNode.getNodeValue();
        } else if (null != node) {
            try {
                val = node.getTextContent();
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:PropertyReaderCharsetXml.parseNodeValue", e);
            }

        }
        return parseContentValue(val, emptyAsNull);
    }

    private String parseContentValue(String valueStr, boolean emptyAsNull) {
        if (null == valueStr || valueStr.length() <= 0) {
            if (emptyAsNull) {
                return null;
            } else {
                return valueStr;
            }
        }
        String str = valueStr.trim();
        if (str.startsWith("\"") && str.endsWith("\"") && str.length() > 1) {
            int subEnd = str.length() - 1;
            return str.substring(1, subEnd);
        } else {
            return str;
        }
    }

    private String parseNameSpace(String superSpaceName, String spaceName) {
        if (null != spaceName && (spaceName.startsWith("\\") || spaceName.startsWith("/"))) {
            String tmpVal = spaceName.substring(1);
            if (null == tmpVal) {
                return "";
            }
            return tmpVal;
        }
        if (StringUtils.isEmpty(superSpaceName) && StringUtils.isEmpty(spaceName)) {
            return "";
        } else if (StringUtils.isEmpty(superSpaceName)) {
            return spaceName;
        } else if (StringUtils.isEmpty(spaceName)) {
            return superSpaceName;
        } else {
            return superSpaceName + "." + spaceName;
        }
    }

    @Override
    public boolean putProperty(String key, String value) {
        log.error("XML模式只支持读取，不支持添加配置");
        throw new RuntimeException("XML模式只支持读取，不支持添加配置");
    }

    @Override
    public boolean removeProperty(String key) {
        log.error("XML模式只支持读取，不支持删除配置");
        throw new RuntimeException("XML模式只支持读取，不支持删除配置");
    }

    @Override
    public boolean storeProperty(String comments) {
        log.error("XML模式只支持读取，不支持存储配置");
        throw new RuntimeException("XML模式只支持读取，不支持存储配置");
    }
}
