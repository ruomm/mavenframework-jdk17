package com.ruomm.javax.basex.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DefBeanEntity {
    /**
     * 对象模型的Key值，对象化默认传参、存储的Key值
     *
     * @return
     */
    public String beanKey();

}
