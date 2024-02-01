/**
 * @copyright wanruome-2020
 * @author 牛牛-wanruome@163.com
 * @create 2020年1月21日 下午1:03:43
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import java.io.BufferedOutputStream;

public interface UrlMultiReqWriteFileInterface {
    public boolean writeFileData(String filePath, BufferedOutputStream os);

    public boolean writeFileBytes(byte[] fileBytes, BufferedOutputStream os);

    public boolean writeBase64ContentData(String base64Content, BufferedOutputStream os);

}