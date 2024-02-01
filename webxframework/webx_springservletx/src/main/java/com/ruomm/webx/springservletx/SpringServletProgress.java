/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月10日 下午1:12:19
 */
package com.ruomm.webx.springservletx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SpringServletProgress {
    public void doProgress(HttpServletRequest request, HttpServletResponse response);
}