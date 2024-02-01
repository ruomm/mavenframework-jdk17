/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月9日 下午12:50:13
 */
package com.ruomm.webx.springservletx;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "SpringServlet")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlSprServlet {
    @XmlElement(name = "config")
    private XmlSprServletConfig config;
    @XmlElements(value = {@XmlElement(name = "servletSpace", type = XmlSprServletSpace.class)})
    private List<XmlSprServletSpace> listServletSpaces;

    public XmlSprServletConfig getConfig() {
        return config;
    }

    public void setConfig(XmlSprServletConfig config) {
        this.config = config;
    }

    public List<XmlSprServletSpace> getListServletSpaces() {
        return listServletSpaces;
    }

    public void setListServletSpaces(List<XmlSprServletSpace> listServletSpaces) {
        this.listServletSpaces = listServletSpaces;
    }

    @Override
    public String toString() {
        return "XmlSprServlet [config=" + config + ", listServletSpaces=" + listServletSpaces + "]";
    }

}