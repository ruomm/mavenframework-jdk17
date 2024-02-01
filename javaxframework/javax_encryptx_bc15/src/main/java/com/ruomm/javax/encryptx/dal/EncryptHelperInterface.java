/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月2日 下午2:40:15
 */
package com.ruomm.javax.encryptx.dal;

public interface EncryptHelperInterface {
    public String generateRandomKey(int keySize);

    public String formatKey(byte[] keyData);

    public byte[] restoreRandomKey(String keyStr);

    public String encodeToString(byte[] binaryData);

    public byte[] decodeToByte(String srcStr);
}
