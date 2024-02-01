package com.ruomm.javax.httpx.dal;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/28 9:22
 */
public class HttpMultipartBuilder {
    private final static Log log = LogFactory.getLog(HttpMultipartBuilder.class);
    private List<HttpxMultipart> lstMultipartDal = new ArrayList<>();

    public void addPart(String name, String value) {
        if (null == name || name.length() <= 0) {
            throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
        }
        HttpxMultipart multipartDal = new HttpxMultipart();
        multipartDal.setName(name);
        multipartDal.setValue(value);
        lstMultipartDal.add(multipartDal);
    }

    public void addPart(String name, String fileName, String filePath) {
        if (null == name || name.length() <= 0) {
            throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
        }
        HttpxMultipart multipartDal = new HttpxMultipart();
        multipartDal.setName(name);
        multipartDal.setFileName(fileName);
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            throw new RuntimeException("multipart/form-data添加的文件不存在");
        }
        if (!file.isFile()) {
            throw new RuntimeException("multipart/form-data添加的文件不正确，不是一个文件");
        }
        multipartDal.setFile(file);
        lstMultipartDal.add(multipartDal);
    }

    public void addPart(String name, String fileName, File file) {
        if (null == name || name.length() <= 0) {
            throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
        }
        HttpxMultipart multipartDal = new HttpxMultipart();
        multipartDal.setName(name);
        multipartDal.setFileName(fileName);
        if (null == file || !file.exists()) {
            throw new RuntimeException("multipart/form-data添加的文件不存在");
        }
        if (!file.isFile()) {
            throw new RuntimeException("multipart/form-data添加的文件不正确，不是一个文件");
        }
        multipartDal.setFile(file);
        lstMultipartDal.add(multipartDal);
    }

    public void addBytes(String name, String fileName, byte[] fileBytes) {
        if (null == name || name.length() <= 0) {
            throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
        }
        HttpxMultipart multipartDal = new HttpxMultipart();
        multipartDal.setName(name);
        multipartDal.setFileName(fileName);
        if (null == fileBytes || fileBytes.length <= 0) {
            throw new RuntimeException("multipart/form-data添加的文件不正确，文件byte[]内容为空");
        }
        multipartDal.setFileBytes(fileBytes);
        lstMultipartDal.add(multipartDal);
    }

    public void addPartBase64Content(String name, String fileName, String base64Content) {
        if (null == name || name.length() <= 0) {
            throw new RuntimeException("multipart/form-data添加的参数名称不能为空");
        }
        HttpxMultipart multipartDal = new HttpxMultipart();
        multipartDal.setName(name);
        multipartDal.setFileName(fileName);
        if (null == base64Content || base64Content.length() <= 0) {
            throw new RuntimeException("multipart/form-data添加的文件不正确，文件base64Content内容为空");
        }
        multipartDal.setBase64Content(base64Content);
        lstMultipartDal.add(multipartDal);
    }

    public List<HttpxMultipart> builder() {
        return this.lstMultipartDal;
    }

}
