package com.ruomm.javax.basex.javaparam.dal;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface JavaPrarmField {
    /**
     * 参数KEY值
     *
     * @return KEY值
     */
    String key() default "";

    /**
     * 默认值
     *
     * @return 默认值
     */
    String valueHasKey() default "";

    String valueNoKey() default "{NULL}";
}
