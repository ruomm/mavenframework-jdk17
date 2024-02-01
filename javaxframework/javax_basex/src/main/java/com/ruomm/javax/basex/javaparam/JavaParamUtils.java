package com.ruomm.javax.basex.javaparam;

import com.ruomm.javax.basex.javaparam.dal.JavaParamDal;
import com.ruomm.javax.basex.javaparam.dal.JavaPrarmField;
import com.ruomm.javax.corex.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2022/11/25 13:47
 */
public class JavaParamUtils {
    private final static String VALUE_NULL = "{NULL}";

    /**
     * 判断是否帮助模式
     *
     * @param emptyAsHelp 空参数是否判断为帮助模式
     * @param args        参数数组
     * @return 是否帮助模式
     */
    public static boolean isHelp(boolean emptyAsHelp, String[] args) {
        boolean isAppHelp = false;
        if (null == args || args.length <= 0) {
            if (emptyAsHelp) {
                isAppHelp = true;
            } else {
                isAppHelp = false;
            }
        } else if ("-h".equalsIgnoreCase(args[0]) || "/h".equalsIgnoreCase(args[0])
                || "-help".equalsIgnoreCase(args[0]) || "/help".equalsIgnoreCase(args[0])
                || "-?".equalsIgnoreCase(args[0]) || "/?".equalsIgnoreCase(args[0])) {
            isAppHelp = true;
        }
        return isAppHelp;
    }

    public static boolean isHelp(String noSkipHeader, boolean emptyAsHelp, String[] args) {
        if (isNoSkipMode(noSkipHeader, args)) {
            boolean isAppHelp = false;
            if (null == args || args.length <= 1) {
                if (emptyAsHelp) {
                    isAppHelp = true;
                } else {
                    isAppHelp = false;
                }
            } else if ("-h".equalsIgnoreCase(args[1]) || "/h".equalsIgnoreCase(args[1])
                    || "-help".equalsIgnoreCase(args[1]) || "/help".equalsIgnoreCase(args[1])
                    || "-?".equalsIgnoreCase(args[1]) || "/?".equalsIgnoreCase(args[1])) {
                isAppHelp = true;
            }
            return isAppHelp;
        } else {
            boolean isAppHelp = false;
            if (null == args || args.length <= 0) {
                if (emptyAsHelp) {
                    isAppHelp = true;
                } else {
                    isAppHelp = false;
                }
            } else if ("-h".equalsIgnoreCase(args[0]) || "/h".equalsIgnoreCase(args[0])
                    || "-help".equalsIgnoreCase(args[0]) || "/help".equalsIgnoreCase(args[0])
                    || "-?".equalsIgnoreCase(args[0]) || "/?".equalsIgnoreCase(args[0])) {
                isAppHelp = true;
            }
            return isAppHelp;
        }
    }

    public static boolean isHelp(boolean isNoSkip, boolean emptyAsHelp, String[] args) {
        if (isNoSkip) {
            boolean isAppHelp = false;
            if (null == args || args.length <= 1) {
                if (emptyAsHelp) {
                    isAppHelp = true;
                } else {
                    isAppHelp = false;
                }
            } else if ("-h".equalsIgnoreCase(args[1]) || "/h".equalsIgnoreCase(args[1])
                    || "-help".equalsIgnoreCase(args[1]) || "/help".equalsIgnoreCase(args[1])
                    || "-?".equalsIgnoreCase(args[1]) || "/?".equalsIgnoreCase(args[1])) {
                isAppHelp = true;
            }
            return isAppHelp;
        } else {
            boolean isAppHelp = false;
            if (null == args || args.length <= 0) {
                if (emptyAsHelp) {
                    isAppHelp = true;
                } else {
                    isAppHelp = false;
                }
            } else if ("-h".equalsIgnoreCase(args[0]) || "/h".equalsIgnoreCase(args[0])
                    || "-help".equalsIgnoreCase(args[0]) || "/help".equalsIgnoreCase(args[0])
                    || "-?".equalsIgnoreCase(args[0]) || "/?".equalsIgnoreCase(args[0])) {
                isAppHelp = true;
            }
            return isAppHelp;
        }

    }

