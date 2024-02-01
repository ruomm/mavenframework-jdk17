/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月22日 上午10:34:21
 */
package com.ruomm.javax.ftpx.core;

import com.jcraft.jsch.*;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 短连接SFTP上传下载删除文件
 *
 * @author 牛牛-wanruome@163.com
 *
 */
public abstract class SFtpXAbstract {
    private final static Log log = LogFactory.getLog(FtpXAbstract.class);
    // sftp服务器地址
    private String hostname = "127.0.0.1";
    // sftp服务器端口号默认为21
    private Integer port = 21;
    // sftp登录账号
    private String username = "anonymous";
    // sftp登录密码
    private String password = "";
    // sftp客户端
    private ChannelSftp channel = null;
    // sftp回话Session
    private Session session;
    // 是否打印日志
    private boolean logPrint = false;

    public SFtpXAbstract() {
        super();
    }

    public SFtpXAbstract(String hostname, Integer port, String username, String password) {
        super();
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogPrint() {
        return logPrint;
    }

    public void setLogPrint(boolean logPrint) {
        this.logPrint = logPrint;
    }

    protected ChannelSftp getRunningChannel() {
        return this.channel;
    }

    protected abstract boolean isLongConnect();

//	protected abstract boolean initFtpClient();

    /**
     * 初始化SFTP服务器
     */
    protected ChannelSftp connectClient() {
        boolean isLogin = false;
        try {
            // 创建JSch对象
            JSch jsch = new JSch();
            // 根据用户名，主机ip，端口获取一个Session对象
            session = jsch.getSession(username, hostname, port);
            if (password != null) {
                // 设置密码
                session.setPassword(password);
            }
            Properties configTemp = new Properties();
            configTemp.put("StrictHostKeyChecking", "no");
            // 为Session对象设置properties
            session.setConfig(configTemp);
            // 设置timeout时间
            session.setTimeout(60000);
            // 通过Session建立链接
            // 打开SFTP通道
            session.connect();
            // 建立SFTP通道的连接
            channel = (ChannelSftp) session.openChannel("sftp");
            // 建立SFTP通道的连接
            channel.connect();
            isLogin = true;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:connectClient", e);
        }
        if (!isLogin) {
            String errMsg = null;
            try {
                if (channel != null) {
                    errMsg = "连接SFTP服务器失败->channel无法建立！ftp服务器:" + this.hostname + ":" + this.port;
                    channel.disconnect();
                }
            } catch (Exception e) {
                log.error("Error:connectClient", e);
            }
            channel = null;
            try {
                if (session != null) {
                    errMsg = "连接SFTP服务器失败->session无法建立！ftp服务器:" + this.hostname + ":" + this.port;
                    session.disconnect();
                }
            } catch (Exception e) {
                log.error("Error:connectClient", e);
            }
            session = null;
            if (null == errMsg || errMsg.length() <= 0) {
                errMsg = "连接SFTP服务器失败->session无法建立！ftp服务器:" + this.hostname + ":" + this.port;
            }
            switchSyso(errMsg);
        } else {
            switchSyso("连接SFTP服务器成功->sftp服务器:" + this.hostname + ":" + this.port);
        }
        return channel;
    }

    /**
     * 断开SFTP服务器
     *
     * @return
     */
    protected void disconnectClient() {
        if (null == channel && null == session) {
            switchSyso("断开SFTP连接成功->没法发现连接！");
        }
        try {
            if (channel != null) {
                switchSyso("disconnect channel running...sftp服务器:" + this.hostname + ":" + this.port);
                channel.disconnect();
            }
        } catch (Exception e) {
            log.error("Error:disconnectClient", e);
        }
        channel = null;
        try {
            if (session != null) {
                switchSyso("disconnect session running...sftp服务器:" + this.hostname + ":" + this.port);
                session.disconnect();
            }
        } catch (Exception e) {
            log.error("Error:disconnectClient", e);
        }
        session = null;
        switchSyso("断开SFTP连接成功->sftp服务器:" + this.hostname + ":" + this.port);
    }

    /**
     * 上传文件
     *
     * @param remotePath     sftp服务保存地址
     * @param fileName       上传到sftp的文件名
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return
     */
    public boolean uploadFile(String remotePath, String fileName, String originfilename) {
        if (isLongConnect() && (null == channel || null == session)) {
            switchSyso("上传文件失败->SFTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        try {

            switchSyso("开始上传文件->远程路径：" + pathname + "/" + fileName + "，本地路径：" + originfilename);
            if (!isLongConnect()) {
                connectClient();
                if (null == session || null == channel) {
                    switchSyso("上传文件失败->SFTPClient还没有连接");
                }
            }
            boolean crtFlag = createDirecroty(pathname, true);
            if (!crtFlag) {
                switchSyso("上传文件失败->无法创建所需目录");
                return false;
            }
            channel.put(originfilename, fileName, ChannelSftp.OVERWRITE);
            flag = true;
            switchSyso("上传文件成功！");
        } catch (Exception e) {
            log.error("Error:uploadFile", e);
            switchSyso("上传文件失败->发生异常");
        } finally {
            if (!isLongConnect()) {
                disconnectClient();
            }
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param remotePath  sftp服务保存地址
     * @param fileName    上传到sftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public boolean uploadFile(String remotePath, String fileName, InputStream inputStream) {
        if (isLongConnect() && (null == channel || null == session)) {
            switchSyso("上传文件失败->SFTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        try {
            switchSyso("开始上传文件->远程路径：" + pathname + "/" + fileName + "，本地内容：输入流");
            if (!isLongConnect()) {
                connectClient();
                if (null == session || null == channel) {
                    switchSyso("上传文件失败->SFTPClient还没有连接");
                }
            }
            boolean crtFlag = createDirecroty(pathname, true);
            if (!crtFlag) {
                switchSyso("上传文件失败->无法创建所需目录");
                return false;
            }
            channel.put(inputStream, fileName, ChannelSftp.OVERWRITE);
            flag = true;
            switchSyso("上传文件成功！");
        } catch (Exception e) {
            log.error("Error:uploadFile", e);
            switchSyso("上传文件失败->发生异常");
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error:uploadFile", e);
                }
            }
            if (!isLongConnect()) {
                disconnectClient();
            }
        }

        return flag;
    }

    /**
     * * 下载文件 *
     *
     * @param remotePath SFTP服务器文件目录 *
     * @param filename   文件名称 *
     * @param localpath  下载后的文件路径 *
     * @return
     */
    public boolean downloadFile(String remotePath, String filename, String localpath) {
        if (isLongConnect() && (null == channel || null == session)) {
            switchSyso("下载文件失败->SFTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        try {
            switchSyso("开始下载文件->远程路径：" + pathname + "/" + filename + "，本地路径：" + localpath);
            if (!isLongConnect()) {
                connectClient();
                if (null == session || null == channel) {
                    switchSyso("下载文件失败->SFTPClient还没有连接");
                }
            }
            // 切换FTP目录
            boolean chgFlag = changeWorkingDirectory(pathname);
            if (!chgFlag) {
                switchSyso("下载文件失败->无法进入指定目录");
                return false;
            }
            channel.get(filename, localpath);
            flag = true;
            switchSyso("下载文件成功！");
        } catch (Exception e) {
            log.error("Error:downloadFile", e);
            switchSyso("下载文件失败->发生异常");
        } finally {
            if (!isLongConnect()) {
                disconnectClient();
            }
        }
        return flag;
    }

    /**
     * * 删除文件 *
     *
     * @param remotePath SFTP服务器保存目录 *
     * @param filename   要删除的文件名称 *
     * @return
     */
    public boolean deleteFile(String remotePath, String filename) {
        if (isLongConnect() && (null == channel || null == session)) {
            switchSyso("删除文件失败->SFTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        try {
            switchSyso("开始删除文件->远程路径：" + pathname + "/" + filename);
            if (!isLongConnect()) {
                connectClient();
                if (null == session || null == channel) {
                    switchSyso("删除文件失败->SFTPClient还没有连接");
                }
            }
            // 切换FTP目录
            boolean chgFlag = changeWorkingDirectory(pathname);
            if (!chgFlag) {
                switchSyso("删除文件失败->无法进入指定目录");
                return false;
            }
            channel.rm(filename);
            flag = true;
            switchSyso("删除文件成功！");
        } catch (Exception e) {
            log.error("Error:deleteFile", e);
            switchSyso("删除文件失败->发生异常");
        } finally {
            if (!isLongConnect()) {
                disconnectClient();
            }
        }
        return flag;
    }

    /**
     * 改变SFTP工作目录
     *
     * @param remotePath SFTP远程目录
     * @return
     */
    public boolean changeWorkingDirectory(String remotePath) {
        String directory = transFtpPath(remotePath);
        if (null == channel || null == session) {
            switchSyso("进入文件夹" + directory + " 失败->，SFTPClient还没有连接");
            return false;
        }
        if (null == directory) {
            switchSyso("进入文件夹" + "null" + " 失败->，目录名称必须输入，不能为NULL");
            return false;
        }
        if (directory.length() <= 0) {
            switchSyso("进入文件夹" + directory + " 成功->输入为空，不改变工作目录位置");
            return true;
        }
        boolean flag = false;
        try {
            channel.cd(directory);
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
            log.error("Error:changeWorkingDirectory", e);
            switchSyso("进入文件夹" + directory + " 失败->发生异常");
        }
        if (flag) {
            switchSyso("进入文件夹" + directory + " 成功！");

        } else {
            switchSyso("进入文件夹->" + directory + " 失败！");
        }
        return flag;
    }

    /**
     * 创建SFTP目录
     *
     * @param remotePath      SFTP远程目录
     * @param isChangeWorkDir 是否修改工作目录
     * @return
     */
    public boolean createDirecroty(String remotePath, boolean isChangeWorkDir) {
        String pathname = transFtpPath(remotePath);
        if (null == pathname) {
            switchSyso("创建目录[" + "null" + "]失败->目录名称必须输入，不能为NULL");
            return false;
        }
        if (null == channel || null == session) {
            switchSyso("创建目录[" + pathname + "]失败->SFTPClient还没有连接");
            return false;
        }
        String workDir = null;
        try {
            workDir = channel.pwd();
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            workDir = null;
            log.error("Error:createDirecroty", e);
        }
//		if (null == workDir) {
//			switchSyso("创建目录[" + pathname + "]失败->无法获取当前工作目录");
//			return false;
//		}
        switchSyso("当前工作目录->" + workDir);
        boolean flag = false;
        try {
            flag = createDirecrotyException(pathname);
        } catch (Exception e) {
            // TODO: handle exception
            flag = false;
            log.error("Error:createDirecroty", e);
        }
        if (!flag) {
            try {
                if (null != workDir && workDir.length() > 0) {
                    channel.cd(workDir);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("Error:createDirecroty", e);
            }
            switchSyso("创建目录[" + pathname + "]失败->回到当前工作目录" + workDir);
            return false;
        } else {
            if (!isChangeWorkDir) {
                try {
                    if (null != workDir && workDir.length() > 0) {
                        channel.cd(workDir);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    log.error("Error:createDirecroty", e);
                }
                switchSyso("创建目录[" + pathname + "]成功->回到当前工作目录" + workDir);
            }
            return true;
        }
    }

    /**
     * 创建SFTP目录
     *
     * @param remotePath SFTP远程目录
     * @return
     * @throws SftpException
     */
    private boolean createDirecrotyException(String remotePath) throws IOException, SftpException {
        String pathname = transFtpPath(remotePath);
        if (null == pathname) {
            switchSyso("创建目录[" + "null" + "]失败->目录名称必须输入，不能为NULL");
            return false;
        }
        if (null == channel || null == session) {
            switchSyso("创建目录[" + pathname + "]失败->SFTPClient还没有连接");
            return false;
        }
        // 不创建目录，在当前的工作目录
        if (pathname.length() <= 0) {
            switchSyso("创建目录[" + pathname + "]->不创建目录，在当前的工作目录");
            return true;
        } else if (pathname.equalsIgnoreCase("/")) {
            // 改变到根目录
            boolean cwkdFlag = changeWorkingDirectory("/");
            if (!cwkdFlag) {
                switchSyso("创建目录[" + pathname + "]->失败，无法修改工作目录到根目录！");
            } else {
                switchSyso("创建目录[" + pathname + "] 成功！");
            }
            return cwkdFlag;
        } else if (changeWorkingDirectory(pathname)) {
            switchSyso("创建目录[" + pathname + "] 成功->目录已经存在，无需创建");
            return true;
        } else {
            String[] dirs = null;
            if (pathname.startsWith("/")) {
                boolean cwkdFlag = changeWorkingDirectory("/");
                if (!cwkdFlag) {
                    switchSyso("创建目录[" + pathname + "]->失败，无法修改工作目录到根目录！");
                    return false;
                }
                dirs = pathname.substring(1).split("/");
            } else {
                dirs = pathname.split("/");
            }

            boolean flag = true;
            for (String tmp : dirs) {
                boolean subFlag = false;
                if (null == tmp || tmp.length() <= 0) {
                    subFlag = true;
                    continue;
                }
                String subDirectory = new String(tmp.getBytes("GBK"), "iso-8859-1");
                subFlag = changeWorkingDirectory(subDirectory);
                if (subFlag) {
                    switchSyso("创建子目录[" + subDirectory + "]成功->已经存在无需创建 ！");
                } else if (existFile(subDirectory)) {
                    switchSyso("创建子目录[" + subDirectory + "] 失败，子目录不可进入或是文件！");
                } else {

                    channel.mkdir(subDirectory);
                    subFlag = changeWorkingDirectory(subDirectory);
                    if (subFlag) {
                        switchSyso("创建子目录[" + subDirectory + "] 成功，并进入成功！");
                    } else {
                        switchSyso("创建子目录[" + subDirectory + "] 成功，但进入失败，返回false！");
                    }
                }
                if (!subFlag) {
                    flag = subFlag;
                    break;
                }
            }
            if (flag) {
                switchSyso("创建目录[" + pathname + "] 成功！");
            } else {
                switchSyso("创建目录[" + pathname + "]失败->部分子目录无法创建！");
            }
            return flag;

        }
    }

    /**
     * 判断SFTP文件或目录是否存在
     *
     * @param remoteFilePath SFTP远程文件路径
     * @return
     */
    public boolean existFile(String remoteFilePath) {
        String path = transFtpPath(remoteFilePath);
        if (null == channel || null == session) {
            switchSyso("检查文件" + path + "存在失败->SFTPClient还没有连接");
            return false;
        }
        try {
            SftpATTRS sftpATTRS = channel.lstat(path);
            if (null == sftpATTRS) {
                return false;
            } else {
                return true;

            }
//			boolean flag = false;
//
//			@SuppressWarnings("rawtypes")
//			Vector vector = channel.ls(path);
//			if (null != vector && !vector.isEmpty()) {
//				flag = true;
//			}
//			return flag;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:existFile", e);
            return false;
        }
    }

    /**
     * 判断SFTP目录是否存在
     *
     * @param remoteFilePath SFTP远程文件路径
     * @return
     */
    public boolean existDir(String remoteFilePath) {
        String path = transFtpPath(remoteFilePath);
        if (null == channel || null == session) {
            switchSyso("检查文件" + path + "存在失败->SFTPClient还没有连接");
            return false;
        }
        try {
            SftpATTRS sftpATTRS = channel.lstat(path);
            if (null == sftpATTRS) {
                return false;
            }
            return sftpATTRS.isDir();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:existDir", e);
            return false;
        }
    }

    private void switchSyso(String msg) {
        if (logPrint) {
            if (null == msg || msg.length() <= 0) {
                log.debug("EMPTY MESSAGE!");
            } else {
                log.info(msg);
            }
        } else {
            if (null == msg || msg.length() <= 0) {
                log.debug("EMPTY MESSAGE!");
            } else {
                log.debug(msg);
            }
        }
    }

    private String transFtpPath(String remotePath) {
        if (null == remotePath || remotePath.length() <= 0) {
            return remotePath;
        }
        String path = remotePath.replace("\\", "/");
        return path;
    }

}
