package com.ruomm.assistx.demo;

import com.ruomm.javax.basex.IDCardUtils;
import com.ruomm.javax.corex.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public final static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd_HHmm");

    public static void main(String[] args) {
        String idCardHeader = "410105199506";
        List<String> listIdCards = new ArrayList<>();
        for (int d = 1; d <= 31; d++) {
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j <= 10; j++) {
                    String idCardStr = idCardHeader + String.format("%02d", d) + String.format("%03d", i) + (j < 10 ? String.valueOf(j) : "X");
                    if (IDCardUtils.isIDCardValidate(idCardStr)) {
                        listIdCards.add(idCardStr);
                    }
                }
            }
        }
        FileUtils.writeFile("D:\\temp\\" + idCardHeader + ".txt", listIdCards, "\r\n");
    }

}
