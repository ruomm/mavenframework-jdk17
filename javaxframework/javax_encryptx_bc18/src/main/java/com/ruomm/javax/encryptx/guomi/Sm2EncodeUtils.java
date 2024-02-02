package com.ruomm.javax.encryptx.guomi;

import java.lang.reflect.Method;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/6/11 9:56
 */
class Sm2EncodeUtils {
    public static byte[] parseGetEncoded(Object object) {
        byte[] resultData = null;
        try {
            boolean hasParam = false;
            Method method = object.getClass().getMethod("getEncoded", boolean.class);
            hasParam = true;
            if (null == method) {
                method = object.getClass().getMethod("getEncoded");
                hasParam = false;
            }
            if (null == method) {
                method = object.getClass().getDeclaredMethod("getEncoded", boolean.class);
                hasParam = true;
            }
            if (null == method) {
                method = object.getClass().getDeclaredMethod("getEncoded");
                hasParam = false;
            }
            method.setAccessible(true);
            if (hasParam) {
                resultData = (byte[]) method.invoke(object, false);
            } else {
                resultData = (byte[]) method.invoke(object);
            }

        } catch (Exception e) {
            resultData = null;
            e.printStackTrace();
        }
        return resultData;

    }
}