    // 如是第一个参数是-nonoskip
    public static boolean isNoSkipMode(String noSkipHeader, String[] args) {
        if (null == args || args.length <= 0) {
            return false;
        }
        return isEqual(noSkipHeader, args[0]);
    }


    public static boolean isEqual(String headerStr, String arg) {
        String header = removeHeaderIdentify(headerStr);
        if (StringUtils.isEmpty(header) || StringUtils.isEmpty(arg)) {
            return false;
        }
        String argLower = arg.toLowerCase();
        if (argLower.equals("-" + header) || argLower.equals("/" + header)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMatch(String headerStr, String arg) {
        String header = removeHeaderIdentify(headerStr);
        if (StringUtils.isEmpty(header) || StringUtils.isEmpty(arg)) {
            return false;
        }
        String argLower = arg.toLowerCase();
        if (argLower.startsWith("-" + header) || argLower.startsWith("/" + header)) {
            return true;
        } else {
            return false;
        }
    }

    public static String parseString(String headerStr, String arg, String defalutVal) {
        int headerSize = parseHeaderLength(headerStr);
        String strVal;
        try {
            strVal = arg.substring(headerSize);
            if (StringUtils.isEmpty(strVal)) {
                strVal = defalutVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            strVal = defalutVal;
        }
        return strVal;
    }

    public static List<String> parseList(String headerStr, String arg, String defalutVal, String splitTag) {
        int headerSize = parseHeaderLength(headerStr);
        String strVal;
        try {
            strVal = arg.substring(headerSize);
            if (StringUtils.isEmpty(strVal)) {
                strVal = defalutVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            strVal = defalutVal;
        }
        if (StringUtils.isEmpty(strVal)) {
            return null;
        }
        List<String> strList = new ArrayList<>();
        try {
            String splitStr = StringUtils.isEmpty(splitTag) ? "," : splitTag;
            String[] tmpStrVals = strVal.split(splitStr);
            for (String tmp : tmpStrVals) {
                if (!StringUtils.isEmpty(tmp)) {
                    strList.add(tmp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strList.size() <= 0) {
            strList.add(strVal);
        }
        return strList;

    }

    public static int parseInt(String headerStr, String arg, int min, int max, int defalutVal) {
        int headerSize = parseHeaderLength(headerStr);
        int intVal;
        try {
            String strVal = arg.substring(headerSize);
            intVal = Integer.parseInt(strVal);
            if (intVal < min || intVal > max) {
                intVal = defalutVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            intVal = defalutVal;
        }
        return intVal;
    }

    public static long parseLong(String headerStr, String arg, long min, long max, long defalutVal) {
        int headerSize = parseHeaderLength(headerStr);
        long longVal;
        try {
            String strVal = arg.substring(headerSize);
            longVal = Long.parseLong(strVal);
            if (longVal < min || longVal > max) {
                longVal = defalutVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            longVal = defalutVal;
        }

        return longVal;
    }

    public static float parseFloat(String headerStr, String arg, float min, float max, int defalutVal) {
        int headerSize = parseHeaderLength(headerStr);
        float floatVal;
        try {
            String strVal = arg.substring(headerSize);
            floatVal = Float.parseFloat(strVal);
            if (floatVal < min || floatVal > max) {
                floatVal = defalutVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            floatVal = defalutVal;
        }

        return floatVal;
    }

    public static double parseDouble(String headerStr, String arg, double min, double max, double defalutVal) {
        int headerSize = parseHeaderLength(headerStr);
        double doubleVal;
        try {
            String strVal = arg.substring(headerSize);
            doubleVal = Float.parseFloat(strVal);
            if (doubleVal < min || doubleVal > max) {
                doubleVal = defalutVal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            doubleVal = defalutVal;
        }

        return doubleVal;
    }

    public static boolean parseBoolean(String headerStr, String arg, boolean defalutVal) {
        int headerSize = parseHeaderLength(headerStr);
        boolean booleanVal;
        try {
            String strVal = arg.substring(headerSize);
            if (strVal.equalsIgnoreCase("true")) {
                booleanVal = true;
            } else if (strVal.equalsIgnoreCase("false")) {
                booleanVal = false;
            } else {
                int tmpIntVal = Integer.parseInt(strVal);
                if (tmpIntVal == 0) {
                    booleanVal = false;
                } else if (tmpIntVal == 1) {
                    booleanVal = true;
                } else {
                    booleanVal = defalutVal;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            booleanVal = defalutVal;
        }

        return booleanVal;
    }

    private static int parseHeaderLength(String headerStr) {
        if (StringUtils.isEmpty(headerStr)) {
            return 0;
        }
        if ((headerStr.startsWith("-") || headerStr.startsWith("/")) && headerStr.length() > 1) {
            return headerStr.length();
        } else {
            return headerStr.length() + 1;
        }
    }

    private static String removeHeaderIdentify(String header) {
        if (StringUtils.isEmpty(header)) {
            return null;
        }
        String headerLower = header.toLowerCase();
        if ((headerLower.startsWith("-") || headerLower.startsWith("/"))) {
            headerLower = headerLower.substring(1);
        }
        if (StringUtils.isEmpty(headerLower)) {
            return null;
        }
        return headerLower;
    }


    /**
     * 解析参数为对象类型
     *
     * @param headerStr 参数头
     * @param arg       参数内容
     * @param tCls      解析结果泛型类型
     * @param <T>       解析结果
     * @return 解析结果
     */
    public static <T> JavaParamDal<T> parseResult(String headerStr, String arg, Class<T> tCls) {
        String header = removeHeaderIdentify(headerStr);
        if (StringUtils.isEmpty(header) || StringUtils.isEmpty(arg)) {
            JavaParamDal<T> javaParamDal = new JavaParamDal<T>();
            javaParamDal.setResult(false);
            return javaParamDal;
        }
        boolean result;
        String valString;
        String argLower = arg.toLowerCase();
        if (argLower.equals("-" + header) || argLower.equals("/" + header)) {
            result = true;
            valString = null;
        } else if (argLower.startsWith("-" + header) || argLower.startsWith("/" + header)) {
            result = true;
            valString = arg.substring(1 + header.length());
        } else {
            result = false;
            valString = null;
        }
        if (!result) {
            JavaParamDal<T> javaParamDal = new JavaParamDal<T>();
            javaParamDal.setResult(false);
            return javaParamDal;
        }
        JavaParamDal<T> javaParamDal = parseValStringToResult(valString, tCls);
        return javaParamDal;
    }

    /**
     *
     */
    private static <T> JavaParamDal<T> parseValStringToResult(String valString, Class<T> tCls) {
        // 空的时候判断
        if (StringUtils.isEmpty(valString)) {
            JavaParamDal<T> javaParamDal = new JavaParamDal<T>();
            javaParamDal.setResult(true);
            javaParamDal.setValString("");
            return javaParamDal;
        }
        T objectVal;
        if (tCls.equals(Byte.class)) {
            try {
                Byte tmpVal = Byte.valueOf(valString);
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Short.class)) {
            try {
                Short tmpVal = Short.valueOf(valString);
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Integer.class)) {
            try {
                Integer tmpVal = Integer.valueOf(valString);
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Long.class)) {
            try {
                Long tmpVal = Long.valueOf(valString);
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Float.class)) {
            try {
                Float tmpVal = Float.valueOf(valString);
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Double.class)) {
            try {
                Double tmpVal = Double.valueOf(valString);
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Character.class)) {
            try {
                Character tmpVal = valString.toCharArray()[0];
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(Boolean.class)) {
            Boolean tmpVal;
            if (valString.equalsIgnoreCase("true")) {
                tmpVal = true;
            } else if (valString.equalsIgnoreCase("false")) {
                tmpVal = false;
            } else {
                Integer intVal;
                try {
                    intVal = Integer.valueOf(valString);
                } catch (Exception e) {
                    e.printStackTrace();
                    intVal = null;
                }
                if (null != intVal && intVal == 1) {
                    tmpVal = true;
                } else {
                    tmpVal = false;
                }
            }
            try {
                objectVal = (T) tmpVal;
            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else if (tCls.equals(String.class)) {
            try {
                if (null == valString) {
                    objectVal = (T) "";
                } else {
                    objectVal = (T) valString;
                }

            } catch (Exception e) {
                e.printStackTrace();
                objectVal = null;
            }
        } else {
            objectVal = null;
        }
        JavaParamDal<T> javaParamDal = new JavaParamDal<T>();
        javaParamDal.setResult(true);
        javaParamDal.setValString(valString);
        javaParamDal.setValObject(objectVal);
        return javaParamDal;
    }


    /**
     * 解析参数为Map列表
     */
    /**
     * 解析参数为Map列表
     *
     * @param args         java参数字符串数组
     * @param paramHeaders 自解析的参数headers数组，在此数组内参数使用{-keyvalue}形式，不在此数组内参数使用{-key val...}形式，
     *                     如是有同样开头的，字符串长的请放前面，如-config和-c,需要-config放在前面，要不会解析错乱
     * @return
     */
    public static Map<String, List<String>> parseToListMap(String[] args, String[] paramHeaders) {
        Map<String, List<String>> map = new HashMap<>();
        if (null == args || args.length <= 0) {
            return map;
        }
        List<String> headers = new ArrayList<>();
        if (null != paramHeaders) {
            for (String header : paramHeaders) {
                if (StringUtils.isEmpty(header)) {
                    continue;
                }
                String headerLower = header.toLowerCase();
                if ((headerLower.startsWith("-") || headerLower.startsWith("/"))) {
                    headerLower = headerLower.substring(1);
                }
                if (StringUtils.isEmpty(headerLower)) {
                    continue;
                }
                headers.add(headerLower);
            }
        }
        String lastHeader = null;
        List<String> lastListString = null;
        for (String arg : args) {
            if (StringUtils.isEmpty(arg)) {
                continue;
            }
            // 单独的-或/符号是非法的，跳过此参数
            if (arg.equals("-")) {
                continue;
            }
            boolean isHeaderMatch = false;
            // 判断是否以header开始
            for (String header : headers) {
                String argLower = arg.toLowerCase();
                if (argLower.equals("-" + header) || argLower.equals("/" + header)) {
                    isHeaderMatch = true;
                    map.put(header, null);
                    break;
                } else if (argLower.startsWith("-" + header) || argLower.startsWith("/" + header)) {
                    isHeaderMatch = true;
                    String valString = arg.substring(1 + header.length());
                    if (StringUtils.isEmpty(valString)) {
                        map.put(header, null);
                    } else {
                        List<String> listString = new ArrayList<>();
                        listString.add(valString);
                        map.put(header, listString);
                    }
                    break;
                } else {
                    continue;
                }
            }
            // header模式匹配则跳过继续下次循环
            if (isHeaderMatch) {
                if (!StringUtils.isEmpty(lastHeader)) {
                    map.put(lastHeader, lastListString);
                }
                lastHeader = null;
                lastListString = null;
                continue;
            }
            // 开始判断是否参数模式
            if (arg.startsWith("-") && arg.length() > 1 && !arg.startsWith("--")) {
                if (!StringUtils.isEmpty(lastHeader)) {
                    map.put(lastHeader, lastListString);
                }
                lastHeader = arg.substring(1).toLowerCase();
                lastListString = null;
            } else {
                if (null == lastListString) {
                    lastListString = new ArrayList<>();
                }
                if (arg.startsWith("--")) {
                    lastListString.add(arg.substring(1));
                } else {
                    lastListString.add(arg);
                }
            }
        }
        if (!StringUtils.isEmpty(lastHeader)) {
            map.put(lastHeader, lastListString);
        }
        return map;
    }

    public static boolean containsKey(String header, Map<String, List<String>> map) {
        if (null == map || map.size() <= 0) {
            return false;
        }
        if (StringUtils.isEmpty(header)) {
            return false;
        }
        return map.containsKey(header);
    }

    public static String parseStringFromMap(String header, Map<String, List<String>> map) {
        if (!containsKey(header, map)) {
            return null;
        }
        List<String> strListInMap = map.get(header);
        if (null == strListInMap || strListInMap.size() <= 0) {
            return null;
        } else {
            String valStr = null;
            // 获取首个不为空的字符串
            for (String tmpValStr : strListInMap) {
                if (!StringUtils.isEmpty(tmpValStr)) {
                    valStr = tmpValStr;
                    break;
                }
            }
            return valStr;
        }
    }

    public static List<String> parseListFormMap(String header, Map<String, List<String>> map, String splitTag) {
        if (!containsKey(header, map)) {
            return null;
        }
        List<String> strListInMap = map.get(header);
        if (null == strListInMap || strListInMap.size() <= 0) {
            return null;
        }
        if (StringUtils.isEmpty(splitTag)) {
            return strListInMap;
        }
        List<String> strList = new ArrayList<>();
        try {
            for (String tmpValStr : strListInMap) {
                if (StringUtils.isEmpty(tmpValStr)) {
                    continue;
                }
                String[] tmpStrVals = tmpValStr.split(splitTag);
                for (String tmp : tmpStrVals) {
                    if (!StringUtils.isEmpty(tmp)) {
                        strList.add(tmp);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strList.size() <= 0) {
            return strListInMap;
        } else {
            return strList;
        }
    }

    public static <T> JavaParamDal<T> parseResultFromMap(String header, Map<String, List<String>> map, Class<T> tCls) {
        if (!containsKey(header, map)) {
            JavaParamDal<T> javaParamDal = new JavaParamDal<T>();
            javaParamDal.setResult(false);
            return javaParamDal;
        }
        String valString = parseStringFromMap(header, map);
        JavaParamDal<T> javaParamDal = parseValStringToResult(valString, tCls);
        return javaParamDal;

    }

    private static <T> T readDefaultValue(String key, Class<T> resultCls, Object defaultObjVal) {
        if (StringUtils.isEmpty(key) || null == defaultObjVal) {
            return null;
        }
        T t;
        try {
            Field tmpDefaultField = defaultObjVal.getClass().getDeclaredField(key);
            if (null == tmpDefaultField) {
                t = null;
            } else {
                tmpDefaultField.setAccessible(true);
                Object tmpObjVal = tmpDefaultField.get(defaultObjVal);
                if (null == tmpObjVal) {
                    t = null;
                } else {
                    t = (T) tmpObjVal;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            t = null;
        }
        return t;
    }

    public static <T> T parseResultObject(String[] args, String[] paramHeaders, Class<T> resultCls, Object defaultObjVal) {
        Map<String, List<String>> map = JavaParamUtils.parseToListMap(args, paramHeaders);
        T t;
        try {
            t = resultCls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            t = null;
        }
        if (null == t) {
            return null;
        }
        Field[] tFields = resultCls.getDeclaredFields();
        for (Field field : tFields) {
            try {
                JavaPrarmField javaPrarmField = field.getAnnotation(JavaPrarmField.class);
                if (null == javaPrarmField) {
                    continue;
                }
                String key = StringUtils.isEmpty(javaPrarmField.key()) ? field.getName().toLowerCase() : javaPrarmField.key().toLowerCase();
                if (StringUtils.isEmpty(key)) {
                    continue;
                }
                String valString = parseStringFromMap(key, map);
                if (StringUtils.isEmpty(valString)) {
                    if (containsKey(key, map)) {
                        if (VALUE_NULL.equalsIgnoreCase(javaPrarmField.valueHasKey())) {
                            valString = null;
                        } else {
                            valString = javaPrarmField.valueHasKey();
                        }
                    } else {
                        if (VALUE_NULL.equalsIgnoreCase(javaPrarmField.valueNoKey())) {
                            valString = null;
                        } else {
                            valString = javaPrarmField.valueNoKey();
                        }
                    }
                }
                Class<?> tCls = field.getType();
                Object objectVal;
                if (tCls.equals(Byte.class) || tCls.equals(byte.class)) {
                    Byte tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : Byte.valueOf(valString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Byte.class, defaultObjVal);
                    }
                    if (tCls.equals(byte.class)) {
                        if (null == tmpVal) {
                            objectVal = (byte) 0;
                        } else {
                            objectVal = tmpVal.byteValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Short.class) || tCls.equals(short.class)) {
                    Short tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : Short.valueOf(valString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Short.class, defaultObjVal);
                    }
                    if (tCls.equals(short.class)) {
                        if (null == tmpVal) {
                            objectVal = (short) 0;
                        } else {
                            objectVal = tmpVal.shortValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Integer.class) || tCls.equals(int.class)) {
                    Integer tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : Integer.valueOf(valString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Integer.class, defaultObjVal);
                    }
                    if (tCls.equals(int.class)) {
                        if (null == tmpVal) {
                            objectVal = (int) 0;
                        } else {
                            objectVal = tmpVal.intValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Long.class) || tCls.equals(long.class)) {
                    Long tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : Long.valueOf(valString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Long.class, defaultObjVal);
                    }
                    if (tCls.equals(long.class)) {
                        if (null == tmpVal) {
                            objectVal = 0l;
                        } else {
                            objectVal = tmpVal.longValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Float.class) || tCls.equals(float.class)) {
                    Float tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : Float.valueOf(valString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Float.class, defaultObjVal);
                    }
                    if (tCls.equals(float.class)) {
                        if (null == tmpVal) {
                            objectVal = 0f;
                        } else {
                            objectVal = tmpVal.floatValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Double.class) || tCls.equals(double.class)) {
                    Double tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : Double.valueOf(valString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Double.class, defaultObjVal);
                    }
                    if (tCls.equals(double.class)) {
                        if (null == tmpVal) {
                            objectVal = 0d;
                        } else {
                            objectVal = tmpVal.doubleValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Character.class) || tCls.equals(char.class)) {
                    Character tmpVal;
                    try {
                        tmpVal = StringUtils.isEmpty(valString) ? null : valString.toCharArray()[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Character.class, defaultObjVal);
                    }
                    if (tCls.equals(char.class)) {
                        if (null == tmpVal) {
                            objectVal = (char) 0;
                        } else {
                            objectVal = tmpVal.charValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(Boolean.class) || tCls.equals(boolean.class)) {
                    Boolean tmpVal;
                    if (null == valString) {
                        tmpVal = null;
                    } else if (StringUtils.isEmpty(valString)) {
                        tmpVal = true;
                    } else if (valString.equalsIgnoreCase("true")) {
                        tmpVal = true;
                    } else if (valString.equalsIgnoreCase("false")) {
                        tmpVal = false;
                    } else {
                        Integer intVal;
                        try {
                            intVal = Integer.valueOf(valString);
                        } catch (Exception e) {
                            e.printStackTrace();
                            intVal = null;
                        }
                        if (null != intVal && intVal == 1) {
                            tmpVal = true;
                        } else {
                            tmpVal = false;
                        }
                    }
                    if (null == tmpVal) {
                        tmpVal = readDefaultValue(field.getName(), Boolean.class, defaultObjVal);
                    }
                    if (tCls.equals(boolean.class)) {
                        if (null == tmpVal) {
                            objectVal = false;
                        } else {
                            objectVal = tmpVal.booleanValue();
                        }
                    } else {
                        objectVal = tmpVal;
                    }
                } else if (tCls.equals(String.class)) {
                    String tmpVal;
                    try {
                        tmpVal = valString;
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmpVal = null;
                    }
                    if (StringUtils.isEmpty(tmpVal)) {
                        String tmpValByDefault = readDefaultValue(field.getName(), String.class, defaultObjVal);
                        if (!StringUtils.isEmpty(tmpValByDefault)) {
                            tmpVal = tmpValByDefault;
                        }
                    }
                    objectVal = tmpVal;
                } else {
                    objectVal = null;
                }
                field.setAccessible(true);
                field.set(t, objectVal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

}
