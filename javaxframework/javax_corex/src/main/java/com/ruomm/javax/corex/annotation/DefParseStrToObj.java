/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年6月13日 上午9:17:53
 */
package com.ruomm.javax.corex.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DefParseStrToObj {
    public final static String PARSE_MOTHOD = "parseStrToObj";

    public String parse();
}
