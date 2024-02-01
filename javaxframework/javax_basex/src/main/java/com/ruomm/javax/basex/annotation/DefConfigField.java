/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.basex.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DefConfigField {
    /**
     * 定义该字段配置的顺序，从0开始
     * @return 义该字段配置的顺序
     */
    public int index();

}
