/**
 * @copyright wanruome-2020
 * @author 牛牛-wanruome@163.com
 * @create 2020年5月6日 上午11:03:30
 */
package com.ruomm.assistx.cmdx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CmdxHelper {
    public static boolean isBlank(String str) {
        if (null == str || str.length() <= 0) {
            return true;
        }
        String str2 = str.trim();
        if (null == str2 || str2.length() <= 0) {
            return true;
        }
        return false;
    }

    public static String trimStr(String str) {
        if (isBlank(str)) {
            return "";
        } else {
            return str.trim();
        }
    }

    public boolean isEmptyBack = false;

    public boolean isEmptyBack() {
        return isEmptyBack;
    }

    public void setEmptyBack(boolean isEmptyBack) {
        this.isEmptyBack = isEmptyBack;
    }

    public String doCmd(String cmdStr, String... paramStr) {
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
            Process process = Runtime.getRuntime().exec(realCmdStr);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String content = br.readLine();
                if (null != content) {
                    if (isEmptyBack) {
                        if (sb.length() > 0) {
                            sb.append("\r\n");
                        }
                        sb.append(content);
                    } else {
                        if (!isBlank(content)) {
                            if (sb.length() > 0) {
                                sb.append("\r\n");
                            }
                            sb.append(content.trim());
                        }
                    }
                } else {
                    break;
                }
            }
            return sb.length() > 0 ? sb.toString() : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> doCmdListBack(String cmdStr, String... paramStr) {
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
        }
        if (null == realCmdStr || realCmdStr.length() <= 0) {
            return null;
        }

        try {
            Process process = Runtime.getRuntime().exec(realCmdStr);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            List<String> list = new ArrayList<String>();
            while (true) {
                String content = br.readLine();
                if (null != content) {
                    if (isEmptyBack) {
                        list.add(content);
                    } else {
                        if (!isBlank(content)) {
                            list.add(content.trim());
                        }
                    }

                } else {
                    break;
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doCmdNumBack(int lineNum, String cmdStr, String... paramStr) {
        if (lineNum <= 0) {
            return null;
        }
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
        }
        if (null == realCmdStr || realCmdStr.length() <= 0) {
            return null;
        }

        try {
            Process process = Runtime.getRuntime().exec(realCmdStr);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            List<String> list = new ArrayList<String>();
            while (true) {
                String content = br.readLine();
                if (null != content) {
                    if (isEmptyBack) {
                        list.add(content);
                    } else {
                        if (!isBlank(content)) {
                            list.add(content.trim());
                        }
                    }

                } else {
                    break;
                }
            }
            if (null == list || list.size() <= 0 || list.size() < lineNum) {
                return null;
            }
            return list.get(lineNum - 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean doCmdRegex(int lineNum, String regexStr, String cmdStr, String... paramStr) {
        String resultStr = doCmdNumBack(lineNum, cmdStr, paramStr);
        if (null == resultStr || resultStr.length() <= 0) {
            return false;
        }
        try {

            if (resultStr.matches(regexStr)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    public boolean doCmdContains(int lineNum, String regexStr, String cmdStr, String... paramStr) {
        String resultStr = doCmdNumBack(lineNum, cmdStr, paramStr);
        if (null == resultStr || resultStr.length() <= 0) {
            return false;
        }
        try {

            if (resultStr.contains(regexStr)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }

    }

    public boolean doMatch(String str, String regex) {
        try {
            if (str.contains(regex)) {
                return true;
            }
            if (str.matches(regex)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }
}
