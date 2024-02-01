/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月6日 上午9:58:06
 */
package com.ruomm.javax.poix.annotation;

import com.ruomm.javax.poix.ExcelConfig;
import com.ruomm.javax.poix.config.ExcelAlignment;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
public @interface DefExcelWtiterSheet {
    /**
     * @return Excel表名称
     */
    public String sheetName() default "";

    /**
     * @return Excel表类型
     */
    public String sheetType() default ExcelConfig.EXCEL_XLS;

    /**
     * @return Excel表标题名称
     */
    public String titleName() default "";

    /**
     * @return Excel表标题行数
     */
    public int titleRowNums() default 0;

    /**
     * @return Excel表标题行高度
     */
    public int titleRowHeight() default 0;

    /**
     * @return Excel表标题字体大小
     */
    public int titleFontSize() default 0;

    /**
     * @return Excel表内容行高度
     */
    public int rowHeight() default 0;

    /**
     * @return Excel表内容字体大小
     */
    public int rowFontSize() default 0;

    /**
     * 标题水平对齐方式
     *
     * @return
     */
    public ExcelAlignment titleHorizontal() default ExcelAlignment.CENTER;

    /**
     * 标题垂直对齐方式
     *
     * @return
     */
    public ExcelAlignment titleVertical() default ExcelAlignment.CENTER;

    /**
     * 单元格默认水平对齐方式
     *
     * @return
     */
    public ExcelAlignment cellDefaultHorizontal() default ExcelAlignment.TOP_OR_LEFT;

    /**
     * 单元格默认垂直对齐方式
     *
     * @return
     */
    public ExcelAlignment cellDefaultVertical() default ExcelAlignment.BOTTOM_OR_RIGHT;

}
