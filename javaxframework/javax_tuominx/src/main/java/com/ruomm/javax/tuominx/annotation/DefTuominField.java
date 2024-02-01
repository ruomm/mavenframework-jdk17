/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月23日 上午10:12:01
 */
package com.ruomm.javax.tuominx.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DefTuominField {
    /**
     * NULL或EMPTY是否使用脱敏
     */
    boolean emptyTuomi() default false;

    /**
     * 脱敏标识
     */
    String tag() default "";

    /**
     * 依据方法获取脱敏标识
     */
    String tagMethod() default "";

    /**
     * 值为空、拼接字符串为空：空返回field名称
     * 值不为空、拼接字符串为空：返回该值
     * 值为空、拼接字符串不为空：field名称以拼接字符串结尾返回field名称，否则返回field名称+拼接字符串
     * 值为不空、拼接字符串不为空：该值以拼接字符串结尾返回该值，否则返回该值+拼接字符串
     */
    String fieldClear() default "";

    /**
     * 掩码字段注解
     * 值为空：拼接字符串为空返回NULL，不为空返回未拼接明文字符串+拼接字符串
     * 值不为空：以拼接字符串结尾则或拼接字符串为空返回该值，否则返回返回该值+拼接字符串
     */
    String fieldMask() default "";

    /**
     * 加密字段注解
     * 值为空：拼接字符串为空返回NULL，不为空返回未拼接明文字符串+拼接字符串
     * 值不为空：以拼接字符串结尾则或拼接字符串为空返回该值，否则返回返回该值+拼接字符串
     */
    String fieldEncrypt() default "";

    /**
     * 散列字段注解
     * 值为空：拼接字符串为空返回NULL，不为空返回未拼接明文字符串+拼接字符串
     * 值不为空：以拼接字符串结尾则或拼接字符串为空返回该值，否则返回返回该值+拼接字符串
     */
    String fieldDigest() default "";

    /**
     * 掩码方法，空的话使用TuominHelper中的掩码方法，非空使用声明的类中的掩码方法
     */
    String methodMask() default "";

    /**
     * 加密方法，空的话使用TuominHelper中的加密方法，非空使用声明的类中的加密方法
     */
    String methodEncrypt() default "";

    /**
     * 解密方法，空的话使用TuominHelper中的解密方法，非空使用声明的类中的解密方法
     */
    String methodDecrypt() default "";

    /**
     * 解密方法加掩码，空的话使用TuominHelper中的解密方法，非空使用声明的类中的解密方法
     */
    String methodDecryptWithMask() default "";

    /**
     * 散列方法，空的话使用TuominHelper中的散列方法，非空使用声明的类中的散列方法
     */
    String methodDigest() default "";

    /**
     * 解密方法加掩码时候无需加掩码
     */
    boolean ignoreDecryptWithMask() default false;

    /**
     * 解密方法时候是否加散列值
     */
    boolean decryptWithDigest() default false;

    /**
     * 脱敏辅助类，如是声明有脱敏辅助类，则使用声明的脱敏辅助类，否则使用入参的脱敏辅助类
     */
    String fieldHelper() default "";


}
