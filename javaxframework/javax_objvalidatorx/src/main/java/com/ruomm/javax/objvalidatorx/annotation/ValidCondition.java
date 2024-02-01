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
public @interface ValidCondition {
    public final static String TAGS_OF_VALID_SUCCESS = "success" + "," + "ok" + "," + "true" + "," + "成功";

    public String conditionNotEmpty() default "";

    public String conditionEmpty() default "";

    public String splitTag() default ",";

    public String defineNull() default "NULL";

    public String defineEmpty() default "EMPTY";

    public String messageNotEmpty() default "";

    public String messageEmpty() default "";

}
