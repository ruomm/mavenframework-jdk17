/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月2日 上午11:30:28
 */
package com.ruomm.javax.encryptx.dal;

import com.ruomm.javax.corex.helper.EncoderHelper;

import java.nio.charset.Charset;
import java.util.Random;

public class EncryptHelper implements EncryptHelperInterface {
    private EncoderHelper keyEncoderHeper;
    private EncoderHelper dataEncoderHelper;
    private static EncryptHelper helper_default = new EncryptHelper(EncoderHelper.EncoderMode.HEX_LOWER, EncoderHelper.EncoderMode.HEX_LOWER);

    public static EncryptHelper getInstance() {
        return helper_default;
    }

    public static EncryptHelperInterface getInstance(EncryptHelperInterface helper) {
        return null == helper ? helper_default : helper;
    }

    public EncryptHelper(EncoderHelper.EncoderMode keyEncoderMode, EncoderHelper.EncoderMode dataEncoderMode) {
        if (null == keyEncoderMode) {
            this.keyEncoderHeper = null;
        } else {
            this.keyEncoderHeper = new EncoderHelper(keyEncoderMode);
        }
        this.dataEncoderHelper = new EncoderHelper(dataEncoderMode);
    }

    public void setKeyEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.keyEncoderHeper = new EncoderHelper(encoderMode);
    }

    public void setDataEncoderMode(EncoderHelper.EncoderMode encoderMode) {
        this.dataEncoderHelper = new EncoderHelper(encoderMode);
    }


    @Override
    public String generateRandomKey(int keySize) {
        return generateRandomKeyByHelper(keySize);
    }

    @Override
    public String formatKey(byte[] keyData) {
        if (null == keyEncoderHeper) {
            return new String(keyData, Charset.forName("ISO-8859-1"));
        }
        return keyEncoderHeper.encodeToString(keyData);
    }

    @Override
    public byte[] restoreRandomKey(String keyStr) {
        if (null == keyEncoderHeper) {
            return keyStr.getBytes(Charset.forName("ISO-8859-1"));
        }
        return keyEncoderHeper.decode(keyStr);
    }

    @Override
    public String encodeToString(byte[] binaryData) {
        return dataEncoderHelper.encodeToString(binaryData);
    }

    @Override
    public byte[] decodeToByte(String srcStr) {
        return dataEncoderHelper.decode(srcStr);
    }

    private String generateRandomKeyByHelper(int randomKeySize) {
        if (null == keyEncoderHeper) {
            return generateRandomKeyString(randomKeySize);
        }
        Random random = new Random();
        byte[] keyData = new byte[randomKeySize];
        for (int i = 0; i < randomKeySize; i++) {
            keyData[i] = (byte) (random.nextInt(256) - 128);
        }
        return keyEncoderHeper.encodeToString(keyData);
    }

    private String generateRandomKeyString(int randomKeySize) {
//		String charDatas = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789`~!@#$%^&*()_+-=,./<>?;':\"{}|[]\\";
        String charDatas = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int charDatasSize = charDatas.length();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < randomKeySize; i++) {
            sb.append(charDatas.charAt(random.nextInt(charDatasSize)));
        }
        return sb.toString();
    }
}
