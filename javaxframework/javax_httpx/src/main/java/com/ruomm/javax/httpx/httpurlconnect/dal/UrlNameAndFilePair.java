/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月12日 下午1:23:16
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

class UrlNameAndFilePair {
    private String name;
    private String fileName;
    private String filePath;
    private byte[] fileBytes;
    private String base64Content;
    private String contentType;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public String getBase64Content() {
        return base64Content;
    }

    public void setBase64Content(String base64Content) {
        this.base64Content = base64Content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "UrlNameAndFilePair{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileBytes=" + (null == fileBytes ? "null" : fileBytes.length + "") +
                ", base64Content='" + base64Content + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}