package com.ruomm.javax.encryptx.rsapadding;

import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.corex.exception.JavaxCorexException;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/11/17 13:11
 */
class UtilsAarray {
    /**
     * byte[]数据合并
     *
     * @param bs1 byte[]数据1
     * @param bs2 byte[]数据2
     * @return 合并后的byte[]数据
     */
    public static byte[] arraysMerage(byte[] bs1, byte[] bs2) {
        int bs1len = null == bs1 ? 0 : bs1.length;
        int bs2len = null == bs2 ? 0 : bs2.length;
        byte[] bs = new byte[bs1len + bs2len];
        if (bs1len > 0) {
            System.arraycopy(bs1, 0, bs, 0, bs1len);
        }
        if (bs2len > 0) {
            System.arraycopy(bs2, 0, bs, bs1len, bs2len);
        }
        return bs;
    }

    /**
     * byte[]数据截取复制
     *
     * @param source byte[]数据源
     * @param start  开始位置
     * @param len    截取复制长度
     * @return byte[]数据截取复制后的结果
     */
    public static byte[] arrayCopy(byte[] source, int start, int len) {
        if (source == null) {
            throw new NullPointerException();
        } else if (start + len > source.length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            byte[] target = new byte[len];
            System.arraycopy(source, start, target, 0, len);
            return target;
        }
    }

    /**
     * byte[]数据截取复制
     *
     * @param source byte[]数据源
     * @param start  开始位置
     * @return byte[]数据截取复制后的结果
     */
    public static byte[] arrayCopy(byte[] source, int start) {
        if (source == null) {
            throw new NullPointerException();
        } else if (start > source.length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int len = source.length - start;
            byte[] target = new byte[len];
            System.arraycopy(source, start, target, 0, len);
            return target;
        }
    }

    public static byte[] arrayRemoveZero(byte[] source, boolean zeroStartPadding, boolean zeroEndPadding) {
        int sourceLength = null == source ? 0 : source.length;
        if (sourceLength <= 0) {
            return source;
        }
        if (!zeroStartPadding && !zeroEndPadding) {
            return source;
        }
        if (zeroEndPadding) {
            int endCount = 0;
            if (zeroEndPadding) {
                for (int i = sourceLength - 1; i >= 0; i--) {
                    byte tmpByte = source[i];
                    if (tmpByte == 0) {
                        endCount = sourceLength - i;
                    } else {
                        break;
                    }
                }
            }
            int len = sourceLength - endCount;
            if (len <= 0) {
                return new byte[0];
            }
            if (endCount >= sourceLength) {
                return new byte[0];
            } else {
                byte[] result = new byte[len];
                System.arraycopy(source, 0, result, 0, len);
                return result;
            }
        } else {
            int startCount = 0;
            if (zeroStartPadding) {
                for (int i = 0; i < sourceLength; i++) {
                    byte tmpByte = source[i];
                    if (tmpByte == 0) {
                        startCount = i + 1;
                    } else {
                        break;
                    }
                }
            }
            int len = sourceLength - startCount;
            if (len <= 0) {
                return new byte[0];
            } else {
                byte[] result = new byte[len];
                System.arraycopy(source, startCount, result, 0, len);
                return result;
            }
        }
    }

    public static byte[] generatePaddingSizeBytes(int dataByteSize, int encryptByteSize) {
        byte[] resultByte = arraysMerage(HexUtils.shortToBytes((short) dataByteSize), HexUtils.shortToBytes((short) encryptByteSize));
        if (null == resultByte || resultByte.length != 4) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "分段加密生成拼接的原始数据长度和加密数据长度的paddingSize错误");
        }
        return resultByte;
    }

    public static int parsePaddingDataSize(byte[] source) {
        if (null == source || source.length <= 4) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "解密时候解析拼接的原始数据长度错误");
        }
        int sourceLength = source.length;
        int dataSize = HexUtils.bytesToShort(arrayCopy(source, sourceLength - 4, 2));
        if (dataSize < 0 || dataSize > 4096) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "解密时候解析拼接的原始数据长度错误-解析后的原始数据长度小于0或大于4096");
        }
        return dataSize;
    }

    public static int parsePaddingEncryptSize(byte[] source) {
        if (null == source || source.length <= 4) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "解密时候解析拼接的加密数据长度错误");
        }
        int sourceLength = source.length;
        int encryptSize = HexUtils.bytesToShort(arrayCopy(source, sourceLength - 2, 2));
        if (encryptSize < 0 || encryptSize > 4096) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "解密时候解析拼接的加密数据长度错误");
        }
        return encryptSize;
    }
}
