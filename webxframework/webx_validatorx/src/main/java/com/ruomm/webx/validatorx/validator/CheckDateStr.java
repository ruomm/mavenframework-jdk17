/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月19日 上午10:57:18
 */
package com.ruomm.webx.validatorx.validator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckDateStrConstraintValidator.class)
public @interface CheckDateStr {
    public final static String CheckDateFormatStr = "yyyy-MM-dd HH:mm:ss";

    String before() default "";

    String after() default "";

    String afterByFormat() default "";

    String beforeByFormat() default "";

    String format() default "yyyyMMdd";

    boolean isDayMode() default true;

    String message() default "验证失败";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}