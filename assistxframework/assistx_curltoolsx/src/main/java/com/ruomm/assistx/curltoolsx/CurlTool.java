/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年1月30日 下午1:33:44
 */
package com.ruomm.assistx.curltoolsx;

import com.ruomm.assistx.curltoolsx.core.HttpUtil;
import com.ruomm.assistx.curltoolsx.core.ResponseText;
import com.ruomm.javax.corex.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurlTool {
    /**
     * -g -p -d "" -d url -b -l
     *
     * @param args
     */
    private static boolean isLog = false;

    public static void main(String[] args) {
        try {
            String requestUrl = null;
            boolean isPost = false;
            boolean isAjax = false;
            boolean isHelp = false;
            boolean isJsonMode = false;
            String charsetName = null;
            List<String> paramList = new ArrayList<>();
            Map<String, Integer> dcMapCount = null;
            int argsSize = null == args ? 0 : args.length;
            for (int i = 0; i < argsSize; ) {
                if ("-help".equals(args[i]) || "-h".equals(args[i])) {
                    isHelp = true;
                    i++;
                    break;
                }
                if (i == argsSize - 1) {
                    requestUrl = args[i];
                } else if ("-g".equals(args[i])) {
                    isPost = false;
                    isJsonMode = false;
                } else if ("-p".equals(args[i])) {
                    isPost = true;
                    isJsonMode = false;
                } else if ("-pj".equals(args[i])) {
                    isPost = true;
                    isJsonMode = true;
                } else if ("-b".equals(args[i])) {
                    isAjax = true;
                } else if ("-l".equals(args[i])) {
                    isLog = true;
                } else if ("-c".equals(args[i])) {
                    if (i + 1 < argsSize - 1) {
                        charsetName = args[i + 1];
                        i++;
                    }
                } else if ("-d".equals(args[i])) {
                    if (i + 1 < argsSize - 1) {
                        paramList.add(args[i + 1]);
                        i++;
                    }
                } else if ("-dc".equals(args[i])) {
                    if (i + 1 < argsSize - 1) {
                        dcMapCount = parseDcCountMap(args[i + 1]);
                        i++;
                    }
                }
                i++;
            }
            if (isHelp) {
                System.out.println("CurlTool Java版本帮助文档");
                System.out.println("传递的参数值会在发送请求时候URLEncode编码，-d传递时候不需要URLEncode编码；");
                System.out.println("请求的URL路径是作为最后一个参数传入的；");
                System.out.println("默认不加-b、-p、-pj、-g、-l: 同步执行，get模式，不打印请求结果；");
                System.out.println("\"-d\"后面填入参数键值对，一个\"-d\"对应一个键值对，可以有多个\"-d\"和键值对；");
                System.out.println("特殊字符(如\"、\')请注意转义。\"转移为\\\",\'转移为\\\',换行转移为\\r\\n或\\n");
                System.out.println("-h或-help:    打印请求结果");
                System.out.println("-d:    参数键值对，如\"name=张三\"");
                System.out.println("-dc:   参数键值对的分割个数，如\"pszCount=100\"");
                System.out.println("-b:    异步执行，后续进程无间隔执行");
                System.out.println("-p:    post模式x-www-form-urlencoded格式传输");
                System.out.println("-pj:   post模式json格式传输。-pj和-p互斥");
                System.out.println("-g:    get模式");
                System.out.println("-c:    字符集编码方式，如\"-c utf-8\"、\"-c gbk\"等，默认\"-c utf-8\"");
                System.out.println("-l:    打印请求结果");
            } else {
                if (StringUtils.isEmpty(requestUrl) || !(requestUrl.toLowerCase().startsWith("http:")
                        || requestUrl.toLowerCase().startsWith("https:"))) {
                    System.out.println("ERROR:    请求路径错误，不能为空且必须以http:或https:开头");
                    return;
                }
                Map<String, String> paramMap = parseParam(paramList);
                if (null == paramMap || paramMap.size() <= 0) {
                    isPost = false;
                }
//				for (String keyString : paramMap.keySet()) {
//					System.out.println(keyString + ":" + paramMap.get(keyString));
//				}
                String dcName = null;
                int dcCount = 0;
                if (null != dcMapCount && dcMapCount.size() == 1 && null != paramMap && paramMap.size() > 0) {
                    for (String key : dcMapCount.keySet()) {
                        if (paramMap.containsKey(key)) {
                            dcName = key;
                            dcCount = dcMapCount.get(key);
                        }
                    }
                }
                if (null == dcName || dcName.length() <= 0) {
                    if (isPost) {
                        doPost(requestUrl, paramMap, charsetName, isAjax, isJsonMode);
                    } else {
                        doGet(requestUrl, paramMap, charsetName, isAjax);
                    }
                } else {
                    List<String> dcParamValuesList = parseDcCoutList(paramMap.get(dcName), dcCount);
                    if (null == dcParamValuesList || dcParamValuesList.size() <= 1) {
                        if (isPost) {
                            doPost(requestUrl, paramMap, charsetName, isAjax, isJsonMode);
                        } else {
                            doGet(requestUrl, paramMap, charsetName, isAjax);
                        }
                    } else {
                        for (String tmp : dcParamValuesList) {
                            Map<String, String> paramMapSub = new HashMap<>();
                            for (String key : paramMap.keySet()) {
                                if (dcName.equals(key)) {
                                    paramMapSub.put(key, tmp);
                                } else {
                                    paramMapSub.put(key, paramMap.get(key));
                                }
                            }
                            if (isPost) {
                                doPost(requestUrl, paramMapSub, charsetName, isAjax, isJsonMode);
                            } else {
                                doGet(requestUrl, paramMapSub, charsetName, isAjax);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private static List<String> parseDcCoutList(String dcVal, int dcCount) {
        if (null == dcVal || dcVal.length() <= 0 || dcCount <= 0) {
            return null;
        }
        String[] dcParamValues = dcVal.split(",");
        List<String> dcParamValuesList = new ArrayList<>();
        StringBuilder sb = null;
        for (int i = 0; i < dcParamValues.length; i++) {
            if (dcCount == 1) {
                if (null != dcParamValues[i] && dcParamValues[i].length() > 0) {
                    dcParamValuesList.add(dcParamValues[i]);
                }
            } else {
                if (i % dcCount == 0) {
                    if (null != sb && sb.length() > 0) {
                        dcParamValuesList.add(sb.toString());
                    }
                    sb = new StringBuilder();
                    if (null != dcParamValues[i] && dcParamValues[i].length() > 0) {
                        sb.append(dcParamValues[i]);
                    }
                } else {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    if (null != dcParamValues[i] && dcParamValues[i].length() > 0) {
                        sb.append(dcParamValues[i]);
                    }
                }
            }
        }
        if (null != sb && sb.length() > 0) {
            dcParamValuesList.add(sb.toString());
        }
        return dcParamValuesList;
    }

    private static Map<String, Integer> parseDcCountMap(String tmp) {
        if (StringUtils.isEmpty(tmp)) {
            return null;
        }
        int tmpIndex = tmp.indexOf("=");
        if (tmpIndex <= 0) {
            return null;
        }
        String tmpKey = null;
        try {
            tmpKey = tmp.substring(0, tmpIndex);
        } catch (Exception e) {
            tmpKey = "";
        }
        String tmpVal = null;
        try {
            tmpVal = tmp.substring(tmpIndex + 1);
        } catch (Exception e) {
            tmpVal = "";
        }
        if (StringUtils.isEmpty(tmpKey) || StringUtils.isEmpty(tmpVal)) {
            return null;
        }
        int val = 0;
        try {
            val = Integer.parseInt(tmpVal);
        } catch (Exception e) {
            val = 0;
        }
        if (val <= 0) {
            return null;
        }
        Map<String, Integer> dcCountMap = new HashMap<>();
        dcCountMap.put(tmpKey, val);
        return dcCountMap;

    }

    private static Map<String, String> parseParam(List<String> paramList) {
        if (null == paramList || paramList.size() <= 0) {
            return null;
        }
        Map<String, String> paramMap = new HashMap<>();
        for (String tmp : paramList) {
            if (StringUtils.isEmpty(tmp)) {
                continue;
            }
            int index = tmp.indexOf("=");
            if (index <= 0) {
                continue;
            }
            String key = null;
            try {
                key = tmp.substring(0, index);
            } catch (Exception e) {
                key = "";
            }
            String val = null;
            try {
                val = tmp.substring(index + 1);
            } catch (Exception e) {
                val = "";
            }
            if (StringUtils.isEmpty(key) || paramMap.containsKey(key)) {
                continue;
            }
            val = val.replace("\\n", "\n").replace("\\r", "\r");
            paramMap.put(key, val);
        }
        return paramMap;
    }

    private static void doGet(final String requestUrl, final Map<String, String> paramMap, final String charsetName,
                              boolean isAjax) {
        if (isAjax) {
            new Thread() {
                @Override
                public void run() {
                    ResponseText responseText = HttpUtil.get(requestUrl, paramMap, charsetName);
                    doLog(responseText);
                }

                ;
            }.start();
        } else {
            ResponseText responseText = HttpUtil.get(requestUrl, paramMap, charsetName);
            doLog(responseText);
        }
    }

    private static void doPost(final String requestUrl, final Map<String, String> paramMap, final String charsetName,
                               boolean isAjax, final boolean isJsonMode) {
        if (isAjax) {
            new Thread() {
                @Override
                public void run() {
                    if (!isJsonMode) {
                        ResponseText responseText = HttpUtil.post(requestUrl, paramMap, charsetName);
                        doLog(responseText);
                    } else {
                        ResponseText responseText = HttpUtil.postJson(requestUrl, paramMap, charsetName);
                        doLog(responseText);
                    }
                }

                ;
            }.start();
        } else {
            if (!isJsonMode) {
                ResponseText responseText = HttpUtil.post(requestUrl, paramMap, charsetName);
                doLog(responseText);
            } else {
                ResponseText responseText = HttpUtil.postJson(requestUrl, paramMap, charsetName);
                doLog(responseText);
            }
        }
    }

    private static void doLog(ResponseText responseText) {
        if (!isLog) {
            return;
        }
        if (null == responseText) {
            System.out.println("ERROR:    请求结果NULL");
        } else if (StringUtils.isEmpty(responseText.resultString)) {
            System.out.println("CODE:    " + responseText.status);
            System.out.println("请求结果Result:    NULL");
        } else {
            System.out.println("CODE:    " + responseText.status);
            System.out.println("请求结果Result:    ");
            System.out.println(responseText.resultString);
        }
    }
}
