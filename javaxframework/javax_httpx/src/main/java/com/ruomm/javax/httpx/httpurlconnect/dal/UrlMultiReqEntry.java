/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月12日 下午2:43:50
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

public class UrlMultiReqEntry extends UrlRequestEntry {
    private final static Log log = LogFactory.getLog(UrlMultiReqEntry.class);
    private final static String PREFIX = "--";// 必须存在
    private final static String LINE_END = "\r\n";// 行结束表示
    private List<UrlNameAndValuePair> nameAndValuePairs;
    private List<UrlNameAndFilePair> nameAndFilePairs;
    private String boundary;
    private boolean isUrlEncode = false;
    private UrlMultiReqWriteFileInterface writeFileInterface;

    public UrlMultiReqWriteFileInterface getWriteFileInterface() {
        return writeFileInterface;
    }

    public void setWriteFileInterface(UrlMultiReqWriteFileInterface writeFileInterface) {
        this.writeFileInterface = writeFileInterface;
    }

    public List<UrlNameAndValuePair> getNameAndValuePairs() {
        return nameAndValuePairs;
    }

    public void setNameAndValuePairs(List<UrlNameAndValuePair> nameAndValuePairs) {
        this.nameAndValuePairs = nameAndValuePairs;
    }

    public List<UrlNameAndFilePair> getNameAndFilePairs() {
        return nameAndFilePairs;
    }

