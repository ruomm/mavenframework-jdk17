package com.ruomm.javax.encryptx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/9/28 15:51
 */
class UtilsApp {

    public static byte[] arraysMerage(List<byte[]> listData) {
        byte[] newData = null;
        if (null != listData && listData.size() > 0) {
            int tmpDesCount = 0;
            for (byte[] tmpItemByte : listData) {
                if (null != tmpItemByte && tmpItemByte.length > 0) {
                    tmpDesCount = tmpDesCount + tmpItemByte.length;
                }
            }
            newData = new byte[tmpDesCount];
            int tmpDesPos = 0;
            for (byte[] tmpItemByte : listData) {
                if (null != tmpItemByte && tmpItemByte.length > 0) {
                    System.arraycopy(tmpItemByte, 0, newData, tmpDesPos, tmpItemByte.length);
                    tmpDesPos = tmpDesPos + tmpItemByte.length;
                }
            }
        }
        return newData;
    }

    /**
     * byte[]数据截取复制
     *
     * @param source         byte[]数据源
     * @param fillStartCount 开始位置截取长度
     * @param fillEndCount   结束位置截取长度
     * @return byte[]数据截取复制后的结果
     */
    public static byte[] arrayCopy(byte[] source, int fillStartCount, int fillEndCount) {
        if (source == null || source.length <= 0) {
            return source;
        }
        int startCount = fillStartCount > 0 ? fillStartCount : 0;
        int endCount = fillEndCount > 0 ? fillEndCount : 0;
        int len = source.length - startCount - endCount;
        if (len == source.length) {
            return source;
        }
        if (len <= 0) {
            return source;
        }
        byte[] result = new byte[len];
        System.arraycopy(source, startCount, result, 0, len);
        return result;
    }

    public static byte[] arrayRemoveZero(byte[] source, boolean zeroStartPadding, boolean zeroEndPadding) {
        int sourceLength = null == source ? 0 : source.length;
        if (sourceLength <= 0) {
            return source;
        }
        if (!zeroStartPadding && !zeroEndPadding) {
            return source;
        }
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
        int len = source.length - startCount - endCount;
        if (len == source.length) {
            return source;
        }
        if (len <= 0) {
            return source;
        }
        byte[] result = new byte[len];
        System.arraycopy(source, startCount, result, 0, len);
        return result;
    }

    public static void verifyDataResult(byte[] tmpBytes, boolean encryptFlag, boolean validNoEmpty) {
        if (null == tmpBytes) {
            if (encryptFlag) {
                throw new RuntimeException("分段加密时候加密结果为null");
            } else {
                throw new RuntimeException("分段解密时候解密结果为null");
            }
        } else if (tmpBytes.length <= 0 && validNoEmpty) {
            if (encryptFlag) {
                throw new RuntimeException("分段加密时候加密结果为空");
            } else {
                throw new RuntimeException("分段解密时候解密结果为空");
            }
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @return
     * @throws IOException
     */
//    public static String readKey(InputStream in) {
//        return readKey(in, null);
//    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String readKey(InputStream in, String charsetName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            int tagCount = 0;
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            br = new BufferedReader(new InputStreamReader(in, charset));
            String readLineStr = null;
            final StringBuilder sbCotent = new StringBuilder();
            boolean isFirst = true;
            while ((readLineStr = br.readLine()) != null) {
                String tmpContent = isFirst ? StringUtils.stringToNoBom(readLineStr) : readLineStr;
                isFirst = false;
                if (null == tmpContent || tmpContent.length() <= 0) {
                    continue;
                }
                if (tmpContent.startsWith("---")) {
                    tagCount++;
                    continue;
                }
                if (tagCount >= 2) {
                    break;
                }
                sb.append(tmpContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sb.setLength(0);
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }
}
