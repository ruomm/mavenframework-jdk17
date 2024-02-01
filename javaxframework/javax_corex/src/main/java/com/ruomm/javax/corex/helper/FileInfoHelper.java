package com.ruomm.javax.corex.helper;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.ListUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.dal.FileInfoDal;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/26 23:55
 */
public class FileInfoHelper {
    private final static Log log = LogFactory.getLog(FileInfoHelper.class);

    /**
     * 文件或文件夹路径
     */
    private String path;
    private int pathLength;
    /**
     * 是否列出文件夹
     */
    private boolean showDir;

    // 文件过滤器
    private FileInfoFilter fileFilter;

    // 文件信息列表
    private List<FileInfoDal> fileInfoList;

    private boolean throwException;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (null == path || path.length() == 0) {
            this.path = null;
        }
        try {
            File tmpFile = new File(path);
            this.path = tmpFile.getPath();
            log.info("FileInfoHelper.setPath->文件或文件夹输入的路径为：" + path + "，转换后的路径为：" + this.path);
        } catch (Exception e) {
            log.error("FileInfoHelper.setPath->文件或文件夹输入的路径不是有效的文件路径，输入的路径为：" + path);
            this.path = path;
            if (throwException) {
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "文件或文件夹输入的路径不是有效的文件路径，输入的路径为：" + path, e);
            }
        }
        this.pathLength = StringUtils.getLength(this.path);
    }

    public void setShowDir(boolean showDir) {
        this.showDir = showDir;
    }

    public boolean isThrowException() {
        return throwException;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }


    public void setFileFilter(FileInfoFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public List<FileInfoDal> getFileInfoList() {
        return getFileInfoList(false);
    }

    public List<FileInfoDal> getFileInfoList(boolean force) {
        if (!force && null != this.fileInfoList) {
            return fileInfoList;
        }
        if (null == fileInfoList) {
            fileInfoList = new ArrayList<>();
        }
        fileInfoList.clear();
        try {
            if (null != path && path.length() > 0) {
                getFile(new File(path));
            }
        } catch (Exception e) {
            fileInfoList.clear();
            log.error("FileInfoHelper.getFileInfos->获取所有文件信息时候发生异常，文件路径为：" + this.path);
            if (throwException) {
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "获取所有文件信息时候发生异常，文件路径为：" + this.path, e);
            }
        }
        return fileInfoList;
    }

    public List<FileInfoDal> getFileInfoListWithSha() {
        getFileInfoList(false);
        calFilesSha();
        return this.fileInfoList;
    }

    public List<FileInfoDal> getFileInfoListWithSha(boolean force) {
        getFileInfoList(force);
        calFilesSha();
        return this.fileInfoList;
    }

    public Map<String, FileInfoDal> getFileInfoMap() {
        getFileInfoList(false);
        return transToMap();
    }

    public Map<String, FileInfoDal> getFileInfoMap(boolean force) {
        getFileInfoList(force);
        return transToMap();
    }

    public Map<String, FileInfoDal> getFileInfoMapWithSha() {
        getFileInfoList(false);
        calFilesSha();
        return transToMap();
    }

    public Map<String, FileInfoDal> getFileInfoMapWithSha(boolean force) {
        getFileInfoList(force);
        calFilesSha();
        return transToMap();
    }

    public Map<String, FileInfoDal> transToMap() {
        if (null == this.fileInfoList) {
            return null;
        }
        Map<String, FileInfoDal> fileInfoMap = new HashMap<>();
        for (FileInfoDal fileInfoDal : this.fileInfoList) {
            fileInfoMap.put(fileInfoDal.getPathLite(), fileInfoDal);
        }
        return fileInfoMap;
    }

    private void getFile(File file) {
        if (null == file || !file.exists()) {
            return;
        } else if (file.isFile()) {
            String tmpPathFull = file.getPath();
            String tmpPathLite = tmpPathFull.length() >= this.pathLength ? tmpPathFull.substring(this.pathLength) : null;
            if (StringUtils.isEmpty(tmpPathFull) || null == tmpPathLite) {
                return;
            }
            if (null != fileFilter && fileFilter.isFilter(tmpPathFull, tmpPathLite)) {
                return;
            }
            FileInfoDal fileInfoDal = new FileInfoDal();
            fileInfoDal.setPathFull(tmpPathFull);
            fileInfoDal.setPathLite(tmpPathLite);
            fileInfoDal.setDirectory(false);
            fileInfoList.add(fileInfoDal);
        } else if (file.isDirectory()) {
            String tmpDirFull = file.getPath();
            String tmpDirLite = tmpDirFull.length() >= this.pathLength ? tmpDirFull.substring(this.pathLength) : null;
            if (StringUtils.isEmpty(tmpDirFull) || null == tmpDirLite) {
                return;
            }
            if (null != fileFilter && fileFilter.isFilter(tmpDirFull, tmpDirLite)) {
                return;
            } else if (showDir) {
                FileInfoDal fileInfoDal = new FileInfoDal();
                fileInfoDal.setPathFull(tmpDirFull);
                fileInfoDal.setPathLite(tmpDirLite);
                fileInfoDal.setDirectory(true);
                fileInfoList.add(fileInfoDal);
            }
            File[] files = file.listFiles();
            if (null != files && files.length <= 0) {
                return;
            }
            for (File tmp : files) {
                getFile(tmp);
            }
        }
    }

    // 计算HASH值
    public void calFilesSha() {
        if (null == this.fileInfoList || this.fileInfoList.size() <= 0) {
            return;
        }
        for (FileInfoDal infoDal : fileInfoList) {
            try {
                if (infoDal.isDirectory()) {
                    infoDal.setSha(null);
                } else if (!StringUtils.isEmpty(infoDal.getSha())) {
                    continue;
                } else {
                    infoDal.setSha(calSha(infoDal.getPathFull()));
                }
            } catch (Exception e) {
                log.error("FileInfoHelper.calFilesSha->文件SHA值计算异常，文件可能不存在了，文件路径：" + infoDal.getPathFull());
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "文件SHA值计算异常，文件可能不存在了，文件路径：" + infoDal.getPathFull(), e);
                }
            }
        }
    }

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath 文件filePath
     */
    private String calSha(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        if (null != fileFilter && fileFilter.isSignByDefine()) {
            return fileFilter.doSignByDefine(filePath);
        } else {
            return FileUtils.calSha(filePath);
        }

    }
