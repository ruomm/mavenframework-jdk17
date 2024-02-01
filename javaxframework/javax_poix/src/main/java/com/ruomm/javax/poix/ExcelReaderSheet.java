/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年8月28日 上午9:35:27
 */
package com.ruomm.javax.poix;

class ExcelReaderSheet {
    /**
     * @return Excel表名称
     */
    private String sheetName;

    /**
     * @return Excel表类型
     */
    private String sheetType;

    /**
     * @return Excel表序号
     */
    private int sheetIndex;

    /**
     * @return Excel读取行开始偏移量，默认0
     */
    private int rowStartOffset;

    /**
     * @return Excel读取行结束偏移量，默认0
     */
    private int rowEndOffset;

    /**
     * @return Excel读取单元行偏移量，默认0
     */
    private int cellOffset;

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

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public int getRowStartOffset() {
        return rowStartOffset;
    }

    public void setRowStartOffset(int rowStartOffset) {
        this.rowStartOffset = rowStartOffset;
    }

    public int getRowEndOffset() {
        return rowEndOffset;
    }

    public void setRowEndOffset(int rowEndOffset) {
        this.rowEndOffset = rowEndOffset;
    }

    public int getCellOffset() {
        return cellOffset;
    }

    public void setCellOffset(int cellOffset) {
        this.cellOffset = cellOffset;
    }

}
