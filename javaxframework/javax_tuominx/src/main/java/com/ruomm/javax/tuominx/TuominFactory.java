package com.ruomm.javax.tuominx;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2022/1/18 10:59
 */
public class TuominFactory {
    private final static TuominCore TUOMIN_CORE_WITH_EXCEPTION = new TuominCore(true, true);
    private final static TuominCore TUOMIN_CORE_NO_EXCEPTION = new TuominCore(false, true);

    public static TuominCore getCoreWithException() {
        return TUOMIN_CORE_WITH_EXCEPTION;
    }

    public static TuominCore getCoreNoException() {
        return TUOMIN_CORE_NO_EXCEPTION;
    }
}
