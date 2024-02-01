package com.ruomm.assistx.cmdx;

/**
 * @author 牛牛-wanruome@163.com
 * @copyright wanruome-2020
 * @create 2020年5月6日 下午15:16:33
 */
public class JEvnApp {
    public static void main(String[] args) {
        StringBuilder sbHelp = new StringBuilder();
        sbHelp.append("帮助文档：");
        sbHelp.append("\r\n");
        sbHelp.append("第一个参数环境变量的用户空间");
        sbHelp.append("\r\n");
        sbHelp.append("第二个参数环境变量的Key");
        sbHelp.append("\r\n");
        sbHelp.append("第三个参数环境变量的值");
        sbHelp.append("\r\n");
        sbHelp.append("第四个参数环境变量更新模式，0跳过模式，1覆盖更新，2匹配追加模式，3.匹配前置追加模式");
        sbHelp.append("\r\n");
        sbHelp.append("第五参数环境变量的匹配值，监测是否匹配");
        sbHelp.append(
                "示例：java -jar CmdxUtil.jar \"<system>\" \"Path\" \"<JAVA_HOME>\\bin\" \"1\" \"<JAVA_HOME>\\bin\"");
        sbHelp.append("\r\n");
        if (null == args || args.length <= 0) {
            System.out.println(sbHelp.toString());
            return;
        } else if (args.length != 4 && args.length != 5) {
            if (args.length == 1) {
                if ("h".equalsIgnoreCase(args[0]) || "help".equalsIgnoreCase(args[0]) || "-h".equalsIgnoreCase(args[0])
                        || "-help".equalsIgnoreCase(args[0]) || "/h".equalsIgnoreCase(args[0])
                        || "/help".equalsIgnoreCase(args[0])) {
                    System.out.println(sbHelp.toString());
                    return;
                }
            }
            System.out.println("参数输入错误，请参考帮助文档！");
            System.out.println(sbHelp.toString());
            return;
        }
        String p_User = args[0];
        String p_Key = args[1];
        String p_Val = args[2];
        String p_Mode = args[3];
        String p_Match = args.length == 5 ? args[4] : null;
//		System.out.println("第一个参数1:" + p_User);
//		System.out.println("第二个参数1:" + p_Key);
//		System.out.println("第三个参数1:" + p_Val);
//		System.out.println("第四个参数1:" + p_Mode);
//		System.out.println("第五个参数1:" + p_Match);
        if ("system".equalsIgnoreCase(p_User) || "<system>".equalsIgnoreCase(p_User)) {
            p_User = "<system>";
        } else if (null != p_User && !p_User.contains("\\")) {
            CmdxHelper cmdxHelper = new CmdxHelper();
            String v_hostname = cmdxHelper.doCmdNumBack(1, "hostName");
            p_User = v_hostname + "\\\\" + p_User;
        }
        if (null != p_Val) {
            p_Val = p_Val.replace("\\%", "%");
            p_Val = p_Val.replace("<", "%");
            p_Val = p_Val.replace(">", "%");
        }
        if (null != p_Match) {
            p_Match = p_Match.replace("\\%", "%");
            p_Match = p_Match.replace("<", "%");
            p_Match = p_Match.replace(">", "%");
        }
        System.out.println("第一个参数:" + p_User);
        System.out.println("第二个参数:" + p_Key);
        System.out.println("第三个参数:" + p_Val);
        System.out.println("第四个参数:" + p_Mode);
        System.out.println("第五个参数:" + p_Match);
        StringBuilder p_Sb = new StringBuilder();
        p_Sb.append("wmic environment where \"name='" + p_Key + "' and username='" + p_User + "'\" get VariableValue");
        System.out.println("查找环境变量：" + p_Sb.toString());
        CmdxHelper cmdxHelper = new CmdxHelper();
        String p_str1 = cmdxHelper.doCmdNumBack(1, p_Sb.toString());
        if ("VariableValue".equals(p_str1)) {
            System.out.println("环境变量已经存在，调用更新模式");
            if ("0".equals(p_Mode)) {
                System.out.println(p_Mode + "模式：跳过更新");
                System.out.println("更新环境变量结果：" + "环境变了以存在，跳过更新");
            } else if ("1".equals(p_Mode)) {
                System.out.println(p_Mode + "模式：覆盖更新");
                StringBuilder pUpdateStr = new StringBuilder();
                pUpdateStr.append(p_Val);
                StringBuilder pUpdateCmd = new StringBuilder();
                pUpdateCmd.append("wmic ENVIRONMENT where \"name='" + p_Key + "' and username='" + p_User
                        + "'\" set VariableValue=\"" + pUpdateStr.toString() + "\"");
                System.out.println("更新环境变量语句：" + pUpdateCmd.toString());
                String pUpdateResult = cmdxHelper.doCmd(pUpdateCmd.toString());
                System.out.println("更新环境变量结果：" + pUpdateResult);
            } else if ("2".equals(p_Mode) || "3".equals(p_Mode)) {
                if ("2".equals(p_Mode)) {
                    System.out.println(p_Mode + "模式：匹配追加模式");
                }
                if ("3".equals(p_Mode)) {
                    System.out.println(p_Mode + "模式：匹配前置追加模式");
                }
                String p_str2 = cmdxHelper.doCmdNumBack(2, p_Sb.toString());
                boolean isMatch = false;
                if (!CmdxHelper.isBlank(p_Match)) {
                    String[] pMatchs = p_Match.split("\\|");
                    for (String tmp : pMatchs) {
                        isMatch = cmdxHelper.doMatch(p_str2, tmp);
                        if (isMatch) {
                            break;
                        }
                    }
                }
                if (!isMatch) {
                    StringBuilder pUpdateStr = new StringBuilder();
                    if ("3".equals(p_Mode)) {
                        if (p_Val.startsWith(";")) {
                            pUpdateStr.append(p_Val.substring(1));
                        } else {
                            pUpdateStr.append(p_Val);
                        }
                        if (!pUpdateStr.toString().endsWith(";")) {
                            pUpdateStr.append(";");
                        }
                        if (p_str2.startsWith(";")) {
                            pUpdateStr.append(p_str2.substring(1));
                        } else {
                            pUpdateStr.append(p_str2);
                        }

                    } else {
                        pUpdateStr.append(p_str2);
                        if (!pUpdateStr.toString().endsWith(";")) {
                            pUpdateStr.append(";");
                        }
                        if (p_Val.startsWith(";")) {
                            pUpdateStr.append(p_Val.substring(1));
                        } else {
                            pUpdateStr.append(p_Val);
                        }
                    }

                    StringBuilder pUpdateCmd = new StringBuilder();
                    pUpdateCmd.append("wmic ENVIRONMENT where \"name='" + p_Key + "' and username='" + p_User
                            + "'\" set VariableValue=\"" + pUpdateStr.toString() + "\"");
                    System.out.println("更新环境变量语句：" + pUpdateCmd.toString());
                    String pUpdateResult = cmdxHelper.doCmd(pUpdateCmd.toString());
                    System.out.println("更新环境变量结果：" + pUpdateResult);
                } else {
                    System.out.println("找到匹配的值，无需更新");
                }
            } else {
                System.out.println(p_Mode + "模式：模式输入不正确，限制输入0、1、2、3");
            }
        } else if (!CmdxHelper.isBlank(p_str1)) {
            // 新建
            System.out.println("环境变量不存在，调用新建模式");
            StringBuilder pUpdateStr = new StringBuilder();
            pUpdateStr.append(p_Val);
            StringBuilder pCreateCmd = new StringBuilder();
            pCreateCmd.append("wmic ENVIRONMENT create name=\"" + p_Key + "\",username=\"" + p_User
                    + "\",VariableValue=\"" + p_Val + "\"");
            System.out.println("新建环境变量语句：" + pCreateCmd.toString());
            String pCreateResult = cmdxHelper.doCmd(pCreateCmd.toString());
            System.out.println("新建环境变量结果：" + pCreateResult);
        } else {
            // 错误不做任何操作
            System.out.println("环境变量不存在，调用新建模式");
            StringBuilder pUpdateStr = new StringBuilder();
            pUpdateStr.append(p_Val);
            StringBuilder pCreateCmd = new StringBuilder();
            pCreateCmd.append("wmic ENVIRONMENT create name=\"" + p_Key + "\",username=\"" + p_User
                    + "\",VariableValue=\"" + p_Val + "\"");
            System.out.println("新建环境变量语句：" + pCreateCmd.toString());
            String pCreateResult = cmdxHelper.doCmd(pCreateCmd.toString());
            System.out.println("新建环境变量结果：" + pCreateResult);
        }
    }
}
