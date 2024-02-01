package com.ruomm.mvnx;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.ruomm.javax.corex.CmdxUtils;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.OsInfoUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.dal.CmdxParam;
import com.ruomm.javax.corex.dal.OsEnvDal;
import com.ruomm.mvnx.dal.ParamOsValue;
import com.ruomm.mvnx.util.FileRepalceItem;
import com.ruomm.mvnx.util.MojoUtils;
import com.ruomm.mvnx.util.ZipUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Goal which touches a timestamp file.
 *
 * @deprecated Don't use!
 */
@Mojo(name = "run", defaultPhase = LifecyclePhase.PACKAGE)
public class FilexMojo extends AbstractMojo {
//    private final static boolean DEBUG = true;
    /**
     * Location of the file.
     */
//    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
//    private File outputDirectory;
    @Parameter
    private String[] cmds;
    @Parameter
    private String skipTag;
    @Parameter(defaultValue = "{nbsp}")
    private String spaceTag;
    @Parameter
    private Boolean disable;
    @Parameter
    private Boolean exec;
    @Parameter
    private Boolean sha;
    @Parameter
    private Boolean errBreak;
    @Parameter
    private Boolean help;

    private List<String> lstSkipTags = null;

    public String[] getCmds() {
        return cmds;
    }

    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }

    public String getSkipTag() {
        return skipTag;
    }

    public void setSkipTag(String skipTag) {
        this.skipTag = skipTag;
    }

    public String getSpaceTag() {
        return spaceTag;
    }

    public void setSpaceTag(String spaceTag) {
        this.spaceTag = spaceTag;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Boolean getExec() {
        return exec;
    }

    public void setExec(Boolean exec) {
        this.exec = exec;
    }

    public Boolean getSha() {
        return sha;
    }

    public void setSha(Boolean sha) {
        this.sha = sha;
    }

    public Boolean getErrBreak() {
        return errBreak;
    }

    public void setErrBreak(Boolean errBreak) {
        this.errBreak = errBreak;
    }

    public Boolean getHelp() {
        return help;
    }

    public void setHelp(Boolean help) {
        this.help = help;
    }

    public List<String> getLstSkipTags() {
        return lstSkipTags;
    }

    public void setLstSkipTags(List<String> lstSkipTags) {
        this.lstSkipTags = lstSkipTags;
    }

    public List<String> parseSkipTags() {
        if (null != lstSkipTags && lstSkipTags.size() > 0) {
            return lstSkipTags;
        }
        List<String> listSkipTags = new ArrayList<String>();
        String skipVal = null == skipTag ? null : skipTag.trim();
        if (null != skipVal && skipVal.length() > 0) {
            String[] skipTagArray = skipVal.split(",");
            if (null != skipTagArray && skipTagArray.length > 0) {
                for (String tmp : skipTagArray) {
                    String tmpTrim = null == tmp ? null : tmp.trim();
                    if (null == tmpTrim || tmpTrim.length() <= 0) {
                        continue;
                    }
                    listSkipTags.add(tmpTrim);
                }
            }
        }
        if (listSkipTags.size() <= 0) {
            listSkipTags.add("EMPTY");
            listSkipTags.add("NULL");
        }
        lstSkipTags = listSkipTags;
        StringBuilder sb = new StringBuilder();
        for (String tmp : listSkipTags) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(tmp);
        }
        this.skipTag = sb.toString();
        return lstSkipTags;
    }

    public void execute() throws MojoExecutionException {
        // 参数初始化
        if (null != disable && disable.booleanValue()) {
            getLog().info("filex command running is disabled!");
            return;
        }
        boolean isHelp = null != help && help.booleanValue();
        if (isHelp) {
            getLog().info("------filex command help doc------");
            getLog().info("command for copy: (copy,cp) {fromDirOrFile} {toDirOrFile}");
            getLog().info("command for unzip: (unzip,zip) {fromZipFile} {toDir}");
            getLog().info("command for delete: (delete,del)  {deleteDir} {toDir}");
            getLog().info("command for writeversion: (writeversion,writever,writedate,writetime)  {toDirOrFile}");
            getLog().info("command for replace: replace {replaceFilePath} {replaceRegex} {replaceSub} {repalceContent} {repalceCharset}");
            getLog().info("command for exec: exec {execContent}[]");
            getLog().info("all command support '-p:' for set extra parameter");
            getLog().info("all command support '-p:" + MojoUtils.OS_WIN + "," + MojoUtils.OS_LINUX + "," + MojoUtils.OS_MAC_OS + "," + MojoUtils.OS_ALL + "' for select os for running");
            getLog().info("copy command support '-p:" + MojoUtils.DO_ROOT_DEL + "," + MojoUtils.DO_FROM_DEL + "," + MojoUtils.DO_TO_DEL + "'");
            getLog().info("unzip command support '-p:" + MojoUtils.DO_ROOT_DEL + "," + MojoUtils.DO_FROM_DEL + "," + MojoUtils.DO_TO_DEL + "'");
            getLog().info("delete command support '-p:" + MojoUtils.DO_ROOT_DEL + "'");
            getLog().info("------filex command help doc end------");
        }
        try {
            parseSkipTags();
            OsEnvDal osEnvDal = OsInfoUtils.parseOsEnv(OsInfoUtils.OsInfoEnum.UNKNOWN.getOsType());
//            if (null != exec && exec.booleanValue()) {
//                osName = OSinfoUtils.getOSNameToLower();
//            } else {
//                osName = null;
//            }
            getLog().info("filex command skipTag is:" + skipTag);
            if (null == cmds || cmds.length <= 0) {
                getLog().info("filex command content is empty!");
            } else {
                for (String tmp : cmds) {
                    try {
                        List<CmdxParam> listCmdParam = CmdxUtils.parseStrToCmdxParam(tmp);
                        Map<String, List<String>> paramMap = MojoUtils.parseParamMap(listCmdParam);
                        List<String> listForCmd = MojoUtils.parseCmdxParamToList(listCmdParam, this.spaceTag);
                        if (isHelp) {
                            getLog().info("解析的参数为：" + (null != paramMap ? paramMap.toString() : ""));
                            getLog().info("解析的命令为：" + (null != listForCmd ? listForCmd.toString() : ""));
                        }
                        if (null == listForCmd) {
                            getLog().info("filex command parse error!" + "command content:" + tmp);
                            continue;
                        } else if (listForCmd.size() <= 0) {
                            getLog().info("filex command parse empty!" + "command content:" + tmp);
                            continue;
                        } else {
                            getLog().info("filex command parse success!" + "command content:" + CmdxUtils.parseCmdxParamToPrintStr(listCmdParam, this.spaceTag));
                        }
                        ParamOsValue paramOsValue = MojoUtils.parseParamOsValue(osEnvDal, paramMap);
                        if (isHelp) {
                            getLog().info("解析的OS参数为：" + (null != paramOsValue ? paramOsValue.toString() : ""));
                        }
                        String cmdIndex0 = listForCmd.get(0);
                        // 复制命令
                        if (cmdIndex0.equalsIgnoreCase("copy") || cmdIndex0.equalsIgnoreCase("cp")) {
                            if (listForCmd.size() < 3) {
                                getLog().info("filex command excute error!copy command parameter error! ");
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(1), true)) {
                                getLog().info("filex command copy skip by tag:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(2), true)) {
                                getLog().info("filex command copy skip by tag:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                                continue;
                            }
                            if (!paramOsValue.isEnable()) {
                                getLog().info("filex command copy skip by os," + paramOsValue.getErrMsg() + ":" + listForCmd.get(1) + "->" + listForCmd.get(2));
                                continue;
                            }
                            boolean isRootDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_ROOT_DEL);
                            boolean isFromDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_FROM_DEL);
                            boolean isToDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_TO_DEL);
                            if (isHelp) {
                                getLog().info("delRoot、from、to:" + isRootDelete + ":" + isFromDelete + ":" + isToDelete);
                            }
                            if (isToDelete) {
                                getLog().info("filex command delete on copy (to):" + listForCmd.get(2));
                                boolean result = FileUtils.delAllFiles(new File(MojoUtils.replaceFileSeparator(listForCmd.get(2))), isRootDelete);
                                if (!result) {
                                    throw new MojoExecutionException(
                                            "filex command delete on copy (to):" + listForCmd.get(2));
                                }
                            }
                            getLog().info("filex command copy:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                            boolean resultCopy = MojoUtils.copyAllFiles(listForCmd.get(1), listForCmd.get(2), this.sha);
                            if (!resultCopy) {
                                throw new MojoExecutionException(
                                        "filex command copy:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                            }
                            if (isFromDelete) {
                                getLog().info("filex command delete on copy (from):" + listForCmd.get(1));
                                boolean result = FileUtils.delAllFiles(new File(MojoUtils.replaceFileSeparator(listForCmd.get(1))), true);
                                if (!result) {
                                    throw new MojoExecutionException(
                                            "filex command delete on copy (from):" + listForCmd.get(1));
                                }
                            }
                        }
                        // zip命令
                        else if (cmdIndex0.equalsIgnoreCase("unzip") || cmdIndex0.equalsIgnoreCase("zip")) {
                            if (listForCmd.size() < 3) {
                                getLog().info("filex command excute error!unzip command parameter error! ");
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(1), true)) {
                                getLog().info("filex command copy unzip skip by tag::" + listForCmd.get(1) + "->" + listForCmd.get(2));
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(2), true)) {
                                getLog().info("filex command copy unzip skip by tag:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                                continue;
                            }
                            if (!paramOsValue.isEnable()) {
                                getLog().info("filex command unzip skip by os," + paramOsValue.getErrMsg() + ":" + listForCmd.get(1) + "->" + listForCmd.get(2));
                                continue;
                            }
//                        String zipPwd = MojoUtils.parseParamValue(listForCmd, 3, "-pwd");
                            boolean isRootDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_ROOT_DEL);
                            boolean isFromDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_FROM_DEL);
                            boolean isToDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_TO_DEL);
                            if (isHelp) {
                                getLog().info("delRoot、from、to:" + isRootDelete + ":" + isFromDelete + ":" + isToDelete);
                            }
                            if (isToDelete) {
                                getLog().info("filex command delete on unzip (to):" + listForCmd.get(2));
                                boolean result = FileUtils.delAllFiles(new File(MojoUtils.replaceFileSeparator(listForCmd.get(2))), isRootDelete);
                                if (!result) {
                                    throw new MojoExecutionException(
                                            "filex command delete on unzip (to):" + listForCmd.get(2));
                                }
                            }
                            getLog().info("filex command unzip:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                            boolean resultZip = false;
                            try {
                                resultZip = ZipUtils.unZip(listForCmd.get(1), listForCmd.get(2));
                            } catch (Exception e) {
                                resultZip = false;
                            }
                            if (!resultZip) {
                                throw new MojoExecutionException(
                                        "filex command unzip:" + listForCmd.get(1) + "->" + listForCmd.get(2));
                            }
                            if (isFromDelete) {
                                getLog().info("filex command delete on unzip (from):" + listForCmd.get(1));
                                boolean result = FileUtils.delAllFiles(new File(MojoUtils.replaceFileSeparator(listForCmd.get(1))), false);
                                if (!result) {
                                    throw new MojoExecutionException(
                                            "filex command delete on unzip (from):" + listForCmd.get(1));
                                }
                            }
                        }
                        // 删除命令
                        else if (cmdIndex0.equalsIgnoreCase("delete") || cmdIndex0.equalsIgnoreCase("del")) {
                            if (listForCmd.size() < 2) {
                                getLog().info("filex command excute error!delete command parameter error! ");
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(1), true)) {
                                getLog().info("filex command delete skip by tag:" + listForCmd.get(1));
                                continue;
                            }
                            if (!paramOsValue.isEnable()) {
                                getLog().info("filex command delete skip by os," + paramOsValue.getErrMsg() + ":" + listForCmd.get(1));
                                continue;
                            }
                            boolean isRootDelete = MojoUtils.praseParamEnable(paramMap, MojoUtils.DO_ROOT_DEL);
                            if (isHelp) {
                                getLog().info("delRoot:" + isRootDelete);
                            }
                            getLog().info("filex command delete:" + listForCmd.get(1));
                            boolean result = FileUtils.delAllFiles(new File(MojoUtils.replaceFileSeparator(listForCmd.get(1))), isRootDelete);
                            if (!result) {
                                throw new MojoExecutionException("filex command delete on copy (from)" + listForCmd.get(1));
                            }
                        }
                        // 写入版本控制命令
                        else if (cmdIndex0.equalsIgnoreCase("writeversion") || cmdIndex0.equalsIgnoreCase("writever")
                                || cmdIndex0.equalsIgnoreCase("writedate") || cmdIndex0.equalsIgnoreCase("writetime")) {
                            if (listForCmd.size() < 2) {
                                getLog().info("filex command excute error!" + cmdIndex0.toLowerCase()
                                        + " command parameter error! ");
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(1), true)) {
                                getLog().info("filex command " + cmdIndex0.toLowerCase() + " skip by tag:" + listForCmd.get(1));
                                continue;
                            }
                            if (!paramOsValue.isEnable()) {
                                getLog().info("filex command " + cmdIndex0.toLowerCase() + " skip by os," + paramOsValue.getErrMsg() + ":" + listForCmd.get(1));
                                continue;
                            }
                            String cmdIndex1 = listForCmd.get(1);
                            String outFilePath = null;
                            if (FileUtils.isEndWithFileSeparator(cmdIndex1)) {
                                outFilePath = cmdIndex1 + File.separator + "appversion.txt";
                            } else {
                                outFilePath = cmdIndex1;
                            }
                            getLog().info("filex command " + cmdIndex0.toLowerCase() + ":" + listForCmd.get(1) + ":"
                                    + outFilePath);
                            boolean dirResult = FileUtils.makeDirs(outFilePath);
                            if (!dirResult) {
                                throw new MojoExecutionException("filex command " + cmdIndex0.toLowerCase()
                                        + ",mkdir error:" + listForCmd.get(1) + ":" + outFilePath);
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            boolean result = FileUtils.writeFile(outFilePath, "datetime:" + sdf.format(new Date()), false);
                            if (!result) {
                                throw new MojoExecutionException("filex command " + cmdIndex0.toLowerCase()
                                        + ",write file error:" + listForCmd.get(1) + ":" + outFilePath);
                            }
                        }
                        // 替换命令
                        else if (cmdIndex0.equalsIgnoreCase("replace")) {
                            if (listForCmd.size() < 5) {
                                getLog().info("filex command excute error!replace command parameter error! ");
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(1), true)) {
                                getLog().info("filex command replace skip by tag:" + listForCmd.get(1));
                                continue;
                            }
                            if (!paramOsValue.isEnable()) {
                                getLog().info("filex command replace skip by os," + paramOsValue.getErrMsg() + ":" + listForCmd.get(1));
                                continue;
                            }
                            FileRepalceItem repalceItem = new FileRepalceItem();
                            repalceItem.setReplaceFilePath(listForCmd.get(1));
                            repalceItem.setReplaceRegex(listForCmd.get(2));
                            repalceItem.setReplaceSub(listForCmd.get(3));
                            repalceItem.setRepalceContent(listForCmd.get(4));
                            repalceItem.setRepalceCharset(listForCmd.size() >= 6 ? listForCmd.get(5) : "");
                            getLog().info("filex command repalce:" + repalceItem.toString());
                            boolean result = MojoUtils.repalceContent(repalceItem.getReplaceFilePath(), repalceItem.getReplaceRegex(), repalceItem.getReplaceSub(), repalceItem.getRepalceContent(), repalceItem.getRepalceCharset());
                            if (!result) {
                                throw new MojoExecutionException("filex command repacle ," + repalceItem.toString());
                            }
                        } else if (cmdIndex0.equalsIgnoreCase("exec")) {
                            if (listForCmd.size() < 2) {
                                getLog().info("filex command excute error!exec command parameter error! ");
                                continue;
                            }
                            if (MojoUtils.isSkip(this.lstSkipTags, listForCmd.get(1), true)) {
                                getLog().info("filex command exec skip by tag:" + listForCmd.get(1));
                                continue;
                            }
                            if (null == this.exec || !this.exec.booleanValue()) {
                                getLog().info("filex command exec skip by param:" + listForCmd.get(1));
                                continue;
                            }
                            if (!paramOsValue.isEnable()) {
                                getLog().info("filex command exec skip by os," + paramOsValue.getErrMsg() + ":" + listForCmd.get(1));
                                continue;
                            }
                            String execStr = MojoUtils.parseCmdxParamToExec(listCmdParam, 1, this.spaceTag);
                            if (isHelp) {
                                getLog().info("filex command exec parse rsult is:" + execStr);
                            }
                            if (StringUtils.isEmpty(execStr)) {
                                getLog().info("filex command exec skip by parse:" + listForCmd.get(1));
                            }
                            String result = CmdxUtils.doCmd(execStr, paramOsValue.getOsEnv().getOsType());
                            if (null == result || result.length() <= 0) {
                                getLog().info("filex command exec on " + osEnvDal.getOsShortName() + ":" + "progress exec not has result,may be failed");
                            } else {
                                getLog().info("filex command exec on " + osEnvDal.getOsShortName() + ":" + "progress exec has result,may be successed");
                            }
                        }
                        // 其他命令暂不支持
                        else {
                            getLog().info("filex command " + cmdIndex0.toLowerCase() + " not defined,skip this command!");
                        }
                    } catch (Exception e) {
                        if (null != this.errBreak && this.errBreak.booleanValue()) {
                            throw e;
                        } else {
                            getLog().error("this filex command run error,other command will continue run!", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof MojoExecutionException) {
                String errorMsg = e.getMessage();
                if (null == errorMsg || errorMsg.length() <= 0) {
                    errorMsg = "filex command runtime error!";
                }
                getLog().error(errorMsg, e);
                throw (MojoExecutionException) e;
            } else {
                String errorMsg = e.getMessage();
                if (null == errorMsg || errorMsg.length() <= 0) {
                    errorMsg = "filex command runtime error!";
                } else {
                    errorMsg = "filex command runtime error," + errorMsg;
                }
                getLog().error(errorMsg, e);
                throw new MojoExecutionException(errorMsg, e);
            }
        }
    }
}
