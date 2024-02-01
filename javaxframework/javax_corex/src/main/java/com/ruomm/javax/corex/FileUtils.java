/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月29日 下午3:19:34
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FileUtils {
    private final static Log log = LogFactory.getLog(FileUtils.class);


    /**
     * 文件指纹校验值散列算法类
     */
    public interface FileShaHelper {

        /**
         * 文件指纹校验值散列算法编码
         *
         * @param inputStream 待结算指纹校验值的输入流
         * @return 文件指纹校验值(MD5 、 SHA1等散列算法)
         */
        public String encoding(InputStream inputStream);
    }

    private final static int INOUT_BUFF_SIZE = 1024;
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    public final static boolean DEFALUT_COPY_FORCE = true;

    public final static FileShaHelper MD5_SHA_HELPER = new FileShaHelper() {
        @Override
        public String encoding(InputStream inputStream) {
            return DigestByJdkUtils.encodingInputStreamMD5(inputStream);
        }
    };

    /**
     * 检测是否文件
     *
     * @param filePath 文件路径
     * @return 是否文件
     * @paramDefault checkContent 是否检测内容为空，改值为true时候，文件内容为空也返回false
     */
    public static boolean checkFileExists(String filePath) {
        return checkFileExists(filePath, false);
    }

    /**
     * 检测是否文件
     *
     * @param file 文件
     * @return 是否文件
     * @paramDefault checkContent 是否检测内容为空，改值为true时候，文件内容为空也返回false
     */
    public static boolean checkFileExists(File file) {
        return checkFileExists(file, false);
    }

    /**
     * 检测是否文件
     *
     * @param filePath     文件路径
     * @param checkContent 是否检测内容为空，改值为true时候，文件内容为空也返回false
     * @return 是否文件
     */
    public static boolean checkFileExists(String filePath, boolean checkContent) {
        if (null == filePath || filePath.length() <= 0) {
            return false;
        }
        try {
            File file = new File(filePath);
            if (null == file || !file.exists() || !file.isFile()) {
                return false;
            } else {
                if (checkContent) {
                    return file.length() > 0;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Error:checkFileExists", e);
            return false;
        }
    }

    /**
     * 检测是否文件
     *
     * @param file         文件
     * @param checkContent 是否检测内容为空，改值为true时候，文件内容为空也返回false
     * @return 是否文件
     */
    public static boolean checkFileExists(File file, boolean checkContent) {
        try {
            if (null == file || !file.exists() || !file.isFile()) {
                return false;
            } else {
                if (checkContent) {
                    return file.length() > 0;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Error:checkFileExists", e);
            return false;
        }
    }

    /**
     * 检测是否文件夹
     *
     * @param dirPath 文件夹路径
     * @return 是否文件夹
     */
    public static boolean checkDirExists(String dirPath) {
        if (null == dirPath || dirPath.length() <= 0) {
            return false;
        }
        try {
            File dirFile = new File(dirPath);
            if (null == dirFile || !dirFile.exists() || !dirFile.isDirectory()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error("Error:checkDirExists", e);
            return false;
        }
    }

    /**
     * 检测是否文件夹
     *
     * @param dirFile 文件夹
     * @return 是否文件夹
     */
    public static boolean checkDirExists(File dirFile) {
        if (null == dirFile || !dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 读取文件为byte字节数组
     *
     * @param filePath -文件路径
     * @return byte字节数组
     */
    public static byte[] readFileToByte(String filePath) {
        if (null == filePath || filePath.length() < 0) {
            return null;
        }
        byte[] buf = null;
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                buf = readInputStreamToByte(new FileInputStream(file));
            } else {
                buf = null;
            }
        } catch (Exception e) {
            buf = null;
            log.error("Error:readFileToByte", e);
        }
        return buf;
    }

    /**
     * 读取文件为byte字节数组
     *
     * @param file -文件
     * @return byte字节数组
     */
    public static byte[] readFileToByte(File file) {
        byte[] buf = null;
        try {
            if (null != file && file.exists() && file.isFile()) {
                buf = readInputStreamToByte(new FileInputStream(file));
            } else {
                buf = null;
            }
        } catch (Exception e) {
            buf = null;
            log.error("Error:readFileToByte", e);
        }
        return buf;
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param filePath 文件路径
     * @return 文件读取结果字符串
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @DefaultParam lineCtrl 行控制符号，默认为Linux格式的\n换行符
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFileByLine(String filePath) {
        return readFileByLine(filePath, null, null, true);
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @return 文件读取结果字符串
     * @DefaultParam lineCtrl 行控制符号，默认为Linux格式的\n换行符
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFileByLine(String filePath, String charsetName) {
        return readFileByLine(filePath, charsetName, null, true);
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @return 文件读取结果字符串
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFileByLine(String filePath, String charsetName, String lineCtrl) {
        return readFileByLine(filePath, charsetName, lineCtrl, true);
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @param ignoreBom   是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 文件读取结果字符串
     */
    public static String readFileByLine(String filePath, String charsetName, String lineCtrl, boolean ignoreBom) {
        if (null == filePath || filePath.length() < 0) {
            return null;
        }
        String content = null;
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                content = readInputStreamToStringByLine(new FileInputStream(file), charsetName, lineCtrl, ignoreBom);
            } else {
                content = null;
            }
        } catch (Exception e) {
            content = null;
            log.error("Error:readFileByLine", e);
        }
        return content;
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param file 文件
     * @return 文件读取结果字符串
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @DefaultParam lineCtrl 行控制符号，默认为Linux格式的\n换行符
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFileByLine(File file) {
        return readFileByLine(file, null, null, true);
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param file        文件
     * @param charsetName 编码方式，默认UTF-8
     * @return 文件读取结果字符串
     * @DefaultParam lineCtrl 行控制符号，默认为Linux格式的\n换行符
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFileByLine(File file, String charsetName) {
        return readFileByLine(file, charsetName, null, true);
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param file        文件
     * @param charsetName 编码方式，默认UTF-8
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @return 文件读取结果字符串
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFileByLine(File file, String charsetName, String lineCtrl) {
        return readFileByLine(file, charsetName, lineCtrl, true);
    }

    /**
     * 逐行读取文件为字符串
     *
     * @param file        文件
     * @param charsetName 编码方式，默认UTF-8
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @param ignoreBom   是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 文件读取结果字符串
     */
    public static String readFileByLine(File file, String charsetName, String lineCtrl, boolean ignoreBom) {
        String content = null;
        try {
            if (null != file && file.exists() && file.isFile()) {
                content = readInputStreamToStringByLine(new FileInputStream(file), charsetName, lineCtrl, ignoreBom);
            } else {
                content = null;
            }
        } catch (Exception e) {
            content = null;
            log.error("Error:readFileByLine", e);
        }
        return content;
    }

    /**
     * 读取文件为字符串，包含换行符
     *
     * @param filePath 文件路径
     * @return 文件读取结果字符串
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFile(String filePath) {
        return readFile(filePath, null, true);
    }

    /**
     * 读取文件为字符串，包含换行符
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @return 文件读取结果字符串
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFile(String filePath, String charsetName) {
        return readFile(filePath, charsetName, true);
    }

    /**
     * 读取文件为字符串，包含换行符
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @param ignoreBom   是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 文件读取结果字符串
     */
    public static String readFile(String filePath, String charsetName, boolean ignoreBom) {
        if (null == filePath || filePath.length() < 0) {
            return null;
        }
        String content = null;
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                content = readInputStreamToStringByByte(new FileInputStream(file), charsetName, ignoreBom);
            } else {
                content = null;
            }
        } catch (Exception e) {
            content = null;
            log.error("Error:readFile", e);
        }
        return content;
    }

    /**
     * 读取文件为字符串，包含换行符
     *
     * @param file 文件
     * @return 文件读取结果字符串
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFile(File file) {
        return readFile(file, null, true);
    }

    /**
     * 读取文件为字符串，包含换行符
     *
     * @param file        文件
     * @param charsetName 编码方式，默认UTF-8
     * @return 文件读取结果字符串
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static String readFile(File file, String charsetName) {
        return readFile(file, charsetName, true);
    }

    /**
     * 读取文件为字符串，包含换行符
     *
     * @param file        文件
     * @param charsetName 编码方式，默认UTF-8
     * @param ignoreBom   是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 文件读取结果字符串
     */
    public static String readFile(File file, String charsetName, boolean ignoreBom) {
        String content = null;
        try {
            if (null != file && file.exists() && file.isFile()) {
                content = readInputStreamToStringByByte(new FileInputStream(file), charsetName, ignoreBom);
            } else {
                content = null;
            }
        } catch (Exception e) {
            content = null;
            log.error("Error:readFile", e);
        }
        return content;
    }

    /**
     * 读取文件为字符串列表
     *
     * @param filePath 文件路径
     * @return 读取后的字符串列表
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @DefaultParam isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToList(String filePath) {
        return readFileToList(filePath, null, true, true);
    }

    /**
     * 读取文件为字符串列表
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @return 读取后的字符串列表
     * @DefaultParam isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        return readFileToList(filePath, charsetName, true, true);
    }

    /**
     * 读取文件为字符串列表
     *
     * @param filePath       文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @return 读取后的字符串列表
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToList(String filePath, String charsetName, boolean isPutEmptyLine) {
        return readFileToList(filePath, charsetName, isPutEmptyLine, true);
    }

    /**
     * 读取文件为字符串列表
     *
     * @param filePath       文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @param ignoreBom      是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 读取后的字符串列表
     */
    public static List<String> readFileToList(String filePath, String charsetName, boolean isPutEmptyLine, boolean ignoreBom) {
        if (null == filePath || filePath.length() < 0) {
            return null;
        }
        List<String> listContent = null;
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                listContent = readInputStreamToList(new FileInputStream(new File(filePath)), charsetName,
                        isPutEmptyLine, ignoreBom);
            } else {
                listContent = null;
            }
        } catch (Exception e) {
            listContent = null;
            log.error("Error:readFileToList", e);
        }
        return listContent;
    }

    /**
     * 依据开始结束标记，读取文件为字符串列表
     *
     * @param filePath    文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @param tagStart    开始的标记
     * @param tagEnd      结束的标记
     * @return 读取后的字符串列表
     * @DefaultParam isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToListByTag(String filePath, String charsetName, String tagStart, String tagEnd) {
        return readFileToListByTag(filePath, charsetName, tagStart, tagEnd, true, true);
    }

    /**
     * 依据开始结束标记，读取文件为字符串列表
     *
     * @param filePath       文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param tagStart       开始的标记
     * @param tagEnd         结束的标记
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @return 读取后的字符串列表
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToListByTag(String filePath, String charsetName, String tagStart, String tagEnd, boolean isPutEmptyLine) {
        return readFileToListByTag(filePath, charsetName, tagStart, tagEnd, isPutEmptyLine, true);
    }

    /**
     * 依据开始结束标记，读取文件为字符串列表
     *
     * @param filePath       文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param tagStart       开始的标记
     * @param tagEnd         结束的标记
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @param ignoreBom      是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 读取后的字符串列表
     */
    public static List<String> readFileToListByTag(String filePath, String charsetName, String tagStart, String tagEnd, boolean isPutEmptyLine, boolean ignoreBom) {
        if (null == filePath || filePath.length() < 0) {
            return null;
        }
        List<String> listContent = null;
        try {
            File file = new File(filePath);
            if (null != file && file.exists() && file.isFile()) {
                listContent = readInputStreamToListByTag(new FileInputStream(new File(filePath)), charsetName, tagStart,
                        tagEnd, isPutEmptyLine, ignoreBom);
            } else {
                listContent = null;
            }
        } catch (Exception e) {
            listContent = null;
            log.error("Error:readFileToList", e);
        }
        return listContent;
    }

    /**
     * 读取文件为字符串列表
     *
     * @param file 文件路径
     * @return 读取后的字符串列表
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @DefaultParam isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToList(File file) {
        return readFileToList(file, null, true, true);
    }

    /**
     * 读取文件为字符串列表
     *
     * @param file        文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @return 读取后的字符串列表
     * @DefaultParam isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToList(File file, String charsetName) {
        return readFileToList(file, charsetName, true, true);
    }

    /**
     * 读取文件为字符串列表
     *
     * @param file           文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @return 读取后的字符串列表
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToList(File file, String charsetName, boolean isPutEmptyLine) {
        return readFileToList(file, charsetName, isPutEmptyLine, true);
    }

    /**
     * 读取文件为字符串列表
     *
     * @param file           文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @param ignoreBom      是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 读取后的字符串列表
     */
    public static List<String> readFileToList(File file, String charsetName, boolean isPutEmptyLine, boolean ignoreBom) {
        if (null == file) {
            return null;
        }
        List<String> listContent = null;
        try {
            if (null != file && file.exists() && file.isFile()) {
                listContent = readInputStreamToList(new FileInputStream(file), charsetName,
                        isPutEmptyLine, ignoreBom);
            } else {
                listContent = null;
            }
        } catch (Exception e) {
            listContent = null;
            log.error("Error:readFileToList", e);
        }
        return listContent;
    }

    /**
     * 依据开始结束标记，读取文件为字符串列表
     *
     * @param file        文件路径
     * @param charsetName 编码方式，默认UTF-8
     * @param tagStart    开始的标记
     * @param tagEnd      结束的标记
     * @return 读取后的字符串列表
     * @DefaultParam isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToListByTag(File file, String charsetName, String tagStart, String tagEnd) {
        return readFileToListByTag(file, charsetName, tagStart, tagEnd, true, true);
    }

    /**
     * 依据开始结束标记，读取文件为字符串列表
     *
     * @param file           文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param tagStart       开始的标记
     * @param tagEnd         结束的标记
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @return 读取后的字符串列表
     * @DefaultParam ignoreBom 是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     */
    public static List<String> readFileToListByTag(File file, String charsetName, String tagStart, String tagEnd, boolean isPutEmptyLine) {
        return readFileToListByTag(file, charsetName, tagStart, tagEnd, isPutEmptyLine, true);
    }

    /**
     * 依据开始结束标记，读取文件为字符串列表
     *
     * @param file           文件路径
     * @param charsetName    编码方式，默认UTF-8
     * @param tagStart       开始的标记
     * @param tagEnd         结束的标记
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @param ignoreBom      是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 读取后的字符串列表
     */
    public static List<String> readFileToListByTag(File file, String charsetName, String tagStart, String tagEnd, boolean isPutEmptyLine, boolean ignoreBom) {
        if (null == file) {
            return null;
        }
        List<String> listContent = null;
        try {
            if (null != file && file.exists() && file.isFile()) {
                listContent = readInputStreamToListByTag(new FileInputStream(file), charsetName, tagStart,
                        tagEnd, isPutEmptyLine, ignoreBom);
            } else {
                listContent = null;
            }
        } catch (Exception e) {
            listContent = null;
            log.error("Error:readFileToList", e);
        }
        return listContent;
    }

    /**
     * 读取输入流为byte字节数组
     *
     * @param is 输入流
     * @return 读取输入流为byte字节数组结果
     * @DefaultParam isBuffer 是否使用Buffer模式，默认true
     */
    public static byte[] readInputStreamToByte(InputStream is) {
        return readInputStreamToByte(is, true);
    }

    /**
     * 读取输入流为byte字节数组
     *
     * @param is       输入流
     * @param isBuffer 是否使用Buffer模式，默认true
     * @return 读取输入流为byte字节数组结果
     */
    public static byte[] readInputStreamToByte(InputStream is, boolean isBuffer) {
        if (null == is) {
            return null;
        }
        if (isBuffer) {
            byte[] resultByte = null;
            BufferedInputStream in = null;
            ByteArrayOutputStream bos = null;
            try {
                in = new BufferedInputStream(is);
                bos = new ByteArrayOutputStream(is.available());
                byte[] buffer = new byte[INOUT_BUFF_SIZE];
                int len = 0;
                while (-1 != (len = in.read(buffer, 0, INOUT_BUFF_SIZE))) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                resultByte = bos.toByteArray();
            } catch (Exception e) {
                log.error("Error:readInputStreamToByte", e);
            } finally {
                try {
                    if (null != in) {
                        in.close();
                    }
                } catch (IOException e) {
                    log.error("Error:readInputStreamToByte", e);
                }
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.error("Error:readInputStreamToByte", e);
                }
                try {
                    if (null != bos) {
                        bos.close();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:readInputStreamToByte", e);
                }
            }
            return resultByte;
        } else {
            byte[] resultByte = null;
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream(is.available());
                byte[] buffer = new byte[INOUT_BUFF_SIZE];
                int len = 0;
                while (-1 != (len = is.read(buffer, 0, INOUT_BUFF_SIZE))) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                resultByte = bos.toByteArray();
            } catch (Exception e) {
                log.error("Error:readInputStreamToByte", e);
            } finally {
                try {
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.error("Error:readInputStreamToByte", e);
                }
                try {
                    if (null != bos) {
                        bos.close();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:readInputStreamToByte", e);
                }

            }
            return resultByte;
        }
    }

    /**
     * 逐行方式读取输入流读取结果字符串
     *
     * @param is          输入流
     * @param charsetName 编码方式，默认UTF-8
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @param ignoreBom   是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 输入流读取结果字符串
     */
    public static String readInputStreamToStringByLine(InputStream is, String charsetName, String lineCtrl, boolean ignoreBom) {
        if (null == is) {
            return null;
        }
        String lineCtrlStr = null == lineCtrl || lineCtrl.length() <= 0 ? "\n" : lineCtrl;
        BufferedReader bufreader = null;
        String content = null;
        try {
            boolean isFirst = true;
            StringBuilder fileContent = new StringBuilder();
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            bufreader = new BufferedReader(new InputStreamReader(is, charset));
            String lineStr = null;
            while ((lineStr = bufreader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    String lineStrReal = null;
                    if (ignoreBom && isCharSetUTF8(charset)) {
                        lineStrReal = StringUtils.stringToNoBom(lineStr);
                    } else {
                        lineStrReal = lineStr;
                    }
                    if (null != lineStrReal && lineStrReal.length() > 0) {
                        fileContent.append(lineStrReal);
                    }
                } else {
                    fileContent.append(lineCtrlStr).append(lineStr);
                }

            }
            content = new String(fileContent);
        } catch (Exception e) {
            log.error("Error:readInputStreamToString", e);
        } finally {
            if (bufreader != null) {
                try {
                    bufreader.close();
                } catch (Exception e) {
                    log.error("Error:readInputStreamToString", e);
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.error("Error:readInputStreamToString", e);
                }
            }

        }
        return content;

    }

    /**
     * 字节流模式读取输入流读取结果字符串
     *
     * @param is          输入流
     * @param charsetName 编码方式，默认UTF-8
     * @param ignoreBom   是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 输入流读取结果字符串
     */
    public static String readInputStreamToStringByByte(InputStream is, String charsetName, boolean ignoreBom) {
        byte[] result = readInputStreamToByte(is, true);
        if (null == result) {
            return null;
        }
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            String resultStr = new String(result, charset);
            if (ignoreBom && isCharSetUTF8(charset)) {
                return StringUtils.stringToNoBom(resultStr);
            } else {
                return resultStr;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:readInputStreamToStringNew", e);
            return null;
        }

    }

    /**
     * 读取文输入流为字符串列表
     *
     * @param is             输入流
     * @param charsetName    编码方式，默认UTF-8
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @param ignoreBom      是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 读取后的字符串列表
     */
    public static List<String> readInputStreamToList(InputStream is, String charsetName, boolean isPutEmptyLine, boolean ignoreBom) {
        if (null == is) {
            return null;
        }
        BufferedReader bufreader = null;
        List<String> realContent = null;
        try {
            List<String> fileContent = new ArrayList<String>();
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            bufreader = new BufferedReader(new InputStreamReader(is, charset));
            String lineStr = null;
            boolean isFirst = true;
            while ((lineStr = bufreader.readLine()) != null) {
                String tmpLineStr = null;
                if (isFirst) {
                    isFirst = false;
                    if (ignoreBom && isCharSetUTF8(charset)) {
                        tmpLineStr = StringUtils.stringToNoBom(lineStr);
                    } else {
                        tmpLineStr = lineStr;
                    }
                } else {
                    tmpLineStr = lineStr;
                }
                if (isPutEmptyLine) {
                    fileContent.add(tmpLineStr);
                } else {
                    if (null != tmpLineStr && tmpLineStr.length() > 0) {
                        fileContent.add(tmpLineStr);
                    }
                }

            }
            realContent = fileContent;

        } catch (Exception e) {
            log.error("Error:readInputStreamToList", e);
        } finally {
            if (null != bufreader) {
                try {
                    bufreader.close();
                } catch (Exception e) {
                    log.error("Error:readInputStreamToList", e);
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.error("Error:readInputStreamToList", e);
                }
            }

        }
        return realContent;
    }

    /**
     * 依据开始结束标记，读取输入流为字符串列表
     *
     * @param is             输入流
     * @param charsetName    编码方式，默认UTF-8
     * @param tagStart       开始的标记
     * @param tagEnd         结束的标记
     * @param isPutEmptyLine 是否放入空，默认true。如为true，则空行也会放入读取的列表中。
     * @param ignoreBom      是否忽略BOM，默认true。ignoreBom忽略BOM值true时候，UTF-8读取文件为字符串则忽略前面的BOM。
     * @return 读取后的字符串列表
     */
    public static List<String> readInputStreamToListByTag(InputStream is, String charsetName, String tagStart, String tagEnd,
                                                          boolean isPutEmptyLine, boolean ignoreBom) {
        if (null == is) {
            return null;
        }
        BufferedReader bufreader = null;
        List<String> realContent = null;

        try {

            List<String> fileContent = new ArrayList<String>();
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            bufreader = new BufferedReader(new InputStreamReader(is, charset));
            boolean isStart = StringUtils.isEmpty(tagStart);
            String lineStr = null;
            boolean isFirst = true;
            while ((lineStr = bufreader.readLine()) != null) {
                String tmpLineStr = null;
                if (isFirst) {
                    isFirst = false;
                    if (ignoreBom && isCharSetUTF8(charset)) {
                        tmpLineStr = StringUtils.stringToNoBom(lineStr);
                    } else {
                        tmpLineStr = lineStr;
                    }
                } else {
                    tmpLineStr = lineStr;
                }
                if (!isStart) {
                    if (StringUtils.isNotEmpty(tmpLineStr) && tagStart.equalsIgnoreCase(tmpLineStr)) {
                        isStart = true;
                    }
                }
                if (!isStart) {
                    continue;
                }
                if (StringUtils.isNotEmpty(tagEnd) && StringUtils.isNotEmpty(tmpLineStr) && tagEnd.equalsIgnoreCase(tmpLineStr)) {
                    break;
                }
                if (isPutEmptyLine) {
                    fileContent.add(tmpLineStr);
                } else {
                    if (null != tmpLineStr && tmpLineStr.length() > 0) {
                        fileContent.add(tmpLineStr);
                    }
                }
            }
            realContent = fileContent;

        } catch (Exception e) {
            log.error("Error:readInputStreamToList", e);
        } finally {
            if (null != bufreader) {
                try {
                    bufreader.close();
                } catch (Exception e) {
                    log.error("Error:readInputStreamToList", e);
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.error("Error:readInputStreamToList", e);
                }
            }

        }
        return realContent;
    }

    /**
     * 把字符串内容写入文件，使用FileWriter方法，不可以指定编码
     *
     * @param filePath 文件路径
     * @param content  内容
     * @param append   是否追加写入。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFileByFileWriter(String filePath, String content, boolean append) {
        return writeFileByFileWriter(createFile(filePath), content, append);
    }


    /**
     * 把字符串内容写入文件，使用FileWriter方法，不可以指定编码
     *
     * @param file    文件
     * @param content 内容
     * @param append  是否追加写入。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFileByFileWriter(File file, String content, boolean append) {
        if (null == file || file.isDirectory()) {
            return false;
        }
        FileWriter fileWriter = null;
        boolean flag = false;
        try {
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            flag = true;

        } catch (Exception e) {
            log.error("Error:writeFile", e);
        } finally {
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (Exception e) {
                    log.error("Error:writeFile", e);
                }
            }
        }
        return flag;
    }

    /**
     * 把字符串内容写入文件，使用OutputStreamWriter方法，可以指定编码
     *
     * @param filePath 文件路径
     * @param content  内容
     * @param append   是否追加写入。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @return 返回true
     * @paramDefault charsetName 编码方式，默认UTF-8
     * @Throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        return writeFile(createFile(filePath), content, append, null);
    }

    /**
     * 把字符串内容写入文件，使用OutputStreamWriter方法，可以指定编码
     *
     * @param file    文件
     * @param content 内容
     * @param append  是否追加写入。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @return 返回true
     * @paramDefault charsetName 编码方式，默认UTF-8
     * @Throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(File file, String content, boolean append) {
        return writeFile(file, content, append, null);
    }

    /**
     * 把字符串内容写入文件，使用OutputStreamWriter方法，可以指定编码
     *
     * @param filePath    文件路径
     * @param content     内容
     * @param append      是否追加写入。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @param charsetName 编码方式，默认UTF-8
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append, String charsetName) {
        return writeFile(createFile(filePath), content, append, charsetName);
    }

    /**
     * 把字符串内容写入文件，使用OutputStreamWriter方法，可以指定编码
     *
     * @param file        文件
     * @param content     内容
     * @param append      是否追加写入。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @param charsetName 编码方式，默认UTF-8
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(File file, String content, boolean append, String charsetName) {
        if (null == file || file.isDirectory()) {
            return false;
        }
        BufferedWriter outWriter = null;
        boolean flag = false;
        try {
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
            outWriter.write(content);
            outWriter.flush();
            flag = true;
        } catch (Exception e) {
            log.error("Error:writeFile", e);
        } finally {
            if (null != outWriter) {
                try {
                    outWriter.close();
                } catch (Exception e) {
                    log.error("Error:writeFile", e);
                }
            }
        }
        return flag;
    }

    /**
     * 把输入流写入文件
     *
     * @param filePath 要打开用于写入的文件路径。
     * @param stream   输入流
     * @return 返回true
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(createFile(filePath), stream, false);
    }

    /**
     * 把输入流写入文件
     *
     * @param filePath 要打开用于写入的文件路径。
     * @param stream   输入流
     * @param append   是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(createFile(filePath), stream, append);
    }


    /**
     * 把输入流写入文件
     *
     * @param file   要打开用于写入的文件。
     * @param stream 输入流
     * @return 返回true
     * @DefaultParam append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * 把输入流写入文件
     *
     * @param file   要打开用于写入的文件。
     * @param stream 输入流
     * @param append 是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        if (null == file || file.isDirectory()) {
            return false;
        }
        BufferedOutputStream outStream = null;
        boolean flag = false;
        try {
            outStream = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                outStream.write(data, 0, length);
            }
            outStream.flush();
            flag = true;
        } catch (Exception e) {
            log.error("Error:writeFile", e);
        } finally {
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (Exception e) {
                    log.error("Error:writeFile", e);
                }
            }
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:writeFile", e);
                }
            }
        }
        return flag;
    }

    /**
     * 把字符串列表写入文件
     *
     * @param filePath    要打开用于写入的文件路径
     * @param listContent 内容列表
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @return 返回true
     * @DefaultParam append  是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, List<String> listContent, String lineCtrl) {
        return writeFile(filePath, listContent, lineCtrl, false, "UTF-8");
    }

    /**
     * 把字符串列表写入文件
     *
     * @param file        要打开用于写入的文件
     * @param listContent 内容列表
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @return 返回true
     * @DefaultParam append  是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @DefaultParam charsetName 编码方式，默认UTF-8
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, List<String> listContent, String lineCtrl) {
        return writeFile(file, listContent, lineCtrl, false, "UTF-8");
    }

    /**
     * 把字符串列表写入文件
     *
     * @param filePath    要打开用于写入的文件路径
     * @param listContent 内容列表
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @param append      是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @param charsetName 编码方式，默认UTF-8
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, List<String> listContent, String lineCtrl, boolean append,
                                    String charsetName) {
        if (null == filePath || filePath.length() <= 0 || null == listContent || listContent.size() <= 0) {
            return false;
        }
        String newLineCtrl = null == lineCtrl || lineCtrl.length() <= 0 ? "\n" : lineCtrl;
        StringBuilder sb = new StringBuilder();
        //逐行写入，不跳过任何行
        for (int i = 0; i < listContent.size(); i++) {
            String tmp = listContent.get(i);
            if (i > 0) {
                sb.append(newLineCtrl);
            }
            sb.append(null == tmp ? "" : tmp);
        }
        //跳过为空的开始行数
//		for (String tmp : listContent) {
//			if (sb.length() > 0) {
//				sb.append(newLine);
//			}
//			sb.append(null == tmp ? "" : tmp);
//		}
        return writeFile(filePath, sb.toString(), append, charsetName);
    }

    /**
     * 把字符串列表写入文件
     *
     * @param file        要打开用于写入的文件
     * @param listContent 内容列表
     * @param lineCtrl    行控制符号，默认为Linux格式的\n换行符
     * @param append      是否追加写入，默认为false。此值为真则则写入内容追加到尾部，否则写入内容覆盖原来内容。
     * @param charsetName 编码方式，默认UTF-8
     * @return 返回true
     * @Throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, List<String> listContent, String lineCtrl, boolean append,
                                    String charsetName) {
        if (null == file || file.isDirectory() || null == listContent || listContent.size() <= 0) {
            return false;
        }
        String newLineCtrl = null == lineCtrl || lineCtrl.length() <= 0 ? "\n" : lineCtrl;
        StringBuilder sb = new StringBuilder();
        //逐行写入，不跳过任何行
        for (int i = 0; i < listContent.size(); i++) {
            String tmp = listContent.get(i);
            if (i > 0) {
                sb.append(newLineCtrl);
            }
            sb.append(null == tmp ? "" : tmp);
        }
        //跳过为空的开始行数
//		for (String tmp : listContent) {
//			if (sb.length() > 0) {
//				sb.append(newLine);
//			}
//			sb.append(null == tmp ? "" : tmp);
//		}
        return writeFile(file, sb.toString(), append, charsetName);
    }

    /**
     * 创建此文件的文件名结尾，包括创建该目录所需的完整目录路径的目录。 <br/>
     * <ul>
     * <strong>注意事项：</strong>
     * <li>makeDirs("C:\\Users\\Tools") 只能创建的用户文件夹</li>
     * <li>makeFolder("C:\\Users\\Tools\\") 可以创建Tools文件夹</li>
     * </ul>
     *
     * @param filePath 文件路径 -
     * @return 如果必要的目录已创建或目标目录已经存在，目录假的不能创建。
     * <ul>
     * <li>如果getFolderName（字符串）返回null，返回false</li>
     * <li>如果目标目录已经存在，则返回true</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        try {
            if (null == filePath || filePath.length() <= 0) {
                return false;
            }
            String folderName = getFolderName(filePath);
            if (StringUtils.isEmpty(folderName)) {
                return true;
            }

            File folder = new File(folderName);
            if (folder.exists()) {
                return folder.isDirectory();
            } else {
                return folder.mkdirs();
            }
        } catch (Exception e) {
            log.error("Error:makeDirs", e);
            return false;
        }
    }

    /**
     * 从路径获取文件夹名称
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath 文件路径 -
     * @return
     */
    public static String getFolderName(String filePath) {

        return getFolderNameWithSeparator(filePath, false);
    }

    /**
     * 从路径获取文件夹名称
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath        文件路径 -
     * @param isWithSeparator 是否拼接路径分格符号
     * @return
     */
    public static String getFolderNameWithSeparator(String filePath, boolean isWithSeparator) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = lastIndexOfFileSeparator(filePath);
        if (isWithSeparator) {
            return filePosi == -1 ? "" : filePath.substring(0, filePosi + 1);
        } else {
            return filePosi == -1 ? "" : filePath.substring(0, filePosi);
        }
    }

    /**
     * 从路径获取文件名，包括后缀
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath 文件路径 -
     * @return 从路径文件名，包括后缀
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = lastIndexOfFileSeparator(filePath);
        return filePosi < 0 ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 从路径获取文件名，不包括后缀
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath 文件路径 -
     * @return 从路径文件名，不包括后缀
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = lastIndexOfFileSeparator(filePath);
        if (filePosi == -1) {
            return extenPosi < 0 ? filePath : filePath.substring(0, extenPosi);
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1);
    }

    /**
     * 获取文件的后缀从路径
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
//		if (filePath.trim().length() <= 0) {
//			return "";
//		}
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = lastIndexOfFileSeparator(filePath);
        if (extenPosi == -1) {
            return "";
        }
        return filePosi >= extenPosi ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * 创建文件夹目录路径
     *
     * @param dirPath 文件夹目录路径
     * @return 文件夹目录文件
     */
    public static File createDir(String dirPath) {
        try {
            if (StringUtils.isEmpty(dirPath)) {
                return null;
            }
            File file = new File(dirPath);
            if (file.exists() && file.isDirectory()) {
                return file;
            } else if (file.exists()) {
                return null;
            } else {
                if (file.mkdirs()) {
                    return file;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:createDir", e);
            return null;
        }

    }

    /**
     * 目录路径和文件名转换为完整文件路径
     *
     * @param path     目录路径
     * @param fileName 文件名称
     * @return 完整文件路径
     */
    public static String transToFileName(String path, String fileName) {
        if (StringUtils.isEmpty(path)) {
            if (StringUtils.isEmpty(fileName)) {
                return null;
            } else {
                return fileName;
            }
        }
        if (isEndWithFileSeparator(path)) {
            return path + fileName;
        } else {
            return path + parseFileSeparator(path) + fileName;
        }
    }

    /**
     * 拼接2个目录或1个目录1个文件为完整文件路径
     *
     * @param path     目录路径
     * @param fileName 文件名称或目录路径
     * @return 完整文件路径
     */
    public static String concatFilePath(String path, String fileName) {
        return concatFilePath(path, fileName, false);
    }

    /**
     * 拼接2个目录或1个目录1个文件为完整文件路径
     *
     * @param path            目录路径
     * @param fileName        文件名称或目录路径
     * @param separatorRevise 是否修订名称中的文件分隔路径
     * @return 完整文件路径
     */
    public static String concatFilePath(String path, String fileName, boolean separatorRevise) {
        if (StringUtils.isEmpty(path) && StringUtils.isEmpty(fileName)) {
            return null;
        } else if (StringUtils.isEmpty(path)) {
            return fileName;
        } else if (StringUtils.isEmpty(fileName)) {
            return path;
        } else {
            String realFileName;
            if (StringUtils.isEmpty(fileName)) {
                realFileName = "";
            } else if (isStartWithFileSeparator(fileName)) {
                realFileName = fileName.substring(1);
            } else {
                realFileName = fileName;
            }
            if (StringUtils.isEmpty(realFileName)) {
                return path;
            }
            String tmpFileSeparator = parseFileSeparator(path);
            if (separatorRevise) {
                realFileName = realFileName.replace("\\", tmpFileSeparator).replace("/", tmpFileSeparator);
            }
            if (isEndWithFileSeparator(path)) {
                return path + realFileName;
            } else {
                return path + parseFileSeparator(path) + realFileName;
            }
        }

    }

    /**
     * 拼接2个目录或1个目录1个文件为完整目录路径
     *
     * @param path    目录路径
     * @param subPath 次级目录路径
     * @return 完整文件路径
     */
    public static String concatDirPath(String path, String subPath) {
        return concatDirPath(path, subPath, false);
    }

    /**
     * 拼接2个目录或1个目录1个文件为完整目录路径
     *
     * @param path            目录路径
     * @param subPath         次级目录路径
     * @param separatorRevise 是否修订名称中的文件分隔路径
     * @return 完整文件路径
     */
    public static String concatDirPath(String path, String subPath, boolean separatorRevise) {
        if (StringUtils.isEmpty(path) && StringUtils.isEmpty(subPath)) {
            return null;
        } else if (StringUtils.isEmpty(path)) {
            return subPath;
        } else if (StringUtils.isEmpty(subPath)) {
            return path;
        } else {

            String realSubPath;
            if (StringUtils.isEmpty(subPath)) {
                realSubPath = "";
            } else if (isStartWithFileSeparator(subPath)) {
                realSubPath = subPath.substring(1);
            } else {
                realSubPath = subPath;
            }
            String tmpFileSeparator = parseFileSeparator(path);
            if (StringUtils.isEmpty(realSubPath)) {
                if (isEndWithFileSeparator(path)) {
                    return path;
                } else {
                    return path + tmpFileSeparator;
                }
            }
            if (separatorRevise) {
                realSubPath = realSubPath.replace("\\", tmpFileSeparator).replace("/", tmpFileSeparator);
            }
            String resultPath;
            if (isEndWithFileSeparator(path)) {
                resultPath = path + realSubPath;
            } else {
                resultPath = path + tmpFileSeparator + realSubPath;
            }
            if (isEndWithFileSeparator(resultPath)) {
                return resultPath;
            } else {
                return resultPath + tmpFileSeparator;
            }

        }

    }

    /**
     * 创建文件，若是路径缺失自动创建文件夹
     *
     * @param filepath 文件路径
     * @return 文件
     */
    public static File createFile(String filepath) {
        try {
            if (StringUtils.isEmpty(filepath)) {
                return null;
            }
            if (isEndWithFileSeparator(filepath)) {
                return createDir(filepath);
            }
            int index = lastIndexOfFileSeparator(filepath);
            if (index < 0) {
                return new File(filepath);
            } else if (index == 0) {
                return new File(filepath);
            } else if (index == filepath.length() - 1) {
                return createDir(filepath);
            } else {
                String dirpath = filepath.substring(0, index);
                //	String filename = filepath.substring(index + 1, filepath.length());
                File filedir = createDir(dirpath);
                if (null == filedir) {
                    return null;
                } else {
                    return new File(filepath);
                    //	return new File(filedir.getPath() + FILE_FOLER_SEPARATOR + filename);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:createFile", e);
            return null;
        }
    }

    /**
     * 创建文件，若是路径缺失自动创建文件夹
     *
     * @param path     文件夹目录路径
     * @param fileName 文件名称
     * @return 文件
     */
    public static File createFile(String path, String fileName) {
        if (null == path || path.length() <= 0 || null == fileName || fileName.length() <= 0) {
            return null;
        }
        try {
//			String dirPath = isEndWithFileSeparator(path) ? path : path + File.separator;
            String dirPath = isEndWithFileSeparator(path) ? path : path + parseFileSeparator(path);
            File dirFile = createDir(dirPath);
            if (null == dirFile) {
                return null;
            }
            return new File(dirPath + fileName);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:createFile", e);
            return null;
        }
    }

    /**
     * 删除文件-不是文件不删除
     *
     * @param filePath 文件路径
     * @return null返回false, 不存在返回true，不是文件返回false，文件返回删除结果
     */
    public static boolean delFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        try {
            return delFile(new File(filePath));
        } catch (Exception e) {
            log.error("Error:delFile", e);
            return false;
        }
    }

    /**
     * 删除文件-不是文件不删除
     *
     * @param file 文件
     * @return null返回false, 不存在返回true，不是文件返回false，文件返回删除结果
     */
    public static boolean delFile(File file) {
        try {
            if (null == file) {
                return false;
            } else if (!file.exists()) {
                return true;
            } else if (!file.isFile()) {
                return false;
            } else {
                boolean result = file.delete();
                return result;
            }
        } catch (Exception e) {
            log.error("Error:delFile", e);
            return false;
        }
    }

    /**
     * 删除文件夹中所有文件，若是文件直接删除，文件夹则清空所有文件
     *
     * @param path      文件夹或文件路径
     * @param isRootDel 是否删除文件夹根目录
     * @return 删除结果boolean值
     */
    public static boolean delAllFiles(String path, boolean isRootDel) {
        boolean result = delAllFiles(path);
        if (!result) {
            return result;
        }
        File rootFolder = new File(path);
        if (isRootDel && null != rootFolder && rootFolder.exists() && rootFolder.isDirectory()) {
            return rootFolder.delete();
        } else {
            return result;
        }
    }

    /**
     * 删除文件夹中所有文件，若是文件直接删除，文件夹则清空所有文件
     *
     * @param file      文件夹或文件
     * @param isRootDel 是否删除文件夹根目录
     * @return 删除结果boolean值
     */
    public static boolean delAllFiles(File file, boolean isRootDel) {
        boolean result = delAllFiles(file);
        if (!result) {
            return result;
        }
        if (isRootDel && null != file && file.exists() && file.isDirectory()) {
            return file.delete();
        } else {
            return result;
        }
    }

    /**
     * 删除文件夹中所有文件，若是文件直接删除，文件夹则保留根目录清空所有文件
     *
     * @param path 文件夹或文件路径
     * @return 删除结果boolean值
     */
    public static boolean delAllFiles(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (null == file) {
            return false;
        } else if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        } else if (!file.isDirectory()) {
            return false;
        }

        String[] tempList = file.list();
        if (null == tempList || tempList.length < 1) {
            tempList = null;
            return true;
        }
        boolean flag = true;
        for (int i = 0; i < tempList.length; i++) {
            File tempFile = null;
            String tempPath = null;
            if (isEndWithFileSeparator(path)) {
                tempPath = path + tempList[i];
            } else {
                tempPath = path + parseFileSeparator(path) + tempList[i];
            }
            tempFile = new File(tempPath);
            if (tempFile.isFile()) {
                boolean result = tempFile.delete();
                if (!result) {
                    flag = false;
                }
            } else if (tempFile.isDirectory()) {
//				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
//				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                boolean result = delAllFiles(tempPath);// 先删除文件夹里面的文件
                if (!result) {
                    flag = false;
                }
                boolean resultFloder = delFolder(tempPath);// 再删除空文件夹
                if (!resultFloder) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 删除文件夹中所有文件，若是文件直接删除，文件夹则保留根目录清空所有文件
     *
     * @param file 文件夹或文件
     * @return 删除结果boolean值
     */
    public static boolean delAllFiles(File file) {
        if (null == file) {
            return false;
        } else if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        } else if (!file.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] tempList = file.listFiles();
        if (null == tempList || tempList.length < 1) {
            tempList = null;
            return true;
        }
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            temp = tempList[i];
            if (temp.isFile()) {
                boolean result = temp.delete();
                if (!result) {
                    flag = false;
                }

            } else if (temp.isDirectory()) {
                boolean result = delAllFiles(temp);// 先删除文件夹里面的文件
                if (!result) {
                    flag = false;
                }
                boolean resultFloder = delFolder(temp);// 再删除空文件夹
                if (!resultFloder) {
                    flag = false;
                }
            } else {
                boolean result = temp.delete();
                if (!result) {
                    flag = false;
                }
            }
        }
        temp = null;
        return flag;
    }

    /**
     * 删除文件夹中所有文件，若是文件直接删除，文件夹则清空所有文件后删除
     *
     * @param folderPath 文件夹或文件路径
     * @return 删除结果boolean值
     */
    public static boolean delFolder(String folderPath) {
        try {
            delAllFiles(folderPath); // 删除完里面所有内容
            File myFilePath = new File(folderPath);
            boolean result; // 删除空文件夹
            if (myFilePath.exists()) {
                result = myFilePath.delete(); // 删除空文件夹
            } else {
                result = true;
            }
            return result;
        } catch (Exception e) {
            log.error("Error:delFolder", e);
            return false;
        }
    }

    /**
     * 删除文件夹中所有文件，若是文件直接删除，文件夹则清空所有文件后删除
     *
     * @param folder 文件夹或文件
     * @return 删除结果boolean值
     */
    public static boolean delFolder(File folder) {
        try {
            delAllFiles(folder); // 删除完里面所有内容
            boolean result; // 删除空文件夹
            if (folder.exists()) {
                result = folder.delete(); // 删除空文件夹
            } else {
                result = true;
            }
            return result;

        } catch (Exception e) {
            log.error("Error:delFolder", e);
            return false;
        }
    }

    /**
     * 获取相对路径
     *
     * @param baseDirPath 根路径，
     * @param absFilePath 文件路径
     * @return 相对路径。根路径为空，返回文件路径。根路径不为空，如文件路径以根路径为开始，则返回相对路径，否则返回NULL。
     */
    public static String getRelativePath(String baseDirPath, String absFilePath) {
        String dirPath;
        String filePath;
        try {
            dirPath = null == baseDirPath || baseDirPath.length() <= 0 ? null : new File(baseDirPath).getPath();
            filePath = null == absFilePath || absFilePath.length() <= 0 ? null : new File(absFilePath).getPath();
        } catch (Exception e) {
            log.error("Error:getRelativePath", e);
            return null;
        }
        if (null == filePath || filePath.length() <= 0) {
            return null;
        }
        int dirPathSize = null == dirPath ? 0 : dirPath.length();
        if (dirPathSize <= 0) {
            return filePath;
        }
        int filePathSize = filePath.length();
        if (filePathSize < dirPathSize) {
            return null;
        }
        if (filePathSize == dirPathSize) {
            if (filePath.equalsIgnoreCase(dirPath)) {
                return filePath;
            } else {
                return null;
            }
        }
        if (filePath.substring(0, dirPathSize).equalsIgnoreCase(dirPath)) {
            return filePath.substring(dirPathSize, filePathSize);
        } else {
            return null;
        }
    }

    /**
     * 计算文件或者文件夹的大小 ，单位 Bytes
     *
     * @param filePath 要计算的文件或者文件夹路径，类型：java.lang.String
     * @return 大小，单位：Bytes
     */
    public static long getSizeBytes(String filePath) {
        if (null == filePath || filePath.length() <= 0) {
            return 0;
        }
        return getSizeBytes(new File(filePath));
    }


    /**
     * 计算文件或者文件夹的大小 ，单位 Bytes
     *
     * @param file 要计算的文件或者文件夹，类型：java.io.File
     * @return 大小，单位：Bytes
     */
    public static long getSizeBytes(File file) {

        // 判断文件是否存在

        if (null != file && file.exists()) {
            // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                // 获取文件大小
                File[] fl = file.listFiles();
                long ss = 0;
                for (File f : fl) {
                    ss += getSizeBytes(f);
                }
                return ss;
            } else {
                long ss = file.length();
                return ss;
            }
        } else {
            log.debug("FileUtils.getSizeBytes->文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }

    /**
     * 计算文件或者文件夹的大小 ，单位 MB
     *
     * @param filePath 要计算的文件或者文件夹路径，类型：java.lang.String
     * @return 大小，单位：MB
     */
    public static double getSizeMB(String filePath) {
        if (null == filePath || filePath.length() <= 0) {
            return 0;
        }
        return getSizeMB(new File(filePath));
    }

    /**
     * 计算文件或者文件夹的大小 ，单位 MB
     *
     * @param file 要计算的文件或者文件夹，类型：java.io.File
     * @return 大小，单位：MB
     */
    public static double getSizeMB(File file) {

        // 判断文件是否存在

        if (null != file && file.exists()) {
            // 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                // 获取文件大小
                File[] fl = file.listFiles();
                double ss = 0;
                for (File f : fl) {
                    ss += getSizeMB(f);
                }
                return ss;
            } else {
                double ss = (double) file.length() / 1024 / 1024;
                return ss;
            }
        } else {
            log.debug("FileUtils.getSizeMB->文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    /**
     * 依据UUID生成文件名称
     *
     * @param filehead 文件头
     * @param filetype 文件结尾
     * @return 生成的文件名称
     */
    public static String getFilaNameByUUID(String filehead, String filetype) {
        String string = StringUtils.nullStrToEmpty(filehead) + UUID.randomUUID().toString() + StringUtils.nullStrToEmpty(filetype);
        return string;
    }

    /**
     * 依据时间生成文件名称
     *
     * @param filehead 文件头
     * @param filetype 文件结尾
     * @return 生成的文件名称
     * @DefaultParam outTimeFormat SimpleDateFormat的格式，默认yyyyMMdd_HHmmss格式
     */
    public static String getFileNameByTime(String filehead, String filetype) {
        return getFileNameByTime(filehead, filetype, null);

    }

    /**
     * 依据时间生成文件名称
     *
     * @param filehead      文件头
     * @param filetype      文件结尾
     * @param outTimeFormat SimpleDateFormat的格式，默认yyyyMMdd_HHmmss格式
     * @return 生成的文件名称
     */
    public static String getFileNameByTime(String filehead, String filetype, String outTimeFormat) {
        Date date = new Date();

        String dateStr = null;
        try {
            SimpleDateFormat dateFormat = StringUtils.isEmpty(outTimeFormat) ? new SimpleDateFormat("yyyyMMdd_HHmmss")
                    : new SimpleDateFormat(outTimeFormat);
            dateStr = dateFormat.format(date);

        } catch (Exception e) {
            dateStr = null;
            log.error("Error:getFileNameByTime", e);
        }
        if (StringUtils.isEmpty(dateStr)) {
            dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        }
        String string = StringUtils.nullStrToEmpty(filehead) + dateStr + StringUtils.nullStrToEmpty(filetype);
        return string;
    }

    /**
     * 依据文件名称和环境名称重新生成文件名称
     * 如是filename为空或env为空则返回filename
     * 如是以文件夹分隔符(\、/)结尾则拼接env形成新文件夹
     * 文件名称(不带后缀)拼接上-env后返回
     *
     * @param fileName 文件名称
     * @param env      环境名称
     * @return 生成的文件名称
     */
    public static String getFileNameByEnv(String fileName, String env) {
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(env)) {
            return fileName;
        }
        String tmpFileName = FileUtils.getFileName(fileName);
        if (StringUtils.isEmpty(tmpFileName)) {
            return fileName + env;
        }
        String tmpEnvDir = fileName.substring(0, fileName.length() - tmpFileName.length());
        String tmpEnvName = FileUtils.getFileNameWithoutExtension(tmpFileName) + "-" + env;
        String tmpEnvExtension = FileUtils.getFileExtension(tmpFileName);
        if (StringUtils.isEmpty(tmpEnvExtension)) {
            return tmpEnvDir + tmpEnvName;
        } else {
            return tmpEnvDir + tmpEnvName + "." + tmpEnvExtension;
        }
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 源文件路径
     * @param desFilePath 目标文件路径
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @paramDefault isForce     是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @paramDefault timeCopy 是否复制修改时间
     */
    public static boolean copyFile(String srcfilePath, String desFilePath) {
        try {
            return copyFile(new File(srcfilePath), new File(desFilePath), DEFALUT_COPY_FORCE, false);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:copyFile", e);
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 源文件路径
     * @param desFilePath 目标文件路径
     * @param isForce     是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @paramDefault timeCopy 是否复制修改时间
     */
    public static boolean copyFile(String srcfilePath, String desFilePath, boolean isForce) {
        try {
            return copyFile(new File(srcfilePath), new File(desFilePath), isForce, false);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:copyFile", e);
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 源文件路径
     * @param desFilePath 目标文件路径
     * @param isForce     是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @param timeCopy    是否复制修改时间
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     */
    public static boolean copyFile(String srcfilePath, String desFilePath, boolean isForce, boolean timeCopy) {
        try {
            return copyFile(new File(srcfilePath), new File(desFilePath), isForce, timeCopy);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:copyFile", e);
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param srcfile 源文件
     * @param desfile 目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @paramDefault isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @paramDefault timeCopy 是否复制修改时间
     */
    public static boolean copyFile(File srcfile, File desfile) {
        return copyFile(srcfile, desfile, DEFALUT_COPY_FORCE, false);
    }

    /**
     * 复制文件
     *
     * @param srcfile 源文件
     * @param desfile 目标文件
     * @param isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @paramDefault timeCopy 是否复制修改时间
     */
    public static boolean copyFile(File srcfile, File desfile, boolean isForce) {
        return copyFile(srcfile, desfile, isForce, false);
    }

    /**
     * 复制文件
     *
     * @param srcfile  源文件
     * @param desfile  目标文件
     * @param isForce  是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @param timeCopy 是否复制修改时间
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     */
    public static boolean copyFile(File srcfile, File desfile, boolean isForce, boolean timeCopy) {
        BufferedInputStream bufis = null;
        BufferedOutputStream bufos = null;
        boolean isTrue = false;
        try {
            if (null == srcfile || !srcfile.exists()) {
                return false;
            }
            if (null == desfile) {
                return false;
            }
            if (desfile.exists() && desfile.isDirectory()) {
                return false;
            }
            if (desfile.exists() && desfile.isFile()) {
                if (isForce) {
                    boolean fileDelete = desfile.delete();
                    if (!fileDelete) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            bufis = new BufferedInputStream(new FileInputStream(srcfile));
            bufos = new BufferedOutputStream(new FileOutputStream(desfile));
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = bufis.read(buffer)) > 0) {
                bufos.write(buffer, 0, count);
            }
            bufos.flush();
            isTrue = true;
        } catch (Exception e) {
            log.error("Error:copyFile", e);
        } finally {
            if (null != bufis) {
                try {
                    bufis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:copyFile", e);
                }
            }
            if (null != bufos) {
                try {
                    bufos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:copyFile", e);
                }
            }
        }
        if (!isTrue) {
            delFile(desfile);
        } else if (timeCopy) {
            try {
                long srcLastModified = srcfile.lastModified();
                if (srcLastModified > 0) {
                    desfile.setLastModified(srcLastModified);
                }
            } catch (Exception e) {
                log.error("Error:copyFile->set copy file last modified time error", e);
            }
        }
        return isTrue;
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 源文件路径
     * @param desFilePath 目标文件路径
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @DefaultParam isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     */
    public static boolean copyFileBySha(String srcfilePath, String desFilePath) {
        return copyFileCommon(srcfilePath, desFilePath, DEFALUT_COPY_FORCE, true, true, 1);
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 源文件路径
     * @param desFilePath 目标文件路径
     * @param isForce     是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     */
    public static boolean copyFileBySha(String srcfilePath, String desFilePath, boolean isForce) {
        return copyFileCommon(srcfilePath, desFilePath, isForce, true, true, 1);
    }

    /**
     * 复制文件
     *
     * @param srcfile 源文件
     * @param desfile 目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     * @DefaultParam isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     */
    public static boolean copyFileBySha(File srcfile, File desfile) {
        return copyFileCommon(srcfile, desfile, DEFALUT_COPY_FORCE, true, true, 1);
    }

    /**
     * 复制文件
     *
     * @param srcfile 源文件
     * @param desfile 目标文件
     * @param isForce 是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     */
    public static boolean copyFileBySha(File srcfile, File desfile, boolean isForce) {
        return copyFileCommon(srcfile, desfile, isForce, true, true, 1);
    }

    /**
     * 复制文件
     *
     * @param srcfilePath 源文件
     * @param desFilePath 目标文件
     * @param isForce     是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @param hashVerify  是否校验复制结果
     * @param tryCount    重试次数，如是小于等于0或大于100，取值1
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     */
    public static boolean copyFileCommon(String srcfilePath, String desFilePath, boolean isForce, boolean hashVerify, boolean timeCopy, int tryCount) {
        try {
            return copyFileCommon(new File(srcfilePath), new File(desFilePath), isForce, hashVerify, timeCopy, tryCount);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:copyFileCommon", e);
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param srcfile    源文件
     * @param desfile    目标文件
     * @param isForce    是否强制覆盖目标文件。强制则覆盖目标文件，非强制则不覆盖目标文件
     * @param hashVerify 是否校验复制结果
     * @param tryCount   重试次数，如是小于等于0或大于100，取值1
     * @return 是否复制完成。true复制已完成没有错误,false 复制没有完成有错误。
     */
    public static boolean copyFileCommon(File srcfile, File desfile, boolean isForce, boolean hashVerify, boolean timeCopy, int tryCount) {
        try {
            if (null == srcfile || !srcfile.exists()) {
                return false;
            }
            if (null == desfile) {
                return false;
            }
            if (desfile.exists() && desfile.isDirectory()) {
                return false;
            }
            if (desfile.exists() && desfile.isFile()) {
                if (isForce) {
                    boolean fileDelete = desfile.delete();
                    if (!fileDelete) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("Error:copyFileCommon", e);
            return false;
        }
        boolean copyResult = false;
        int copyCount = tryCount > 0 && tryCount <= 100 ? tryCount : 1;
        for (int i = 0; i < copyCount; i++) {
            BufferedInputStream bufis = null;
            BufferedOutputStream bufos = null;
            boolean isTrue = false;
            try {
                if (i > 0) {
                    if (desfile.exists() && desfile.isFile()) {
                        boolean fileDelete = desfile.delete();
                        if (!fileDelete) {
                            return false;
                        }
                    } else if (desfile.exists()) {
                        return false;
                    }
                }
                bufis = new BufferedInputStream(new FileInputStream(srcfile));
                bufos = new BufferedOutputStream(new FileOutputStream(desfile));
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = bufis.read(buffer)) > 0) {
                    bufos.write(buffer, 0, count);
                }
                bufos.flush();
                isTrue = true;
            } catch (Exception e) {
                log.error("Error:copyFileCommon", e);
            } finally {
                if (null != bufis) {
                    try {
                        bufis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        log.error("Error:copyFileCommon", e);
                    }
                }
                if (null != bufos) {
                    try {
                        bufos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        log.error("Error:copyFileCommon", e);
                    }
                }
            }
            if (isTrue && timeCopy) {
                try {
                    long srcLastModified = srcfile.lastModified();
                    if (srcLastModified > 0) {
                        desfile.setLastModified(srcLastModified);
                    }
                } catch (Exception e) {
                    log.error("Error:copyFileCommon->set copy file last modified time error", e);
                }
            }
            if (!isTrue) {
                delFile(desfile);
                copyResult = false;
            } else if (!hashVerify) {
                copyResult = true;
            } else {
                boolean hashVerifyReuslt = shaVerifyCopyResult(srcfile, desfile);
                if (hashVerifyReuslt) {
                    copyResult = true;
                } else {
                    copyResult = false;
                }
            }
            if (copyResult) {
                break;
            }
        }
        return copyResult;
    }

    private static boolean shaVerifyCopyResult(File srcfile, File desfile) {
        String md5Src = calSha(srcfile, MD5_SHA_HELPER);
        String md5Dest = calSha(desfile, MD5_SHA_HELPER);
        if (StringUtils.isEmpty(md5Src) || StringUtils.isEmpty(md5Dest)) {
            try {
                desfile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else if (!md5Src.equalsIgnoreCase(md5Dest)) {
            try {
                desfile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath 文件filePath
     * @return 指纹校验值(MD5 、 SHA1等算法)
     * @paramDefault fileShaHelper 散列算法类，如是为空则使用MD5算法
     */
    public static String calSha(String filePath) {
        return calSha(filePath, MD5_SHA_HELPER);
    }

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath      文件路径
     * @param fileShaHelper 文件指纹校验值散列算法类
     * @return 指纹校验值(MD5 、 SHA1等算法)
     */
    public static String calSha(String filePath, FileShaHelper fileShaHelper) {
        if (null == filePath || filePath.length() <= 0) {
            return null;
        }
        String resultShaStr = null;
        try {
            File fileMd5 = new File(filePath);
            resultShaStr = calSha(fileMd5, fileShaHelper);
            fileMd5 = null;
        } catch (Exception e) {
            resultShaStr = null;
            log.error("Error:getDigestEncode", e);
        }
        return resultShaStr;

    }

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param file 文件
     * @return 指纹校验值(MD5 、 SHA1等算法)
     * @paramDefault fileShaHelper 散列算法类，如是为空则使用MD5算法
     */
    public static String calSha(File file) {
        return calSha(file, MD5_SHA_HELPER);
    }

    /**
     * 获取文件指纹校验值(MD5、SHA1等算法)
     *
     * @param file          文件
     * @param fileShaHelper 散列算法类，如是为空则使用MD5算法
     * @return 指纹校验值(MD5 、 SHA1等算法)
     */
    public static String calSha(File file, FileShaHelper fileShaHelper) {
        if (null == file) {
            return null;
        }
        String resultShaStr = null;
        BufferedInputStream in = null;
//        FileInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
//            in = new FileInputStream(file);
            if (null == fileShaHelper) {
                resultShaStr = MD5_SHA_HELPER.encoding(in);
            } else {
                resultShaStr = fileShaHelper.encoding(in);
            }
        } catch (Exception e) {
            resultShaStr = null;
            log.error("Error:getDigestEncode", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("Error:getDigestEncode", e);
                }
            }
        }
        return resultShaStr;
    }

    /**
     * 校验文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath  文件路径
     * @param shaResult 指纹校验值(MD5 、 SHA1等算法)
     * @return 校验结果
     * @paramDefault fileShaHelper 散列算法类
     */
    public static boolean verifySha(String filePath, String shaResult) {
        return verifySha(filePath, MD5_SHA_HELPER, shaResult);
    }

    /**
     * 校验文件指纹校验值(MD5、SHA1等算法)
     *
     * @param file      文件
     * @param shaResult 指纹校验值(MD5 、 SHA1等算法)
     * @return 校验结果
     * @paramDefault fileShaHelper 散列算法类
     */
    public static boolean verifySha(File file, String shaResult) {
        return verifySha(file, MD5_SHA_HELPER, shaResult);
    }

    /**
     * 校验文件指纹校验值(MD5、SHA1等算法)
     *
     * @param filePath      文件路径
     * @param fileShaHelper 散列算法类
     * @param shaResult     指纹校验值(MD5 、 SHA1等算法)
     * @return 校验结果
     */
    public static boolean verifySha(String filePath, FileShaHelper fileShaHelper, String shaResult) {
        if (StringUtils.isEmpty(shaResult)) {
            return false;
        }
        String shaByFile = calSha(filePath, fileShaHelper);
        if (StringUtils.isEmpty(shaByFile)) {
            return false;
        } else {
            return shaResult.equals(shaByFile);
        }
    }

    /**
     * 校验文件指纹校验值(MD5、SHA1等算法)
     *
     * @param file          文件
     * @param fileShaHelper 散列算法类
     * @param shaResult     指纹校验值(MD5 、 SHA1等算法)
     * @return 校验结果
     */
    public static boolean verifySha(File file, FileShaHelper fileShaHelper, String shaResult) {
        if (StringUtils.isEmpty(shaResult)) {
            return false;
        }
        String shaByFile = calSha(file, fileShaHelper);
        if (StringUtils.isEmpty(shaByFile)) {
            return false;
        } else {
            return shaResult.equals(shaByFile);
        }
    }

    /**
     * 文件路径是否以文件夹连接符号(File.separator)结尾
     *
     * @param filePath 文件路径
     * @return 文件路径是否以文件夹连接符号(File.separator)结尾
     */
    public static boolean isEndWithFileSeparator(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        boolean flag = filePath.endsWith("\\");
        if (!flag) {
            flag = filePath.endsWith("/");
        }
        return flag;
    }

    /**
     * 文件路径是否以文件夹连接符号(File.separator)开始
     *
     * @param filePath 文件路径
     * @return 文件路径是否以文件夹连接符号(File.separator)开始
     */
    public static boolean isStartWithFileSeparator(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        boolean flag = filePath.startsWith("\\");
        if (!flag) {
            flag = filePath.startsWith("/");
        }
        return flag;
    }

    /**
     * 查找文件分割符号最后的位置
     *
     * @param filePath 文件路径
     * @return 返回分割符号最后的位置，空返回-99，没有返回-1，
     */
    public static int lastIndexOfFileSeparator(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return -99;
        }
        int filePosi1 = filePath.lastIndexOf("\\");
        int filePosi2 = filePath.lastIndexOf("/");
        if (filePosi1 < 0 && filePosi2 < 0) {
            return -1;
        } else {
            return filePosi1 > filePosi2 ? filePosi1 : filePosi2;
        }
    }

    /**
     * 查找文件分割符号第一个的位置
     *
     * @param filePath 文件路径
     * @return 返回分割符号第一个的位置位置，空返回-99，没有返回-1，
     */
    public static int firstIndexOfFileSeparator(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return -99;
        }
        int filePosi1 = filePath.indexOf("\\");
        int filePosi2 = filePath.indexOf("/");
        if (filePosi1 < 0 && filePosi2 < 0) {
            return -1;
        } else if (filePosi1 < 0) {
            return filePosi2;
        } else if (filePosi2 < 0) {
            return filePosi1;
        } else {
            return filePosi1 < filePosi2 ? filePosi1 : filePosi2;
        }
    }

    /**
     * 依据文文件或文件夹路径智能获取文件夹连接符号(File.separator)。
     *
     * @param filePath 文件或文件夹路径
     * @return 文件夹连接符号(File.separator)
     */
    public static String parseFileSeparator(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return File.separator;
        }
        int filePosi1 = filePath.lastIndexOf("\\");
        int filePosi2 = filePath.lastIndexOf("/");
        if (filePosi1 >= 0 && filePosi2 >= 0) {
            return filePosi1 > filePosi2 ? "\\" : "/";
//			return File.separator;
        } else if (filePosi1 >= 0) {
            return "\\";
        } else if (filePosi2 >= 0) {
            return "/";
        } else {
            return File.separator;
        }
    }

    /**
     * 判断文件夹是否根目录
     */
    public static boolean isRootDir(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        int count = StringUtils.counterAppearNum(filePath, '\\') + StringUtils.counterAppearNum(filePath, '/');
        if (count == 1 && FileUtils.isEndWithFileSeparator(filePath)) {
            if (filePath.length() == 1) {
                // Linux下面
                return true;
            } else if (filePath.contains(":")) {
                // Windows 下面
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取文件夹或文件层级，如是以文件分隔符结尾则去除
     */
    public static int calFileLevel(String filePath, boolean fileMode) {
        if (StringUtils.isEmpty(filePath)) {
            return 0;
        }

        if (fileMode) {
            String tmpPath;
            try {
                tmpPath = new File(filePath).getPath();
            } catch (Exception e) {
                e.printStackTrace();
                tmpPath = null;
            }
            if (StringUtils.isEmpty(tmpPath)) {
                tmpPath = filePath;
            }
            int count = StringUtils.counterAppearNum(tmpPath, '\\') + StringUtils.counterAppearNum(tmpPath, '/');
            if (isEndWithFileSeparator(tmpPath)) {
                return count - 1;
            } else {
                return count;
            }
        } else {
            int count = StringUtils.counterAppearNum(filePath, '\\') + StringUtils.counterAppearNum(filePath, '/');
            if (isEndWithFileSeparator(filePath)) {
                return count - 1;
            } else {
                return count;
            }
        }
    }

    /**
     * 文件加密存储
     *
     * @param srcPath     源文件路径
     * @param desPath     目标文件路径
     * @param fileEncrypt 加解密辅助类
     * @return 是否正确加密完成
     */
    public static boolean fileEnc(String srcPath, String desPath, FileEncrypt fileEncrypt) {
        if (null == srcPath || srcPath.length() <= 0 || null == desPath || desPath.length() <= 0) {
            return false;
        }
        return fileEnc(new File(srcPath), createFile(desPath), fileEncrypt);
    }

    /**
     * 文件加密存储
     *
     * @param srcFile     源文件路径
     * @param desFile     目标文件路径
     * @param fileEncrypt 加解密辅助类
     * @return 是否正确加密完成
     */
    public static boolean fileEnc(File srcFile, File desFile, FileEncrypt fileEncrypt) {
        if (null == srcFile || !srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        if (null == desFile || desFile.isDirectory()) {
            return false;
        }
        if (null == fileEncrypt) {
            return false;
        }
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        boolean flag = false;
        try {
            byte[] body = null;
//			InputStream is = new FileInputStream(srcPath);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(srcFile));
            body = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(body);
            byte[] outBody = fileEncrypt.encrypt(body);
            if (null == outBody || outBody.length <= 0) {
                flag = false;
            } else {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desFile));
                bufferedOutputStream.write(outBody);
                bufferedOutputStream.flush();
                flag = true;
            }

        } catch (Exception e) {
            flag = false;
            log.error("Error:fileEnc", e);
        } finally {
            try {
                if (null != bufferedOutputStream) {
                    bufferedOutputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:fileEnc", e);
            }
            try {
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:fileEnc", e);
            }
            bufferedOutputStream = null;
            bufferedInputStream = null;
        }
        return flag;
    }

    /**
     * 文件解密密存储
     *
     * @param srcPath     源文件路径
     * @param desPath     目标文件路径
     * @param fileEncrypt 加解密辅助类
     * @return 是否正确解密完成
     */
    public static boolean fileDec(String srcPath, String desPath, FileEncrypt fileEncrypt) {
        if (null == srcPath || srcPath.length() <= 0 || null == desPath || desPath.length() <= 0) {
            return false;
        }
        return fileDec(new File(srcPath), createFile(desPath), fileEncrypt);
    }

    /**
     * 文件解密存储
     *
     * @param srcFile     源文件路径
     * @param desFile     目标文件路径
     * @param fileEncrypt 加解密辅助类
     * @return 是否正确解密完成
     */
    public static boolean fileDec(File srcFile, File desFile, FileEncrypt fileEncrypt) {
        if (null == srcFile || !srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        if (null == desFile || desFile.isDirectory()) {
            return false;
        }
        if (null == fileEncrypt) {
            return false;
        }
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        boolean flag = false;
        try {
            byte[] body = null;
            bufferedInputStream = new BufferedInputStream(new FileInputStream(srcFile));
            body = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(body);
            byte[] outBody = fileEncrypt.decrypt(body);
            if (null == outBody || outBody.length <= 0) {
                flag = false;
            } else {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(desFile));
                bufferedOutputStream.write(outBody);
                bufferedOutputStream.flush();
                flag = true;
            }

        } catch (Exception e) {
            flag = false;
            log.error("Error:fileDec", e);
        } finally {
            try {
                if (null != bufferedOutputStream) {
                    bufferedOutputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:fileDec", e);
            }
            try {
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:fileDec", e);
            }
            bufferedOutputStream = null;
            bufferedInputStream = null;
        }
        return flag;
    }

    /**
     * 文件加密为byte[]数据
     *
     * @param srcPath     源文件路径
     * @param fileEncrypt 加解密辅助类
     * @return 加密结果byte[]数据
     */
    public static byte[] fileEncToByte(String srcPath, FileEncrypt fileEncrypt) {
        return fileEncToByte(new File(srcPath), fileEncrypt);
    }

    /**
     * 文件加密为byte[]数据
     *
     * @param srcFile     源文件
     * @param fileEncrypt 加解密辅助类
     * @return 加密结果byte[]数据
     */
    public static byte[] fileEncToByte(File srcFile, FileEncrypt fileEncrypt) {
        BufferedInputStream bufferedInputStream = null;
        byte[] outBody = null;
        try {
            byte[] body = null;
            bufferedInputStream = new BufferedInputStream(new FileInputStream(srcFile));
            body = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(body);
            outBody = fileEncrypt.encrypt(body);

        } catch (Exception e) {
            outBody = null;
            log.error("Error:fileDecToByte", e);
        } finally {
            try {
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:fileDecToByte", e);
            }
            bufferedInputStream = null;
        }
        return outBody;
    }

    /**
     * 文件解密为byte[]数据
     *
     * @param srcPath     源文件路径
     * @param fileEncrypt 加解密辅助类
     * @return 解密结果byte[]数据
     */
    public static byte[] fileDecToByte(String srcPath, FileEncrypt fileEncrypt) {
        return fileDecToByte(new File(srcPath), fileEncrypt);
    }

    /**
     * 文件解密为byte[]数据
     *
     * @param srcFile     源文件
     * @param fileEncrypt 加解密辅助类
     * @return 解密结果byte[]数据
     */
    public static byte[] fileDecToByte(File srcFile, FileEncrypt fileEncrypt) {
        BufferedInputStream bufferedInputStream = null;
        byte[] outBody = null;
        try {
            byte[] body = null;
            bufferedInputStream = new BufferedInputStream(new FileInputStream(srcFile));
            body = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(body);
            outBody = fileEncrypt.decrypt(body);

        } catch (Exception e) {
            outBody = null;
            log.error("Error:fileDecToByte", e);
        } finally {
            try {
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:fileDecToByte", e);
            }
            bufferedInputStream = null;
        }
        return outBody;
    }

    /**
     * 文件加解密类
     */
    public interface FileEncrypt {
        /**
         * 加密方法
         *
         * @param resByte 待加密字节数组
         * @return 加密后字节数组
         */
        public byte[] encrypt(byte[] resByte);

        /**
         * 解密方法
         *
         * @param resByte 待解密字节数组
         * @return 解密后字节数组
         */
        public byte[] decrypt(byte[] resByte);
    }

    /**
     * 是否UTF-8编码
     *
     * @param charset 字符编码
     * @return 是否UTF-8编码
     */
    private static boolean isCharSetUTF8(Charset charset) {
        if (null == charset) {
            return false;
        }
        String charsetName = charset.name();
        if (null == charsetName || charsetName.length() <= 0) {
            return false;
        }
        if ("UTF-8".equalsIgnoreCase(charsetName) || "UTF8".equalsIgnoreCase(charsetName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否UTF-8编码
     *
     * @param charsetName 字符编码
     * @return 是否UTF-8编码
     */
    private static boolean isCharSetUTF8(String charsetName) {
        if (null == charsetName || charsetName.length() <= 0) {
            return false;
        }
        if ("UTF-8".equalsIgnoreCase(charsetName) || "UTF8".equalsIgnoreCase(charsetName)) {
            return true;
        } else {
            return false;
        }
    }
}
