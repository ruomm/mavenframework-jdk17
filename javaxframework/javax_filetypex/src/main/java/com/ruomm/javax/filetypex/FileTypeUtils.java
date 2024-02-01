package com.ruomm.javax.filetypex;


import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/9 9:37
 */
public class FileTypeUtils {
    private final static Log log = LogFactory.getLog(FileTypeUtils.class);
    //    private final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
    private final static List<FileTypeDal> FILE_TYPE_LIST = new ArrayList<FileTypeDal>();
    private final static int MAX_SIZE;

    static {
        Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
        //images
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)
        FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)
        FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)
        FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)
        FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)
        //
        FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)
        FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)
        FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)
        FILE_TYPE_MAP.put("xml", "3C3F786D6C");
        FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)
        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)
        // FILE_TYPE_MAP.put("doc","D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样
        // FILE_TYPE_MAP.put("xls","D0CF11E0");  //MS Word
        FILE_TYPE_MAP.put("doc,xls", "D0CF11E0");
        FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)
        FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)
        // FILE_TYPE_MAP.put("docx", "504B0304");  //MS Excel 注意：word 和 excel的文件头一样
        // FILE_TYPE_MAP.put("xlsx", "504B0304");  //MS Word
        FILE_TYPE_MAP.put("docx", "504B0304140006");
        FILE_TYPE_MAP.put("xlsx", "504B0304140008");
        FILE_TYPE_MAP.put("rar", "52617221");
        FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)
        FILE_TYPE_MAP.put("avi", "41564920");
        FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)
        // FILE_TYPE_MAP.put("mpg", "000001BA");  //
        // FILE_TYPE_MAP.put("mpg", "000001B3");  //
        FILE_TYPE_MAP.put("mpg", "000001BA,000001B3");  //
        FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)
        FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)
        FILE_TYPE_MAP.put("gz", "1F8B08");  //MIDI (mid)
        FILE_TYPE_MAP.put("zip", "504B0304");
        FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)
        FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)
        FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)
        FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)
        FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)
        int maxSize = 0;
        Set<String> keySet = FILE_TYPE_MAP.keySet();
        for (String typeKey : keySet) {
            String[] typeValues = FILE_TYPE_MAP.get(typeKey).split(",");
            if (null == typeValues || typeValues.length <= 0) {
                continue;
            }
            for (String typeValue : typeValues) {
                if (null == typeValue || typeValue.length() <= 0) {
                    continue;
                }
                int tmpSize = (typeValue.length() + 1) / 2;
                if (maxSize < tmpSize) {
                    maxSize = tmpSize;
                }
                FileTypeDal fileTypeDal = new FileTypeDal();
                fileTypeDal.setKey(typeKey);
                fileTypeDal.setValue(typeValue);
                FILE_TYPE_LIST.add(fileTypeDal);
            }
        }
        MAX_SIZE = maxSize;
        FILE_TYPE_LIST.sort(new Comparator<FileTypeDal>() {
            @Override
            public int compare(FileTypeDal o1, FileTypeDal o2) {
                int lenO1 = null == o1.getValue() ? 0 : o1.getValue().length();
                int len02 = null == o2.getValue() ? 0 : o2.getValue().length();
                if (lenO1 < len02) {
                    return 1;
                } else if (lenO1 > len02) {
                    return -1;
                } else if (lenO1 == 0) {
                    return 0;
                } else {
                    return o1.getValue().compareTo(o2.getValue());
                }
            }
        });
        log.debug("static->" + "文件类型头部类别加载:" + FILE_TYPE_LIST.toString());
        log.debug("static->" + "文件类型头部类别最大值:" + MAX_SIZE);
    }

    /**
     * 字节数组转换为二进制字符串
     *
     * @param src 字节数组
     * @return 二进制字符串
     */
    private static String bytesToHexString(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            String hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    public static String getFileTypeByHeader(String fileHader) {
        if (null == fileHader || fileHader.length() <= 0) {
            return null;
        }
        String fileType = null;
        for (FileTypeDal fileTypeDal : FILE_TYPE_LIST) {
            String typeKey = fileTypeDal.getKey();
            String typeValue = fileTypeDal.getValue();
            if (null == typeKey || typeKey.length() <= 0 || null == typeValue || typeValue.length() <= 0) {
                continue;
            }
            if (fileHader.startsWith(typeValue)) {
                fileType = typeKey;
            }
            if (null != fileType && fileType.length() > 0) {
                break;
            }
        }
        return fileType;
    }

    /**
     * 解析文件类型
     *
     * @param file 文件路径
     * @return 文件类型
     */
    public static String getFileType(String file) {
        return getFileTypeByHeader(getFileHeader(file, MAX_SIZE, false));
    }

    /**
     * 解析文件类型
     *
     * @param file 文件
     * @return 文件类型
     */
    public static String getFileType(File file) {
        return getFileTypeByHeader(getFileHeader(file, MAX_SIZE, false));
    }

    /**
     * 解析文件类型
     *
     * @param is 文件输入流
     * @return 文件类型
     */
    public static String getFileType(InputStream is) {
        return getFileTypeByHeader(getFileHeader(is, MAX_SIZE, false));
    }

    /**
     * 获取文件头二进制标识
     *
     * @param file 文件路径
     * @return 文件头二进制标识
     * @paramDefault len 头部长度,默认长度4
     */
    public static String getFileHeader(String file) {
        return getFileHeader(file, 4, false);
    }

    /**
     * 获取文件头二进制标识
     *
     * @param file 文件路径
     * @param len  头部长度
     * @return 文件头二进制标识
     */
    public static String getFileHeader(String file, int len) {
        return getFileHeader(file, len, false);
    }

    /**
     * 获取文件头二进制标识
     *
     * @param file  文件路径
     * @param len   头部长度
     * @param isFit 是否自适应文件大小，自适应的话获取文件头可能小于需要的长度
     * @return 文件头二进制标识
     */
    public static String getFileHeader(String file, int len, boolean isFit) {
        try {
            return getFileHeader(new FileInputStream(file), len, isFit);
        } catch (Exception e) {
            log.error("Error:getFileHeader", e);
            return null;
        }
    }

    /**
     * 获取文件头二进制标识
     *
     * @param file 文件
     * @return 文件头二进制标识
     * @paramDefault len 头部长度,默认长度4
     */
    public static String getFileHeader(File file) {
        return getFileHeader(file, 4, false);
    }

    /**
     * 获取文件头二进制标识
     *
     * @param file 文件
     * @param len  头部长度
     * @return 文件头二进制标识
     */
    public static String getFileHeader(File file, int len) {
        return getFileHeader(file, len, false);
    }

    /**
     * 获取文件头二进制标识
     *
     * @param file  文件
     * @param len   头部长度
     * @param isFit 是否自适应文件大小，自适应的话获取文件头可能小于需要的长度
     * @return 文件头二进制标识
     */
    public static String getFileHeader(File file, int len, boolean isFit) {
        try {
            return getFileHeader(new FileInputStream(file), len, isFit);
        } catch (Exception e) {
            log.error("Error:getFileHeader", e);
            return null;
        }
    }

    /**
     * 获取文件头二进制标识
     *
     * @param is 输入流
     * @return 文件头二进制标识
     * @paramDefault len 头部长度,默认长度4
     */
    public static String getFileHeader(InputStream is) {
        return getFileHeader(is, 4, false);
    }

    /**
     * 获取文件头二进制标识
     *
     * @param is  输入流
     * @param len 头部长度
     * @return 文件头二进制标识
     */
    public static String getFileHeader(InputStream is, int len) {
        return getFileHeader(is, len, false);
    }

    /**
     * 获取文件头二进制标识
     *
     * @param is    输入流
     * @param len   头部长度
     * @param isFit 是否自适应文件大小，自适应的话获取文件头可能小于需要的长度
     * @return 文件头二进制标识
     */
    public static String getFileHeader(InputStream is, int len, boolean isFit) {
        if (len <= 0) {
            return null;
        }
        int readLen = 0;
        String valHeader = null;
        try {
            if (isFit) {
                int fileSize = is.available();
                if (fileSize > 0 && fileSize < len) {
                    readLen = fileSize;
                } else {
                    readLen = len;
                }
            } else {
                readLen = len;
            }
            byte[] b = new byte[readLen];
            is.read(b, 0, readLen);
            valHeader = bytesToHexString(b);
        } catch (Exception e) {
            log.error("Error:getFileHeader", e);
            valHeader = null;
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {
                log.error("Error:getFileHeader", e);
            }
        }
        log.debug("getFileHeader->" + "文件的头部信息:" + valHeader);
        return valHeader;
    }
}
