package com.ruomm.javax.encryptx.guomi;

/**
 * Created by $(USER) on $(DATE)
 */
public class SM4_Context {
    public int mode;

    public long[] sk;

    public boolean isPadding;

    public SM4_Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }
}
