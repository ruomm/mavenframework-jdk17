package com.ruomm.javax.propertiesx;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/17 10:26
 */
class ItemStore {
    private String header;
    private String conent;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getConent() {
        return conent;
    }

    public void setConent(String conent) {
        this.conent = conent;
    }

    @Override
    public String toString() {
        return "PropertyItemStore{" +
                "header='" + header + '\'' +
                ", conent='" + conent + '\'' +
                '}';
    }
}
