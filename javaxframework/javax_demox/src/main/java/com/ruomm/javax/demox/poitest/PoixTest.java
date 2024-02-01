package com.ruomm.javax.demox.poitest;

import com.ruomm.javax.basex.NameUtils;
import com.ruomm.javax.demox.poitest.dal.ExcelDal;
import com.ruomm.javax.poix.ExcelReaderUtil;
import com.ruomm.javax.poix.ExcelWriterUtil;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/17 11:18
 */
public class PoixTest {
    private final static String XLS_PATH = "D:\\temp\\poix\\my2.xlsx";

    public static void main(String[] args) {
        readExcle();
    }

    private static void readExcle() {
        try {
            List<ExcelDal> list = ExcelReaderUtil.readExcel(new FileInputStream(XLS_PATH), ExcelDal.class);
            System.out.print(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeExcle() {
        List<ExcelDal> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExcelDal excelDal = new ExcelDal();
            excelDal.setName(NameUtils.generateName());
            excelDal.setSex(NameUtils.generateContent(1));
            list.add(excelDal);
        }
        ExcelWriterUtil.writerExcel(XLS_PATH, list, ExcelDal.class);
    }
}
