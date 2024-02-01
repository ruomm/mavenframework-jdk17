package com.ruomm.javax.basex;

import com.ruomm.javax.basex.dal.MapObjValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/2 9:57
 */
public class MapParseUtilsTest {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "张三");
        map.put("success", "true");
        map.put("val", "2021-07-06");
        map.put("p_age", "11");
        MapObjValue mapObjValue = new MapObjValue();
        MapParseUtils.mapToObject(mapObjValue, map);
        System.out.println(mapObjValue.toString());
        System.out.println(mapObjValue.getP_age());
    }
}
