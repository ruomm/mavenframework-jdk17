package com.ruomm.assistx.dbgenerate.parse;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Context;

import java.util.List;
import java.util.Properties;

public class DbGeneratorUtils {
    public static String transBlankStr(String str) {
        return transBlankStr(str, "无");
    }

    public static String transBlankStr(String str, String defaultStr) {
        String data = null == str || str.length() <= 0 ? str : str.trim();
        if (null == data || data.length() <= 0) {
            return defaultStr;
        } else {
            return data;
        }
    }

    public static boolean isTureProperties(Properties properties, String key, boolean defaultVal) {
        if (null == properties || properties.size() <= 0) {
            return defaultVal;
        }
        if (null == key || key.length() <= 0) {
            return defaultVal;
        } else {
            String tmpVal = properties.getProperty(key);
            if (null == tmpVal || tmpVal.length() <= 0) {
                return defaultVal;
            }
            String tmpValTrim = tmpVal.toLowerCase().trim();
            if (null == tmpValTrim || tmpValTrim.length() <= 0) {
                return defaultVal;
            }
            if ("true".equalsIgnoreCase(tmpValTrim) || "1".equalsIgnoreCase(tmpValTrim)) {
                return true;
            } else if ("false".equalsIgnoreCase(tmpValTrim) || "0".equalsIgnoreCase(tmpValTrim)) {
                return false;
            } else {
                return defaultVal;
            }
        }
    }

    /**
     * 为属性添加@Id和@Column注解
     *
     * @param field              属性变量
     * @param introspectedTable  表
     * @param introspectedColumn 列
     * @param isAddLength        是否添加长度
     */
    public static void addColumnAndIdPersistence(Field field,
                                                 IntrospectedTable introspectedTable,
                                                 IntrospectedColumn introspectedColumn, boolean isAddLength, boolean isAddNullable) {
        try {
            boolean isPrimaryKeyColumn = false;
            try {
                List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
                if (null != primaryKeyColumns && primaryKeyColumns.size() > 0) {
                    for (IntrospectedColumn tmpColumn : primaryKeyColumns) {
                        if (tmpColumn.getActualColumnName().equals(introspectedColumn.getActualColumnName())) {
                            isPrimaryKeyColumn = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                isPrimaryKeyColumn = false;
            }
            if (isPrimaryKeyColumn) {
                field.addAnnotation("@Id");
            }
//            if(introspectedColumn.isIdentity())
//            {
//                field.addAnnotation("@Id");
//            }
            StringBuilder sb = new StringBuilder();
            sb.append("@Column( name = \"" +
                    introspectedColumn.getActualColumnName() +
                    "\" ");
            if (isAddLength) {
                sb.append(", length = " + introspectedColumn.getLength() + " ");
            }
            if (isAddNullable) {
                sb.append(", nullable = " + introspectedColumn.isNullable() + " ");
            }
            sb.append(")");
//
//            if(isAddLength){
//                sb.append("@Column(name = \"" +
//                        introspectedColumn.getActualColumnName() +
//                        "\",length = " +
//                        introspectedColumn.getLength() +
//                        ")");
//            }
//            else{
//                sb.append("@Column(name = \"" +
//                        introspectedColumn.getActualColumnName() +
//                        "\")");
//            }
            field.addAnnotation(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 添加自定义变量和方法
     *
     * @param context                环境变量
     * @param topLevelClass          类
     * @param introspectedTable      表
     * @param name                   变量名称
     * @param fullyQualifiedJavaType 变量类型
     * @param remark                 变量属性注解
     * @param isGetter               是否生成getter
     * @param isSetter               是否生成setter
     */
    public static void addFieldAndMethod(Context context, TopLevelClass topLevelClass, IntrospectedTable introspectedTable,
                                         String name, FullyQualifiedJavaType fullyQualifiedJavaType, String remark,
                                         boolean isGetter, boolean isSetter) {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        /*Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fullyQualifiedJavaType);
        field.setName(name);*/
        Field field = new Field(name, fullyQualifiedJavaType);
        field.setVisibility(JavaVisibility.PRIVATE);
//		field.setInitializationString("-1");
//		commentGenerator.addFieldComment(field, introspectedTable);
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append("@" + name + " " + remark);
        sb.append(" ");
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
        topLevelClass.addField(field);

        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        if (isSetter) {
            /*Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setName("set" + camel);
            method.addParameter(new Parameter(fullyQualifiedJavaType, name));
            method.addBodyLine("this." + name + " = " + name + ";");
            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addMethod(method);*/
            Method method = new Method("set" + camel);
            method.setVisibility(JavaVisibility.PUBLIC);
            method.addParameter(new Parameter(fullyQualifiedJavaType, name));
            method.addBodyLine("this." + name + " = " + name + ";");
            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addMethod(method);
        }
        if (isGetter) {
            /*Method method = new Method();
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(fullyQualifiedJavaType);
            method.setName("get" + camel);
            method.addBodyLine("return " + name + ";");
            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addMethod(method);*/
            Method method = new Method("get" + camel);
            method.setVisibility(JavaVisibility.PUBLIC);
            method.setReturnType(fullyQualifiedJavaType);
            method.addBodyLine("return " + name + ";");
            commentGenerator.addGeneralMethodComment(method, introspectedTable);
            topLevelClass.addMethod(method);
        }
    }
}
