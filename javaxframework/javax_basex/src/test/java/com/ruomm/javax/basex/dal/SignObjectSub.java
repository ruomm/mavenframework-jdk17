package com.ruomm.javax.basex.dal;

import com.ruomm.javax.basex.SignHelper;
import com.ruomm.javax.basex.annotation.DefSerializeField;
import com.ruomm.javax.basex.annotation.DefSerializeType;
import com.ruomm.javax.corex.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/5/12 15:37
 */
@DefSerializeType(isNullSerialize = true)
//@DefSignMethod(value = "toSignString")
public class SignObjectSub {

    private String nameSub;
    private String sexSub;
    private int ageSub;
    @DefSerializeField(serializeMethod = "parseDate")
    private Date date;

    public String parseDate() {
        if (null == date) {
            return null;
        } else {
            return TimeUtils.getCurrentTimeString(new SimpleDateFormat("yyyyMMdd-HHmmss"));
        }
    }

    public String getNameSub() {
        return nameSub;
    }

    public void setNameSub(String nameSub) {
        this.nameSub = nameSub;
    }

    public String getSexSub() {
        return sexSub;
    }

    public void setSexSub(String sexSub) {
        this.sexSub = sexSub;
    }

    public int getAgeSub() {
        return ageSub;
    }

    public void setAgeSub(int ageSub) {
        this.ageSub = ageSub;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SignObjectSub{" +
                "nameSub='" + nameSub + '\'' +
                ", sexSub='" + sexSub + '\'' +
                ", ageSub=" + ageSub +
                ", date=" + date +
                '}';
    }

    public String toSignString() {
        return
                new SignHelper("=", ",").getSignStrByObject(this);
    }
}
