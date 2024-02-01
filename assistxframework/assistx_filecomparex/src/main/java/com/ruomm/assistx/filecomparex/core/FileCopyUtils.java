package com.ruomm.assistx.filecomparex.core;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.helper.FileInfoFilter;

import java.io.File;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/30 11:32
 */
class FileCopyUtils {

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath 文件filePath
     */
    public static String calSha(FileInfoFilter fileFilter, String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        if (null != fileFilter && fileFilter.isSignByDefine()) {
            return fileFilter.doSignByDefine(filePath);
        } else {
            return FileUtils.calSha(filePath);
        }

    }

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param file 文件filePath
     */
    public static String calSha(FileInfoFilter fileFilter, File file) {
        if (null == file || !file.exists() || !file.isFile()) {
            return null;
        }
        if (null != fileFilter && fileFilter.isSignByDefine()) {
            return fileFilter.doSignByDefine(file.getPath());
        } else {
            return FileUtils.calSha(file);
        }
    }

    /**
     * 校验文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath  文件路径
     * @param shaResult 指纹校验值(MD5 、 SHA1等算法)
     * @return 校验结果
     */
    public static boolean verifySha(FileInfoFilter fileFilter, String filePath, String shaResult) {
        if (StringUtils.isEmpty(shaResult)) {
            return false;
        }
        String shaByFile = calSha(fileFilter, filePath);
        if (StringUtils.isEmpty(shaByFile)) {
            return false;
        } else {
            return shaResult.equals(shaByFile);
        }

    }

    /**
     * 校验文件指纹校验值(MD5、SHA1等算法)
     *
     * @param file      文件
     * @param shaResult 指纹校验值(MD5 、 SHA1等算法)
     * @return 校验结果
     */
    public static boolean verifySha(FileInfoFilter fileFilter, File file, String shaResult) {
        if (StringUtils.isEmpty(shaResult)) {
            return false;
        }
        String shaByFile = calSha(fileFilter, file);
        if (StringUtils.isEmpty(shaByFile)) {
            return false;
        } else {
            return shaResult.equals(shaByFile);
        }
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 目标文件路径
     * @param desfilePath 目标文件路径
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @paramDefault isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @paramDefault timeCopy 是否复制修改时间
     */
    public static boolean copyFileBySha(FileInfoFilter fileFilter, String srcfilePath, String desfilePath, String shaResult, boolean shaEnable) {
        boolean copyResult = FileUtils.copyFile(srcfilePath, desfilePath, true, true);
        if (!copyResult) {
            return false;
        } else if (shaEnable) {
            boolean verifyReuslt = verifySha(fileFilter, desfilePath, shaResult);
            if (!verifyReuslt) {
                FileUtils.delFile(desfilePath);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 复制文件
     *
     * @param srcfile 源文件
     * @param desfile 目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @paramDefault isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @paramDefault timeCopy 是否复制修改时间
     */
    public static boolean copyFileBySha(FileInfoFilter fileFilter, File srcfile, File desfile, String shaResult, boolean shaEnable) {
        boolean copyResult = FileUtils.copyFile(srcfile, desfile, true, true);
        if (!copyResult) {
            return false;
        } else if (shaEnable) {
            boolean verifyReuslt = verifySha(fileFilter, desfile, shaResult);
            if (!verifyReuslt) {
                FileUtils.delFile(desfile);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
