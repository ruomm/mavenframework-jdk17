/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月18日 下午3:52:13
 */
package com.ruomm.webx.validatorx.verify.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface DefVerifyObject {
    public final static String TAG_ALL = "ALL";

    public String verifyTag() default "";
}