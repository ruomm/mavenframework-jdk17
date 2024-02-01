/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.basex.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DefCopyType {

    /**
     * 定义该类是否复制，如是最终类则不需要这个注释就复制，父类需要注释才会复制父类中的字段，没有注释的父类不会复制父类中的字段。
     * @return 定义该类是否复制，如是最终类则不需要这个注释就复制，父类需要注释才会复制父类中的字段，没有注释的父类不会复制父类中的字段。
     */
    public boolean isCopy() default true;

    /**
     * 定义字段是否默认复制，若是false，必须定义了DefCopyField注解的字段才赋值，true则包含所有字段都会复制
     * @return 定义字段是否默认复制，若是false，必须定义了DefCopyField注解的字段才赋值，true则包含所有字段都会复制
     */
    public boolean defaultCopy() default true;

    // 定义该类复制的Map列表的方法，若是定义非空则Field定义的DefCopyField不起作用，只有DefCopyType起作用

    /**
     * 定义该类复制的Map列表的方法，若是定义了非空切依据该类获取到了Map<String,String>源类对象和目标类对象的字段列表关系，则依据列表关系复制字段，字段注解DefCopyField的isCopy和dstName失效。
     * @return 定义该类复制的Map列表的方法，若是定义了非空切依据该类获取到了Map<String,String>源类对象和目标类对象的字段列表关系，则依据列表关系复制字段，字段注解DefCopyField的isCopy和dstName失效。
     */
    public String copyMapMethod() default "";

    /**
     * 定义源字段值是否覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为true，源字段值强制覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为false，源字段值不为NULL时候强制覆盖目标字段值。
     * 若是isOverWrite为false，则目标字段值为NULL时候覆盖目标字段值，不为NULL时候不覆盖目标字段值。
     * @return 定义源字段值是否覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为true，源字段值强制覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为false，源字段值不为NULL时候强制覆盖目标字段值。
     * 若是isOverWrite为false，则目标字段值为NULL时候覆盖目标字段值，不为NULL时候不覆盖目标字段值。
     */
    public boolean isOverWrite() default true;

    // true时候始终使用原值替换目标值，fasle时候只有原值不为NULL才替换目标值，此必须在isOverWrite为true时候起作用

    /**
     * 定义源字段值覆盖目标字段值时候源字段NULL时候是否覆盖。
     * 若是isOverWrite为true且isNullOverWrite为true，源字段值强制覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为false，源字段值不为NULL时候强制覆盖目标字段值。
     * 若是isOverWrite为false，则目标字段值为NULL时候覆盖目标字段值，不为NULL时候不覆盖目标字段值。
     * @return 定义源字段值覆盖目标字段值时候源字段NULL时候是否覆盖。
     * 若是isOverWrite为true且isNullOverWrite为true，源字段值强制覆盖目标字段值。
     * 若是isOverWrite为true且isNullOverWrite为false，源字段值不为NULL时候强制覆盖目标字段值。
     * 若是isOverWrite为false，则目标字段值为NULL时候覆盖目标字段值，不为NULL时候不覆盖目标字段值。
     */
    public boolean isNullOverWrite() default false;
}
