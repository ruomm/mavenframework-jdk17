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
public @interface ValidEnCn2Str {
    public final static String TYPE_CN = "cn";
    public final static String TYPE_EN = "en";
    public final static String TYPE_MIX = "mix";

    public int min() default -1;

    public int max() default -1;

    public int minCn2() default -1;

    public int maxCn2() default -1;

    String strType() default TYPE_MIX;

    public String message() default "";
}
