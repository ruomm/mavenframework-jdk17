/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年8月26日 下午4:17:28
 */
package com.ruomm.javax.poix;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ExcelWriterCell {
    private int cellIndex;

    private String cellName;
    private Method cellMethod;
    private Field cellField;
    private int cellWidth;
    private boolean wrapText;

    public int getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public Method getCellMethod() {
        return cellMethod;
    }

    public void setCellMethod(Method cellMethod) {
        this.cellMethod = cellMethod;
    }

    public Field getCellField() {
        return cellField;
    }

    public void setCellField(Field cellField) {
        this.cellField = cellField;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public boolean isWrapText() {
        return wrapText;
    }

    public void setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
    }
}
