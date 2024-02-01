package com.ruomm.javax.demox.objvalidatorx;

import com.ruomm.javax.objvalidatorx.ObjValidResult;
import com.ruomm.javax.objvalidatorx.ObjValidUtil;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//		String result = CharacterParser.getInstance().getSelling("中华人民共和国 Demo 交 警");
//		System.out.println(result);
//		Random random1 = new Random(1000);
//		for (int i = 0; i < 10; i++) {
//			System.out.print(random1.nextInt() + " ");
//		}
//		System.out.println("");
//		Random random2 = new Random(1000);
//		for (int i = 0; i < 10; i++) {
//			System.out.print(random2.nextInt() + " ");
//		}
//		System.out.println("");
        ValidDal validDal = new ValidDal();
//		validDal.setServiceFee("20001");
        validDal.setDebitUpperLimit("2000");
        validDal.setDebitFeeRate("0.0059");
        ObjValidResult objValidResult = ObjValidUtil.verify(validDal);
        System.out.println(objValidResult.toString());
    }
}
