package com.ruomm.javax.jsonx.test;

import com.ruomm.javax.jsonx.JSONParseCommon;
import com.ruomm.javax.jsonx.XJSON;
import com.ruomm.javax.loggingx.LoggerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/19 15:45
 */
public class XJSONTest {
    public static void main(String[] args) {
        LoggerConfig.configDebugLevel(LoggerConfig.DebugLevel.INFO);
        String jsonString = testToJSONString();
        parseJsonObject(jsonString);
    }

    public static String testToJSONString() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "姓名");
        map.put("sex", "性别");
        map.put("age", 1);
        Map<String, Object> mapData1 = new HashMap<>();
        mapData1.put("name1", "姓名1");
        mapData1.put("sex1", "性别1");
        Map<String, Object> mapData2 = new HashMap<>();
        mapData2.put("name2", "姓名2");
        mapData2.put("sex2", "性别2");
        mapData1.put("mapData2", mapData2);
        map.put("mapData1", mapData1);
        map.put("mapData2", mapData2);
//        XJSON.setPrimaryParseMode(JSONParseCommon.JSONParseMode.Gson);
        String jsonString = XJSON.toJSONString(map, JSONParseCommon.JSONParseMode.JackSon);
//        String jsonString = XJSON.toJSONString(map);
        System.out.println(map);
        System.out.println(jsonString);
        return jsonString;
//        Map<String,Object> mapJson= XJSON.parseToPropertyMap(jsonString);
//        String age=(String)mapJson.get("age");
//        System.out.println("age"+age);
//        System.out.println(mapJson);

    }

    public static void parseJsonObject(String jsonString) {
        Map jsonObject = XJSON.parseObject(jsonString, Map.class);
        System.out.println(jsonObject);
    }
}