    public void setNameAndFilePairs(List<UrlNameAndFilePair> nameAndFilePairs) {
        this.nameAndFilePairs = nameAndFilePairs;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public boolean isUrlEncode() {
        return isUrlEncode;
    }

    public void setUrlEncode(boolean isUrlEncode) {
        this.isUrlEncode = isUrlEncode;
    }

    @Override
    public boolean setHttpContentType(HttpURLConnection conn) {
        boolean flag = false;
        try {
            // 设置编码方式和Content-Type模式1
            if (null != getCharset() && getCharset().length() > 0) {
                conn.setRequestProperty("Charset", getCharset());
            }
            String contentTypeNoCharset = null == getMimeType() || getMimeType().length() <= 0
                    ? "multipart/form-data" + "; boundary=" + getBoundary()
                    : getMimeType() + "; boundary=" + getBoundary();
            conn.setRequestProperty("Content-Type", contentTypeNoCharset);
            // 设置编码方式和Content-Type模式2
//			if (null == getCharset() || getCharset().length() <= 0) {
//				String contentType = null == getMimeType() || getMimeType().length() <= 0
//						? "multipart/form-data" + "; boundary=" + getBoundary()
//						: getMimeType() + "; boundary=" + getBoundary();
//				conn.setRequestProperty("Content-Type", contentType);
//			}
//			else {
//				String contentType = null == getMimeType() || getMimeType().length() <= 0
//						? "multipart/form-data" + "; charset=" + getCharset() + "; boundary=" + getBoundary()
//						: getMimeType() + "; charset=" + getCharset() + "; boundary=" + getBoundary();
//				conn.setRequestProperty("Content-Type", contentType);
//			}
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
//		if (null != getCharset() && getCharset().length() > 0) {
//			lst.add(new UrlNameAndValuePair("Charset", getCharset()));
//		}
//		String contentTypeNoCharset = null == getMimeType() || getMimeType().length() <= 0
//				? "multipart/form-data" + "; boundary=" + getBoundary()
//				: getMimeType() + "; boundary=" + getBoundary();
//		lst.add(new UrlNameAndValuePair("Content-Type", contentTypeNoCharset));
//		// 设置编码方式和Content-Type模式2
////		if (null == getCharset() || getCharset().length() <= 0) {
////			String contentType = null == getMimeType() || getMimeType().length() <= 0
////					? "multipart/form-data" + "; boundary=" + getBoundary()
////					: getMimeType() + "; boundary=" + getBoundary();
////			lst.add(new UrlNameAndValuePair("Content-Type", contentType));
////		}
////		else {
////			String contentType = null == getMimeType() || getMimeType().length() <= 0
////					? "multipart/form-data" + "; charset=" + getCharset() + "; boundary=" + getBoundary()
////					: getMimeType() + "; charset=" + getCharset() + "; boundary=" + getBoundary();
////			lst.add(new UrlNameAndValuePair("Content-Type", contentType));
////		}
//		return lst;
//	}

    public String getRequestString() {
        StringBuilder sb = new StringBuilder();
        if (null != nameAndFilePairs) {
            for (UrlNameAndValuePair tmpPair : nameAndValuePairs) {
                sb.append(getPairString(tmpPair));
            }
        }
        if (null != nameAndFilePairs) {
            for (UrlNameAndFilePair tmpPair : nameAndFilePairs) {
                sb.append(getPairString(tmpPair));
            }
        }
        sb.append(PREFIX).append(boundary).append(PREFIX).append(LINE_END);
        // 可能部分服务器不需要拼接此换行结尾
        sb.append(LINE_END);
        return sb.toString();
    }

    public String getPairString(UrlNameAndValuePair pair) {
        StringBuilder pairSb = new StringBuilder();
        pairSb.append(PREFIX).append(boundary).append(LINE_END);
        pairSb.append("Content-Disposition: form-data; name=\"").append(pair.getName()).append("\"").append(LINE_END);
        if (null != getCharset() && getCharset().length() > 0) {
            pairSb.append("Content-Type: text/plain; charset=" + getCharset()).append(LINE_END);
        } else {
            pairSb.append("Content-Type: text/plain").append(LINE_END);
        }
        // 设置传输编码方式
        pairSb.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
        pairSb.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
        pairSb.append(encodeValue(pair.getValue()));
        pairSb.append(LINE_END);
        return pairSb.toString();
    }

    public String getPairString(UrlNameAndFilePair pair) {
        StringBuilder pairSb = new StringBuilder();
        pairSb.append(PREFIX).append(boundary).append(LINE_END);
        pairSb.append("Content-Disposition: form-data; name=\"").append(pair.getName()).append("\"; filename=\"")
                .append(encodeValue(pair.getFileName())).append("\"").append(LINE_END);
        String pairContentType = null == pair.getContentType() || pair.getContentType().length() <= 0
                ? getFileContentType(pair.getFileName())
                : pair.getContentType();
        if (null != getCharset() && getCharset().length() > 0) {
            pairSb.append("Content-Type: ").append(pairContentType).append("; charset=" + getCharset())
                    .append(LINE_END);
        } else {
            pairSb.append("Content-Type: ").append(pairContentType).append(LINE_END);
        }
        // 设置传输编码方式，非必须
        pairSb.append("Content-Transfer-Encoding: binary").append(LINE_END);
        pairSb.append(LINE_END);
        pairSb.append("具体文件内容：" + pair.getFilePath());
        pairSb.append(LINE_END);
        return pairSb.toString();
    }
    // 使用URLEncode编码字符串

    // 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
    private static String getFileContentType(String fileName) {
        if (null == fileName || fileName.length() <= 0) {
            return "application/octet-stream";
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream";
        }
    }

    // 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
    private String encodeValue(String value) {
        if (null == value || value.length() <= 0) {
            return value;
        }

        if (isUrlEncode) {
            try {
                String encodeCharset = CharsetUtils.parseRealCharsetName(getCharset(), "utf-8");
                return URLEncoder.encode(value, encodeCharset);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:encodeValue", e);
//				throw new RuntimeException("请求参数URLEncoder时候出错");
                throw new RuntimeException(e);
            }
        } else {
            return value;
        }

    }

    @Override
    public boolean doOutputStream(HttpURLConnection conn) {
        // TODO Auto-generated method stub
        OutputStream out = null;
        BufferedOutputStream os = null;
        boolean flag = false;
        try {
            out = conn.getOutputStream();
            os = new BufferedOutputStream(out);
            if (null != nameAndValuePairs) {
                for (UrlNameAndValuePair pair : nameAndValuePairs) {

                    StringBuilder pairSb = new StringBuilder();
                    pairSb.append(PREFIX).append(boundary).append(LINE_END);
                    pairSb.append("Content-Disposition: form-data; name=\"").append(pair.getName()).append("\"")
                            .append(LINE_END);
                    if (null != getCharset() && getCharset().length() > 0) {
                        pairSb.append("Content-Type: text/plain; charset=" + getCharset()).append(LINE_END);
                    } else {
                        pairSb.append("Content-Type: text/plain").append(LINE_END);
                    }
                    // 设置传输编码方式
                    pairSb.append("Content-Transfer-Encoding: 8bit").append(LINE_END);
                    pairSb.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
                    if (null != getCharset() && getCharset().length() > 0) {
                        os.write(pairSb.toString().getBytes(getCharset()));
                    } else {
                        os.write(pairSb.toString().getBytes());
                    }
                    // 写入具体的值
                    if (null != getCharset() && getCharset().length() > 0) {
                        os.write(encodeValue(pair.getValue()).getBytes(getCharset()));
                    } else {
                        os.write(encodeValue(pair.getValue()).getBytes());
                    }
                    // 写入换行
                    if (null != getCharset() && getCharset().length() > 0) {
                        os.write(LINE_END.getBytes(getCharset()));
                    } else {
                        os.write(LINE_END.getBytes());
                    }
                }
            }
            boolean fileFlag = true;
            if (null != nameAndFilePairs) {
                for (UrlNameAndFilePair pair : nameAndFilePairs) {
                    if (null != pair.getFilePath() && pair.getFilePath().length() > 0) {
                        StringBuilder pairSb = new StringBuilder();
                        pairSb.append(PREFIX).append(boundary).append(LINE_END);
                        pairSb.append("Content-Disposition: form-data; name=\"").append(pair.getName())
                                .append("\"; filename=\"").append(encodeValue(pair.getFileName())).append("\"")
                                .append(LINE_END);
                        String pairContentType = null == pair.getContentType() || pair.getContentType().length() <= 0
                                ? getFileContentType(pair.getFileName())
                                : pair.getContentType();
                        if (null != getCharset() && getCharset().length() > 0) {
                            pairSb.append("Content-Type: ").append(pairContentType).append("; charset=" + getCharset())
                                    .append(LINE_END);
                        } else {
                            pairSb.append("Content-Type: ").append(pairContentType).append(LINE_END);
                        }
                        // 设置传输编码方式，非必须
                        pairSb.append("Content-Transfer-Encoding: binary").append(LINE_END);
                        pairSb.append(LINE_END);
                        if (null != getCharset() && getCharset().length() > 0) {
                            os.write(pairSb.toString().getBytes(getCharset()));
                        } else {
                            os.write(pairSb.toString().getBytes());
                        }
                        boolean tmpFlag = null == writeFileInterface ? writeFileData(pair.getFilePath(), os)
                                : writeFileInterface.writeFileData(pair.getFilePath(), os);
                        if (!tmpFlag) {
                            fileFlag = false;
                            break;
                        }
                        // 写入换行
                        if (null != getCharset() && getCharset().length() > 0) {
                            os.write(LINE_END.getBytes(getCharset()));
                        } else {
                            os.write(LINE_END.getBytes());
                        }
                    } else if (null != pair.getFileBytes() && pair.getFileBytes().length > 0) {
                        StringBuilder pairSb = new StringBuilder();
                        pairSb.append(PREFIX).append(boundary).append(LINE_END);
                        pairSb.append("Content-Disposition: form-data; name=\"").append(pair.getName())
                                .append("\"; filename=\"").append(encodeValue(pair.getFileName())).append("\"")
                                .append(LINE_END);
                        String pairContentType = null == pair.getContentType() || pair.getContentType().length() <= 0
                                ? getFileContentType(pair.getFileName())
                                : pair.getContentType();
                        if (null != getCharset() && getCharset().length() > 0) {
                            pairSb.append("Content-Type: ").append(pairContentType).append("; charset=" + getCharset())
                                    .append(LINE_END);
                        } else {
                            pairSb.append("Content-Type: ").append(pairContentType).append(LINE_END);
                        }
                        // 设置传输编码方式，非必须
                        pairSb.append("Content-Transfer-Encoding: binary").append(LINE_END);
                        pairSb.append(LINE_END);
                        if (null != getCharset() && getCharset().length() > 0) {
                            os.write(pairSb.toString().getBytes(getCharset()));
                        } else {
                            os.write(pairSb.toString().getBytes());
                        }
                        boolean tmpFlag = null == writeFileInterface ? writeFileBytes(pair.getFileBytes(), os)
                                : writeFileInterface.writeFileBytes(pair.getFileBytes(), os);
                        if (!tmpFlag) {
                            fileFlag = false;
                            break;
                        }
                        // 写入换行
                        if (null != getCharset() && getCharset().length() > 0) {
                            os.write(LINE_END.getBytes(getCharset()));
                        } else {
                            os.write(LINE_END.getBytes());
                        }
                    } else if (null != pair.getBase64Content() && pair.getBase64Content().length() > 0) {
                        StringBuilder pairSb = new StringBuilder();
                        pairSb.append(PREFIX).append(boundary).append(LINE_END);
                        pairSb.append("Content-Disposition: form-data; name=\"").append(pair.getName())
                                .append("\"; filename=\"").append(encodeValue(pair.getFileName())).append("\"")
                                .append(LINE_END);
                        String pairContentType = null == pair.getContentType() || pair.getContentType().length() <= 0
                                ? getFileContentType(pair.getFileName())
                                : pair.getContentType();
                        if (null != getCharset() && getCharset().length() > 0) {
                            pairSb.append("Content-Type: ").append(pairContentType).append("; charset=" + getCharset())
                                    .append(LINE_END);
                        } else {
                            pairSb.append("Content-Type: ").append(pairContentType).append(LINE_END);
                        }
                        // 设置传输编码方式，非必须
                        pairSb.append("Content-Transfer-Encoding: binary").append(LINE_END);
                        pairSb.append(LINE_END);
                        if (null != getCharset() && getCharset().length() > 0) {
                            os.write(pairSb.toString().getBytes(getCharset()));
                        } else {
                            os.write(pairSb.toString().getBytes());
                        }
                        boolean tmpFlag = null == writeFileInterface ? writeBase64ContentData(pair.getBase64Content(), os)
                                : writeFileInterface.writeBase64ContentData(pair.getBase64Content(), os);
                        if (!tmpFlag) {
                            fileFlag = false;
                            break;
                        }
                        // 写入换行
                        if (null != getCharset() && getCharset().length() > 0) {
                            os.write(LINE_END.getBytes(getCharset()));
                        } else {
                            os.write(LINE_END.getBytes());
                        }
                    }

                }
            }
            if (fileFlag) {
                StringBuilder sbEnd = new StringBuilder();
                sbEnd.append(PREFIX).append(boundary).append(PREFIX).append(LINE_END);
                // 可能部分服务器不需要拼接此换行结尾
                sbEnd.append(LINE_END);
                if (null != getCharset() && getCharset().length() > 0) {
                    os.write(sbEnd.toString().getBytes(getCharset()));
                } else {
                    os.write(sbEnd.toString().getBytes());
                }
                os.flush();
                flag = true;
            } else {
                flag = false;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("Error:doOutputStream", e);
            flag = false;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:doOutputStream", e);
            flag = false;
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

    public boolean writeFileData(String filePath, BufferedOutputStream os) {
        boolean tmpFlag = false;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath));
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            tmpFlag = true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:writeFileData", e);
        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                log.error("Error:writeFileData", e2);
            }
        }
        return tmpFlag;
    }

    public boolean writeFileBytes(byte[] fileBytes, BufferedOutputStream os) {
        boolean tmpFlag = false;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(fileBytes);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = byteArrayInputStream.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            tmpFlag = true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:writeFileBytes", e);
        } finally {
            try {
                if (null != byteArrayInputStream) {
                    byteArrayInputStream.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                log.error("Error:writeFileBytes", e2);
            }
        }
        return tmpFlag;
    }

    public boolean writeBase64ContentData(String base64Content, BufferedOutputStream os) {
        boolean tmpFlag = false;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(Base64Util.getDecoder().decode(base64Content));
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = byteArrayInputStream.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            tmpFlag = true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:writeBase64ContentData", e);
        } finally {
            try {
                if (null != byteArrayInputStream) {
                    byteArrayInputStream.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                log.error("Error:writeBase64ContentData", e2);
            }
        }
        return tmpFlag;
    }

    @Override
    public String toString() {
        return "UrlMultiReqEntry [nameAndValuePairs=" + nameAndValuePairs + ", nameAndFilePairs=" + nameAndFilePairs
                + ", boundary=" + boundary + "]";
    }

}