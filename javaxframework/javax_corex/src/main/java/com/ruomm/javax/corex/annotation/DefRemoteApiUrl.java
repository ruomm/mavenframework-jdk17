package com.ruomm.javax.corex.annotation;

import java.lang.annotation.*;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/7/6 12:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DefRemoteApiUrl {
    // 请求的接口地址
    public String apiUrl();

    public String apiDesc() default "";

    public Class<?> targetClass() default String.class;
}
