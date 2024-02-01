package com.ruomm.javax.basex;

import com.ruomm.javax.basex.dal.StrConfigDal;

import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/8 9:51
 */
public class StringConfigUtilsTest {

    public static void main(String[] args) {
//        Map map=StringConfigUtils.parseConfigToMap("823:45864,dfas:12321,45,54666:1312312:12");
//        System.out.println(map);
//
        StringConfigHelper stringConfigHelper = new StringConfigHelper();
        List<StrConfigDal> list = stringConfigHelper.parseConfigToList("82&0x44;&0x58;30x44&0x58;:45864,dfas:12321,45,54666:1312312:12,123:213:31:123:23", StrConfigDal.class);
        System.out.println(list);
        Map map = stringConfigHelper.parseConfigToMap("823:45864");
        System.out.println(map);

//        List<StrConfigDal> list =StringConfigUtils.parseConfigToList("823:45864,dfas:12321,45,54666:1312312:12,123:213:31:123:23",StrConfigDal.class,"name","sex1","age");
//        System.out.println(list);
    }
}
