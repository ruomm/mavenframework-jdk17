/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月14日 下午2:06:37
 */
package org.assistx_dbjavax;

import org.assistx_dbjavax.util.DbJavaToOracleTableUtil;

public class DbJavaToOracleTable {
    public final static String JAVA_PATH = "E:\\RuommWork\\ruommwebapp\\src\\com\\ruomm\\bo\\";
    public final static String OUT_PATH = "D:\\temp\\UnionpayOpenWebApp\\";

    public final static String TBL_NAME = "D2D_";

    public static void main(String[] args) {

//		parseJavaToSQL(JAVA_PATH, TBL_NAME, OUT_PATH);
        parseJavaToSQL(JAVA_PATH + "D2dMchtInfo.java", TBL_NAME + "MCHT_INFO_TMP", OUT_PATH);
        parseJavaToSQL(JAVA_PATH + "D2dMchtShopInfo.java", TBL_NAME + "MCHT_SHOP_INFO_TMP", OUT_PATH);
        parseJavaToSQL(JAVA_PATH + "D2dMchtTermInfo.java", TBL_NAME + "MCHT_POS_INFO_TMP", OUT_PATH);
        System.out.println(OUT_PATH);
    }

    public static void parseJavaToSQL(String javaPath, String tableName, String outPath) {
        DbJavaToOracleTableUtil.parseJavaToSQL(javaPath, tableName, outPath, false, false, true);
    }

    public static void parseJavaToSQL(String javaPath, String tableName, String outPath, boolean isCommitFront,
                                      boolean isLineCommitFront, boolean isColumnAnnotationOver) {
        DbJavaToOracleTableUtil.parseJavaToSQL(javaPath, tableName, outPath, isCommitFront, isLineCommitFront,
                isColumnAnnotationOver);

    }
}
