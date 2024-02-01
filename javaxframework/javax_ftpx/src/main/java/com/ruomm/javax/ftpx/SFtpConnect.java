/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月22日 上午10:34:21
 */
package com.ruomm.javax.ftpx;

import com.jcraft.jsch.ChannelSftp;
import com.ruomm.javax.ftpx.core.SFtpXAbstract;

/**
 * 长连接FTP上传下载删除文件以及其它
 *
 * @author 牛牛-wanruome@163.com
 *
 */
public class SFtpConnect extends SFtpXAbstract {

    public SFtpConnect() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SFtpConnect(String hostname, Integer port, String username, String password) {
        super(hostname, port, username, password);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean isLongConnect() {
        // TODO Auto-generated method stub
        return true;
    }

//	public ChannelSftp connect() {
//		return connectClient();
//	}

    public void connect() {
        connectClient();
    }

    public void disconnect() {
        disconnectClient();
    }

    public ChannelSftp getConnectChannel() {
        return getRunningChannel();
    }
}
