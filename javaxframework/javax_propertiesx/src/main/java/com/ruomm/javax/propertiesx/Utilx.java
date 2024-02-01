/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月29日 下午3:19:34
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.util.ArrayList;
import java.util.List;

class Utilx {
    private final static Log log = LogFactory.getLog(Utilx.class);
    public final static String BR_START = "<br>";
    public final static String BR_END = "</br>";
    public final static String BR_ONE = "<br/>";
    private final static int INOUT_BUFF_SIZE = 1024;

    public static String fillZero(int val, int length) {
        String valStr = val + "";
        int valLength = valStr.length();
        int offsetLength = length - valLength;
        if (offsetLength <= 0) {
            return valStr;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < offsetLength; i++) {
            sb.append("0");
        }
        sb.append(valStr);
        return sb.toString();
    }

    /**
     * 解析字符串列表为PropertyItem对象列表
     * @param lstLineStr 字符串列表
     * @param emptyAsNull 空值是否作为NULL
     * @return PropertyItem对象列表
     */
    public static List<PropertyItem> listStrToPropertyItemList(List<String> lstLineStr, String keyValueAppends, boolean emptyAsNull) {
        if (null == lstLineStr) {
            return null;
        }
        List<PropertyItem> listPropertyItem = new ArrayList<>();
        if (null == lstLineStr || lstLineStr.size() <= 0) {
            return listPropertyItem;
        }
        StringBuilder sbCommit = new StringBuilder();
        boolean isCommitStart = false;
        boolean isCommitEnd = false;
        for (String lineStr : lstLineStr) {
            // 开始注释行判断
            int commitStartIndex = -1;
            int commitEndIndex = -1;
            //符合代码监测
            if (!isCommitStart && !isCommitEnd) {
                sbCommit.setLength(0);
                commitStartIndex = parseCommitStartIndex(lineStr);
                if (commitStartIndex >= 0) {
                    isCommitStart = true;
                    commitEndIndex = parseCommitEndIndex(lineStr);
                    if (commitEndIndex >= 0) {
                        isCommitEnd = true;
                    }
                }
            } else if (isCommitStart && !isCommitEnd) {
                commitEndIndex = parseCommitEndIndex(lineStr);
                if (commitEndIndex >= 0) {
                    isCommitEnd = true;
                }
            }
            if (isCommitStart) {
                //注释在同一行
                if (commitStartIndex >= 0 && commitEndIndex >= 0) {
                    if (commitStartIndex + 1 >= commitEndIndex) {
                        sbCommit.append(" ");
                    } else {
                        sbCommit.append(lineStr.substring(commitStartIndex + 1, commitEndIndex));
                    }
                } else if (commitStartIndex >= 0) {
                    sbCommit.append(lineStr.substring(commitStartIndex + 1));
                    sbCommit.append("\n");
                } else if (commitEndIndex >= 0) {
                    sbCommit.append(lineStr.substring(0, commitEndIndex));
                }
            }
            if (isCommitEnd) {
                PropertyItem propertyItem = new PropertyItem();
                propertyItem.setCommit(sbCommit.toString());
                propertyItem.setMode(ItemMode.COMMIT_MULTI);
                listPropertyItem.add(propertyItem);
                isCommitEnd = false;
                isCommitStart = false;
                sbCommit.setLength(0);
                continue;
            } else if (isCommitStart) {
                continue;
            }
            // 结束注释行判断

            PropertyItem propertyItem = lineStrToPropertyItem(lineStr, keyValueAppends);
            if (null == propertyItem || null == propertyItem.getMode()) {
                continue;
            }
            // 若是换行符号，获取最后一个值得拼接
            if (ItemMode.APPEND == propertyItem.getMode()) {
                int tmpLstSize = listPropertyItem.size();
                PropertyItem previewItem = tmpLstSize <= 0 ? null : listPropertyItem.get(tmpLstSize - 1);
                // 如是前一个不存在或者不是KEY_VALUE_LINE则跳过
                if (null == previewItem || ItemMode.KEY_VAL != previewItem.getMode()) {
                    continue;
                }
                // 依据前一个值和当前值拼接为真实值
                String previewVal = previewItem.getValue();
                String itemVal = propertyItem.getValue();
                StringBuilder sbVal = new StringBuilder();
                if (!StringUtils.isEmpty(previewVal)) {
                    sbVal.append(previewVal);
                }
                if (!StringUtils.isEmpty(itemVal)) {
                    sbVal.append(itemVal);
                }
                if (sbVal.length() <= 0) {
                    if (emptyAsNull) {
                        previewItem.setValue(null);
                    } else {
                        previewItem.setValue("");
                    }
                } else {
                    previewItem.setValue(sbVal.toString());
                }
                continue;
            } else {
                addPropertyItem(listPropertyItem, propertyItem);
            }
        }
        return listPropertyItem;
    }

