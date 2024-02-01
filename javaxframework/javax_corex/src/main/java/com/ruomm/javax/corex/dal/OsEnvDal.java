package com.ruomm.javax.corex.dal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/26 10:53
 */
@Getter
@Setter
@ToString
public class OsEnvDal {


    /**
     * 系统类型，1-windows  2-linux 3-macos 4.android 其他.未知
     */
    private int osType;

    /**
     * 系统名称
     */
    private String osName;
    /**
     * 系统名称简称
     */
    private String osShortName;
}
