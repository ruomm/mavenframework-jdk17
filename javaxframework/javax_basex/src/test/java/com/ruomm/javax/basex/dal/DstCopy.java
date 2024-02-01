package com.ruomm.javax.basex.dal;

public class DstCopy {
    private String name;
    private String id;
    private String cardNo;
    private Integer age;
    private String sex;
    private String p_name;
    private String p_id;
    private String p_cardNo;
    private Integer p_age;
    private String p_sex;

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

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_cardNo() {
        return p_cardNo;
    }

    public void setP_cardNo(String p_cardNo) {
        this.p_cardNo = p_cardNo;
    }

    public Integer getP_age() {
        return p_age;
    }

    public void setP_age(Integer p_age) {
        this.p_age = p_age;
    }

    public String getP_sex() {
        return p_sex;
    }

    public void setP_sex(String p_sex) {
        this.p_sex = p_sex;
    }

    @Override
    public String toString() {
        return "DstCopy{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", p_name='" + p_name + '\'' +
                ", p_id='" + p_id + '\'' +
                ", p_cardNo='" + p_cardNo + '\'' +
                ", p_age=" + p_age +
                ", p_sex='" + p_sex + '\'' +
                '}';
    }
}