    /**
     * 解析字符串为PropertyItem对象
     * @param lineStr 字符串
     * @return PropertyItem对象
     */
    public static PropertyItem lineStrToPropertyItem(String lineStr, String keyValueAppends) {
        String str = StringUtils.trim(lineStr);

        if (StringUtils.isEmpty(str)) {
            // 空行返回不合法
            PropertyItem propertyItem = new PropertyItem();
            propertyItem.setMode(ItemMode.EMPTY);
            propertyItem.setKey(null);
            propertyItem.setValue(null);
            propertyItem.setCommit(null);
            propertyItem.setLineStr(null == lineStr ? "" : lineStr);
            return propertyItem;
        } else if (str.startsWith("#")) {
            // 注释行,此行commit截取并trim
            PropertyItem propertyItem = new PropertyItem();
            propertyItem.setMode(ItemMode.COMMIT);
            propertyItem.setKey(null);
            propertyItem.setValue(null);
            propertyItem.setCommit(tranCommitStr(str, "#"));
            propertyItem.setLineStr(null);
            return propertyItem;
        } else if (str.startsWith("//")) {
            // 注释行
            PropertyItem propertyItem = new PropertyItem();
            propertyItem.setMode(ItemMode.COMMIT);
            propertyItem.setKey(null);
            propertyItem.setValue(null);
            propertyItem.setCommit(tranCommitStr(str, "/"));
            propertyItem.setLineStr(null);
            return propertyItem;
        } else if (str.startsWith("----")) {
            // 注释行
            PropertyItem propertyItem = new PropertyItem();
            propertyItem.setMode(ItemMode.COMMIT);
            propertyItem.setKey(null);
            propertyItem.setValue(null);
            propertyItem.setCommit(tranCommitStr(str, "-"));
            propertyItem.setLineStr(null);
            return propertyItem;
        }
        String strLower = str.toLowerCase();
        if (strLower.toLowerCase().startsWith(BR_START) || strLower.startsWith(BR_ONE)) {
            // 换行拼接行
            String tmpData = parsePropertyValue(lineStr);
            PropertyItem propertyItem = new PropertyItem();
            propertyItem.setMode(ItemMode.APPEND);
            propertyItem.setKey(null);
            propertyItem.setValue(tmpData);
            propertyItem.setCommit(null);
            propertyItem.setLineStr(null);
            return propertyItem;
        }
        int stSplit = indexOfKeyValSeparator(str, keyValueAppends);
        if (stSplit > 0) {
            // 键值对行
            String key = str.substring(0, stSplit).trim();
            String val = parsePropertyValue(str.substring(stSplit + 1));
            if (null != key && key.length() > 0) {
                PropertyItem propertyItem = new PropertyItem();
                propertyItem.setMode(ItemMode.KEY_VAL);
                propertyItem.setKey(key);
                propertyItem.setValue(val);
                propertyItem.setCommit(null);
                propertyItem.setLineStr(null);
                return propertyItem;
            } else {
                PropertyItem propertyItem = new PropertyItem();
                propertyItem.setMode(ItemMode.NO_DATA);
                propertyItem.setKey(null);
                propertyItem.setValue(null);
                propertyItem.setCommit(null);
                propertyItem.setLineStr(lineStr);
                return propertyItem;
            }
        } else {
            PropertyItem propertyItem = new PropertyItem();
            propertyItem.setMode(ItemMode.NO_DATA);
            propertyItem.setKey(null);
            propertyItem.setValue(null);
            propertyItem.setCommit(null);
            propertyItem.setLineStr(lineStr);
            return propertyItem;
        }
    }

