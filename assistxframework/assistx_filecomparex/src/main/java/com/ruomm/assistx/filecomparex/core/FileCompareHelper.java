package com.ruomm.assistx.filecomparex.core;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.ListUtils;
import com.ruomm.javax.corex.dal.FileInfoDal;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.corex.helper.FileInfoFilter;
import com.ruomm.javax.corex.helper.FileInfoHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/28 18:28
 */
public class FileCompareHelper {
    private final static Log log = LogFactory.getLog(FileCompareHelper.class);

    public interface CompareHelper extends FileInfoFilter {
        // 是否自定义对比
        boolean isCompareByDefine(String fileKey, FileInfoDal newFileInfo, FileInfoDal oldFileInfo);

        // 如是自定义对比，返回true则一致，返回false则不一致
        boolean doCompareByDefine(String fileKey, FileInfoDal newFileInfo, FileInfoDal oldFileInfo);
    }

    private String newPath;
    private String oldPath;
    private CompareHelper compareHelper;

    private boolean shaEnable;

    private boolean debug;

    private boolean throwException;

    private FileInfoHelper fileInfoNew;
    private FileInfoHelper fileInfoOld;

    private Map<String, FileInfoDal> mapFilesInfoNew;
    private Map<String, FileInfoDal> mapFilesInfoOld;


    private List<String> lstNewFile;
    private List<String> lstModifyFile;
    private List<String> lstDeleteFile;
    private List<String> lstSameFile;
    private List<String> ignoreValues;


    public FileCompareHelper() {
        this(null, null, null, false, false);
    }

    public FileCompareHelper(String newPath, String oldPath) {
        this(null, newPath, oldPath, false, false);
    }

    public FileCompareHelper(CompareHelper compareHelper) {
        this(compareHelper, null, null, false, false);
    }

    public FileCompareHelper(CompareHelper compareHelper, String newPath, String oldPath) {
        this(compareHelper, newPath, oldPath, false, false);
    }

    public FileCompareHelper(CompareHelper compareHelper, String newPath, String oldPath, boolean shaEnable) {
        this(compareHelper, newPath, oldPath, shaEnable, false);
    }

    public FileCompareHelper(CompareHelper compareHelper, String newPath, String oldPath, boolean shaEnable, boolean debug) {
        super();
        this.compareHelper = compareHelper;
        this.shaEnable = shaEnable;
        this.debug = debug;
        this.setNewPath(newPath);
        this.setOldPath(oldPath);
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        FileInfoHelper fileInfoHelper = new FileInfoHelper();
        fileInfoHelper.setPath(newPath);
        fileInfoHelper.setShowDir(false);
        this.newPath = fileInfoHelper.getPath();
        this.fileInfoNew = fileInfoHelper;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        FileInfoHelper fileInfoHelper = new FileInfoHelper();
        fileInfoHelper.setPath(oldPath);
        fileInfoHelper.setShowDir(false);
        this.oldPath = fileInfoHelper.getPath();
        this.fileInfoOld = fileInfoHelper;
    }

    public CompareHelper getCompareHelper() {
        return compareHelper;
    }

