/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月4日 下午4:17:09
 */
package com.ruomm.javax.httpx.config;

import java.io.File;
import java.util.Map;

public interface FileHttpListener {
    /**
     * 下载文件结果
     *
     * @param file   下载的文件;
     * @param status 下载文件的结果状态;
     */
    public void httpCallBackFile(File file, Map<String, String> resultHeaders, int status);

    /**
     * 下载文件进度
     *
     * @param bytesWritten  已经下载的内容大小;
     * @param totalSize     总的下载文件大小;
     * @param valueProgress 下载文件的进度(0-1.0);
     */
    public void httpCallBackProgress(long bytesWritten, long totalSize, double valueProgress);
}