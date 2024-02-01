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
public @interface ValidDateStr {
    String before() default "";

    String after() default "";

    String afterByFormat() default "";

    String beforeByFormat() default "";

    String format() default "yyyyMMdd";

    boolean isDayMode() default true;

    public String message() default "";
}
