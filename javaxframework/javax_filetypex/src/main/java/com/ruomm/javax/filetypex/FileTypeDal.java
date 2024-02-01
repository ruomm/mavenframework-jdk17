package com.ruomm.javax.filetypex;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/9 11:47
 */
public class FileTypeDal {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FileTypeDal{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
