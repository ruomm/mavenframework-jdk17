/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.propertiesx.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
public @interface DefPropertySpace {
    public String spaceName() default "";

    /**
     * 解析完成时候执行的方法
     * @return 解析完成时候执行的方法
     */
    public String excuteMethodOnEnd() default "";

}
