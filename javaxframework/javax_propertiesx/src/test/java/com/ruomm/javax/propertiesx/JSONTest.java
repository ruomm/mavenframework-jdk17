package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.FileUtils;
import com.ruomm.javax.jsonx.JSONParseCommon;
import com.ruomm.javax.jsonx.XJSON;
import com.ruomm.javax.loggingx.LoggerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2020/12/18 13:20
 */
public class JSONTest {
    public static void main(String[] args) {
        doJsonRemove();
    }

    public static void doJsonReader() {
        LoggerConfig.configDebugLevel(LoggerConfig.DebugLevel.INFO);
        String jsonPath = "D:\\temp\\temp\\jsonproperty.json";
        PropertyReaderCharsetJson propertyReader = new PropertyReaderCharsetJson(jsonPath, null, true);
        propertyReader.loadProps(true);
        propertyReader.putProperty("root1.root2.value01", "测试值01");
        propertyReader.putProperty("root1.root2.value01.sub01", "测试值sub01");
        Map<String, String> mapString = propertyReader.propertyToMap();
        System.out.println(propertyReader.getProperty("root1.root2.value01.sub01"));

        for (String key : mapString.keySet()) {
            System.out.println(propertyReader.getProperty(key));
            System.out.println(mapString.get(key));
//            System.out.println(key);
            System.out.println(propertyReader.getProperty(key).equals(mapString.get(key)));
        }
        propertyReader.storeProperty(null);


    }

    public static void doJsonRemove() {
        LoggerConfig.configDebugLevel(LoggerConfig.DebugLevel.INFO);
        String jsonPath = "D:\\temp\\temp\\jsonproperty.json";
        PropertyReaderCharsetJson propertyReader = new PropertyReaderCharsetJson(jsonPath, null, false);
        propertyReader.loadProps(true);
//        propertyReader.putProperty("root1.root2.value01","测试值01");
//        propertyReader.putProperty("root1.root2.value01.sub01","测试值sub01");
        Map<String, String> mapString = propertyReader.propertyToMap();
        System.out.println(propertyReader.getProperty("root1.root2.value01.sub01"));
        propertyReader.removeProperty("root1.root2.value01.sub01");
        propertyReader.putProperty("root1.root2", "测试值sub01");
//        propertyReader.storeProperty(null);


    }

    public static void testJson() {
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
        XJSON.setPrimaryParseMode(JSONParseCommon.JSONParseMode.JackSon);
        String jsonString = XJSON.toJSONString(map);
        System.out.println(map);
        System.out.println(jsonString);
        Map<String, Object> mapJson = XJSON.parseToPropertyMap(jsonString);
        String age = (String) mapJson.get("age");
        System.out.println("age" + age);
        System.out.println(mapJson);
        FileUtils.writeFile("D:\\temp\\temp\\jsonproperty.json", XJSON.toJSONString(map), false, null);
    }
}
