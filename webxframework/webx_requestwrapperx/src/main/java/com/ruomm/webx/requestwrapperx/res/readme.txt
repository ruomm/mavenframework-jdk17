--------依赖说明--------
	<dependency>
		<groupId>javax.servlet</groupId>
		<artifactId>javax.servlet-api</artifactId>
	</dependency>
--------初始化说明--------
Web容器web.xml声明com.ruomm.webx.requestwrapperx.PostRepeatReaderFilter过滤器，并设置init-param参数。
	init-param参数包含configPath、uriLevel、debug、uris、types。
	configPath为空时为参数配置模式其他参数起作用；
	configPath不为空时为XML文件配置模式，其他参数不起作用。

--------通用配置说明--------
参数配置模式说明:
	uriLevel：请求URI解析为真实接口URI的层级，默认为1，若是容器直接在域名下，输入0
	debug：是否调试模式，默认为false，需要打印接口日志输入true
	uris：匹配的URL路径前缀路径。*为通用匹配
	types：匹配的contentType类型,多个类型以英文逗号(,)分隔
XML文件配置模式说明：
	config:uriLevel->请求URI解析为真实接口URI的层级，默认为1，若是容器直接在域名下，输入0
	config:debug->是否调试模式，默认为false，需要打印接口日志输入true
	filter:urlSpace->匹配的URL路径前缀路径。*为通用匹配
	filter:contentType->匹配的contentType类型类别，可以存在多个

--------配置XML示例--------
<?xml version="1.0" encoding="UTF-8"?>
<RepeatWapper>
    <config uriLevel="1" debug="true"/>
    <filter urlSpace="*">
	<contentType>application/json</contentType>
	<contentType>application/xml</contentType>
	<contentType>text/json</contentType>
	<contentType>text/xml</contentType>
    </filter>
    <filter urlSpace="api/demo01/">
	<contentType>application/xml</contentType>
	<contentType>text/json</contentType>
	<contentType>text/xml</contentType>
    </filter>
    <filter urlSpace="/api/demo02/">
		<contentType>application/json</contentType>
		<contentType>text/plain</contentType>
	<contentType>text/json</contentType>
	<contentType>text/xml</contentType>
    </filter>
</RepeatWapper>