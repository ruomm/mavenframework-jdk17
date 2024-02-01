/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月17日 下午4:24:58
 */
package com.ruomm.webx.requestwrapperx;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
class XmlWapperFilter {
    // 路径命名空间
    @XmlAttribute(name = "urlSpace")
    private String urlSpace;
    @XmlElements(value = {@XmlElement(name = "contentType", type = String.class)})
    private List<String> lstContentTypes;

    public String getUrlSpace() {
        return urlSpace;
    }

    public void setUrlSpace(String urlSpace) {
        this.urlSpace = urlSpace;
    }

    public List<String> getLstContentTypes() {
        return lstContentTypes;
    }

    public void setLstContentTypes(List<String> lstContentTypes) {
        this.lstContentTypes = lstContentTypes;
    }

    @Override
    public String toString() {
        return "XmlWapperFilter [urlSpace=" + urlSpace + ", lstContentTypes=" + lstContentTypes + "]";
    }

}
