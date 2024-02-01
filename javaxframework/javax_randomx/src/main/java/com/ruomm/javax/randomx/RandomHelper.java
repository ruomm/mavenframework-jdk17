/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年6月14日 上午10:57:58
 */
package com.ruomm.javax.randomx;

import com.ruomm.javax.randomx.valid.RandomValidHelper;

import java.util.Random;
import java.util.UUID;

public class RandomHelper {
    public final static String TOKEN_LETTER_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String TOKEN_LETTER_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    public final static String TOKEN_NUMBER = "0123456789";
    public final static String TOKEN_SYMBOL = "/_";
    public final static String TOKEN_SYMBOL_TWO = ",./<>?;:'\"[]{}`~!@#$%^&*()-_=+";
    private final static int TOKEN_DEFAULT_SIZE = 16;
    private String tokenStr = null;
    private String tokenStrNoZero = null;
    private int defaultTokenSize = TOKEN_DEFAULT_SIZE;
    private RandomValidHelper randomValidHelper = null;

    public RandomHelper(String tokenStr) {
        this(tokenStr, TOKEN_DEFAULT_SIZE, null);
    }

    public RandomHelper(String tokenStr, int defaultTokenSize) {
        this(tokenStr, defaultTokenSize, null);
    }

    public RandomHelper(String tokenStr, RandomValidHelper randomValidHelper) {
        this(tokenStr, 0, randomValidHelper);
    }

    public RandomHelper(String tokenStr, int defaultTokenSize, RandomValidHelper randomValidHelper) {
        super();
        if (null == tokenStr || tokenStr.length() <= 0) {
            this.tokenStr = TOKEN_LETTER_UPPER_CASE + TOKEN_LETTER_LOWER_CASE + TOKEN_NUMBER + TOKEN_SYMBOL;
        } else {
            this.tokenStr = tokenStr;
        }
        this.tokenStrNoZero = this.tokenStr.replaceAll("0", "");
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
        this.randomValidHelper = randomValidHelper;
    }

    public String generateToken() {
        return generateToken(defaultTokenSize, false);
    }

    public String generateToken(int length) {
        return generateToken(length, false);
    }

    public String generateToken(boolean isFirstNoZero) {
        return generateToken(defaultTokenSize, isFirstNoZero);
    }

    public synchronized String generateToken(int length, boolean isFirstNoZero) {
        String result = generateTokenItem(length, isFirstNoZero);
        if (null == result || result.length() <= 0) {
            throw new RuntimeException("TokenHelper.generateToken->生产token错误！");
        }
        if (null == randomValidHelper) {
            return result;
        }
        if (randomValidHelper.isVaildRandomValue(result)) {
            return result;
        } else {
            return generateToken(length, isFirstNoZero);
        }
    }

    private String generateTokenItem(int length, boolean isFirstNoZero) {
        int tokenStrSize = null == this.tokenStr ? 0 : this.tokenStr.length();
        if (tokenStrSize <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (i == 0 && isFirstNoZero) {
                if (tokenStrNoZero.length() > 0) {
                    sb.append(tokenStrNoZero.charAt(random.nextInt(tokenStrNoZero.length())));
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
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
