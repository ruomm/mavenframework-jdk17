/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年7月20日 上午11:32:55
 */
package com.ruomm.javax.csvx;

import org.apache.commons.csv.CSVFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CsvTest {
    public static void main(String[] args) {
        boolean isHeader = true;
        writeCsv(CSVFormat.RFC4180, isHeader);
        readCsv(CSVFormat.RFC4180, isHeader);
    }

    private static void writeCsv(CSVFormat csvFormat, boolean isHeader) {
        List<Object[]> datas = new ArrayList<Object[]>();
        for (int i = 0; i < 10; i++) {
            Object[] object = new Object[]{"姓名" + i, "性别" + i,
                    new Date(System.currentTimeMillis() - i * 10 * 1000l * 3600l * 24l)};
            datas.add(object);
        }
        if (isHeader) {
            CsvUtil.writeCsv(csvFormat, "D:\\temp\\baidu\\testWithHeader.csv", "GBK",
                    Arrays.asList(new String[]{"姓名", "性别", "年龄"}), datas, false);
        } else {
            CsvUtil.writeCsv(csvFormat, "D:\\temp\\baidu\\testNoHeader.csv", "GBK", null, datas, false);
        }
    }

    private static void readCsv(CSVFormat csvFormat, boolean isHeader) {
        List<String[]> datas = null;
        if (isHeader) {
            datas = CsvUtil.readCsv(csvFormat, "D:\\temp\\baidu\\testWithHeader.csv", "GBK",
                    Arrays.asList(new String[]{"姓名", "性别", "年龄"}), false);
        } else {
            datas = CsvUtil.readCsv(csvFormat, "D:\\temp\\baidu\\testNoHeader.csv", "GBK", 3, false);
        }
//
        for (String[] tmpData : datas) {
            StringBuffer sb = new StringBuffer();
            for (String tmp : tmpData) {

                sb.append(tmp).append("|");
            }
            System.out.println(sb.toString());
        }

    }
}
