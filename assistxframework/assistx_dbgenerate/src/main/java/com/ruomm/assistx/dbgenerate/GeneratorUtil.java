package com.ruomm.assistx.dbgenerate;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.WebUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class GeneratorUtil {
    private final static Log log = LogFactory.getLog(GeneratorUtil.class);

    public static void generateDbResorce(String configXml) {
        if (StringUtils.isEmpty(configXml)) {
            log.error("Error:DbGenerateUtil.generateDbResorce->请正确输入配置文件路径！支持绝对路径、相对路径、resource文件夹里的文件名。");
            throw new RuntimeException("Error:DbGenerateUtil.generateDbResorce->请正确输入配置文件路径！支持绝对路径、相对路径、resource文件夹里的文件名。");
        }
        String configXmlPath = null;
        if (FileUtils.lastIndexOfFileSeparator(configXml) < 0) {
            if (configXml.toLowerCase().endsWith(".xml")) {
                configXmlPath = WebUtils.parseRealPathForIdea(WebUtils.PATH_TAG_USERDIR + "assistxframework/assistx_dbgenerate/src/main/java/resource/" + configXml, "assistx_dbgenerate");
            } else {
                configXmlPath = WebUtils.parseRealPathForIdea(WebUtils.PATH_TAG_USERDIR + "assistxframework/assistx_dbgenerate/src/main/java/resource/" + configXml + ".xml", "assistx_dbgenerate");
            }
        } else {
            configXmlPath = WebUtils.parseRealPathForIdea(configXml, "assistx_dbgenerate");
        }
//        System.out.println(FileUtils.readFile(configXmlPath));
        parseXmlAndCreateDir(configXmlPath);
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            File configFile = new File(configXmlPath);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            org.mybatis.generator.config.Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            log.info("DbGenerateUtil.generateDbResorce->----------------执行完毕----------------");
            System.out.println("----------------执行完毕----------------");
        } catch (Exception e) {
            log.error("Error:DbGenerateUtil.generateDbResorce", e);
        }

    }

    public static void parseXmlAndCreateDir(String configXmlPath) {
        if (StringUtils.isEmpty(configXmlPath)) {
            log.error("Error:DbGenerateUtil.parseXmlAndCreateDirAndCreateDir->解析配置文件创建文件夹失败，配置文件不存在！");
            throw new RuntimeException("Error:DbGenerateUtil.parseXmlAndCreateDir->解析配置文件创建文件夹失败，配置文件不存在！");
        }
        File file = new File(configXmlPath);
        if (null == file || !file.exists()) {
            log.error("Error:DbGenerateUtil.parseXmlAndCreateDir->解析配置文件创建文件夹失败，配置文件不存在！");
            throw new RuntimeException("Error:DbGenerateUtil.parseXmlAndCreateDir->解析配置文件创建文件夹失败，配置文件不存在！");
        }
        InputStream dIn = null;
        try {
            log.info("DbGenerateUtil.parseXmlAndCreateDir->开始加载配置文件。" + configXmlPath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            dIn = new FileInputStream(file);
            Document d = builder.parse(dIn);
            // 获取根节点
            NodeList nodeList = d.getChildNodes();
            Node nodeRoot = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tmpNode = nodeList.item(i);
                if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                    nodeRoot = nodeList.item(i);
                }
            }
            log.info("DbGenerateUtil.parseXmlAndCreateDir->根节点(generatorConfiguration)：" + nodeRoot.getNodeName());
            //获取context节点
            Node contextRoot = null;
            nodeList = nodeRoot.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tmpNode = nodeList.item(i);

                if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {
                    log.info("DbGenerateUtil.parseXmlAndCreateDir->javaModelGenerator：" + tmpNode.getNodeName());
                }
                if (tmpNode.getNodeType() == Node.ELEMENT_NODE && "context".equalsIgnoreCase(tmpNode.getNodeName())) {
                    contextRoot = nodeList.item(i);
                }
            }
            log.info("DbGenerateUtil.parseXmlAndCreateDir->节点(context)：" + contextRoot.getNodeName());
            nodeList = contextRoot.getChildNodes();
            String[] targetPathsNodesKey = new String[]{"javaModelGenerator", "sqlMapGenerator", "javaClientGenerator"};
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node tmpNode = nodeList.item(i);
                if (tmpNode.getNodeType() == Node.ELEMENT_NODE) {

                    String targetNodeKey = null;
                    for (String nodeKey : targetPathsNodesKey) {
                        if (nodeKey.equalsIgnoreCase(tmpNode.getNodeName())) {
                            targetNodeKey = nodeKey;
                        }
                    }
                    if (StringUtils.isEmpty(targetNodeKey)) {
                        continue;
                    }
                    log.info("DbGenerateUtil.parseXmlAndCreateDir->节点(" + targetNodeKey + "):" + tmpNode.getNodeName());
                    String targetProjectVal = parseTargetProjectAttribVal(tmpNode, "targetProject");
                    log.info("DbGenerateUtil.parseXmlAndCreateDir->节点属性值(" + targetNodeKey + ".targetProject):" + targetProjectVal);
                    if (StringUtils.isEmpty(targetProjectVal)) {
                        continue;
                    }
                    if (!FileUtils.isEndWithFileSeparator(targetProjectVal)) {
                        targetProjectVal = targetProjectVal + FileUtils.parseFileSeparator(targetProjectVal);
                    }
                    //创建相关文件夹
                    FileUtils.createDir(targetProjectVal);
                    log.info("DbGenerateUtil.parseXmlAndCreateDir->创建目标文件夹(" + targetNodeKey + ".targetProject):" + targetProjectVal);
                }

            }
        } catch (Exception e) {
            log.error("Error:DbGenerateUtil.parseXmlAndCreateDir", e);
            throw new RuntimeException("DbGenerateUtil.parseXmlAndCreateDir->加载配置文件出现异常！", e);
        } finally {
            if (null != dIn) {
                try {
                    dIn.close();
                } catch (Exception e) {

                }
                dIn = null;
            }
        }
    }

    public static String parseTargetProjectAttribVal(Node tmpNode, String attrKey) {
        try {
            NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
            if (null == tmpNodeMap || tmpNodeMap.getLength() < 0) {
                return null;
            }
            Node tmpAttrNode = tmpNode.getAttributes().getNamedItem(StringUtils.isEmpty(attrKey) ? "targetProject" : attrKey);
            if (null == tmpAttrNode) {
                return null;
            }
            return tmpAttrNode.getNodeValue();
        } catch (Exception e) {
            log.error("Error:DbGenerateUtil.parseTargetProjectAttribVal", e);
            return null;
        }

    }
}
