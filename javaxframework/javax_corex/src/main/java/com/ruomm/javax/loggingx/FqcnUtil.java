package com.ruomm.javax.loggingx;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/15 14:40
 */
public class FqcnUtil {
    public static StackTraceElement getStackTraceElementByFqcn(StackTraceElement[] stackTraceElements, String fqcn, String loggerName) {
        if (null == stackTraceElements || stackTraceElements.length <= 0) {
            System.out.println("statck数组为空，找不到日志的StackTraceElement");
            return null;
        }
        int stackTraceSize = stackTraceElements.length;
        String packageName = FqcnUtil.class.getPackage().getName() + ".";
        StackTraceElement fqcnStackTraceElement = null;
        StackTraceElement nameStackTraceElement = null;
        StackTraceElement packStackTraceElement = null;
        for (int i = stackTraceSize - 1; i >= 0; i--) {
//            System.out.println(String.format("%04d",i)+"stackTrace:"+stackTraceElements[i].getClassName()
//                    +":"+stackTraceElements[i].getMethodName()
//                    +":"+stackTraceElements[i].getLineNumber());

            StackTraceElement stack = stackTraceElements[i];
            String stackClassName = stack.getClassName();
            if (null == stackClassName || stackClassName.length() <= 0) {
                //  若是获取不到className，继续找下一个StackTraceElement
                continue;
            }
            if (null != fqcn && fqcn.length() > 0 && stackClassName.equalsIgnoreCase(fqcn)) {
                // 若是找到fqcn节点，设置fqcnStackTrace为上一StackTraceElement，并结束循环
                int indexFqcn = i + 1;
                if (indexFqcn >= 0 && indexFqcn < stackTraceSize) {
                    fqcnStackTraceElement = stackTraceElements[indexFqcn];
                    break;
                }
            } else if (null != loggerName && loggerName.length() >= 0 && stackClassName.equalsIgnoreCase(loggerName)) {
                // 若是StackTraceElement的className和loggerName相同，nameStackTrace
                nameStackTraceElement = stack;
            } else if (stackClassName.startsWith(packageName)) {
                // 若是找到本包节点，设置packStackTrace为上一StackTraceElement并结束循环
                int indexFqcn = i + 1;
                if (indexFqcn >= 0 && indexFqcn < stackTraceSize) {
                    packStackTraceElement = stackTraceElements[indexFqcn];
                }
                break;
            }

        }
        StackTraceElement resultStackTraceElement = null;
        if (null != fqcnStackTraceElement) {
//            System.out.println("依据fqcn找到日志的StackTraceElement");
            resultStackTraceElement = fqcnStackTraceElement;
        } else if (null != nameStackTraceElement) {
//            System.out.println("依据loggerName找到日志的StackTraceElement");
            resultStackTraceElement = nameStackTraceElement;
        } else if (null != packStackTraceElement) {
//            System.out.println("依据packageName找到日志的StackTraceElement");
            resultStackTraceElement = packStackTraceElement;
        } else {
//            System.out.println("依据statck数组找到日志的StackTraceElement");
            if (stackTraceSize >= 4) {
                resultStackTraceElement = stackTraceElements[3];
            } else {
                resultStackTraceElement = stackTraceElements[stackTraceSize - 1];
            }
        }
        return resultStackTraceElement;
    }

    public static String parseSimpleClassName(String className) {
        if (null == className || className.length() <= 0) {
            return className;
        }
        int lastIndex = className.lastIndexOf(".");
        if (lastIndex >= 0 && lastIndex < className.length() - 1) {
            return className.substring(lastIndex + 1);
        } else {
            return className;
        }
    }
}
