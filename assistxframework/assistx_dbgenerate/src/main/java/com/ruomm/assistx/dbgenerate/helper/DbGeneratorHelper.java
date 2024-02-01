package com.ruomm.assistx.dbgenerate.helper;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;

public interface DbGeneratorHelper {
    /**
     * 自定义Import引用
     *
     * @param topLevelClass     类
     * @param introspectedTable 表
     */
    public void addModelClassImportedType(TopLevelClass topLevelClass,
                                          IntrospectedTable introspectedTable);

    /**
     * 自定义Annotation注解
     *
     * @param topLevelClass     类
     * @param introspectedTable 表
     */
    public void addModelClassAnnotation(TopLevelClass topLevelClass,
                                        IntrospectedTable introspectedTable);

    /**
     * 自定义Annotation注解
     *
     * @param field             field变量
     * @param introspectedTable 表
     */
    public void addModelFieldAnnotation(Field field,
                                        IntrospectedTable introspectedTable,
                                        IntrospectedColumn introspectedColumn);

    /**
     * 自定义ToString方法
     *
     * @param context           环境变量
     * @param introspectedTable 表
     * @param topLevelClass     类
     * @return 是否自定义String方法，true使用自定义，false使用默认方法
     */
    public boolean generateToString(Context context, TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    /**
     * 添加自定义变量和方法
     *
     * @param context           环境变量
     * @param topLevelClass     类
     * @param introspectedTable 表
     * @param isGetter          是否生成getter
     * @param isSetter          是否生成setter
     * @paramDefault name 变量名称
     * @paramDefault fullyQualifiedJavaType 变量类型
     * @paramDefault remark 变量属性注解
     */
    public void addFieldAndMethod(Context context, TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                  boolean isGetter, boolean isSetter);
}
