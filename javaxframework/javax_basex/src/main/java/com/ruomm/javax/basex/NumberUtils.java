package com.ruomm.javax.basex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/11/3 13:24
 */
public class NumberUtils {

    public static List<Integer> randomNumberList(int min, int max, int count) {
        if (count <= 0) {
            return null;
        } else if (count > Math.abs(max - min) + 1) {
            throw new RuntimeException("NumRandomUtils.randomNoRepeatNumber->生成个数超过允许的个数");
        }
        int maxVal = max < min ? min : max;
        int minVal = max < min ? max : min;
        List<Integer> listSource = new ArrayList<>();
        for (int i = minVal; i <= maxVal; i++) {
            listSource.add(i);
        }
        Random random = new Random();
        List<Integer> listDest = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int tmpIndex = random.nextInt(listSource.size());
            Integer tmpVal = listSource.get(tmpIndex);
            listSource.remove(tmpVal);
            listDest.add(tmpVal);
        }
        return listDest;
    }
}
