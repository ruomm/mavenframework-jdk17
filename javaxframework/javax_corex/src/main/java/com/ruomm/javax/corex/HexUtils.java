/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月1日 上午11:03:21
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.nio.charset.Charset;

public class HexUtils {
    private final static Log log = LogFactory.getLog(HexUtils.class);
    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private final static char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private final static char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    /**
     * 判断是否16进制编码字符串
     *
     * @param str 字符串
     * @return 是否16进制编码字符串
     * @paramDefault lengthValid 是否验证长度，默认true
     */
    public static boolean isHexStr(String str) {
        return isHexStr(str, true);
    }

    /**
     * 判断是否16进制编码字符串
     *
     * @param str         字符串
     * @param lengthValid 是否验证长度，默认true
     * @return 是否16进制编码字符串
     */
    public static boolean isHexStr(String str, boolean lengthValid) {
        if (null == str || str.length() <= 0) {
            return false;
        }
        if (lengthValid) {
            if (str.length() % 2 != 0) {
                return false;
            }
        }
        boolean hexFlag = true;
        for (char charVal : str.toCharArray()) {
            boolean tmpBool = false;
            if (charVal >= '0' && charVal <= '9') {
                tmpBool = true;
            } else if (charVal >= 'A' && charVal <= 'F') {
                tmpBool = true;
            } else if (charVal >= 'a' && charVal <= 'f') {
                tmpBool = true;
            }
            if (!tmpBool) {
                hexFlag = false;
                break;
            }
        }
        return hexFlag;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str         待转换的ASCII字符串
     * @param spaceFormat 是否使用空格格式化显示
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String encodeString(String str, String charsetName, boolean spaceFormat) {
        if (null == str) {
            return null;
        }
        try {
//            char[] chars = "0123456789ABCDEF".toCharArray();
//            StringBuilder sb = new StringBuilder("");
//            Charset charset = CharsetUtils.parseRealCharset(charsetName,Charset.forName("UTF-8"));
//            byte[] bs = str.getBytes(charset);
//            int bit;
//
//            for (int i = 0; i < bs.length; i++) {
//                if (spaceFormat && i > 0) {
//                    sb.append(StringUtils.STR_SPACE_VALUE);
//                }
//                bit = (bs[i] & 0x0f0) >> 4;
//                sb.append(chars[bit]);
//                bit = bs[i] & 0x0f;
//                sb.append(chars[bit]);
//            }
//            return sb.toString().trim();
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] bs = str.getBytes(charset);
            return encodeToString(bs, spaceFormat);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:str2HexStr", e);
            return null;
        }
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String encodeString(String str, String charsetName) {
        return encodeString(str, charsetName, false);
    }

    public static String encodeString(String str) {
        return encodeString(str, null, false);
    }

    /**
     * 十六进制转换字符串
     *
     * @param hexStr      Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @param spaceFormat 是否使用空格格式化显示
     * @return String 对应的字符串
     */
    public static String decodeString(String hexStr, String charsetName, boolean spaceFormat) {
        if (null == hexStr) {
            return null;
        }
        try {
            String realHexStr;
            if (spaceFormat) {
                realHexStr = hexStr.replace(StringUtils.STR_SPACE_VALUE, "");
            } else {
                realHexStr = hexStr;
            }
//            String str = "0123456789ABCDEF";
//            char[] hexs = realHexStr.toUpperCase().toCharArray();
//            byte[] bytes = new byte[hexs.length / 2];
//            int n;
//
//            for (int i = 0; i < bytes.length; i++) {
//                n = str.indexOf(hexs[2 * i]) * 16;
//                n += str.indexOf(hexs[2 * i + 1]);
//                bytes[i] = (byte) (n & 0xff);
//            }
//            Charset charset = CharsetUtils.parseRealCharset(charsetName,Charset.forName("UTF-8"));
//            return new String(bytes, charset);
            byte[] bytes = decode(realHexStr, spaceFormat);
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            return new String(bytes, charset);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:hexStr2Str", e);
            return null;
        }
    }

    /**
     * 十六进制转换字符串
     *
     * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String decodeString(String hexStr, String charsetName) {
        return decodeString(hexStr, charsetName, false);
    }

    public static String decodeString(String hexStr) {
        return decodeString(hexStr, null, false);
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String encodeToString(byte[] b, boolean spaceFormat) {
        if (null == b) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            if (spaceFormat && n > 0) {
                sb.append(StringUtils.STR_SPACE_VALUE);
            }
            String stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
        }
        return sb.toString().trim();
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String encodeToString(byte[] b) {
        return encodeToString(b, false);
    }

    /**
     * 十六进制字符串字符串转换为Byte值
     *
     * @param hexStr 十六进制字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] decode(String hexStr, boolean spaceFormat) {
        if (null == hexStr) {
            return null;
        }
        String currentData = null;
        try {
            int m = 0, n = 0;
            String realHexStr;
            if (spaceFormat) {
                realHexStr = hexStr.replace(StringUtils.STR_SPACE_VALUE, "");
            } else {
                realHexStr = hexStr;
            }
            int l = realHexStr.length() / 2;
            byte[] ret = new byte[l];
            for (int i = 0; i < l; i++) {
                m = i * 2 + 1;
                n = m + 1;
                currentData = "0x" + realHexStr.substring(i * 2, m) + realHexStr.substring(m, n);
//                ret[i] = Byte.decode("0x" + realHexStr.substring(i * 2, m) + realHexStr.substring(m, n));
                int intVal = Integer.decode("0x" + realHexStr.substring(i * 2, m) + realHexStr.substring(m, n));
                ret[i] = Byte.valueOf((byte) intVal);
            }
            return ret;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:hexStr2Bytes" + currentData, e);
            return null;
        }

    }

    /**
     * 十六进制字符串字符串转换为Byte值
     *
     * @param src 十六进制字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] decode(String src) {
        return decode(src, false);
    }

    /**
     * String的字符串转换成unicode的String
     *
     * @param strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @Throws Exception
     */
    public static String strToUnicode(String strText) throws Exception {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128) {
                str.append("\\u" + strHex);
            } else {
                // 低位在前面补00
                str.append("\\u00" + strHex);
            }
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     *
     * @param hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, false);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制char[]
     */
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, false);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
     * @return 十六进制String
     */
    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param data 十六进制char[]
     * @return byte[]
     * @Throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHex(char[] data) {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param hexStr 十六进制char[]
     * @return byte[]
     * @Throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    public static byte[] decodeHexStr(String hexStr) {
        char[] data = hexStr.toCharArray();
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch    十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @Throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException(
                    "Error:HexUtil.toDigit->Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * 打印十六进制字符串
     *
     * @param bytes
     */
    public static String getPrintHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append("0x");
            sb.append(hex.toUpperCase());
            if (i < bytes.length - 1) {
                sb.append(",");
            }

        }
        return sb.toString();
    }

    // 数字和字节数组byte[]转换工具

    /**
     * 短整形数据short转换为2长度的字节数组byte[]
     *
     * @param num 短整形数据short
     * @return 2长度的字节数组byte[]
     * @paramDefault endMode 是否从字节结尾开始写入模式
     */
    public static byte[] shortToBytes(short num) {
        return shortToBytes(num, false);
    }

    /**
     * 短整形数据short转换为2长度的字节数组byte[]
     *
     * @param num     短整形数据short
     * @param endMode 是否从字节结尾开始写入模式
     * @return 2长度的字节数组byte[]
     */
    public static byte[] shortToBytes(short num, boolean endMode) {
        int byteSize = 2;
        byte[] bytes = new byte[byteSize];
        for (int i = 0; i < byteSize; i++) {
            int byteIndex = endMode ? (byteSize - i - 1) : i;
            bytes[byteIndex] = (byte) (0xff & (num >> i * 8));
        }
        return bytes;
    }


    /**
     * 2长度的字节数组byte[]转换为短整形数据short
     *
     * @param bytes 2长度的字节数组byte[]
     * @return 短整形数据short
     * @paramDefault endMode 是否从字节结尾开始写入模式
     */
    public static short bytesToShort(byte[] bytes) {
        return bytesToShort(bytes, false);
    }

    /**
     * 2长度的字节数组byte[]转换为短整形数据short
     *
     * @param bytes   2长度的字节数组byte[]
     * @param endMode 是否从字节结尾开始写入模式
     * @return 短整形数据short
     */
    public static short bytesToShort(byte[] bytes, boolean endMode) {
        int num = 0;
        int byteSize = 2;
        for (int i = 0; i < byteSize; i++) {
            byte b;
            if (endMode) {
                b = bytes[byteSize - i - 1];
            } else {
                b = bytes[i];
            }
            int temp = (0x000000ff & b) << i * 8;
            num = num | temp;
        }
        return (short) num;
    }

    /**
     * 整形数据int转换为4长度的字节数组byte[]
     *
     * @param num 整形数据int
     * @return 4长度的字节数组byte[]
     * @paramDefault endMode 是否从字节结尾开始写入模式
     */
    public static byte[] intToBytes(int num) {
        return intToBytes(num, false);
    }

    /**
     * 整形数据int转换为4长度的字节数组byte[]
     *
     * @param num     整形数据int
     * @param endMode 是否从字节结尾开始写入模式
     * @return 4长度的字节数组byte[]
     */
    public static byte[] intToBytes(int num, boolean endMode) {
        int byteSize = 4;
        byte[] bytes = new byte[byteSize];
        for (int i = 0; i < byteSize; i++) {
            int byteIndex = endMode ? (byteSize - i - 1) : i;
            bytes[byteIndex] = (byte) (0xff & (num >> i * 8));
        }
        return bytes;
    }

    /**
     * 4长度的字节数组byte[]转换为整形数据int
     *
     * @param bytes 4长度的字节数组byte[]
     * @return 整形数据int
     * @paramDefault endMode 是否从字节结尾开始写入模式
     */
    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, false);
    }

    /**
     * 4长度的字节数组byte[]转换为整形数据int
     *
     * @param bytes   4长度的字节数组byte[]
     * @param endMode 是否从字节结尾开始写入模式
     * @return 整形数据int
     */
    public static int bytesToInt(byte[] bytes, boolean endMode) {
        int num = 0;
        int byteSize = 4;
        for (int i = 0; i < byteSize; i++) {
            byte b;
            if (endMode) {
                b = bytes[byteSize - i - 1];
            } else {
                b = bytes[i];
            }
            int temp = (0x000000ff & b) << i * 8;
            num = num | temp;
        }
        return num;
    }

    /**
     * 长整形数据long转换为8长度的字节数组byte[]
     *
     * @param num 长整形数据long
     * @return 8长度的字节数组byte[]
     * @paramDefault endMode 是否从字节结尾开始写入模式
     */
    public static byte[] longToBytes(long num) {
        return longToBytes(num, false);
    }

    /**
     * 长整形数据long转换为8长度的字节数组byte[]
     *
     * @param num     长整形数据long
     * @param endMode 是否从字节结尾开始写入模式
     * @return 8长度的字节数组byte[]
     */
    public static byte[] longToBytes(long num, boolean endMode) {
        int byteSize = 8;
        byte[] bytes = new byte[byteSize];
        for (int i = 0; i < byteSize; i++) {
            int byteIndex = endMode ? (byteSize - i - 1) : i;
            bytes[byteIndex] = (byte) (0xff & (num >> i * 8));
        }
        return bytes;
    }

    /**
     * 8长度的字节数组byte[]转换为长整形数据long
     *
     * @param bytes 8长度的字节数组byte[]
     * @return 长整形数据long
     * @paramDefault endMode 是否从字节结尾开始写入模式
     */
    public static long bytesToLong(byte[] bytes) {
        return bytesToLong(bytes, false);
    }

    /**
     * 8长度的字节数组byte[]转换为长整形数据long
     *
     * @param bytes   8长度的字节数组byte[]
     * @param endMode 是否从字节结尾开始写入模式
     * @return 长整形数据long
     */
    public static long bytesToLong(byte[] bytes, boolean endMode) {
        long num = 0;
        int byteSize = 8;
        for (int i = 0; i < byteSize; i++) {
            byte b;
            if (endMode) {
                b = bytes[byteSize - i - 1];
            } else {
                b = bytes[i];
            }
            long temp = ((long) (0x000000ff & b)) << i * 8;
            num = num | temp;
        }
        return num;
    }

}
