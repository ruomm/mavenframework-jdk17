package com.ruomm.mvnx.dal;

import com.ruomm.javax.corex.dal.OsEnvDal;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/25 23:29
 */
public class ParamOsValue {
    private OsEnvDal osEnv;
    private boolean enable;
    private String errMsg;

    public OsEnvDal getOsEnv() {
        return osEnv;
    }

    public void setOsEnv(OsEnvDal osEnv) {
        this.osEnv = osEnv;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "ParamOsValue{" +
                "osEnv=" + osEnv +
                ", enable=" + enable +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
