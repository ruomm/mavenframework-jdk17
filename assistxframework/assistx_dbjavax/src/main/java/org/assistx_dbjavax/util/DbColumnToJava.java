/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月29日 下午2:19:59
 */
package org.assistx_dbjavax.util;

import com.ruomm.javax.corex.StringUtils;

import java.util.List;

public class DbColumnToJava {
    public static String DB_COLUMN_STRING = "id, \r\n" + "org_code, \r\n" + "acc_no, \r\n" + "acc_name, \r\n"
            + "trans_date, \r\n" + "trans_time, \r\n" + "trans_amt, \r\n" + "trans_currcy_code, \r\n" + "bal, \r\n"
            + "trans_no, \r\n" + "lend_flag, \r\n" + "part_acc_name, \r\n" + "part_acc_no, \r\n"
            + "open_bank_name, \r\n" + "cust_mess, \r\n" + "created_ts, \r\n" + "remark1, \r\n" + "remark2, \r\n"
            + "remark3";

    public static void main(String[] args) {

//		String resultStr = parseDbColunmToJava(DB_COLUMN_STRING, true);
//		System.out.println("转换结果：");
//		System.out.println(resultStr);
        System.out.println(parseDbColunmToField("POSPAY_ACCT_TRANS_DTL", true));
        System.out.println(parseDbColunmToField("POSPAY_CARD_TRANS_SUM", true));
        System.out.println(parseDbColunmToField("POSPAY_CARD_TRANS_SUM", true));
        System.out.println(parseDbColunmToField("POSPAY_QR_TRANS_DTL", true));
        System.out.println(parseDbColunmToField("POSPAY_QR_TRANS_SUM", true));
    }

    public static String parseDbColunmToJava(String dbColumnStr, boolean isForceUpperCase) {
        StringBuilder sb = new StringBuilder();
        List<String> lstData = parseDbColunmsToList(dbColumnStr);
        for (int i = 0; i < lstData.size(); i++) {
            String tmpColumn = lstData.get(i).trim();
            sb.append("\t\t\t@Column(name = \"" + (isForceUpperCase ? tmpColumn.toUpperCase() : tmpColumn) + "\")")
                    .append("\r\n");
            sb.append("\t\t\tprotected  String " + parseDbColunmToField(tmpColumn, false) + ";").append("\r\n");
            sb.append("\t\t\r\n");

        }
//		System.out.println("转换结果：");
//		System.out.println(sb.toString());
        return sb.toString();
    }

    public static List<String> parseDbColunmsToList(String dbData) {
        String realDbString = dbData.replace(" \r\n", "").replace("\r", "").replace("\r", "");
//		System.out.println(realDbString);
        List<String> listData = StringUtils.getListString(realDbString, ",", false);

        return listData;
    }

    private static String parseDbColunmToField(String dbColumn, boolean isFirstToUpper) {
        if (StringUtils.isEmpty(dbColumn)) {
            return "";
        }
        char[] dbColumnChars = dbColumn.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean isNextBig = false;
        boolean isFirst = true;
        for (char tmp : dbColumnChars) {
            if (tmp == '-' || tmp == '_') {
                isNextBig = true;
                continue;
            }
            if (isFirst) {
                isFirst = false;
                if (tmp >= 'a' && tmp <= 'z') {
                    sb.append((char) (tmp - 32));
                } else {
                    sb.append(tmp);
                }
                isNextBig = false;
                continue;
            }
            if (isNextBig) {
                isNextBig = false;
                if (tmp >= 'a' && tmp <= 'z') {
                    sb.append((char) (tmp - 32));
                } else {
                    sb.append(tmp);
                }
            } else {
                if (tmp >= 'A' && tmp <= 'Z') {
                    sb.append((char) (tmp + 32));
                } else {
                    sb.append(tmp);
                }
            }
        }
        return sb.toString();
    }
}
