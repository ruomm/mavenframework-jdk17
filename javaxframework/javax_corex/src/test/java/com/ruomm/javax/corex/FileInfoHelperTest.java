package com.ruomm.javax.corex;

import com.ruomm.javax.corex.helper.FileInfoFilter;
import com.ruomm.javax.corex.helper.FileInfoHelper;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/27 0:28
 */
public class FileInfoHelperTest {
    public static void main(String[] args) {
        testFileInfoHelper("D:\\capturewebapp");
    }

    public static void testFileInfoHelper(String path) {
        FileInfoHelper fileInfoHelper = new FileInfoHelper();
        fileInfoHelper.setPath(path);
        fileInfoHelper.setShowDir(true);
        fileInfoHelper.setFileFilter(new FileInfoFilter() {
            @Override
            public boolean isFilter(String pathFull, String pathLite) {
                if (pathLite.contains("include\\win32")) {
                    return false;
                } else {
                    return false;
                }
            }

            @Override
            public boolean isSignByDefine() {
                return false;
            }

            @Override
            public String doSignByDefine(String pathFull) {
                return null;
            }
        });
        fileInfoHelper.getFileInfoList(true);
        fileInfoHelper.calFilesSha();
        fileInfoHelper.calFilesSha();
        fileInfoHelper.getFileInfoList(true);
        fileInfoHelper.calFilesSha();
        fileInfoHelper.calFilesSha();
//        for (FileInfoDal tmp : fileInfoHelper.getFileInfos()) {
//            System.out.println(tmp.toString());
//        }
        System.out.println(fileInfoHelper.getFileInfoList().size());
        System.out.println(fileInfoHelper.counterFileNum());
        System.out.println(fileInfoHelper.counterDirNum());
        System.out.println(fileInfoHelper.getSizeBytes());
        System.out.println(fileInfoHelper.getSizeMB());
    }
}
