/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月3日 上午11:24:56
 */
package com.ruomm.javax.tuominx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

class UtilJavax {
    /**
     * 依据名称取得对象的field字段
     * @param obj 对象类
     * @param publicSeach 是否搜索类继承的public
     * @param parentSearch 是否搜索类的父类
     * @param parentLevel 父类搜索深度，若是小于等于0不限制
     * @param fieldName filed字段名称
     * @return 对象的field字段
     */
    public static Field parseFieldByObject(Object obj, boolean publicSeach, boolean parentSearch, int parentLevel, String fieldName) {
        if (null == obj) {
            return null;
        }
        return parseFieldByClass(obj.getClass(), publicSeach, parentSearch, parentLevel, fieldName);
    }

    /**
     * 依据名称取得对象的field字段
     * @param cls 对象类
     * @param publicSeach 是否搜索类继承的public
     * @param parentSearch 是否搜索类的父类
     * @param parentLevel 父类搜索深度，若是小于等于0不限制
     * @param fieldName filed字段名称
     * @return 对象的field字段
     */
    public static Field parseFieldByClass(Class<?> cls, boolean publicSeach, boolean parentSearch, int parentLevel, String fieldName) {
        if (null == cls) {
            return null;
        }
        if (null == fieldName || fieldName.length() <= 0) {
            return null;
        }
        Class<?> fieldClass = cls;
        int cycleCount = 0;
        Field fileResult = null;
        while (null != fieldClass) {
            try {
                fileResult = fieldClass.getDeclaredField(fieldName);
            } catch (Exception e) {
                if (!(e instanceof NoSuchFieldException)) {
                    e.printStackTrace();
                }
                fileResult = null;
            }
            if (null == fileResult && publicSeach && cycleCount == 0) {
                try {
                    fileResult = fieldClass.getField(fieldName);
                } catch (Exception e) {
                    if (!(e instanceof NoSuchFieldException)) {
                        e.printStackTrace();
                    }
                    fileResult = null;
                }
            }
            cycleCount++;
            if (!parentSearch) {
                fieldClass = null;
                break;
            } else {
                if (parentLevel > 0 && cycleCount >= parentLevel) {
                    fieldClass = null;
                    break;
                } else if (null != fileResult) {
                    fieldClass = null;
                    break;
                } else {
                    fieldClass = fieldClass.getSuperclass();
                    if (fieldClass == Object.class) {
                        fieldClass = null;
                        break;
                    }
                }
            }
        }
        return fileResult;
    }

    /**
     * 依据名称取得对象的method方法
     * @param obj 对象
     * @param publicSeach 是否搜索类继承的public
     * @param parentSearch 是否搜索类的父类
     * @param parentLevel 父类搜索深度，若是小于等于0不限制
     * @param methodName method方法名称
     * @param parameterTypes method方法参数
     * @return 对象的method方法
     */
    public static Method parseMethodByObject(Object obj, boolean publicSeach, boolean parentSearch, int parentLevel, String methodName, Class<?>... parameterTypes) {
        if (null == obj) {
            return null;
        }
        return parseMethodByClass(obj.getClass(), publicSeach, parentSearch, parentLevel, methodName, parameterTypes);
    }

    /**
     * 依据名称取得对象的method方法
     * @param cls 对象类
     * @param publicSeach 是否搜索类继承的public
     * @param parentSearch 是否搜索类的父类
     * @param parentLevel 父类搜索深度，若是小于等于0不限制
     * @param methodName method方法名称
     * @param parameterTypes method方法参数
     * @return 对象的method方法
     */
    public static Method parseMethodByClass(Class<?> cls, boolean publicSeach, boolean parentSearch, int parentLevel, String methodName, Class<?>... parameterTypes) {
        if (null == cls) {
            return null;
        }
        if (null == methodName || methodName.length() <= 0) {
            return null;
        }
        Class<?> fieldClass = cls;
        int cycleCount = 0;
        Method methodResult = null;
        while (null != fieldClass) {
            try {
                methodResult = fieldClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (Exception e) {
                if (!(e instanceof NoSuchMethodException)) {
                    e.printStackTrace();
                }
                methodResult = null;
            }
            if (null == methodResult && publicSeach && cycleCount == 0) {
                try {
                    methodResult = fieldClass.getMethod(methodName, parameterTypes);
                } catch (Exception e) {
                    if (!(e instanceof NoSuchMethodException)) {
                        e.printStackTrace();
                    }
                    methodResult = null;
                }
            }
            cycleCount++;
            if (!parentSearch) {
                fieldClass = null;
                break;
            } else {
                if (parentLevel > 0 && cycleCount >= parentLevel) {
                    fieldClass = null;
                    break;
                } else if (null != methodResult) {
                    fieldClass = null;
                    break;
                } else {
                    fieldClass = fieldClass.getSuperclass();
                    if (fieldClass == Object.class) {
                        fieldClass = null;
                        break;
                    }
                }
            }
        }
        return methodResult;
    }

    /**
     * 获取对象所在类中所有的DeclaredFields
     * @param obj 对象
     * @param parentSearch 是否搜索类的父类
     * @param parentLevel 父类搜索深度，若是小于等于0不限制
     * @param annotationClass 是否注解，有注解的话取得特定注解的DeclaredFields,没有注解的话取得全部DeclaredFields
     * @return 对象所在类中所有的DeclaredFields
     */
    public static List<Field> parseTuominFieldsByObject(Object obj, boolean parentSearch, int parentLevel, Class<? extends Annotation> annotationClass) {
        if (null == obj) {
            return null;
        }
        return parseTuominFieldsByClass(obj.getClass(), parentSearch, parentLevel, annotationClass);
    }

    /**
     * 获取类中所有的DeclaredFields
     * @param cls 对象类
     * @param parentSearch 是否搜索类的父类
     * @param parentLevel 父类搜索深度，若是小于等于0不限制
     * @param annotationClass 是否注解，有注解的话取得特定注解的DeclaredFields,没有注解的话取得全部DeclaredFields
     * @return 类中所有的DeclaredFields
     */
    public static List<Field> parseTuominFieldsByClass(Class<?> cls, boolean parentSearch, int parentLevel, Class<? extends Annotation> annotationClass) {
        if (null == cls) {
            return null;
        }
        List<Field> listFields = new ArrayList<>();
        Class<?> fieldClass = cls;
        int cycleCount = 0;
        while (null != fieldClass) {
            Field[] fieldsTmp = fieldClass.getDeclaredFields();
            if (null != fieldsTmp && fieldsTmp.length > 0) {
                for (Field fieldTmp : fieldsTmp) {
                    // 静态跳过
                    if (Modifier.isStatic(fieldTmp.getModifiers())) {
                        continue;
                    }
                    // final类型跳过
                    if (Modifier.isFinal(fieldTmp.getModifiers())) {
                        continue;
                    }
                    if (null != annotationClass && null == fieldTmp.getAnnotation(annotationClass)) {
                        continue;
                    }
                    listFields.add(fieldTmp);
                }
            }
            cycleCount++;
            if (!parentSearch) {
                fieldClass = null;
                break;
            } else {
                if (parentLevel > 0 && cycleCount >= parentLevel) {
                    fieldClass = null;
                    break;
                } else {
                    fieldClass = fieldClass.getSuperclass();
                    if (fieldClass == Object.class) {
                        fieldClass = null;
                        break;
                    }
                }
            }
        }
        return listFields;
    }
}
