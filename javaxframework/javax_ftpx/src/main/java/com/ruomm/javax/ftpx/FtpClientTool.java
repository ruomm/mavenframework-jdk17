/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月22日 上午9:31:02
 */
package com.ruomm.javax.ftpx;

import com.ruomm.javax.ftpx.core.FtpXAbstract;

/**
 * 短连接FTP上传下载删除文件
 *
 * @author 牛牛-wanruome@163.com
 *
 */
public class FtpClientTool extends FtpXAbstract {

    public FtpClientTool() {
        super();
        // TODO Auto-generated constructor stub
    }

    public FtpClientTool(String hostname, Integer port, String username, String password) {
        super(hostname, port, username, password);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean isLongConnect() {
        // TODO Auto-generated method stub
        return false;
    }

}
