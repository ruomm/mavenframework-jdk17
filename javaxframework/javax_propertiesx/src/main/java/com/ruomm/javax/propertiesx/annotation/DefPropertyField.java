/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.propertiesx.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DefPropertyField {
    public String name() default "";

    public String readerType() default "";

    public String readerFormat() default "";

    public boolean readerIgnore() default false;

    public String readerMethod() default "";
}
