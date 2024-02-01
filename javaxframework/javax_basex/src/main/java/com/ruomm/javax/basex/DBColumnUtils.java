package com.ruomm.javax.basex;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/27 15:47
 */
public class DBColumnUtils {
    //    private String d15AAniHHaoAA;
//    private boolean d15AAniHHaoAB;
//    public static void main(String[] args) {
//        List<String> list = parseClassToDb(DBColumnUtils.class,true);
//        System.out.println(StringUtils.appendString(list,"\r\n"));
//    }
    public static List<String> parseClassToDb(Class<?> cls, boolean withOrcaleType) {
        if (null == cls) {
            return null;
        }
        Field[] fields = cls.getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            StringBuilder sb = new StringBuilder();
            int fieldNameSize = fieldName.length();
            for (int i = 0; i < fieldNameSize; i++) {
                if (i == 0) {
                    sb.append(fieldName.substring(0, 1).toUpperCase());
                } else {


                    String cur = fieldName.substring(i, i + 1);
                    if (!isCharacter(cur)) {
                        sb.append(cur.toUpperCase());
                    } else if (isUpperCase(cur)) {
                        //判断上一个是否大写
                        String pre = fieldName.substring(i - 1, i);
                        if (isUpperCase(pre)) {
                            sb.append(cur.toUpperCase());
                        } else {
                            sb.append("_").append(cur.toUpperCase());
                        }
                    } else {
                        sb.append(cur.toUpperCase());
                    }

                }
            }
            if (withOrcaleType) {
                if (field.getType() == String.class) {
                    sb.append("\t\tVARCHAR2(32),");
                } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    sb.append("\t\tNUMBER(2),");
                } else if (field.getType() == Integer.class || field.getType() == int.class) {
                    sb.append("\t\tNUMBER(11),");
                } else if (field.getType() == Long.class || field.getType() == long.class) {
                    sb.append("\t\tNUMBER(20),");
                } else if (field.getType() == Short.class || field.getType() == Short.class) {
                    sb.append("\t\tNUMBER(6),");
                } else if (field.getType() == Float.class || field.getType() == float.class) {
                    sb.append("\t\tNUMBER(11,7),");
                } else if (field.getType() == Double.class || field.getType() == double.class) {
                    sb.append("\t\tNUMBER(20,14),");
                } else {
                    sb.append("\t\tVARCHAR2(32),");
                }
            }
            list.add(sb.toString());
        }
        return list;

    }

    public static boolean isCharacter(String a) {
        if (a.toLowerCase().equals(a.toUpperCase())) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUpperCase(String a) {
        if (a.toLowerCase().equals(a.toUpperCase())) {
            return false;
        } else if (a.toUpperCase().equals(a)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLowerCase(String a) {
        if (a.toLowerCase().equals(a.toUpperCase())) {
            return false;
        } else if (a.toLowerCase().equals(a)) {
            return true;
        } else {
            return false;
        }
    }
}
