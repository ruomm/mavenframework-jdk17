package com.ruomm.javax.filetypex;

import java.io.IOException;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/9 9:52
 */
public class FileTypexTest {
    public static void main(String[] args) throws IOException {
        String type = FileTypeUtils.getFileType("D:\\TempLuoli\\20211008_100422.xlsx");
        System.out.println(type);
    }
}
