package com.ruomm.javax.basex;

import com.ruomm.javax.basex.dal.DstCopy;
import com.ruomm.javax.basex.dal.SrcCopy;

import java.util.HashMap;
import java.util.Map;

public class ObjCopyTest {
    public static void main(String[] args) {
        copyTest();
    }

    public static void copyTest() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "p_name");
        map.put("id", "p_id");
        map.put("p_sex", "sex");
        Map<String, String> mapReName = new HashMap<>();
        mapReName.put("name", "cardNo");
//        mapReName.put("id","p_id");
//        mapReName.put("p_sex","sex");
        SrcCopy srcCopy = generateSrcCopy();
        DstCopy dstCopy = new DstCopy();
        dstCopy.setP_sex("男");
        ObjectCopyUtil.copyObjectRename(srcCopy, dstCopy, mapReName, "中国", true);
        System.out.println(dstCopy);

    }

    public static SrcCopy generateSrcCopy() {
        SrcCopy srcCopy = new SrcCopy();
        srcCopy.setAge(12);
        srcCopy.setId("1000");
        srcCopy.setName("张三");
        srcCopy.setP_sex("男");
        srcCopy.setP_cardNo("4103271989");
        srcCopy.setP_name("李四");
        srcCopy.setP_data("nihao");
        return srcCopy;

    }
}
