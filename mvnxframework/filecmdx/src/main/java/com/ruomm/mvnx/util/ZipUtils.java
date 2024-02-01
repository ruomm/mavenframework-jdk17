/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月18日 下午5:12:19
 */
package com.ruomm.mvnx.util;

import com.ruomm.javax.corex.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
    public static boolean unZip(String sourcePath, String dstDirParam) throws Exception {
        String dstDir = replaceFileSeparator(dstDirParam);
        String dstDirPath = FileUtils.isEndWithFileSeparator(dstDir) ? dstDir : dstDir + FileUtils.parseFileSeparator(dstDir);
        File fileDesDir = new File(dstDirPath);
        if (!fileDesDir.exists()) {
            fileDesDir.mkdirs();
        }
        // 开始解压
        ZipFile zipFile = null;

        try {
            zipFile = new ZipFile(sourcePath);
            Enumeration<?> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹

                if (entry.isDirectory()) {
                    String dirPath = dstDirPath + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(dstDirPath + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
//					if(targetFile.exists()&&targetFile.isFile()){
//						targetFile.delete();
//					}
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;

                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("unzip error from ZipUtils", e);

        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    private static String replaceFileSeparator(String filePath) {
        if (null == filePath || filePath.length() <= 0) {
            return filePath;
        }
        String resultPath = filePath.replace("\\", File.separator).replace("/", File.separator).replace(File.separator + File.separator, File.separator);
        return resultPath;
    }
}
