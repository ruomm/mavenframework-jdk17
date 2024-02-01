/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月13日 下午4:42:34
 */
package com.ruomm.javax.poix;

class CellWriteResult {
    private boolean skip;
    private boolean end;
    private boolean throwErr;

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isThrowErr() {
        return throwErr;
    }

    public void setThrowErr(boolean throwErr) {
        this.throwErr = throwErr;
    }

    public boolean isValid() {
        return !skip && !end && !throwErr;
    }

    @Override
    public String toString() {
        return "CellWriteResult [skip=" + skip + ", end=" + end + ", throwErr=" + throwErr + "]";
    }

}
