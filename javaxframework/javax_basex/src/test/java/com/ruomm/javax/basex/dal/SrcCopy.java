package com.ruomm.javax.basex.dal;


import com.ruomm.javax.basex.annotation.DefCopyType;
import com.ruomm.javax.basex.annotation.DefEncryptField;

import java.util.HashMap;
import java.util.Map;

@DefCopyType()
public class SrcCopy extends SrcBaseCopy {
    @DefEncryptField
    private String name;
    private String id;
    @DefEncryptField
    private String cardNo;
    private Integer age;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Map<String, String> copyMapMethod2() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "p_name");
        map.put("cardNo", "p_id");
        return map;
    }

    @Override
    public String toString() {
        return "SrcCopy{" +
                "p_name='" + getP_name() + '\'' +
                ", p_id='" + getP_id() + '\'' +
                ", p_cardNo='" + getP_cardNo() + '\'' +
                ", p_age=" + getP_age() +
                ", p_sex='" + getP_sex() + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
