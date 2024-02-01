/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年8月14日 下午1:53:20
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UrlByteReqEntry extends UrlRequestEntry {
    private byte[] requestData;

    public byte[] getRequestData() {
        return this.requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    @Override
    public boolean setHttpContentType(HttpURLConnection conn) {
        boolean flag = false;
        try {
            if ((getCharset() == null) || (getCharset().length() <= 0)) {
                conn.setRequestProperty("Content-Type", getMimeType());
            } else {
                conn.setRequestProperty("Content-Type", getMimeType() + "; charset=" + getCharset());
            }

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean doOutputStream(HttpURLConnection conn) {
        OutputStream out = null;
        BufferedOutputStream os = null;
        boolean flag = false;
        try {
            byte[] data = this.requestData;
            conn.setRequestProperty("Content-Length", data == null ? "0" : String.valueOf(data.length));
            out = conn.getOutputStream();
            os = new BufferedOutputStream(out);
            byte[] headerData = null;
            byte[] footerData = null;
            int footerSize = data.length - data.length / 2;
            if (footerSize > 20) {
                footerSize = 20;
            }
            int headerSize = data.length - footerSize;
            if (headerSize > 0) {
                headerData = new byte[headerSize];
            }
            if (headerData != null) {
                System.arraycopy(data, 0, headerData, 0, headerSize);
            }
            footerData = new byte[footerSize];
            System.arraycopy(data, headerSize, footerData, 0, footerSize);

            if (headerData != null) {
                os.write(headerData);
            }
            os.write(footerData);
            os.flush();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();

            if (os != null) {
                try {
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (os != null) {
                try {
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return flag;
    }
}
