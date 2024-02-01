/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.objvalidatorx.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface ValidMethod {

    public final static String TAGS_OF_VALID_SUCCESS = "success" + "," + "ok" + "," + "true" + "," + "成功";
    public final static String VALID_SUCCESS = TAGS_OF_VALID_SUCCESS.split(",")[0];

    public String message() default "";
}