    public void setCompareHelper(CompareHelper compareHelper) {
        this.compareHelper = compareHelper;
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

    public FileInfoHelper getFileInfoNew() {
        return fileInfoNew;
    }

    public FileInfoHelper getFileInfoOld() {
        return fileInfoOld;
    }

    public List<String> getLstNewFile() {
        return lstNewFile;
    }

    public List<String> getLstModifyFile() {
        return lstModifyFile;
    }

    public List<String> getLstDeleteFile() {
        return lstDeleteFile;
    }

    public List<String> getLstSameFile() {
        return lstSameFile;
    }

    public List<String> getIgnoreValues() {
        return ignoreValues;
    }

    public void setIgnoreValues(List<String> ignoreValues) {
        this.ignoreValues = ignoreValues;
    }

    public void setIgnoreValues(String... ignoreValues) {
        if (null == ignoreValues || ignoreValues.length <= 0) {
            this.ignoreValues = null;
        } else {
            List<String> ignoreValuesList = new ArrayList<>();
            for (String tmp : ignoreValues) {
                if (null != tmp && tmp.length() > 0) {
                    ignoreValuesList.add(tmp);
                }
            }
            this.ignoreValues = ignoreValuesList;
        }
    }

    public void doCompair() {
        this.fileInfoNew.setFileFilter(this.compareHelper);
        this.fileInfoOld.setFileFilter(this.compareHelper);
        this.fileInfoNew.setThrowException(this.throwException);
        this.fileInfoOld.setThrowException(this.throwException);
        List<String> tmpLstNewFile = new ArrayList<>();
        List<String> tmpLstModifyFile = new ArrayList<>();
        List<String> tmpLstDeleteFile = new ArrayList<>();
        List<String> tmpLstSameFile = new ArrayList<>();
        Map<String, FileInfoDal> mapInfoNew = this.fileInfoNew.getFileInfoMapWithSha();
        Set<String> keyInfoNew = mapInfoNew.keySet();
        if (this.debug) {
            System.out.println("----------新的文件信息如下----------");
            for (String key : keyInfoNew) {
                System.out.println(mapInfoNew.get(key).toString());
            }
        }
        Map<String, FileInfoDal> mapInfoOld = this.fileInfoOld.getFileInfoMapWithSha();
        Set<String> keyInfoOld = mapInfoOld.keySet();
        if (this.debug) {
            System.out.println("----------旧的文件信息如下----------");
            for (String key : keyInfoOld) {
                System.out.println(mapInfoOld.get(key).toString());
            }
        }
        for (String key : keyInfoOld) {
            if (isIngoreFile(key)) {
                continue;
            }
            if (!keyInfoNew.contains(key)) {
                tmpLstDeleteFile.add(key);
            } else {
                if (null != compareHelper && compareHelper.isCompareByDefine(key, mapInfoNew.get(key), mapInfoOld.get(key))) {
                    if (compareHelper.doCompareByDefine(key, mapInfoNew.get(key), mapInfoOld.get(key))) {
                        tmpLstSameFile.add(key);
                    } else {
                        tmpLstModifyFile.add(key);
                    }
                } else {
                    String shaNew = mapInfoNew.get(key).getSha();
                    String shaOld = mapInfoOld.get(key).getSha();
                    if (null != shaNew && shaNew.length() > 0 && shaNew.equals(shaOld)) {
                        tmpLstSameFile.add(key);
                    } else {
                        tmpLstModifyFile.add(key);
                    }
                }
            }
        }
        for (String key : keyInfoNew) {
            if (isIngoreFile(key)) {
                continue;
            }
            if (!keyInfoOld.contains(key)) {
                tmpLstNewFile.add(key);
            }
        }
        this.lstNewFile = tmpLstNewFile;
        this.lstSameFile = tmpLstSameFile;
        this.lstModifyFile = tmpLstModifyFile;
        this.lstDeleteFile = tmpLstDeleteFile;
        this.mapFilesInfoNew = mapInfoNew;
        this.mapFilesInfoOld = mapInfoOld;
        if (this.debug) {
            System.out.println("----------相同的文件信息如下----------");
            if (ListUtils.isEmpty(this.lstSameFile)) {
                System.out.println("没有相同的文件");
            } else {
                for (String fileKey : this.lstSameFile) {
                    System.out.println(fileKey);
                }
            }
            System.out.println("----------新增的文件信息如下----------");
            if (ListUtils.isEmpty(this.lstNewFile)) {
                System.out.println("没有新增的文件");
            } else {
                for (String fileKey : this.lstNewFile) {
                    System.out.println(fileKey);
                }
            }
            System.out.println("----------删除的文件信息如下----------");
            if (ListUtils.isEmpty(this.lstDeleteFile)) {
                System.out.println("没有删除的文件");
            } else {
                for (String fileKey : this.lstDeleteFile) {
                    System.out.println(fileKey);
                }
            }
            System.out.println("----------修改的文件信息如下----------");
            if (ListUtils.isEmpty(this.lstModifyFile)) {
                System.out.println("没有修改的文件");
            } else {
                for (String fileKey : this.lstModifyFile) {
                    System.out.println(fileKey);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        sb.append("--------------------------------").append("\r\n");
        sb.append("文件对比完成").append("\r\n");
        sb.append("新的文件路径：" + this.newPath).append("\r\n");
        sb.append("旧的文件路径：" + this.oldPath).append("\r\n");
        sb.append("相同的文件个数：" + ListUtils.getSize(this.lstSameFile)).append("\r\n");
        sb.append("删除的文件个数：" + ListUtils.getSize(this.lstDeleteFile)).append("\r\n");
        sb.append("新增的文件个数：" + ListUtils.getSize(this.lstNewFile)).append("\r\n");
        sb.append("修改的文件个数：" + ListUtils.getSize(this.lstModifyFile)).append("\r\n");
        sb.append("--------------------------------");
        log.info(sb.toString());
    }

    public void copyModifyFile(String desPath) {
        String tag = "新的修改的文件";
        if (!ListUtils.isEmpty(this.getLstModifyFile())) {
            int errorCout = 0;
            for (String key : this.getLstModifyFile()) {
                FileUtils.makeDirs(desPath + key);
                File srcfile = new File(this.getNewPath() + key);
                File desfile = new File(desPath + key);
                boolean copyResult = FileCopyUtils.copyFileBySha(this.compareHelper, srcfile, desfile, this.mapFilesInfoNew.get(key).getSha(), shaEnable);
                if (!copyResult) {
                    errorCout++;
                }
                if (this.debug) {
                    if (copyResult) {
                        System.out.println(tag + "-复制成功，文件相对路径：" + key);
                    } else {
                        System.out.println(tag + "-复制失败，文件相对路径：" + key);
                    }
                }
            }
            if (errorCout > 0) {
                log.error(tag + "-复制完毕，失败的文件数量为：" + errorCout + "，输出路径为：" + desPath);
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, tag + "-复制中有部分文件复制失败，失败的文件数量为：" + errorCout);
                }
            } else {
                log.info(tag + "-复制完毕，输出路径为：" + desPath);
            }
        } else {
            log.info(tag + "-列表为空，不需要复制");
        }
    }

    public void copyModifyFileOld(String desPath) {
        String tag = "旧的修改的文件";
        if (!ListUtils.isEmpty(this.getLstModifyFile())) {
            int errorCout = 0;
            for (String key : this.getLstModifyFile()) {
                FileUtils.makeDirs(desPath + key);
                File srcfile = new File(this.getOldPath() + key);
                File desfile = new File(desPath + key);
                boolean copyResult = FileCopyUtils.copyFileBySha(this.compareHelper, srcfile, desfile, this.mapFilesInfoOld.get(key).getSha(), shaEnable);
                if (!copyResult) {
                    errorCout++;
                }
                if (this.debug) {
                    if (copyResult) {
                        System.out.println(tag + "-复制成功，文件相对路径：" + key);
                    } else {
                        System.out.println(tag + "-复制失败，文件相对路径：" + key);
                    }
                }
            }
            if (errorCout > 0) {
                log.error(tag + "-复制完毕，失败的文件数量为：" + errorCout + "，输出路径为：" + desPath);
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, tag + "-复制中有部分文件复制失败，失败的文件数量为：" + errorCout);
                }
            } else {
                log.info(tag + "-复制完毕，输出路径为：" + desPath);
            }
        } else {
            log.info(tag + "-列表为空，不需要复制");
        }
    }

