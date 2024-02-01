/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月12日 下午12:42:48
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UrlStringReqEntry extends UrlRequestEntry {
    private final static Log log = LogFactory.getLog(UrlStringReqEntry.class);
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean setHttpContentType(HttpURLConnection conn) {
        boolean flag = false;
        try {
            // 设置编码方式和Content-Type模式1
//			if (null == getCharset() || getCharset().length() <= 0) {
//				conn.setRequestProperty("Charset", getCharset());
//			}
//			conn.setRequestProperty("Content-Type", getMimeType());
            // 设置编码方式和Content-Type模式2
            if (null == getCharset() || getCharset().length() <= 0) {
                conn.setRequestProperty("Content-Type", getMimeType());
            } else {
                conn.setRequestProperty("Content-Type", getMimeType() + "; charset=" + getCharset());

            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:setHttpContentType", e);
        }
        return flag;
    }
//	@Override
//	public List<UrlNameAndValuePair> getRequestProperty() {
//		List<UrlNameAndValuePair> lst = new ArrayList<UrlNameAndValuePair>();
//		// 设置Accept类型
//		if (null != getAcceptType() && getAcceptType().length() > 0) {
//			lst.add(new UrlNameAndValuePair("Accept", getAcceptType()));
//		}
//		// 设置是否保持连接
//		if (isKeepAlive()) {
//			lst.add(new UrlNameAndValuePair("Connection", "keep-alive"));
//		}
//		else {
//			lst.add(new UrlNameAndValuePair("Connection", "close"));
//		}
//		// 设置浏览器编码类型
//		if (null != getUserAgent() && getUserAgent().length() > 0) {
////			lst.add(new UrlNameAndValuePair("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)"));
//			lst.add(new UrlNameAndValuePair("User-Agent", getUserAgent()));
//		}
//		// 设置编码方式和Content-Type模式1
////		if (null != getCharset() && getCharset().length() > 0) {
////			lst.add(new UrlNameAndValuePair("Charset", getCharset()));
////		}
////		lst.add(new UrlNameAndValuePair("Content-Type", getMimeType()));
//		// 设置编码方式和Content-Type模式2
//		if (null == getCharset() || getCharset().length() <= 0) {
//			lst.add(new UrlNameAndValuePair("Content-Type", getMimeType()));
//		}
//		else {
//			lst.add(new UrlNameAndValuePair("Content-Type", getMimeType() + "; charset=" + getCharset()));
//		}
//		return lst;
//	}

    @Override
    public boolean doOutputStream(HttpURLConnection conn) {
        // TODO Auto-generated method stub
        OutputStream out = null;
        BufferedOutputStream os = null;
        boolean flag = false;
        try {

            byte[] data = null;
            if (null != getCharset() && getCharset().length() > 0) {
                if (null != getText()) {
                    data = getText().getBytes(getCharset());
                }
            } else {
                if (null != getText()) {
                    data = getText().getBytes();
                }
            }
            conn.setRequestProperty("Content-Length", null == data ? "0" : String.valueOf(data.length));
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
            if (null != headerData) {
                System.arraycopy(data, 0, headerData, 0, headerSize);
            }
            footerData = new byte[footerSize];
            System.arraycopy(data, headerSize, footerData, 0, footerSize);

            if (null != headerData) {
                os.write(headerData);
            }
            os.write(footerData);
            os.flush();
            flag = true;
//			if (null != getCharset() && getCharset().length() > 0) {
//				os.write(getText().getBytes(getCharset()));
//			}
//			else {
//				os.write(getText().getBytes());
//			}
//			os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("Error:doOutputStream", e);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:doOutputStream", e);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    log.error("Error:doOutputStream", e2);
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    log.error("Error:doOutputStream", e2);
                }
            }
        }
        return flag;
    }
}