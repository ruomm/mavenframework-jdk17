/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月10日 上午9:46:21
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;

public class UrlInputReqEntry extends UrlRequestEntry {
    private final static Log log = LogFactory.getLog(UrlInputReqEntry.class);
    public InputStream inputStream;
    public String filePath;
    public File file;

    public UrlInputReqEntry(InputStream inputStream) {
        super();
        this.inputStream = inputStream;
        this.filePath = null;
        this.file = null;
    }

    public UrlInputReqEntry(String filePath) {
        super();
        this.inputStream = null;
        this.filePath = filePath;
        this.file = null;
    }

    public UrlInputReqEntry(File file) {
        super();
        this.inputStream = null;
        this.filePath = null;
        this.file = file;
    }

    @Override
    public boolean doOutputStream(HttpURLConnection conn) {
        // TODO Auto-generated method stub
        BufferedInputStream bufis = null;
        BufferedOutputStream bufos = null;
        boolean isTrue = false;
        try {
            if (null != inputStream) {
                bufis = new BufferedInputStream(inputStream);
            } else if (null != file) {
                bufis = new BufferedInputStream(new FileInputStream(file));
            } else if (null != filePath && filePath.length() > 0) {
                bufis = new BufferedInputStream(new FileInputStream(filePath));
            }
            bufos = new BufferedOutputStream(conn.getOutputStream());
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bufis.read(buffer)) > 0) {
                bufos.write(buffer, 0, count);
            }
            bufos.flush();
            isTrue = true;
        } catch (Exception e) {
            log.error("Error:doOutputStream", e);
        } finally {
            if (null != bufis) {
                try {
                    bufis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:doOutputStream", e);
                }
            }
            if (null != bufos) {
                try {
                    bufos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:doOutputStream", e);
                }
            }
        }
        return isTrue;
    }

    @Override
    public boolean setHttpContentType(HttpURLConnection conn) {
        // TODO Auto-generated method stub
        boolean flag = false;
        try {
            // 设置编码方式和Content-Type模式2
            if (null == getCharset() || getCharset().length() <= 0) {
                if (null == getMimeType() || getMimeType().length() <= 0) {
                    conn.setRequestProperty("Content-Type", "application/octet-stream");
                } else {
                    conn.setRequestProperty("Content-Type", getMimeType());
                }
            } else {
                if (null == getMimeType() || getMimeType().length() <= 0) {
                    conn.setRequestProperty("Content-Type", "application/octet-stream" + "; charset=" + getCharset());
                } else {
                    conn.setRequestProperty("Content-Type", getMimeType() + "; charset=" + getCharset());
                }

            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:setHttpContentType", e);
        }

        return flag;
    }

}