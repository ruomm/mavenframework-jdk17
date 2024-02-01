/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月4日 下午2:40:23
 */
package com.ruomm.javax.encryptx.dal;

import java.security.KeyPair;

public class KeyPairStoreFormat {
    private String publicKey;
    private String privateKey;
    private int keySize;
    private KeyPair keyPair;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public String toString() {
        return "KeyPairStoreFormat{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", keySize=" + keySize +
                ", keyPair=" + keyPair +
                '}';
    }
}
