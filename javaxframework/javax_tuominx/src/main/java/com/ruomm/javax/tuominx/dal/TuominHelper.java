/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月23日 上午10:40:06
 */
package com.ruomm.javax.tuominx.dal;

public interface TuominHelper {
    // NULL或EMPTY是否使用脱敏

    /**
     * NULL或EMPTY是否使用脱敏
     *
     * @return NULL或EMPTY是否使用脱敏
     */
    public boolean emptyTuomi();

    /**
     * 脱敏时候掩码方法
     *
     * @param tag      脱敏标识
     * @param clearVal 明文值
     * @return 掩码值
     */
    public abstract String mask(String tag, String clearVal);

    /**
     * 脱敏时候加密方法
     *
     * @param tag      脱敏标识
     * @param clearVal 明文值
     * @return 加密值
     */
    public abstract String encrypt(String tag, String clearVal);

    /**
     * 脱敏时候解密方法
     *
     * @param tag       脱敏标识
     * @param encVal    密文值
     * @param digestVal 散列值
     * @return 解密值
     */
    public abstract String decrypt(String tag, String encVal, String digestVal);

    /**
     * 脱敏时候解密方法
     *
     * @param tag                   脱敏标识
     * @param ignoreDecryptWithMask 解密方法加掩码时候无需加掩码
     * @param encOrMaskVal          密文值或掩码值
     * @param digestVal             散列值
     * @return 解密值
     */
    public abstract String decryptWithMask(String tag, boolean ignoreDecryptWithMask, String encOrMaskVal, String digestVal);

    /**
     * 脱敏时候散列方法
     *
     * @param tag      脱敏标识
     * @param clearVal 明文值
     * @return 散列值
     */
    public abstract String digest(String tag, String clearVal);
}
