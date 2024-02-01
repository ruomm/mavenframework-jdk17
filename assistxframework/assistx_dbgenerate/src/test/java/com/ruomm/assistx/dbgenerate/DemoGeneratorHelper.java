package com.ruomm.assistx.dbgenerate;

import com.ruomm.assistx.dbgenerate.helper.DbGeneratorHelper;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;

public class DemoGeneratorHelper implements DbGeneratorHelper {


    @Override
    public void addModelClassImportedType(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//        topLevelClass.addImportedType("org.hibernate.annotations.DynamicUpdate");

        topLevelClass.addImportedType("javax.persistence.*");
//
//        topLevelClass.addImportedType("java.math.BigDecimal");
//        topLevelClass.addImportedType("java.sql.Date");
//        topLevelClass.addImportedType("java.sql.Timestamp");
    }

    @Override
    public void addModelClassAnnotation(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//       topLevelClass.addAnnotation("@Entity");
//       topLevelClass.addAnnotation("@DynamicUpdate");
//       topLevelClass.addAnnotation("@Table(name = \"" + introspectedTable.getFullyQualifiedTable() + "\")");
    }

    @Override
    public void addModelFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        field.addAnnotation("@Entity打发的撒");
    }

    @Override
    public boolean generateToString(Context context, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return false;
//        Method method = new Method("toString");
//        method.setVisibility(JavaVisibility.PUBLIC);
//        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
//        method.addAnnotation("@Override");
//
//        // 添加方法注释
//        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
//
//        // 下面两句method.addBodyLine使用其中之一即可
//
//        // 使用自定义toString工具类
//        // method.addBodyLine("return ToStringUtils.toSimpleString(this);");
//
//        // 使用commons-lang3的工具类
//        // 添加包
//        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder");
//        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringStyle");
//        method.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);");
//
//        // 添加方法
//        topLevelClass.addMethod(method);
//        return true;

    }

    @Override
    public void addFieldAndMethod(Context context, TopLevelClass topLevelClass, IntrospectedTable introspectedTable, boolean isGetter, boolean isSetter) {
//        DbGeneratorUtils.addFieldAndMethod(context,topLevelClass, introspectedTable, "upload", new FullyQualifiedJavaType("java.io.File"),
//                "实际上传文件",isGetter,isSetter);
//        DbGeneratorUtils.addFieldAndMethod(context,topLevelClass, introspectedTable, "uploadContentType", FullyQualifiedJavaType.getStringInstance(),
//				"文件的内容类型",isGetter,isSetter);
//        DbGeneratorUtils.addFieldAndMethod(context,topLevelClass, introspectedTable, "uploadFileName", FullyQualifiedJavaType.getStringInstance(),
//				"上传文件名",isGetter,isSetter);
//        DbGeneratorUtils.addFieldAndMethod(context,topLevelClass, introspectedTable, "fileCaption", FullyQualifiedJavaType.getStringInstance(),
//				"上传文件时的备注",isGetter,isSetter);
//        DbGeneratorUtils.addFieldAndMethod(context,topLevelClass, introspectedTable, "reportFile", FullyQualifiedJavaType.getStringInstance(),
//				"报表下载文件路径",isGetter,isSetter);
//
//        DbGeneratorUtils.addFieldAndMethod(context,topLevelClass, introspectedTable, "defineMode", FullyQualifiedJavaType.getBooleanPrimitiveInstance(),
//				"是否严格匹配",isGetter,isSetter);
    }
}
