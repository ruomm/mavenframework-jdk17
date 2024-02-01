package com.ruomm.javax.basex;

import com.ruomm.javax.basex.annotation.DefEncryptField;
import com.ruomm.javax.corex.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/10/29 13:39
 */
public class ObjectEncryptUtils {
    public static interface ObjectEncryptHelper {
        public String encrypt(String strClear);

        public String decrypt(String strEnc);
    }

    private static boolean doObjectProgress(Object obj, ObjectEncryptHelper objectEncryptHelper, boolean isEncrypt) {
        if (null == obj || null == objectEncryptHelper) {
            return false;
        }
        List<Class<?>> list = ObjectUtils.listClassByObject(obj, ObjectUtils.CLASS_DEEP_COUNT, null);
        if (null == list || list.size() <= 0) {
            return false;
        }
        for (Class<?> fieldClass : list) {
            Field[] fields = fieldClass.getDeclaredFields();
            if (null == fields || fields.length <= 0) {
                continue;
            }
            for (Field field : fields) {
                try {
                    // 静态跳过
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    // final类型跳过
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    if (field.getType() != String.class) {
                        continue;
                    }
                    DefEncryptField defEncryptField = field.getAnnotation(DefEncryptField.class);
                    if (null == defEncryptField || !defEncryptField.isEncrypt()) {
                        continue;
                    }
                    field.setAccessible(true);
                    String val = (String) field.get(obj);
                    String resultVal = isEncrypt ? objectEncryptHelper.encrypt(val) : objectEncryptHelper.decrypt(val);
                    field.set(obj, resultVal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean encryptObj(Object obj, ObjectEncryptHelper objectEncryptHelper) {
        return doObjectProgress(obj, objectEncryptHelper, true);
    }

    public static boolean decryptObj(Object obj, ObjectEncryptHelper objectEncryptHelper) {
        return doObjectProgress(obj, objectEncryptHelper, false);
    }
}
