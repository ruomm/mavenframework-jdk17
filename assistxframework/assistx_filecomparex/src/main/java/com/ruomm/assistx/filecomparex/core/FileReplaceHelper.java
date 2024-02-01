/**
 * @copyright wanruome-2017
 * @author 牛牛-wanruome@163.com
 * @create 2017年8月16日 上午11:14:37
 */
package com.ruomm.assistx.filecomparex.core;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.dal.FileInfoDal;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.corex.helper.FileInfoFilter;
import com.ruomm.javax.corex.helper.FileInfoHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.util.List;

public class FileReplaceHelper {

    private final static Log log = LogFactory.getLog(FileReplaceHelper.class);

    public interface ReplaceHelper extends FileInfoFilter {
        // 复制时候目标文件名称，如是返回空则不复制
        String destFileName(String fileKey, FileInfoDal newFileInfo);

        // 是否自定义复制
        boolean isCopyReplaceByDefine(String fileKey, FileInfoDal srcFileInfo);

        // 如是自定义复制，则使用自定义逻辑复制，返回true则成功，返回false则失败
        boolean doCopyReplaceByDefine(String fileKey, FileInfoDal srcFileInfo, String destPath);
    }

    private String srcPath;
    private String destPath;

    private boolean shaEnable;

    private ReplaceHelper replaceHelper;
    private boolean throwException;

    private boolean debug;
    private FileInfoHelper fileInfoSrc;


    public FileReplaceHelper() {
        this(null, null, null, false, false);
    }

    public FileReplaceHelper(String newPath, String oldPath) {
        this(null, newPath, oldPath, false, false);
    }

    public FileReplaceHelper(ReplaceHelper replaceHelper) {
        this(replaceHelper, null, null, false, false);
    }

    public FileReplaceHelper(ReplaceHelper replaceHelper, String srcPath, String destPath) {
        this(replaceHelper, srcPath, destPath, false, false);
    }

    public FileReplaceHelper(ReplaceHelper replaceHelper, String srcPath, String destPath, boolean shaEnable) {
        this(replaceHelper, srcPath, destPath, shaEnable, false);
    }

    public FileReplaceHelper(ReplaceHelper replaceHelper, String srcPath, String destPath, boolean shaEnable, boolean debug) {
        super();
        this.replaceHelper = replaceHelper;
        this.shaEnable = shaEnable;
        this.debug = debug;
        this.setSrcPath(srcPath);
        this.setDestPath(destPath);
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        FileInfoHelper fileInfoHelper = new FileInfoHelper();
        fileInfoHelper.setPath(srcPath);
        fileInfoHelper.setShowDir(false);
        this.srcPath = fileInfoHelper.getPath();
        this.fileInfoSrc = fileInfoHelper;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public ReplaceHelper getReplaceHelper() {
        return replaceHelper;
    }

    public void setReplaceHelper(ReplaceHelper replaceHelper) {
        this.replaceHelper = replaceHelper;
    }


    public boolean isShaEnable() {
        return shaEnable;
    }

    public void setShaEnable(boolean shaEnable) {
        this.shaEnable = shaEnable;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isThrowException() {
        return throwException;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

    public FileInfoHelper getFileInfoSrc() {
        return fileInfoSrc;
    }

    public void doCopyReplace() {
        this.fileInfoSrc.setThrowException(this.isThrowException());
        this.fileInfoSrc.setFileFilter(this.replaceHelper);
        List<FileInfoDal> listInfoSrc;
        if (shaEnable) {
            listInfoSrc = this.fileInfoSrc.getFileInfoList();
        } else {
            listInfoSrc = this.fileInfoSrc.getFileInfoListWithSha();
        }
        if (this.debug) {
            System.out.println("----------来源文件信息如下----------");
            for (FileInfoDal dal : listInfoSrc) {
                System.out.println(dal);
            }
        }
        int errorCout = 0;
        for (FileInfoDal fileInfoDal : listInfoSrc) {
            String destName;
            if (null != replaceHelper) {
                destName = replaceHelper.destFileName(fileInfoDal.getPathLite(), fileInfoDal);
            } else {
                destName = fileInfoDal.getPathLite();
            }
            if (null == destName) {
                if (isDebug()) {
                    System.out.println("不复制，因为定义的目标文件名为null，来源文件：" + fileInfoDal.getPathLite());
                }
                continue;
            }

            if (null != replaceHelper && replaceHelper.isCopyReplaceByDefine(fileInfoDal.getPathLite(), fileInfoDal)) {
                boolean copyResult = replaceHelper.doCopyReplaceByDefine(fileInfoDal.getPathLite(), fileInfoDal, FileUtils.concatFilePath(this.destPath, destName));
                if (!copyResult) {
                    errorCout++;
                }
                if (isDebug()) {
                    if (copyResult) {
                        System.out.println("替换复制模式-成功，来源文件：" + fileInfoDal.getPathLite() + "，目标文件：" + destName);
                    } else {
                        System.out.println("替换复制模式-成功，来源是失败：" + fileInfoDal.getPathLite() + "，目标文件：" + destName);
                    }
                }
            } else {
                File srcfile = new File(fileInfoDal.getPathFull());
                String destFilePath = FileUtils.concatFilePath(this.destPath, destName);
                File desfile = FileUtils.createFile(destFilePath);
                boolean copyResult = FileCopyUtils.copyFileBySha(replaceHelper, srcfile, desfile, fileInfoDal.getSha(), shaEnable);
                if (!copyResult) {
                    errorCout++;
                }
                if (isDebug()) {
                    if (copyResult) {
                        System.out.println("文件复制模式-成功，来源文件：" + fileInfoDal.getPathLite() + "，目标文件：" + destName);
                    } else {
                        System.out.println("文件复制模式-成功，来源是失败：" + fileInfoDal.getPathLite() + "，目标文件：" + destName);
                    }
                }
            }
        }
        if (errorCout > 0) {
            log.error("文件替换复制-执行中有部分文件失败，失败的文件数量为：" + errorCout);
            if (throwException) {
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "文件替换复制-执行中有部分文件失败，失败的文件数量为：" + errorCout);
            }
        } else {
            log.info("文件替换复制-执行完毕，输出路径为：" + this.destPath);
        }
    }

}
