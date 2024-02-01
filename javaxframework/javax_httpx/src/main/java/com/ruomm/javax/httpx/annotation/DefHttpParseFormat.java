package com.ruomm.javax.httpx.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DefHttpParseFormat {
    /**
     * 对象模型的Key值，对象化默认传参、存储的Key值
     *
     * @return
     */
    public String parseMethod() default "";

    public String format() default "";

}