package com.ruomm.javax.basex.dal;

import com.ruomm.javax.basex.annotation.DefMapTransObj;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/2 9:59
 */
public class MapObjValue extends SrcBaseCopy {
    private String name;
    private boolean success;
    @DefMapTransObj(transMethod = "parseValDate")
    private Date val;

    private Date parseValDate(String mapVal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(mapVal);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public String toString() {
        return "MapObjValue{" +
                "name='" + name + '\'' +
                ", success=" + success +
                ", val=" + val +
                '}';
    }
}
