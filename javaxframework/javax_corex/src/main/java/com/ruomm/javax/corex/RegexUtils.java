/**
 * @copyright 亿康通-2015
 * @author 牛牛-wanruome@163.com
 * @create 2015年5月20日 下午2:17:55
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regex正则表达式判断是否符合条件
 *
 * @author Ruby
 */
public class RegexUtils {
    private final static Log log = LogFactory.getLog(RegexUtils.class);
    public final static String MOBILE_NUM = "^1\\d{10}$";
    public final static String TEL_NUM = "^0\\d{1,4}-?\\d{6,9}";
    public final static String PHONE_NUM = "^(0\\d{1,4}-?\\d{6,9})|1[345678]\\d{9}$";
    public final static String EMAILS = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public final static String ISBirthday = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
    public final static String CarPlatenumber = "^[\\u4E00-\\u9FA5][a-zA-Z][\\da-zA-Z]{5}$";
    // 本应用
    public final static String OPERATOR_PASSWORD = "^[!-~]{6,16}$";
    public final static String OPERATOR_ACCOUNT = "^[a-zA-Z][a-zA-Z0-9\\_\\-]{3,15}$";
    public final static String CONFIGVALUE_KEY = "^[a-zA-Z][a-zA-Z0-9\\_\\-]{3,32}$";
    // IP
    public final static String IPV4 = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

    public static boolean doRegex(String value, String regularExpression) {
        if (null == value || value.length() <= 0) {
            return false;
        }
        if (null == regularExpression || regularExpression.length() <= 0) {
            return true;
        }
        try {
            return value.matches(regularExpression);
        } catch (Exception e) {
            log.error("Error:doRegex", e);
            return false;
        }

    }

    public static boolean doMatcher(String value, String regularExpression) {
        if (null == value || value.length() <= 0) {
            return false;
        }
        if (null == regularExpression || regularExpression.length() <= 0) {
            return true;
        }
        try {
            Pattern p = Pattern.compile(regularExpression);
            Matcher m = p.matcher(value);
            return m.matches();
        } catch (Exception e) {
            log.error("Error:doMatcher", e);
            return false;
        }
    }

}
