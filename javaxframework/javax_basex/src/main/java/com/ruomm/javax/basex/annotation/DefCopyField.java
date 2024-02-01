/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.basex.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DefCopyField {
    /**
     * 定义该字段是否复制。
     * @return 义该字段是否复制。
     */
    public boolean isCopy() default false;

    /**
     * 定义该字段复制到目标对象中的字段名称
     * @return
     */
    public String dstName() default "";

    // 定义复制时候给字段解析的方法，解析后的值复制到目标字段

    /**
     * 定义复制时候该字段解析的方法，解析后的值复制到目标字段
     * @return
     */
    public String copyMethod() default "";

    /**
     * 定义字段解析的方法时候是否使用field值作为参数，默认false
     * @return
     */
    public boolean copyMethodWithFieldValue() default false;

    /**
     * 定义源字段值是否覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为true，源字段值强制覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为false，源字段值不为NULL时候强制覆盖目标字段值。
     * 若是isOverWrite为false，则目标字段值为NULL时候覆盖目标字段值，不为NULL时候不覆盖目标字段值。
     * @return 定义源字段值是否覆盖目标字段值。
     */
    public boolean isOverWrite() default true;

    /**
     * 定义源字段值覆盖目标字段值时候源字段NULL时候是否覆盖。
     * 若是isOverWrite为true且isNullOverWrite为true，源字段值强制覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为false，源字段值不为NULL时候强制覆盖目标字段值。
     * 若是isOverWrite为false，则目标字段值为NULL时候覆盖目标字段值，不为NULL时候不覆盖目标字段值。
     * @return 定义源字段值覆盖目标字段值时候源字段NULL时候是否覆盖。
     */
    public boolean isNullOverWrite() default false;
}
