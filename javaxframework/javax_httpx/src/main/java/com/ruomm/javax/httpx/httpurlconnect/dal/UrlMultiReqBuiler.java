/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月12日 下午2:55:27
 */
package com.ruomm.javax.httpx.httpurlconnect.dal;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;

import java.util.ArrayList;
import java.util.UUID;

public class UrlMultiReqBuiler {
    private UrlMultiReqEntry urlMultiReqEntry;
    private boolean isPutEmpty = false;

    public UrlMultiReqBuiler() {
        this(null, false);
    }

    public UrlMultiReqBuiler(String charsetName) {
        this(charsetName, false);
    }

    public UrlMultiReqBuiler(String charsetName, boolean isPutEmpty) {
        super();
        this.urlMultiReqEntry = new UrlMultiReqEntry();
        String realCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "utf-8");
        this.urlMultiReqEntry.setCharset(realCharsetName);
        this.urlMultiReqEntry.setMimeType("multipart/form-data");
//		this.urlMultiReqEntry.setBoundary("----------------" + UUID.randomUUID() + "----------------");// 16headers
        this.urlMultiReqEntry.setBoundary("--------" + UUID.randomUUID() + "--------");// 8headers
        this.urlMultiReqEntry.setUrlEncode(false);
        this.isPutEmpty = isPutEmpty;
    }

    public UrlMultiReqEntry build() {
//		this.urlMultiReqEntry.setBoundary("----------------" + UUID.randomUUID() + "----------------");// 16headers
        this.urlMultiReqEntry.setBoundary("--------" + UUID.randomUUID() + "--------");// 8headers
        return this.urlMultiReqEntry;
    }

    public UrlMultiReqBuiler setUrlEncode(boolean isUrlEncode) {
        this.urlMultiReqEntry.setUrlEncode(isUrlEncode);
        return this;
    }

    public UrlMultiReqBuiler setWriteFileInterface(UrlMultiReqWriteFileInterface writeFileInterface) {
        this.urlMultiReqEntry.setWriteFileInterface(writeFileInterface);
        return this;
    }

    public UrlMultiReqBuiler add(String name, String value) {
        if (null == name || name.length() <= 0) {
            return this;
        }
        if (null == value || value.length() <= 0) {
            if (!isPutEmpty) {
                return this;
            }
        }
        if (null == urlMultiReqEntry.getNameAndValuePairs()) {
            urlMultiReqEntry.setNameAndValuePairs(new ArrayList<UrlNameAndValuePair>());
        }
        UrlNameAndValuePair objPair = new UrlNameAndValuePair();
        objPair.setName(name);
        objPair.setValue(StringUtils.nullStrToEmpty(value));
        UrlNameAndValuePair repeatObj = null;
        for (UrlNameAndValuePair tmp : urlMultiReqEntry.getNameAndValuePairs()) {
            if (objPair.getName().equals(tmp.getName())) {
                repeatObj = tmp;
                break;
            }
        }
        if (null == repeatObj) {
            urlMultiReqEntry.getNameAndValuePairs().add(objPair);
        } else {
            urlMultiReqEntry.getNameAndValuePairs().remove(repeatObj);
            urlMultiReqEntry.getNameAndValuePairs().add(objPair);
        }
        return this;
    }

    public UrlMultiReqBuiler add(String name, String fileName, String file) {
        if (null == name || name.length() <= 0) {
            return this;
        }
        if (null == file || file.length() <= 0) {
            return this;
        }
        String realFileName = null;
        if (null == fileName || fileName.length() <= 0) {
            realFileName = FileUtils.getFileName(file);
        } else {
            realFileName = fileName;
        }
        if (null == realFileName || realFileName.length() <= 0) {
            return this;
        }
        if (null == urlMultiReqEntry.getNameAndFilePairs()) {
            urlMultiReqEntry.setNameAndFilePairs(new ArrayList<UrlNameAndFilePair>());
        }
        UrlNameAndFilePair objPair = new UrlNameAndFilePair();
        objPair.setName(name);
        objPair.setFileName(realFileName);
        objPair.setFilePath(file);
        UrlNameAndFilePair repeatObj = null;
        for (UrlNameAndFilePair tmp : urlMultiReqEntry.getNameAndFilePairs()) {
            if (objPair.getName().equals(tmp.getName()) && objPair.getFileName().equalsIgnoreCase(tmp.getFileName())) {
                repeatObj = tmp;
                break;
            }
        }
        if (null == repeatObj) {
            urlMultiReqEntry.getNameAndFilePairs().add(objPair);
        } else {
            urlMultiReqEntry.getNameAndFilePairs().remove(repeatObj);
            urlMultiReqEntry.getNameAndFilePairs().add(objPair);
        }

        return this;
    }

    public UrlMultiReqBuiler addBytes(String name, String fileName, byte[] fileBytes) {
        if (null == name || name.length() <= 0) {
            return this;
        }
        if (null == fileBytes || fileBytes.length <= 0) {
            return this;
        }
        String realFileName = null;
        if (null == fileName || fileName.length() <= 0) {
            realFileName = name + ".tmp_upload";
        } else {
            realFileName = fileName;
        }
        if (null == realFileName || realFileName.length() <= 0) {
            return this;
        }
        if (null == urlMultiReqEntry.getNameAndFilePairs()) {
            urlMultiReqEntry.setNameAndFilePairs(new ArrayList<UrlNameAndFilePair>());
        }
        UrlNameAndFilePair objPair = new UrlNameAndFilePair();
        objPair.setName(name);
        objPair.setFileName(realFileName);
        objPair.setFileBytes(fileBytes);
        UrlNameAndFilePair repeatObj = null;
        for (UrlNameAndFilePair tmp : urlMultiReqEntry.getNameAndFilePairs()) {
            if (objPair.getName().equals(tmp.getName()) && objPair.getFileName().equalsIgnoreCase(tmp.getFileName())) {
                repeatObj = tmp;
                break;
            }
        }
        if (null == repeatObj) {
            urlMultiReqEntry.getNameAndFilePairs().add(objPair);
        } else {
            urlMultiReqEntry.getNameAndFilePairs().remove(repeatObj);
            urlMultiReqEntry.getNameAndFilePairs().add(objPair);
        }

        return this;
    }

    public UrlMultiReqBuiler addBase64Content(String name, String fileName, String base64Content) {
        if (null == name || name.length() <= 0) {
            return this;
        }
        if (null == base64Content || base64Content.length() <= 0) {
            return this;
        }
        String realFileName = null;
        if (null == fileName || fileName.length() <= 0) {
            realFileName = name + ".tmp_upload";
        } else {
            realFileName = fileName;
        }
        if (null == realFileName || realFileName.length() <= 0) {
            return this;
        }
        if (null == urlMultiReqEntry.getNameAndFilePairs()) {
            urlMultiReqEntry.setNameAndFilePairs(new ArrayList<UrlNameAndFilePair>());
        }
        UrlNameAndFilePair objPair = new UrlNameAndFilePair();
        objPair.setName(name);
        objPair.setFileName(realFileName);
        objPair.setBase64Content(base64Content);
        UrlNameAndFilePair repeatObj = null;
        for (UrlNameAndFilePair tmp : urlMultiReqEntry.getNameAndFilePairs()) {
            if (objPair.getName().equals(tmp.getName()) && objPair.getFileName().equalsIgnoreCase(tmp.getFileName())) {
                repeatObj = tmp;
                break;
            }
        }
        if (null == repeatObj) {
            urlMultiReqEntry.getNameAndFilePairs().add(objPair);
        } else {
            urlMultiReqEntry.getNameAndFilePairs().remove(repeatObj);
            urlMultiReqEntry.getNameAndFilePairs().add(objPair);
        }

        return this;
    }
}