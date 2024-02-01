package com.ruomm.javax.corex.helper;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/27 0:16
 */
public interface FileInfoFilter {

    boolean isFilter(String pathFull, String pathLite);

    boolean isSignByDefine();

    String doSignByDefine(String pathFull);
}
