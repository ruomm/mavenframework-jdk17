/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月3日 上午11:15:28
 */
package com.ruomm.javax.propertiesx.loader;

import java.util.Map;

class PropertyLoaderReuslt {
    private Map<String, String> properyMap;
    private String active;
    private String append;
    private String inclues;

    public Map<String, String> getProperyMap() {
        return properyMap;
    }

    public void setProperyMap(Map<String, String> properyMap) {
        this.properyMap = properyMap;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAppend() {
        return append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    public String getInclues() {
        return inclues;
    }

    public void setInclues(String inclues) {
        this.inclues = inclues;
    }

    @Override
    public String toString() {
        return "PropertyLoaderReuslt [properyMap=" + properyMap + ", active=" + active + ", append=" + append
                + ", inclues=" + inclues + "]";
    }

}
