/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月19日 上午10:57:18
 */
package com.ruomm.webx.validatorx.validator;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckEnCn2StrConstraintValidator.class)
public @interface CheckEnCn2Str {
    public final static String TYPE_CN = "cn";
    public final static String TYPE_EN = "en";
    public final static String TYPE_MIX = "mix";

    String strType() default TYPE_MIX;

    int min() default -1;

    int max() default -1;

    public int minCn2() default -1;

    public int maxCn2() default -1;

    String message() default "验证失败";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}