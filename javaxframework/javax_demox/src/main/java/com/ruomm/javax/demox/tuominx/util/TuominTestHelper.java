package com.ruomm.javax.demox.tuominx.util;

import com.ruomm.javax.encryptx.DigestUtil;
import com.ruomm.javax.tuominx.dal.TuominHelper;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/6 13:29
 */
public class TuominTestHelper implements TuominHelper {


    @Override
    public boolean emptyTuomi() {
        return false;
    }

    @Override
    public String mask(String tag, String clearVal) {
        return "tag_" + tag + "_" + clearVal + "_msk";
    }

    @Override
    public String encrypt(String tag, String clearVal) {
        return "encrypt_" + clearVal;
    }

    @Override
    public String decrypt(String tag, String encVal, String digestVal) {
        return encVal.substring(8) + "_decrypt";
    }

    @Override
    public String decryptWithMask(String tag, boolean ignoreDecryptWithMask, String encOrMaskVal, String digestVal) {
        if (ignoreDecryptWithMask) {
            return encOrMaskVal.substring(8) + "_decrypt";
        }
        return encOrMaskVal;
    }

    @Override
    public String digest(String tag, String encVal) {
        return DigestUtil.encodingMD5(encVal);
    }
}
