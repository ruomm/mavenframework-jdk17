package com.ruomm.javax.demox.tuominx.dal;

import com.ruomm.javax.tuominx.annotation.DefTuominField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/6 12:59
 */
@Getter
@Setter
@ToString
public class TuominDalBase {

    @DefTuominField(fieldClear = "nameClear", fieldMask = "name", methodDecrypt = "decrypt", emptyTuomi = true)
    private String name;
    //    @DefTuominField(fieldClear = "name",fieldMask = "name")
    private String nameClear;
    private String nameCipher;
    private String nameMsk;
    private String nameSha;
    @DefTuominField
    private String area;
    private String areaClear;
    private String areaCipher;
    private String areaMsk;
    private String areaSha;

    private String decrypt(String val) {
        return val + "_zidingyi";
    }
}
