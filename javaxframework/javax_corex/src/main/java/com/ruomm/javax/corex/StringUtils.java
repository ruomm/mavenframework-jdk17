/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年7月26日 下午11:17:14
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.corex.annotation.DefParseObjToStr;
import com.ruomm.javax.corex.annotation.DefParseStrToObj;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {
    private final static Log log = LogFactory.getLog(StringUtils.class);
    public final static String STR_EMPTY_VALUE = "";
    public final static String STR_SPACE_VALUE = " ";
    private final static boolean Parse_StrToNumLst_By_BigDecimal = true;
    private final static boolean Parse_StrToNum_By_BigDecimal = false;
    private final static String Parse_StrToObj_Method = DefParseStrToObj.PARSE_MOTHOD;
    private final static String Parse_ObjToStr_Method = DefParseObjToStr.PARSE_MOTHOD;

    /**
     * @param str CharSequence value
     * @return If CharSequence value is NULL or length of CharSequence value is 0,this function return true,else this
     * function retrun false
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() <= 0;
    }

    /**
     * @param str CharSequence value
     * @return If CharSequence value is NULL or length of CharSequence value after trim is 0,this function return
     * true,else this function retrun false
     */
    public static boolean isBlank(CharSequence str) {
        if (null == str) {
            return true;
        } else if (str instanceof String) {
            return ((String) str).trim().length() <= 0;
        } else {
            String s = str.toString();
            return s == null || s.trim().length() <= 0;
        }
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * @param strSeq CharSequence value
     * @return If String value is NULL return null,else return String value after trim
     */
    public static String trim(CharSequence strSeq) {
        String str = charSequenceToString(strSeq);
        return null == str ? null : str.trim();
    }

    /**
     * @param strSeq CharSequence value
     * @return If String value is NULL return empty string,else return String value after trim
     */
    public static String trimToNotNullString(CharSequence strSeq) {
        String str = charSequenceToString(strSeq);
        return null == str ? STR_EMPTY_VALUE : str.trim();
    }

    public static int getLength(CharSequence str) {
        return null == str ? 0 : str.length();
    }

    public static int getBytelength(String str) {
        return getBytelength(str, null);
    }

    public static int getBytelength(String str, String charsetName) {
        if (null == charsetName || charsetName.length() <= 0) {
            return null == str ? 0 : str.getBytes().length;
        }
        try {
            return str.getBytes(charsetName).length;
        } catch (Exception e) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "获取特定编码字符串长度时候编码设置错误!");
        }
    }

    /**
     * NULL字符串为空字符串
     *
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(CharSequence str) {
        if (null == str) {
            return STR_EMPTY_VALUE;
        } else if (str instanceof String) {
            return (String) str;
        } else {
            String s = str.toString();
            return null == s ? STR_EMPTY_VALUE : s;
        }
    }

    /**
     * 将字符串转换为大写字符串，null不转换直接返回
     *
     * @param str 字符串
     * @return 大写字符串
     */
    public static String toUpperCase(CharSequence str) {
        String resultStr = charSequenceToString(str);
        return null == resultStr || resultStr.length() <= 0 ? resultStr : resultStr.toUpperCase();
    }

    /**
     * 将字符串转换为小写字符串，null不转换直接返回
     *
     * @param str 字符串
     * @return 小写字符串
     */
    public static String toLowerCase(CharSequence str) {
        String resultStr = charSequenceToString(str);
        return null == resultStr || resultStr.length() <= 0 ? resultStr : resultStr.toLowerCase();
    }

    /**
     * 获取字符串长度，一个中文算2个字
     *
     * @param str 字符串内容
     * @return 字符串长度，一个中文算2个字
     */
    public static int getLengthCnTo2Size(CharSequence str) {
        if (null == str || str.length() <= 0) {
            return 0;
        }
        int length = 0;
        for (int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 127) {
                length++;
            } else {
                length += 2;
            }

        }
        return length;
    }

    /**
     * 判断字符串的字符是否包含中文字符，只要有字符是双字节字符才就是中文字符串。
     *
     * @param str 字符串内容
     * @return 字符串的字符是否包含中文字符
     */
    public static boolean isZhCNString(CharSequence str) {
        if (null == str || str.length() <= 0) {
            return false;
        }
        boolean b = false;
        for (int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if (ascii < 0 || ascii > 127) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * 判断字符串的字符是否全部中文字符，所有字符都是双字节字符才是中文字符串。
     *
     * @param str 字符串内容
     * @return 否符串的字符是否全部中文字符
     */
    public static boolean isZhCNStringAll(CharSequence str) {
        if (null == str || str.length() <= 0) {
            return false;
        }
        boolean b = true;
        for (int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 127) {
                b = false;
                break;
            }
        }
        return b;
    }

    public static boolean isEnStr(CharSequence str) {
        return !isZhCNString(str);
    }

    public static boolean isEquals(CharSequence str1, CharSequence str2) {
        return isEquals(str1, str2, false, false);
    }

    public static boolean isEqualsIgnoreCase(CharSequence str1, CharSequence str2) {
        return isEquals(str1, str2, false, true);
    }

    public static boolean isEqaulsIgnoreNULL(CharSequence str1, CharSequence str2) {
        return isEquals(str1, str2, true, false);
    }

    public static boolean isEqaulsIgnoreNULLandCase(CharSequence str1, CharSequence str2) {
        return isEquals(str1, str2, true, true);
    }

    /**
     * 比较两个字符串
     *
     * @param str1            字符串1
     * @param str2            字符串2
     * @param isNullEmptySame 是否null和empty相等
     * @param ignoreCase      是否忽略大小写
     * @return 如果两者都为空，则返回true 否则 返回实际
     */
    private static boolean isEquals(CharSequence str1, CharSequence str2, boolean isNullEmptySame, boolean ignoreCase) {
        if (isNullEmptySame) {
            String s1 = nullStrToEmpty(str1);
            String s2 = nullStrToEmpty(str2);
            if (ignoreCase) {
                return s1.equalsIgnoreCase(s2);
            } else {
                return s1.equals(s2);
            }
        } else {
            if (null == str1 && null == str2) {
                return true;
            } else if (null == str1 || null == str2) {
                return false;
            }
            String s1 = charSequenceToString(str1);
            String s2 = charSequenceToString(str2);
            if (null == s1 && null == s2) {
                return true;
            } else if (null == s1 || null == s2) {
                return false;
            }
            if (ignoreCase) {
                return s1.equalsIgnoreCase(s2);
            } else {
                return s1.equals(s2);
            }
        }
    }

    /**
     * 若是str长度小于等于length长度则取得整个字符串，若是String长度大于length长度，则取得length长度子字符，多余部分替换为...
     *
     * @param str    字符串
     * @param length 截取长度
     * @return
     */
    public static String getSubstr(CharSequence str, int length) {
        return getSubstr(str, length, 0, "...");
    }

    /**
     * 若是str长度小于等于length长度则取得整个字符串，若是String长度大于length长度，则取得length-repalceOffSet长度子字符，多余部分替换为...
     *
     * @param str           字符串
     * @param length        截取长度
     * @param repalceOffSet 替换偏移量
     * @return
     */
    public static String getSubstr(CharSequence str, int length, int repalceOffSet) {
        return getSubstr(str, length, repalceOffSet, "...");
    }

    /**
     * 若是str长度小于等于length长度则取得整个字符串，若是String长度大于length长度，则取得length-repalceOffSet长度子字符，多余部分替换为replaceStr
     *
     * @param str           字符串
     * @param length        截取长度
     * @param repalceOffSet 替换偏移量
     * @param repalceStr    替换字符串
     * @return
     */
    public static String getSubstr(CharSequence str, int length, int repalceOffSet, String repalceStr) {
        String s = charSequenceToString(str);
        if (null == s) {
            return null;
        } else {
            int tmp = repalceOffSet > 0 && repalceOffSet < length ? repalceOffSet : 0;
            return (s.length() > length) ? (s.substring(0, length - tmp) + (null == repalceStr ? STR_EMPTY_VALUE : repalceStr)) : s;
        }
    }

    /**
     * 取得从0开始长度为length的子字符串，如果str的长度小于length的长度则会抛出异常 <功能详细描述>
     *
     * @param str
     * @param length
     * @return [参数说明]
     * @return String [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @author lKF13186 2009-7-23
     * @see [类、类#方法、类#成员]
     */
    public static String substr(CharSequence str, int length) {
        return substring(str, 0, length);

    }

    public static String substr(CharSequence str, int beginIndex, int length) {
        return substring(str, beginIndex, beginIndex + length);

    }

    public static String substring(CharSequence str, int beginIndex) {
        String s = charSequenceToString(str);
        if (null == s) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "字符串截取时候该字符不能为NULL!");
        }
        return s.substring(beginIndex);
    }

    public static String substring(CharSequence str, int beginIndex, int endIndex) {
        String s = charSequenceToString(str);
        if (null == s) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "字符串截取时候该字符不能为NULL!");
        }
        try {
            return s.substring(beginIndex, endIndex);
        } catch (Exception e) {
            if (e instanceof StringIndexOutOfBoundsException) {
                throw new StringIndexOutOfBoundsException(e.getMessage());
            } else {
                throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "字符串截取时候出错，可能超过字符串边界!", e);
            }
        }
    }

    public static String firstToUpperCase(String str) {
        String s = charSequenceToString(str);
//		if (isEmpty(s)) {
//			return s;
//		}
//		else if (s.length() == 1) {
//			return s.toUpperCase();
//		}
//		else {
//			return s.substring(0, 1).toUpperCase() + s.substring(1);
//		}
        if (isEmpty(s)) {
            return s;
        }
        char c = s.charAt(0);
        return !Character.isLetter(c) || Character.isUpperCase(c) ? s
                : new StringBuilder(s.length()).append(Character.toUpperCase(c)).append(s.substring(1)).toString();
    }

    public static String firstToLowerCase(String str) {
        String s = charSequenceToString(str);
//		if (isEmpty(s)) {
//			return s;
//		}
//		else if (s.length() == 1) {
//			return s.toLowerCase();
//		}
//		else {
//			return s.substring(0, 1).toLowerCase() + s.substring(1);
//		}
        if (isEmpty(s)) {
            return s;
        }
        char c = s.charAt(0);
        return !Character.isLetter(c) || Character.isLowerCase(c) ? s
                : new StringBuilder(s.length()).append(Character.toLowerCase(c)).append(s.substring(1)).toString();
    }

    public static String charSequenceToString(CharSequence str) {
        if (null == str) {
            return null;
        } else if (str instanceof String) {
            return (String) str;
        } else {
            String s = str.toString();
            return s;
        }
    }

    public static String fillStringOnEnd(CharSequence charSequence, int length) {
        return fillString(charSequence, length, null, false, false);
    }

    public static String fillStringOnEnd(CharSequence charSequence, int length, String fillTag) {
        return fillString(charSequence, length, fillTag, false, false);
    }

    public static String fillStringOnEnd(CharSequence charSequence, int length, String fillTag, boolean isFillEmpty) {
        return fillString(charSequence, length, fillTag, false, isFillEmpty);
    }

    public static String fillStringOnStart(CharSequence charSequence, int length) {
        return fillString(charSequence, length, null, true, false);
    }

    public static String fillStringOnStart(CharSequence charSequence, int length, String fillTag) {
        return fillString(charSequence, length, fillTag, true, false);
    }

    public static String fillStringOnStart(CharSequence charSequence, int length, String fillTag, boolean isFillEmpty) {
        return fillString(charSequence, length, fillTag, true, isFillEmpty);
    }

    public static String fillStringOnCenter(CharSequence charSequence, int length) {
        return fillStringOnCenter(charSequence, length, null, false);
    }

    public static String fillStringOnCenter(CharSequence charSequence, int length, String fillTag) {
        return fillStringOnCenter(charSequence, length, fillTag, false);
    }

    private static String fillString(CharSequence charSequence, int length, String fillTag, boolean isAppendStart,
                                     boolean isFillEmpty) {
        if (null == charSequence) {
            return null;
        }
        String text = charSequenceToString(charSequence);
        if (null == text) {
            return null;
        }
        if (text.length() <= 0 && !isFillEmpty) {
            return text;
        }
        int size = text.length();
        int sizeOff = length - size;
        if (sizeOff <= 0) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String tag = null;
        if (!isEmpty(fillTag)) {
            tag = fillTag;
        } else {
            tag = " ";
        }
        for (int i = 0; i < sizeOff; i++) {
            stringBuilder.append(tag);
        }

        if (isAppendStart) {
            return stringBuilder.toString() + text;
        } else {
            return text + stringBuilder.toString();
        }
    }

    public static String fillStringOnCenter(CharSequence charSequence, int length, String fillTag,
                                            boolean isFillEmpty) {
        if (null == charSequence) {
            return null;
        }
        String text = charSequenceToString(charSequence);
        if (null == text) {
            return null;
        }
        if (text.length() <= 0 && !isFillEmpty) {
            return text;
        }
        int size = text.length();
        int sizeOff = length - size;
        if (sizeOff <= 0) {
            return text;
        }
        StringBuilder stringBuilderLeft = new StringBuilder();
        StringBuilder stringBuilderRight = new StringBuilder();
        int sizeLeft = sizeOff / 2;
        int sizeRight = sizeOff - sizeLeft;
        String tag = null;
        if (!isEmpty(fillTag)) {
            tag = fillTag;
        } else {
            tag = " ";
        }
        for (int i = 0; i < sizeLeft; i++) {
            stringBuilderLeft.append(tag);
        }
        for (int i = 0; i < sizeRight; i++) {
            stringBuilderRight.append(tag);
        }
        return stringBuilderLeft.toString() + text + stringBuilderRight.toString();

    }

    /**
     * 从HREF得到的innerHTML
     *
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>如果HREF为null，则返回""</li>
     * <li>如果不匹配REGX，返回源</li>
     * <li>返回匹配REGX的最后一个字符串</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return STR_EMPTY_VALUE;
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * 在html中处理特殊字符
     *
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return isEmpty(source) ? source
                : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;",
                "\"");
    }

    /**
     * 变换半宽字符到全角字符
     *
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param str
     * @return
     */
    public static String fullWidthToHalfWidth(CharSequence str) {
        String s = charSequenceToString(str);
        if (isEmpty(s)) {
            return s;
        }
        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 变换全宽字符到半角字符
     *
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param str
     * @return
     */
    public static String halfWidthToFullWidth(CharSequence str) {
        String s = charSequenceToString(str);
        if (isEmpty(s)) {
            return s;
        }
        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    // /**
    // * 数量转换为k比例 当值大于999时，以k为单位。如：1000显示为1.0k,1001至1100显示为1.1k,11000显示为11.0k ,10001至10999显示为10.1k
    // *
    // * @param nums
    // * @return 创建人：王龙能 2015年1月5日 下午4:06:59
    // */
    // public static String getKtointValue(int nums) {
    // return getNumberString(nums, 1000, "k");
    // }

    public static String getNumberStringByKB(long nums) {
        return getNumberStringByTagPoint(nums, 1000, "k", 1);
    }

    public static String getNumberStringByKB(long nums, int pointSize) {
        return getNumberStringByTagPoint(nums, 1000, "k", pointSize);
    }

    private static String getNumberStringByTagPoint(long nums, int perValue, String tag, int scale) {
        if (perValue < 2) {
            return String.valueOf(nums);
        }
        if (nums >= perValue) {
            BigDecimal bigDecimalVal = new BigDecimal(nums);
            BigDecimal bigDecimalPerVal = new BigDecimal(perValue);
            BigDecimal tmp = bigDecimalVal.divide(bigDecimalPerVal, scale, BigDecimal.ROUND_HALF_UP);
            return tmp.toString();
        } else {
            return String.valueOf(nums);
        }
    }

    /**
     * 删除小数点后面的0,例如：5.40 -> 5.4 ; 5.00->5 ; 5.04 不变
     */
    public static String removeZeroAfterDecimalPoint(CharSequence str) {
        String data = charSequenceToString(str);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(10);
        return nf.format(Double.valueOf(data));
    }

    /**
     * 字符串转换为NO BOM格式
     *
     * @param str 原始字符串
     * @return NO BOM字符串
     */
    public static String stringToNoBom(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        boolean isBom = false;
        int charCode = str.charAt(0);
        if (charCode == 65279) {
            isBom = true;
        }
        if (isBom) {
            return str.substring(1);
        } else {
            return str;
        }
    }

    /**
     * 字符串转换为BOM格式
     *
     * @param str            原始字符串
     * @param nullEmptyToBom 空字符串是否转换为BOM字符窜
     * @return BOM格式字符串
     */
    public static String stringToBom(String str, boolean nullEmptyToBom) {
        if (StringUtils.isEmpty(str) && !nullEmptyToBom) {
            if (nullEmptyToBom) {
                StringBuilder sb = new StringBuilder();
                char bomChar = 65279;
                sb.append(bomChar);
                return sb.toString();
            } else {
                return str;
            }
        } else {
            boolean isBom = false;
            int charCode = str.charAt(0);
            if (charCode == 65279) {
                isBom = true;
            }
            if (isBom) {
                return str;
            } else {
                StringBuilder sb = new StringBuilder();
                char bomChar = 65279;
                sb.append(bomChar);
                sb.append(str);
                return str;
            }
        }
    }

    public static List<String> getListByPage(CharSequence str, int pageSize) {
        String s = charSequenceToString(str);
        if (null == s || s.length() <= 0) {
            return null;
        }
        int len = s.length();

        List<String> list = new ArrayList<String>();
        if (pageSize < 1 || pageSize >= len) {
            list.add(s);
            return list;
        }
        int pageCount = len / pageSize;
        for (int i = 0; i < pageCount; i++) {
            list.add(s.substring(i * pageSize, (i + 1) * pageSize));
        }
        if (len % pageSize > 0) {
            list.add(s.substring(pageCount * pageSize));
        }
        return list;
    }

    public static List<String> getListString(CharSequence str, String split, boolean isPutEmpty) {
        return getList(str, split, String.class, isPutEmpty, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<String> getListString(CharSequence str, String split, boolean isPutEmpty, boolean trim) {
        return getList(str, split, String.class, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, null);
    }


    public static List<Integer> getListInteger(CharSequence str, String split, boolean isPutEmpty) {
        return getList(str, split, Integer.class, isPutEmpty, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Integer> getListInteger(CharSequence str, String split, boolean isPutEmpty, boolean trim) {
        return getList(str, split, Integer.class, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Long> getListLong(CharSequence str, String split, boolean isPutEmpty) {
        return getList(str, split, Long.class, isPutEmpty, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Long> getListLong(CharSequence str, String split, boolean isPutEmpty, boolean trim) {
        return getList(str, split, Long.class, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Double> getListDouble(CharSequence str, String split, boolean isPutEmpty) {
        return getList(str, split, Double.class, isPutEmpty, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Double> getListDouble(CharSequence str, String split, boolean isPutEmpty, boolean trim) {
        return getList(str, split, Double.class, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Float> getListFloat(CharSequence str, String split, boolean isPutEmpty) {
        return getList(str, split, Float.class, isPutEmpty, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static List<Float> getListFloat(CharSequence str, String split, boolean isPutEmpty, boolean trim) {
        return getList(str, split, Float.class, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static <T> List<T> getList(CharSequence str, String split, Class<T> tCls) {
        return getList(str, split, tCls, false, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static <T> List<T> getList(CharSequence str, String split, Class<T> tCls, boolean isPutEmpty) {
        return getList(str, split, tCls, isPutEmpty, false, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static <T> List<T> getList(CharSequence str, String split, Class<T> tCls, boolean isPutEmpty, boolean trim) {
        return getList(str, split, tCls, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, null);
    }

    public static <T> List<T> getList(CharSequence str, String split, Class<T> tCls, boolean isPutEmpty, boolean trim, String tag) {
        return getList(str, split, tCls, isPutEmpty, trim, Parse_StrToNumLst_By_BigDecimal, tag);
    }


    /**
     * 分割字符串为List类型
     *
     * @param str                       原始字符串
     * @param split                     分割标识
     * @param tCls                      目标类
     * @param isPutEmpty                empty和null是否放入
     * @param isParseNumberByBigDecimal 是否使用BigDecimal解析数字类型
     * @param trim                      是否进行trim
     * @param tag                       解析对象时候的标识符
     * @param <T>
     * @return 解析好的类型
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getList(CharSequence str, String split, Class<T> tCls, boolean isPutEmpty, boolean trim,
                                      boolean isParseNumberByBigDecimal, String tag) {
        String s = charSequenceToString(str);
        ArrayList<T> list = new ArrayList<T>();
        if (isEmpty(s)) {
            return list;
        }
        String[] strings = s.split(split);
        if (null == strings) {
            return list;
        }
        Method parseMethod = null;
        for (String tmpStr : strings) {
            String tmp;
            if (trim) {
                tmp = trim(tmpStr);
            } else {
                tmp = tmpStr;
            }
            if (null == tmp) {
                if (isPutEmpty) {
                    list.add(null);
                }
            } else if (tmp.length() <= 0) {
                if (isPutEmpty) {
                    T tVal = null;
                    try {
                        if (String.class == tCls) {
                            tVal = (T) tmp;
                        } else {
                            tVal = null;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        tVal = null;
                        log.error("Error:getList", e);

                    }
                    list.add(tVal);
                }
            } else {
                T tVal = null;
                try {
                    if (String.class == tCls) {
                        tVal = (T) tmp;
                    } else if (Integer.class == tCls) {
                        if (isParseNumberByBigDecimal) {
                            BigDecimal bigDecimal = new BigDecimal(tmp).setScale(0, BigDecimal.ROUND_HALF_UP);
                            Integer val = bigDecimal.intValue();
                            tVal = (T) val;
                        } else {
                            Integer val = Integer.valueOf(tmp);
                            tVal = (T) val;
                        }
                    } else if (Long.class == tCls) {
                        if (isParseNumberByBigDecimal) {
                            BigDecimal bigDecimal = new BigDecimal(tmp).setScale(0, BigDecimal.ROUND_HALF_UP);
                            Long val = bigDecimal.longValue();
                            tVal = (T) val;
                        } else {
                            Long val = Long.valueOf(tmp);
                            tVal = (T) val;
                        }
                    } else if (Float.class == tCls) {
                        if (isParseNumberByBigDecimal) {
                            BigDecimal bigDecimal = new BigDecimal(tmp);
                            Float val = bigDecimal.floatValue();
                            tVal = (T) val;
                        } else {
                            Float val = Float.valueOf(tmp);
                            tVal = (T) val;
                        }
                    } else if (Double.class == tCls) {
                        if (isParseNumberByBigDecimal) {
                            BigDecimal bigDecimal = new BigDecimal(tmp);
                            Double val = bigDecimal.doubleValue();
                            tVal = (T) val;
                        } else {
                            Double val = Double.valueOf(tmp);
                            tVal = (T) val;
                        }
                    } else if (BigDecimal.class == tCls) {
                        BigDecimal bigDecimal = new BigDecimal(tmp);
                        tVal = (T) bigDecimal;
                    } else if (Boolean.class == tCls) {
                        Boolean val = null;
                        if ("true".equalsIgnoreCase(tmp)) {
                            val = true;
                        } else {
                            val = false;
                        }
                        tVal = (T) val;
                    } else if (StringBuilder.class == tCls) {
                        StringBuilder val = new StringBuilder();
                        val.append(tmp);
                        tVal = (T) val;
                    } else if (StringBuffer.class == tCls) {
                        StringBuffer val = new StringBuffer();
                        val.append(tmp);
                        tVal = (T) val;
                    } else {
                        if (null == parseMethod) {
                            DefParseStrToObj parseAnnotation = tCls.getAnnotation(DefParseStrToObj.class);
                            String parseMethodName = null == parseAnnotation || isEmpty(parseAnnotation.parse())
                                    ? Parse_StrToObj_Method
                                    : parseAnnotation.parse();
                            if (isEmpty(tag)) {
                                parseMethod = tCls.getDeclaredMethod(parseMethodName, String.class);

                                parseMethod.setAccessible(true);
                            } else {
                                parseMethod = tCls.getDeclaredMethod(parseMethodName, String.class, String.class);
                                parseMethod.setAccessible(true);
                            }
                        }

                        if (isEmpty(tag)) {
                            tVal = tCls.newInstance();
                            parseMethod.invoke(tVal, tmp);
                        } else {
                            tVal = tCls.newInstance();
                            parseMethod.invoke(tVal, tmp, tag);
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    tVal = null;
                    log.error("Error:getList", e);
                }
                if (isPutEmpty) {
                    list.add(tVal);
                } else {
                    if (null != tVal) {
                        list.add(tVal);
                    }
                }
            }

        }
        return list;
    }

    public static <V> String appendString(List<V> list) {
        return appendString(list, ",", false, null);
    }

    public static <V> String appendString(List<V> list, String appendTag) {
        return appendString(list, appendTag, false, null);
    }

    public static <V> String appendString(List<V> list, String appendTag, boolean isEmptyPut) {
        return appendString(list, appendTag, isEmptyPut, null);
    }

    public static <V> String appendString(List<V> list, String appendTag, boolean isEmptyPut, String tag) {
        if (null == list || list.size() == 0) {
            return STR_EMPTY_VALUE;
        }
        Method parseMethod = null;
        int vSize = list.size();
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < vSize; index++) {
            int tmp1 = sb.length();
            V v = list.get(index);
            if (null != v && (v instanceof String || v instanceof Boolean || v instanceof Character
                    || v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof Float
                    || v instanceof CharSequence)) {
                sb.append(v);
            } else if (null != v) {
                String tmpVal = null;
                try {
                    if (null == parseMethod) {
                        Class<?> tCls = v.getClass();
                        DefParseObjToStr parseAnnotation = v.getClass().getAnnotation(DefParseObjToStr.class);
                        String parseMethodName = null == parseAnnotation || isEmpty(parseAnnotation.parse())
                                ? Parse_ObjToStr_Method
                                : parseAnnotation.parse();
                        if (isEmpty(tag)) {
                            parseMethod = tCls.getDeclaredMethod(parseMethodName);
                            parseMethod.setAccessible(true);
                        } else {
                            parseMethod = tCls.getDeclaredMethod(parseMethodName, String.class);
                            parseMethod.setAccessible(true);
                        }
                    }
                    if (isEmpty(tag)) {
                        tmpVal = (String) parseMethod.invoke(v);
                    } else {
                        tmpVal = (String) parseMethod.invoke(v, tag);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    tmpVal = null;
                    log.error("Error:appendString", e);
                }
                if (!isEmpty(tmpVal)) {
                    sb.append(tmpVal);
                }

            }
            int tmp2 = sb.length();
            if (isEmptyPut && index < vSize - 1) {
                sb.append(appendTag);
            } else {
                if (tmp2 > tmp1 && index < vSize - 1) {
                    sb.append(appendTag);
                }
            }
        }
        String string = new String(sb);
        return string;
    }

    public static <V> String appendString(V[] arrayData) {
        return appendString(arrayData, ",", false, null);
    }

    public static <V> String appendString(V[] arrayData, String appendTag) {
        return appendString(arrayData, appendTag, false, null);
    }

    public static <V> String appendString(V[] arrayData, String appendTag, boolean isEmptyPut) {
        return appendString(arrayData, appendTag, isEmptyPut, null);
    }

    public static <V> String appendString(V[] arrayData, String appendTag, boolean isEmptyPut, String tag) {
        if (null == arrayData || arrayData.length == 0) {
            return STR_EMPTY_VALUE;
        }
        Method parseMethod = null;
        int vSize = arrayData.length;
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < vSize; index++) {
            int tmp1 = sb.length();
            V v = arrayData[index];
            if (null != v && (v instanceof String || v instanceof Boolean || v instanceof Character
                    || v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof Float
                    || v instanceof CharSequence)) {
                sb.append(v);
            } else if (null != v) {
                String tmpVal = null;
                try {
                    if (null == parseMethod) {
                        Class<?> tCls = v.getClass();
                        DefParseObjToStr parseAnnotation = v.getClass().getAnnotation(DefParseObjToStr.class);
                        String parseMethodName = null == parseAnnotation || isEmpty(parseAnnotation.parse())
                                ? Parse_ObjToStr_Method
                                : parseAnnotation.parse();
                        if (isEmpty(tag)) {
                            parseMethod = tCls.getDeclaredMethod(parseMethodName);
                            parseMethod.setAccessible(true);
                        } else {
                            parseMethod = tCls.getDeclaredMethod(parseMethodName, String.class);
                            parseMethod.setAccessible(true);
                        }
                    }
                    if (isEmpty(tag)) {
                        tmpVal = (String) parseMethod.invoke(v);
                    } else {
                        tmpVal = (String) parseMethod.invoke(v, tag);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    tmpVal = null;
                    log.error("Error:appendString", e);
                }
                if (!isEmpty(tmpVal)) {
                    sb.append(tmpVal);
                }

            }
            int tmp2 = sb.length();
            if (isEmptyPut && index < vSize - 1) {
                sb.append(appendTag);
            } else {
                if (tmp2 > tmp1 && index < vSize - 1) {
                    sb.append(appendTag);
                }
            }
        }
        String string = new String(sb);
        return string;
    }

    /**
     * 获取匹配的字符串
     *
     * @param str
     * @param pattern
     * @return
     */
    public static String getMatchCode(String str, String pattern) {
        try {
            if (null != str && str.trim().length() > 0 && null != pattern && pattern.trim().length() > 0) {
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(str);
                while (matcher.find()) {
                    return matcher.group(0);
                }
            }
            return STR_EMPTY_VALUE;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:getMatchCode", e);
            return STR_EMPTY_VALUE;
        }

    }

    /**
     * 返回matcher的所有结果
     *
     * @param str
     * @param pattern
     * @return
     */
    public static List<String> getMatchCodeList(String str, String pattern) {
        try {
            if (null != str && str.trim().length() > 0 && null != pattern && pattern.trim().length() > 0) {
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(str);
                List<String> listStr = new ArrayList<String>();
                while (matcher.find()) {
                    listStr.add(matcher.group());
                }
                if (!listStr.isEmpty()) {
                    return listStr;
                }

            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:getMatchCodeList", e);
            return null;
        }

    }

    /**
     * 获取指定字符出现的次数
     *
     * @param str 源字符串
     * @param c   要查找的字符
     * @return
     */
    public static int counterAppearNum(CharSequence str, char c) {
        if (null == str || str.length() <= 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取指定字符串出现的次数
     *
     * @param str      源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int counterAppearNum(CharSequence str, String findText) {
        String s = charSequenceToString(str);
        if (null == s) {
            return 0;
        }
        int count = 0;
        try {
            Pattern p = Pattern.compile(findText);
            Matcher m = p.matcher(s);
            while (m.find()) {
                count++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:counterAppearNum", e);
        }
        return count;
    }

    public static int toInt(String s, int def) {
        Integer value = toInteger(s);
        return null == value ? def : value;
    }

    public static Integer toInteger(String s) {
        return toInteger(s, Parse_StrToNum_By_BigDecimal);
    }

    public static Integer toInteger(String s, boolean isParseNumberByBigDecimal) {
        Integer value = null;
        try {
            if (isParseNumberByBigDecimal) {
                BigDecimal bigDecimal = new BigDecimal(s).setScale(0, BigDecimal.ROUND_HALF_UP);
                value = bigDecimal.intValue();
            } else {
                value = Integer.parseInt(s);
            }
        } catch (Exception e) {
            value = null;
            log.error("Error:toInteger", e);
        }
        return value;
    }

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", STR_EMPTY_VALUE);
    }

    /**
     * 分割字符转转义
     *
     * @param splitTag 待转义的分割字符
     * @return 转义后的分割字符
     */

    public static String transSplitTag(String splitTag) {
        if (null == splitTag || splitTag.length() <= 0) {
            return splitTag;
        }
        if (splitTag.equals("|") || splitTag.equals(".") || splitTag.equals("*") || splitTag.equals("+")) {
            return "\\" + splitTag;
        } else {
            return splitTag;
        }
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str, String charsetName) {
        try {
            char[] chars = "0123456789abcdef".toCharArray();
            StringBuilder sb = new StringBuilder();
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] bs = str.getBytes(charset);
            int bit;

            for (int i = 0; i < bs.length; i++) {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 0x0f;
                sb.append(chars[bit]);
//			sb.append('');
            }
            return sb.toString().trim();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:str2HexStr", e);
            return null;
        }
    }

    public static String str2HexStr(String str) {
        return str2HexStr(str, null);
    }


    public static String getEncoding(String str) {
        if (null == str || str.length() <= 0) {
            log.error("Error:getEncoding->获取字符串编码时候，字符串不能为空");
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "获取字符串编码时候，字符串不能为空");
        }
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception e) {

        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception e) {

        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception e) {

        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception e) {

        }
        //如果都不是，说明输入的内容不属于常见的编码格式。
        return STR_EMPTY_VALUE;
    }

    public static boolean verifyEncoding(String str, String charsetName) {
        String encode = getEncoding(str);
        String logEncode = StringUtils.isEmpty(encode) ? "UNKNOWN" : encode;
        if (isEmpty(encode) || !encode.equalsIgnoreCase(charsetName)) {
            throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "字符串编码验证失败，必须是" + charsetName + "编码，但解析后的编码为" + logEncode);
        } else {
            return true;
        }
    }

    public static List<String> getListStringCSVMode(String str, String spilt) {
        return getListStringCSVMode(str, spilt, null, false);
    }

    public static List<String> getListStringCSVMode(String str, String spilt, String spiltTrans) {
        return getListStringCSVMode(str, spilt, spiltTrans, false);
    }

    /**
     * CSV模式获取字符串列表
     *
     * @param str            字符串
     * @param spilt          字符串分隔符
     * @param spiltTrans     字符串分割符号转义字符
     * @param forceTransBy0x 空的时候是否使用&0x转义
     * @return 字符串列表
     */
    public static List<String> getListStringCSVMode(String str, String spilt, String spiltTrans, boolean forceTransBy0x) {
        String spiltStr = null == spilt || spilt.length() <= 0 ? "," : spilt;
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        try {
            List<String> listStr = new ArrayList<>();
            String spiltTansStr;
            if (forceTransBy0x) {
                spiltTansStr = StringUtils.isEmpty(spiltTrans) ? "&0x" + StringUtils.str2HexStr(spiltStr) : spiltTrans;
            } else {
                spiltTansStr = spiltTrans;
            }
            String strItem = str;
            while (true) {
                if (null == strItem || strItem.length() <= 0) {
                    listStr.add(strItem);
                    break;
                }
                int tmpIndex = strItem.indexOf(spiltStr);
                // 不含有则添加并跳过
                if (tmpIndex < 0) {
                    String tmpStrAdd = strItem;
                    if (null != tmpStrAdd && tmpStrAdd.length() > 0 && null != spiltTansStr && spiltTansStr.length() > 0) {
                        tmpStrAdd = tmpStrAdd.replace(spiltTansStr, spiltStr);
                    }
                    listStr.add(tmpStrAdd);
                    strItem = null;
                    break;
                } else {
                    String tmpStrAdd = strItem.substring(0, tmpIndex);
                    if (null != tmpStrAdd && tmpStrAdd.length() > 0 && null != spiltTansStr && spiltTansStr.length() > 0) {
                        tmpStrAdd = tmpStrAdd.replace(spiltTansStr, spiltStr);
                    }
                    listStr.add(tmpStrAdd);
                    strItem = strItem.substring(tmpIndex + 1);
                }

            }
            return listStr;
        } catch (Exception e) {
            // TODO: handle exception

            log.error("Error:getListStringCSVMode", e);
            return null;

        }
    }


}
