package com.ruomm.assistx.filecomparex;

import com.ruomm.assistx.filecomparex.core.FileCompareHelper;
import com.ruomm.assistx.filecomparex.utils.FileCompareUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.dal.FileInfoDal;
import com.ruomm.javax.corex.exception.JavaxCorexException;

import java.text.SimpleDateFormat;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/22 16:56
 */
public class FileCompareApp {
    public static void main(String[] args) {
        String[] myargs = args;
//        if (null == args || args.length <= 0) {
//            myargs = new String[5];
//            myargs[0] = "D:\\temp\\testnew";
//            myargs[1] = "D:\\temp\\testold";
//            myargs[2] = "D:\\temp\\test_compare";
//            myargs[3] = "log";
//            myargs[4] = "sha";
//        } else {
//            myargs = args;
//        }
//        myargs = new String[1];
//        myargs[0] = "-h";
        boolean verifyResult = verifyParam(3, 5, myargs);
        if (!verifyResult) {
            return;
        }
        boolean shaEnable = parseEnableValue(3, "sha", myargs);
        boolean logEnable = parseEnableValue(3, "log", myargs);
        compareProject(myargs[0], myargs[1], myargs[2], shaEnable, logEnable);
    }

    public static void compareProject(String newPath, String oldPath, String outPath, boolean shaEnable, boolean isDebug) {
        if (StringUtils.isEmpty(newPath)) {
            throw new JavaxCorexException(JavaxCorexException.ERR_CORE, "新的文件路径为空");
        } else if (StringUtils.isEmpty(oldPath)) {
            throw new JavaxCorexException(JavaxCorexException.ERR_CORE, "旧的文件路径为空");
        } else if (StringUtils.isEmpty(outPath)) {
            throw new JavaxCorexException(JavaxCorexException.ERR_CORE, "输出的文件路径为空");
        }
        // 输出子文件夹进行路径拼接日期
        FileCompareUtils.doCompare(compareHelper, newPath, oldPath, outPath, new SimpleDateFormat("yyyyMMddHHmm"), shaEnable, isDebug);
        // 输出根文件夹进行路径拼接日期
//        String resultOutPath;
//        if (FileUtils.isEndWithFileSeparator(outPath)) {
//            int tmpSize = outPath.length();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//            resultOutPath = outPath.substring(0, tmpSize - 1) + "_" + TimeUtils.formatTime(System.currentTimeMillis(), sdf) + outPath.substring(tmpSize - 1);
//        } else {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
//            resultOutPath = outPath + "_" + TimeUtils.formatTime(System.currentTimeMillis(), sdf) + FileUtils.parseFileSeparator(outPath);
//        }
//        FileCompareUtils.doCompare(compareHelper, newPath, oldPath, resultOutPath, null, debug);
    }

    private static FileCompareHelper.CompareHelper compareHelper = new FileCompareHelper.CompareHelper() {
        @Override
        public boolean isCompareByDefine(String fileKey, FileInfoDal newFileInfo, FileInfoDal oldFileInfo) {
            return false;
        }

        @Override
        public boolean doCompareByDefine(String fileKey, FileInfoDal newFileInfo, FileInfoDal oldFileInfo) {
            return false;
        }

        @Override
        public boolean isFilter(String pathFull, String pathLite) {
            boolean isFilter = false;
            try {
                String fileStrReal = pathLite.replace("/", "\\");
                String[] fileStrArrays = fileStrReal.split("\\\\");
                if (null != fileStrArrays && fileStrArrays.length > 0) {
                    for (String tmp : fileStrArrays) {
                        if (".svn".equalsIgnoreCase(tmp)) {
                            isFilter = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {

            }
            return isFilter;
        }

        @Override
        public boolean isSignByDefine() {
            return false;
        }

        @Override
        public String doSignByDefine(String pathFull) {
            return null;
//            File file = new File(pathFull);
//            String sha =  file.lastModified()+"-"+FileUtils.getSizeBytes(file);
//            return sha;
        }
    };


    private static boolean parseEnableValue(int skipCount, String key, String... myargs) {
        int argsCount = null == myargs ? 0 : myargs.length;
        int argsSkipCount = skipCount < 0 ? 0 : skipCount;
        String keyLower = null == key ? null : key.toLowerCase();
        if (argsCount <= argsSkipCount) {
            return false;
        }
        boolean result = false;
        for (String tmpKey : myargs) {
            if (StringUtils.isEmpty(tmpKey)) {
                continue;
            }
            String tmpKeyLower = tmpKey.toLowerCase();
            if (tmpKeyLower.contains(keyLower)) {
                result = true;
                break;
            }
        }
        return result;

    }

    private static boolean verifyParam(int minLength, int maxLength, String... myargs) {
        if (myargs.length == 1 && null != myargs[0] && myargs[0].length() > 0 && (
                "?".equalsIgnoreCase(myargs[0]) || "/?".equalsIgnoreCase(myargs[0]) || "-?".equalsIgnoreCase(myargs[0])
                        || "help".equalsIgnoreCase(myargs[0]) || "/help".equalsIgnoreCase(myargs[0]) || "-help".equalsIgnoreCase(myargs[0])
                        || "h".equalsIgnoreCase(myargs[0]) || "/h".equalsIgnoreCase(myargs[0]) || "-h".equalsIgnoreCase(myargs[0]))) {
            System.out.println("显示帮助文档");
            showReadeMe();
            return false;

        }
        int argsCount = null == myargs ? 0 : myargs.length;
        int minCount = minLength < 0 ? 0 : minLength;
        int maxCount;
        if (maxLength < 0) {
            maxCount = -1;
        } else if (maxLength <= minCount) {
            maxCount = minCount;
        } else {
            maxCount = maxLength;
        }
        // 判断是否有效参数
        if (minCount >= 0) {
            if (argsCount < minCount) {
                System.out.println("参数个数错误，最小个数为：" + minCount + "，输入个数为：" + argsCount + "个");
                showReadeMe();
                return false;
            }
        }
        if (maxCount >= 0) {
            if (argsCount > maxCount) {
                System.out.println("参数个数错误，最大个数为：" + maxCount + "，输入个数为：" + argsCount + "个");
                showReadeMe();
                return false;
            }
        }
        for (int i = 0; i < minCount; i++) {
            if (StringUtils.isEmpty(myargs[i])) {
                int iIdex = i + 1;
                System.out.println("前" + minCount + "参数，是必须输入的参数，请输入第" + iIdex + "个参数内容");
                showReadeMe();
                return false;
            }
        }
        return true;
    }

    private static void showReadeMe() {
        System.out.println("--------文件夹差异对比工具帮助文档--------");
        System.out.println("参数个数限制为3个或更多，前3个是必须填写的参数");
        System.out.println("参数1：新的文件夹路径");
        System.out.println("参数2：旧的文件夹路径");
        System.out.println("参数3：差异输出文件夹路径");
        System.out.println("参数log：输出详细日志");
        System.out.println("参数sha：复制文件是否sha校验");
        System.out.println("--------文件夹差异对比工具帮助文档--------");
    }
}
