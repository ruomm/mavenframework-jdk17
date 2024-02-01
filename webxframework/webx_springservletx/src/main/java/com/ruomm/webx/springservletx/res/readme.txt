--------依赖说明--------
	<dependency>
		<groupId>jakarta.servlet</groupId>
		<artifactId>jakarta.servlet-api</artifactId>
	</dependency>
--------初始化说明--------
Web容器web.xml声明com.ruomm.webx.springservletx.SpringServletFilter过滤器，并设置init-param参数configPath的路径。
	configPath为空加载默认路径class:config/filterconfig/spring_servlet_config.xml
	configPath支持类路径class:和classpath:、支持Web容器路径webroot:和webdir、支持文件路径file://和file:、支持绝对路径。
--------通用配置说明--------
通用:mode->多个节点继承，以从外层到内层最后一个非空的为准，ref:接口方法模式,其他：反射方法模式
	mode若是接口方法模式调用com.ruomm.webx.springservletx.SpringServletProgress来获取方法执行，若是反射方法模式调用类反射方法来执行。
通用:method->多个节点继承，以从外层到内层最后一个非空的为准。
	若是mode为反射方法模式则method起作用，调用该method处理，默认为doProgress，若是mode为接口方法模式则method不起作用。
--------config配置说明--------
config:mode->同通用:mode定义
config:method->同通用:method定义
config:uriLevel->请求URI解析为真实接口URI的层级，默认为1，若是容器直接在域名下，输入0
config:parse->依据SpringName获取Bean对象的实现类，必须填写实现com.ruomm.webx.springservletx.SpringServletAware的类名称
config:reloadUri->重新加载配置的URI路径，没有则不能重新加载
config:debug->是否调试模式，默认为false，需要打印接口日志输入true
--------servletSpace配置说明--------
servletSpace:mode->同通用:mode定义
servletSpace:method->同通用:method定义
servletSpace:packageSpace->不为空则校验取得的执行对象是否属于该package
servletSpace:urlSpace->接口请求的根路径
servletSpace:servlet->不为空依据列表执行，为空则依据请求Uri解析自动匹配执行
--------servlet配置说明--------
servlet:mode->同通用:mode定义
servlet:method->同通用:method定义
servlet:servletName->具体的servlet路径，若为空依据springName自动生成。
	依据springName自动生成时候带Servlet、Action、Service、Controller、Progress的SpringName会映射自身名称和去掉后缀后的名称的servlet的路径
servlet:springName->具体的spring名称，为空则依据servlet自动解析生成。若是带有多个‘.’的依据class来实例化，否则依据名称来实例化
	若是springName和servletName全部为'*'时候，除了列表匹配的外，其他依据依据请求Uri解析自动匹配执行
--------配置说明结束--------

--------配置XML示例--------
<?xml version="1.0" encoding="UTF-8"?>
<SpringServlet>
    <config mode="ref" method="doProgress" uriLevel="1" parse="com.ruomm.webx.springservletx.SpringServletAware" debug="true" reloadUri="servlet/reload"/>
    <servletSpace urlSpace="/servlet/demo01" packageSpace="com.ruomm.app.springservletframework01" mode="interface"/>
    <servletSpace urlSpace="/servlet/demo02" method="progress" mode="ref">
        <servlet servletName="node01Api" springName="node01Servlet" method="progress05" mode="interface"/>
        <servlet servletName="node02Servlet" method="progress02"/>
        <servlet springName="node03Servlet" mode="interface"/>
        <servlet servletName="node04Api" springName="node04Servlet"/>
        <servlet servletName="*" springName="*"/>
    </servletSpace>
</SpringServlet>