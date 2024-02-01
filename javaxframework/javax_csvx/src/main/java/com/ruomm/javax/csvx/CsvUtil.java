/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月16日 下午3:11:08
 */
package com.ruomm.javax.csvx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private final static Log log = LogFactory.getLog(CsvUtil.class);
    private final static String DEFAULT_CHASET_NAME = "UTF-8";

    public static void writeCsv(CSVFormat csvFormat, String csvFileName, String charsetName, List<String> headersData,
                                List<Object[]> datas, boolean isAppend) {

        boolean isHeader = null != headersData && headersData.size() > 0;
        boolean isData = null != datas && datas.size() > 0;
        if (!isHeader && !isData) {
            return;
        }
        CSVPrinter csvPrinter = null;
        BufferedWriter outWriter = null;
        try {
            outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName, isAppend),
                    parseRealCharsetName(charsetName, DEFAULT_CHASET_NAME)));
//			PrintWriter printWriter = new PrintWriter(new File(csvFileName),
//					parseRealCharsetName(charsetName, DEFAULT_CHASET_NAME));
            CSVFormat parserFormat = null == csvFormat ? CSVFormat.EXCEL : csvFormat;
            if (isHeader) {
                csvPrinter = parserFormat.withHeader(headersData.toArray(new String[headersData.size()]))
                        .print(outWriter);
            } else {
                csvPrinter = parserFormat.print(outWriter);
            }
            for (int i = 0; i < datas.size(); i++) {
                csvPrinter.printRecord(datas.get(i));
            }
            csvPrinter.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error:writeCsv", e);
        } // 指定GBK,解决Microsoft不兼容
        finally {

            try {
                if (null != csvPrinter) {
                    csvPrinter.close();
                    csvPrinter = null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("Error:writeCsv", e);
            }
            try {
                if (null != outWriter) {
                    outWriter.close();
                    outWriter = null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("Error:writeCsv", e);
            }
        }
    }

    public static List<String[]> readCsv(CSVFormat csvFormat, String csvFileName, String charsetName, int headersSize,
                                         boolean isFirstRecordAsHeader) {
        List<String[]> datas = null;
        Reader reader = null;
        try {
            CSVFormat parserFormat = null == csvFormat ? CSVFormat.EXCEL : csvFormat;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName),
                    parseRealCharsetName(charsetName, DEFAULT_CHASET_NAME)));
//			reader = new FileReader(csvFileName);
            Iterable<CSVRecord> records = null;

            if (isFirstRecordAsHeader) {
                records = parserFormat.withFirstRecordAsHeader().parse(reader);
            } else {
                records = parserFormat.parse(reader);
            }
            datas = new ArrayList<String[]>();
            for (CSVRecord csvRecord : records) {// 第一行不会被打印出来
                String[] tmpData = new String[headersSize];
                for (int i = 0; i < headersSize; i++) {
                    tmpData[i] = csvRecord.get(i);
                }
                datas.add(tmpData);
            }
        } catch (Exception e) {
            // TODO: handle exception
            reader = null;
            log.error("Error:readCsv", e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (Exception e) {
                log.error("Error:readCsv", e);
            }
        }
        return datas;
    }

    public static List<String[]> readCsv(CSVFormat csvFormat, String csvFileName, String charsetName,
                                         List<String> headersData, boolean isFirstRecordAsHeader) {
        List<String[]> datas = null;
        Reader reader = null;
        try {
            CSVFormat parserFormat = null == csvFormat ? CSVFormat.EXCEL : csvFormat;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName),
                    parseRealCharsetName(charsetName, DEFAULT_CHASET_NAME)));
//			reader = new FileReader(csvFileName);
            Iterable<CSVRecord> records = null;
            if (isFirstRecordAsHeader) {
                records = parserFormat.withFirstRecordAsHeader()
                        .withHeader(headersData.toArray(new String[headersData.size()])).parse(reader);
            } else {
                records = parserFormat.withHeader(headersData.toArray(new String[headersData.size()])).parse(reader);
            }

            datas = new ArrayList<String[]>();
            for (CSVRecord csvRecord : records) {// 第一行不会被打印出来
                String[] tmpData = new String[headersData.size()];
                for (int i = 0; i < headersData.size(); i++) {
                    tmpData[i] = csvRecord.get(headersData.get(i));
                }
                datas.add(tmpData);
            }
        } catch (Exception e) {
            // TODO: handle exception
            reader = null;
            log.error("Error:readCsv", e);
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (Exception e) {
                log.error("Error:readCsv", e);
            }
        }
        return datas;
    }

    private static String parseRealCharsetName(String charsetName, String defaultCharsetName) {
        String realCharsetName = null;
        try {
            if (null == charsetName || charsetName.length() <= 0 || !Charset.isSupported(charsetName)) {
                realCharsetName = null;
            } else {
                realCharsetName = charsetName;
            }
        } catch (Exception e) {
            // TODO: handle exception
            realCharsetName = null;
            log.error("Error:parseRealCharsetName", e);
        }
        if (null == realCharsetName) {
            return defaultCharsetName;
        }
        return realCharsetName;
    }
}
