/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年6月16日 下午2:59:01
 */
package com.ruomm.webx.requestwrapperx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

public class PostRepeatReaderRequestWrapper extends HttpServletRequestWrapper {
    private final static Log log = LogFactory.getLog(PostRepeatReaderRequestWrapper.class);

    private byte[] body = null; // 报文

    public PostRepeatReaderRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = readBytes(request.getInputStream());
//		String tmp = new String(body);
//		log.debug(tmp);
//		Map<String, String[]> data = request.getParameterMap();
//		log.debug(data);
//		log.debug(body);
    }

    public byte[] getWrapperBody() {
        return body;
    }

    public String getWrapperString(String contentType) {
        if (null == body) {
            return null;
        }
        String charsetName = null;
        if (null == contentType || contentType.length() <= 0) {
            charsetName = null;
        } else {
            String[] datas = contentType.split(";");
            for (String tmp : datas) {
                if (null == tmp || tmp.length() <= 0) {
                    continue;
                }
                if (tmp.trim().toLowerCase().startsWith("charset")) {
                    String[] charDatas = tmp.split("=");
                    if (null != charDatas && charDatas.length == 2 && null != charDatas[1]
                            && charDatas[1].length() > 0) {
                        charsetName = charDatas[1];
                        break;
                    }
                }
            }
        }
        Charset charset = null;
        try {
            if (null == charsetName || charsetName.length() <= 0 || !Charset.isSupported(charsetName)) {
                charset = null;
            } else {
                charset = Charset.forName(charsetName);
            }
        } catch (Exception e) {
            // TODO: handle exception
            charset = null;
            log.error("Error:getWrapperString", e);
        }
        try {
            if (null != charset) {
                return new String(body);
            } else {
                return new String(body, charset);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:getWrapperString", e);
            return null;
        }

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                // TODO Auto-generated method stub
                return bais.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isReady() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isFinished() {
                // TODO Auto-generated method stub
                return false;
            }
        };

    }

    private static byte[] readBytes(InputStream in) throws IOException {
        BufferedInputStream bufin = new BufferedInputStream(in);
        final int buffSize = 1024;
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
        byte[] temp = new byte[buffSize];
        int size = 0;
        while ((size = bufin.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        out.flush();
        byte[] content = out.toByteArray();
        bufin.close();
        out.close();
        return content;
    }

}
