/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月5日 下午3:31:57
 */
package com.ruomm.assistx.demo;

import com.ruomm.assistx.filecomparex.core.FileReplaceHelper;
import com.ruomm.assistx.filecomparex.core.FileReplaceHelper.ReplaceHelper;
import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.dal.FileInfoDal;

public class FileReplaceTest {
    public static void main(String[] args) {
        FileReplaceHelper fileReplaceHelper = new FileReplaceHelper(
                "D:\\AndroidStudioProjectsBJ\\crossborderpaySupportV7\\ruomm_android_library\\",
                "D:\\temp\\baidu\\ruomm_android_library");
        fileReplaceHelper.setShaEnable(true);
        fileReplaceHelper.setDebug(true);
        fileReplaceHelper.setReplaceHelper(replaceHelper);
        fileReplaceHelper.doCopyReplace();

    }

    private static ReplaceHelper replaceHelper = new ReplaceHelper() {
        @Override
        public String destFileName(String fileKey, FileInfoDal newFileInfo) {
            return fileKey;
        }

        @Override
        public boolean isCopyReplaceByDefine(String fileKey, FileInfoDal srcFileInfo) {
            if (fileKey.endsWith(".java")) {
                return true;
            }
            if (fileKey.endsWith("build.gradle")) {
                return true;
            }
            return false;
        }

        @Override
        public boolean doCopyReplaceByDefine(String fileKey, FileInfoDal srcFileInfo, String destPath) {
            String content = FileUtils.readFile(srcFileInfo.getPathFull(), "UTF-8");
            int countLength = content.length();
            content = content.replaceAll("import android.support.annotation.NonNull",
                    "import androidx.annotation.NonNull");
            content = content.replaceAll("import android.support.annotation.Nullable",
                    "import androidx.annotation.Nullable");
            content = content.replaceAll("import android.support.annotation.ColorInt",
                    "import androidx.annotation.ColorInt");
            content = content.replaceAll("import android.support.annotation.IntRange",
                    "import androidx.annotation.IntRange");
            content = content.replaceAll("import android.support.annotation.DrawableRes",
                    "import androidx.annotation.DrawableRes");
            content = content.replaceAll("import android.support.v4.app.Fragment",
                    "import androidx.fragment.app.Fragment");
            content = content.replaceAll("import android.support.v4.app.FragmentActivity",
                    "import androidx.fragment.app.FragmentActivity");
            content = content.replaceAll("import android.support.v4.app.FragmentManager",
                    "import androidx.fragment.app.FragmentManager");
            content = content.replaceAll("import android.support.v4.widget.DrawerLayout",
                    "import androidx.drawerlayout.widget.DrawerLayout");
            content = content.replaceAll("import android.support.v4.content.ContextCompat",
                    "import androidx.core.content.ContextCompat");
            content = content.replaceAll("com.android.support:appcompat-v7:28.0.0",
                    "androidx.appcompat:appcompat:1.1.0");
            boolean isModify = !(countLength == content.length());
//			System.out.println(srcfile.getPath() + "文件是否修改：" + isModify);
            return FileUtils.writeFile(destPath, content, false);
        }

        @Override
        public boolean isFilter(String pathFull, String pathLite) {
            return false;
        }

        @Override
        public boolean isSignByDefine() {
            return false;
        }

        @Override
        public String doSignByDefine(String pathFull) {
            return null;
        }
    };
}
