/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.poix.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface DefExcelWriterCell {
    // cell序号
    public int cellIndex() default -1;

    public String cellName() default "";

    public int cellWidth() default 0;


    public boolean wrapText() default false;
}
