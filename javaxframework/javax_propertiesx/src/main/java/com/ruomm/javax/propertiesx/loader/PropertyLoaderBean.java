/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年9月3日 上午10:28:34
 */
package com.ruomm.javax.propertiesx.loader;

class PropertyLoaderBean {
    private String active;
    private String activePath;
    private String inclues;
    private String extension;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActivePath() {
        return activePath;
    }

    public void setActivePath(String activePath) {
        this.activePath = activePath;
    }

    public String getInclues() {
        return inclues;
    }

    public void setInclues(String inclues) {
        this.inclues = inclues;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "PropertyLoaderBean [active=" + active + ", activePath=" + activePath + ", inclues=" + inclues
                + ", extension=" + extension + "]";
    }

}
