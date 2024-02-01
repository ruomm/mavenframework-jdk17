/**
 * @copyright wanruome-2020
 * @author 牛牛-wanruome@163.com
 * @create 2020年3月19日 下午4:30:33
 */
package com.ruomm.webx.validatorx.util;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class BindResultUtil {
    private final static Log log = LogFactory.getLog(BindResultUtil.class);
    private static boolean IS_SHOW_FIELD_NAME = true;

    public static void setShowFieldName(boolean isShowFieldName) {
        IS_SHOW_FIELD_NAME = isShowFieldName;
    }

    public static List<String> errToList(BindingResult bindingResult) {
        return errToList(bindingResult, IS_SHOW_FIELD_NAME);
    }

    public static List<String> errToList(BindingResult bindingResult, boolean isShowFieldName) {
        if (null == bindingResult || !bindingResult.hasErrors()) {
            return null;
        }
        List<String> listBindErrors = new ArrayList<String>();
        try {

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                String tag = "";
                if (objectError instanceof FieldError) {
                    FieldError error = (FieldError) objectError;
                    tag = error.getField();
                } else {
                    tag = objectError.getObjectName();
                }
                String tmpMsg = objectError.getDefaultMessage();
                String tmpMsgTrim = null == tmpMsg ? null : tmpMsg.trim();
                if (null == tmpMsgTrim || tmpMsgTrim.length() <= 0) {
                    listBindErrors.add(tag + "值错误");
                } else {
                    listBindErrors.add(isShowFieldName ? tag + tmpMsgTrim : tmpMsgTrim);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:errToList", e);
        }
        if (listBindErrors.size() <= 0) {
            listBindErrors.add("对象校验错误");
        }
        return listBindErrors;

    }

    public static String errToStrFirst(BindingResult bindingResult) {
        return errToStrFirst(bindingResult, IS_SHOW_FIELD_NAME);
    }

    public static String errToStrFirst(BindingResult bindingResult, boolean isShowFieldName) {
        if (null == bindingResult || !bindingResult.hasErrors()) {
            return null;
        }
        String msg = null;
        try {
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                String tag = "";
                if (objectError instanceof FieldError) {
                    FieldError error = (FieldError) objectError;
                    tag = error.getField();
                } else {
                    tag = objectError.getObjectName();
                }
                String tmpMsg = objectError.getDefaultMessage();
                String tmpMsgTrim = null == tmpMsg ? null : tmpMsg.trim();
                if (null == tmpMsgTrim || tmpMsgTrim.length() <= 0) {
                    msg = tag + "值错误";
                } else {
                    msg = isShowFieldName ? tag + tmpMsgTrim : tmpMsgTrim;
                }
                break;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:errToStrFirst", e);
        }

        if (null == msg || msg.length() <= 0) {
            return "对象校验错误";
        } else {
            return msg;
        }
    }

    public static String errToStrAll(BindingResult bindingResult) {
        return errToStrAll(bindingResult, IS_SHOW_FIELD_NAME);
    }

    public static String errToStrAll(BindingResult bindingResult, boolean isShowFieldName) {
        if (null == bindingResult || !bindingResult.hasErrors()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                String tag = "";
                if (objectError instanceof FieldError) {
                    FieldError error = (FieldError) objectError;
                    tag = error.getField();
                } else {
                    tag = objectError.getObjectName();
                }
                String tmpMsg = objectError.getDefaultMessage();
                String tmpMsgTrim = null == tmpMsg ? null : tmpMsg.trim();
                if (null == tmpMsgTrim || tmpMsgTrim.length() <= 0) {
                    sb.append(tag + "值错误");
                } else {
                    sb.append(isShowFieldName ? tag + tmpMsgTrim : tmpMsgTrim);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:errToStrAll", e);
        }
        if (sb.length() > 0) {
            return sb.toString();
        } else {
            return "对象校验错误";
        }
    }
}