    // 解析PropertyItem的value数据,可以使用trim函数
    public static String parsePropertyValue(String valueStr) {
        String str = StringUtils.trim(valueStr);

        if (null == str || str.length() <= 0) {
            return "";
        }
        String strLowerCase = str.toLowerCase();
        String tmpHeader = null;
        String tmpContent = null;
        if (strLowerCase.startsWith(BR_ONE)) {
            tmpHeader = "";
            tmpContent = StringUtils.trim(str.substring(BR_ONE.length()));
        } else if (strLowerCase.startsWith(BR_START)) {
            //查找BR_END位置
            int index = strLowerCase.indexOf(BR_END);
            if (index > 0) {
                tmpHeader = str.substring(BR_START.length(), index).replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t");
                tmpContent = StringUtils.trim(str.substring(index + BR_END.length()));
            } else {
                tmpHeader = "";
                tmpContent = StringUtils.trim(str.substring(BR_START.length()));
            }
        } else {
            tmpHeader = "";
            tmpContent = str;
        }
        if (tmpContent.startsWith("\"") && tmpContent.endsWith("\"") && tmpContent.length() > 1) {
            int subEnd = tmpContent.length() - 1;
            tmpContent = tmpContent.substring(1, subEnd);
        }
        return tmpHeader + tmpContent;
    }

