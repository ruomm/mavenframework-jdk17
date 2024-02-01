/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年11月5日 上午11:40:30
 */
package com.ruomm.demotest.jsonx;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.ruomm.javax.jsonx.XJSON;

import java.util.LinkedHashMap;

public class XJSONLinkedTest {
    public static void main(String[] args) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		HashMap<String, String> map = new HashMap<String, String>();
//		SerializerFeature.WriteNullStringAsEmpty;
//		SerializerFeature.WriteMapNullValue;
        SerializeConfig config = new SerializeConfig();
        map.put("name", "");
        map.put("sex", NameUtils.generateName());
        map.put("age", NameUtils.generateName());
        map.put("area", NameUtils.generateName());
        map.put("city", NameUtils.generateName());
        map.put("province", NameUtils.generateName());
        map.put("part", NameUtils.generateName());
        map.put("dept", NameUtils.generateName());
        System.out.println(XJSON.getWorkJsonName());
        System.out.println(XJSON.toJSONString(map));
    }
}
