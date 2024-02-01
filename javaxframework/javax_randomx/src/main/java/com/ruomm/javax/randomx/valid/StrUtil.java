/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年3月8日 上午10:10:38
 */
package com.ruomm.javax.randomx.valid;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

class StrUtil {
    private final static Log log = LogFactory.getLog(StrUtil.class);

    public static int maxRepeatCount(String str) {
        if (null == str || str.length() <= 1) {
            return 0;
        }
        int resultSize = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmpSize = 0;
            String tmpi = str.substring(i, i + 1);
            for (int j = i + 1; j < str.length(); j++) {
                String tmpj = str.substring(j, j + 1);
                if (tmpi.equals(tmpj)) {
                    tmpSize++;
                } else {
                    break;
                }
            }
            if (resultSize < tmpSize) {
                resultSize = tmpSize;
            }

        }
        return resultSize == 0 ? 0 : resultSize + 1;
    }

    public static int maxOrderCount(String str, boolean isDesc) {
        if (null == str || str.length() <= 1) {
            return 0;
        }
        int resultSize = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmpSize = 0;
            int tmpi = str.charAt(i);
            for (int j = i + 1; j < str.length(); j++) {
                int tmpj = str.charAt(j);
                if (isDesc) {
                    if (tmpi - tmpj == j - 1) {
                        tmpSize++;
                    } else {
                        break;
                    }
                } else {
                    if (tmpj - tmpi == j - 1) {
                        tmpSize++;
                    } else {
                        break;
                    }
                }
            }
            if (resultSize < tmpSize) {
                resultSize = tmpSize;
            }

        }
        return resultSize == 0 ? 0 : resultSize + 1;
    }

}
