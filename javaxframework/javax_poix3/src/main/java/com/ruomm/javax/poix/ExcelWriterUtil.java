/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月12日 下午5:21:03
 */
package com.ruomm.javax.poix;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.poix.annotation.DefExcelWriterCell;
import com.ruomm.javax.poix.annotation.DefExcelWtiterSheet;
import com.ruomm.javax.poix.config.ExcelAlignment;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExcelWriterUtil {
    private final static Log log = LogFactory.getLog(ExcelWriterUtil.class);

    /**
     * 判断Excel的版本,获取Workbook
     *
     * @param in
     * @param in
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbok(String sheelType, InputStream in) throws IOException {
        if (null != in) {
            Workbook wb = null;
            if (ExcelConfig.EXCEL_XLS.equalsIgnoreCase(sheelType)) {
                wb = new HSSFWorkbook(in);
            } else {
                wb = new XSSFWorkbook(in);
            }
            return wb;
        } else {
            Workbook wb = null;
            if (ExcelConfig.EXCEL_XLS.equalsIgnoreCase(sheelType)) {
                wb = new HSSFWorkbook();
            } else {
                wb = new XSSFWorkbook();
            }
            return wb;
        }

    }

    public static <T> boolean writerExcel(String pathXls, List<T> listData, Class<T> cls) {
        return writerExcel(new File(pathXls), listData, cls);
    }

    public static <T> boolean writerExcel(File fileXls, List<T> listData, Class<T> cls) {
        Workbook wb = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            ExcelWriterSheet excelWriterSheet = getExcelWriterSheet(cls, fileXls.getPath());
            List<ExcelWriterCell> listExcelWriterCell = getListExcelWriterCell(cls);
            int listExcelWriterCellSize = null == listExcelWriterCell ? 0 : listExcelWriterCell.size();
            if (listExcelWriterCellSize <= 0) {
                new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelUtil.writerExcel->写入Excel文件时候发生异常，无法获取列的数量");
            }
            if (fileXls.exists()) {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(fileXls));
            }
            wb = getWorkbok(excelWriterSheet.getSheetType(), bufferedInputStream);

            Sheet sheet = wb.getSheet(excelWriterSheet.getSheetName());
            CellStyle cellStyleData = null;
            if (null == sheet) {
                sheet = wb.createSheet(excelWriterSheet.getSheetName());
//				HSSFDataFormat dataFormat = workbook.createDataFormat();
                DataFormat dataFormat = wb.createDataFormat();
                cellStyleData = getRowDataCellStyle(excelWriterSheet, wb, dataFormat, false);
                CellStyle cellStyleHeader = getHeaderCellStyle(excelWriterSheet, wb, dataFormat, true);
                CellStyle cellStyleTitle = wb.createCellStyle();
                cellStyleTitle.setAlignment(excelWriterSheet.getTitleHorizontal());
                cellStyleTitle.setVerticalAlignment(excelWriterSheet.getTitleVertical());
                cellStyleTitle.setDataFormat(dataFormat.getFormat("@"));
                if (excelWriterSheet.getTitleFontSize() > 0) {
                    Font font = wb.createFont();
                    font.setFontHeightInPoints((short) excelWriterSheet.getTitleFontSize());
                    cellStyleTitle.setFont(font);
                }
                if (!StringUtils.isEmpty(excelWriterSheet.getTitleName())) {
                    for (int j = 0; j < excelWriterSheet.getTitleRowNums(); j++) {

                        Row row = sheet.createRow(j);
                        if (excelWriterSheet.getTitleRowHeight() > 0) {
                            row.setHeight((short) (excelWriterSheet.getTitleRowHeight() * 20));
                        }
                        for (int i = 0; i < listExcelWriterCellSize; i++) {
                            Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                            if (i == 0 && j == 0) {
                                cell.setCellValue(excelWriterSheet.getTitleName());
                            }
                            cell.setCellStyle(cellStyleTitle);
                        }
                    }
                    CellRangeAddress region = new CellRangeAddress(0, excelWriterSheet.getTitleRowNums() - 1, 0,
                            listExcelWriterCellSize - 1);
                    sheet.addMergedRegion(region);
                }

                Row row = sheet.createRow(excelWriterSheet.getTitleRowNums());
                for (int i = 0; i < listExcelWriterCellSize; i++) {
                    Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    String cellValue = listExcelWriterCell.get(i).getCellName();
                    cell.setCellValue(cellValue);
                    cell.setCellStyle(cellStyleHeader);
                }
                for (int i = 0; i < listExcelWriterCellSize; i++) {
                    ExcelWriterCell excelWriterCell = listExcelWriterCell.get(i);
                    sheet.setDefaultColumnStyle(i, cellStyleData);
                    if (excelWriterCell.getCellWidth() <= 0) {
                        continue;
                    }
                    sheet.setColumnWidth(i, excelWriterCell.getCellWidth() * 256);
                }
            } else {
                DataFormat dataFormat = wb.createDataFormat();
                cellStyleData = getRowDataCellStyle(excelWriterSheet, wb, dataFormat, false);

            }
            int lastRowNum = sheet.getLastRowNum();
            for (int j = 0; j < listData.size(); j++) {
                Row row = sheet.createRow(j + lastRowNum + 1);
                if (excelWriterSheet.getRowHeight() > 0) {
                    row.setHeight((short) (excelWriterSheet.getRowHeight() * 20));
                }
                for (int i = 0; i < listExcelWriterCellSize; i++) {
                    Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    String cellValue = getDataByExcelCellWriter(listData.get(j), listExcelWriterCell.get(i));
                    cell.setCellValue(cellValue);
                    if (null != cellStyleData) {
                        if (listExcelWriterCell.get(i).isWrapText()) {
                            cellStyleData.setWrapText(listExcelWriterCell.get(i).isWrapText());
                        }
                        cell.setCellStyle(cellStyleData);
                    }
                }
            }
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileXls));
            wb.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            return true;
        } catch (IOException e) {
            // TODO: handle exception
            log.error("Error:readExcel", e);
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelUtil.writerExcel->写入Excel文件时候发生异常");
        } finally {
//			if (wb != null) {
//				try {
//					wb.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					log.error("Error:ExcelUtil.writerExcel", e);
//				}
//			}
            if (null != bufferedInputStream) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e2) {
                    log.error("Error:ExcelUtil.writerExcel", e2);
                }
            }
            if (null != bufferedOutputStream) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    log.error("Error:ExcelUtil.writerExcel", e2);
                }
            }
        }
    }

    private static CellStyle getRowDataCellStyle(ExcelWriterSheet excelWriterSheet, Workbook wb, DataFormat dataFormat,
                                                 boolean isBold) {
        CellStyle cellStyleData = wb.createCellStyle();
        cellStyleData.setAlignment(excelWriterSheet.getCellDefaultHorizontal());
        cellStyleData.setVerticalAlignment(excelWriterSheet.getCellDefaultVertical());
        cellStyleData.setDataFormat(dataFormat.getFormat("@"));
        if (excelWriterSheet.getRowFontSize() > 0) {
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) excelWriterSheet.getRowFontSize());
            if (isBold) {
                font.setBold(true);
            }
            cellStyleData.setFont(font);
        }
        return cellStyleData;
    }

    private static CellStyle getHeaderCellStyle(ExcelWriterSheet excelWriterSheet, Workbook wb, DataFormat dataFormat,
                                                boolean isBold) {
        CellStyle cellStyleData = wb.createCellStyle();
        cellStyleData.setAlignment(HorizontalAlignment.CENTER);
        cellStyleData.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleData.setDataFormat(dataFormat.getFormat("@"));
        if (excelWriterSheet.getRowFontSize() > 0) {
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) excelWriterSheet.getRowFontSize());
            font.setBold(true);
            cellStyleData.setFont(font);
        }
        return cellStyleData;
    }

    private static String getDataByExcelCellWriter(Object obj, ExcelWriterCell cellWriter) {
        if (null != cellWriter.getCellField()) {
            try {
                cellWriter.getCellField().setAccessible(true);
                Object resultObj = cellWriter.getCellField().get(obj);
                if (null == resultObj) {
                    return "";
                } else if (resultObj instanceof String) {
                    return (String) resultObj;
                } else {
                    return String.valueOf(resultObj);
                }
            } catch (Exception e) {
                log.error("Error:getDataByExcelCellWriter", e);
                return null;
            }

        }
        if (null != cellWriter.getCellMethod()) {
            try {
                cellWriter.getCellMethod().setAccessible(true);
                Object resultObj = cellWriter.getCellMethod().invoke(obj);
                if (null == resultObj) {
                    return "";
                } else if (resultObj instanceof String) {
                    return (String) resultObj;
                } else {
                    return String.valueOf(resultObj);
                }
            } catch (Exception e) {
                log.error("Error:getDataByExcelCellWriter", e);
                return null;
            }

        } else {
            return "";
        }
    }

    private static ExcelWriterSheet getExcelWriterSheet(Class<?> cls, String pathXls) {
        DefExcelWtiterSheet def = cls.getAnnotation(DefExcelWtiterSheet.class);
        ExcelWriterSheet excelWriterSheet = new ExcelWriterSheet();
        excelWriterSheet
                .setSheetName(null == def || StringUtils.isEmpty(def.sheetName()) ? cls.getSimpleName() : def.sheetName());
        excelWriterSheet.setSheetType(ExcelConfig.parseSheetType(null == def ? null : def.sheetType(), pathXls));
        excelWriterSheet.setTitleName(null == def ? null : def.titleName());
        int titleRowNums = 0;
        if (StringUtils.isEmpty(excelWriterSheet.getTitleName())) {
            titleRowNums = 0;
        } else if (null == def) {// def.titleRowNums() <= 0
            titleRowNums = 1;
        } else if (def.titleRowNums() <= 0) {//
            titleRowNums = 1;
        } else {
            titleRowNums = def.titleRowNums();
        }
        excelWriterSheet.setTitleRowNums(titleRowNums);
        excelWriterSheet.setTitleRowHeight(null == def ? 0 : def.titleRowHeight());
        excelWriterSheet.setTitleFontSize(null == def ? 0 : def.titleFontSize());

        excelWriterSheet.setRowHeight(null == def ? 0 : def.rowHeight());
        excelWriterSheet.setRowFontSize(null == def ? 0 : def.rowFontSize());
        if (null == def || null == def.titleHorizontal()) {
            excelWriterSheet.setTitleHorizontal(HorizontalAlignment.CENTER);
        } else if (def.titleHorizontal() == ExcelAlignment.TOP_OR_LEFT) {
            excelWriterSheet.setTitleHorizontal(HorizontalAlignment.LEFT);
        } else if (def.titleHorizontal() == ExcelAlignment.BOTTOM_OR_RIGHT) {
            excelWriterSheet.setTitleHorizontal(HorizontalAlignment.RIGHT);
        } else if (def.titleHorizontal() == ExcelAlignment.CENTER) {
            excelWriterSheet.setTitleHorizontal(HorizontalAlignment.CENTER);
        } else {
            excelWriterSheet.setTitleHorizontal(HorizontalAlignment.CENTER);
        }
        if (null == def || null == def.titleVertical()) {
            excelWriterSheet.setTitleVertical(VerticalAlignment.CENTER);
        } else if (def.titleVertical() == ExcelAlignment.TOP_OR_LEFT) {
            excelWriterSheet.setTitleVertical(VerticalAlignment.TOP);
        } else if (def.titleVertical() == ExcelAlignment.BOTTOM_OR_RIGHT) {
            excelWriterSheet.setTitleVertical(VerticalAlignment.BOTTOM);
        } else if (def.titleVertical() == ExcelAlignment.CENTER) {
            excelWriterSheet.setTitleVertical(VerticalAlignment.CENTER);
        } else {
            excelWriterSheet.setTitleVertical(VerticalAlignment.CENTER);
        }

        if (null == def || null == def.cellDefaultHorizontal()) {
            excelWriterSheet.setCellDefaultHorizontal(HorizontalAlignment.LEFT);
        } else if (def.cellDefaultHorizontal() == ExcelAlignment.TOP_OR_LEFT) {
            excelWriterSheet.setCellDefaultHorizontal(HorizontalAlignment.LEFT);
        } else if (def.cellDefaultHorizontal() == ExcelAlignment.BOTTOM_OR_RIGHT) {
            excelWriterSheet.setCellDefaultHorizontal(HorizontalAlignment.RIGHT);
        } else if (def.cellDefaultHorizontal() == ExcelAlignment.CENTER) {
            excelWriterSheet.setCellDefaultHorizontal(HorizontalAlignment.CENTER);
        } else {
            excelWriterSheet.setCellDefaultHorizontal(HorizontalAlignment.LEFT);
        }
        if (null == def || null == def.cellDefaultVertical()) {
            excelWriterSheet.setCellDefaultVertical(VerticalAlignment.BOTTOM);
        } else if (def.cellDefaultVertical() == ExcelAlignment.TOP_OR_LEFT) {
            excelWriterSheet.setCellDefaultVertical(VerticalAlignment.TOP);
        } else if (def.cellDefaultVertical() == ExcelAlignment.BOTTOM_OR_RIGHT) {
            excelWriterSheet.setCellDefaultVertical(VerticalAlignment.BOTTOM);
        } else if (def.cellDefaultVertical() == ExcelAlignment.CENTER) {
            excelWriterSheet.setCellDefaultVertical(VerticalAlignment.CENTER);
        } else {
            excelWriterSheet.setCellDefaultVertical(VerticalAlignment.BOTTOM);
        }
        return excelWriterSheet;
    }

    private static List<ExcelWriterCell> getListExcelWriterCell(Class<?> cls) {
        List<ExcelWriterCell> list = new ArrayList<ExcelWriterCell>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            // 常量跳过
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            // 静态跳过
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            DefExcelWriterCell defExcelWriterCell = field.getAnnotation(DefExcelWriterCell.class);
            if (null == defExcelWriterCell || defExcelWriterCell.cellIndex() < 0) {
                continue;
            }
            String cellName = StringUtils.isEmpty(defExcelWriterCell.cellName()) ? field.getName()
                    : defExcelWriterCell.cellName();
            ExcelWriterCell excelCellWriter = new ExcelWriterCell();
            excelCellWriter.setCellIndex(defExcelWriterCell.cellIndex());
            excelCellWriter.setCellName(cellName);
            if (defExcelWriterCell.cellWidth() > 0) {
                excelCellWriter.setCellWidth(defExcelWriterCell.cellWidth());
            } else {
                excelCellWriter.setCellWidth(20);
            }
            excelCellWriter.setWrapText(defExcelWriterCell.wrapText());
            excelCellWriter.setCellField(field);
            list.add(excelCellWriter);
        }
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            // 静态跳过
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            DefExcelWriterCell defExcelWriterCell = method.getAnnotation(DefExcelWriterCell.class);
            if (null == defExcelWriterCell || defExcelWriterCell.cellIndex() < 0) {
                continue;
            }
            String cellName = StringUtils.isEmpty(defExcelWriterCell.cellName()) ? method.getName()
                    : defExcelWriterCell.cellName();
            ExcelWriterCell excelCellWriter = new ExcelWriterCell();
            excelCellWriter.setCellIndex(defExcelWriterCell.cellIndex());
            excelCellWriter.setCellName(cellName);
            if (defExcelWriterCell.cellWidth() > 0) {
                excelCellWriter.setCellWidth(defExcelWriterCell.cellWidth());
            } else {
                excelCellWriter.setCellWidth(20);
            }
            excelCellWriter.setWrapText(defExcelWriterCell.wrapText());
            excelCellWriter.setCellMethod(method);
            list.add(excelCellWriter);
        }
        Collections.sort(list, new Comparator<ExcelWriterCell>() {
            @Override
            public int compare(ExcelWriterCell o1, ExcelWriterCell o2) {
                // TODO Auto-generated method stub
                return o1.getCellIndex() - o2.getCellIndex();
            }
        });
        return list;
    }

}
