package com.ruomm.javax.basex.dal;

import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/5/12 15:37
 */
public class SignObject {

    private String name;
    private String sex;
    private int age;
    private SignObjectSub signSub;
    private List<String> listAreas;
    private List<SignObjectSub> listSubs;
    private Map<String, String> mapAreas;
    private Map<String, SignObjectSub> mapSubs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SignObjectSub getSignSub() {
        return signSub;
    }

    public void setSignSub(SignObjectSub signSub) {
        this.signSub = signSub;
    }

    public List<String> getListAreas() {
        return listAreas;
    }

    public void setListAreas(List<String> listAreas) {
        this.listAreas = listAreas;
    }

    public List<SignObjectSub> getListSubs() {
        return listSubs;
    }

    public void setListSubs(List<SignObjectSub> listSubs) {
        this.listSubs = listSubs;
    }

    public Map<String, String> getMapAreas() {
        return mapAreas;
    }

    public void setMapAreas(Map<String, String> mapAreas) {
        this.mapAreas = mapAreas;
    }

    public Map<String, SignObjectSub> getMapSubs() {
        return mapSubs;
    }

    public void setMapSubs(Map<String, SignObjectSub> mapSubs) {
        this.mapSubs = mapSubs;
    }

    @Override
    public String toString() {
        return "SignObject{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", signSub=" + signSub +
                ", listAreas=" + listAreas +
                ", listSubs=" + listSubs +
                ", mapAreas=" + mapAreas +
                ", mapSubs=" + mapSubs +
                '}';
    }
}
