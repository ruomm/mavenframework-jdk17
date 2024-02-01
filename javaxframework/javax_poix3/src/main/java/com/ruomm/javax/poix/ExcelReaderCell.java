/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月13日 上午11:33:21
 */
package com.ruomm.javax.poix;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ExcelReaderCell {
    private Field cellFiled;
    private Method cellMethod;

    // cell序号
    private int cellIndex;

    // Date类型日期，number类型 plain/scale/none
    private String cellFormat;

    // 四舍五入规则
    private int numRoundingMode;

    // 保留小数位数
    private int numScale;

    // 去除前置头
    private String suffixStart;

    // 去除尾部头
    private String suffixEnd;

    // 是否去除字符串开始结束空白
    private boolean isStringTrim;

    // 读取cell异常执行的逻辑
    private CellEmptyError errorMode;

    public Method getCellMethod() {
        return cellMethod;
    }

    public void setCellMethod(Method cellMethod) {
        this.cellMethod = cellMethod;
    }

    public Field getCellFiled() {
        return cellFiled;
    }

    public void setCellFiled(Field cellFiled) {
        this.cellFiled = cellFiled;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getCellFormat() {
        return cellFormat;
    }

    public void setCellFormat(String cellFormat) {
        this.cellFormat = cellFormat;
    }

    public int getNumRoundingMode() {
        return numRoundingMode;
    }

    public void setNumRoundingMode(int numRoundingMode) {
        this.numRoundingMode = numRoundingMode;
    }

    public int getNumScale() {
        return numScale;
    }

    public void setNumScale(int numScale) {
        this.numScale = numScale;
    }

    public String getSuffixStart() {
        return suffixStart;
    }

    public void setSuffixStart(String suffixStart) {
        this.suffixStart = suffixStart;
    }

    public String getSuffixEnd() {
        return suffixEnd;
    }

    public void setSuffixEnd(String suffixEnd) {
        this.suffixEnd = suffixEnd;
    }

    public boolean isStringTrim() {
        return isStringTrim;
    }

    public void setStringTrim(boolean isStringTrim) {
        this.isStringTrim = isStringTrim;
    }

    public CellEmptyError getErrorMode() {
        return errorMode;
    }

    public void setErrorMode(CellEmptyError errorMode) {
        this.errorMode = errorMode;
    }

    @Override
    public String toString() {
        return "ExcelReaderCell [cellFiled=" + cellFiled + ", cellMethod=" + cellMethod + ", cellIndex=" + cellIndex
                + ", cellFormat=" + cellFormat + ", numRoundingMode=" + numRoundingMode + ", numScale=" + numScale
                + ", suffixStart=" + suffixStart + ", suffixEnd=" + suffixEnd + ", isStringTrim=" + isStringTrim
                + ", errorMode=" + errorMode + "]";
    }

}