//
//    /**
//     * 获取文件指纹校验值(MD5、SHA1等算法)
//     *
//     * @param file 文件filePath
//     */
//    public String calSha(File file) {
//        if (null == file || !file.exists() || !file.isFile()) {
//            return null;
//        }
//        if (null != fileFilter && fileFilter.isSignByDefine()) {
//            return fileFilter.doSignByDefine(file.getPath());
//        } else {
//            return FileUtils.calSha(file);
//        }
//    }
//
//    /**
//     * 校验文件指纹校验值(MD5、SHA1等算法)
//     *
//     * @param filePath  文件路径
//     * @param shaResult 指纹校验值(MD5 、 SHA1等算法)
//     * @return 校验结果
//     */
//    public boolean verifySha(String filePath, String shaResult) {
//        if (StringUtils.isEmpty(shaResult)) {
//            return false;
//        }
//        String shaByFile = calSha(filePath);
//        if (StringUtils.isEmpty(shaByFile)) {
//            return false;
//        } else {
//            return shaResult.equals(shaByFile);
//        }
//
//    }
//
//    /**
//     * 校验文件指纹校验值(MD5、SHA1等算法)
//     *
//     * @param file      文件
//     * @param shaResult 指纹校验值(MD5 、 SHA1等算法)
//     * @return 校验结果
//     */
//    public boolean verifySha(File file, String shaResult) {
//        if (StringUtils.isEmpty(shaResult)) {
//            return false;
//        }
//        String shaByFile = calSha(file);
//        if (StringUtils.isEmpty(shaByFile)) {
//            return false;
//        } else {
//            return shaResult.equals(shaByFile);
//        }
//    }

    /**
     * 计算文件或者文件夹的大小 ，单位 Bytes
     *
     * @return 大小，单位：Bytes
     */
    public long getSizeBytes() {
        if (null == this.fileInfoList || this.fileInfoList.size() <= 0) {
            return 0;
        }
        long ss = 0;
        for (FileInfoDal infoDal : fileInfoList) {
            if (infoDal.isDirectory()) {
                continue;
            }
            try {
                ss = ss + new File(infoDal.getPathFull()).length();
            } catch (Exception e) {
                log.error("FileInfoHelper.getSizeBytes->文件大小无法正确获取，文件可能不存在了，文件路径：" + infoDal.getPathFull());
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "文件大小无法正确获取，文件可能不存在了，文件路径：" + infoDal.getPathFull(), e);
                }
            }
        }
        return ss;
    }

    /**
     * 计算文件或者文件夹的大小 ，单位 MB
     *
     * @return 大小，单位：MB
     */
    public double getSizeMB() {
        double ss = getSizeBytes() * 1.0 / 1024 / 1024;
        return ss;
    }

    /**
     * 获取文件数量
     */

    public int counterFileNum() {
        if (!showDir) {
            return ListUtils.getSize(this.fileInfoList);
        } else {
            int count = 0;
            for (FileInfoDal infoDal : fileInfoList) {
                if (!infoDal.isDirectory()) {
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * 获取文件夹数量
     */

    public int counterDirNum() {
        if (!showDir) {
            return 0;
        } else {
            int count = 0;
            for (FileInfoDal infoDal : fileInfoList) {
                if (infoDal.isDirectory()) {
                    count++;
                }
            }
            return count;
        }
    }
}
