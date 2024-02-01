package com.ruomm.mvnx.util;

import com.ruomm.javax.corex.*;
import com.ruomm.javax.corex.dal.CmdxParam;
import com.ruomm.javax.corex.dal.OsEnvDal;
import com.ruomm.mvnx.dal.ParamOsValue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/25 23:10
 */
public class MojoUtils {
    public final static String REPALCE_DEFAULT_SDF = "yyyy-MM-dd HH:mm:ss";
    public final static String DO_ROOT_DEL = "rootdel";
    public final static String DO_FROM_DEL = "fromdel";
    public final static String DO_TO_DEL = "todel";
    public final static String OS_WIN = "win";
    public final static String OS_LINUX = "linux";
    public final static String OS_MAC_OS = "mac";
    public final static String OS_UNKNOWN = "unknown";
    public final static String OS_ALL = "osall";
    public final static String PARAM_KEY_START = "p:";

    public static List<String> transListForCmd(List<String> listForCmd, String spaceTag) {
        if (null == listForCmd || listForCmd.size() <= 0 || null == spaceTag || spaceTag.length() <= 0) {
            return listForCmd;
        } else {
            List<String> listResult = new ArrayList<>();
            for (String tmp : listForCmd) {
                listResult.add(tmp.replace(spaceTag, " "));
            }
            return listResult;
        }
    }

    public static List<String> parseCmdxParamToList(List<CmdxParam> listCmdxParam, String spaceTag) {
        return CmdxUtils.parseCmdxParamToList(parseCmdxParamToListCommon(listCmdxParam, PARAM_KEY_START), spaceTag);
    }

