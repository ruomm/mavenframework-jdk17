package com.ruomm.javax.basex.annotation;

import java.lang.annotation.*;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/2 11:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DefMapTransObj {
    // 转换方法
    String transMethod();
}
