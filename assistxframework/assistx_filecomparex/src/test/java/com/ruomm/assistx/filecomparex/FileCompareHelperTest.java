package com.ruomm.assistx.filecomparex;

import com.ruomm.assistx.filecomparex.core.FileCompareHelper;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/28 19:22
 */
public class FileCompareHelperTest {
    public static void main(String[] args) {
        FileCompareHelper fileCompareHelper =new FileCompareHelper();
        fileCompareHelper.setNewPath("D:\\capturewebappNEW");
        fileCompareHelper.setOldPath("D:\\capturewebapp");
        fileCompareHelper.doCompair();
        fileCompareHelper.copyModifyFile("D:\\temp\\capturewebapp\\modify\\");
        fileCompareHelper.copySameFile("D:\\temp\\capturewebapp\\save\\");
        fileCompareHelper.copyNewFile("D:\\temp\\capturewebapp\\new\\");
        fileCompareHelper.copyDeleteFile("D:\\temp\\capturewebapp\\del\\");

    }
}
