package com.ruomm.javax.encryptx.dal;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/28 12:08
 */
public class KeyIvStoreFormat {
    private String key;
    private String iv;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public String toString() {
        return "KeyIvStoreFormat{" +
                "key='" + key + '\'' +
                ", iv='" + iv + '\'' +
                '}';
    }
}
