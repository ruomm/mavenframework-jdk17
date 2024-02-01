package com.ruomm.javax.httpx.dal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/28 9:24
 */
public class HttpxMultipart {
    private String name;
    private String value;
    private String fileName;
    private String base64Content;
    private File file;
    private byte[] fileBytes;
    private List<File> deleteFileLists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
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

    public List<File> getDeleteFileLists() {
        return deleteFileLists;
    }

    public void setDeleteFileLists(List<File> deleteFileLists) {
        this.deleteFileLists = deleteFileLists;
    }

    public void addDeleteFiles(File file) {
        if (null == file) {
            return;
        }
        if (null == this.deleteFileLists) {
            this.deleteFileLists = new ArrayList<>();
        }
        this.deleteFileLists.add(file);
    }

    @Override
    public String toString() {
        return "HttpxMultipart{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", fileName='" + fileName + '\'' +
                ", base64Content='" + base64Content + '\'' +
                ", file=" + file +
                ", fileBytes=" + (null == fileBytes ? "null" : fileBytes.length + "") +
                ", deleteFileLists=" + deleteFileLists +
                '}';
    }
}
