/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年7月28日 上午9:34:23
 */
package com.ruomm.javax.randomx.valid;

import com.ruomm.javax.corex.DigestByJdkUtils;

public class RandomValidStrongHelper implements RandomValidHelper {
    private final static String SLAT_KEY = "9rQ5wVCze70UmxAYufdsa()&";
    private String headerMd5 = null;
    private String footerMd5 = null;
    private boolean strongToken = true;

    @Override
    public boolean isVaildRandomValue(String tokenStr) {
        // TODO Auto-generated method stub
        int size = tokenStr.length();
        String tmpHeaderMd5 = null;
        String tmpFooterMd5 = null;
        if (size == 1) {
            tmpHeaderMd5 = DigestByJdkUtils.encodingMD5(tokenStr + SLAT_KEY);
            tmpFooterMd5 = DigestByJdkUtils.encodingMD5(tokenStr + SLAT_KEY);
        } else {
            int tmpSub = size / 2;
            tmpHeaderMd5 = DigestByJdkUtils.encodingMD5(tokenStr.substring(0, size - tmpSub) + SLAT_KEY);
            tmpFooterMd5 = DigestByJdkUtils.encodingMD5(tokenStr.substring(tmpSub) + SLAT_KEY);
        }
        if (null == tmpHeaderMd5 || tmpHeaderMd5.length() <= 0 || null == tmpFooterMd5 || tmpFooterMd5.length() <= 0) {
            throw new RuntimeException("TokenHelper.generateToken->生产token错误！");
        }
        if (null == headerMd5 || null == footerMd5) {
            if (size == 1) {
                headerMd5 = tmpHeaderMd5;
                footerMd5 = tmpFooterMd5;
                return true;
            } else if (!strongToken) {
                headerMd5 = tmpHeaderMd5;
                footerMd5 = tmpFooterMd5;
                return true;
            } else if (!tmpHeaderMd5.equals(tmpFooterMd5)) {
                int maxRepeatSize = size / 2;
                if (StrUtil.maxRepeatCount(tokenStr) >= maxRepeatSize) {
                    return false;
                } else if (StrUtil.maxOrderCount(tokenStr, false) >= maxRepeatSize) {
                    return false;
                } else if (StrUtil.maxOrderCount(tokenStr, true) >= maxRepeatSize) {
                    return false;
                } else {
                    headerMd5 = tmpHeaderMd5;
                    footerMd5 = tmpFooterMd5;
                    return true;
                }
            } else {
                return false;
            }
        }
        if (!tmpHeaderMd5.equals(headerMd5) && !tmpFooterMd5.equals(footerMd5)) {
            if (size == 1) {
                headerMd5 = tmpHeaderMd5;
                footerMd5 = tmpFooterMd5;
                return true;
            } else if (!strongToken) {
                headerMd5 = tmpHeaderMd5;
                footerMd5 = tmpFooterMd5;
                return true;
            } else if (!tmpHeaderMd5.equals(tmpFooterMd5)) {
                int maxRepeatSize = size / 2;
                if (StrUtil.maxRepeatCount(tokenStr) >= maxRepeatSize) {
                    return false;
                } else if (StrUtil.maxOrderCount(tokenStr, false) >= maxRepeatSize) {
                    return false;
                } else if (StrUtil.maxOrderCount(tokenStr, true) >= maxRepeatSize) {
                    return false;
                } else {
                    headerMd5 = tmpHeaderMd5;
                    footerMd5 = tmpFooterMd5;
                    return true;
                }
            }
        }
        return false;
    }
}
