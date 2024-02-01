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
import com.ruomm.javax.poix.annotation.DefExcelReaderCell;
import com.ruomm.javax.poix.annotation.DefExcelReaderSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelReaderUtil {
    private final static Log log = LogFactory.getLog(ExcelReaderUtil.class);

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

    /**
     * 读取表格数据
     *
     * @param <T>     泛型类型
     * @param pathXls 文件路径
     * @param tCls    对象解析类型
     * @return
     */
    public static <T> List<T> readExcel(String pathXls, Class<T> tCls) {
        try {
            return readExcel(new FileInputStream(pathXls), pathXls, tCls);
        } catch (Exception e) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取错误->pathXls(" + pathXls + ")文件读取错误，无法获取Excel读取的输入流！");
        }

    }

    /**
     * 读取表格数据
     *
     * @param <T>     泛型类型
     * @param fileXls 文件
     * @param tCls    对象解析类型
     * @return
     */
    public static <T> List<T> readExcel(File fileXls, Class<T> tCls) {
        String filePath = null;
        try {
            filePath = fileXls.getPath();
            return readExcel(new FileInputStream(fileXls), filePath, tCls);
        } catch (Exception e) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取错误->fileXls(" + filePath + ")文件读取错误，无法获取Excel读取的输入流！");
        }

    }

    /**
     * 读取表格数据
     *
     * @param <T>         泛型类型
     * @param inputStream 表格数据输入流
     * @param tCls        对象解析类型
     * @return
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> tCls) {
        return readExcel(inputStream, null, tCls);
    }

    /**
     * 读取表格数据
     *
     * @param <T>         泛型类型
     * @param inputStream 表格数据输入流
     * @param sheetType   表格存储类型 xls xlsx
     * @param tCls        对象解析类型
     * @return
     */
    public static <T> List<T> readExcel(InputStream inputStream, String sheetType, Class<T> tCls) {
        if (null == inputStream) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取错误->inputStream为空，无法获取Excel读取的输入流！");
        }
        ExcelReaderSheet excelReaderSheet = getExcelReaderSheet(tCls, null);
        List<ExcelReaderCell> listCells = getListExcelReaderCell(tCls, excelReaderSheet.getCellOffset());
        if (null == listCells || listCells.size() <= 0) {
            log.error("Error:readExcel->没有DefExcelCell注解或DefExcelCell注解的cellIndex不合法");
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取设置错误->没有DefExcelCell注解或DefExcelCell注解的cellIndex不合法");
        }
        List<T> list = null;
        Workbook wb = null;
        try {
            String realSheetType = ExcelConfig.parseSheetType(excelReaderSheet.getSheetType(), sheetType);
            wb = getWorkbok(realSheetType, inputStream);
            Sheet sheet = null;
            // 若是SheetName不为空则取sheetName的表格否则取第一个表格
            if (!StringUtils.isEmpty(excelReaderSheet.getSheetName())) {
                sheet = wb.getSheet(excelReaderSheet.getSheetName());
                if (null == sheet) {
                    if (excelReaderSheet.getSheetIndex() >= 0) {
                        int numberOfSheets = wb.getNumberOfSheets();
                        if (excelReaderSheet.getSheetIndex() >= numberOfSheets) {
                            log.error("Error:readExcel->" + "读取设置错误->sheet数量为" + numberOfSheets + "，声明的序号"
                                    + excelReaderSheet.getSheetIndex() + "超界");
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取设置错误->sheet数量为"
                                    + numberOfSheets + "，声明的序号" + excelReaderSheet.getSheetIndex() + "超界");
                        }
                        sheet = wb.getSheetAt(excelReaderSheet.getSheetIndex());
                    } else {
                        int numberOfSheets = wb.getNumberOfSheets();
                        if (numberOfSheets != 1) {
                            log.error("Error:readExcel->" + "读取设置错误->excle的sheet数量为" + numberOfSheets
                                    + "，超过1个，必须声明sheet的名称或序号");
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取设置错误->excle的sheet数量为"
                                    + numberOfSheets + "，超过1个，必须声明sheet的名称或序号");
                        } else {
                            sheet = wb.getSheetAt(0);
                        }
                    }
                }
            } else if (excelReaderSheet.getSheetIndex() >= 0) {
                int numberOfSheets = wb.getNumberOfSheets();
                if (excelReaderSheet.getSheetIndex() >= numberOfSheets) {
                    log.error("Error:readExcel->" + "读取设置错误->sheet数量为" + numberOfSheets + "，声明的序号"
                            + excelReaderSheet.getSheetIndex() + "超界");
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取设置错误->sheet数量为"
                            + numberOfSheets + "，声明的序号" + excelReaderSheet.getSheetIndex() + "超界");
                }
                sheet = wb.getSheetAt(excelReaderSheet.getSheetIndex());
            } else {
                int numberOfSheets = wb.getNumberOfSheets();
                if (numberOfSheets != 1) {
                    log.error(
                            "Error:readExcel->" + "读取设置错误->excle的sheet数量为" + numberOfSheets + "，超过1个，必须声明sheet的名称或序号");
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取设置错误->excle的sheet数量为"
                            + numberOfSheets + "，超过1个，必须声明sheet的名称或序号");
                } else {
                    sheet = wb.getSheetAt(0);
                }
            }
            if (null == sheet) {
                log.error("Error:readExcel->" + "读取设置错误->无法依据DefExcelReaderSheet注解获取到excel表单，无法读取！");
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "readExcel->" + "读取设置错误->无法依据DefExcelReaderSheet注解获取到excel表单，无法读取！");
            }
            int firstRowIndex = sheet.getFirstRowNum() + excelReaderSheet.getRowStartOffset(); // 得到第一行的下标，此处加2目的是为了避免读取活动首行
            int lastRowIndex = sheet.getLastRowNum() - excelReaderSheet.getRowEndOffset(); // 得到最后一行的下标 ，他们之差代表总行数
            if (firstRowIndex > lastRowIndex) {
                log.error("Error:readExcel->" + "读取设置错误->excel读取的开始行号大于结束行号数");
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->" + "读取设置错误->excel读取的开始行号大于结束行号数");
            }
            list = new ArrayList<T>();
            for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                T t = tCls.newInstance();
                Row row = sheet.getRow(rIndex);
                int firstCellIndex = row.getFirstCellNum(); // 得到第一列
                int lastCellIndex = row.getLastCellNum(); // 得到最后一列，他们之差是总列数
                boolean isSkip = false;
                boolean isEnd = false;
                boolean isThrowErr = false;
                int errorIndex = 0;
                for (ExcelReaderCell excelReaderCell : listCells) {
                    if (excelReaderCell.getCellIndex() < firstCellIndex
                            || excelReaderCell.getCellIndex() > lastCellIndex) {
                        // 若是超过边界自动跳过
                        continue;
                    }
                    Cell cell = row.getCell(excelReaderCell.getCellIndex());
                    CellWriteResult cellWriteResult = writeCellToObjcet(excelReaderCell, cell, t, rIndex);
                    if (null != cellWriteResult && cellWriteResult.isSkip()) {
                        errorIndex = excelReaderCell.getCellIndex();
                        isSkip = true;
                        continue;
                    }
                    if (null != cellWriteResult && cellWriteResult.isEnd()) {
                        errorIndex = excelReaderCell.getCellIndex();
                        isSkip = true;
                        isEnd = true;
                        break;
                    }
                    if (null != cellWriteResult && cellWriteResult.isThrowErr()) {
                        errorIndex = excelReaderCell.getCellIndex();
                        isSkip = true;
                        isEnd = true;
                        isThrowErr = true;
                        break;
                    }
                }
                if (isThrowErr) {
                    list.clear();
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析" + rIndex + "行" + errorIndex + "列"
                            + "数据时候发生异常，结束Excel解析，抛出异常，不返回解析结果！");
                } else if (isEnd) {
                    break;
                } else if (isSkip) {
                    continue;
                } else {
                    list.add(t);
                }
            }

        } catch (IOException | InstantiationException | IllegalAccessException e) {
            // TODO: handle exception
            log.error("Error:readExcel", e);
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->读取Excel文件时候发生异常");
        } catch (JavaxCorexException e) {
            // TODO: handle exception
            throw e;
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    log.error("Error:ExcelReaderUtil.readExcel", e2);
                }
            }
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:ExcelReaderUtil.readExcel", e);
                }
            }

        }
        return list;
    }

    public static int getMinCellIndex(List<ExcelReaderCell> listExcelReaderCell) {
//		int min = 0;
//		for (ExcelReaderCell tmpCell : listExcelReaderCell) {
//			if (tmpCell.getCellIndex() < min) {
//				min = tmpCell.getCellIndex();
//			}
//		}
//		return min;
        return listExcelReaderCell.get(0).getCellIndex();
    }

    public static int getMaxCellIndex(List<ExcelReaderCell> listExcelReaderCell) {
//		int max = 0;
//		for (ExcelReaderCell tmpCell : listExcelReaderCell) {
//			if (tmpCell.getCellIndex() > max) {
//				max = tmpCell.getCellIndex();
//			}
//		}
//		return max;
        int maxIndex = listExcelReaderCell.size() - 1;
        return listExcelReaderCell.get(maxIndex).getCellIndex();
    }

    private static <T> CellWriteResult writeCellToObjcet(ExcelReaderCell excelCell, Cell cell, T t, int rowIndex) {
        CellWriteResult writeResult = new CellWriteResult();

        writeResult.setSkip(false);
        writeResult.setEnd(false);
        writeResult.setThrowErr(false);
        String cellTag = rowIndex + "行" + (null == excelCell ? "" : excelCell.getCellIndex() + "列");
        if (null == excelCell) {
            return writeResult;
        } else if (null == cell || null == t) {
            log.error("Error:CellWriteResult->ExcelReaderCell:" + excelCell.toString());
            if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.SKIP) {
                writeResult.setSkip(true);
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候cell值为空或对象t为空，跳过改行解析，继续解析其他行数据！");
            } else if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.END) {
                writeResult.setSkip(true);
                writeResult.setEnd(true);
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候cell值为空或对象t为空，结束Excel解析，返回部分解析结果！");
            } else if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.THROW) {
                writeResult.setSkip(true);
                writeResult.setEnd(true);
                writeResult.setThrowErr(true);
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候cell值为空或对象t为空，结束Excel解析，抛出异常，不返回解析结果！");
            } else {
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候cell值为空或对象t为空，跳过该单元解析，继续解析该行其他单元数据！");
            }
            return writeResult;
        }
        try {
            Class<?> cellFieldType = invokeGetCellType(excelCell);
            CellType cellType = cell.getCellType();
            if (cellType == CellType.NUMERIC) {
                double cellVal = cell.getNumericCellValue();
                if (cellFieldType == int.class || cellFieldType == Integer.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                    invokeSetCellValue(excelCell, t, bigDecimal.intValue());
                } else if (cellFieldType == long.class || cellFieldType == Long.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                    invokeSetCellValue(excelCell, t, bigDecimal.longValue());
                } else if (cellFieldType == short.class || cellFieldType == Short.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                    invokeSetCellValue(excelCell, t, bigDecimal.shortValue());
                } else if (cellFieldType == byte.class || cellFieldType == Byte.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                    invokeSetCellValue(excelCell, t, bigDecimal.byteValue());
                } else if (cellFieldType == float.class || cellFieldType == Float.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal);
                    if (excelCell.getNumScale() >= 0) {
                        bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                    }
                    invokeSetCellValue(excelCell, t, bigDecimal.floatValue());
                } else if (cellFieldType == double.class || cellFieldType == Double.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal);
                    if (excelCell.getNumScale() >= 0) {
                        bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                    }
                    invokeSetCellValue(excelCell, t, bigDecimal.doubleValue());
                } else if (cellFieldType == boolean.class || cellFieldType == Boolean.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                    boolean tmpIntVal = bigDecimal.intValue() > 0 ? true : false;
                    invokeSetCellValue(excelCell, t, tmpIntVal);
                } else if (cellFieldType == String.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal);
                    if (excelCell.getNumScale() >= 0) {
                        bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                    }
                    invokeSetCellValue(excelCell, t, bigDecimal.toPlainString());
                } else if (cellFieldType == StringBuilder.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal);
                    if (excelCell.getNumScale() >= 0) {
                        bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                    }
                    invokeSetCellValue(excelCell, t, new StringBuilder().append(bigDecimal.toPlainString()));
                } else if (cellFieldType == StringBuffer.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal);
                    if (excelCell.getNumScale() >= 0) {
                        bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                    }
                    invokeSetCellValue(excelCell, t, new StringBuffer().append(bigDecimal.toPlainString()));
                } else if (cellFieldType == Date.class) {
                    BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                    if (StringUtils.isEmpty(excelCell.getCellFormat())) {
                        invokeSetCellValue(excelCell, t, new Date(bigDecimal.longValue()));
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(excelCell.getCellFormat());
                        invokeSetCellValue(excelCell, t, simpleDateFormat.parse(bigDecimal.toPlainString()));
                    }
                }

            } else if (cellType == CellType.BOOLEAN) {
                boolean cellVal = cell.getBooleanCellValue();
                if (cellFieldType == int.class || cellFieldType == Integer.class) {
                    invokeSetCellValue(excelCell, t, cellVal ? 1 : 0);
                } else if (cellFieldType == long.class || cellFieldType == Long.class) {
                    invokeSetCellValue(excelCell, t, cellVal ? 1l : 0);
                } else if (cellFieldType == boolean.class || cellFieldType == Boolean.class) {
                    invokeSetCellValue(excelCell, t, cellVal);
                } else if (cellFieldType == String.class) {
                    invokeSetCellValue(excelCell, t, cellVal ? "true" : "false");
                } else if (cellFieldType == StringBuilder.class) {
                    invokeSetCellValue(excelCell, t, new StringBuilder().append(cellVal ? "true" : "false"));
                } else if (cellFieldType == StringBuffer.class) {
                    invokeSetCellValue(excelCell, t, new StringBuffer().append(cellVal ? "true" : "false"));
                }
            } else if (cellType == CellType.STRING || cellType == CellType.FORMULA) {
                String cellVal = null;
                if (cellType == CellType.FORMULA) {
                    cellVal = cell.getCellFormula();
                } else {
                    cellVal = cell.getStringCellValue();
                }
                if (!StringUtils.isEmpty(cellVal)) {
                    if (excelCell.isStringTrim()) {
                        cellVal = cellVal.trim();
                    }
                    if (!StringUtils.isEmpty(excelCell.getSuffixStart()) && cellVal.startsWith(excelCell.getSuffixStart())) {
                        cellVal = cellVal.substring(excelCell.getSuffixStart().length());
                    }
                    if (!StringUtils.isEmpty(excelCell.getSuffixEnd()) && cellVal.endsWith(excelCell.getSuffixEnd())) {
                        int length = cellVal.length();
                        cellVal = cellVal.substring(0, length - excelCell.getSuffixEnd().length());
                    }
                    if (excelCell.isStringTrim()) {
                        cellVal = cellVal.trim();
                    }
                }
                if (cellFieldType == int.class || cellFieldType == Integer.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Integer.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为int类型时候值不能为空！");
                        }
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                        invokeSetCellValue(excelCell, t, bigDecimal.intValue());
                    }
                } else if (cellFieldType == long.class || cellFieldType == Long.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Long.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为long类型时候值不能为空！");
                        }
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                        invokeSetCellValue(excelCell, t, bigDecimal.longValue());
                    }
                } else if (cellFieldType == short.class || cellFieldType == Short.class) {

                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Short.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为short类型时候值不能为空！");
                        }
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                        invokeSetCellValue(excelCell, t, bigDecimal.shortValue());
                    }
                } else if (cellFieldType == byte.class || cellFieldType == Byte.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Byte.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为byte类型时候值不能为空！");
                        }
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                        invokeSetCellValue(excelCell, t, bigDecimal.byteValue());
                    }
                } else if (cellFieldType == float.class || cellFieldType == Float.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Float.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为float类型时候值不能为空！");
                        }
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(cellVal);
                        if (excelCell.getNumScale() >= 0) {
                            bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                        }
                        invokeSetCellValue(excelCell, t, bigDecimal.floatValue());
                    }
                } else if (cellFieldType == double.class || cellFieldType == Double.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Double.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为double类型时候值不能为空！");
                        }
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(cellVal);
                        if (excelCell.getNumScale() >= 0) {
                            bigDecimal = bigDecimal.setScale(excelCell.getNumScale(), excelCell.getNumRoundingMode());
                        }
                        invokeSetCellValue(excelCell, t, bigDecimal.doubleValue());
                    }
                } else if (cellFieldType == boolean.class || cellFieldType == Boolean.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        if (cellFieldType == Boolean.class) {
                            invokeSetCellValue(excelCell, t, null);
                        } else {
                            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "CellWriteResult->解析为boolean类型时候值不能为空！");
                        }
                    } else {
                        boolean tmpBooleanVal = false;
                        if (cellVal.equalsIgnoreCase("true") || cellVal.equalsIgnoreCase("1")) {
                            tmpBooleanVal = true;
                        } else if (cellVal.equalsIgnoreCase("false") || cellVal.equalsIgnoreCase("0")) {
                            tmpBooleanVal = false;
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                            tmpBooleanVal = bigDecimal.intValue() > 0 ? true : false;
                        }
                        invokeSetCellValue(excelCell, t, tmpBooleanVal);
                    }
                } else if (cellFieldType == String.class) {
                    invokeSetCellValue(excelCell, t, cellVal);
                } else if (cellFieldType == StringBuilder.class) {
                    if (null == cellVal) {
                        invokeSetCellValue(excelCell, t, null);
                    } else if (null != cellVal && cellVal.length() > 0) {
                        invokeSetCellValue(excelCell, t, new StringBuilder().append(cellVal));

                    } else if (null != cellVal) {
                        invokeSetCellValue(excelCell, t, new StringBuilder());
                    }
                } else if (cellFieldType == StringBuffer.class) {
                    if (null == cellVal) {
                        invokeSetCellValue(excelCell, t, null);
                    } else if (null != cellVal && cellVal.length() > 0) {
                        invokeSetCellValue(excelCell, t, new StringBuffer().append(cellVal));
                    } else if (null != cellVal) {
                        invokeSetCellValue(excelCell, t, new StringBuffer());
                    }
                } else if (cellFieldType == Date.class) {
                    if (StringUtils.isEmpty(cellVal)) {
                        invokeSetCellValue(excelCell, t, null);
                    } else if (StringUtils.isEmpty(excelCell.getCellFormat())) {
                        BigDecimal bigDecimal = new BigDecimal(cellVal).setScale(0, excelCell.getNumRoundingMode());
                        invokeSetCellValue(excelCell, t, new Date(bigDecimal.longValue()));
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(excelCell.getCellFormat());
                        invokeSetCellValue(excelCell, t, simpleDateFormat.parse(cellVal));
                    }
                }

            } else {
                if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.SKIP) {
                    writeResult.setSkip(true);
                } else if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.END) {
                    writeResult.setSkip(true);
                    writeResult.setEnd(true);
                } else if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.THROW) {
                    writeResult.setSkip(true);
                    writeResult.setEnd(true);
                    writeResult.setThrowErr(true);
                }
            }
            return writeResult;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:CellWriteResult->ExcelReaderCell:" + excelCell.toString());
            if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.SKIP) {
                writeResult.setSkip(true);
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候发生异常，跳过改行解析，继续解析其他行数据！", e);
            } else if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.END) {
                writeResult.setSkip(true);
                writeResult.setEnd(true);
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候发生异常，结束Excel解析，返回部分解析结果！", e);
            } else if (null != excelCell.getErrorMode() && excelCell.getErrorMode() == CellEmptyError.THROW) {
                writeResult.setSkip(true);
                writeResult.setEnd(true);
                writeResult.setThrowErr(true);
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候发生异常，结束Excel解析，抛出异常，不返回解析结果！", e);
            } else {
                log.error("Error:CellWriteResult->解析" + cellTag + "数据时候发生异常，跳过该单元解析，继续解析该行其他单元数据！", e);
            }
            return writeResult;
        }

    }

    private static Class<?> invokeGetCellType(ExcelReaderCell excelReaderCell) {
        if (null != excelReaderCell.getCellFiled()) {
            return excelReaderCell.getCellFiled().getType();
        } else {
            return excelReaderCell.getCellMethod().getParameterTypes()[0];
        }
    }

    private static void invokeSetCellValue(ExcelReaderCell excelReaderCell, Object obj, Object value)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (null != excelReaderCell.getCellFiled()) {
            excelReaderCell.getCellFiled().setAccessible(true);
            excelReaderCell.getCellFiled().set(obj, value);
        } else {
            excelReaderCell.getCellMethod().setAccessible(true);
            excelReaderCell.getCellMethod().invoke(obj, value);
        }
    }

    //	 读取单元格
    public static List<Map<String, String>> readExcel(InputStream inputStream, String suffix, String sheetName,
                                                      Integer index, int startOffset, int endOffset, int cellOffset, String[] headers) {
        Workbook wb = null;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            if (ExcelConfig.EXCEL_XLS.equalsIgnoreCase(suffix)) {
                wb = new HSSFWorkbook(inputStream); // 解析xls格式
            } else if (ExcelConfig.EXCEL_XLSX.equalsIgnoreCase(suffix)) {
                wb = new XSSFWorkbook(inputStream); // 解析xlsx格式
            } else if (ExcelConfig.EXCEL_XLSX.equalsIgnoreCase(ExcelConfig.EXCEL_DEFAULT)) {
                wb = new XSSFWorkbook(inputStream); // 解析xlsx格式
            } else {
                wb = new HSSFWorkbook(inputStream); // 解析xls格式
            }
            Sheet sheet = wb.getSheet(sheetName);
            wb.getNumberOfSheets();
            // 若是SheetName不为空则取sheetName的表格否则取第一个表格
            if (!StringUtils.isEmpty(sheetName)) {
                sheet = wb.getSheet(sheetName);
            } else if (null != index && index >= 0) {
                int numberOfSheets = wb.getNumberOfSheets();
                if (index >= numberOfSheets) {
                    log.error("Error:readExcel->" + "读取错误，excle的sheet数量为" + numberOfSheets + "，声明的序号" + index + "超界");
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->读取错误，excle的sheet数量为" + numberOfSheets
                            + "，声明的序号" + index + "超界");
                }
                sheet = wb.getSheetAt(index);
            } else {

                int numberOfSheets = wb.getNumberOfSheets();
                if (numberOfSheets != 1) {
                    log.error("Error:readExcel->" + "读取错误，excle的sheet数量为" + numberOfSheets + "，超过1个，必须声明sheet的名称或序号");
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->读取错误，excle的sheet数量为" + numberOfSheets
                            + "，超过1个，必须声明sheet的名称或序号");
                } else {
                    sheet = wb.getSheetAt(0);
                }
                sheet = wb.getSheetAt(0);
            }
            int firstRowOffset = startOffset <= 0 ? 0 : startOffset;
            int lastRowOffset = endOffset <= 0 ? 0 : endOffset;
            int firstRowIndex = sheet.getFirstRowNum() + firstRowOffset; // 得到第一行的下标，此处加2目的是为了避免读取活动首行
            int lastRowIndex = sheet.getLastRowNum() - lastRowOffset; // 得到最后一行的下标 ，他们之差代表总行数
            if (firstRowIndex > lastRowIndex) {
                log.error("Error:readExcel->" + "读取错误，读取excel文件开始行号大于结束行号数");
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->读取错误，读取excel文件开始行号大于结束行号数");
            }
            for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                Row row = sheet.getRow(rIndex);
                int firstCellOffset = cellOffset <= 0 ? 0 : cellOffset;
                int firstCellIndex = row.getFirstCellNum() + firstCellOffset; // 得到第一列
                int lastCellIndex = row.getLastCellNum(); // 得到最后一列，他们之差是总列数
                if (lastCellIndex - firstCellIndex + 1 != headers.length) {// 表头列不对应
                    log.error("Error:readExcel->" + "Error:ExcelReaderUtil.readExcel->读取excel文件时，文件第" + (rIndex + 1)
                            + "行数据列数与映射属性个数不相符");
                    // throw new Exception(Error:ExcelUtil.readExcel->+"读取excel文件时，文件第" + (rIndex + 1) +
                    // "行数据列数与映射属性个数不相符");
                }
                if (firstCellIndex > lastCellIndex) {
                    log.error("Error:readExcel->读取错误，读取exlce单元行时候开始列号大于结束列号");
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ExcelReaderUtil.readExcel->读取错误，读取exlce单元行时候开始列号大于结束列号");
                }
                int headersSize = headers.length;
                Map<String, String> dfJson = new HashMap<String, String>(); // 每次循环新建一个对象接收
                for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {
                    int hIndex = cIndex - firstCellIndex;
                    if (hIndex >= headersSize) {
                        log.debug(
                                "Error:readExcel" + "读取excle第" + rIndex + "行在" + cIndex + "列结束，声明的headers长度不足，继续读取下一行");
                        break;
                    }
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        CellType cellType = cell.getCellType();
                        if (cellType == CellType.STRING) {
                            dfJson.put(headers[hIndex], cell.getStringCellValue());
                        } else if (cellType == CellType.NUMERIC) {
                            BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
                            dfJson.put(headers[hIndex], bigDecimal.toPlainString());
                        } else if (cellType == CellType.BOOLEAN) {
                            dfJson.put(headers[hIndex], String.valueOf(cell.getBooleanCellValue()));
                        } else if (cellType == CellType.FORMULA) {
                            dfJson.put(headers[hIndex], String.valueOf(cell.getCellFormula()));
                        } else if (cellType == CellType.BLANK) {
                            dfJson.put(headers[hIndex], "");
                        } else {
                            dfJson.put(headers[hIndex], cell.toString());
                        }

                    }
                }
                list.add(dfJson);// 循环完成一次则将一个对象放入集合一次，直到循环结束
            }

        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:readExcel", e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    log.error("Error:readExcel->关闭Excel文件异常", e);
                }
            }
        }
        return list;

    }

    private static ExcelReaderSheet getExcelReaderSheet(Class<?> cls, String pathXls) {
        DefExcelReaderSheet def = cls.getAnnotation(DefExcelReaderSheet.class);
        ExcelReaderSheet excelReaderSheet = new ExcelReaderSheet();
        if (null == def) {
            excelReaderSheet.setSheetName(null);
            excelReaderSheet.setSheetIndex(-1);
            excelReaderSheet.setSheetType(ExcelConfig.parseSheetType(null, pathXls));
            excelReaderSheet.setCellOffset(0);
            excelReaderSheet.setRowStartOffset(0);
            excelReaderSheet.setRowEndOffset(0);
        } else {
            if (StringUtils.isEmpty(def.sheetName())) {
                excelReaderSheet.setSheetName(null);
                excelReaderSheet.setSheetIndex(def.sheetIndex());
            } else {
                excelReaderSheet.setSheetName(def.sheetName());
                excelReaderSheet.setSheetIndex(def.sheetIndex());
            }
            excelReaderSheet.setSheetType(ExcelConfig.parseSheetType(def.sheetType(), pathXls));
            excelReaderSheet.setCellOffset(def.cellOffset());
            excelReaderSheet.setRowStartOffset(def.rowStartOffset());
            excelReaderSheet.setRowEndOffset(def.rowEndOffset());
        }
        return excelReaderSheet;
    }

    private static List<ExcelReaderCell> getListExcelReaderCell(Class<?> cls, int cellOffset) {
        List<ExcelReaderCell> listCells = new ArrayList<ExcelReaderCell>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            DefExcelReaderCell defExcelReaderCell = field.getAnnotation(DefExcelReaderCell.class);
            if (null == defExcelReaderCell || defExcelReaderCell.cellIndex() + cellOffset < 0) {
                continue;
            }
            // 常量跳过
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            // 静态跳过
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ExcelReaderCell excelCell = new ExcelReaderCell();
            excelCell.setCellFiled(field);
            excelCell.setCellIndex(defExcelReaderCell.cellIndex() + cellOffset);
            excelCell.setCellFormat(defExcelReaderCell.cellFormat());
            excelCell.setNumScale(defExcelReaderCell.numScale());
            excelCell.setNumRoundingMode(getNumRoundingMode(defExcelReaderCell.numRoundingMode()));
            excelCell.setSuffixStart(defExcelReaderCell.suffixStart());
            excelCell.setSuffixEnd(defExcelReaderCell.suffixEnd());
            excelCell.setStringTrim(defExcelReaderCell.isStringTrim());
            excelCell.setErrorMode(defExcelReaderCell.errorMode());
            listCells.add(excelCell);
        }
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            // 静态跳过
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            DefExcelReaderCell defExcelReaderCell = method.getAnnotation(DefExcelReaderCell.class);
            if (null == defExcelReaderCell || defExcelReaderCell.cellIndex() + cellOffset < 0) {
                continue;
            }
            ExcelReaderCell excelCell = new ExcelReaderCell();
            excelCell.setCellMethod(method);
            excelCell.setCellIndex(defExcelReaderCell.cellIndex() + cellOffset);
            excelCell.setCellFormat(defExcelReaderCell.cellFormat());
            excelCell.setNumScale(defExcelReaderCell.numScale());
            excelCell.setNumRoundingMode(getNumRoundingMode(defExcelReaderCell.numRoundingMode()));
            excelCell.setSuffixStart(defExcelReaderCell.suffixStart());
            excelCell.setSuffixEnd(defExcelReaderCell.suffixEnd());
            excelCell.setStringTrim(defExcelReaderCell.isStringTrim());
            excelCell.setErrorMode(defExcelReaderCell.errorMode());
            listCells.add(excelCell);
        }
        Collections.sort(listCells, new Comparator<ExcelReaderCell>() {
            @Override
            public int compare(ExcelReaderCell o1, ExcelReaderCell o2) {
                // TODO Auto-generated method stub
                return o1.getCellIndex() - o2.getCellIndex();
            }
        });
        return listCells;
    }

    private static int getNumRoundingMode(int numRounding) {

        if (numRounding == BigDecimal.ROUND_CEILING || numRounding == BigDecimal.ROUND_DOWN
                || numRounding == BigDecimal.ROUND_FLOOR || numRounding == BigDecimal.ROUND_HALF_DOWN
                || numRounding == BigDecimal.ROUND_HALF_EVEN || numRounding == BigDecimal.ROUND_HALF_UP
                || numRounding == BigDecimal.ROUND_UNNECESSARY || numRounding == BigDecimal.ROUND_UP) {
            return numRounding;
        } else {
            return BigDecimal.ROUND_HALF_UP;
        }
    }
}
