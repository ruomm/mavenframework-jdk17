/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月22日 上午9:31:02
 */
package com.ruomm.javax.ftpx.core;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.MalformedURLException;

/**
 * 短连接FTP上传下载删除文件
 *
 * @author 牛牛-wanruome@163.com
 *
 */
public abstract class FtpXAbstract {
    private final static Log log = LogFactory.getLog(FtpXAbstract.class);
    // ftp服务器地址
    private String hostname = "127.0.0.1";
    // ftp服务器端口号默认为21
    private Integer port = 21;
    // ftp登录账号
    private String username = "anonymous";
    // ftp登录密码
    private String password = "";
    // ftp客户端
    private FTPClient ftpClient = null;
    // 是否打印日志
    private boolean logPrint = false;

    public FtpXAbstract() {
        super();
    }

    public FtpXAbstract(String hostname, Integer port, String username, String password) {
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

    protected FTPClient getRunningChannel() {
        return ftpClient;
    }

    protected abstract boolean isLongConnect();

    /**
     * 初始化ftp服务器
     */
    protected FTPClient connectClient() {
        boolean isLogin = false;

        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            switchSyso("connecting...ftp服务器:" + this.hostname + ":" + this.port);
            ftpClient.connect(hostname, port); // 连接ftp服务器
            ftpClient.login(username, password); // 登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                switchSyso("connect failed...ftp服务器:" + this.hostname + ":" + this.port);
            } else {
                isLogin = true;
                switchSyso("connect successful...ftp服务器:" + this.hostname + ":" + this.port);
            }
        } catch (MalformedURLException e) {
            log.error("Error:connectClient", e);
        } catch (IOException e) {
            log.error("Error:connectClient", e);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:connectClient", e);
        }
        if (!isLogin) {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                    switchSyso("连接FTP服务器失败->网络不可用！ftp服务器:" + this.hostname + ":" + this.port);
                } else {
                    switchSyso("连接FTP服务器失败->用户名密码不正确！ftp服务器:" + this.hostname + ":" + this.port);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("Error:connectClient", e);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:connectClient", e);
            }
            ftpClient = null;
        } else {
            switchSyso("连接FTP服务器成功->ftp服务器:" + this.hostname + ":" + this.port);
        }
        return ftpClient;
    }

    /**
     * 断开FTP服务器
     *
     * @return
     */
    protected void disconnectClient() {
        if (null == ftpClient) {
            switchSyso("断开FTP连接成功->没法发现连接！");
            return;
        }
        try {
            switchSyso("logout running...ftp服务器:" + this.hostname + ":" + this.port);
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("Error:disconnectClient", e);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:disconnectClient", e);
        }
        try {
            switchSyso("disconnect running...ftp服务器:" + this.hostname + ":" + this.port);
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("Error:disconnectClient", e);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:disconnectClient", e);
        }
        switchSyso("断开FTP连接成功->ftp服务器:" + this.hostname + ":" + this.port);
        ftpClient = null;
    }

    /**
     * 上传文件
     *
     * @param remotePath     ftp服务保存地址
     * @param fileName       上传到ftp的文件名
     * @param originfilename 待上传文件的名称（绝对地址） *
     * @return
     */
    public boolean uploadFile(String remotePath, String fileName, String originfilename) {
        if (isLongConnect() && null == ftpClient) {
            switchSyso("上传文件失败->FTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        InputStream inputStream = null;
        try {
            switchSyso("开始上传文件->远程路径：" + pathname + "/" + fileName + "，本地路径：" + originfilename);
            if (!isLongConnect()) {
                connectClient();
                if (null == ftpClient) {
                    switchSyso("上传文件失败->FTPClient还没有连接");
                }
            }
            boolean crtFlag = createDirecroty(pathname, true);
            if (!crtFlag) {
                switchSyso("上传文件失败->无法创建所需目录");
                return false;
            }
            inputStream = new FileInputStream(new File(originfilename));
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.storeFile(fileName, inputStream);
            ftpClient.logout();
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
            inputStream = null;
            if (!isLongConnect()) {
                disconnectClient();
            }

        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param remotePath  ftp服务保存地址
     * @param fileName    上传到ftp的文件名
     * @param inputStream 输入文件流
     * @return
     */
    public boolean uploadFile(String remotePath, String fileName, InputStream inputStream) {
        if (isLongConnect() && null == ftpClient) {
            switchSyso("上传文件失败->FTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        try {
            switchSyso("开始上传文件->远程路径：" + pathname + "/" + fileName + "，本地内容：输入流");
            if (!isLongConnect()) {
                connectClient();
                if (null == ftpClient) {
                    switchSyso("上传文件失败->FTPClient还没有连接");
                }
            }
            boolean crtFlag = createDirecroty(pathname, true);
            if (!crtFlag) {
                switchSyso("上传文件失败->无法创建所需目录");
                return false;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.storeFile(fileName, inputStream);
            ftpClient.logout();
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
     * @param remotePath FTP服务器文件目录 *
     * @param filename   文件名称 *
     * @param localpath  下载后的文件路径 *
     * @return
     */
    public boolean downloadFile(String remotePath, String filename, String localpath) {
        if (isLongConnect() && null == ftpClient) {
            switchSyso("下载文件失败->FTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        OutputStream os = null;
        try {
            switchSyso("开始下载文件->远程路径：" + pathname + "/" + filename + "，本地路径：" + localpath);
            if (!isLongConnect()) {
                connectClient();
                if (null == ftpClient) {
                    switchSyso("下载文件失败->FTPClient还没有连接");
                }
            }
            // 切换FTP目录
            boolean chgFlag = changeWorkingDirectory(pathname);
            if (!chgFlag) {
                switchSyso("下载文件失败->无法进入指定目录");
                return false;
            }
            FTPFile[] ftpFiles = ftpClient.listFiles();
            boolean isHas = false;
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File localFile = null;
                    if (localpath.endsWith("/") || localpath.endsWith("\\")) {
                        localFile = new File(localpath + "/" + file.getName());
                    } else {
                        localFile = new File(localpath);
                    }
                    switchSyso("下载文件本地路径->" + localFile.getAbsolutePath());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                    isHas = true;
                }
            }
            ftpClient.logout();
            flag = isHas;
            if (isHas) {
                switchSyso("下载文件成功！");
            } else {
                switchSyso("下载文件失败->文件不存在");
            }
        } catch (Exception e) {
            log.error("Error:downloadFile", e);
            switchSyso("下载文件失败->发生异常");
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("Error:downloadFile", e);
                }
            }
            os = null;
            if (!isLongConnect()) {
                disconnectClient();
            }
        }
        return flag;
    }

    /**
     * * 删除文件 *
     *
     * @param remotePath FTP服务器保存目录 *
     * @param filename   要删除的文件名称 *
     * @return
     */
    public boolean deleteFile(String remotePath, String filename) {
        if (isLongConnect() && null == ftpClient) {
            switchSyso("下载文件失败->FTPClient还没有连接");
            return false;
        }
        String pathname = transFtpPath(remotePath);
        boolean flag = false;
        try {
            switchSyso("开始删除文件->远程路径：" + pathname + "/" + filename);
            if (!isLongConnect()) {
                connectClient();
                if (null == ftpClient) {
                    switchSyso("下载文件失败->FTPClient还没有连接");
                }
            }
            // 切换FTP目录
            boolean chgFlag = changeWorkingDirectory(pathname);
            if (!chgFlag) {
                switchSyso("删除文件失败->无法进入指定目录");
                return false;
            }
            ftpClient.dele(filename);
            ftpClient.logout();
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
     * 改变FTP工作目录
     *
     * @param remotePath FTP远程目录
     * @return
     */
    public boolean changeWorkingDirectory(String remotePath) {
        String directory = transFtpPath(remotePath);
        if (null == ftpClient) {
            switchSyso("进入文件夹" + directory + " 失败->，FTPClient还没有连接");
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
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                switchSyso("进入文件夹" + directory + " 成功！");

            } else {
                switchSyso("进入文件夹->" + directory + " 失败！");
            }
        } catch (IOException e) {
            log.error("Error:changeWorkingDirectory", e);
            switchSyso("进入文件夹" + directory + " 失败->发生异常");
        }
        return flag;
    }

    /**
     * 创建FTP目录
     *
     * @param remotePath      FTP远程目录
     * @param isChangeWorkDir 是否修改工作目录
     * @return
     */
    public boolean createDirecroty(String remotePath, boolean isChangeWorkDir) {
        String pathname = transFtpPath(remotePath);
        if (null == pathname) {
            switchSyso("创建目录[" + "null" + "]失败->目录名称必须输入，不能为NULL");
            return false;
        }
        if (null == ftpClient) {
            switchSyso("创建目录[" + pathname + "]失败->FTPClient还没有连接");
            return false;
        }
        String workDir = null;
        try {
            workDir = ftpClient.printWorkingDirectory();
        } catch (Exception e) {
            // TODO: handle exception
            workDir = null;
            log.error("Error:createDirecroty", e);
        }
        if (null == workDir) {
            switchSyso("创建目录[" + pathname + "]失败->无法获取当前工作目录");
            return false;
        }
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
                ftpClient.changeWorkingDirectory(workDir);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("Error:createDirecroty", e);
            }
            switchSyso("创建目录[" + pathname + "]失败->回到当前工作目录" + workDir);
            return false;
        } else {
            if (!isChangeWorkDir) {
                try {
                    ftpClient.changeWorkingDirectory(workDir);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:createDirecroty", e);
                }
                switchSyso("创建目录[" + pathname + "]成功->回到当前工作目录" + workDir);
            }
            return true;
        }
    }

    /**
     * 创建FTP目录
     *
     * @param remotePath FTP远程目录
     * @return
     */
    private boolean createDirecrotyException(String remotePath) throws IOException {
        String pathname = transFtpPath(remotePath);
        if (null == pathname) {
            switchSyso("创建目录[" + "null" + "]失败->目录名称必须输入，不能为NULL");
            return false;
        }
        if (null == ftpClient) {
            switchSyso("创建目录[" + pathname + "]失败->FTPClient还没有连接");
            return false;
        }
        // 不创建目录，在当前的工作目录
        if (pathname.length() <= 0) {
            switchSyso("创建目录[" + pathname + "]->不创建目录，在当前的工作目录");
            return true;
        } else if (pathname.equalsIgnoreCase("/")) {
            // 改变到根目录
            boolean cwkdFlag = ftpClient.changeWorkingDirectory("/");
            if (!cwkdFlag) {
                switchSyso("创建目录[" + pathname + "]->失败，无法修改工作目录到根目录！");
            } else {
                switchSyso("创建目录[" + pathname + "] 成功！");
            }
            return cwkdFlag;
        } else if (ftpClient.changeWorkingDirectory(pathname)) {
            switchSyso("创建目录[" + pathname + "] 成功->目录已经存在，无需创建");
            return true;
        } else {
            String[] dirs = null;
            if (pathname.startsWith("/")) {
                boolean cwkdFlag = ftpClient.changeWorkingDirectory("/");
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
                FTPFile[] ftpFileArr = ftpClient.listFiles(subDirectory);
                if (ftpFileArr.length > 0) {
                    subFlag = ftpClient.changeWorkingDirectory(subDirectory);
                    switchSyso("创建子目录[" + subDirectory + "]成功->已经存在无需创建 ！");
                } else {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        subFlag = ftpClient.changeWorkingDirectory(subDirectory);
                        if (subFlag) {
                            switchSyso("创建子目录[" + subDirectory + "] 成功，并进入成功！");
                        } else {
                            switchSyso("创建子目录[" + subDirectory + "] 成功，但进入失败，返回false！");
                        }
                    } else {
                        subFlag = false;
                        switchSyso("创建子目录[" + subDirectory + "] 失败！");
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
     * 判断FTP文件是否存在
     *
     * @param remoteFilePath FTP远程文件路径
     * @return
     */
    public boolean existFile(String remoteFilePath) {
        String path = transFtpPath(remoteFilePath);
        if (null == ftpClient) {
            switchSyso("检查文件" + path + "存在失败->FTPClient还没有连接");
            return false;
        }
        try {
            boolean flag = false;
            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
            if (null != ftpFileArr && ftpFileArr.length > 0) {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:existFile", e);
            return false;
        }
    }

    /**
     * 判断FTP目录是否存在
     *
     * @param remoteFilePath FTP远程文件路径
     * @return
     */
    public boolean existDir(String remoteFilePath) {
        String path = transFtpPath(remoteFilePath);
        if (null == ftpClient) {
            switchSyso("检查目录" + path + "存在失败->FTPClient还没有连接");
            return false;
        }
        try {
            FTPFile[] ftpFileArr = ftpClient.listFiles(path);
            if (null == ftpFileArr) {
                return false;
            } else if (ftpFileArr.length > 5) {
                return true;
            } else if (ftpFileArr.length <= 1) {
                return false;
            } else {
                boolean isOne = false;
                boolean isTwo = false;
                for (FTPFile ftpFile : ftpFileArr) {
                    if (".".equalsIgnoreCase(ftpFile.getName())) {
                        isOne = true;
                    }
                    if ("..".equalsIgnoreCase(ftpFile.getName())) {
                        isTwo = true;
                    }
                    if (isOne && isTwo) {
                        break;
                    }
                }
                if (isOne && isTwo) {
                    return true;
                } else {
                    return false;
                }
            }
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
