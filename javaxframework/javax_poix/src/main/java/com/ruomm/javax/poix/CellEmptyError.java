/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月13日 下午6:04:34
 */
package com.ruomm.javax.poix;

public enum CellEmptyError {
    NONE, // 不执行任何操作，添加
    SKIP, // 跳过
    END, // 结束
    THROW // 抛出异常
}