    //解析value为输出字段
    public static String parsePropertyPrint(String valueStr, int lineSize) {
        if (null == valueStr || valueStr.length() <= 0) {
            return "";
        }
        List<ItemStore> lstStoreNoLine = new ArrayList<ItemStore>();
        int valueStrSzie = valueStr.length();
        for (int i = 0; i < valueStrSzie; i++) {
            //获取最后一个itemStore
            if (lstStoreNoLine.size() <= 0) {
                lstStoreNoLine.add(new ItemStore());
            }
            ItemStore preViewItemStore = lstStoreNoLine.get(lstStoreNoLine.size() - 1);
            String strUnit = valueStr.substring(i, i + 1);
            if (strUnit.equals("\r")) {
                if (StringUtils.isEmpty(preViewItemStore.getConent())) {
                    String header = StringUtils.nullStrToEmpty(preViewItemStore.getHeader()) + "\\r";
                    preViewItemStore.setHeader(header);
                } else {
                    ItemStore itemStore = new ItemStore();
                    itemStore.setHeader("\\r");
                    lstStoreNoLine.add(itemStore);
                }
            } else if (strUnit.equals("\n")) {
                if (StringUtils.isEmpty(preViewItemStore.getConent())) {
                    String header = StringUtils.nullStrToEmpty(preViewItemStore.getHeader()) + "\\n";
                    preViewItemStore.setHeader(header);
                } else {
                    ItemStore itemStore = new ItemStore();
                    itemStore.setHeader("\\n");
                    lstStoreNoLine.add(itemStore);
                }
            } else if (strUnit.equals("\t")) {
                if (StringUtils.isEmpty(preViewItemStore.getConent())) {
                    String header = StringUtils.nullStrToEmpty(preViewItemStore.getHeader()) + "\\t";
                    preViewItemStore.setHeader(header);
                } else {
                    ItemStore itemStore = new ItemStore();
                    itemStore.setHeader("\\t");
                    lstStoreNoLine.add(itemStore);
                }
            } else {
                String content = StringUtils.nullStrToEmpty(preViewItemStore.getConent()) + strUnit;
                preViewItemStore.setConent(content);
            }
        }
        List<ItemStore> lstStoreLine = new ArrayList<ItemStore>();
        for (ItemStore itemStore : lstStoreNoLine) {
            if (null == itemStore.getHeader()) {
                itemStore.setHeader("");
            }
            if (null == itemStore.getConent()) {
                itemStore.setConent("");
            }
            List<String> lstContent = StringUtils.getListByPage(itemStore.getConent(), lineSize);
            if (null == lstContent) {
                lstStoreLine.add(itemStore);
            } else {
                int lstContentSize = lstContent.size();
                for (int i = 0; i < lstContentSize; i++) {
                    ItemStore lineStroeItem = new ItemStore();
                    if (i == 0) {
                        lineStroeItem.setHeader(itemStore.getHeader());
                    } else {
                        lineStroeItem.setHeader("");
                    }
                    lineStroeItem.setConent(lstContent.get(i));
                    lstStoreLine.add(lineStroeItem);
                }
            }
        }
        for (ItemStore itemStore : lstStoreLine) {
            if (null == itemStore.getHeader()) {
                itemStore.setHeader("");
            }
            if (null == itemStore.getConent()) {
                itemStore.setConent("");
            } else if (itemStore.getConent().trim().length() != itemStore.getConent().length()) {
                String content = "\"" + itemStore.getConent() + "\"";
                itemStore.setConent(content);
            } else if (itemStore.getConent().toLowerCase().startsWith(BR_START) || itemStore.getConent().toLowerCase().startsWith(BR_ONE)) {
                String content = "\"" + itemStore.getConent() + "\"";
                itemStore.setConent(content);
            } else if (itemStore.getConent().startsWith("\"") || itemStore.getConent().endsWith("\"")) {
                String content = "\"" + itemStore.getConent() + "\"";
                itemStore.setConent(content);
            }
        }
        int lstStoreLineSize = lstStoreLine.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lstStoreLineSize; i++) {
            ItemStore itemStore = lstStoreLine.get(i);
            if (i > 0) {
                sb.append("\r\n");
            }
            if (StringUtils.isEmpty(itemStore.getHeader())) {
                if (i > 0) {
                    sb.append(BR_ONE);
                }
            } else {
                sb.append(BR_START).append(itemStore.getHeader()).append(BR_END);
            }
            if (!StringUtils.isEmpty(itemStore.getConent())) {
                sb.append(itemStore.getConent());
            }
        }
        return sb.toString();
    }


    // 添加一个PropertyItem项目
    public static void addPropertyItem(List<PropertyItem> lstPropertyItems, PropertyItem propertyItem) {
        if (ItemMode.KEY_VAL != propertyItem.getMode()) {
            lstPropertyItems.add(propertyItem);
        } else if (StringUtils.isEmpty(propertyItem.getKey())) {
            return;
        } else {
            List<PropertyItem> listPropertyItemsDel = new ArrayList<PropertyItem>();
            for (PropertyItem tmpItem : lstPropertyItems) {
                if (ItemMode.KEY_VAL != tmpItem.getMode()) {
                    continue;
                }
                if (tmpItem.getKey().equals(propertyItem.getKey())) {
                    listPropertyItemsDel.add(tmpItem);
                }
            }
            if (listPropertyItemsDel.size() > 0) {
                lstPropertyItems.removeAll(listPropertyItemsDel);
            }
            lstPropertyItems.add(propertyItem);
        }
    }

    /**
     * 转换注释字符串
     */
    public static String tranCommitStr(String str, String commitHeader) {
        int strSize = str.length();
        StringBuilder sb = new StringBuilder();
        boolean isStart = false;
        for (int i = 0; i < strSize; i++) {

            String tmp = str.substring(i, i + 1);
            if (!isStart) {
                if (null == commitHeader) {
                    isStart = true;
                } else if (!commitHeader.equals(tmp)) {
                    isStart = true;
                }
            }
            if (isStart) {
                sb.append(tmp);
            }
        }
        return sb.toString();
    }

    /**
     * 获取注释字符串开头
     */
    public static String tranCommitHeader(String lineStr) {
        String str = StringUtils.trim(lineStr);
        if (StringUtils.isEmpty(str)) {
            return "#";
        } else if (str.startsWith("#")) {
            return "#";
        } else if (str.startsWith("//")) {
            return "//";
        } else if (str.startsWith("----")) {
            return "----";
        } else {
            return "#";
        }
    }

    private static String[] parseKeyValueAppendArray(String keyValueAppends) {
        String[] fuhaoAppends = null;
        if (null == keyValueAppends || keyValueAppends.length() <= 0) {
            fuhaoAppends = new String[]{"=", ":", "："};
        } else {
            String[] tmpFuhaoAppends = keyValueAppends.split(",");
            boolean isOk = true;
            if (null == tmpFuhaoAppends || tmpFuhaoAppends.length <= 0) {
                isOk = false;
            } else {
                for (String tmp : tmpFuhaoAppends) {
                    if (null == tmp || tmp.length() != 1) {
                        isOk = false;
                        break;
                    }
                }
            }
            if (isOk) {
                fuhaoAppends = tmpFuhaoAppends;
            } else {
                fuhaoAppends = new String[]{"=", ":", "："};
            }

        }
        return fuhaoAppends;
    }

    public static String parseKeyValueAppendValue(String keyValueAppends) {
        String[] fuhaoAppends = parseKeyValueAppendArray(keyValueAppends);
        if (null == fuhaoAppends || fuhaoAppends.length <= 0) {
            return "=";
        } else {
            return fuhaoAppends[0];
        }
    }

    /**
     * 查找键值对分隔符的开始位置
     *
     * @param keyValStr 键值对字符串
     * @return 键值对分隔符的开始位置
     */
    public static int indexOfKeyValSeparator(String keyValStr, String keyValueAppends) {
        if (StringUtils.isEmpty(keyValStr)) {
            return -1;
        }
        String[] fuhaoAppends = parseKeyValueAppendArray(keyValueAppends);
        int index = -1;
        for (String tmp : fuhaoAppends) {
            int tmpIndex = keyValStr.indexOf(tmp);
            if (tmpIndex < 0) {
                continue;
            }
            if (index < 0) {
                index = tmpIndex;
                continue;
            }
            if (tmpIndex < index) {
                index = tmpIndex;
            }
        }
        return index;
    }

    /**
     * 获取多行注释的开始位置
     * @param lineStr 行字符串
     * @return 行注释的开始位置
     */
    public static int parseCommitStartIndex(String lineStr) {
        if (StringUtils.isEmpty(lineStr)) {
            return -1;
        }
        int startIndex = -1;
        int firstIndex = -1;
        int lineStrSize = lineStr.length();
        boolean isStart = false;
        for (int i = 0; i < lineStrSize; i++) {
            char tmpChar = lineStr.charAt(i);
            if (!isStart) {
                if (tmpChar >= 0 && tmpChar <= 32) {
                    continue;
                } else if (tmpChar == '/') {
                    isStart = true;
                    continue;
                } else {
                    break;
                }
            } else {
                if (firstIndex > 0 && i > firstIndex + 1) {
                    break;
                }
                if (tmpChar >= 0 && tmpChar <= 32) {
                    continue;
                } else if (tmpChar == '*') {
                    startIndex = i;
                    if (firstIndex < 0) {
                        firstIndex = i;
                    }
                    continue;
                } else {
                    break;
                }
            }
        }
        return startIndex;
    }

    /**
     * 获取多行注释的结束位置
     * @param lineStr 行字符串
     * @return 行注释的开始位置
     */
    private static int parseCommitEndIndex(String lineStr) {
        if (StringUtils.isEmpty(lineStr)) {
            return -1;
        }
        int startIndex = -1;
        int firstIndex = -1;
        int lineStrSize = lineStr.length();
        boolean isStart = false;
        for (int i = lineStrSize - 1; i >= 0; i--) {
            char tmpChar = lineStr.charAt(i);
            if (!isStart) {
                if (tmpChar >= 0 && tmpChar <= 32) {
                    continue;
                } else if (tmpChar == '/') {
                    isStart = true;
                    continue;
                } else {
                    break;
                }
            } else {
                if (firstIndex > 0 && i < firstIndex - 1) {
                    break;
                }
                if (tmpChar >= 0 && tmpChar <= 32) {
                    continue;
                } else if (tmpChar == '*') {
                    startIndex = i;
                    if (firstIndex < 0) {
                        firstIndex = i;
                    }
                    continue;
                } else {
                    break;
                }
            }
        }
        return startIndex;
    }

}
