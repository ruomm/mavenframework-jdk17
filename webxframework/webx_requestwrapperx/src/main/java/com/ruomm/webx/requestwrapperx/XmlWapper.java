/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月17日 下午4:27:38
 */
package com.ruomm.webx.requestwrapperx;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "RepeatWapper")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlWapper {
    @XmlElement(name = "config")
    private XmlWapperConfig config;
    @XmlElements(value = {@XmlElement(name = "filter", type = XmlWapperFilter.class)})
    private List<XmlWapperFilter> listFilters;

    public XmlWapperConfig getConfig() {
        return config;
    }

    public void setConfig(XmlWapperConfig config) {
        this.config = config;
    }

    public List<XmlWapperFilter> getListFilters() {
        return listFilters;
    }

    public void setListFilters(List<XmlWapperFilter> listFilters) {
        this.listFilters = listFilters;
    }

    @Override
    public String toString() {
        return "XmlWapper [config=" + config + ", listFilters=" + listFilters + "]";
    }

}
