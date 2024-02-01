package com.ruomm.javax.demox.tuominx.dal;

import com.ruomm.javax.tuominx.annotation.DefTuominField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/6 12:59
 */
@Getter
@Setter
public class TuominDal extends TuominDalBase {
    @DefTuominField
    private String city;
    private String cityClear;
    private String cityCipher;
    private String cityMsk;
    private String citySha;

    private String decrypt(String val) {
        return val + "_zidingyi";
    }

    @Override
    public String toString() {
        return "TuominDal{" +
                "city='" + city + '\'' +
                ", cityClear='" + cityClear + '\'' +
                ", cityCipher='" + cityCipher + '\'' +
                ", cityMsk='" + cityMsk + '\'' +
                ", citySha='" + citySha + '\'' +
                ", parentObj='" + super.toString() +
                '}';
    }
}
