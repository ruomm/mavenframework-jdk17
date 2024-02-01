/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月17日 下午4:24:37
 */
package com.ruomm.webx.requestwrapperx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlWapperConfig {
    @XmlAttribute(name = "uriLevel")
    private String uriLevel;
    @XmlAttribute(name = "debug")
    private String debug;

    public String getUriLevel() {
        return uriLevel;
    }

    public void setUriLevel(String uriLevel) {
        this.uriLevel = uriLevel;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return "XmlWapperConfig [uriLevel=" + uriLevel + ", debug=" + debug + "]";
    }

}
