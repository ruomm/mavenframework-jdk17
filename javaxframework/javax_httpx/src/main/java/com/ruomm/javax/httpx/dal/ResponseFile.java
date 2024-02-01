/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2016年4月13日 上午9:39:58
 */
package com.ruomm.javax.httpx.dal;

import java.io.File;
import java.util.Map;

public class ResponseFile {
    /**
     * 下载的文件
     */
    public File file;
    /**
     *
     */
    private int status;
    private Map<String, String> headers;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "ResponseFile [file=" + file + ", status=" + status + ", headers=" + headers + "]";
    }

}