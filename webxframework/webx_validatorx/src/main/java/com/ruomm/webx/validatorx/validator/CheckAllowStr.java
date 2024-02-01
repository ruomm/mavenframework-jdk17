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
@Constraint(validatedBy = CheckAllowStrConstraintValidator.class)
public @interface CheckAllowStr {
    public String allowStr();

    public boolean isSingleSelect() default true;

    public boolean isSameSelect() default false;

    public boolean ignoreCase() default false;

    public String splitTag() default ",";

    String message() default "验证失败";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};
}