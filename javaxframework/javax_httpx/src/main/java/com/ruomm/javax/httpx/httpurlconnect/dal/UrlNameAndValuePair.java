/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月12日 下午3:16:17
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

class UrlNameAndValuePair {
    private String name;
    private String value;

    public UrlNameAndValuePair() {
        super();
    }

    public UrlNameAndValuePair(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UrlNameAndValuePair [name=" + name + ", value=" + value + "]";
    }

}