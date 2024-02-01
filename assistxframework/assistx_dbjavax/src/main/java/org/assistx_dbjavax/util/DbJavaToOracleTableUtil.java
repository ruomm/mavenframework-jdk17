/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月13日 下午5:25:42
 */
package org.assistx_dbjavax.util;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.corex.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DbJavaToOracleTableUtil {
    private final static boolean isLog = true;
    private final static String DEF_VARCHAR_LENGTH = "64";

    public static void parseJavaToSQL(String javaPath, String tableName, String outPath, boolean isCommitFront,
                                      boolean isLineCommitFront, boolean isColumnAnnotationOver) {
        List<String> javaLineList = parseJavaToLstStr(javaPath);
        String javaName = parseToJavaName(javaLineList);
        String packageName = parseToPackageName(javaLineList);
        List<DbJavaLineVal> DbColumnValList = parseToDbJavaLineVal(javaLineList, isCommitFront, isLineCommitFront);
        parseJavaToDbTable(DbColumnValList, tableName, packageName, javaName, outPath, isColumnAnnotationOver);
    }

    private static List<String> parseJavaToLstStr(String javaPath) {
        List<String> list = FileUtils.readFileToList(javaPath, "UTF-8", true);
        return list;
    }

    private static String parseToPackageName(List<String> javaLineList) {
        String packageName = null;
        for (String tmpVal : javaLineList) {
            if (StringUtils.isEmpty(tmpVal)) {
                continue;
            }
            String tmpReplaceVal = tmpVal.replace("\t", " ").replace("    ", " ").replace("   ", " ").replace("  ",
                    " ");
            if (null != tmpReplaceVal && tmpReplaceVal.startsWith(" ")) {
                tmpReplaceVal = tmpReplaceVal.substring(1);
            }
//			System.out.println(tmpReplaceVal);
            if (StringUtils.isEmpty(tmpReplaceVal)) {
                continue;
            }
            String[] tmpSpilt = tmpReplaceVal.split(" ");
            if (tmpSpilt.length >= 2 && "package".equals(tmpSpilt[0]) && null != tmpSpilt[1] && tmpSpilt[1].length() > 2
                    && tmpSpilt[1].endsWith(";")) {
                packageName = tmpSpilt[1].substring(0, tmpSpilt[1].length() - 1);
                break;
            }
        }
        return packageName;
    }

    private static String parseToJavaName(List<String> javaLineList) {
        String javaName = null;
        for (String tmpVal : javaLineList) {
            if (StringUtils.isEmpty(tmpVal)) {
                continue;
            }
            String tmpReplaceVal = tmpVal.replace("\t", " ").replace("    ", " ").replace("   ", " ").replace("  ",
                    " ");
            if (null != tmpReplaceVal && tmpReplaceVal.startsWith(" ")) {
                tmpReplaceVal = tmpReplaceVal.substring(1);
            }
//			System.out.println(tmpReplaceVal);
            if (StringUtils.isEmpty(tmpReplaceVal)) {
                continue;
            }
            String[] tmpSpilt = tmpReplaceVal.split(" ");
            if (tmpSpilt.length >= 3 && ("protected".equals(tmpSpilt[0]) || "public".equals(tmpSpilt[0]))
                    && "class".equals(tmpSpilt[1])) {
                javaName = tmpSpilt[2];
                break;
            } else if (tmpSpilt.length >= 3 && "class".equals(tmpSpilt[0])) {
                javaName = tmpSpilt[1];
                break;
            }
        }
        return javaName;
    }

    private static List<DbJavaLineVal> parseToDbJavaLineVal(List<String> javaLineList, boolean isCommitFront,
                                                            boolean isLineCommitFront) {
        List<DbJavaLineVal> lstData = new ArrayList<DbJavaLineVal>();
        int javaLineSize = javaLineList.size();
        int codeLine = -1;
        for (int i = 0; i < javaLineSize; i++) {
            DbJavaLineVal dbColumnVal = new DbJavaLineVal();
            String tmpVal = javaLineList.get(i);
            dbColumnVal.setIndex(i);
            dbColumnVal.setVal(tmpVal);
            dbColumnVal.setCommitFlag(false);
            dbColumnVal.setKeyFlag(false);
            dbColumnVal.setColumnAnnotation(false);
            if (StringUtils.isEmpty(tmpVal)) {
                dbColumnVal.setKey(null);
                dbColumnVal.setCommit(null);
            } else {
                String tmpReplaceVal = tmpVal.replace("\t", " ").replace("    ", " ").replace("   ", " ").replace("  ",
                        " ");
                if (null != tmpReplaceVal) {
                    tmpReplaceVal = tmpReplaceVal.trim();
                }
                if (StringUtils.isEmpty(tmpReplaceVal)) {
                    dbColumnVal.setKey(null);
                    dbColumnVal.setCommit(null);
                }

                if (codeLine < 0) {
                    String[] tmpSpilt = tmpReplaceVal.split(" ");
                    if (tmpSpilt.length >= 3 && ("protected".equals(tmpSpilt[0]) || "public".equals(tmpSpilt[0]))
                            && "class".equals(tmpSpilt[1])) {
                        codeLine = i;
                    } else if (tmpSpilt.length >= 3 && "class".equals(tmpSpilt[0])) {
                        codeLine = i;
                    }
                } else if (tmpReplaceVal.startsWith("@Column")) {
                    dbColumnVal.setColumnAnnotation(true);
                } else {
                    String[] tmpSpilt = tmpReplaceVal.split(" ");
                    if (null != tmpSpilt && tmpSpilt.length >= 3
                            && ("private".equals(tmpSpilt[0]) || "public".equals(tmpSpilt[0])
                            || "protected".equals(tmpSpilt[0]))
                            && StringUtils.isNotEmpty(tmpSpilt[1]) && StringUtils.isNotEmpty(tmpSpilt[2])
                            && tmpSpilt[2].length() > 1) {
                        if (tmpSpilt[2].endsWith(";")) {
                            dbColumnVal.setKey(tmpSpilt[2].substring(0, tmpSpilt[2].length() - 1));
                            dbColumnVal.setKeyFlag(true);
                        } else {
                            int dcIndex = tmpSpilt[2].indexOf(";/");
                            if (dcIndex > 0) {
                                dbColumnVal.setKey(tmpSpilt[2].substring(0, dcIndex));
                                dbColumnVal.setKeyFlag(true);
                            }
                        }
                        dbColumnVal.setKeyType(tmpSpilt[1]);
                    }
                    boolean isCommitFlag = true;
                    if (null != tmpSpilt && tmpSpilt.length >= 4 && "//".equals(tmpSpilt[0])
                            && ("private".equals(tmpSpilt[1]) || "public".equals(tmpSpilt[1])
                            || "protected".equals(tmpSpilt[1]))
                            && StringUtils.isNotEmpty(tmpSpilt[2]) && StringUtils.isNotEmpty(tmpSpilt[3])
                            && tmpSpilt[3].indexOf(";") > 0) {
                        isCommitFlag = false;
                    } else if (null != tmpSpilt && tmpSpilt.length >= 2 && "//".equals(tmpSpilt[0])) {
                        isCommitFlag = !isAnnotation(tmpSpilt[1]);
                    } else if (!dbColumnVal.isKeyFlag()) {
                        if (null == tmpSpilt || tmpSpilt.length < 1 || StringUtils.isEmpty(tmpSpilt[0])) {
                            isCommitFlag = false;
                        } else if (tmpSpilt[0].equals("//")) {
                            if (tmpSpilt.length <= 1) {
                                isCommitFlag = false;
                            }
                        } else if (!tmpSpilt[0].startsWith("//")) {
                            isCommitFlag = false;
                        }

                    }
                    int index = tmpVal.indexOf("//");
                    if (isCommitFlag && index >= 0) {
                        String commit = tmpVal.substring(index + 2);
                        if (null != commit) {
                            commit = commit.trim();
                        }
                        if (StringUtils.isNotEmpty(commit)) {
                            dbColumnVal.setCommitFlag(true);
                            dbColumnVal.setCommit(commit);
                        }
                    }

                }
            }
            lstData.add(dbColumnVal);
        }
        if (isCommitFront && isLineCommitFront) {
            StringBuilder sb = null;
            for (DbJavaLineVal dbJavaLineValItem : lstData) {
                if (dbJavaLineValItem.getIndex() <= codeLine) {
                    continue;
                }
                if (dbJavaLineValItem.isCommitFlag()) {
                    if (null == sb) {
                        sb = new StringBuilder();
                    }
                    sb.append(dbJavaLineValItem.getCommit());
                }
                if (dbJavaLineValItem.isKeyFlag()) {
                    if (null != sb && sb.length() > 0) {
                        dbJavaLineValItem.setDbCommit(sb.toString());
                    }
                    sb = null;
                }
            }
        } else if (isCommitFront && !isLineCommitFront) {
            StringBuilder sb = null;
            for (DbJavaLineVal dbJavaLineValItem : lstData) {
                if (dbJavaLineValItem.getIndex() <= codeLine) {
                    continue;
                }

                if (dbJavaLineValItem.isKeyFlag()) {
                    if (null != sb && sb.length() > 0) {
                        dbJavaLineValItem.setDbCommit(sb.toString());
                    }
                    sb = null;
                }
                if (dbJavaLineValItem.isCommitFlag()) {
                    if (null == sb) {
                        sb = new StringBuilder();
                    }
                    sb.append(dbJavaLineValItem.getCommit());
                }
            }
        } else {
            StringBuilder sb = null;
            DbJavaLineVal dbJavaLineVal = null;
            for (DbJavaLineVal dbJavaLineValItem : lstData) {
                if (dbJavaLineValItem.getIndex() <= codeLine) {
                    continue;
                }
                if (dbJavaLineValItem.isKeyFlag()) {
                    if (null != dbJavaLineVal) {
                        if (null != sb && sb.length() > 0) {
                            dbJavaLineVal.setDbCommit(sb.toString());
                        }
                    }
                    dbJavaLineVal = dbJavaLineValItem;
                    sb = new StringBuilder();
                }
                if (dbJavaLineValItem.getIndex() == javaLineSize - 1) {
                    if (null != dbJavaLineVal) {
                        if (null != sb && sb.length() > 0) {
                            dbJavaLineVal.setDbCommit(sb.toString());
                        }
                    }
                    dbJavaLineVal = null;
                    sb = null;
                }
                if (dbJavaLineValItem.isCommitFlag() && null != sb) {
                    sb.append(dbJavaLineValItem.getCommit());
                }

            }
        }
        if (isLog) {

            System.out.println("--------Java文件解析信息打印开始--------");
            for (DbJavaLineVal tmpVal : lstData) {
                System.out.println(tmpVal.toString());
                System.out.println();
            }
            System.out.println("--------Java文件解析信息打印结束--------");
            System.out.println("--------Java字段Field信息打印开始--------");
            for (DbJavaLineVal tmpVal : lstData) {
                if (tmpVal.isKeyFlag()) {
                    System.out.println(tmpVal.toString());
                    System.out.println();
                }

            }
            System.out.println("--------Java字段Field信息打印结束--------");
        }
        return lstData;
    }

    private static void parseJavaToDbTable(List<DbJavaLineVal> lstData, String tableName, String packageName,
                                           String javaName, String outputPath, boolean isColumnAnnotationOver) {
        List<DbJavaLineVal> dbLstData = new ArrayList<DbJavaLineVal>();
        for (DbJavaLineVal dbJavaLineVal : lstData) {
            if (dbJavaLineVal.isKeyFlag()) {
                dbLstData.add(dbJavaLineVal);
            }
        }

        if (null != lstData) {
            StringBuilder sb = new StringBuilder();
            if (isColumnAnnotationOver) {
                for (DbJavaLineVal dbJavaLineVal : lstData) {
                    if (dbJavaLineVal.isColumnAnnotation()) {
                        continue;
                    }
                    if (dbJavaLineVal.isKeyFlag()) {
                        String dbKey = parseToDbColunm(dbJavaLineVal.getKey()).toLowerCase();
                        String keyString = "\t@Column(name = \"" + dbKey + "\")" + "\r\n";
                        sb.append(keyString);

                    }
                    sb.append(StringUtils.nullStrToEmpty(dbJavaLineVal.getVal()) + "\r\n");
                }
            } else {
                boolean isColumnAnnotationWrite = false;
                for (DbJavaLineVal dbJavaLineVal : lstData) {
                    if (dbJavaLineVal.isColumnAnnotation()) {
                        isColumnAnnotationWrite = true;
                    }
                    if (dbJavaLineVal.isKeyFlag()) {
                        if (!isColumnAnnotationWrite) {
                            String dbKey = parseToDbColunm(dbJavaLineVal.getKey()).toLowerCase();
                            String keyString = "\t@Column(name = \"" + dbKey + "\")" + "\r\n";
                            sb.append(keyString);
                            isColumnAnnotationWrite = true;
                        }
                        isColumnAnnotationWrite = false;

                    }
                    sb.append(StringUtils.nullStrToEmpty(dbJavaLineVal.getVal()) + "\r\n");
                }
            }
            System.out.println("--------JAVA类开始--------");
            System.out.println(sb.toString());
            System.out.println("--------JAVA类结束--------");
            if (StringUtils.isNotEmpty(outputPath)) {
                String outPath = outputPath + javaName + ".java";
                FileUtils.writeFile(outPath, sb.toString(), false);
            }
        }
        if (dbLstData.size() > 0) {
            String tblName = null;
            if (StringUtils.isEmpty(tableName)) {
                tblName = parseToDbColunm(javaName);
            } else if (tableName.length() <= 5 && (tableName.endsWith("_") || tableName.endsWith("-"))) {
                tblName = tableName.toUpperCase() + parseToDbColunm(javaName);
            } else if (tableName.length() <= 4) {
                tblName = tableName.toUpperCase() + "_" + parseToDbColunm(javaName);
            } else {
                tblName = tableName.toUpperCase();
            }
            StringBuilder sbDb = new StringBuilder();
            StringBuilder sbCommit = new StringBuilder();
            sbDb.append("CREATE TABLE " + tblName + " (\r\n");

            for (int index = 0; index < dbLstData.size(); index++) {
                String fieldKey = dbLstData.get(index).getKey();
                String keyType = dbLstData.get(index).getKeyType();
                String dbKey = parseToDbColunm(fieldKey);
                if ("float".equalsIgnoreCase(keyType)) {
                    sbDb.append("\"").append(dbKey.toUpperCase()).append("\"").append(" NUMBER(16,6) NULL ");
                } else if ("double".equalsIgnoreCase(keyType)) {
                    sbDb.append("\"").append(dbKey.toUpperCase()).append("\"").append(" NUMBER(30,10) NULL ");
                } else if ("integer".equalsIgnoreCase(keyType) || "int".equalsIgnoreCase(keyType)) {
                    sbDb.append("\"").append(dbKey.toUpperCase()).append("\"").append(" NUMBER(10) NULL ");
                } else if ("long".equalsIgnoreCase(keyType)) {
                    sbDb.append("\"").append(dbKey.toUpperCase()).append("\"").append(" NUMBER(20) NULL ");
                } else {
                    sbDb.append("\"").append(dbKey.toUpperCase()).append("\"")
                            .append(" VARCHAR2(" + DEF_VARCHAR_LENGTH + " BYTE) NULL ");
                }

                if (index < dbLstData.size() - 1) {
                    sbDb.append(",");
                }
                sbDb.append("\r\n");
                if (StringUtils.isNotEmpty(dbLstData.get(index).getDbCommit())) {
                    sbCommit.append("COMMENT ON COLUMN \"" + tblName + "\".\"" + dbKey + "\" IS '"
                            + dbLstData.get(index).getDbCommit() + "';" + "\r\n");
                }
            }

            sbDb.append(")\r\n" + "LOGGING\r\n" + "NOCOMPRESS\r\n" + "NOCACHE\r\n" + "\r\n" + ";");
            sbDb.append("\r\n");
            sbDb.append(sbCommit.toString());
            if (StringUtils.isNotEmpty(outputPath)) {
                String outPath = outputPath + tblName + ".sql";
                FileUtils.writeFile(outPath, sbDb.toString() + "\r\n", false);
            }
            System.out.println("--------DBSQL开始--------");
            System.out.println(sbDb.toString());
            System.out.println("--------DBSQL类结束--------");
        }

        if (dbLstData.size() > 0) {
//			<resultMap id="dbSquenceResultMap" type="com.ruomm.webapp.dal.mapperbean.DbSequence">
            String resultMapId = StringUtils.firstToLowerCase(javaName) + "Map";
            String resultMapType = packageName + "." + javaName;

            StringBuilder sbDbMapperXml = new StringBuilder();
            StringBuilder sbCommit = new StringBuilder();
            sbDbMapperXml.append("\t").append("<resultMap id=\"" + resultMapId + "\" type=\"" + resultMapType + "\">")
                    .append("\r\n");

            for (int index = 0; index < dbLstData.size(); index++) {
                String fieldKey = dbLstData.get(index).getKey();
                String keyType = dbLstData.get(index).getKeyType();
                String dbKey = parseToDbColunm(fieldKey).toLowerCase();
                String dbKeyType = null;

                if ("float".equalsIgnoreCase(keyType) || "double".equalsIgnoreCase(keyType)
                        || "integer".equalsIgnoreCase(keyType) || "int".equalsIgnoreCase(keyType)
                        || "long".equalsIgnoreCase(keyType) || "BigDecimal".equalsIgnoreCase(keyType)) {

                    dbKeyType = "NUMERIC";
                } else {
                    dbKeyType = "VARCHAR";
                }
//				<result property="seqValue" column="SEQ_VALUE" jdbcType="NUMERIC"/>
                sbDbMapperXml.append("\t").append("\t").append("<result property=\"").append(fieldKey)
                        .append("\" column=\"").append(dbKey.toUpperCase()).append("\" jdbcType=\"").append(dbKeyType)
                        .append("\"/>").append("\r\n");
            }

            sbDbMapperXml.append("\t").append("</resultMap>");
            sbDbMapperXml.append(sbCommit.toString());
            if (StringUtils.isNotEmpty(outputPath)) {
                String outPath = outputPath + javaName + "MapperMap.xml";
                FileUtils.writeFile(outPath, sbDbMapperXml.toString() + "\r\n", false);
            }
            System.out.println("--------DBMapper开始--------");
            System.out.println(sbDbMapperXml.toString());
            System.out.println("--------DBMapper类结束--------");
        }
        if (dbLstData.size() > 0) {
            StringBuilder sbCloums = new StringBuilder();
            StringBuilder sbFields = new StringBuilder();
            StringBuilder sbOracleSet = new StringBuilder();

            StringBuilder sbDbMapperXml = new StringBuilder();

            for (int index = 0; index < dbLstData.size(); index++) {
                String fieldKey = dbLstData.get(index).getKey();
                String keyType = dbLstData.get(index).getKeyType();
                String dbKey = parseToDbColunm(fieldKey).toLowerCase();
                String dbKeyType = null;

                if ("float".equalsIgnoreCase(keyType) || "double".equalsIgnoreCase(keyType)
                        || "integer".equalsIgnoreCase(keyType) || "int".equalsIgnoreCase(keyType)
                        || "long".equalsIgnoreCase(keyType) || "BigDecimal".equalsIgnoreCase(keyType)) {

                    dbKeyType = "NUMERIC";
                } else {
                    dbKeyType = "VARCHAR";
                }
                if (index % 8 == 0) {
                    sbCloums.append("\r\n");
                    sbFields.append("\r\n");

                }
                if (index % 5 == 0) {
                    sbOracleSet.append("\r\n");
                }
                sbCloums.append(dbKey.toUpperCase()).append(",");
                sbFields.append("#{" + fieldKey + ",jdbcType=" + dbKeyType + "}").append(",");
                sbOracleSet.append(dbKey.toUpperCase() + "=#{" + fieldKey + ",jdbcType=" + dbKeyType + "}").append(",");

            }
            sbDbMapperXml.append(sbCloums.toString());
            sbDbMapperXml.append("\r\n");
            sbDbMapperXml.append("\r\n");
            sbDbMapperXml.append(sbFields.toString());
            sbDbMapperXml.append("\r\n");
            sbDbMapperXml.append("\r\n");
            sbDbMapperXml.append(sbOracleSet.toString());
            sbDbMapperXml.append("\r\n");
            sbDbMapperXml.append("\r\n");
            if (StringUtils.isNotEmpty(outputPath)) {
                String outPath = outputPath + javaName + "MapperMap.txt";
                FileUtils.writeFile(outPath, sbDbMapperXml.toString() + "\r\n", false);
            }
            System.out.println("--------DBMapperText开始--------");
            System.out.println(sbDbMapperXml.toString());
            System.out.println("--------DBMapperText类结束--------");
        }
    }

    private static String parseToDbColunm(String fieldName) {
        char[] fieldChars = fieldName.toCharArray();
        StringBuilder sb = new StringBuilder();
        Character lastTmp = null;
        for (char tmp : fieldChars) {
            if (tmp >= 'A' && tmp <= 'Z') {
                if (sb.length() > 0) {
                    sb.append("_").append(tmp);
                } else {
                    sb.append(tmp);
                }
            } else if (tmp >= '0' && tmp <= '9') {
                if (null != lastTmp && (lastTmp < '0' || lastTmp > '9')) {
                    sb.append("_").append(tmp);
                } else {
                    sb.append(tmp);
                }
            } else if (tmp >= 'a' && tmp <= 'z') {
                sb.append((char) (tmp - 32));
            } else {
                sb.append(tmp);
            }
            lastTmp = tmp;
        }

        return sb.toString();
    }

    private static boolean isAnnotation(String tmpStr) {
        if (StringUtils.isEmpty(tmpStr)) {
            return false;
        }
        if (!tmpStr.startsWith("@") || tmpStr.length() < 2) {
            return false;
        }
        boolean flag = true;
        int index = tmpStr.indexOf("(");
        char[] tmpChars = null;
        if (index > 1) {
            tmpChars = tmpStr.substring(1, index).toCharArray();
        } else {
            tmpChars = tmpStr.substring(1).toCharArray();
        }
        for (char tmp : tmpChars) {
            if (!(tmp >= 'A' && tmp <= 'Z') && !(tmp >= 'a' && tmp <= 'z') && !(tmp >= '0' && tmp <= '9')) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        String data = parseToDbColunm("123NNIhao1132546");
        System.out.println(data);
    }
}
