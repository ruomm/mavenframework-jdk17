package com.ruomm.javax.basex;

import com.ruomm.javax.basex.dal.SignObject;
import com.ruomm.javax.basex.dal.SignObjectSub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/5/8 13:57
 */
public class SignHelperTest {
    public static void main(String[] args) {
        SignObject signObject = new SignObject();
        signObject.setAge(10);
        signObject.setName("张三");
        signObject.setListAreas(new ArrayList<>());
        signObject.setListSubs(new ArrayList<>());
        signObject.setMapAreas(new HashMap<>());
        signObject.setMapSubs(new HashMap<>());
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            SignObjectSub signObjectSub = new SignObjectSub();
            String sexSub = random.nextInt(100) < 3 ? (random.nextInt(100) < 50 ? "男" + i : "女" + i) : null;
            String nameSub = random.nextInt(100) < 50 ? NameUtils.generateName(true) : null;
            Date dateNow = random.nextInt(100) < 50 ? new Date() : null;
            signObjectSub.setAgeSub(20 + i);
            signObjectSub.setSexSub(sexSub);
            signObjectSub.setNameSub(nameSub);
            signObjectSub.setDate(dateNow);
            signObject.getListAreas().add("地区" + random.nextInt(10));
            signObject.getMapAreas().put("area" + random.nextInt(10), "地区" + random.nextInt(10));
            signObject.getListSubs().add(signObjectSub);
            signObject.getMapSubs().put("sub" + i, signObjectSub);
            signObject.setSignSub(signObjectSub);
        }
        SignHelper signHelper = new SignHelper();
        String signData = signHelper.getSignStrByObject(signObject);
        System.out.println(signData);
    }
}