    public static String parseCmdxParamToExec(List<CmdxParam> listCmdxParam, int startIndex, String spaceTag) {
        List<CmdxParam> listCmdxParamResult = parseCmdxParamToListCommon(listCmdxParam, PARAM_KEY_START);
        if (ListUtils.isEmpty(listCmdxParamResult)) {
            return null;
        }
        int listSize = listCmdxParamResult.size();
        int realStartIndex = startIndex < 0 ? 0 : startIndex;
        if (realStartIndex >= listSize) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int c = realStartIndex; c < listSize; c++) {
            CmdxParam cmdxParam = listCmdxParamResult.get(c);
            String realTag = StringUtils.trim(spaceTag);
            String realStr;
            if (StringUtils.isEmpty(realTag)) {
                realStr = cmdxParam.getParamStr();
            } else {
                realStr = cmdxParam.getParamStr().replace(realTag, StringUtils.STR_SPACE_VALUE);
            }
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (StringUtils.isEmpty(cmdxParam.getQuoteTag())) {
                sb.append(realStr);
            } else {
                sb.append("\"").append(realStr).append("\"");
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    public static List<CmdxParam> parseCmdxParamToListCommon(List<CmdxParam> listCmdxParam, String... paramKeys) {
        if (null == listCmdxParam || listCmdxParam.size() <= 0) {
            return null;
        }
        List<String> listMapKey;
        if (null == paramKeys || paramKeys.length <= 0) {
            listMapKey = null;
        } else {
            listMapKey = new ArrayList<>();
            for (String key : paramKeys) {
                if (null == key || key.length() <= 0) {
                    continue;
                }
                String mapKey;
                if (key.startsWith("-")) {
                    mapKey = key.toLowerCase().substring(1);
                } else {
                    mapKey = key.toLowerCase();
                }
                if (StringUtils.isEmpty(mapKey)) {
                    continue;
                }
                listMapKey.add(mapKey);
            }
        }
        List<CmdxParam> listCmdxParamResult = new ArrayList<>();
        for (CmdxParam cmdxParam : listCmdxParam) {
            if (null == cmdxParam) {
                continue;
            }
            if (StringUtils.isEmpty(cmdxParam.getParamStr())) {
                continue;
            }
            boolean isParam;
            if (ListUtils.isEmpty(listMapKey)) {
                isParam = false;
            } else {
                isParam = false;
                for (String mapKey : listMapKey) {
                    if (cmdxParam.getParamStr().toLowerCase().startsWith("-" + mapKey)) {
                        isParam = true;
                        break;
                    }
                }
            }
            if (isParam) {
                continue;
            }
            listCmdxParamResult.add(cmdxParam);
        }
        return listCmdxParamResult;
    }

    public static Map<String, List<String>> parseParamMap(List<CmdxParam> listCmdxParam) {
        return parseParamMapCommon(listCmdxParam, PARAM_KEY_START);
    }

    public static Map<String, List<String>> parseParamMapCommon(List<CmdxParam> listCmdxParam, String... paramKeys) {
        if (null == listCmdxParam || listCmdxParam.size() <= 0) {
            return null;
        }

        List<String> listMapKey;
        if (null == paramKeys || paramKeys.length <= 0) {
            listMapKey = null;
        } else {
            listMapKey = new ArrayList<>();
            for (String key : paramKeys) {
                if (null == key || key.length() <= 0) {
                    continue;
                }
                String mapKey;
                if (key.startsWith("-")) {
                    mapKey = key.toLowerCase().substring(1);
                } else {
                    mapKey = key.toLowerCase();
                }
                if (StringUtils.isEmpty(mapKey)) {
                    continue;
                }
                listMapKey.add(mapKey);
            }
        }
        if (ListUtils.isEmpty(listMapKey)) {
            return null;
        }
        Map<String, List<String>> paramMap = new HashMap<>();
        for (CmdxParam cmdxParam : listCmdxParam) {
            if (null == cmdxParam) {
                continue;
            }
            if (StringUtils.isEmpty(cmdxParam.getParamStr())) {
                continue;
            }
            for (String mapKey : listMapKey) {
                if (cmdxParam.getParamStr().toLowerCase().startsWith("-" + mapKey)) {
                    String tmpValue = cmdxParam.getParamStr().substring(mapKey.length() + 1);
                    if (StringUtils.isEmpty(tmpValue)) {
                        continue;
                    }
                    List<String> tmpList = StringUtils.getListString(tmpValue, ",", false, true);
                    if (ListUtils.isEmpty(tmpList)) {
                        continue;
                    }
                    List<String> listParam = paramMap.get(mapKey);
                    if (null == listParam) {
                        listParam = new ArrayList<>();
                        ListUtils.addDistinctList(listParam, tmpList);
                        paramMap.put(mapKey, listParam);
                    } else {
                        ListUtils.addDistinctList(listParam, tmpList);
                    }
                }
            }
        }
        return paramMap.size() <= 0 ? null : paramMap;
    }

    public static boolean praseParamEnable(Map<String, List<String>> paramMap, String... paramKeys) {


        if (null == paramKeys || paramKeys.length <= 0) {
            return false;
        }
        if (null == paramMap || paramMap.size() <= 0) {
            return false;
        }
        List<String> paramList = paramMap.get(PARAM_KEY_START);
        if (null == paramList || paramList.size() <= 0) {
            return false;
        }
        boolean paramEnable = false;
        for (String key : paramKeys) {
            if (StringUtils.isEmpty(key)) {
                return false;
            }
            paramEnable = ListUtils.containsIgnoreCase(paramList, key);
            if (paramEnable) {
                break;
            }

        }
        return paramEnable;
    }

    public static ParamOsValue parseParamOsValue(OsEnvDal osEnvDal, Map<String, List<String>> paramMap) {
        if (null == osEnvDal || null == osEnvDal.getOsShortName() || osEnvDal.getOsShortName().length() <= 0) {
            ParamOsValue paramOsValue = new ParamOsValue();
            paramOsValue.setOsEnv(null);
            paramOsValue.setEnable(false);
            paramOsValue.setErrMsg("current os unknown,os info is null");
            return paramOsValue;
        }

        boolean macEnable;
        boolean linuxEnable;
        boolean winEnable;
        if (praseParamEnable(paramMap, OS_ALL)) {
            macEnable = true;
            linuxEnable = true;
            winEnable = true;
        } else {
            macEnable = MojoUtils.praseParamEnable(paramMap, OS_MAC_OS);
            linuxEnable = MojoUtils.praseParamEnable(paramMap, OS_LINUX);
            winEnable = MojoUtils.praseParamEnable(paramMap, OS_WIN);
            if (!macEnable && !linuxEnable && !winEnable) {
                macEnable = true;
                linuxEnable = true;
                winEnable = true;
            }
        }
        StringBuilder sb = new StringBuilder();
        if (winEnable) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(OS_WIN);
        }
        if (linuxEnable) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(OS_LINUX);
        }
        if (macEnable) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(OS_MAC_OS);
        }
        if (sb.length() <= 0) {
            sb.append(OS_UNKNOWN);
        }
        ParamOsValue paramOsValue = new ParamOsValue();
        paramOsValue.setOsEnv(osEnvDal);
        if (macEnable && osEnvDal.getOsType() == 3) {
            paramOsValue.setEnable(true);
            paramOsValue.setErrMsg("current os is " + osEnvDal.getOsShortName() + ",only running on [" + sb.toString() + "] os");
        } else if (linuxEnable && osEnvDal.getOsType() == 2) {
            paramOsValue.setEnable(true);
            paramOsValue.setErrMsg("current os is " + osEnvDal.getOsShortName() + ",only running on [" + sb.toString() + "] os");
        } else if (winEnable && osEnvDal.getOsType() == 1) {
            paramOsValue.setEnable(true);
            paramOsValue.setErrMsg("current os is " + osEnvDal.getOsShortName() + ",only running on [" + sb.toString() + "] os");
        } else {
            paramOsValue.setEnable(false);
            paramOsValue.setErrMsg("current os is " + osEnvDal.getOsShortName() + ",only running on [" + sb.toString() + "] os");
        }
        return paramOsValue;
    }

    public static boolean isSkip(List<String> skipTags, String strVal, boolean isLikeMode) {
        String str = null == strVal ? null : strVal.trim();
        if (null == str || str.length() <= 0) {
            return false;
        }
        List<String> lstSkipTags;
        if (null == skipTags || skipTags.size() <= 0) {
            lstSkipTags = new ArrayList<String>();
            lstSkipTags.add("EMPTY");
            lstSkipTags.add("NULL");
        } else {
            lstSkipTags = skipTags;
        }
        boolean isSkip = false;
        for (String tmp : lstSkipTags) {
            if (null == tmp || tmp.length() <= 0) {
                continue;
            }
            if (!isLikeMode) {
                if (str.equals(tmp)) {
                    isSkip = true;
                    break;
                }
            } else {
                if (str.startsWith(tmp) || str.endsWith(tmp)) {
                    isSkip = true;
                }
            }
        }
        return isSkip;
    }

    public static boolean copyAllFiles(String srcFileParam, String desDirParam, Boolean sha) {
        String srcFilePath = replaceFileSeparator(srcFileParam);
        if (null == srcFilePath || srcFilePath.length() <= 0) {
            return false;
        }
        File file = new File(srcFilePath);
        if (null == file || !file.exists()) {
            return false;
        }
        String desDir = replaceFileSeparator(desDirParam);
        boolean shaEnable = null == sha ? false : sha.booleanValue();
        if (file.isFile()) {
            String desFilePath = null;
            if (FileUtils.isEndWithFileSeparator(desDir)) {
                desFilePath = desDir + file.getName();
            } else {
                desFilePath = desDir;
            }
            FileUtils.makeDirs(desFilePath);
            return FileUtils.copyFileCommon(file, new File(desFilePath), true, shaEnable, true, 1);

        } else if (file.isDirectory()) {
            boolean flag = true;
            List<File> lstFiles = getAllFiles(file);
            if (null == lstFiles || lstFiles.size() <= 0) {
                return true;
            }
            String basePath = file.getPath();
            int basePathSize = basePath.length();
            if (!FileUtils.isEndWithFileSeparator(basePath)) {
                basePathSize++;
            }
            String desFolePath = FileUtils.isEndWithFileSeparator(desDir) ? desDir : desDir + FileUtils.parseFileSeparator(desDir);
            for (File tmpFile : lstFiles) {
                if (tmpFile.isFile()) {
                    String fileName = tmpFile.getPath().substring(basePathSize);
                    String desFilePath = desFolePath + fileName;
                    FileUtils.makeDirs(desFilePath);
                    boolean result = FileUtils.copyFileCommon(tmpFile, new File(desFilePath), true, shaEnable, true, 1);
                    if (!result) {
                        flag = false;
                    }
                } else if (tmpFile.isDirectory()) {
                    if (tmpFile.getPath().length() <= basePathSize) {
                        continue;
                    }
                    String fileName = tmpFile.getPath().substring(basePathSize);
                    String desFileName = desFolePath + fileName;
                    if (!FileUtils.isEndWithFileSeparator(desFileName)) {
                        desFileName = desFileName + FileUtils.parseFileSeparator(desFileName);
                    }
                    boolean result = FileUtils.makeDirs(desFileName);
                    if (!result) {
                        flag = false;
                    }
                }
            }
            return flag;
        } else {
            return false;
        }
    }

    public static List<File> getAllFiles(File file) {
        List<File> lstFiles = new ArrayList<File>();
        getFilePrivate(file, lstFiles);

        return lstFiles;
    }

    private static void getFilePrivate(File file, List<File> lstFiles) {

        if (null == file || !file.exists()) {
            return;
        } else if (file.isFile()) {
            if (!lstFiles.contains(file)) {
                lstFiles.add(file);
            }
        } else if (file.isDirectory()) {
            if (!lstFiles.contains(file)) {
                lstFiles.add(file);
            }
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                return;
            }
            for (File tmp : files) {
                getFilePrivate(tmp, lstFiles);
            }
        }
    }

    public static boolean repalceContent(String replaceFilePath, String replaceRegex, String replaceSub, String repalceContent, String repalceCharset) {
        if (StringUtils.isEmpty(replaceFilePath) || StringUtils.isEmpty(replaceRegex) || StringUtils.isEmpty(replaceSub) || StringUtils.isEmpty(repalceContent)) {
            return false;
        }

        // 判断文件是否存在
        try {
            File fileReplace = new File(replaceFilePath);
            if (null == fileReplace || !fileReplace.exists() || !fileReplace.isFile()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        int subStart = 0;
        int subEnd = 0;
        boolean isSubOk = false;
        String paramRule = replaceSub.toLowerCase();
        if (paramRule.startsWith("sub:") || paramRule.startsWith("substring:")
                || paramRule.startsWith("substring:")) {
            try {
                int index = paramRule.indexOf(":");
                String subRoleStr = paramRule.substring(index + 1);
                String[] subRoleStrArr = subRoleStr.split(",");
                if (null == subRoleStrArr || subRoleStrArr.length <= 0) {
                    subStart = 0;
                    subEnd = 0;
                } else if (subRoleStrArr.length == 1) {
                    subStart = Integer.valueOf(subRoleStrArr[0]);
                    subEnd = Integer.valueOf(subRoleStrArr[0]);
                } else {
                    subStart = Integer.valueOf(subRoleStrArr[0]);
                    subEnd = Integer.valueOf(subRoleStrArr[1]);
                }
                isSubOk = true;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                subStart = 0;
                subEnd = 0;
                isSubOk = false;
            }
        }
        if (!isSubOk || subStart < 0 || subEnd < 0) {
            return false;
        }
        // 读取文件内容
        List<String> listContent = FileUtils.readFileToList(replaceFilePath, repalceCharset, true, true);
        if (null == listContent || listContent.size() <= 0) {
            return false;
        }
        String lineCtrlStr = parseFileLineCtr(replaceFilePath, repalceCharset);
        boolean hasException = false;
        boolean hasRepalce = false;
        List<String> listWriteContent = new ArrayList<String>();
        for (String str : listContent) {
            if (StringUtils.isEmpty(str) || !RegexUtils.doRegex(str, replaceRegex)) {
                listWriteContent.add(str);
                continue;
            }
            try {
                int strSize = str.length();
                String strStart = null;
                String strEnd = null;
                if (subStart <= 0) {
                    strStart = "";
                } else if (subStart >= strSize) {
                    strStart = str;
                } else {
                    strStart = str.substring(0, subStart);
                }
                if (subEnd <= 0) {
                    strEnd = "";
                } else if (subEnd >= strSize) {
                    strEnd = str;
                } else {
                    strEnd = str.substring(strSize - subEnd, strSize);
                }
                String repalceContentLower = repalceContent.toLowerCase();
                String strCenter = null;
                if (repalceContentLower.equals("date") || repalceContentLower.equals("date:")) {
                    SimpleDateFormat sdf = new SimpleDateFormat(REPALCE_DEFAULT_SDF);
                    strCenter = sdf.format(new Date());
                } else if (repalceContentLower.startsWith("date:")) {
                    try {
                        String sdfStr = repalceContent.substring(5);
                        SimpleDateFormat sdfUser = new SimpleDateFormat(sdfStr);
                        strCenter = sdfUser.format(new Date());
                    } catch (Exception e) {
                        e.printStackTrace();
                        strCenter = null;
                    }
                    if (StringUtils.isEmpty(strCenter)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(REPALCE_DEFAULT_SDF);
                        strCenter = sdf.format(new Date());
                    }
                } else if (repalceContentLower.equals("random") || repalceContentLower.equalsIgnoreCase("random:")) {
                    int tokenSize = strSize - subEnd - subStart;
                    if (tokenSize < 6 || tokenSize > 32) {
                        tokenSize = 16;
                    }
                    TokenHelper tokenHelper = new TokenHelper(TokenHelper.TOKEN_NUMBER);
                    strCenter = tokenHelper.generateToken(tokenSize, true);
                } else if (repalceContentLower.startsWith("random:")) {
                    int tokenSize = 0;
                    try {
                        tokenSize = Integer.parseInt(repalceContent.substring(7));
                    } catch (Exception e) {
                        e.printStackTrace();
                        tokenSize = 0;
                    }
                    if (tokenSize <= 0 || tokenSize > 32) {
                        tokenSize = 16;
                    }
                    TokenHelper tokenHelper = new TokenHelper(TokenHelper.TOKEN_NUMBER);
                    strCenter = tokenHelper.generateToken(tokenSize, true);
                } else {
                    strCenter = repalceContent;
                }
                StringBuilder sb = new StringBuilder();
                if (!StringUtils.isEmpty(strStart)) {
                    sb.append(strStart);
                }
                if (!StringUtils.isEmpty(strCenter)) {
                    sb.append(strCenter);
                }
                if (!StringUtils.isEmpty(strEnd)) {
                    sb.append(strEnd);
                }
                if (sb.length() > 0) {
                    String tmpReusltStr = sb.toString();
                    if (!tmpReusltStr.equals(str)) {
                        hasRepalce = true;
                    }
                    listWriteContent.add(tmpReusltStr);
                } else {
                    hasException = true;
                    listWriteContent.add(str);
                }
            } catch (Exception e) {
                hasException = true;
                e.printStackTrace();
            }

        }
        if (hasException) {
            return false;
        }
        // 不替换的也返回true，避免异常报错。
        if (!hasRepalce) {
            return true;
        }
        int listWirteSize = listWriteContent.size();
        StringBuilder sbWriteContent = new StringBuilder();
        for (int i = 0; i < listWirteSize; i++) {
            if (i > 0) {
                sbWriteContent.append(lineCtrlStr);
            }
            String tmpStr = listWriteContent.get(i);
            if (!StringUtils.isEmpty(tmpStr)) {
                sbWriteContent.append(tmpStr);
            }
        }
        if (sbWriteContent.length() <= 0) {
            return false;
        }
        return FileUtils.writeFile(replaceFilePath, sbWriteContent.toString(), false, repalceCharset);
    }

    private static String parseFileLineCtr(String filePath, String repalceCharset) {
        String content = FileUtils.readFile(filePath, repalceCharset, true);
        int rNum = StringUtils.counterAppearNum(content, '\r');
        int nNum = StringUtils.counterAppearNum(content, '\n');
        if (nNum <= 0) {
            return "\r\n";
        } else {
            if (rNum > 0 && rNum >= nNum / 2) {
                return "\r\n";
            } else {
                return "\n";
            }
        }
    }

    public static String replaceFileSeparator(String filePath) {
        if (null == filePath || filePath.length() <= 0) {
            return filePath;
        }
        String resultPath;
        if ("\\".equals(File.separator)) {
            resultPath = filePath.replace("/", "\\").replace("\\\\", "\\");
        } else {
            resultPath = filePath.replace("\\", "/").replace("//", "/");
        }
        return resultPath;
    }
}
