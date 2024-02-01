package com.ruomm.javax.demox.poitest.dal;

import com.ruomm.javax.poix.annotation.DefExcelReaderCell;
import com.ruomm.javax.poix.annotation.DefExcelReaderSheet;
import com.ruomm.javax.poix.annotation.DefExcelWriterCell;
import com.ruomm.javax.poix.annotation.DefExcelWtiterSheet;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/17 11:19
 */
@DefExcelWtiterSheet(sheetName = "你好", sheetType = "xlsx", titleName = "性别表", titleRowNums = 2)
@DefExcelReaderSheet(sheetName = "你好", sheetType = "xlsx", rowStartOffset = 1, rowEndOffset = 0)
public class ExcelDal {
    @DefExcelReaderCell(cellIndex = 0)
    @DefExcelWriterCell(cellName = "姓名", cellIndex = 0, cellWidth = 30)
    private String name;
    @DefExcelReaderCell(cellIndex = 1)
    @DefExcelWriterCell(cellName = "性别", cellIndex = 1, cellWidth = 30)
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "ExcelDal{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
