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
public @interface ValidBigDecimal {

    public String max() default "";

    public String min() default "";

    public int maxLength() default -1;

    public int pointDigits() default -1;

    public boolean zeroStartAllow() default false;

    public String message() default "";
}
