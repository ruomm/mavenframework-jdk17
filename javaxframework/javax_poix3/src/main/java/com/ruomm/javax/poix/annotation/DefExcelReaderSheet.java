/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.poix.annotation;

import com.ruomm.javax.poix.ExcelConfig;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
public @interface DefExcelReaderSheet {
    /**
     * @return Excel表名称
     */
    public String sheetName() default "";

    /**
     * @return Excel表类型
     */
    public String sheetType() default ExcelConfig.EXCEL_XLS;

    /**
     * @return Excel表序号
     */
    public int sheetIndex() default -1;

    /**
     * @return Excel读取行开始偏移量，默认0
     */
    public int rowStartOffset() default 0;

    /**
     * @return Excel读取行结束偏移量，默认0
     */
    public int rowEndOffset() default 0;

    /**
     * @return Excel读取单元行偏移量，默认0
     */
    public int cellOffset() default 0;

}
