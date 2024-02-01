package com.ruomm.javax.basex.dal;

import com.ruomm.javax.basex.annotation.DefCopyField;
import com.ruomm.javax.basex.annotation.DefCopyType;
import com.ruomm.javax.basex.annotation.DefEncryptField;

import java.util.HashMap;
import java.util.Map;

@DefCopyType(isCopy = true, defaultCopy = false, isOverWrite = true, isNullOverWrite = true)
public class SrcBaseCopy {
    @DefEncryptField
    private String p_name;
    private String p_id;
    @DefEncryptField
    @DefCopyField(isCopy = true)
    private String p_cardNo;
    @DefEncryptField
    private Integer p_age;
    @DefCopyField(isCopy = true, copyMethod = "nihao", isOverWrite = true, isNullOverWrite = true, dstName = "p_id")
    private String p_sex;
    @DefCopyField(isCopy = true, isOverWrite = true, isNullOverWrite = true)
    private String p_data;

    public Map<String, String> copyMapMethod() {
        Map<String, String> map = new HashMap<>();
//        map.put("p_sex","p_name");
//        map.put("p_cardNo","p_id");
        return map;
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

    public String getP_data() {
        return p_data;
    }

    public void setP_data(String p_data) {
        this.p_data = p_data;
    }

    public String nihao() {
        return p_sex + "POK";
    }

    public String nihao(String data) {
        return p_sex + "POK" + data;
    }

    @Override
    public String toString() {
        return "SrcBaseCopy{" +
                "p_name='" + p_name + '\'' +
                ", p_id='" + p_id + '\'' +
                ", p_cardNo='" + p_cardNo + '\'' +
                ", p_age=" + p_age +
                ", p_sex='" + p_sex + '\'' +
                '}';
    }
}
