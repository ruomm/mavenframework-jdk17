/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月27日 下午4:51:18
 */
package org.assistx_dbjavax.util;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLCompress {
    public static void main(String[] args) {
        compress("C:\\Users\\QX\\Documents\\表", "C:\\Users\\QX\\Documents\\表\\temp");
    }

    public static void compress(String sourcePath, String desPath) {
        List<File> listFiles = new ArrayList<File>();
        File sourceFile = new File(sourcePath);
        if (null == sourceFile || !sourceFile.exists()) {
            return;
        } else if (sourceFile.isFile()) {
            listFiles.add(sourceFile);
        } else {
            File[] tmpFiles = sourceFile.listFiles();
            if (null == tmpFiles || tmpFiles.length <= 0) {
                return;
            }
            for (File tmpF : tmpFiles) {
                if (tmpF.exists() && tmpF.isFile() && tmpF.getPath().toLowerCase().endsWith(".sql")) {
                    listFiles.add(tmpF);
                }
            }
        }
        if (listFiles.size() <= 0) {
            return;
        }
        for (File tmpF : listFiles) {
            List<String> listStr = FileUtils.readFileToList(tmpF.getPath(), "UTF-8", true);
            String itemDesPath = desPath + "\\" + FileUtils.getFileName(tmpF.getPath());
            int count = 0;
            boolean isAppend = false;
            for (String tmp : listStr) {
                if (null != tmp && tmp.startsWith("DROP TABLE ")) {
                    continue;
                }
                if (null != tmp && tmp.startsWith("-- Checks structure for table ")) {
                    count++;
                } else if (count > 0 && null != tmp && tmp.startsWith("-- --------------")) {
                    count++;
                }
                if (count == 2 && null != tmp && tmp.startsWith("ALTER TABLE ")) {
                    continue;
                }
                if (count > 2) {
                    count = 0;
                }
                FileUtils.writeFile(itemDesPath, StringUtils.nullStrToEmpty(tmp) + "\r\n", isAppend);
                if (!isAppend) {
                    isAppend = true;
                }
            }
        }
    }
}
