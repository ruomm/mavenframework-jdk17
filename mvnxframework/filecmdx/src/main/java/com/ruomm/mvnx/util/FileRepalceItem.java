package com.ruomm.mvnx.util;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/3/9 13:59
 */
public class FileRepalceItem {
    private String replaceFilePath;
    private String replaceRegex;
    private String replaceSub;
    private String repalceContent;
    private String repalceCharset;

    public String getReplaceFilePath() {
        return replaceFilePath;
    }

    public void setReplaceFilePath(String replaceFilePath) {
        this.replaceFilePath = replaceFilePath;
    }

    public String getReplaceRegex() {
        return replaceRegex;
    }

    public void setReplaceRegex(String replaceRegex) {
        this.replaceRegex = replaceRegex;
    }

    public String getReplaceSub() {
        return replaceSub;
    }

    public void setReplaceSub(String replaceSub) {
        this.replaceSub = replaceSub;
    }

    public String getRepalceContent() {
        return repalceContent;
    }

    public void setRepalceContent(String repalceContent) {
        this.repalceContent = repalceContent;
    }

    public String getRepalceCharset() {
        return repalceCharset;
    }

    public void setRepalceCharset(String repalceCharset) {
        this.repalceCharset = repalceCharset;
    }

    @Override
    public String toString() {
        return "FileRepalceItem{" +
                "replaceFilePath='" + replaceFilePath + '\'' +
                ", replaceRegex='" + replaceRegex + '\'' +
                ", replaceSub='" + replaceSub + '\'' +
                ", repalceContent='" + repalceContent + '\'' +
                ", repalceCharset='" + repalceCharset + '\'' +
                '}';
    }
}
