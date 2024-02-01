/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月11日 下午12:49:53
 */
package com.ruomm.webx.springservletx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;

public class SpringServletHelper {
    private final static Log log = LogFactory.getLog(SpringServletHelper.class);

    public static String xmlHelpPrint() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------依赖说明--------").append("\r\n");

        sb.append("<dependency>" + "<groupId>jakarta.servlet</groupId>" + "<artifactId>jakarta.servlet-api</artifactId>"
                + "</dependency>").append("\r\n");

        sb.append("--------初始化说明--------").append("\r\n");
        sb.append("Web容器web.xml声明com.ruomm.webx.springservletx.SpringServletFilter过滤器，并设置init-param参数configPath的路径。")
                .append("\r\n");
        sb.append("\tconfigPath为空加载默认路径class:config/filterconfig/spring_servlet_config.xml").append("\r\n");
        sb.append("\tconfigPath支持类路径class:和classpath:、支持Web容器路径webroot:和webdir、支持文件路径file://和file:、支持绝对路径。")
                .append("\r\n");
        sb.append("--------通用配置说明--------").append("\r\n");
        sb.append("通用:mode->多个节点继承，以从外层到内层最后一个非空的为准，ref:接口方法模式,其他：反射方法模式").append("\r\n");
        sb.append("\tmode若是接口方法模式调用com.ruomm.webx.springservletx.SpringServletProgress来获取方法执行，若是反射方法模式调用类反射方法来执行。")
                .append("\r\n");
        sb.append("通用:method->多个节点继承，以从外层到内层最后一个非空的为准。").append("\r\n");
        sb.append("\t若是mode为反射方法模式则method起作用，调用该method处理，默认为doProgress，若是mode为接口方法模式则method不起作用。").append("\r\n");
        sb.append("--------config配置说明--------").append("\r\n");
        sb.append("config:mode->同通用:mode定义").append("\r\n");
        sb.append("config:method->同通用:method定义").append("\r\n");
        sb.append("config:uriLevel->请求URI解析为真实接口URI的层级，默认为1，若是容器直接在域名下，输入0").append("\r\n");
        sb.append("config:parse->依据SpringName获取Bean对象的实现类，必须填写实现com.ruomm.webx.springservletx.SpringServletAware的类名称")
                .append("\r\n");
        sb.append("config:reloadUri->重新加载配置的URI路径，没有则不能重新加载").append("\r\n");
        sb.append("config:debug->是否调试模式，默认为false，需要打印接口日志输入true").append("\r\n");
        sb.append("--------servletSpace配置说明--------").append("\r\n");
        sb.append("servletSpace:mode->同通用:mode定义").append("\r\n");
        sb.append("servletSpace:method->同通用:method定义").append("\r\n");
        sb.append("servletSpace:packageSpace->不为空则校验取得的执行对象是否属于该package").append("\r\n");
        sb.append("servletSpace:urlSpace->接口请求的根路径").append("\r\n");
        sb.append("servletSpace:servlet->不为空依据列表执行，为空则依据请求Uri解析自动匹配执行").append("\r\n");
        sb.append("--------servlet配置说明--------").append("\r\n");
        sb.append("servlet:mode->同通用:mode定义").append("\r\n");
        sb.append("servlet:method->同通用:method定义").append("\r\n");
        sb.append("servlet:servletName->具体的servlet路径，若为空依据springName自动生成。").append("\r\n");
        sb.append(
                        "\t依据springName自动生成时候带Servlet、Action、Service、Controller、Progress的SpringName会映射自身名称和去掉后缀后的名称的servlet的路径")
                .append("\r\n");
        sb.append("servlet:springName->具体的spring名称，为空则依据servlet自动解析生成。若是带有多个‘.’的依据class来实例化，否则依据名称来实例化").append("\r\n");
        sb.append("\t若是springName和servletName全部为'*'时候，除了列表匹配的外，其他依据依据请求Uri解析自动匹配执行").append("\r\n");
        sb.append("--------配置说明结束--------").append("\r\n");
        log.debug(sb.toString());
        XmlSprServlet xmlSprServlet = new XmlSprServlet();
        xmlSprServlet.setConfig(new XmlSprServletConfig());

        xmlSprServlet.getConfig().setDebug("true");
        xmlSprServlet.getConfig().setMethodName("doProgress");
        xmlSprServlet.getConfig().setMode(SprServletHelper.Mode_Ref);
        xmlSprServlet.getConfig().setParse(SpringServletAware.class.getName());
        xmlSprServlet.getConfig().setReloadUri("servlet/reload");
        xmlSprServlet.getConfig().setUriLevel("1");
        xmlSprServlet.setListServletSpaces(new ArrayList<XmlSprServletSpace>());
        XmlSprServletSpace space01 = new XmlSprServletSpace();
        space01.setMode(SprServletHelper.Mode_Interface);
        space01.setMethodName(null);
        space01.setPackageSpace("com.ruomm.app.springservletframework01");
        space01.setUrlSpace("/servlet/demo01");
        space01.setLstServlets(null);
        XmlSprServletSpace space02 = new XmlSprServletSpace();
        space02.setMode(SprServletHelper.Mode_Ref);
        space02.setMethodName("progress");
        space02.setPackageSpace(null);
        space02.setUrlSpace("/servlet/demo02");
        space02.setLstServlets(null);
        XmlSprServletNode node01 = new XmlSprServletNode();
        node01.setMethodName("progress01");
        node01.setMode(SprServletHelper.Mode_Interface);
        node01.setServletName("node01Api");
        node01.setSpringName("node01Servlet");
        XmlSprServletNode node02 = new XmlSprServletNode();
        node02.setMethodName("progress02");
        node02.setServletName("node02Servlet");
        XmlSprServletNode node03 = new XmlSprServletNode();
        node03.setMode(SprServletHelper.Mode_Interface);
        node03.setSpringName("node03Servlet");
        XmlSprServletNode node04 = new XmlSprServletNode();
        node04.setServletName("node04Api");
        node04.setSpringName("node04Servlet");
        XmlSprServletNode node05 = new XmlSprServletNode();
        node05.setServletName("*");
        node05.setSpringName("*");
        node01.setMethodName("progress05");
        node01.setMode(SprServletHelper.Mode_Interface);
        space02.setLstServlets(new ArrayList<XmlSprServletNode>());
        space02.getLstServlets().add(node01);
        space02.getLstServlets().add(node02);
        space02.getLstServlets().add(node03);
        space02.getLstServlets().add(node04);
        space02.getLstServlets().add(node05);
        xmlSprServlet.getListServletSpaces().add(space01);
        xmlSprServlet.getListServletSpaces().add(space02);
        try {
            JAXBContext cxt = JAXBContext.newInstance(xmlSprServlet.getClass());
            Marshaller marshaller = cxt.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
//			marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\"encoding=\"UTF-8\"?>");

            StringWriter writer = new StringWriter();
            marshaller.marshal(xmlSprServlet, writer);
            String xml = writer.toString().replace(" standalone=\"yes\"", "");
            log.debug("--------配置XML示例--------");
            log.debug(xml);
            return xml;
        } catch (Exception e) {
            log.error("Error:xmlHelpPrint", e);
            log.debug("--------配置XML示例--------");
            log.debug("无法生成示例，可能需要引入相关Jar包");
            return "";
        }

    }
}