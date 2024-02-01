/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年8月27日 下午1:55:59
 */
package com.ruomm.javax.poix;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

class ExcelWriterSheet {
    /**
     * @return Excel表名称
     */
    private String sheetName;

    /**
     * @return Excel表类型
     */
    private String sheetType;

    /**
     * @return Excel表标题名称
     */
    private String titleName;
    /**
     * @return Excel表标题行数
     */
    private int titleRowNums;
    /**
     * @return Excel表标题行高度
     */
    private int titleRowHeight;
    /**
     * @return Excel表标题字体大小
     */
    private int titleFontSize;
    /**
     * @return Excel表内容行高度
     */
    private int rowHeight;

    /**
     * @return Excel表内容字体大小
     */
    public int rowFontSize;

    /**
     * 标题水平对齐方式
     *
     * @return
     */
    private HorizontalAlignment titleHorizontal;

    /**
     * 标题垂直对齐方式
     *
     * @return
     */
    private VerticalAlignment titleVertical;

    /**
     * 单元格默认水平对齐方式
     *
     * @return
     */
    private HorizontalAlignment cellDefaultHorizontal;

    /**
     * 单元格默认垂直对齐方式
     *
     * @return
     */
    private VerticalAlignment cellDefaultVertical;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetType() {
        return sheetType;
    }

    public void setSheetType(String sheetType) {
        this.sheetType = sheetType;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getTitleRowNums() {
        return titleRowNums;
    }

    public void setTitleRowNums(int titleRowNums) {
        this.titleRowNums = titleRowNums;
    }

    public int getTitleRowHeight() {
        return titleRowHeight;
    }

    public void setTitleRowHeight(int titleRowHeight) {
        this.titleRowHeight = titleRowHeight;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getRowFontSize() {
        return rowFontSize;
    }

    public void setRowFontSize(int rowFontSize) {
        this.rowFontSize = rowFontSize;
    }

    public HorizontalAlignment getTitleHorizontal() {
        return titleHorizontal;
    }

    public void setTitleHorizontal(HorizontalAlignment titleHorizontal) {
        this.titleHorizontal = titleHorizontal;
    }

    public VerticalAlignment getTitleVertical() {
        return titleVertical;
    }

    public void setTitleVertical(VerticalAlignment titleVertical) {
        this.titleVertical = titleVertical;
    }

    public HorizontalAlignment getCellDefaultHorizontal() {
        return cellDefaultHorizontal;
    }

    public void setCellDefaultHorizontal(HorizontalAlignment cellDefaultHorizontal) {
        this.cellDefaultHorizontal = cellDefaultHorizontal;
    }

    public VerticalAlignment getCellDefaultVertical() {
        return cellDefaultVertical;
    }

    public void setCellDefaultVertical(VerticalAlignment cellDefaultVertical) {
        this.cellDefaultVertical = cellDefaultVertical;
    }

    @Override
    public String toString() {
        return "ExcelWriterSheet [sheetName=" + sheetName + ", sheetType=" + sheetType + ", titleName=" + titleName
                + ", titleRowNums=" + titleRowNums + ", titleRowHeight=" + titleRowHeight + ", titleFontSize="
                + titleFontSize + ", rowHeight=" + rowHeight + ", rowFontSize=" + rowFontSize + ", titleHorizontal="
                + titleHorizontal + ", titleVertical=" + titleVertical + ", cellDefaultHorizontal="
                + cellDefaultHorizontal + ", cellDefaultVertical=" + cellDefaultVertical + "]";
    }

}
