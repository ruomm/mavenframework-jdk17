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

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlSprServletConfig {
    @XmlAttribute(name = "mode")
    private String mode;
    @XmlAttribute(name = "method")
    private String methodName;
    @XmlAttribute(name = "uriLevel")
    private String uriLevel;
    @XmlAttribute(name = "parse")
    private String parse;
    @XmlAttribute(name = "debug")
    private String debug;
    @XmlAttribute(name = "reloadUri")
    private String reloadUri;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUriLevel() {
        return uriLevel;
    }

    public void setUriLevel(String uriLevel) {
        this.uriLevel = uriLevel;
    }

    public String getParse() {
        return parse;
    }

    public void setParse(String parse) {
        this.parse = parse;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getReloadUri() {
        return reloadUri;
    }

    public void setReloadUri(String reloadUri) {
        this.reloadUri = reloadUri;
    }

    @Override
    public String toString() {
        return "XmlSprServletConfig [mode=" + mode + ", methodName=" + methodName + ", uriLevel=" + uriLevel
                + ", parse=" + parse + ", debug=" + debug + ", reloadUri=" + reloadUri + "]";
    }

}