package com.ruomm.javax.httpx.config;

public interface ResponseParseText {

    public Object parseResponseText(String resultData, Class<?> cls, String respCharsetName, String respFormat);
}