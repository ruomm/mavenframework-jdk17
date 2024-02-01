package com.ruomm.javax.httpx.util;

import java.io.File;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/7/12 14:26
 */
public class FileBytesForHttp {
    private String name;
    private String fileName;
    private File file;
    private byte[] fileBytes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    @Override
    public String toString() {
        return "FileBase64{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", file=" + file +
                ", fileBytes=" + (null == fileBytes ? "null" : fileBytes.length + "") +
                '}';
    }
}
