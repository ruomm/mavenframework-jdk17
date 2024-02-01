/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月9日 下午12:50:13
 */
package com.ruomm.webx.springservletx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "servlet")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlSprServletNode {
    // servlet类的名称
    @XmlAttribute(name = "servletName")
    private String servletName;
    // spring的类名称
    @XmlAttribute(name = "springName")
    private String springName;
    // 执行方法
    @XmlAttribute(name = "method")
    private String methodName;
    // 执行模式，ref为反射模式，其他为Spring管理模式
    @XmlAttribute(name = "mode")
    private String mode;

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getSpringName() {
        return springName;
    }

    public void setSpringName(String springName) {
        this.springName = springName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "XmlSprServletNode [servletName=" + servletName + ", springName=" + springName + ", methodName="
                + methodName + ", mode=" + mode + "]";
    }

}