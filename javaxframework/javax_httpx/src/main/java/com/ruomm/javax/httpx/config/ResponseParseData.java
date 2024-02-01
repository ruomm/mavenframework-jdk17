package com.ruomm.javax.httpx.config;

public interface ResponseParseData {
//	public Object parseResponseText(String resourceString, Class<?> cls, String respFormat);

    public Object parseResponseData(byte[] resultData, Class<?> cls, String respCharsetName, String respFormat);


}