    public void copyDeleteFile(String desPath) {
        String tag = "删除的文件";
        if (!ListUtils.isEmpty(this.getLstDeleteFile())) {
            int errorCout = 0;
            for (String key : this.getLstDeleteFile()) {
                FileUtils.makeDirs(desPath + key);
                File srcfile = new File(this.getOldPath() + key);
                File desfile = new File(desPath + key);
                boolean copyResult = FileCopyUtils.copyFileBySha(this.compareHelper, srcfile, desfile, this.mapFilesInfoOld.get(key).getSha(), shaEnable);
                if (!copyResult) {
                    errorCout++;
                }
                if (this.debug) {
                    if (copyResult) {
                        System.out.println(tag + "-复制成功，文件相对路径：" + key);
                    } else {
                        System.out.println(tag + "-复制失败，文件相对路径：" + key);
                    }
                }
            }
            if (errorCout > 0) {
                log.error(tag + "-复制完毕，失败的文件数量为：" + errorCout + "，输出路径为：" + desPath);
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, tag + "-复制中有部分文件复制失败，失败的文件数量为：" + errorCout);
                }
            } else {
                log.info(tag + "-复制完毕，输出路径为：" + desPath);
            }
        } else {
            log.info(tag + "-列表为空，不需要复制");
        }
    }

    public void copyNewFile(String desPath) {
        String tag = "新增的文件";
        if (!ListUtils.isEmpty(this.getLstNewFile())) {
            int errorCout = 0;
            for (String key : this.getLstNewFile()) {
                FileUtils.makeDirs(desPath + key);
                File srcfile = new File(this.getNewPath() + key);
                File desfile = new File(desPath + key);
                boolean copyResult = FileCopyUtils.copyFileBySha(this.compareHelper, srcfile, desfile, this.mapFilesInfoNew.get(key).getSha(), shaEnable);
                if (!copyResult) {
                    errorCout++;
                }
                if (this.debug) {
                    if (copyResult) {
                        System.out.println(tag + "-复制成功，文件相对路径：" + key);
                    } else {
                        System.out.println(tag + "-复制失败，文件相对路径：" + key);
                    }
                }
            }
            if (errorCout > 0) {
                log.error(tag + "-复制完毕，失败的文件数量为：" + errorCout + "，输出路径为：" + desPath);
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, tag + "-复制中有部分文件复制失败，失败的文件数量为：" + errorCout);
                }
            } else {
                log.info(tag + "-复制完毕，输出路径为：" + desPath);
            }
        } else {
            log.info(tag + "-列表为空，不需要复制");
        }
    }

    public void copySameFile(String desPath) {
        String tag = "相同的文件";
        if (!ListUtils.isEmpty(this.getLstSameFile())) {
            int errorCout = 0;
            for (String key : this.getLstSameFile()) {
                FileUtils.makeDirs(desPath + key);
                File srcfile = new File(this.getNewPath() + key);
                File desfile = new File(desPath + key);
                boolean copyResult = FileCopyUtils.copyFileBySha(this.compareHelper, srcfile, desfile, this.mapFilesInfoNew.get(key).getSha(), shaEnable);
                if (!copyResult) {
                    errorCout++;
                }
                if (this.debug) {
                    if (copyResult) {
                        System.out.println(tag + "-复制成功，文件相对路径：" + key);
                    } else {
                        System.out.println(tag + "-复制失败，文件相对路径：" + key);
                    }
                }
            }
            if (errorCout > 0) {
                log.error(tag + "-复制完毕，失败的文件数量为：" + errorCout + "，输出路径为：" + desPath);
                if (throwException) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, tag + "-复制中有部分文件复制失败，失败的文件数量为：" + errorCout);
                }
            } else {
                log.info(tag + "-复制完毕，输出路径为：" + desPath);
            }
        } else {
            log.info(tag + "-列表为空，不需要复制");
        }
    }

    private boolean isIngoreFile(String key) {
        if (null == ignoreValues || ignoreValues.size() <= 0) {
            return false;
        }
        boolean isIngore = false;
        for (String fileIngoreStr : ignoreValues) {
            if (isIngoreFile(key, fileIngoreStr)) {
                isIngore = true;
                break;
            }
        }
        return isIngore;
    }

    private static boolean isIngoreFile(String fileKeyStr, String fileIngoreStr) {
        if (null == fileKeyStr || fileKeyStr.length() <= 0) {
            return false;
        }
        if (null == fileIngoreStr || fileIngoreStr.length() <= 0) {
            return false;
        }
        String realKey = fileKeyStr.toLowerCase().replace("\\", "/");
        if (null != realKey && realKey.startsWith("/")) {
            realKey = realKey.substring(1);
        }
        if (null == realKey || realKey.length() <= 0) {
            return false;
        }
        String regexStr = fileIngoreStr.toLowerCase().replace("\\", "/");
        if (regexStr.startsWith("*") && regexStr.endsWith("*")) {
            if (regexStr.length() <= 1) {
                return false;
            }
            String regStr = regexStr.substring(1);
            int subSize = regStr.length() - 1;
            regStr = regStr.substring(0, subSize);
            if (null == regStr || regStr.length() <= 0) {
                return false;
            }
            if (realKey.contains(regStr)) {
                return true;
            } else {
                return false;
            }
        } else if (regexStr.startsWith("*")) {
            String regStr = regexStr.substring(1);
            if (null == regStr || regStr.length() <= 0) {
                return false;
            }
            if (realKey.endsWith(regStr)) {
                return true;
            } else {
                return false;
            }
        } else if (regexStr.endsWith("*")) {
            int subSize = regexStr.length() - 1;
            String regStr = regexStr.substring(0, subSize);
            if (null != regStr && regStr.startsWith("/")) {
                regStr = regStr.substring(1);
            }
            if (null == regStr || regStr.length() <= 0) {
                return false;
            }
            if (realKey.startsWith(regStr)) {
                return true;
            } else {
                return false;
            }
        } else {
            String regStr = regexStr.startsWith("/") ? regexStr.substring(1) : regexStr;
            if (null == regStr || regStr.length() <= 0) {
                return false;
            }
            if (realKey.equals(regStr)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
