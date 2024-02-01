/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月29日 下午4:23:02
 */
package com.ruomm.assistx.dbgenerate.parse;

import com.ruomm.assistx.dbgenerate.helper.DbGeneratorHelper;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.Iterator;
import java.util.List;

/**
 * mybatis generator 自定义toString插件 基于MBG 1.3.2
 */
public class RuommPluginAdapter extends PluginAdapter {
    /**
     * 是否生成getter方法，默认true
     */
    private boolean isGetter = true;
    /**
     * 是否生成setter方法，默认true
     */
    private boolean isSetter = true;
    /**
     * 是否生成toString方法，默认true
     */
    private boolean isToString = false;
    /*
     *  自定义GeneratorHelper辅助类，若是为空则使用自定义GeneratorHelper来生成部分属性
     */
    private DbGeneratorHelper generatorHelper = null;

    private boolean isConfigHasLoad = false;

    public RuommPluginAdapter() {
    }

    public void loadPropertiesConfig() {
        if (isConfigHasLoad) {
            return;
        }
        isGetter = DbGeneratorUtils.isTureProperties(properties, DbGeneratorConfig.PLUGIN_KEY_IS_GETTER, true);
        isSetter = DbGeneratorUtils.isTureProperties(properties, DbGeneratorConfig.PLUGIN_KEY_IS_SETTER, true);
        isToString = DbGeneratorUtils.isTureProperties(properties, DbGeneratorConfig.PLUGIN_KEY_IS_TOSTRING, true);
        String generatorHelperVal = properties.getProperty(DbGeneratorConfig.HELPER_GeneratorHelper);
        if (null == generatorHelperVal || generatorHelperVal.length() <= 0) {
            this.generatorHelper = null;
        } else {
            try {
                this.generatorHelper = (DbGeneratorHelper) Class.forName(generatorHelperVal).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isConfigHasLoad = true;

    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        loadPropertiesConfig();
        if (null != generatorHelper) {
            generatorHelper.addFieldAndMethod(this.context, topLevelClass, introspectedTable, isGetter, isSetter);
        }
        this.generateToString(topLevelClass, introspectedTable);
//		addFieldAndMethod(topLevelClass, introspectedTable, "upload", new FullyQualifiedJavaType("java.io.File"), "实际上传文件");
//		addFieldAndMethod(topLevelClass, introspectedTable, "uploadContentType", FullyQualifiedJavaType.getStringInstance(),
//				"文件的内容类型");
//		addFieldAndMethod(topLevelClass, introspectedTable, "uploadFileName", FullyQualifiedJavaType.getStringInstance(),
//				"上传文件名");
//		addFieldAndMethod(topLevelClass, introspectedTable, "fileCaption", FullyQualifiedJavaType.getStringInstance(),
//				"上传文件时的备注");
//		addFieldAndMethod(topLevelClass, introspectedTable, "reportFile", FullyQualifiedJavaType.getStringInstance(),
//				"报表下载文件路径");
//
//		addFieldAndMethod(topLevelClass, introspectedTable, "defineMode", FullyQualifiedJavaType.getBooleanPrimitiveInstance(),
//				"是否严格匹配");
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
                                                      IntrospectedTable introspectedTable) {
        this.generateToString(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToString(topLevelClass, introspectedTable);
        return true;
    }

    private void generateToStringBuilder(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        loadPropertiesConfig();
        if (!isToString) {
            return;
        }
        if (null != generatorHelper && generatorHelper.generateToString(this.context, topLevelClass, introspectedTable)) {
            return;
        }
		/*Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getStringInstance());
		method.setName("toString");
		if (introspectedTable.isJava5Targeted()) {
			method.addAnnotation("@Override");
		}*/
        Method method = new Method("toString");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addAnnotation("@Override");

        // 添加方法注释
        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        // 下面两句method.addBodyLine使用其中之一即可

        // 使用自定义toString工具类
        // method.addBodyLine("return ToStringUtils.toSimpleString(this);");

        // 使用commons-lang3的工具类
        // 添加包
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder");
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringStyle");
        method.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);");

        // 添加方法
        topLevelClass.addMethod(method);
    }


    private void generateToString(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        loadPropertiesConfig();
        if (!isToString) {
            return;
        }
        if (null != generatorHelper && generatorHelper.generateToString(this.context, topLevelClass, introspectedTable)) {
            return;
        }
		/*Method method =new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
	 	method.setReturnType(FullyQualifiedJavaType.getStringInstance());
	 	method.setName("toString");
	 	if(introspectedTable.isJava5Targeted())
	 	{
	 		method.addAnnotation("@Override");
	 	}*/
        Method method = new Method("toString");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addAnnotation("@Override");
        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("StringBuilder sb = new StringBuilder();");
        method.addBodyLine("sb.append(getClass().getSimpleName());");
        method.addBodyLine("sb.append(\" {\");");
        StringBuilder sb = new StringBuilder();
        Iterator i$ = topLevelClass.getFields().iterator();
        boolean isFirstField = true;
        while (i$.hasNext()) {
            String tmpVal = isFirstField ? " " : ", ";
            isFirstField = false;
            Field field = (Field) i$.next();
            String property = field.getName();
            sb.setLength(0);
            sb.append("sb.append(\"").append(tmpVal).append(property).append("=\")").
                    append(".append(").append(property).append(");");
            method.addBodyLine(sb.toString());
        }
        method.addBodyLine("sb.append(\"}\");");
        method.addBodyLine("return sb.toString();");
        topLevelClass.addMethod(method);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    // 该方法在生成每一个属性的getter方法时候调用，如果我们不想生成getter，直接返回false即可；
    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        loadPropertiesConfig();
        return isGetter;
    }

    // 该方法在生成每一个属性的setter方法时候调用，如果我们不想生成setter，直接返回false即可；
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        loadPropertiesConfig();
        return isSetter;
    }

    /*
     * 实体中添加属性和方法
     */
    public void addFieldAndMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name,
                                  FullyQualifiedJavaType fullyQualifiedJavaType, String remark) {
        DbGeneratorUtils.addFieldAndMethod(context, topLevelClass, introspectedTable, name, fullyQualifiedJavaType, remark, isGetter, isSetter);
    }
}
