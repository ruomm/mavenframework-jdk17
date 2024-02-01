/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月17日 下午2:04:27
 */
package com.ruomm.assistx.filecomparex.utils;

import com.ruomm.assistx.filecomparex.core.FileCompareHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FileCompareUtils {
    private final static Log log = LogFactory.getLog(FileCompareUtils.class);
    private final static boolean CONFIG_DEBUG = false;
    private final static boolean CONFIG_SHA = false;
    private final static SimpleDateFormat CONFIG_SDF = new SimpleDateFormat("yyyyMMddHHmm");

    public static void doCompare(String newPath, String oldPath, String outPath) {
        doCompareProgress(null, newPath, oldPath, outPath, CONFIG_SDF, CONFIG_SHA, CONFIG_DEBUG, null);
    }

    public static void doCompare(String newPath, String oldPath, String outPath, SimpleDateFormat dataDateFormat) {
        doCompareProgress(null, newPath, oldPath, outPath, dataDateFormat, CONFIG_SHA, CONFIG_DEBUG, null);
    }

    public static void doCompare(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                 String outPath) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, CONFIG_SDF, CONFIG_SHA, CONFIG_DEBUG, null);
    }

    public static void doCompare(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                 String outPath, SimpleDateFormat dataDateFormat) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, dataDateFormat, CONFIG_SHA, CONFIG_DEBUG, null);
    }

    public static void doCompare(String newPath, String oldPath, String outPath, SimpleDateFormat dataDateFormat,
                                 boolean shaEnable) {
        doCompareProgress(null, newPath, oldPath, outPath, dataDateFormat, shaEnable, CONFIG_DEBUG, null);
    }

    public static void doCompare(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                 String outPath, SimpleDateFormat dataDateFormat, boolean shaEnable) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, dataDateFormat, shaEnable, CONFIG_DEBUG, null);
    }

    public static void doCompare(String newPath, String oldPath, String outPath, SimpleDateFormat dataDateFormat,
                                 boolean shaEnable, boolean isDebug) {
        doCompareProgress(null, newPath, oldPath, outPath, dataDateFormat, shaEnable, isDebug, null);
    }

    public static void doCompare(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                 String outPath, SimpleDateFormat dataDateFormat, boolean shaEnable, boolean isDebug) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, dataDateFormat, shaEnable, isDebug, null);
    }


    public static void doCompareWithIgnore(String newPath, String oldPath, String outPath, List<String> ignoreValues) {
        doCompareProgress(null, newPath, oldPath, outPath, CONFIG_SDF, CONFIG_SHA, CONFIG_DEBUG, ignoreValues);
    }

    public static void doCompareWithIgnore(String newPath, String oldPath, String outPath, SimpleDateFormat dataDateFormat, List<String> ignoreValues) {
        doCompareProgress(null, newPath, oldPath, outPath, dataDateFormat, CONFIG_SHA, CONFIG_DEBUG, ignoreValues);
    }

    public static void doCompareWithIgnore(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                           String outPath, List<String> ignoreValues) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, CONFIG_SDF, CONFIG_SHA, CONFIG_DEBUG, ignoreValues);
    }

    public static void doCompareWithIgnore(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                           String outPath, SimpleDateFormat dataDateFormat, List<String> ignoreValues) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, dataDateFormat, CONFIG_SHA, CONFIG_DEBUG, ignoreValues);
    }

    public static void doCompareWithIgnore(String newPath, String oldPath, String outPath, SimpleDateFormat dataDateFormat,
                                           boolean shaEnable, List<String> ignoreValues) {
        doCompareProgress(null, newPath, oldPath, outPath, dataDateFormat, shaEnable, CONFIG_DEBUG, ignoreValues);
    }

    public static void doCompareWithIgnore(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                           String outPath, SimpleDateFormat dataDateFormat, boolean shaEnable, List<String> ignoreValues) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, dataDateFormat, shaEnable, CONFIG_DEBUG, ignoreValues);
    }

    public static void doCompareWithIgnore(String newPath, String oldPath, String outPath, SimpleDateFormat dataDateFormat,
                                           boolean shaEnable, boolean isDebug, List<String> ignoreValues) {
        doCompareProgress(null, newPath, oldPath, outPath, dataDateFormat, shaEnable, isDebug, ignoreValues);
    }

    public static void doCompareWithIgnore(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                           String outPath, SimpleDateFormat dataDateFormat, boolean shaEnable, boolean isDebug, List<String> ignoreValues) {
        doCompareProgress(userDefineHelper, newPath, oldPath, outPath, dataDateFormat, shaEnable, isDebug, ignoreValues);
    }

    private static void doCompareProgress(FileCompareHelper.CompareHelper userDefineHelper, String newPath, String oldPath,
                                          String outPath, SimpleDateFormat dataDateFormat, boolean shaEnable, boolean isDebug, List<String> ignoreValues) {
        String timeStr = null == dataDateFormat ? "" : dataDateFormat.format(new Date()) + "_";
        FileCompareHelper fileCompair = new FileCompareHelper(userDefineHelper, newPath, oldPath, shaEnable, isDebug);
        fileCompair.setIgnoreValues(ignoreValues);
        fileCompair.setThrowException(true);
        fileCompair.doCompair();
        fileCompair.copySameFile(concatFilePath(outPath, timeStr + "same"));
        fileCompair.copyNewFile(concatFilePath(outPath, timeStr + "new"));
        fileCompair.copyDeleteFile(concatFilePath(outPath, timeStr + "delete"));
        fileCompair.copyModifyFileOld(concatFilePath(outPath, timeStr + "modifyOld"));
        fileCompair.copyModifyFile(concatFilePath(outPath, timeStr + "modify"));
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        sb.append("--------------------------------").append("\r\n");
        sb.append("文件对比和复制输出完成").append("\r\n");
        sb.append("新的文件路径：" + fileCompair.getNewPath()).append("\r\n");
        sb.append("旧的文件路径：" + fileCompair.getOldPath()).append("\r\n");
        sb.append("对比结果文件路径：" + outPath).append("\r\n");
        sb.append("--------------------------------");
        log.info(sb.toString());
    }

    private static String concatFilePath(String path, String fileName) {
        if (null == path || path.length() <= 0 || null == fileName || fileName.length() <= 0) {
            return null;
        }
        if (path.endsWith("\\") || path.endsWith("/")) {
            return path + fileName;
        } else {
            return path + File.separator + fileName;
        }
    }
}
