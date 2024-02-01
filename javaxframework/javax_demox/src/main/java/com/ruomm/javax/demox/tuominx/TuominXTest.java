package com.ruomm.javax.demox.tuominx;

import com.ruomm.javax.basex.StringConfigHelper;
import com.ruomm.javax.corex.HexUtils;
import com.ruomm.javax.demox.tuominx.dal.TuominDal;
import com.ruomm.javax.demox.tuominx.util.TuominTestHelper;
import com.ruomm.javax.tuominx.TuominFactory;

import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/6 12:58
 */
public class TuominXTest {
    public static void main(String[] args) {
        String[] fieldNames = new String[]{"nameClear", "area"};
        TuominFactory.getCoreWithException().generateFieldConfig(fieldNames, "Clear", "Msk", "Enc", "Sha1", null);
        TuominDal tuominDal = new TuominDal();
        tuominDal.setNameClear("张三");
        tuominDal.setArea("杭州");
        TuominFactory.getCoreWithException().generateFieldConfigByObject(tuominDal, "Clear", "Msk", "Cipher", "Sha", null);
        TuominFactory.getCoreWithException().doTuomin(tuominDal, "Clear", "Msk", "Cipher", "Sha", new TuominTestHelper());
        System.out.println(tuominDal);
        TuominFactory.getCoreWithException().undoTuomin(tuominDal, "Clear", "Msk", "Cipher", "Sha", new TuominTestHelper());
        System.out.println(tuominDal);
        System.out.println("&0x" + HexUtils.encodeString(":"));
        StringConfigHelper stringConfigHelper = new StringConfigHelper(":", ",");
        String data = "01,营业&0x2c执照:02&0x2c&0x3a,事业单位法人证书:03,身份证件:04,其他证明文件供选择";
        Map<String, String> reuslt = stringConfigHelper.parseConfigToMap(data);
        System.out.println(reuslt);
    }
}
