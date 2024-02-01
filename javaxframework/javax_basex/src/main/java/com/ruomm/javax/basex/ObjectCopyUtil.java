/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月5日 上午10:06:32
 */
package com.ruomm.javax.basex;

import com.ruomm.javax.basex.annotation.DefCopyField;
import com.ruomm.javax.basex.annotation.DefCopyType;
import com.ruomm.javax.corex.ObjectUtils;
import com.ruomm.javax.corex.exception.JavaxCorexException;
import com.ruomm.javax.corex.helper.ClassListHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectCopyUtil {
    private final static Log log = LogFactory.getLog(ObjectCopyUtil.class);
    private static boolean Is_Exception_Print = false;

    /**
     * 设置异常是否打印错误日志
     *
     * @param isExceptionPrint 设置异常是否打印错误日志
     */
    public static void setExceptionPrint(boolean isExceptionPrint) {
        Is_Exception_Print = isExceptionPrint;
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj 原始对象，非空
     * @param dstObj 目标对象，非空
     * @return 源对象和目标对象复制结果
     * @paramDefault tag    复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @paramDefault defineCopyMaps 自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @paramDefault defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     * @paramDefault isEmptyAsNull 复制时候源字符窜空是否转换为NULL。默认值为true
     */
    public static boolean copyObject(Object srcObj, Object dstObj) {
        return copyObjectCommon(srcObj, dstObj, null, null, null, true);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj        原始对象，非空
     * @param dstObj        目标对象，非空
     * @param tag           复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @param isEmptyAsNull 复制时候源字符窜空是否转换为NULL。默认值为true
     * @return 源对象和目标对象复制结果
     * @paramDefault defineCopyMaps 自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @paramDefault defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     */
    public static boolean copyObject(Object srcObj, Object dstObj, String tag, boolean isEmptyAsNull) {
        return copyObjectCommon(srcObj, dstObj, null, null, tag, isEmptyAsNull);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj         原始对象，非空
     * @param dstObj         目标对象，非空
     * @param defineCopyMaps 自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @return 源对象和目标对象复制结果
     * @paramDefault tag    复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @paramDefault defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     * @paramDefault isEmptyAsNull 复制时候源字符窜空是否转换为NULL。默认值为true
     */
    public static boolean copyObject(Object srcObj, Object dstObj, Map<String, String> defineCopyMaps) {
        return copyObjectCommon(srcObj, dstObj, defineCopyMaps, null, null, true);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj         原始对象，非空
     * @param dstObj         目标对象，非空
     * @param tag            复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @param defineCopyMaps 自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @param isEmptyAsNull  复制时候源字符窜空是否转换为NULL。默认值为true
     * @return 源对象和目标对象复制结果
     * @paramDefault defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     */
    public static boolean copyObject(Object srcObj, Object dstObj, Map<String, String> defineCopyMaps, String tag, boolean isEmptyAsNull) {
        return copyObjectCommon(srcObj, dstObj, defineCopyMaps, null, tag, isEmptyAsNull);
    }


    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj          原始对象，非空
     * @param dstObj          目标对象，非空
     * @param defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     * @return 源对象和目标对象复制结果
     * @paramDefault tag    复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @paramDefault defineCopyMaps 自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @paramDefault isEmptyAsNull 复制时候源字符窜空是否转换为NULL。默认值为true
     */
    public static boolean copyObjectRename(Object srcObj, Object dstObj, Map<String, String> defineRenameMap) {
        return copyObjectCommon(srcObj, dstObj, null, defineRenameMap, null, true);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj          原始对象，非空
     * @param dstObj          目标对象，非空
     * @param tag             复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @param defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     * @param isEmptyAsNull   复制时候源字符窜空是否转换为NULL。默认值为true
     * @return 源对象和目标对象复制结果
     * @paramDefault defineCopyMaps 自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     */
    public static boolean copyObjectRename(Object srcObj, Object dstObj, Map<String, String> defineRenameMap, String tag, boolean isEmptyAsNull) {
        return copyObjectCommon(srcObj, dstObj, null, defineRenameMap, tag, isEmptyAsNull);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj          原始对象，非空
     * @param dstObj          目标对象，非空
     * @param defineCopyMaps  自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @param defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     * @return 源对象和目标对象复制结果
     * @paramDefault tag    复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @paramDefault isEmptyAsNull 复制时候源字符窜空是否转换为NULL。默认值为true
     */
    public static boolean copyObjectRename(Object srcObj, Object dstObj, Map<String, String> defineCopyMaps, Map<String, String> defineRenameMap) {
        return copyObjectCommon(srcObj, dstObj, defineCopyMaps, defineRenameMap, null, true);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj          原始对象，非空
     * @param dstObj          目标对象，非空
     * @param tag             复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。默认值为NULL
     * @param defineCopyMaps  自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。默认值为NULL
     * @param defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制。默认值为NULL
     * @param isEmptyAsNull   复制时候源字符窜空是否转换为NULL。默认值为true
     * @return 源对象和目标对象复制结果
     */
    public static boolean copyObjectRename(Object srcObj, Object dstObj, Map<String, String> defineCopyMaps, Map<String, String> defineRenameMap, String tag, boolean isEmptyAsNull) {
        return copyObjectCommon(srcObj, dstObj, defineCopyMaps, defineRenameMap, tag, isEmptyAsNull);
    }

    /**
     * 源对象和目标对象复制方法
     *
     * @param srcObj          原始对象
     * @param dstObj          目标对象
     * @param tag             复制时候的Tag，依据tag可以获取不同的copyMapMethod 和 copyMethod。
     * @param defineCopyMaps  自定义复制时候源字段和目标字段的对象列表。此列表不为空存在,则DefCopyFiled的isCopy和dstName失效，DefCopyType的copyMapMethod方法失效。
     * @param defineRenameMap 若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制
     * @param isEmptyAsNull   复制时候源字符窜空是否转换为NULL。
     * @return 源对象和目标对象复制结果
     */
    private static boolean copyObjectCommon(Object srcObj, Object dstObj, Map<String, String> defineCopyMaps, Map<String, String> defineRenameMap, String tag, boolean isEmptyAsNull) {
        if (null == srcObj || null == dstObj) {
            return false;
        }
        ClassListHelper classListHelper = new ClassListHelper() {
            @Override
            public boolean isBreakList(boolean isSuperClass, Class<?> classzz) {
                if (!isSuperClass) {
                    return false;
                } else {
                    DefCopyType defCopyType = classzz.getAnnotation(DefCopyType.class);
                    if (null == defCopyType) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };
        List<Class<?>> listClass = ObjectUtils.listClassByObject(srcObj, ObjectUtils.CLASS_DEEP_COUNT, classListHelper);
        if (null == listClass || listClass.size() <= 0) {
            return false;
        }
        Class<?> dstCls = dstObj.getClass();
        for (int i = 0; i < listClass.size(); i++) {
            Class<?> fieldClass = listClass.get(i);
            CopyTypeDal copyTypeDal = new CopyTypeDal();
            copyTypeDal.isCopy = true;
            copyTypeDal.defaultCopy = true;
            copyTypeDal.copyMapMethod = "";
            copyTypeDal.isOverWrite = true;
            copyTypeDal.isNullOverWrite = false;
            if (i == 0) {
                DefCopyType antCopyType = fieldClass.getAnnotation(DefCopyType.class);
                if (null == antCopyType) {
                    copyTypeDal.isCopy = true;
                    copyTypeDal.defaultCopy = true;
                    copyTypeDal.copyMapMethod = "";
                } else {
                    copyTypeDal.isCopy = antCopyType.isCopy();
                    copyTypeDal.defaultCopy = antCopyType.defaultCopy();
                    copyTypeDal.copyMapMethod = antCopyType.copyMapMethod();
                    copyTypeDal.isOverWrite = antCopyType.isOverWrite();
                    copyTypeDal.isNullOverWrite = antCopyType.isNullOverWrite();
                }
            } else {
                DefCopyType antCopyType = fieldClass.getAnnotation(DefCopyType.class);
                if (null == antCopyType) {
                    copyTypeDal.isCopy = false;
                    break;
                } else {
                    copyTypeDal.isCopy = antCopyType.isCopy();
                    copyTypeDal.defaultCopy = antCopyType.defaultCopy();
                    copyTypeDal.copyMapMethod = antCopyType.copyMapMethod();
                    copyTypeDal.isOverWrite = antCopyType.isOverWrite();
                    copyTypeDal.isNullOverWrite = antCopyType.isNullOverWrite();
                }
            }
            if (!copyTypeDal.isCopy) {
                continue;
            }
            Map<String, String> fieldCopyMap = null;
            if (null != defineCopyMaps && defineCopyMaps.size() > 0) {
                fieldCopyMap = defineCopyMaps;
            } else if (null != copyTypeDal.copyMapMethod && copyTypeDal.copyMapMethod.length() > 0) {
                boolean isExp = false;
                if (null == tag || tag.length() <= 0) {
                    boolean isNoSuchMethodException = false;
                    try {
                        Method method = fieldClass.getDeclaredMethod(copyTypeDal.copyMapMethod);
                        method.setAccessible(true);
                        fieldCopyMap = (Map<String, String>) method.invoke(srcObj);
                    } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                             IllegalArgumentException
                             | InvocationTargetException e) {
                        // TODO: handle exception
                        fieldCopyMap = null;
                        if (e instanceof NoSuchMethodException) {
                            isNoSuchMethodException = true;
                        } else {
                            isExp = true;
                            log.error("Error:copyObject", e);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        fieldCopyMap = null;
                        isExp = true;
                        log.error("Error:copyObject", e);
                    }
                    if (isNoSuchMethodException) {
                        isNoSuchMethodException = false;
                        try {
                            Method method = fieldClass.getDeclaredMethod(copyTypeDal.copyMapMethod, String.class);
                            method.setAccessible(true);
                            fieldCopyMap = (Map<String, String>) method.invoke(srcObj, "");
                        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                                 IllegalArgumentException
                                 | InvocationTargetException e) {
                            // TODO: handle exception
                            fieldCopyMap = null;
                            isExp = true;
                            log.error("Error:copyObject", e);
                        } catch (Exception e) {
                            // TODO: handle exception
                            fieldCopyMap = null;
                            isExp = true;
                            log.error("Error:copyObject", e);
                        }
                    }
                } else {
                    //获取带参数的方法
                    try {
                        Method method = fieldClass.getDeclaredMethod(copyTypeDal.copyMapMethod, String.class);
                        method.setAccessible(true);
                        fieldCopyMap = (Map<String, String>) method.invoke(srcObj, tag);
                    } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                             IllegalArgumentException
                             | InvocationTargetException e) {
                        // TODO: handle exception
                        fieldCopyMap = null;
                        isExp = true;
                        log.error("Error:copyObject", e);
                    } catch (Exception e) {
                        // TODO: handle exception
                        fieldCopyMap = null;
                        isExp = true;
                        log.error("Error:copyObject", e);
                    }
                }
                if (isExp) {
                    throw new JavaxCorexException(JavaxCorexException.CODE_ERR_JAVAX_COREX, "ObjectCopyUtil invoke method get fieldCopyMap occur error!");
//					continue;
                }
            }

            if (null != fieldCopyMap && fieldCopyMap.size() > 0) {
                Set<String> mapCopyFieldKeySet = fieldCopyMap.keySet();
                for (String tmpKey : mapCopyFieldKeySet) {
                    Field srcItemField = null;
                    try {
                        srcItemField = fieldClass.getDeclaredField(tmpKey);
                    } catch (NoSuchFieldException e) {
                        if (Is_Exception_Print) {
                            log.debug("Error:copyObject", e);
                        }
                    }
                    invokeField(copyTypeDal, fieldClass, srcItemField, srcObj, dstCls, dstObj, fieldCopyMap, defineRenameMap, tag, isEmptyAsNull);
                }
            } else {
                Field[] fields = fieldClass.getDeclaredFields();
                for (Field srcItemField : fields) {
                    invokeField(copyTypeDal, fieldClass, srcItemField, srcObj, dstCls, dstObj, null, defineRenameMap, tag, isEmptyAsNull);
                }
            }
        }
        return true;
    }

    private static class CopyTypeDal {
        // 定义是否复制
        boolean isCopy = true;
        // 字段不注解是否默认复制
        boolean defaultCopy = true;
        // 复制的字段map列表
        String copyMapMethod = "";
        // true时候原值不为NULL则复制，false时候目标值为NULL，原值不为NULL则复制
        boolean isOverWrite = true;
        // true时候原值不为NULL则复制，false时候目标值为NULL，原值不为NULL则复制
        boolean isNullOverWrite = false;

    }

    private static class CopyFieldDal {
        // 定义是否复制
        public boolean isCopy = false;
        // 定义复制目标名称
        public String dstName = "";
        // 定义复制时候给字段解析的方法，解析后的值复制到目标字段
        public String copyMethod = "";
        //义字段解析的方法时候是否使用field值作为参数，默认false
        public boolean copyMethodWithFieldValue = false;
        // true时候原值不为NULL则复制，false时候目标值为NULL，原值不为NULL则复制
        public boolean isOverWrite = true;
        // true时候始终使用原值替换目标值，fasle时候只有原值不为NULL才替换目标值，此必须在isOverWrite为true时候起作用
        public boolean isNullOverWrite = false;
    }

    //执行源字段目标字段复制方法
    private static void invokeField(CopyTypeDal copyTypeDal, Class<?> srcCls, Field srcItemField, Object srcObj,
                                    Class<?> dstCls, Object dstObj, Map<String, String> fieldCopyMap, Map<String, String> defineRenameMap, String tag, boolean isEmptyAsNull) {
        //若是检测到为空则不复制
        if (null == copyTypeDal || null == srcCls || null == srcItemField || null == srcObj || null == dstCls || null == dstObj) {
            return;
        }

        try {
            // 常量跳过
            if (Modifier.isFinal(srcItemField.getModifiers())) {
                return;
            }
            // 静态跳过
            if (Modifier.isStatic(srcItemField.getModifiers())) {
                return;
            }
            String srcItemFieldName = srcItemField.getName();
            DefCopyField defCopyField = srcItemField.getAnnotation(DefCopyField.class);
            CopyFieldDal copyFieldDal = new CopyFieldDal();
            //若是复制源字段目标字段对应的定义列表为空，则依据注解解析复制
            if (null == fieldCopyMap || fieldCopyMap.size() <= 0) {

                if (copyTypeDal.defaultCopy) {
                    //类定义默认复制，字段有DefCopyField注解且DefCopyField注解的isCopy为false则不复制
                    if (null != defCopyField && !defCopyField.isCopy()) {
                        copyFieldDal.isCopy = false;
                    } else {
                        copyFieldDal.isCopy = true;
                    }
                } else {
                    //类定义默认不复制，字段没有DefCopyField注解或DefCopyField注解的isCopy为false则不复制
                    if (null == defCopyField || !defCopyField.isCopy()) {
                        copyFieldDal.isCopy = false;
                    } else {
                        copyFieldDal.isCopy = true;
                    }
                }
            } else {
                //若是复制源字段目标字段对应的定义Map列表不为空，则依据定义Map列表复制
                if (fieldCopyMap.containsKey(srcItemFieldName)) {
                    copyFieldDal.isCopy = true;
                } else {
                    copyFieldDal.isCopy = false;
                }
            }
            if (!copyFieldDal.isCopy) {
                return;
            }
            //字段没有DefCopyField注解,依据类DefCopyType注解获取默认值
            if (null == defCopyField) {
                if (null == fieldCopyMap || fieldCopyMap.size() <= 0) {
                    copyFieldDal.isCopy = true;
                    copyFieldDal.dstName = "";
                    copyFieldDal.copyMethod = "";
                    copyFieldDal.copyMethodWithFieldValue = false;
                    copyFieldDal.isOverWrite = copyTypeDal.isOverWrite;
                    copyFieldDal.isNullOverWrite = copyTypeDal.isNullOverWrite;
                } else {
                    copyFieldDal.isCopy = true;
                    copyFieldDal.dstName = fieldCopyMap.get(srcItemFieldName);
                    copyFieldDal.copyMethod = "";
                    copyFieldDal.copyMethodWithFieldValue = false;
                    copyFieldDal.isOverWrite = copyTypeDal.isOverWrite;
                    copyFieldDal.isNullOverWrite = copyTypeDal.isNullOverWrite;
                }

            }
            //字段有DefCopyField注解,依据字段DefCopyField注解获取默认值
            else {
                if (null == fieldCopyMap || fieldCopyMap.size() <= 0) {
                    copyFieldDal.isCopy = true;
                    copyFieldDal.dstName = defCopyField.dstName();
                    copyFieldDal.copyMethod = defCopyField.copyMethod();
                    copyFieldDal.copyMethodWithFieldValue = defCopyField.copyMethodWithFieldValue();
                    copyFieldDal.isOverWrite = defCopyField.isOverWrite();
                    copyFieldDal.isNullOverWrite = defCopyField.isNullOverWrite();
                } else {
                    copyFieldDal.isCopy = true;
                    copyFieldDal.dstName = fieldCopyMap.get(srcItemFieldName);
                    copyFieldDal.copyMethod = defCopyField.copyMethod();
                    copyFieldDal.copyMethodWithFieldValue = defCopyField.copyMethodWithFieldValue();
                    copyFieldDal.isOverWrite = defCopyField.isOverWrite();
                    copyFieldDal.isNullOverWrite = defCopyField.isNullOverWrite();
                }

            }

            if (null != defineRenameMap && defineRenameMap.size() > 0 && defineRenameMap.containsKey(srcItemFieldName)) {
                //若是定义了重定义Map则依据重定义的Map来获取目标字段名称，若是目标字段名称为空则不复制
                copyFieldDal.dstName = defineRenameMap.get(srcItemFieldName);
            } else if (null == copyFieldDal.dstName || copyFieldDal.dstName.length() <= 0) {
                //若是没有定义目标字段的名称(dstName)，依据源字段名称来获取目标字段名称
                copyFieldDal.dstName = srcItemFieldName;
            }
            if (null == copyFieldDal.dstName || copyFieldDal.dstName.length() <= 0) {
                return;
            }
            //获取目标字段，若是为空则不复制
            Field dstItemField = dstCls.getDeclaredField(copyFieldDal.dstName);
            if (null == dstItemField) {
                return;
            }
            // 常量跳过
            if (Modifier.isFinal(dstItemField.getModifiers())) {
                return;
            }
            // 静态跳过
            if (Modifier.isStatic(dstItemField.getModifiers())) {
                return;
            }
            Object srcItemObj = null;
            if (null == copyFieldDal.copyMethod || copyFieldDal.copyMethod.length() <= 0) {
                srcItemField.setAccessible(true);
                srcItemObj = srcItemField.get(srcObj);
            } else {
                if (copyFieldDal.copyMethodWithFieldValue) {
                    srcItemField.setAccessible(true);
                    Object tmpSrcItemObj = srcItemField.get(srcObj);
                    if (null == tag || tag.length() <= 0) {
                        Method method = srcCls.getDeclaredMethod(copyFieldDal.copyMethod, srcItemField.getType());
                        method.setAccessible(true);
                        srcItemObj = method.invoke(srcObj, new Object[]{tmpSrcItemObj});
                    } else {
                        Method method = srcCls.getDeclaredMethod(copyFieldDal.copyMethod, srcItemField.getType(), String.class);
                        method.setAccessible(true);
                        srcItemObj = method.invoke(srcObj, new Object[]{tmpSrcItemObj, tag});
                    }
                } else {
                    if (null == tag || tag.length() <= 0) {
                        Method method = srcCls.getDeclaredMethod(copyFieldDal.copyMethod);
                        method.setAccessible(true);
                        srcItemObj = method.invoke(srcObj);
                    } else {
                        Method method = srcCls.getDeclaredMethod(copyFieldDal.copyMethod, String.class);
                        method.setAccessible(true);
                        srcItemObj = method.invoke(srcObj, new Object[]{tag});
                    }
                }

            }
            //旧的方法，空转NULL
//			if (null != srcItemObj && isEmptyAsNull) {
//				String tmpValStr = String.valueOf(srcItemObj);
//				if (null == tmpValStr || tmpValStr.length() <= 0) {
//					srcItemObj = null;
//				}
//			}
            //新的方法，空转NULL
            if (null != srcItemObj && isEmptyAsNull && srcItemObj instanceof CharSequence) {
                String tmpValStr = charSequenceToString((CharSequence) srcItemObj);
                if (null == tmpValStr || tmpValStr.length() <= 0) {
                    srcItemObj = null;
                }
            }
            //源字段为NULL，且NULL不覆盖目标字段则不复制，保持目标字段的值不变
            if (null == srcItemObj && !copyFieldDal.isNullOverWrite) {
                return;
            }

            if (copyFieldDal.isOverWrite) {
                //覆盖目标字段时候，目标字段不管有没有值都会被覆盖
                if (copyFieldDal.isNullOverWrite) {
                    //源字段值任何时候都覆盖目标字段值
                    dstItemField.setAccessible(true);
                    dstItemField.set(dstObj, srcItemObj);
                } else {
                    //源字段值不为NULL时候覆盖目标字段值，为NULL不覆盖目标字段值
                    if (null != srcItemObj) {
                        dstItemField.setAccessible(true);
                        dstItemField.set(dstObj, srcItemObj);
                    }
                }
            } else {
                //覆盖目标字段时候，目标字段不为NULL时候才会覆盖
                dstItemField.setAccessible(true);
                Object desFieldObj = dstItemField.get(dstObj);
                //目标字段值不为NULL，则不覆盖目标字段值
                if (null != desFieldObj) {
                    return;
                }
                //目标字段值为NULL，则覆盖目标字段值
                if (copyFieldDal.isNullOverWrite) {
                    dstItemField.set(dstObj, srcItemObj);
                } else {
                    if (null != srcItemObj) {
                        dstItemField.set(dstObj, srcItemObj);
                    }
                }

            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException
                 | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            // TODO: handle exception
            if (Is_Exception_Print) {
                log.debug("Error:copyObject", e);
            }
        } catch (Exception e) {
            // TODO: handle exception
            if (Is_Exception_Print) {
                log.debug("Error:copyObject", e);
            }
        }

    }

    private static String charSequenceToString(CharSequence str) {
        if (null == str) {
            return null;
        } else if (str instanceof String) {
            return (String) str;
        } else {
            String s = str.toString();
            return s;
        }
    }

}
