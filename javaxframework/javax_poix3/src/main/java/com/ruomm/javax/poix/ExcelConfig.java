/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年8月28日 下午2:12:45
 */
package com.ruomm.javax.poix;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;

public class ExcelConfig {
    public final static String EXCEL_XLS = "xls";
    public final static String EXCEL_XLSX = "xlsx";
    public final static String EXCEL_DEFAULT = "xlsx";

    public static String parseSheetType(String sheelType, String filePath) {
        String fileExtension = null;
        if (null != filePath && (filePath.length() == 3 || filePath.length() == 4) && !filePath.startsWith(".")) {
            fileExtension = filePath;
        } else if (null != filePath && (filePath.length() == 4 || filePath.length() == 5) && filePath.startsWith(".")) {
            fileExtension = filePath.substring(1);
        } else {
            fileExtension = FileUtils.getFileExtension(filePath);
        }
        if (!StringUtils.isEmpty(sheelType) && sheelType.equalsIgnoreCase(EXCEL_XLS)) {
            return EXCEL_XLS;
        } else if (!StringUtils.isEmpty(sheelType) && sheelType.equalsIgnoreCase(EXCEL_XLSX)) {
            return EXCEL_XLSX;
        } else if (!StringUtils.isEmpty(fileExtension) && fileExtension.equalsIgnoreCase(EXCEL_XLS)) {
            return EXCEL_XLS;
        } else if (!StringUtils.isEmpty(fileExtension) && fileExtension.equalsIgnoreCase(EXCEL_XLSX)) {
            return EXCEL_XLSX;
        } else {
            return EXCEL_DEFAULT;
        }
    }
}
