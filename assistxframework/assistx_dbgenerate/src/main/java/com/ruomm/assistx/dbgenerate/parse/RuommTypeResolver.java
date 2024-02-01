/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月29日 下午3:47:41
 */
package com.ruomm.assistx.dbgenerate.parse;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;
import java.util.Date;
import java.util.Properties;

public class RuommTypeResolver extends JavaTypeResolverDefaultImpl {
    /**
     * 是否强制转换short类型Integer类型，默认true
     */
    private boolean forceIntger = true;
    /**
     * 是否转换number类型为string类型，默认false
     */
    private boolean transNumberToString = false;
    /**
     * 时间格式是否使用java类型，默认false
     */
    private boolean timeJavaMode = false;

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
//		this.properties.putAll(properties);
//		forceBigDecimals = StringUtility
//				.isTrue(properties
//						.getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));
        this.forceIntger = DbGeneratorUtils.isTureProperties(properties, DbGeneratorConfig.TYPE_KEY_FORCE_INTEGER, true);
        this.timeJavaMode = DbGeneratorUtils.isTureProperties(properties, DbGeneratorConfig.TYPE_KEY_TIME_JAVA_MODE, false);
        this.transNumberToString = DbGeneratorUtils.isTureProperties(properties, DbGeneratorConfig.TYPE_KEY_TRANS_NUMBER_TO_STRING, false);
    }

    @Override
    protected FullyQualifiedJavaType calculateBigDecimalReplacement(IntrospectedColumn column,
                                                                    FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (transNumberToString) {
            answer = new FullyQualifiedJavaType(String.class.getName());
        } else if (column.getScale() > 0 || column.getLength() > 30 || forceBigDecimals) {
            answer = defaultType;
        } else if (column.getLength() > 9) {
            answer = new FullyQualifiedJavaType(Long.class.getName());
        } else if (column.getLength() > 4) {
            answer = new FullyQualifiedJavaType(Integer.class.getName());
        } else {
            if (forceIntger) {
                answer = new FullyQualifiedJavaType(Integer.class.getName());
            } else {
                answer = new FullyQualifiedJavaType(Short.class.getName());
            }
        }
        return answer;
    }

    @Override
    protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column,
                                                         FullyQualifiedJavaType defaultType) {
        int jdbcType = column.getJdbcType();
        if (jdbcType == Types.NUMERIC || jdbcType == Types.DECIMAL) {
            FullyQualifiedJavaType answer = calculateBigDecimalReplacement(column, defaultType);
            return answer;
        } else if (jdbcType == Types.DATE || jdbcType == Types.DATE) {
            if (timeJavaMode) {
                return new FullyQualifiedJavaType(Date.class.getName());
            } else {
                return super.overrideDefaultType(column, defaultType);
            }
        } else {
            return super.overrideDefaultType(column, defaultType);
        }

    }


}
