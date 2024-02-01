/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.objvalidatorx.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface ValidAllowStr {
    public String allowStr();

    public boolean isSingleSelect() default true;

    public boolean isSameSelect() default false;

    public boolean ignoreCase() default false;

    public String splitTag() default ",";

    public String message() default "";
}
