package com.ruomm.javax.corex.exception;

public class JavaxCorexException extends RuntimeException {
    // 定义成功的参数
    public final static String CODE_SUCCESS = "0000";
    // 定义业务处理错误的参数
    public final static String ERR_CORE = "0001";
    // 定义参数值错误的参数
    public final static String ERR_PARAM = "0002";
    // 定义系统异常错误
    public final static String ERR_SYSTEM = "0999";
    // 定义mavenframework异常的错误
    public final static String CODE_ERR_JAVAX_COREX = "0999";


    private String code;

    public JavaxCorexException() {
    }

    public JavaxCorexException(String message) {
        super(message);
    }

    public JavaxCorexException(String code, String message) {
        super(message);
        this.code = code;
    }

    public JavaxCorexException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavaxCorexException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public JavaxCorexException(Throwable cause) {
        super(cause);
    }

    public JavaxCorexException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JavaxCorexException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
