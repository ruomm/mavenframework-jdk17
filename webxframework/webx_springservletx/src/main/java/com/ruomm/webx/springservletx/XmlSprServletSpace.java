/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月9日 下午12:50:13
 */
package com.ruomm.webx.springservletx;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "servletSpace")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlSprServletSpace {
    // 路径命名空间
    @XmlAttribute(name = "urlSpace")
    private String urlSpace;
    // 类命名空间
    @XmlAttribute(name = "packageSpace")
    private String packageSpace;
    // 默认的执行方法
    @XmlAttribute(name = "method")
    private String methodName;
    // 执行模式，ref为反射模式，其他为Spring管理模式
    @XmlAttribute(name = "mode")
    private String mode;
    @XmlElements(value = {@XmlElement(name = "servlet", type = XmlSprServletNode.class)})
    private List<XmlSprServletNode> lstServlets;

    public String getUrlSpace() {
        return urlSpace;
    }

    public void setUrlSpace(String urlSpace) {
        this.urlSpace = urlSpace;
    }

    public String getPackageSpace() {
        return packageSpace;
    }

    public void setPackageSpace(String packageSpace) {
        this.packageSpace = packageSpace;
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

    public List<XmlSprServletNode> getLstServlets() {
        return lstServlets;
    }

    public void setLstServlets(List<XmlSprServletNode> lstServlets) {
        this.lstServlets = lstServlets;
    }

    @Override
    public String toString() {
        return "XmlSprServletSpace [urlSpace=" + urlSpace + ", packageSpace=" + packageSpace + ", methodName="
                + methodName + ", mode=" + mode + ", lstServlets=" + lstServlets + "]";
    }

}