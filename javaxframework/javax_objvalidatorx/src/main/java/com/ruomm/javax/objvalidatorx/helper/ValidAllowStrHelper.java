/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月2日 下午4:17:16
 */
package com.ruomm.javax.objvalidatorx.helper;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.annotation.ValidAllowStr;

import java.util.ArrayList;
import java.util.List;

public class ValidAllowStrHelper extends ObjValidHelper<ValidAllowStr> {
    private final static Log log = LogFactory.getLog(ValidAllowStrHelper.class);

    public ValidAllowStrHelper(Class<ValidAllowStr> clazz) {
        super(clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDefaultValidMessage() {
        // TODO Auto-generated method stub
        return "不在可选内容中(ValidAllowStr验证不通过)";
    }

    @Override
    public ObjValidResult verify(Object object, ValidAllowStr validAnnotation, String fieldTag, Object fieldObj, String valueStr) {
        // TODO Auto-generated method stub
        if (null == valueStr || valueStr.length() <= 0) {
            return createSuccessResult();
        }
        if (null == validAnnotation.allowStr() || validAnnotation.allowStr().length() <= 0) {
            return createSuccessResult();
        }
        String splitTag = null == validAnnotation.splitTag() || validAnnotation.splitTag().length() <= 0 ? ","
                : validAnnotation.splitTag();
        List<String> allowStrLst = StringUtils.getListString(validAnnotation.allowStr(), splitTag, false);
        if (null == allowStrLst || allowStrLst.size() <= 0) {
            return createFailResult(validAnnotation, fieldTag);
        }
        if (validAnnotation.isSingleSelect()) {
            boolean isContainer = false;
            for (String tmp : allowStrLst) {
                boolean tmpFlag = false;
                if (validAnnotation.ignoreCase()) {
                    tmpFlag = valueStr.equalsIgnoreCase(tmp);
                } else {
                    tmpFlag = valueStr.equals(tmp);
                }
                if (tmpFlag) {
                    isContainer = tmpFlag;
                    break;
                }
            }
            if (!isContainer) {
                return createFailResult(validAnnotation, fieldTag);
            } else {
                return createSuccessResult();
            }
        } else {
            String[] fieldStrs = valueStr.split(splitTag);
            if (null == fieldStrs || fieldStrs.length <= 0) {
                return createFailResult(validAnnotation, fieldTag);
            }
            boolean isValid = true;
            for (String subFieldStr : fieldStrs) {
                if (null == subFieldStr || subFieldStr.length() <= 0) {
                    isValid = false;
                    break;
                }
                boolean isContainer = false;
                for (String tmp : allowStrLst) {
                    if (null == tmp || tmp.length() <= 0) {
                        continue;
                    }
                    boolean tmpFlag = false;
                    if (validAnnotation.ignoreCase()) {
                        tmpFlag = subFieldStr.equalsIgnoreCase(tmp);
                    } else {
                        tmpFlag = subFieldStr.equals(tmp);
                    }
                    if (tmpFlag) {
                        isContainer = tmpFlag;
                        break;
                    }
                }
                if (!isContainer) {
                    isValid = false;
                    break;
                }
            }

            if (!validAnnotation.isSameSelect() && isValid) {
                List<String> tmpList = new ArrayList<String>();
                for (String subFieldStr : fieldStrs) {
                    if (null == subFieldStr || subFieldStr.length() <= 0) {
                        continue;
                    }
                    String tmpStr = validAnnotation.ignoreCase() ? subFieldStr.toLowerCase() : subFieldStr;
                    if (!tmpList.contains(tmpStr)) {
                        tmpList.add(tmpStr);
                    }
                }
                if (tmpList.size() != fieldStrs.length) {
                    isValid = false;
                }
            }
            if (!isValid) {
                return createFailResult(validAnnotation, fieldTag);
            } else {
                return createSuccessResult();
            }
        }

    }

    private String getParamStr(String[] allowStrs) {
        if (null == allowStrs || allowStrs.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String tmp : allowStrs) {
            if (null == tmp || tmp.length() <= 0) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append("、");
            }
            sb.append(tmp);
        }
        return sb.length() > 0 ? sb.toString() : "";
    }

    @Override
    public String parseValidMessage(ValidAllowStr validAnnotation) {
        // TODO Auto-generated method stub
        if (null == validAnnotation.message() || validAnnotation.message().length() <= 0) {
            return validAnnotation.message();
        }
        String replaceStr = null;
        if (null == validAnnotation.allowStr() || validAnnotation.allowStr().length() <= 0) {
            replaceStr = "";
        } else {
            try {
                String splitTag = null == validAnnotation.splitTag() || validAnnotation.splitTag().length() <= 0 ? ","
                        : validAnnotation.splitTag();
                String[] allowStrs = validAnnotation.allowStr().split(splitTag);
                replaceStr = getParamStr(allowStrs);
            } catch (Exception e) {
                // TODO: handle exception
                replaceStr = "";
                log.debug("Error:parseValidMessage", e);
            }

        }
        try {
            return validAnnotation.message().replace("{allowStr}", replaceStr);
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:parseValidMessage", e);
            return validAnnotation.message();
        }

    }
}
