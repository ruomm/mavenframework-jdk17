package com.ruomm.javax.basex.dal;

import com.ruomm.javax.basex.annotation.DefConfigField;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/8 9:52
 */
public class StrConfigDal {
    @DefConfigField(index = 0)
    private String name;
    @DefConfigField(index = 1)
    private String sex;
    @DefConfigField(index = 2)
    private String age;
    @DefConfigField(index = 2)
    private String area;
    @DefConfigField(index = 3)
    private String city;

    @Override
    public String toString() {
        return "StrConfigDal{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
