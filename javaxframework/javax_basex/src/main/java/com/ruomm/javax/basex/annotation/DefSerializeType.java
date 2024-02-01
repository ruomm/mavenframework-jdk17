package com.ruomm.javax.basex.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DefSerializeType {
    public final static String SIGN_KEY = "sign";

    // 是否序列号该类
    public boolean isSerialize() default true;

    // 字段为空时候是否序列化
    public boolean isNullSerialize() default false;

    // 序列化时候不序列化的Tag，若是不为空，则含有Tag的时候不序列化，NULL代表Tag为空，标识不传Tag时候不序列化，多个以英文逗号分隔
    public String noSerializeTag() default "";

    // 定义字段是否默认序列化，false则只有DefSerializeField注解的字段序列化
    public boolean defaultSerialize() default true;

    // 非空序列类时候使用序列方法来获取序列值，返回类型是Map<String,Object>,此时DefSerializeField注解不起作用
    public String serializeMethod() default "";

}
