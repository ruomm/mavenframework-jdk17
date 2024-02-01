package com.ruomm.javax.basex;

import com.ruomm.javax.basex.annotation.DefBeanEntity;
import com.ruomm.javax.corex.StringUtils;

public class BeanEntryUtil {
    /**
     * 获取Bean模型的Key
     *
     * @param cls 数据类型
     * @return
     */
    public static String getBeanKey(Class<?> cls) {
        DefBeanEntity defBeanEntry = cls.getAnnotation(DefBeanEntity.class);

        if (null == defBeanEntry || StringUtils.isEmpty(defBeanEntry.beanKey())) {
            return cls.getSimpleName();
        } else {
            return defBeanEntry.beanKey();
        }
    }

    /**
     * 获取Bean模型的List的Key
     *
     * @param cls 数据类型
     * @return
     */
    public static String getBeanListKey(Class<?> cls) {
        DefBeanEntity defBeanEntry = cls.getAnnotation(DefBeanEntity.class);

        if (null == defBeanEntry || StringUtils.isEmpty(defBeanEntry.beanKey())) {
            return cls.getSimpleName() + "List";
        } else {
            return defBeanEntry.beanKey() + "List";
        }
    }

    /**
     * 获取需要存储读取Bean模型Value值的真正Key
     *
     * @param key 自定义的key；
     * @param cls 数据对象模型；
     * @return 真正的Key值；
     */
    public static String getRealBeanKey(String key, Class<?> cls) {
        if (!StringUtils.isEmpty(key)) {
            return key;
        } else {
            return getBeanKey(cls);
        }
    }

    /**
     * 判断给出的字符串是否和Bean类型的Key值相同
     *
     * @param tag
     * @param cls
     * @return
     */
    public static boolean isBeanKeyEqual(String tag, Class<?> cls) {
        if (StringUtils.isEmpty(tag) || null == cls) {
            return false;
        } else {
            return tag.equals(getBeanKey(cls));
        }
    }

}
