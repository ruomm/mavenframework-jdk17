package com.ruomm.javax.encryptx.rsapadding;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/30 15:24
 */
public interface PaddingHelper {

    /**
     * @param keySize 秘钥长度
     * @return 加密的每个单元的数据大小
     */
    public int blockSizeEncrypt(int keySize);

    // 解密的每个单元的数据大小

    /**
     * @param keySize 秘钥长度
     * @return 解密的每个单元的数据大小
     */
    public int blockSizeDecrypt(int keySize);

    /**
     * @return 是否写入原始数据长度到尾部
     */
    public boolean writeBlockSize();

    /**
     * @return 加密函数是否拼接0到头部以补充不足
     */
    public boolean zeroPaddingStart();

    /**
     * @return 加密函数是否拼接0到尾部以补充不足
     */
    public boolean zeroPaddingEnd();

    /**
     * 加密数据封包
     *
     * @param source  原始数据
     * @param encData 加密数据
     * @return 封包后数据
     */
    byte[] doPadding(byte[] source, byte[] encData);

    /**
     * 加密数据解包
     *
     * @param blockEncData 封包后的加密数据
     * @return 解包后加密数据
     */
    byte[] unPadding(byte[] blockEncData);

    /**
     * 解密数据解包
     *
     * @param blockEncData 封包后的加密数据
     * @param decData      解密数据
     * @return 解包后数据
     */
    byte[] doDecrypt(byte[] blockEncData, byte[] decData);
}
