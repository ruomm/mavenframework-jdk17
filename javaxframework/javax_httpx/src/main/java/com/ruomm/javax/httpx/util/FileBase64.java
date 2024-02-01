package com.ruomm.javax.httpx.util;

import java.io.File;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/7/12 14:26
 */
public class FileBase64 {
    private String name;
    private String fileName;
    private File file;
    private String base64Content;

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

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    @Override
    public String toString() {
        return "FileBase64{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", file=" + file +
                ", base64Content='" + base64Content + '\'' +
                '}';
    }
}
