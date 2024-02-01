package com.ruomm.assistx.dbgenerate.parse;

public class DbGeneratorConfig {
    /*
     *  自定义GeneratorHelper辅助类，若是为空则使用自定义GeneratorHelper来生成部分属性
     */
    public final static String HELPER_GeneratorHelper = "generatorHelper";
    /**
     * 是否使用lombok插件来生成getter、setter、toString方法，默认false
     */
    public final static String GENERATOR_KEY_USE_LOMBOK = "useLombok";
    /**
     * 是否生成@Id、@Column、@Table注解，默认false
     */
    public final static String GENERATOR_KEY_DB_ANNOTATION = "dbAnnotation";
    /**
     * 生成@Column的注解是否添加length属性，默认false
     */
    public final static String GENERATOR_KEY_DB_COLUMN_LENGTH = "dbColumnLength";
    /**
     * 生成@Column的注解是否添加nullable属性，默认false
     */
    public final static String GENERATOR_KEY_DB_COLUMN_NULLABLE = "dbColumnNullable";
    /**
     * 是否添加hibernate的@DynamicUpdate注解，默认false
     */
    public final static String GENERATOR_KEY_HIBERNAME_ANNOTATION = "hibernateAnnotation";
    /**
     * 是否强制转换short类型Integer类型，默认true
     */
    public final static String TYPE_KEY_FORCE_INTEGER = "forceInteger";
    /**
     * 时间格式是否使用java类型，默认false
     */
    public final static String TYPE_KEY_TIME_JAVA_MODE = "timeJavaMode";
    /**
     * 是否转换number类型为string类型，默认false
     */
    public final static String TYPE_KEY_TRANS_NUMBER_TO_STRING = "transNumberToString";
    /**
     * 是否生成getter方法，默认true
     */
    public final static String PLUGIN_KEY_IS_GETTER = "isGetter";
    /**
     * 是否生成setter方法，默认true
     */
    public final static String PLUGIN_KEY_IS_SETTER = "isSetter";
    /**
     * 是否生成toString方法，默认true
     */
    public final static String PLUGIN_KEY_IS_TOSTRING = "isToString";
}
