/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 上午10:24:04
 */
package com.ruomm.javax.objvalidatorx;

public class ObjValidResult {
    private boolean isValid;
    private String message;
    private String errCode;
    private String errMsg;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "ObjValidResult [isValid=" + isValid + ", message=" + message + ", errCode=" + errCode + ", errMsg="
                + errMsg + "]";
    }

}
