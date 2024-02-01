package com.ruomm.javax.corex;

import com.ruomm.javax.corex.dal.CmdxParam;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CmdxUtils {
    private final static Log log = LogFactory.getLog(CmdxUtils.class);

    /**
     * 解析命令命令单元列表为字符串列表
     *
     * @param listCmdxParam
     * @param spaceTag      空格转义标识
     * @return 命令单元列表
     */
    public static List<String> parseCmdxParamToList(List<CmdxParam> listCmdxParam, String spaceTag) {
        if (null == listCmdxParam || listCmdxParam.size() <= 0) {
            return null;
        }
        String realTag = StringUtils.trim(spaceTag);
        List<String> listStr = new ArrayList<>();
        for (CmdxParam cmdxParam : listCmdxParam) {
            if (StringUtils.isEmpty(realTag)) {
                listStr.add(cmdxParam.getParamStr());
            } else {
                listStr.add(cmdxParam.getParamStr().replace(realTag, StringUtils.STR_SPACE_VALUE));
            }
        }
        return listStr;
    }

    /**
     * 解析命令命令单元列表为打印字符串
     *
     * @param listCmdxParam
     * @param spaceTag      空格转义标识
     * @return 命令单元列表
     */
    public static String parseCmdxParamToPrintStr(List<CmdxParam> listCmdxParam, String spaceTag) {
        if (null == listCmdxParam || listCmdxParam.size() <= 0) {
            return "";
        }
        String realTag = StringUtils.trim(spaceTag);
        StringBuilder sb = new StringBuilder();
        for (CmdxParam cmdxParam : listCmdxParam) {
            if (null == cmdxParam || null == cmdxParam.getParamStr() || cmdxParam.getParamStr().length() <= 0) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(" ");
            }
            String realParamStr;
            if (StringUtils.isEmpty(realTag)) {
                realParamStr = cmdxParam.getParamStr().replace("\"", "\\\"").replace("'", "\\'");
            } else {
                realParamStr = cmdxParam.getParamStr().replace("\"", "\\\"").replace("'", "\\'").replace(realTag, StringUtils.STR_SPACE_VALUE);
            }
            if (null != cmdxParam.getQuoteTag() && cmdxParam.getQuoteTag().length() > 0) {
                sb.append(cmdxParam.getQuoteTag()).append(realParamStr).append(cmdxParam.getQuoteTag());
            } else {
                sb.append(realParamStr);
            }

        }
        return sb.toString();
    }
    /**
     * 解析命令内容为命令单元列表
     *
     * @param string     "或'为分隔符，若是命令中有"或'需要转义为\”
     * @paramDefault readByLine 是否按照行模式读取，按照行模式读取换行之间会以空格替换
     * @return 命令单元列表
     */
    public static List<CmdxParam> parseStrToCmdxParam(String string) {
        return parseStrToCmdxParam(string, true);
    }

    /**
     * 解析命令内容为命令单元列表
     *
     * @param string     "或'为分隔符，若是命令中有"或'需要转义为\”
     * @param readByLine 是否按照行模式读取，按照行模式读取换行之间会以空格替换
     * @return 命令单元列表
     */
    public static List<CmdxParam> parseStrToCmdxParam(String string, boolean readByLine) {
        if (StringUtils.isBlank(string)) {
            return null;
        }
        String str;
        if (readByLine&&string.contains("\n")) {
            ByteArrayInputStream byteArrayInputStream = null;
            List<String> listResult;
            try {
                String charset = "UTF-8";
                byteArrayInputStream = new ByteArrayInputStream(string.getBytes(charset));
                listResult = FileUtils.readInputStreamToList(byteArrayInputStream, charset, false, true);
            } catch (Exception e) {
                e.printStackTrace();
                listResult = null;
            } finally {
                try {
                    if (null != byteArrayInputStream) {
                        byteArrayInputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null == listResult || listResult.size() <= 0) {
                str = null;
            } else {
                StringBuilder sbResult = new StringBuilder();
                for (String tmp : listResult) {
                    String tmpTrim = StringUtils.trim(tmp);
                    if (StringUtils.isEmpty(tmpTrim)) {
                        continue;
                    }
                    if (sbResult.length() > 0) {
                        sbResult.append(" ");
                    }
                    sbResult.append(tmpTrim);
                }
                str = sbResult.toString();
            }
        } else {
            str = StringUtils.trimToNotNullString(string);
        }
        if (StringUtils.isBlank(str)) {
            return null;
        }
        List<CmdxParam> listParam = new ArrayList<CmdxParam>();
        StringBuilder sb = new StringBuilder();
        int strSize = str.length();
        boolean isFuhaoStart = false;
        String fuhaoTag = "";
        for (int i = 0; i < strSize; i++) {
            boolean isQuote = false;
            String itemStr = str.substring(i, i + 1);
            String itemStrPre = i <= 0 ? null : str.substring(i - 1, i);
            String itemStrNext = i >= strSize - 1 ? null : str.substring(i + 1, i + 2);
            if ("\"".equals(itemStr) || "'".equals(itemStr)) {
                if (StringUtils.isEmpty(itemStrPre) || !"\\".equals(itemStrPre)) {
                    isQuote = true;
                    isFuhaoStart = !isFuhaoStart;
                    fuhaoTag = itemStr;
                }
            }
            if (isQuote) {
                //如果是开始分割符或结束分隔符，输出上一组字符串
                if (sb.length() > 0) {
                    String tmpSbStr = StringUtils.trimToNotNullString(sb.toString());
                    if (StringUtils.isNotEmpty(tmpSbStr)) {
                        listParam.add(new CmdxParam(tmpSbStr, fuhaoTag));
                    }
                    sb.setLength(0);
                }
            } else if (isFuhaoStart) {
                //如果是分割符内的内容，拼接分割符中的内容
                if ("\\".equals(itemStr)) {
                    if (StringUtils.isEmpty(itemStrNext)) {
                        sb.append(itemStr);
                    } else if ("\"".equals(itemStrNext) || "'".equals(itemStrNext)) {
                        continue;
                    } else {
                        sb.append(itemStr);
                    }
                } else {
                    sb.append(itemStr);
                }
            } else {
                if ("\\".equals(itemStr)) {
                    if (StringUtils.isEmpty(itemStrNext)) {
                        sb.append(itemStr);
                    } else if ("\"".equals(itemStrNext) || "'".equals(itemStrNext)) {
                        continue;
                    } else {
                        sb.append(itemStr);
                    }
                } else if (itemStr.equals(" ") || itemStr.equals("\t") || itemStr.equals("\r") || itemStr.equals("\n")) {
                    if (sb.length() > 0) {
                        String tmpSbStr = StringUtils.trimToNotNullString(sb.toString());
                        if (StringUtils.isNotEmpty(tmpSbStr)) {
                            listParam.add(new CmdxParam(tmpSbStr, null));
                        }
                        sb.setLength(0);
                    }
                    continue;
                } else {
                    sb.append(itemStr);
                }
            }
        }
        if (sb.length() > 0) {
            String tmpSbStr = StringUtils.trimToNotNullString(sb.toString());
            if (StringUtils.isNotEmpty(tmpSbStr)) {
                if (isFuhaoStart) {
                    listParam.add(new CmdxParam(tmpSbStr, fuhaoTag));
                } else {
                    listParam.add(new CmdxParam(tmpSbStr, null));
                }
            }
        }
        log.debug("CmdxUtils.parseCmdStrToList->原始命令内容：" + string);
        log.debug("CmdxUtils.parseCmdStrToList->命令单元列表大小：" + listParam.size());
        log.debug("CmdxUtils.parseCmdStrToList->解析后的内容：" + parseCmdxParamToPrintStr(listParam, null));
        return listParam;
    }

    public static String doCmd(String cmdStr, int osType, String... paramStr) {
        String realCmdStr = cmdStr;
        try {

            if (null != paramStr && paramStr.length > 0) {
                for (String param : paramStr) {
                    int index = cmdStr.indexOf("<?>");
                    realCmdStr = realCmdStr.substring(0, index) + param + realCmdStr.substring(index + 3);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            realCmdStr = null;
            return null;
        }
        if (null == realCmdStr || realCmdStr.length() <= 0) {
            return null;
        }

        try {

            if (osType == 1) {

                Process process = Runtime.getRuntime().exec("cmd /c " + realCmdStr);
//                String[] commands = new String[]{"cmd", "/c ", realCmdStr};
//                Process process = Runtime.getRuntime().exec(commands);
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "GBK");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while (true) {
                    String content = br.readLine();
                    if (null != content) {
                        if (StringUtils.isNotBlank(content)) {
                            if (sb.length() > 0) {
                                sb.append("\r\n");
                            }
                            sb.append(content);
                        }
                    } else {
                        break;
                    }
                }
                return sb.length() > 0 ? sb.toString() : null;
            } else {
                String[] commands = new String[]{"/usr/bin/sh", "-c", realCmdStr};
                Process process = Runtime.getRuntime().exec(commands);
//                process.waitFor();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while (true) {
                    String content = br.readLine();
                    if (null != content) {
                        if (StringUtils.isNotBlank(content)) {
                            if (sb.length() > 0) {
                                sb.append("\r\n");
                            }
                            sb.append(content);
                        }
                    } else {
                        break;
                    }
                }
                return sb.length() > 0 ? sb.toString() : null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public static String doCmd(String cmdStr, boolean needRetrun, String... paramStr) {
        String realCmdStr = cmdStr;
        try {

            if (null != paramStr && paramStr.length > 0) {
                for (String param : paramStr) {
                    int index = cmdStr.indexOf("<?>");
                    realCmdStr = realCmdStr.substring(0, index) + param + realCmdStr.substring(index + 3);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            realCmdStr = null;
            return null;
        }
        if (null == realCmdStr || realCmdStr.length() <= 0) {
            return null;
        }

        try {
//            Process process = Runtime.getRuntime().exec("cmd /c " +realCmdStr);
            Process process = Runtime.getRuntime().exec("cmd", new String[]{"/c", realCmdStr});
//            Process process = Runtime.getRuntime().exec(realCmdStr);
            if (needRetrun) {
                process.waitFor();
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "GBK");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                while (true) {
                    String content = br.readLine();
                    if (null != content) {
                        if (StringUtils.isNotBlank(content)) {
                            if (sb.length() > 0) {
                                sb.append("\r\n");
                            }
                            sb.append(content);
                        }
                    } else {
                        break;
                    }
                }
                return sb.length() > 0 ? sb.toString() : null;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }
}
