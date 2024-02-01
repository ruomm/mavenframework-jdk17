package com.ruomm.javax.encryptx.rsapadding;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/30 17:08
 */
public class RsaPaddingHelper implements PaddingHelper {

    /**
     * 分段加密时候分割加密byte[]数据的偏移量，总长度=keySize/8-偏移量
     */
    private int encryptOffset;
    /**
     * 分段解密时候分割解密byte[]数据的偏移量，总长度=keySize/8+偏移量+writeBlockSize长度
     */
    private int decryptOffset;

    private boolean writeBlockSize;

    private boolean zeroPaddingStart;
    private boolean zeroPaddingEnd;

    public RsaPaddingHelper(int encryptOffset, int decryptOffset) {
        this(encryptOffset, decryptOffset, false, false, false);
    }

    public RsaPaddingHelper(int encryptOffset, int decryptOffset, boolean writeBlockSize) {
        this(encryptOffset, decryptOffset, writeBlockSize, false, false);
    }

    public RsaPaddingHelper(int encryptOffset, int decryptOffset, boolean writeBlockSize, boolean zeroPaddingStart, boolean zeroPaddingEnd) {
        this.encryptOffset = encryptOffset;
        this.decryptOffset = decryptOffset;
        this.writeBlockSize = writeBlockSize;
        this.zeroPaddingStart = zeroPaddingStart;
        this.zeroPaddingEnd = zeroPaddingEnd;
    }


    @Override
    public int blockSizeEncrypt(int keySize) {
        return keySize / 8 - this.encryptOffset;
    }

    @Override
    public int blockSizeDecrypt(int keySize) {
        if (writeBlockSize()) {
            return keySize / 8 + this.decryptOffset + 4;
        } else {
            return keySize / 8 + this.decryptOffset;
        }
    }

    @Override
    public boolean writeBlockSize() {
        return this.writeBlockSize;
    }

    @Override
    public boolean zeroPaddingStart() {
        return this.zeroPaddingStart;
    }

    @Override
    public boolean zeroPaddingEnd() {
        return this.zeroPaddingEnd;
    }

    /**
     * 加密数据封包
     *
     * @param source  原始数据
     * @param encData 加密数据
     * @return 封包后数据
     */
    public byte[] doPadding(byte[] source, byte[] encData) {
        if (null == encData || encData.length <= 0) {
            return encData;
        }
        if (writeBlockSize()) {
            // 写入加密数据长度和原始数据长度
            int sourceLength = null == source ? 0 : source.length;
            int encDataLength = null == encData ? 0 : encData.length;
            byte[] paddingByte = UtilsAarray.generatePaddingSizeBytes(sourceLength, encDataLength);
            return UtilsAarray.arraysMerage(encData, paddingByte);
        } else {
            return encData;
        }
    }

    /**
     * 加密数据解包
     *
     * @param blockEncData 封包后的加密数据
     * @return 解包后加密数据
     */
    public byte[] unPadding(byte[] blockEncData) {
        if (!writeBlockSize()) {
            return blockEncData;
        } else if (null == blockEncData || blockEncData.length < 4) {
            return blockEncData;
        } else {
            return UtilsAarray.arrayCopy(blockEncData, 0, blockEncData.length - 4);
//            int paddingSize = UtilsAarray.parsePaddingEncryptSize(blockEncData);
//            if (paddingSize < 0 || paddingSize >= blockEncData.length) {
//                return blockEncData;
//            } else {
//                return UtilsAarray.arrayCopy(blockEncData, paddingSize);
//            }
        }
    }

    /**
     * 解密数据解包
     *
     * @param blockEncData 解密数据
     * @return 解包后数据
     */
    public byte[] doDecrypt(byte[] blockEncData, byte[] decData) {
        if (!writeBlockSize()) {
            return UtilsAarray.arrayRemoveZero(decData, this.zeroPaddingStart(), this.zeroPaddingEnd());
        } else {
            if (null == decData || decData.length <= 0) {
                return decData;
            }
            int realDataSize = UtilsAarray.parsePaddingDataSize(blockEncData);
            if (decData.length <= realDataSize) {
                return decData;
            }
            if (this.zeroPaddingEnd()) {
                return UtilsAarray.arrayCopy(decData, 0, realDataSize);
            } else {
                return UtilsAarray.arrayCopy(decData, decData.length - realDataSize);
            }
        }
    }


}
