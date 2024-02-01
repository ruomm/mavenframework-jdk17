/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年6月14日 上午10:57:58
 */
package com.ruomm.javax.basex;

import java.util.Random;
import java.util.UUID;

public class TokenHelper {
    public final static String TOKEN_LETTER_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String TOKEN_LETTER_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public final static String TOKEN_NUMBER = "0123456789";
    public final static String TOKEN_SYMBOL = "/_";
    public final static String TOKEN_HEX_STRING = "0123456789abcdef";
    public final static String TOKEN_SYMBOL_TWO = ",./<>?;:'\"[]{}`~!@#$%^&*()-_=+";
    private final static int TOKEN_DEFAULT_SIZE = 16;

    private String tokenStr = null;
    private int defaultTokenSize = TOKEN_DEFAULT_SIZE;

    public static TokenHelper getInstanceHexTokenHelper() {
        return new TokenHelper(TOKEN_HEX_STRING, 32);
    }

    public TokenHelper(String tokenStr) {
        this(tokenStr, 0);
    }

    public TokenHelper(String tokenStr, int defaultTokenSize) {
        super();
        if (null == tokenStr || tokenStr.length() <= 0) {
            this.tokenStr = TOKEN_LETTER_UPPER_CASE + TOKEN_LETTER_LOWER_CASE + TOKEN_NUMBER + TOKEN_SYMBOL;
        } else {
            this.tokenStr = tokenStr;
        }
        if (defaultTokenSize <= 0) {
            int tmp = TOKEN_DEFAULT_SIZE;
            if (tmp <= 0) {
                this.defaultTokenSize = 16;
            } else {
                this.defaultTokenSize = tmp;
            }
        } else {
            this.defaultTokenSize = defaultTokenSize;
        }
    }

    public String generateToken() {
        return generateToken(defaultTokenSize, false);
    }

    public String generateToken(int length) {
        return generateToken(length, false);
    }

    public String generateToken(int length, boolean isFirstNoZero) {
        int tokenStrSize = null == this.tokenStr ? 0 : this.tokenStr.length();
        if (tokenStrSize <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (i == 0 && isFirstNoZero) {
                String tmpTokenStr = tokenStr.replace("0", "");
                if (tmpTokenStr.length() > 0) {
                    sb.append(tmpTokenStr.charAt(random.nextInt(tmpTokenStr.length())));
                } else {
                    sb.append(tokenStr.charAt(random.nextInt(tokenStrSize)));
                }

            } else {
                sb.append(tokenStr.charAt(random.nextInt(tokenStrSize)));
            }
        }
        return sb.toString();
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

}
