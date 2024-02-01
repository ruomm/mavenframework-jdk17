/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.poix.annotation;

import com.ruomm.javax.poix.CellEmptyError;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Inherited
public @interface DefExcelReaderCell {
    // cell序号
    public int cellIndex() default -1;

    // Date类型日期，number类型 plain/scale/none
    public String cellFormat() default "";

    // 四舍五入规则
    public int numRoundingMode() default -1;

    // 保留小数位数
    public int numScale() default -1;

    // 去除前置头
    public String suffixStart() default "";

    // 去除尾部头
    public String suffixEnd() default "";

    // 是否去除字符串开始结束空白
    public boolean isStringTrim() default true;

    // 读取cell异常时候执行的逻辑
    public CellEmptyError errorMode() default CellEmptyError.NONE;
}
